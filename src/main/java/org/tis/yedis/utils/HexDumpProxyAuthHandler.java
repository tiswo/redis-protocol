package org.tis.yedis.utils;/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import org.tis.yedis.reply.StatusReply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HexDumpProxyAuthHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        ctx.read();
        ctx.fireChannelActive();
        System.out.println("HexDumpProxyAuthHandler channelActive");
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        System.out.println("HexDumpProxyAuthHandler channelRead");
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[4];
        byteBuf.getBytes(8, bytes);
        if (new String(bytes).equals("AUTH")) {
//            byteBuf.clear();
//            byteBuf.writeByte('+');
//            byteBuf.writeBytes(String.valueOf("OK").getBytes());
//            byteBuf.writeBytes(new byte[]{'\r','\n'});
            ctx.write(StatusReply.OK);
            ctx.flush();
//            ctx.fireChannelReadComplete();
            ctx.read();
        }else {
            ctx.fireChannelRead(msg);
        }
//        ctx.writeAndFlush(msg);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("HexDumpProxyAuthHandler channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }
}
