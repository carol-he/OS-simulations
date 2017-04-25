/**
 * object that holds instruction data. Each instruction has a String activity, an int delay, an int resourceType,
 * and an int numNeeded
 * @author carolhe
 *
 */
public class Instruction {
	private String activity;
	private int delay;
	private int resourceType;
	private int numNeeded;
	public Instruction(String activity, int delay, int resourceType, int numNeeded) {
		super();
		this.activity = activity;
		this.delay = delay;
		this.resourceType = resourceType;
		this.numNeeded = numNeeded;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	public int getNumNeeded() {
		return numNeeded;
	}
	public void setNumNeeded(int numNeeded) {
		this.numNeeded = numNeeded;
	}
	
}
