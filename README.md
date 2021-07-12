# Netty_IM
基于Netty的即时通讯系统

## 知识点目录
[ByteBuf简述](https://github.com/DanielCorleone2001/Netty_IM/blob/main/ByteBuf.md)
## Commit历史变更
### NettyServer启动类

![架构](https://tva3.sinaimg.cn/large/0085EwgIgy1gsdb0j4m3oj30l70dbwf3.jpg)

### NettyClient启动类
![架构](https://tvax1.sinaimg.cn/large/0085EwgIgy1gsdc4ovk9oj30iu0bsdg9.jpg)

### Client与Server的双向简单通信
![架构](https://tva4.sinaimg.cn/large/0085EwgIgy1gsddc4z6naj30qe0pomzo.jpg)

Server:

![server收到消息](https://tva1.sinaimg.cn/large/0085EwgIgy1gsddd6n52cj30fs03ujrk.jpg)

Client:

![client收到消息](https://tva2.sinaimg.cn/large/0085EwgIgy1gsdddsqj5ej30hf04gmxe.jpg)

### Client和Server的通信协议
>定义了通信协议, 以及编码器，解码器。

协议：
> ![image](https://tvax1.sinaimg.cn/large/0085EwgIgy1gsejzdr8v2j30yg053q4i.jpg)

传输消息架构：
>![image](https://tvax4.sinaimg.cn/large/0085EwgIgy1gsek64rfxzj30io0h80tp.jpg)

编码与解码不过多赘述。
>解码器需要获取指令和序列化方式的处理逻辑，所以用两个HashMap存了"指令-具体的实现逻辑类"和"序列化算法-序列化实现类"的键值对。

### 客户端登录
![image](https://tva3.sinaimg.cn/large/0085EwgIgy1gsellnnw07j30n80rp405.jpg)
server:
>![image](https://tvax2.sinaimg.cn/large/0085EwgIgy1gselm0i7q7j30bu040t8t.jpg)

client：
>![image](https://tvax2.sinaimg.cn/large/0085EwgIgy1gselm8pctuj30ef02bq2y.jpg)
