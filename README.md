# junx
Junx toolkit是辅助java开发人员开发应用程序，可以灵活构建服务器端应用程序，提供统一kv缓存接口封装，简单易懂的事件总线event bus，简单的生命周期管理接口service，常用工具类封装等。

# junx-core
1、封装通用工具包utils
2、可伸缩的线程池StandardThreadExecutor
3、统一异常封装exception
4、简单的选择器Chooser，目前支持顺序选择、一致性哈希算法选择
5、支持生命周期管理的统一接口及其实现lifecycle
6、轻量级环形缓存ringfiber
7、轻量级进度跟踪器tracker

# junx-cache
Cache是操作KV缓存的统一接口，目前支持Redis、Aerospike、EHCache。支持项目本地cache.properties配置与zookeeper集中配置。

# junx-event
轻量级进程内事件总线EventBus，基于发布->订阅模式实现，可以在进程内进行跨线程通信。目前事件总线实现了基于当前线程发布事件的SimpleEventBus，支持异步发布事件的DisruptorEventBus。每个事件订阅者由一个事件通道EventChannel和一个事件处理类EventChannelHandler组成，事件从EventBus发布出来过后，经由EventChannel，再到具体的事件处理类EventChannelHandler。每个EventChannel实现都提供了不同的功能，目前主要提供：
1、SimpleEventChannel：当前线程执行事件处理
2、ExecutorEventChannel：到指定线程池执行事件处理
3、DisruptorEventChannel：基于Disruptor框架实现的单线程执行事件处理
业务实现EventChannelHandler即可，通过事件通道EventChannel与事件处理EventChannelHandler分离的设计方式，可以将业务执行与线程解耦，可以通过配置注入灵活配置业务执行方式。
