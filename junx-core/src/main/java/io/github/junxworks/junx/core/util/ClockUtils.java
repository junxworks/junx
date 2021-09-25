/*
 ***************************************************************************************
 * 
 * @Title:  ClockUtils.java   
 * @Package io.github.junxworks.junx.core.util   
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
package io.github.junxworks.junx.core.util;

/**
 * 用于系统内部计时
 *
 * @ClassName:  ClockUtils
 * @author: Michael
 * @date:   2017-7-24 17:29:57
 * @since:  v1.0
 */
public class ClockUtils {

	/**
	 * new一个新的clock对象，用于系统计时 .
	 *
	 * @return clock 属性
	 */
	public static Clock createClock() {
		return new Clock();
	}

	/**
	 * 计时器对象.
	 *
	 * @ClassName:  Clock
	 * @author: Michael
	 * @date:   2017-7-24 17:29:57
	 * @since:  v1.0
	 */
	public static class Clock {

		/** current nanotime. */
		private long currentNanotime;

		private long createTimeMillis;

		/**
		 * 构造一个新的 clock 对象.
		 */
		public Clock() {
			createTimeMillis = System.currentTimeMillis();
			reset();
		}

		public long createTimeMillis() {
			return createTimeMillis;
		}

		/**
		 * Reset.
		 */
		public void reset() {
			currentNanotime = System.nanoTime();
		}

		/**
		 * 纳秒计时
		 *
		 * @return the long
		 */
		public long countNanos() {
			return count(1);
		}

		/**
		 *微秒计时
		 *
		 * @return the long
		 */
		public long countMicros() {
			return count(1000);
		}

		/**
		 *毫秒计时
		 *
		 * @return the long
		 */
		public long countMillis() {
			return count(1000 * 1000);
		}

		/**
		 * 秒计时
		 *
		 * @return the long
		 */
		public long countSeconds() {
			return count(1000 * 1000 * 1000);
		}

		/**
		 * 计算时间.
		 *
		 * @param ratio 计算时间的比例，基础计算的值是纳秒，因此要换算成其他单位
		 * @return the long
		 */
		private long count(long ratio) {
			return count(currentNanotime, ratio);
		}

		/**
		 * 计算时间.
		 *
		 * @param initTime 开始值
		 * @param ratio 计算时间的比例，基础计算的值是纳秒，因此要换算成其他单位
		 * @return the long
		 */
		private long count(long initTime, long ratio) {
			return count(initTime, System.nanoTime(), ratio);
		}

		/**
		 * 计算时间.
		 * (当前值-初始值)/比例
		 * @param initTime 开始值
		 * @param currentTime 当前值
		 * @param ratio 计算时间的比例，基础计算的值是纳秒，因此要换算成其他单位
		 * @return the long
		 */
		private long count(long initTime, long currentTime, long ratio) {
			return (currentTime - initTime) / ratio;
		}

		/**
		 * 获取毫秒计时后的值，重置计数器，在连续计时的时候用
		 *
		 * @return the long
		 */
		public long countMillisAndReset() {
			long now = System.nanoTime();
			long ret = count(currentNanotime, now, 1000 * 1000);
			currentNanotime = now;
			return ret;
		}

		/**
		 * 获取秒计时后的值，重置计数器，在连续计时的时候用
		 *
		 * @return the long
		 */
		public long countSecondsAndReset() {
			long now = System.nanoTime();
			long ret = count(currentNanotime, now, 1000 * 1000 * 100);
			currentNanotime = now;
			return ret;
		}

		/**
		 * Count micros.
		 *
		 * @param currentNanos the current nanos
		 * @return the long
		 */
		public long countMicros(long currentNanos) {
			return count(currentNanotime, currentNanos, 1000);
		}

		/**
		 * Count millis.
		 *
		 * @param currentNanos the current nanos
		 * @return the long
		 */
		public long countMillis(long currentNanos) {
			return count(currentNanotime, currentNanos, 1000 * 1000);
		}

		/**
		 * Count seconds.
		 *
		 * @param currentNanos the current nanos
		 * @return the long
		 */
		public long countSeconds(long currentNanos) {
			return count(currentNanotime, currentNanos, 1000 * 1000 * 1000);
		}

		/**
		 * 查看基于入参timeoutMillis，剩余的时间，一般用于查看是否超时
		 *
		 * @param timeoutMillis the timeout millis
		 * @return the int
		 */
		public int leftMillis(int timeoutMillis) {
			long left = (int) (timeoutMillis - countMillis());
			return (int) (left >= 0 ? left : 0);
		}

	}
}
