package com.daniel.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

/**
 * @Package: com.daniel.server
 * @ClassName: NettyServer
 * @Author: daniel
 * @CreateTime: 2021/7/11 17:46
 * @Description: Netty的Server端。主要是用于启动一个NettyServer端
 */
public class NettyServer {

    private static final int port = 8080;

    /**
     * server的启动类，配置channel为NIO，配置工作线程，配置每个channel的具体业务逻辑
     * @param args
     */
    public static void main(String[] args) {
        NioEventLoopGroup masterGroup = new NioEventLoopGroup();// 用于监听端口, 获取新连接的线程组，相当于揽活的老板
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();// 用于处理每个线程请求。相当于干活的工人

        ServerBootstrap  serverBootstrap = new ServerBootstrap();// 服务引导类，配置一些基本信息
        serverBootstrap.group(masterGroup, workerGroup)
                       .channel(NioServerSocketChannel.class)             // 网络传输模型为NIO
                        .option(ChannelOption.SO_BACKLOG, 1024)     // 系统用于临时存放已完成三次握手的请求的队列的最大长度
                        .option(ChannelOption.SO_KEEPALIVE, true)   // 开启TCP底层心跳机制
                        .option(ChannelOption.TCP_NODELAY, true)    // 是否开启Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
                        .childHandler(new ChannelInitializer<NioSocketChannel>() {//定义后续每条连接的数据读写，业务处理逻辑
                            protected void initChannel(NioSocketChannel nioSocketChannel) {
                                // TODO: 添加具体的处理逻辑
                            }
                        });
        bindPort(serverBootstrap, port); // 寻找合适的server端口进行绑定
    }

    /**
     *  以port为开始绑定的端口，自增的绑定端口，直到知道了空闲的端口，并绑定
     * @param serverBootstrap
     * @param port 目标端口
     */
    private static void bindPort(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener( future -> {
            if ( future.isSuccess() ) {
                System.out.println(new Date() + ": 成功绑定端口:[" + port + "] !" );
            } else {
                System.err.println(new Date() + ": 绑定端口：[" + port + "] 失败！");
                bindPort(serverBootstrap, port + 1);
            }
        });
    }
}
