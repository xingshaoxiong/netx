# netx
一个基于Reactor模式的多线程网络库，部分结构参考了netty的实现
基本思路技术one loop one thread，主要执行类为EventLoop
实现了网络事件分发和handler的链式处理，具有较强的可定制性和扩展性。
例如可以很容易定制自己的解码器，作为例子，实现了基于分隔符的解码器，用来解决TCP粘包问题。
同时实现了定制化的内存池，可以对堆外内存进行分配和管理。
很多调试时的亢余代码，暂时没有去掉，同时接口设计也不够好，但程序的测试基本没有问题，很多类似的小问题准备后续再优化。

## update
同时将本来的单线程的线程池改为了真正的单个线程，有些问题需要进一步处理，因为单个线程比线程池难管理，因为很多细节实现的没那么好
初步实现了异步执行，类似Promise，比如可以异步关闭，添加listener