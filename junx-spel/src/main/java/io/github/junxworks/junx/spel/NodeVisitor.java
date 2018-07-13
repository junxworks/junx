/*
 ***************************************************************************************
 * 
 * @Title:  NodeVisitor.java   
 * @Package io.github.junxworks.junx.spel   
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
package io.github.junxworks.junx.spel;

import org.springframework.expression.spel.SpelNode;

public interface NodeVisitor {
	public void visit(SpelNode node);
}
