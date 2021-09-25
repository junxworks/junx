/*
 ***************************************************************************************
 * 
 * @Title:  ZookeeperInitializer.java   
 * @Package io.github.junxworks.junx.cache.initializer   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:38:51   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.cache.initializer;

import java.util.List;
import java.util.Properties;

import org.apache.curator.framework.CuratorFramework;

public class ZookeeperInitializer extends ConfigInitializer {

	private CuratorFramework curatorFramework;

	public CuratorFramework getCuratorFramework() {
		return curatorFramework;
	}

	public void setCuratorFramework(CuratorFramework curatorFramework) {
		this.curatorFramework = curatorFramework;
	}

	protected Properties initProperties() throws Exception {
		Properties p = new Properties();
		List<String> ps = curatorFramework.getChildren().forPath(configPath);
		if (ps != null && !ps.isEmpty()) {
			for (String child : ps) {
				if (child.startsWith(cachePrefix)) {
					byte[] data = curatorFramework.getData().forPath(configPath + "/" + child);
					if (data != null && data.length > 0) {
						String value = new String(data);
						p.setProperty(child, value);
					}
				}
			}
		}
		return p;
	}

}
