/*
 ***************************************************************************************
 * 
 * @Title:  MethodDescriptor.java   
 * @Package io.github.junxworks.junx.spel.function   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 10:23:26   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.spel.function;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Objects;

import io.github.junxworks.junx.core.util.StringUtils;

/**
 * 扫描出来的method描述类
 *
 * @ClassName:  MethodDescriptor
 * @author: Michael
 * @date:   2018-5-17 15:02:00
 * @since:  v5.0
 */
public class MethodDescriptor {

	/** 函数名. */
	@JSONField(ordinal = 0)
	private String name;

	/** 函数说明，用于展示. */
	@JSONField(ordinal = 1)
	private String description;

	/** 具体的方法. */
	@JSONField(serialize = false)
	private Method method;

	/** 定义此方法的class. */
	@JSONField(ordinal = 2)
	private Class<?> defineClass;

	/** 函数的分组，可用于定义函数类型. */
	@JSONField(ordinal = 3)
	private String[] groups;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Class<?> getDefineClass() {
		return defineClass;
	}

	public void setDefineClass(Class<?> defineClass) {
		this.defineClass = defineClass;
	}

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	/**
	 * 是否属于该组
	 *
	 * @param group the group
	 * @return 返回布尔值 in gourp
	 */
	public boolean isInGourp(String group) {
		return StringUtils.isIn(group, groups);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return (o != null && o instanceof MethodDescriptor) && name.equals(((MethodDescriptor) o).name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
