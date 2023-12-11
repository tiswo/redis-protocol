package org.tis.yedis.netty;

import org.tis.yedis.command.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名称: RedisCommandDecoder <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 下午6:23
 */
public class RedisCommandDecoder extends ReplayingDecoder<Void> {

    public static final byte CR = '\r';
    public static final byte LF = '\n';
    public static final byte HT = '\t';


    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';

    public static final byte SPACE = ' ';

    public static final char ZERO = '0';

    public static final byte[] WHITE_SPACE = new byte[]{SPACE, CR, HT };

    private byte[][] bytes;
    private int arguments = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {
        if (bytes != null) {
            int numArgs = bytes.length;
            for (int i = arguments; i < numArgs; i++) {
                if (in.readByte() == DOLLAR_BYTE) {
                    int l = readInt(in);
                    if (l > Integer.MAX_VALUE) {
                        throw new IllegalArgumentException(
                                "Java only supports arrays up to "
                                        + Integer.MAX_VALUE + " in size");
                    }
                    int size = (int) l;
                    bytes[i] = new byte[size];
                    in.readBytes(bytes[i]);
                    if (in.bytesBefore((byte) CR) != 0) {
                        throw new RuntimeException("Argument doesn't end in CRLF");
                    }
                    // Skip CRLF(\r\n)
                    in.skipBytes(2);
                    arguments++;
                    checkpoint();
                } else {
                    throw new IOException("Unexpected character");
                }
            }
            try {
                out.add(new Command(bytes));
            } finally {
                bytes = null;
                arguments = 0;
            }
        } else if (in.readByte() == ASTERISK_BYTE) {
            int l = readInt(in);
            if (l > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(
                        "Java only supports arrays up to " + Integer.MAX_VALUE
                                + " in size");
            }
            int numArgs = (int) l;
            if (numArgs < 0) {
                throw new RuntimeException("Invalid size: " + numArgs);
            }
            bytes = new byte[numArgs][];
            checkpoint();
            decode(ctx, in, out);
        } else {
            //兼容telnet模式,以空格分割命令和参数, 内联命令
            in.readerIndex(in.readerIndex() - 1);
            List<byte[]> cmdArgs = new ArrayList<>();
            int offset = 0;
            int length = in.bytesBefore(LF);
            for (int i = 0; i < length; i++) {
                if (in.getByte(i) == SPACE || in.getByte(i) == CR || in.getByte(i) == HT) {
                    if (offset > 0) {
                        cmdArgs.add(in.readBytes(offset).array());
                        offset = 0;
                    }
                    in.skipBytes(1);
                } else {
                    offset++;
                }
            }
            in.skipBytes(1);//Skip LF
            out.add(new Command(cmdArgs.toArray(), true));
        }
    }

    private static int readInt(ByteBuf in) {
        int integer = 0;
        char c;
        while ((c = (char) in.readByte()) != CR) {
            integer = (integer * 10) + (c - '0');
        }

        if (in.readByte() != LF) {
            throw new IllegalStateException("Invalid number");
        }
        return integer;
    }

    public static long readLong(ByteBuf is) throws IOException {
        long size = 0;
        int sign = 1;
        int read = is.readByte();
        if (read == '-') {
            read = is.readByte();
            sign = -1;
        }
        do {
            if (read == CR) {
                if (is.readByte() == LF) {
                    break;
                }
            }
            int value = read - ZERO;
            if (value >= 0 && value < 10) {
                size *= 10;
                size += value;
            } else {
                throw new IOException("Invalid character in integer");
            }
            read = is.readByte();
        } while (true);
        return size * sign;
    }

    @Override
    protected void checkpoint() {
        super.checkpoint();
    }
}