package com.daniel.client;

import com.daniel.client.handler.FirstClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import sun.plugin.net.protocol.jar.CachedJarURLConnection;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.daniel.client
 * @ClassName: NettyClient
 * @Author: daniel
 * @CreateTime: 2021/7/11 21:08
 * @Description: NettyClient的配置，和Server一样，需要配置NIO，引导配置bootstrap(server的则是serverBootstrap),
 *              workGroup,initChannel中的处理逻辑。然后调用connect进行链连接，连接是异步的，所以返回一个future，监听这个future可得知是否连接成功
 */
public class NettyClient {

    private static final int MAX_RETRY = 5;            // 最大重试次数

    private static final String host = "127.0.0.1";

    private static final int port = 8080;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)  // 表示连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.SO_KEEPALIVE, true)            // 开启TCP心跳机制
                .option(ChannelOption.TCP_NODELAY, true)             //  表示是否开始 Nagle 算法，true 表示关闭，false 表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就设置为 true 关闭，如果需要减少发送次数减少网络交互，就设置为 false 开启
                .handler(new ChannelInitializer<SocketChannel>() {         // client对于每个channel的处理逻辑
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // TODO:添加处理逻辑
                        socketChannel.pipeline().addLast(new FirstClientHandler()); //  addLast() 方法 添加一个逻辑处理器，这个逻辑处理器为的就是在客户端建立连接成功之后，向服务端写数据
                    }
                });

        connect(bootstrap, host, port, MAX_RETRY); // client开始进行连接
    }

    /**
     * client进行连接。
     * 定时重试的实现：定时任务是调用 bootstrap.config().group().schedule(),
     * 其中 bootstrap.config() 这个方法返回的是 BootstrapConfig，他是对 Bootstrap 配置参数的抽象，
     * 然后 bootstrap.config().group() 返回的就是我们在一开始的时候配置的线程模型 workerGroup，
     * 调 workerGroup 的 schedule 方法即可实现定时任务逻辑
     * @param bootstrap     连接的引导配置
     * @param host          IP
     * @param port          端口
     * @param retry         剩余的重试次数
     */
    private static void connect (Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if ( future.isSuccess() ) {
                System.out.println(new Date() + "：连接" + "ip:[" + host + "]的" +port +"端口成功!");
            } else if ( retry == 0 ) {
                System.err.println(new Date() + "：重试次数已经用完，放弃连接！");
            } else {
                int order = (MAX_RETRY - retry) + 1;    // 第几次重连
                int delay = 1 << order;                 // 本次重连的间隔
                System.err.println(new Date() + ":连接失败， 进行第" + order + "次重连ing");
                bootstrap.config().group().schedule( () -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS); // 以 2 的幂次来建立连接，然后到达一定次数之后就放弃连接， 比如每隔 1 秒、2 秒、4 秒、8 秒
            }
        });
    }



}
