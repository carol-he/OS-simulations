import java.util.ArrayList;
/**
 * each task object keeps info about a certain task. It keeps track of time taken, the time it's spent waiting
 * if it's blocked/aborted, and all the instructions left to be done.
 * @author carolhe
 *
 */
public class Task {
	private int timeTaken;
	private int waitingTime;
	private boolean aborted;
	private int blocked;
	private ArrayList<Instruction> Instructions = new ArrayList<Instruction>();
	public Task(int timeTaken, int waitingTime, boolean aborted, int blocked, ArrayList<Instruction> Instructions) {
		super();
		this.timeTaken = timeTaken;
		this.waitingTime = waitingTime;
		this.aborted = aborted;
		this.blocked = blocked;
		this.Instructions = Instructions;
	}
	public int getTimeTaken() {
		return timeTaken;
	}
	public void setTimeTaken(int timeTaken) {
		this.timeTaken = timeTaken;
	}
	public int getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	public boolean isAborted() {
		return aborted;
	}
	public void setAborted(boolean aborted) {
		this.aborted = aborted;
	}
	public int getBlocked() {
		return blocked;
	}
	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}
	public ArrayList<Instruction> getInstructions() {
		return Instructions;
	}
	public void setInstructions(ArrayList<Instruction> Instructions) {
		this.Instructions = Instructions;
	}

}
