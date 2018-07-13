/*
 ***************************************************************************************
 * 
 * @Title:  FuncMethod.java   
 * @Package io.github.junxworks.junx.spel.function.anno   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 10:23:27   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.spel.function.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 函数扫描注解
 *
 * @ClassName:  FuncMethod
 * @author: Michael
 * @date:   2018-5-18 11:14:34
 * @since:  v5.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FuncMethod {
	
	/**
	 * 函数名
	 *
	 * @return the string
	 */
	public String funcName();

	/**
	 * 函数分组
	 *
	 * @return the string[]
	 */
	public String[] funcGroup();
	
	/**
	 * 函数描述
	 *
	 * @return the string
	 */
	public String funcDesc();

	
}
