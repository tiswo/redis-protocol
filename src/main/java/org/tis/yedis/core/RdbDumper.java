package org.tis.yedis.core;

import io.netty.buffer.ByteBuf;

/**
 * 类名称: RdbDumper <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 2020/9/16 下午8:13
 */
public class RdbDumper {


    private void readHead(ByteBuf buf){
        byte[] bytes = new byte[5];
        if (buf.nioBuffer().remaining() <5){
            System.out.println(buf.nioBuffer().remaining());
        }
        buf.readBytes(bytes);
        System.out.println(new String(bytes));
    }

    public void dump(ByteBuf buf){
        readHead(buf);
    }

}
