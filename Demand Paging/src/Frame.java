
public class Frame {
	private int startTime;
	private int endTime;
	private int pageNum;
	private int processID;
	public Frame(int startTime, int endTime, int pageNum, int processID) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.pageNum = pageNum;
		this.processID = processID;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getProcessID() {
		return processID;
	}
	public void setProcessID(int processID) {
		this.processID = processID;
	}
}
