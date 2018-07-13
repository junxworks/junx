/*
 ***************************************************************************************
 * 
 * @Title:  ImportBuiltInFunctions.java   
 * @Package io.github.junxworks.junx.spel.function   
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
package io.github.junxworks.junx.spel.function;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.junxworks.junx.spel.function.builtin.DateFunc;
import io.github.junxworks.junx.spel.function.builtin.MathFunc;
import io.github.junxworks.junx.spel.function.builtin.StringFunc;
import org.springframework.context.annotation.Import;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ MathFunc.class, DateFunc.class, StringFunc.class })
public @interface ImportBuiltInFunctions {
}
