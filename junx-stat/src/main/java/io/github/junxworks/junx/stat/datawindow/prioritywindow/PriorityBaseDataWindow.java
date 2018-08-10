/*
 ***************************************************************************************
 * 
 * @Title:  PriorityBaseDataWindow.java   
 * @Package io.github.junxworks.junx.stat.datawindow.prioritywindow   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-8-10 16:11:10   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.stat.datawindow.prioritywindow;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.esotericsoftware.minlog.Log;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;
import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.core.util.ByteUtils;
import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.datawindow.AbstractDataWindow;
import io.github.junxworks.junx.stat.datawindow.DataBundle;
import io.github.junxworks.junx.stat.datawindow.SlicedBlock;
import io.github.junxworks.junx.stat.datawindow.SlicedBlockFactory;

/**
 * 基于数据优先级的数据窗口
 * 
 * @ClassName:  PriorityBaseDataWindow
 * @author: Michael
 * @date:   2018-8-10 10:49:21
 * @since:  v1.0
 */
public class PriorityBaseDataWindow extends AbstractDataWindow {

	/** 窗口定义. */
	private PriorityWindowDefinition definition;

	/** 经过优先级排序的data bundle，累计的时候会先进行优先级排序，去除多余的bundle */
	private LinkedList<DataBundle> sortedBundles;

	/** 数据集合. */
	private Map<DataBundle, SlicedBlock> blocks;

	/** 切分块工厂，由每个函数自己去实现 */
	private SlicedBlockFactory blockFactory;

	/**
	 * 构造一个新的 priority base data window 对象.
	 *
	 * @param definition the definition
	 */
	public PriorityBaseDataWindow(PriorityWindowDefinition definition, SlicedBlockFactory blockFactory) {
		this.definition = definition;
		this.blockFactory = blockFactory;
		sortedBundles = Lists.newLinkedList();
		blocks = Maps.newHashMap();
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.Serializable#toBytes()
	 */
	@Override
	public byte[] toBytes() throws Exception {
		if (blocks.isEmpty()) {
			return null;
		}
		ByteContainer bytes = new ByteContainer();
		bytes.writeInt(blocks.size());
		Iterator<Entry<DataBundle, SlicedBlock>> entries = blocks.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<DataBundle, SlicedBlock> entry = entries.next();
			try {
				bytes.writeBytes(ByteUtils.object2KryoBytes(entry.getKey()));
				bytes.writeBytes(entry.getValue().toBytes());
			} catch (Exception e) {
				Log.error("DataWindow to bytes failed.", e);
				return null;
			}
		}
		return bytes.toBytes();
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.Serializable#readBytes(byte[])
	 */
	@Override
	public void readBytes(byte[] data) throws Exception {
		sortedBundles = Lists.newLinkedList();
		blocks = Maps.newHashMap();
		ByteContainer bytes = new ByteContainer(data);
		for (int i = 0, size = bytes.readInt(); i < size; i++) {
			DataBundle key = ByteUtils.KryoBytes2Object(DataBundle.class, bytes.readByteArray());
			SlicedBlock value = blockFactory.createBlock(this);
			value.readBytes(bytes.readByteArray());
			blocks.put(key, value);
			sortedBundles.add(key);
		}
	}

	@Override
	public Collection<?> getData() {
		return extractSlicedBlocks(blocks.values());
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#compose(io.github.junxworks.junx.stat.datawindow.DataBundle[])
	 */
	@Override
	public void compose(DataBundle... newData) throws Exception {
		Set<DataBundle> newBundles = Sets.newHashSet(newData);
		sortedBundles.addAll(newBundles);
		int windowSize = definition.getWindowSize();
		if (sortedBundles.size() > windowSize) {
			//先进行优先级排序，找出多余的bundle
			sortedBundles.sort(definition.getComparator());
			while (sortedBundles.size() > windowSize) {
				DataBundle removed = sortedBundles.removeLast();
				blocks.remove(removed);
				newBundles.remove(removed);
			}
		}
		if (!newBundles.isEmpty()) {
			newBundles.stream().forEach(n -> {
				try {
					addBundle(n);
				} catch (Exception e) {
					Log.error("Compose data bundle failed.", e);
				}
			});
		}
	}

	private void addBundle(DataBundle bundle) throws Exception {
		SlicedBlock block = blockFactory.createBlock(this);
		block.compose(bundle);
		blocks.put(bundle, block);
	}

	@Override
	public void setData(Collection<?> data) {
		if (data != null) {
			DataBundle[] bundles = data.toArray(new DataBundle[0]);
			try {
				compose(bundles);
			} catch (Exception e) {
				throw new BaseRuntimeException(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#extractData(io.github.junxworks.junx.stat.StatContext)
	 */
	@Override
	public Collection<?> extractData(StatContext context) throws Exception {
		return getData();
	}

	/* (non-Javadoc)
	 * @see io.github.junxworks.junx.stat.datawindow.DataWindow#clear()
	 */
	@Override
	public void clear() {
		sortedBundles.clear();
		blocks.clear();
	}

}
