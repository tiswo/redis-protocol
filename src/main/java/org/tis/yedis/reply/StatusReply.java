/**
 * Copyright: Copyright (c)2011
 * Organization: Tis
 */
package org.tis.yedis.reply;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * 类名称: StatusReply <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 18/3/25 上午12:13
 */
public class StatusReply implements Reply<String>{

    private static final char MARKER = '+';

    public static final StatusReply OK = new StatusReply("OK");

    public static final StatusReply QUIT = new StatusReply("OK");

    public static final StatusReply PONG = new StatusReply("PONG");

    private final String data;


    public StatusReply(String data) {
        this.data = data;
    }

    @Override
    public String data() {
        return this.data;
    }

    @Override
    public void write(ByteBuf out) throws IOException {
        out.writeByte(MARKER);
        out.writeBytes(String.valueOf(data).getBytes());
        out.writeBytes(CRLF);
    }

    @Override
    public String toString() {
        return "StatusReply{" +
                "data=" + data +
                '}';
    }
}
