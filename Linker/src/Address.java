
public class Address{
	private int address;
	private int moduleNum;
	private boolean exceedMachine;
	private boolean exceedModule;
	private Symbol noExist;
	private int usedTimes;
	//constructor
	public Address(int address, int moduleNum, boolean exceedMachine, boolean exceedModule, Symbol noExist, int usedTimes){
		this.address = address;
		this.moduleNum = moduleNum;
		this.exceedMachine = exceedMachine;
		this.noExist = noExist;
		this.usedTimes = usedTimes;
	}
	public Symbol getNoExist() {
		return noExist;
	}
	public void setNoExist(Symbol noExist) {
		this.noExist = noExist;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public boolean isExceedMachine() {
		return exceedMachine;
	}
	public void setExceedMachine(boolean exceedMachine) {
		this.exceedMachine = exceedMachine;
	}
	public int getModuleNum() {
		return moduleNum;
	}
	public void setModuleNum(int moduleNum) {
		this.moduleNum = moduleNum;
	}
	public boolean isExceedModule() {
		return exceedModule;
	}
	public void setExceedModule(boolean exceedModule) {
		this.exceedModule = exceedModule;
	}
	public int getUsedTimes() {
		return usedTimes;
	}
	public void setUsedTimes(int usedTimes) {
		this.usedTimes = usedTimes;
	}

}
