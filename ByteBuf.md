## ByteBuf

![bytebuf的结构](https://tvax2.sinaimg.cn/large/0085EwgIgy1gsddhrsq4mj30yg0ezgs8.jpg)

```
①ByteBuf 是一个字节容器，容器里面的的数据分为三个部分，第一个部分是已经丢弃的字节，这部分数据是无效的；第二部分是可读字节，这部分数据是 ByteBuf 的主体数据， 从 ByteBuf 里面读取的数据都来自这一部分;最后一部分的数据是可写字节，所有写到 ByteBuf 的数据都会写到这一段。最后一部分虚线表示的是该 ByteBuf 最多还能扩容多少容量

②以上三段内容是被两个指针给划分出来的，从左到右，依次是读指针（readerIndex）、写指针（writerIndex），然后还有一个变量 capacity，表示 ByteBuf 底层内存的总容量

 ③从 ByteBuf 中每读取一个字节，readerIndex 自增1，ByteBuf 里面总共有 writerIndex-readerIndex 个字节可读, 由此可以推论出当 readerIndex 与 writerIndex 相等的时候，ByteBuf 不可读

 ④写数据是从 writerIndex 指向的部分开始写，每写一个字节，writerIndex 自增1，直到增到 capacity，这个时候，表示 ByteBuf 已经不可写了

 ⑤ByteBuf 里面其实还有一个参数 maxCapacity，当向 ByteBuf 写数据的时候，如果容量不足，那么这个时候可以进行扩容，直到 capacity 扩容到 maxCapacity，超过 maxCapacity 就会报错
```

### API

#### 容量API   

`capacity()`:	ByteBuf 底层占用了多少字节的内存（包括丢弃的字节、可读字节、可写字节）

`maxCapacity()`:	ByteBuf 底层最大能够占用多少字节的内存

`readableBytes()`: 	ByteBuf 当前可读的字节数，它的值等于 writerIndex-readerIndex

`isReadable()`:	writerIndex-readerIndex，如果两者相等，则不可读,返回 false

`writableBytes()`:	表示 ByteBuf 当前可写的字节数，它的值等于 capacity-writerIndex

`isWritable()`:	capacity-writerIndex，如果两者相等，则表示不可写，isWritable() 返回 false

` maxWritableBytes()` :	表示可写的最大字节数，它的值等于 maxCapacity-writerIndex

#### 读写指针API

`readerIndex()`:	返回当前的读指针 readerIndex

`readerIndex(int)`: 	设置读指针

`writeIndex()`： 返回当前的写指针 writerIndex

 `writeIndex(int) `： 设置写指针

`markReaderIndex()` ： 把当前的读指针保存起来

`resetReaderIndex()`：把当前的读指针恢复到之前保存的值

#### 读写API

`writeBytes(byte[] src)`： 把字节数组 src 里面的数据全部写到 ByteBuf

`buffer.readBytes(byte[] dst)`：把 ByteBuf 里面的数据全部读取到 dst，这里 dst 字节数组的大小通常等于 readableBytes()，而 src 字节数组大小的长度通常小于等于 writableBytes()

`writeByte(byte b)`:	往 ByteBuf 中写一个字节

`buffer.readByte()`:	从 ByteBuf 中读取一个字节

`release()`和`retain()`:  Netty 使用了堆外内存，而堆外内存是不被 jvm 直接管理的，也就是说申请到的内存无法被垃圾回收器直接回收，所以需要我们手动回收。Netty 的 ByteBuf 是通过引用计数的方式管理的，如果一个 ByteBuf 没有地方被引用到，需要回收底层内存。默认情况下，当创建完一个 ByteBuf，它的引用为1，然后每次调用 retain() 方法， 它的引用就加一， release() 方法原理是将引用计数减一，减完之后如果发现引用计数为0，则直接回收 ByteBuf 底层的内存。

`slice()`:	从原始 ByteBuf 中截取一段，这段数据是从 readerIndex 到 writeIndex，同时，返回的新的 ByteBuf 的最大容量 maxCapacity 为原始 ByteBuf 的 readableBytes()

`duplicate()`: 	把整个 ByteBuf 都截取出来，包括所有的数据，指针信息

>1. slice() 方法与 duplicate() 方法的相同点是：底层内存以及引用计数与原始的 ByteBuf 共享，也就是说经过 slice() 或者 duplicate() 返回的 ByteBuf 调用 write 系列方法都会影响到 原始的 ByteBuf，但是它们都维持着与原始 ByteBuf 相同的内存引用计数和不同的读写指针
>2. slice() 方法与 duplicate() 不同点就是：slice() 只截取从 readerIndex 到 writerIndex 之间的数据，它返回的 ByteBuf 的最大容量被限制到 原始 ByteBuf 的 readableBytes(), 而 duplicate() 是把整个 ByteBuf 都与原始的 ByteBuf 共享
>3. slice() 方法与 duplicate() 方法不会拷贝数据，它们只是通过改变读写指针来改变读写的行为，而最后一个方法 copy() 会直接从原始的 ByteBuf 中拷贝所有的信息，包括读写指针以及底层对应的数据，因此，往 copy() 返回的 ByteBuf 中写数据不会影响到原始的 ByteBuf
>4. slice() 和 duplicate() 不会改变 ByteBuf 的引用计数，所以原始的 ByteBuf 调用 release() 之后发现引用计数为零，就开始释放内存，调用这两个方法返回的 ByteBuf 也会被释放，这个时候如果再对它们进行读写，就会报错。因此，我们可以通过调用一次 retain() 方法 来增加引用，表示它们对应的底层的内存多了一次引用，引用计数为2，在释放内存的时候，需要调用两次 release() 方法，将引用计数降到零，才会释放内存