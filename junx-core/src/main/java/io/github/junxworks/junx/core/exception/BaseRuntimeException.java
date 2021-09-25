/*
 ***************************************************************************************
 * 
 * @Title:  BaseRuntimeException.java   
 * @Package io.github.junxworks.junx.core.exception   
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
package io.github.junxworks.junx.core.exception;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 * 自定义异常类的基类，提供了统一的异常格式化输出.
 *
 * @author: Michael
 * @date:   2017-5-7 17:17:20
 * @since:  v1.0
 * @see ContextedRuntimeException
 */
public class BaseRuntimeException extends ContextedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 异常id，可选。 */
	private String id;

	/** 异常详细描述。 */
	private String description;

	/**
	 * 构造一个新的 BaseRuntimeException 对象.
	 */
	public BaseRuntimeException() {
		super("");
	}

	/**
	 * 构造一个新的 BaseRuntimeException 对象.
	 *
	 * @param message 异常详细信息
	 */
	public BaseRuntimeException(String message) {
		super(message);
		this.description = message;
	}

	/**
	 * 根据一个Throwable对象构造一个新的BaseRuntimeException对象.
	 *
	 * @param ex Throwable对象或者其子类
	 */
	public BaseRuntimeException(Throwable ex) {
		super(ex);
	}

	/**
	 * 构造一个新的BaseRuntimeException对象.
	 *
	 * @param message 异常详细信息
	 * @param ex Throwable对象或者其子类
	 */
	public BaseRuntimeException(String message, Throwable ex) {
		super(message, ex);
		this.description = message;
	}

	/**
	 *构造一个新的 BaseRuntimeException 对象.
	 *详细信息可以根据string对象的格式化方法进行格式化
	 *
	 * @param message 异常详细信息的模板，可用string直接格式化
	 * @param args message里面需要格式化的参数
	 */
	public BaseRuntimeException(String message, Object... args) {
		this(String.format(message, args));
	}

	/**
	 * 构造一个新的BaseRuntimeException对象.
	 *
	 * @param message t异常详细信息的模板，可用string直接格式化
	 * @param e 实际发生的异常对象
	 * @param args message里面需要格式化的参数
	 */
	public BaseRuntimeException(String message, Exception e, Object... args) {
		this(String.format(message, args), e);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
