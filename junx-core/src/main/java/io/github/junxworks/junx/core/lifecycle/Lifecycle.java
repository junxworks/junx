/*
 ***************************************************************************************
 * 
 * @Title:  Lifecycle.java   
 * @Package io.github.junxworks.junx.core.lifecycle   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:36   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.lifecycle;

/**
 *生命周期的接口定义类，本类中定义了一个生命周期的完整状态及其行为。<br>
 *<p>目前定义的生命周期状态包含了<b>STARTING、RUNNING、STOPPING、STOPPED、SUSPENDING、SUSPENDED</b>。
 *目前定义的操作接口<b>OPERATION_START、OPERATION_SUSPENDED、OPERATION_RESUME、OPERATION_STOP、OPERATION_REPORT</b>。
 *操作中文名称分别对应启动、暂停、恢复、停止、打印当前状态，其中启动、暂停、恢复、停止这几个操作会对生命周期对象的当前状态产生
 *影响。</p>
 *<p>每个操作对状态的影响如下图所示：</p> 
 *{@code 
 *                        ----------      STOP            
 *      ------------------|stopping|<--------------
 *      |                 ----------              |
 *      ↓                                         |
 *  ---------    START    ----------          ---------
 *  |stopped| ----------> |starting|--------->|running| <----------------                
 *  ---------             ----------          ---------                 |
 *                                                |                     |
 *  							         SUSPENDED|                     |RESUME
 *                                                ↓                     |
 *                                           ------------          -----------
 *                                           |suspending|--------->|suspended|
 *                                           ------------          -----------               
 * }
 *                       
 * @author: Michael
 * @date:   2017-5-7 15:33:45
 * @since:  v1.0
 */
public interface Lifecycle {
	
	/** 常量 正在启动. */
	static final int STARTING = 0;

	/** 常量 正在运行. */
	static final int RUNNING = 1;

	/** 常量 正在停止. */
	static final int STOPPING = 2;

	/** 常量 已经停止. */
	static final int STOPPED = 3;

	/** 常量 正在暂停. */
	static final int SUSPENDING = 4;

	/** 常量 已经暂停. */
	static final int SUSPENDED = 5;

	/** 常量 操作名称，“启动”. */
	public static final String OPERATION_START = "start";

	/** 常量 操作名称，“停止”. */
	public static final String OPERATION_SUSPENDED = "suspended";

	/** 常量 操作名称，“恢复”. */
	public static final String OPERATION_RESUME = "resume";

	/** 常量 操作名称，“停止”. */
	public static final String OPERATION_STOP = "stop";

	/** 常量 操作名称，“打印对象当前状态”. */
	public static final String OPERATION_REPORT = "report";

	/** 常量 状态名称数组. */
	public static final String[] STATUS_NAMES = { "STARTING", "RUNNING", "STOPPING", "STOPPED", "SUSPENDING", "SUSPENDED" };

	/**
	 * 启动.状态由STOPPED转变成RUNNING
	 *
	 * @return 操作成功后返回true
	 */
	boolean start();

	/**
	 * 暂停，由RUNNING状态转变成SUSPENDING
	 *
	 * @return 操作成功后返回true
	 */
	boolean suspend();

	/**
	 * 恢复，由SUSPENDED状态恢复到RUNNING状态
	 *
	 * @return 操作成功后返回true
	 */
	boolean resume();

	/**
	 * 停止，调用此方法后，生命周期进入STOPPING状态
	 *
	 * @return 操作成功后返回true
	 */
	boolean stop();

	/**
	 * 获得当前状态的代码
	 * @return 当前状态代码
	 */
	int getStatus();

	/**
	 * 获得当前状态的名字
	 * @return 当前状态名字
	 */
	String getStatusName();
}
