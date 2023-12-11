package org.tis.yedis;

import org.tis.yedis.core.LevelDBRedisServer;
import org.tis.yedis.core.RedisServer;
import org.tis.yedis.netty.RedisCommandDecoder;
import org.tis.yedis.netty.RedisCommandHandler;
import org.tis.yedis.netty.RedisReplyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisApplication implements CommandLineRunner {

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(RedisApplication.class);
//        app.setWebEnvironment(false);
        app.run(args);
    }


    @Override
    public void run(String... strings) throws Exception {
        int port = 6399;
        EventLoopGroup group = new NioEventLoopGroup();
        RedisServer redisServer = new LevelDBRedisServer();
        try {
            ServerBootstrap b = new ServerBootstrap()
                    .group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
//                    .childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new RedisCommandDecoder())
                                    .addLast(new RedisReplyEncoder())
                                    .addLast(new RedisCommandHandler(redisServer));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128);
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
//            while (true) {
//
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shutdown the EventLoopGroup, which releases all resources.
            group.shutdownGracefully();
        }
    }
}