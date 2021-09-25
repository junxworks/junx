/*
 ***************************************************************************************
 * 
 * @Title:  PhaseInitializer.java   
 * @Package io.github.junxworks.junx.core.tracker   
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
package io.github.junxworks.junx.core.tracker;

import java.util.ArrayList;

/**
 * 阶段初始化器，由外部自定义
 *
 * @ClassName:  PhaseInitializer
 * @author: Michael
 * @date:   2018-6-11 13:31:43
 * @since:  v1.0
 */
public interface PhaseInitializer {
	public ArrayList<Phase> initPhase();
}
