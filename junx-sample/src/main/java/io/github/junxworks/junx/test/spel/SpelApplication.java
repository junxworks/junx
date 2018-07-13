/*
 ***************************************************************************************
 * 
 * @Title:  SpelApplication.java   
 * @Package io.github.junxworks.junx.test.spel   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:15   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.spel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.junxworks.junx.spel.function.FunctionLoader;

@FunctionLoader
@SpringBootApplication
public class SpelApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpelApplication.class, args);
	}
}
