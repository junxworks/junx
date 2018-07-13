/*
 ***************************************************************************************
 * 
 * @Title:  Snapshot.java   
 * @Package io.github.junxworks.junx.stat.function.snapshot   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-12 20:49:29   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.function.snapshot;

import java.util.Collection;

import io.github.junxworks.junx.core.util.StringUtils;
import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.function.BaseFunction;

/**
 * 快照函数，用于统计最近一次的交易结果.
 *
 * @ClassName:  Snapshot
 * @author: Michael
 * @date:   2017-6-29 9:58:40
 * @since:  v1.0
 */
public class Snapshot extends BaseFunction {

	@Override
	public Object getValue(Collection<?> collection, StatContext context) throws Exception {
		if (collection.size() > 0) {
			SnapshotObject so = (SnapshotObject) collection.stream().distinct().max((o1, o2) -> ((SnapshotObject) o1).getTimestamp() > ((SnapshotObject) o2).getTimestamp() ? 1 : -1).get();
			String v = so.getValue();
			return StringUtils.isNull(v) ? null : v;
		} else {
			// 快照默认值
			return null;
		}
	}

}
