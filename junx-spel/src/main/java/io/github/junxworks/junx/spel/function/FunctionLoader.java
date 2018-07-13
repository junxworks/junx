/*
 ***************************************************************************************
 * 
 * @Title:  FunctionLoader.java   
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 扫描加载并初始化表达式用到的函数
 *
 * @ClassName:  FunctionLoader
 * @author: Michael
 * @date:   2018-5-21 13:57:11
 * @since:  v5.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ImportBuiltInFunctions
@Import(FunctionLoadListener.class)
public @interface FunctionLoader {
}
