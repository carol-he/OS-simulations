
public class Symbol {
	private String name;
	private int value;
	private boolean used;
	private int definedIn;
	private boolean useExceedsModSize;
	private boolean multDef;
	private boolean defExceedsModSize;
	//constructor
	public Symbol(String name, int value, boolean used, int definedIn, boolean useExceedsModSize,
			 boolean multDef, boolean defExceedsModSize){
		this.name = name;
		this.value = value;
		this.used = used;
		this.definedIn = definedIn;
		this.useExceedsModSize = useExceedsModSize;
		this.multDef = multDef;
		this.defExceedsModSize = defExceedsModSize;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public int getDefinedIn() {
		return definedIn;
	}
	public void setDefinedIn(int definedIn) {
		this.definedIn = definedIn;
	}
	public boolean isUseExceedsModSize() {
		return useExceedsModSize;
	}
	public void setUseExceedsModSize(boolean exceedsModSize) {
		this.useExceedsModSize = exceedsModSize;
	}
	public boolean isMultDef() {
		return multDef;
	}
	public void setMultDef(boolean multDef) {
		this.multDef = multDef;
	}
	public boolean isDefExceedsModSize() {
		return defExceedsModSize;
	}
	public void setDefExceedsModSize(boolean defExceedsModSize) {
		this.defExceedsModSize = defExceedsModSize;
	}
	
	
}
