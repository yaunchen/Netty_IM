package com.daniel.serializer;

/**
 * @Package: com.daniel.serializer
 * @ClassName: SerializerAlgorithm
 * @Author: daniel
 * @CreateTime: 2021/7/12 22:05
 * @Description: 序列化算法，虽然只实现了Json的，但做成了接口，可拓展性强
 */
public interface SerializerAlgorithm {

    /**
     * JSON序列化算法
     */
    byte JSON = 1;
}
