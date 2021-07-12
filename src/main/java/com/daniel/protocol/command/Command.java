package com.daniel.protocol.command;

/**
 * @Package: com.daniel.protocol.command
 * @ClassName: Commnd
 * @Author: daniel
 * @CreateTime: 2021/7/12 21:53
 * @Description: 指令的类型，后面会嵌入到实际的数据包中。
 */
public interface Command {

    Byte LOGIN_REQUEST = 1;         // 登录请求指令


}
