package com.daniel.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @Package: com.daniel.client.handler
 * @ClassName: FirstClientHandler
 * @Author: daniel
 * @CreateTime: 2021/7/11 21:44
 * @Description: 简单的Client处理逻辑。
 *              · 继承自 ChannelInboundHandlerAdapter，然后覆盖了 channelActive()方法
 *              · 客户端连接建立成功之后，调用到 channelActive() 方法。 在这个方法里面，我们编写向服务端写数据的逻辑
 * 写数据的步骤： ①获取一个 netty 对二进制数据的抽象 ByteBuf
 *              ②把字符串的二进制数据填充到 ByteBuf
 *              ③ctx.channel().writeAndFlush() 把数据写到服务端
 *
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * Client对消息最简单的处理逻辑，处理完毕后将数据推送给Server
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始写数据 ！");

        ByteBuf byteBuf = getByteBuf(ctx);          // 获取数据

        ctx.channel().writeAndFlush(byteBuf);       // 写数据，推送到服务端
    }

    /**
     *  将数据封装陈成ByteBuf后返回
     * @param ctx
     * @return
     */
    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = ctx.alloc().buffer();     // 获取二进制抽象 ByteBuf。, ctx.alloc() 获取到一个 ByteBuf 的内存管理器，这个 内存管理器的作用就是分配一个 ByteBu

        byte[] bytes = "Hello, I'm Client !".getBytes(Charset.forName("utf-8"));    // 准备数据，指定字符串的字符集为 utf-8

        byteBuf.writeBytes(bytes);                  // 填充数据到 ByteBuf

        return byteBuf;

    }

    /**
     * client接收到服务端的消息并打印
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }
}
