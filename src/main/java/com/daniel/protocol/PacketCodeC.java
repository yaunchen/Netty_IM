package com.daniel.protocol;

import com.daniel.protocol.request.LoginRequestPacket;
import com.daniel.serializer.Serializer;
import com.daniel.serializer.impl.JSONSerializerImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import jdk.nashorn.internal.codegen.TypeMap;

import java.util.HashMap;
import java.util.Map;

import static com.daniel.protocol.command.Command.LOGIN_REQUEST;

/**
 * @Package: com.daniel.protocol
 * @ClassName: PacketCodeC
 * @Author: daniel
 * @CreateTime: 2021/7/12 22:16
 * @Description: 数据包的编码与解码器
 */
public class PacketCodeC {

    public final static PacketCodeC INSTANCE = new PacketCodeC();

    /**
     * 存放指令对应的处理逻辑类
     * 也就是比如LOGIN_REQUEST，那就映射到LOGIN_REQUEST实现逻辑中，从而能将数据包进行解析
     */
    private final  Map<Byte, Class<? extends  Packet>> commandTypeMap;

    /**
     * 取指令的处理逻辑类
     * @param command   需要处理的指令
     * @return          具体的逻辑实现类
     */
    private Class<? extends Packet> getCommandType(Byte command) {
        return commandTypeMap.get(command);
    }

    /**
     * 存放序列化算法对应的序列化实现逻辑。
     * 也就是比如JSON算法，那就映射到JSON的序列化和反序列化的实现逻辑中，从而能将数据包进行解析
     */
    private final  Map<Byte, Serializer> serializerMap;

    /**
     * 通过序列化算法，获取到对应的处理逻辑(序列化与反序列化)
     * @param serializerAlgorithm   序列化算法
     * @return                      对应的处理逻辑
     */
    private Serializer getSerializer(Byte serializerAlgorithm) {
        return serializerMap.get(serializerAlgorithm);
    }
    /**
     * 构造方法
     */
    public PacketCodeC() {
        commandTypeMap = new HashMap<>();
        commandTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);

        //----------我----是------无----情-------的-----分-----割---------线---------------------------------------//
        Serializer serializer = new JSONSerializerImpl();
        serializerMap = new HashMap<>();

        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }
    /**
     * 数据包的魔数，声明为我们认可的协议包。
     */
    private static final int MAGIC_NUMBER = 0x98765432;

    /**
     * 编码器，使用ByteBuf作为传输载体，附上魔数，序列化后的数据，版本，算法，数据长度。
     * @param packet 需要传输的数据包
     * @return      编码后的ByteBuf
     */
    public ByteBuf encode(Packet packet) {
        /**
         * 调用 Netty 的 ByteBuf 分配器来创建，ioBuffer() 方法会返回适配 io 读写相关的内存，
         * 它会尽可能创建一个直接内存，直接内存可以理解为不受 jvm 堆管理的内存空间，写到 IO 缓冲区的效果更高。
         */
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();  //  创建ByteBuf对象
        byte[] bytes = Serializer.DEFAULT_SERIALIZATION.serializer(packet); // 使用JSON序列化

        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT_SERIALIZATION.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     *  解码器，从ByteBuf中获取序列化方式，请求的类型。
     *  然后从Map中查找出对应的处理类，调用反序列化将对象还原
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf) {
        byteBuf.skipBytes(4);       // 跳过魔数
        byteBuf.skipBytes(1);       // 跳过版本号

        byte serializerAlgorithm = byteBuf.readByte();      // 获取序列化的方式

        byte command = byteBuf.readByte();                  // 获取指令的类型

        int len = byteBuf.readInt();                        // 获取数据包的长度

        byte[] bytes = new byte[len];

        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getCommandType(command);  // 获取指令的类型

        Serializer serializer = getSerializer(serializerAlgorithm);     // 获取序列化方式

        if ( requestType != null && serializer != null ) {
            return serializer.deserializer(requestType, bytes);         // 根据序列化方式与请求方式, 将其进行解码
        }

        return null;
    }


}
