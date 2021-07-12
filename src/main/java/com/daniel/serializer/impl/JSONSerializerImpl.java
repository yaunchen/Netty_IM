package com.daniel.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.daniel.serializer.Serializer;
import com.daniel.serializer.SerializerAlgorithm;

/**
 * @Package: com.daniel.serializer.impl
 * @ClassName: JSONSerializerImpl
 * @Author: daniel
 * @CreateTime: 2021/7/12 22:09
 * @Description:
 */
public class JSONSerializerImpl implements Serializer {

    /**
     * 序列化方式为JSON
     * @return
     */
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    /**
     * 调用fastJSON接口直接序列化
     * @param object 需要序列化的对象
     * @return
     */
    @Override
    public byte[] serializer(Object object) {
        return JSON.toJSONBytes(object);
    }

    /**
     * 调用fastJSON接口直接反序列化
     * @param clazz 需要还原成的对象类型
     * @param bytes 需要还原的二进制数组
     * @param <T>
     * @return
     */
    @Override
    public <T> T deserializer(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
