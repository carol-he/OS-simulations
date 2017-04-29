
public class Process {
	private double A;
	private double B;
	private double C;
	private int wordReferenced;
	private int faultCount = 0;
	private int residencyTime = 0;
	private int evictCount = 0;
	public Process(double a, double b, double c, int wordReferenced) {
		super();
		A = a;
		B = b;
		C = c;
		this.wordReferenced = wordReferenced;
	}
	public double getA() {
		return A;
	}
	public void setA(double a) {
		A = a;
	}
	public double getB() {
		return B;
	}
	public void setB(double b) {
		B = b;
	}
	public double getC() {
		return C;
	}
	public void setC(double c) {
		C = c;
	}
	public int getWordReferenced() {
		return wordReferenced;
	}
	public void setWordReferenced(int wordReferenced) {
		this.wordReferenced = wordReferenced;
	}
	public int getFaultCount() {
		return faultCount;
	}
	public void setFaultCount(int faultCount) {
		this.faultCount = faultCount;
	}
	public int getResidencyTime() {
		return residencyTime;
	}
	public void setResidencyTime(int residencyTime) {
		this.residencyTime = residencyTime;
	}
	public int getEvictCount() {
		return evictCount;
	}
	public void setEvictCount(int evictsCount) {
		this.evictCount = evictsCount;
	}
}
