/*
 ***************************************************************************************
 * 
 * @Title:  SlicedTimeBasedDataWindow.java   
 * @Package io.github.junxworks.junx.stat.datawindow.timewindow   
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
package io.github.junxworks.junx.stat.datawindow.timewindow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.github.junxworks.junx.stat.StatContext;
import io.github.junxworks.junx.stat.datawindow.AbstractDataWindow;
import io.github.junxworks.junx.stat.datawindow.DataBundle;

import io.github.junxworks.junx.core.exception.NullParameterException;
import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.core.util.DateUtils;

/**
 * 我们把基于时间的数据窗口（Time-Based DataWindow）简称为时间窗口，
 * 我们把交易事件的实际发生时间作为时间窗口的标准时间，所有时间窗口内的数据是否过期都是以
 * 交易事件的实际发生时间作为计算依据（这个实际发生时间是由客户系统提供），时间窗口定义了
 * 一个基于事件发生时间的数据集合，这个集合内部定义了一个过期时间，凡是超过了过期时间的事
 * 件，都会被时间窗口踢出集合，时间窗口的作用就是维护这么一个具有过期时间的数据集合。
 *
 * @author: michael
 * @date: 2017-5-17 15:46:43
 * @since: v1.0
 */
public class SlicedTimeBasedDataWindow extends AbstractDataWindow implements TimeBasedDataWindow {

	/** 过期时间点. */
	private long expireTimePoint;

	/** 领跑时间，目前是以领跑事件所在block的领跑时间为当前时间窗口的领跑时间，领跑时间不会跨度到下一个block. */
	private long pacemakerTime;

	/** 时间窗口定义，包含单位，周期等. */
	private TimeWindowDefinition definition;

	/** 切分块列表. */
	private LinkedList<SlicedBlock> blocks = new LinkedList<>();// 一开始切分块为空，不会初始化，通过事件的到来，动态调整切分块大小，同时还可以动态设置TimeWindowDefinition

	/** 切分块工厂，由每个函数自己去实现 */
	private SlicedBlockFactory blockFactory;

	/**
	 * 构造一个新的对象.
	 *
	 * @param definition
	 *            the definition
	 * @param blockFactory
	 *            the block factory
	 */
	public SlicedTimeBasedDataWindow(TimeWindowDefinition definition, SlicedBlockFactory blockFactory) {
		this.definition = definition;
		this.blockFactory = blockFactory;
	}

	public long getExpireTimePoint() {
		return expireTimePoint;
	}

	public void setExpireTimePoint(long expireTimePoint) {
		this.expireTimePoint = expireTimePoint;
	}

	public long getPacemakerTime() {
		return pacemakerTime;
	}

	public void setPacemakerTime(long pacemakerTime) {
		this.pacemakerTime = pacemakerTime;
	}

	public TimeWindowDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(TimeWindowDefinition definition) {
		this.definition = definition;
	}

	public SlicedBlockFactory getBlockFactory() {
		return blockFactory;
	}

	public void setBlockFactory(SlicedBlockFactory blockFactory) {
		this.blockFactory = blockFactory;
	}

	/**
	 * 同包可用，不外露，只让切分策略使用
	 *
	 * @return blocks 属性
	 */
	public LinkedList<SlicedBlock> getBlocks() {
		return blocks;
	}

	/**
	 * 清空数据窗口中的数据(切分块).
	 */
	@Override
	public void clear() {
		blocks.clear();
	}

	@Override
	public void compose(DataBundle... newData) throws Exception {
		if (newData == null) {
			throw new UnsupportedOperationException("The data to compose into SlicedTimeBasedDataWindow can't be null.");
		}
		if (TimeUnit.eternal == definition.getUnit()) {
			if (blocks.isEmpty()) {
				insertBlockIntoList(TimeUnit.eternal.getStrategy().slice(this, -1));
			}
			// 没有过期时间
			blocks.getFirst().compose(newData);// 有且只有一个block，直接往第一个切分块中组合即可
		} else {
			boolean needToCheckBlocks = false;//是否需要检查有过期block，当且仅当有新增block的时候才会检查，没有新增就原封不懂
			for (int i = 0, len = newData.length; i < len; i++) {
				DataBundle data = newData[i];
				long eventTimeStamp = data.getTimestamp();
				dispatchBlock(data);// 根据事件的发生时间，分配到对应的block里面，这里强制转型，能进来的一定是ExpirableObject的子类
				if (eventTimeStamp > pacemakerTime) {
					// 当前事件的发生时间超过了时间窗口的范围，肯定更新了领跑时间
					// 注意，新增了block不一定会更新领跑时间，因为新增的block可能是在时间窗口内部，而事件发生时间大于领跑时间时候，
					// 肯定是新增了block，并且会以当前最新block的领跑时间为窗口的领跑时间
					long _pacemakerTime = blocks.getFirst().getPacemakerTime(); // 取当前blocks里面第一个block的pacemaker时间为领跑时间
					if (pacemakerTime < _pacemakerTime) {
						pacemakerTime = _pacemakerTime; // 设置领跑时间
						expireTimePoint = calculateWindowExpireTimePoint(pacemakerTime);// 过期时间是通过领跑时间动态计算出来的
					}
					needToCheckBlocks = true;
				}
			}
			if (needToCheckBlocks) {
				checkSlicedBlocks(blocks, expireTimePoint);
			}
		}
	}

	/**
	 * 按事件的发生时间，分配到具体的切分块里面去
	 *
	 * @param obj
	 *            the obj
	 */
	private void dispatchBlock(DataBundle obj) {
		final long createTime = obj.getTimestamp();
		if (createTime < this.expireTimePoint) {// 如果事件时间撮直接在窗口之外，则直接返回
			return;
		}
		boolean needToCreateBlock = true;
		Iterator<SlicedBlock> _blocks = blocks.iterator();
		SlicedBlock block;
		while (_blocks.hasNext()) {
			block = _blocks.next();
			if (createTime <= block.getPacemakerTime() && createTime >= block.getExpireTimePoint()) {// 当事件发生时间在切分块的有效时间区间内时，将事件的统计累计到切分块中
				try {
					block.compose(obj);
				} catch (Exception e) {
					logger.error("Exception occurred when compose stat value into SlicedBlock.", e);
				} finally {
					needToCreateBlock = false;
				}
				break;
			}
		}
		if (needToCreateBlock) {// 当所有切分块都不满足当前的事件时间撮时，根据切分策略新增加一个切分块
			SlicedBlock newBlock = null;
			try {
				newBlock = definition.getUnit().getStrategy().slice(this, createTime);
				newBlock.compose(obj);
				insertBlockIntoList(newBlock);
			} catch (Exception e) {
				logger.error("Exception occurred when compose stat value into SlicedBlock.", e);
			}
		}
	}

	/**
	 * 将新的切分块插入到现有的list中，按block的领跑时间先后顺序插入，领跑时间越小的，离队尾越近
	 *
	 * @param block
	 *            the block
	 * @param blocks
	 *            the blocks
	 */
	private void insertBlockIntoList(SlicedBlock block) {
		// long pacemakerTime = block.getPacemakerTime();
		// if (blocks.isEmpty()) {
		// blocks.addFirst(block);
		// return;
		// }
		// for (int i = 0, len = blocks.size(); i < len; i++) {
		// SlicedBlock _block = blocks.get(i);
		// if (_block.getPacemakerTime() < pacemakerTime) {
		// blocks.add(i, block);
		// }
		// }
		blocks.addFirst(block);
	}

	/**
	 * 根据当前的领跑时间以及时间窗定义，计算当前时间窗口的过期时间点。 目前已知的能够计算过期时间点的参数有：领跑时间、窗口单位、窗口周期。
	 * 如果单位是秒、分、时、日、周的，则可以根据单位的固定时间长度，计算出本窗口的过期时间点。
	 * 如果是月、年的，则需要动态计算，因为月和年的长度不定，每个月例如1月、2月，他们的时间宽度是不一样的。 年份的话，分闰年和平年，天数也是不一样的。
	 * 如果时间单位是永久的话，则不计算过期时间，因为没有过期。
	 * 
	 * @param pTime
	 *            当前时间窗口的领跑时间
	 * @return 当前时间窗口的过期时间点
	 */
	private long calculateWindowExpireTimePoint(long pTime) {
		TimeUnit unit = this.definition.getUnit();
		int interval = this.definition.getInterval();
		if (TimeUnit.second.equals(unit) || TimeUnit.minute.equals(unit) || TimeUnit.hour.equals(unit) || TimeUnit.day.equals(unit) || TimeUnit.week.equals(unit)) {
			return pTime - interval * unit.getMillis();
		} else if (TimeUnit.month.equals(unit)) {
			long month = DateUtils.parseDate(DateUtils.formatDate(pTime, "yyyy-MM"), "yyyy-MM").getTime();
			Date mDate = new Date(month);
			Date firstDayOfMonth = DateUtils.addMonths(mDate, -interval + 1);
			return firstDayOfMonth.getTime();
		} else if (TimeUnit.year.equals(unit)) {
			long year = DateUtils.parseDate(DateUtils.formatDate(pTime, "yyyy"), "yyyy").getTime();
			Date yDate = new Date(year);
			Date firstDayOfYear = DateUtils.addMonths(yDate, -interval + 1);
			return firstDayOfYear.getTime();
		}
		return 0;
	}

	/**
	 * 检查切分块集合里面哪些切分块过期，从list里面剔除。
	 */
	private void checkSlicedBlocks(Collection<SlicedBlock> blocks, long expireTimePoint) {
		if (!blocks.isEmpty()) {
			Iterator<SlicedBlock> _blocks = blocks.iterator();
			SlicedBlock block;
			while (_blocks.hasNext()) {
				block = _blocks.next();
				if (block.isExpired(expireTimePoint)) {
					_blocks.remove();
				}
			}
		}
	}

	@Override
	public Collection<?> extractData(StatContext ctx) throws Exception {
		if (ctx == null) {
			throw new NullParameterException("Parameter can not be null. ");
		}
		if (TimeUnit.eternal == this.definition.getUnit()) {
			//如果是永久时间窗口，则直接返回当前数据集	
			if (!blocks.isEmpty()) {
				return blocks.getFirst().getData();
			}
			return new ArrayList<>();
		}
		long timestamp = ctx.getTimestamp();
		if (!blocks.isEmpty()) {
			List<SlicedBlock> _blocks = new LinkedList<>();
			_blocks.addAll(blocks);
			SlicedBlock block = this.definition.getUnit().getStrategy().slice(this, timestamp);
			long _pacemakerTime = block.getPacemakerTime();
			long expireTimeP = this.expireTimePoint;
			if (pacemakerTime < _pacemakerTime) {// 当指定时间撮所在block的领跑时间大于当前时间窗口的领跑时间时，要重新计算过期时间点
				expireTimeP = calculateWindowExpireTimePoint(_pacemakerTime);// 过期时间是通过领跑时间动态计算出来的
			}
			checkSlicedBlocks(_blocks, expireTimeP);// 过滤过期block
			if (!_blocks.isEmpty()) {// 过滤后如果还不为空
				return _extractData(_blocks);
			}
		}
		return new ArrayList<>();
	}

	@Override
	public Collection<?> getData() {
		return _extractData(blocks);
	}

	/**
	 * 从指定的集合中抽取数据到一个集合
	 *
	 * @param <T>
	 *            the generic type
	 * @param type
	 *            the type
	 * @param blocks
	 *            the blocks
	 * @return the t[]
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection<?> _extractData(List<SlicedBlock> blocks) {
		if (!blocks.isEmpty()) {
			Collection res = new ArrayList<>();
			Iterator<SlicedBlock> _blocks = blocks.iterator();
			while (_blocks.hasNext()) {
				SlicedBlock block = _blocks.next();
				res.addAll(block.getData());
			}
			return res;
		}
		return new ArrayList<>();
	}

	/**
	 * 将已有切分块存入时间窗口中.
	 *
	 * @param blocks
	 *            the blocks
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setData(Collection<?> data) {
		if (blocks.size() <= 0) {
			blocks.addAll((Collection<? extends SlicedBlock>) data);
		}
	}

	@Override
	public byte[] toBytes() throws Exception {
		if (blocks.isEmpty()) {
			return null;
		}
		//将目前的所有block写入到byte数组中
		ByteContainer bc = new ByteContainer();
		//1、先写如当前blocks的size
		bc.writeShort(Short.valueOf(String.valueOf(blocks.size())));
		SlicedBlock head = blocks.getFirst();
		long baseTime = head.getPacemakerTime();
		bc.writeLong(baseTime);
		Iterator<SlicedBlock> itr = blocks.iterator();
		while (itr.hasNext()) {
			SlicedBlock block = itr.next();
			byte[] sbData = block.toBytes();
			if (sbData != null && sbData.length > 0) {
				int timeOffset = Long.valueOf(baseTime - block.getPacemakerTime()).intValue();//时间偏移量
				bc.writeInt(timeOffset);
				bc.writeBytes(sbData);
			}
		}
		return bc.toBytes();
	}

	@Override
	public void readBytes(byte[] bytes) throws Exception {
		ByteContainer bc = new ByteContainer(bytes);
		short slicedBlockCount = bc.readShort(); //总的block数量
		long baseTimestamp = bc.readLong();//基础时间撮
		//下面按序列化的反序还原数据窗口
		for (int i = 0; i < slicedBlockCount; i++) {
			int timeOffset = bc.readInt();
			long slicedBlockPacemakertime = baseTimestamp - timeOffset;
			SlicedBlock block = this.definition.getUnit().getStrategy().slice(this, slicedBlockPacemakertime);
			block.readBytes(bc.readByteArray());
			blocks.addLast(block);//后面的都排队尾去，因为序列化的时候是按先到后的顺序序列化的
		}
	}

}
