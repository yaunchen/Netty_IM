package com.daniel.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @Package: com.daniel.server.handler
 * @ClassName: FirstServerHandler
 * @Author: daniel
 * @CreateTime: 2021/7/11 22:00
 * @Description:
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *  当读取到的消息转成String然后输出。简单的强转成ByteBuf即可
     * @param ctx
     * @param msg           读到的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));

        System.out.println(new Date() + ": 服务端写出数据");
        ByteBuf out = getByteBuf(ctx);
        ctx.channel().writeAndFlush(out);
    }

    /**
     * server将消息转成Bytebuf然后写回给client
     * @param ctx
     * @return
     */
    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        byte[] bytes = "Hello, I'm NettyServer ~ ".getBytes(Charset.forName("utf-8"));

        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }
}
