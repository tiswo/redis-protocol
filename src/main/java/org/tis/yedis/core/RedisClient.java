package org.tis.yedis.core;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.redis.RedisArrayAggregator;
import io.netty.handler.codec.redis.RedisBulkStringAggregator;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 类名称: RedisClient <br>
 * 类描述: <br>
 *
 * @author tis
 * @version 1.0.0
 * @since 2020/9/16 下午7:30
 */
public class RedisClient {

    private Channel channel;

    private RedisDB db;

    private int argc;

    private Object[] argv;

    public void write(){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new RedisExtDecoder());
                            p.addLast(new RedisDecoder());
                            p.addLast(new RedisBulkStringAggregator());
                            p.addLast(new RedisArrayAggregator());
                            p.addLast(new RedisEncoder());
                            p.addLast(new RedisClientHandler());
                        }
                    });

            // Start the connection attempt.
            Channel ch = null;
            try {
                ch = b.connect("localhost", 6379).sync().channel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Read commands from the stdin.
//            System.out.println("Enter Redis commands (quit to end)");
            ChannelFuture lastWriteFuture = ch.writeAndFlush("REPLCONF listening-port 6399");
            lastWriteFuture.addListener(new GenericFutureListener<ChannelFuture>() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        System.err.print("write failed: ");
                        future.cause().printStackTrace(System.err);
                    }
//                    channel.read();
                }
            });

            // Wait until all messages are flushed before closing the channel.
            lastWriteFuture.sync();
            ch.writeAndFlush("SYNC");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
