package io.github.junxworks.junx.stat.datawindow.prioritywindow;

public class PriorityWindowDefinition {
	/** 窗口大小，一定要指定一个大小，而且不能是无穷大，
	 * 因为数据窗口会转换成byte流，不要采用无穷尽的累计，导致byte流过大. 
	 */
	private int windowSize;

	private BundleComparator comparator;

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public BundleComparator getComparator() {
		return comparator;
	}

	public void setComparator(BundleComparator comparator) {
		this.comparator = comparator;
	}

}
