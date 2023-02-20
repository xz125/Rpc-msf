package com.msf.server.transport;

import com.msf.core.codec.RpcDecoder;
import com.msf.core.codec.RpcEncoder;
import com.msf.server.handler.RpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 * @author : msf
 * @date : 2023/2/19
 */

@Slf4j
public class NettyRpcServer implements RpcServer {


    @Override
    public void start(int port) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            // 本地端口号
            String serverAddress = InetAddress.getLocalHost().getHostAddress();
            // netty 服务端启动器
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置工作组，boss负责accept事件，worker负责其他事件。
            bootstrap.group(boss, worker)
                    // 选择服务端的ServerSocketChannel实现
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    // 1. 协议解码
                                    // 3. 协议编码
                                    .addLast(new RpcEncoder<>())
                                    .addLast(new RpcDecoder())
                                    // 2. 请求处理器
                                    .addLast(new RpcRequestHandler())
                                    ;
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(serverAddress, port).sync();
            log.info("server addr {} started on port {}", serverAddress, port);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
