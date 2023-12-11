/**
 * Copyright: Copyright (c)2011
 * Organization: Tis
 */
package org.tis.yedis.reply;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * 类名称: ErrorReply <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 上午12:16
 */
public class ErrorReply implements Reply{

    public static final ErrorReply NYI_REPLY = new ErrorReply("Not yet implemented");

    private static final char MARKER = '-';

    private final String data;

    public ErrorReply(String data) {
        this.data = data;
    }

    @Override
    public Object data() {
        return data;
    }

    @Override
    public void write(ByteBuf os) throws IOException {
        os.writeByte(MARKER);
        os.writeBytes(data.getBytes());
        os.writeBytes(CRLF);
    }

    @Override
    public String toString() {
        return "ErrorReply{" +
                "data='" + data + '\'' +
                '}';
    }
}
