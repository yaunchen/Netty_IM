package com.daniel.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 将协议包写成抽象类，后面所有的协议包都需要实现getCommand
 * @Package: com.daniel.protocol
 * @ClassName: Packet
 * @Author: daniel
 * @CreateTime: 2021/7/12 21:50
 * @Description: 将协议包写成抽象类，后面所有的协议包都需要实现getCommand
 */
@Data
public abstract class Packet {

    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;

    /**
     * 获取对应的指令
     * @return 指令的Byte
     */
    @JSONField (serialize = false)
    public abstract Byte getCommand();
}
