public class Cycle {
	private String state;
	private int remainingBurst;
	private int randomUsed;
	public Cycle(String state, int remainingBurst) {
		super();
		this.state = state;
		this.remainingBurst = remainingBurst;
	}
	public Cycle(String state, int remainingBurst, int randomUsed) {
		super();
		this.state = state;
		this.remainingBurst = remainingBurst;
		this.randomUsed = randomUsed;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getRemainingBurst() {
		return remainingBurst;
	}
	public void setRemainingBurst(int remainingBurst) {
		this.remainingBurst = remainingBurst;
	}
	public int getRandomUsed() {
		return randomUsed;
	}
	public void setRandomUsed(int randomUsed) {
		this.randomUsed = randomUsed;
	}
	
}
