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
