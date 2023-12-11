package org.tis.yedis.reply;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 类名称: BulkReply <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 上午12:19
 */
public class BulkReply implements Reply<byte[]> {

    private static final char MARKER = '$';

    private final byte[] data;

    private final int len;

    public BulkReply() {
        this.data = null;
        this.len = -1;
    }

    public BulkReply(byte[] data) {
        this.data = data;
        this.len = data.length;
    }

    @Override
    public byte[] data() {
        return this.data;
    }

    @Override
    public void write(ByteBuf out) throws IOException {
        // 1.Write header
        out.writeByte(MARKER);
        out.writeBytes(String.valueOf(len).getBytes());
        out.writeBytes(CRLF);

        // 2.Write data
        if (len > 0) {
            out.writeBytes(data);
            out.writeBytes(CRLF);
        }
    }

    public String asString(Charset charset) {
        if (data == null) return null;
        return new String(data, charset);
    }

    @Override
    public String toString() {
        return "BulkReply{" +
                "bytes=" + Arrays.toString(data) +
                '}';
    }
}