package com.daniel.protocol.request;

import com.daniel.protocol.Packet;
import lombok.Data;

import static com.daniel.protocol.command.Command.LOGIN_REQUEST;

/**
 * @Package: com.daniel.protocol.request
 * @ClassName: LoginRequestPacket
 * @Author: daniel
 * @CreateTime: 2021/7/12 21:55
 * @Description: 登录请求的数据包
 */

@Data
public class LoginRequestPacket extends Packet {

    private String userName;

    private String password;

    /**
     * 声明自己的数据包的指令为登录请求。
     * @return
     */
    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
