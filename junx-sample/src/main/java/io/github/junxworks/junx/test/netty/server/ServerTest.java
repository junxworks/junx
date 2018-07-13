/*
 ***************************************************************************************
 * 
 * @Title:  ServerTest.java   
 * @Package io.github.junxworks.junx.test.netty.server   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:16   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.netty.server;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class ServerTest {

	@Test
	public void serverTest() throws Exception {
		new CountDownLatch(1).await(); //block住主线程
	}

}
