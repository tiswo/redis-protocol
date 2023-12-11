package org.tis.yedis.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.bytes.ByteArrayDecoder;

import java.io.IOException;
import java.util.List;

/**
 * 类名称: RedisCommandDecoder <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 下午6:23
 */
public class AuthCommandDecoder extends ByteArrayDecoder {

    public static final byte CR = '\r';
    public static final byte LF = '\n';

    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';

    public static final byte EMPTY_BYTE = ' ';

    public static final char ZERO = '0';

    private byte[][] bytes;
    private int arguments = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf,
                          List<Object> out) throws Exception {
//        byte[] bytes = new byte[4];
//        buf.getBytes(8,bytes);
//        if(new String(bytes).equals("AUTH")){
//            out.add(false);
//        }else {
//            out.add(buf);
//        }
        System.out.println();
//        out.add(false);
//        if (bytes != null) {
//            int numArgs = bytes.length;
//            int index = 0;
//            for (int i = arguments; i < numArgs; i++) {
//                if (in.getByte(index) == DOLLAR_BYTE) {
//                    int l = readInt(in);
//                    if (l > Integer.MAX_VALUE) {
//                        throw new IllegalArgumentException(
//                                "Java only supports arrays up to "
//                                        + Integer.MAX_VALUE + " in size");
//                    }
//                    int size = (int) l;
//                    bytes[i] = new byte[size];
//                    in.getBytes(index,bytes[i]);
//                    if (in.bytesBefore((byte) CR) != 0) {
//                        throw new RuntimeException("Argument doesn't end in CRLF");
//                    }
//                    // Skip CRLF(\r\n)
//                    in.skipBytes(2);
//                    arguments++;
//                    checkpoint();
//                } else {
//                    throw new IOException("Unexpected character");
//                }
//            }
//            try {
//                out.add(new Command(bytes));
//            } finally {
//                bytes = null;
//                arguments = 0;
//            }
//        } else if (in.getByte(0) == ASTERISK_BYTE) {
//            int l = readInt(in);
//            if (l > Integer.MAX_VALUE) {
//                throw new IllegalArgumentException(
//                        "Java only supports arrays up to " + Integer.MAX_VALUE
//                                + " in size");
//            }
//            int numArgs = (int) l;
//            if (numArgs < 0) {
//                throw new RuntimeException("Invalid size: " + numArgs);
//            }
//            bytes = new byte[numArgs][];
//            checkpoint();
//            decode(ctx, in, out);
//        }

//        int offset = 0;
//        int length = in.bytesBefore(LF);
//        for (int i = 0; i < length; i++) {
//            if (in.getByte(i) == EMPTY_BYTE || in.getByte(i) == CR) {
//                if (offset > 0) {
//                    ByteBuf byteBuf = in.readBytes(offset);
////                    System.out.println(new String(.array()));
//                    offset = 0;
//                }
//                in.skipBytes(1);
//            } else {
//                offset++;
//            }
//        }
//        if (in.getByte(0) == 'A') {
//            byte[] command = new byte[4];
//            in.getBytes(0, command);
//            System.out.println(new String(command));
//            out.add(true);
//        }else{
//            out.add(false);
//        }
    }

    private static int readInt(ByteBuf in) {
        int integer = 0;
        char c;
        while ((c = (char) in.getByte(0)) != CR) {
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

}