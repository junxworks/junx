/*
 ***************************************************************************************
 * 
 * @Title:  Service.java   
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;
import io.github.junxworks.junx.core.exception.FatalException;
import io.github.junxworks.junx.core.util.StringUtils;

/**
 * 抽象的服务对象，此对象实现了生命周期对象 {@link io.github.junxworks.junx.core.lifecycle.Lifecycle}，
 * 此处采用模板模式实现，此类是所有生命周期对象的基类，提供了基础的启动、暂停、恢复、停止、打印当前状态逻辑，
 * 具体实现 {@link #doStart()}、{@link #doStop()}需要子类自己去实现。
 *
 * @author: Michael
 * @date:   2017-5-7 17:46:03
 * @since:  v1.0
 */
public abstract class Service implements Lifecycle, Sortable {
	/** 每个服务对象有自己的日志打印。 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/** 服务对象名，内部标识，当description为空的时候，使用此属性作为服务名提供给使用者. */
	protected String name;

	/** 当前是否可用. */
	protected boolean enable = true;

	/** 服务描述，如果有值，则会作为本对象的服务名提供给使用者，此属性的优先级比name要高. */
	protected String description;

	/** 服务组. */
	protected String group = "default";

	/** 能否被远程控制管理，可以使用jmx远程控制. */
	protected boolean manageable = true;

	/** 服务当前状态. */
	private volatile int status = STOPPED;

	/** 服务优先级，值越小优先级越高，越先启动。 */
	private int priority = 0;

	/** 依赖的服务，依赖服务启动后才能启动本服务。 */
	protected String dependency;

	/**
	 * 构造一个新的对象.
	 */
	public Service() {
	}

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	/**
	 * 构造一个新的对象.
	 *
	 * @param name 指定服务名
	 */
	public Service(String name) {
		this.name = name;
	}

	public String getName() {
		return StringUtils.defaultString(name, getClass().getName());
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Lifecycle#start()
	 */
	public boolean start() {
		if (!isEnable()) {
			logger.error("Service [name=" + getServiceName() + "] is disabled.");
			return false;
		}
		if (status == STOPPED) {
			status = STARTING;
			try {
				logger.info("Start service " + getServiceName());
				doStart();
			} catch (Throwable ex) {
				doException(ex);
				logger.error("Exceptions occurred when start service [name=" + getServiceName() + "]", ex);
				return false;
			}
			status = RUNNING;
		} else {
			Exception e = new StartServiceException("Could not start Service [name=" + getServiceName() + "] in status [" + STATUS_NAMES[status] + "].");
			logger.warn(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Lifecycle#suspend()
	 */
	public boolean suspend() {
		if (!isEnable()) {
			return false;
		}
		if (status == RUNNING) {
			status = SUSPENDING;
			try {
				doSuspend();
				logger.info("Service [name=" + getServiceName() + "] suspended.");
			} catch (Throwable ex) {
				logger.error("Exceptions occurred when suspend service [name=" + getServiceName() + "]", ex);
				return false;
			}
			status = SUSPENDED;
		} else {
			Exception e = new SuspendServiceException("Could not suspend Service [name=" + getServiceName() + "] in status [" + STATUS_NAMES[status] + "].");
			logger.warn(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Lifecycle#resume()
	 */
	public boolean resume() {
		if (!isEnable()) {
			return false;
		}
		if (status == SUSPENDING || status == SUSPENDED) {
			try {
				doResume();
				logger.info("Service [name=" + getServiceName() + "] resumed.");
			} catch (Throwable ex) {
				logger.error("Exceptions occurred when resume service [name=" + getServiceName() + "]", ex);
				return false;
			}
			status = RUNNING;
		} else {
			Exception e = new ResumeServiceException("Could not resume Service [name=" + getServiceName() + "] in status [" + STATUS_NAMES[status] + "].");
			logger.warn(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Lifecycle#stop()
	 */
	public boolean stop() {
		if (!isEnable()) {
			return false;
		}
		if (status == STOPPING || status == STOPPED) {
			Exception e = new StopServiceException("Could not stop Service [name=" + getServiceName() + "] in status [" + STATUS_NAMES[status] + "].");
			logger.warn(e.toString());
			return true;
		} else {
			status = STOPPING;
			try {
				doStop();
				logger.info("Service [name=" + getServiceName() + "] stopped.");
			} catch (Throwable ex) {
				logger.error("Exceptions occurred when stop service [name=" + getServiceName() + "]", ex);
				return false;
			}
			status = STOPPED;
		}
		return true;
	}

	/**
	 * 重启，先关闭，然后在启动本对象
	 */
	public void restart() {
		if (!isEnable()) {
			return;
		}
		if (isRunning()) {
			stop();
		}

		start();
	}

	public int getStatus() {
		return status;
	}

	/**
	 * 是否在running状态
	 *
	 * @return 如果当前状态是running，则返回true
	 */
	public boolean isRunning() {
		return status == RUNNING;
	}

	/**
	 * 是否在starting状态
	 *
	 * @return 如果当前状态是starting，则返回true
	 */
	public boolean isStarting() {
		return status == STARTING;
	}

	/**
	 * 是否在stopped状态
	 *
	 * @return 如果当前状态是stopped，则返回true
	 */
	public boolean isStopped() {
		return status == STOPPED;
	}

	/**
	 * 是否在stopping状态
	 *
	 * @return 如果当前状态是stopping，则返回true
	 */
	public boolean isStopping() {
		return status == STOPPING;
	}

	/**
	 * 是否在suspending状态
	 *
	 * @return 如果当前状态是suspending，则返回true
	 */
	public boolean isSuspending() {
		return status == SUSPENDING;
	}

	/**
	 * 是否在suspended状态
	 *
	 * @return 如果当前状态是suspended，则返回true
	 */
	public boolean isSuspended() {
		return status == SUSPENDED;
	}

	public String getServiceName() {
		return StringUtils.isBlank(description) ? StringUtils.isBlank(name) ? getClass().getName() : name : description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * 服务在启动过程中，应该做的事，此方法由子类实现。
	 *
	 * @throws Throwable the throwable
	 */
	protected abstract void doStart() throws Throwable;

	/**
	 * 服务启动的时候，发生异常时调用此方法进行处理，默认是选择性实现，根据子类需求。
	 *
	 * @param ex 发生的异常对象
	 * @throws Throwable 
	 */
	protected void doException(Throwable ex) {
		if(ex instanceof FatalException){
			throw (BaseRuntimeException)ex;
		}
	}

	/**
	 * 暂停的执行方法，可选实现。
	 *
	 * @throws Throwable the throwable
	 */
	protected void doSuspend() throws Throwable {

	}

	/**
	 * 回复的执行方法，可选实现。
	 *
	 * @throws Throwable the throwable
	 */
	protected void doResume() throws Throwable {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isManageable() {
		return manageable;
	}

	public void setManageable(boolean manageable) {
		this.manageable = manageable;
	}

	public String getStatusName() {
		return STATUS_NAMES[status];
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return (o != null) && (o.getClass().equals(this.getClass())) && (((Service) o).getName() == null ? this.name == null : ((Service) o).getName().equals(this.name)) && (((Service) o).getGroup() == null ? this.group == null : ((Service) o).getGroup().equals(this.group));
	}

	/**
	 * 停止服务时候的执行方法，在子类中去实现.
	 *
	 * @throws Throwable the throwable
	 */
	protected abstract void doStop() throws Throwable;
}