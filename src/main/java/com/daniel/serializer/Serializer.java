package com.daniel.serializer;

import com.daniel.serializer.impl.JSONSerializerImpl;

/**
 * @Package: com.daniel.serializer
 * @ClassName: Serializer
 * @Author: daniel
 * @CreateTime: 2021/7/12 21:58
 * @Description: 序列化的顶层interface.
 *              如果想要实现其他序列化算法的话，只需要继承一下 Serializer，
 *              然后定义一下序列化算法的标识，再覆盖一下两个方法即可
 */
public interface Serializer {

    /**
     * 默认的序列化方式为JSON
     */
    Serializer DEFAULT_SERIALIZATION = new JSONSerializerImpl();

    /**
     * 获取所实现的序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * 通过序列化算法，将对象序列化成二进制数组
     * @param object
     * @return
     */
    byte[] serializer(Object object);

    /**
     * 反序列化，还原成对应的对象
     * @param clazz 需要还原成的对象类型
     * @param bytes 需要还原的二进制数组
     * @param <T>
     * @return      还原后的对象
     */
    <T> T deserializer(Class<T> clazz, byte[] bytes);
}
