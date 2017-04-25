import java.util.ArrayList;
import java.util.Comparator;
//baiscally it's the columns of the cycle table/the processes
public abstract class Process implements Comparable<Process>{  //**** for array sort compareto()  compare()
    int[] ABCIO = new int[4];
    private int[] copy = new int[4];
    private int startTime = -1;
    private int finishingTime;
    private int turnaroundTime;
    private int IOTime;
    private int waitingTime;
    private String state;    
    private int sequence;
    
    private ArrayList<Cycle> cycles = new ArrayList<Cycle>();
    public Process(int[] ABCIO, int[] copy, int startTime, int finishingTime, int turnaroundTime, int IOTime, int waitingTime, ArrayList<Cycle> cycles, String state,int sequence) {
	super();
	this.ABCIO = ABCIO;
	this.copy = copy;
	this.finishingTime = finishingTime;
	this.startTime = startTime;
        this.turnaroundTime = turnaroundTime;
	this.IOTime = IOTime;
	this.waitingTime = waitingTime;
	this.cycles = cycles;
	this.state = state;
        this.sequence = sequence;
    }
    public Process() {
		// TODO Auto-generated constructor stub
    }
    public int[] getABCIO() {
	return ABCIO;
    }
    public void setABCIO(int[] aBCIO) {
	ABCIO = aBCIO;
    }
    public int[] getCopy() {
	return copy;
    }
    public void setCopy(int[] copy) {
	this.copy = copy;
    }
    public int getFinishingTime() {
	return finishingTime;
    }
    public void setFinishingTime(int finishingTime) {
        this.finishingTime = finishingTime;
    }
    public int getStartingTime() {
	return startTime;
    }
    public void setStartingTime(int startTime) {
	this.startTime = startTime;
    }
    public int getTurnaroundTime() {
	return turnaroundTime;
    }
    public void setTurnaroundTime(int turnaroundTime) {
	this.turnaroundTime = turnaroundTime;
    }
    public int getIOTime() {
	return IOTime;
    }
    public void setIOTime(int iOTime) {
	IOTime = iOTime;
    }
    public int getWaitingTime() {
	return waitingTime;
    }
    public void setWaitingTime(int waitingTime) {
	this.waitingTime = waitingTime;
    }
    public ArrayList<Cycle> getCycles() {
	return cycles;
    }
    public void setCycles(ArrayList<Cycle> cycles) {
	this.cycles = cycles;
    }
    public String getState() {
	return state;
    }
    public void setState(String state) {
	this.state = state;
    }
    public int getsequence() {
	return sequence;
    }
    public void setcsequence(int sequence) {
	this.sequence = sequence;
    }
    public int compareTo(Process compareProcess) {
        int compareQuantity = ((Process) compareProcess).getABCIO()[0];
        //ascending order
        return this.ABCIO[0] - compareQuantity;
        //descending order
        //return compareQuantity - this.quantity;
    }          
public static Comparator<Process> TimeComparator
                          = new Comparator<Process>() {

	    public int compare(Process Process1, Process Process2) {

	      int t1 = Process1.copy[2];
	      int t2 = Process2.copy[2];

	      //ascending order
	      return (t1-t2);

	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }

	};
public static Comparator<Process> SequenceComparator
                          = new Comparator<Process>() {

	    public int compare(Process Process1, Process Process2) {

	      int s1 = Process1.sequence;
	      int s2 = Process2.sequence;

	      //ascending order
	      return (s1-s2);

	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }

	};
}
