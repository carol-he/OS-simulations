import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class linker{
	public static int modules = 0;
	public static int num = 0;
	public static String symName;
	public static int symVal;
	public static int baseAddress = 0;
	public static ArrayList<Integer> modSizes = new ArrayList<Integer>();
	//sizes of modules
	public static ArrayList<Symbol> sym = new ArrayList<Symbol>();
	public static ArrayList<Address> addr = new ArrayList<Address>();
	//holds base addresses
	public static ArrayList<Integer> moduleArray = new ArrayList<Integer>();
	
	public static void passOne(File fileName){
		try {
			Scanner src = new Scanner(fileName);
            if(src.hasNextInt() == true){
            	modules = src.nextInt();
        	}
            for(int i = 0; i <= modules - 1; i++){
            //change 0 to i
            //keeps track of base addresses
            	moduleArray.add(baseAddress);
            	//definitions list
            	num = src.nextInt();
            	if(num != 0){
		            for(int j = 0; j <= num - 1; j++){
		            	symName = src.next();
		            	boolean dupe = false;
		           		for(int k = 0; k <= sym.size() - 1; k++){
		           			if(sym.get(k).getName().equals(symName)){
		           				dupe = true;
		           				sym.get(k).setMultDef(true);
		            		}
		            	}
		            	if(dupe == true){
		            		
		            		src.nextInt();
		            	}
		            	else{
		            		symVal = src.nextInt() + baseAddress;
		            		Symbol temp = new Symbol(symName, symVal, false, i + 1, false, false, false);
		            		sym.add(temp);
		            	}
		            }
            	}
            	//where definitions are used
            	num = src.nextInt();
            	if(num != 0){
		            for(int j = 0; j <= num - 1; j++){
		            	src.next();
		            	int test = 0;
		            	while(test != -1){
		            		test = src.nextInt();
		            	}
		            }
            	}
            	//addresses   	
            	num = src.nextInt();
            	modSizes.add(num);
            	int tempAddress = 0;
            	for(int j = 0; j <= num - 1; j++){
                 	src.next();
            		tempAddress = src.nextInt();
            		Address temp = new Address(tempAddress, i + 1, false, false, null, 0);
            		addr.add(temp);
		        }
                for(int j = 0; j < sym.size(); j++){
                //loop through symbol table to find if defn exceeds module size
                	if(sym.get(j).getDefinedIn() == i + 1){
                		//size of current module shouldnt be less than or equal to the symbol's relative value
	                	if(num <= sym.get(j).getValue() - moduleArray.get(sym.get(j).getDefinedIn() - 1)){
	                		sym.get(j).setDefExceedsModSize(true);
	                		sym.get(j).setValue(moduleArray.get(sym.get(j).getDefinedIn() - 1));
	                	}
                	}
                }
            	baseAddress = baseAddress + num;	
            }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void passTwo(File fileName){
		try {
			Scanner src = new Scanner(fileName);
			src.nextInt();
			int count = 0;
            for(int i = 0; i <= modules - 1; i++){
            	//definitions list
            	num = src.nextInt();
            	if(num != 0){
		            for(int j = 0; j <= num - 1; j++){
		            	src.next();
		            	src.nextInt();
		            }
            	}
            	//where definitions are used
            	String symUsed;
            	int point = 0;
            	num = src.nextInt();
            	if(num != 0){
		            for(int j = 0; j <= num - 1; j++){
		            	symUsed = src.next();
		            	point = src.nextInt();
		            	boolean exists = false;
		            	boolean exceeded = false;
		            	while(point != -1){
	            			if(addr.get(moduleArray.get(i) + point).getModuleNum() != (i + 1)){
	            				//ignore use
	            				point = 0;
			            		for(int k = 0; k <= sym.size() - 1; k++){
			            			if(sym.get(k).getName().equals(symUsed)){
			            				 sym.get(k).setUseExceedsModSize(true);
			            			}
			            		} 
			            		exceeded = true;
	            			} else {
			            		int old = addr.get(moduleArray.get(i) + point).getAddress();
			            		int valUsed = 0;
			            		for(int k = 0; k <= sym.size() - 1; k++){
			            			if(sym.get(k).getName().equals(symUsed)){
			            				 valUsed = sym.get(k).getValue();
			            				 sym.get(k).setUsed(true);
			            				 exists = true;
			            			}
			            		}
			            		//if symbol doesn't exist, create it and make the value 0
			            		if(exists == false){
			            			Symbol temp;
			            			if(!exceeded){
			            				temp = new Symbol(symUsed, 0, true, i + 1, false, false, false);
			            			}
			            			else{
			            				temp = new Symbol(symUsed, 0, true, i + 1, true, false, false);
			            			}
			            			addr.get(moduleArray.get(i) + point).setNoExist(temp);
			            		}
			            		//if it hasn't been used, use it
			            		if(addr.get(moduleArray.get(i) + point).getUsedTimes() == 0){
			            			int nu = ((old / 1000) * 1000) + valUsed;
			            			addr.get(moduleArray.get(i) + point).setAddress(nu);
			            		}
			            		//increment used times
			            		int x = addr.get(moduleArray.get(i) + point).getUsedTimes() + 1;
			            		addr.get(moduleArray.get(i) + point).setUsedTimes(x);
	            			}
		            		point = src.nextInt();
		            	}
		            }
            	}
            	//addresses   	
            	num = src.nextInt(); 
            	int absAddr = 0;
            	String type = "";
            	if(num != 0){
            		//loop through number of addresses in console
            		for(int j = 0; j <= num - 1; j++){
            			type = src.next();
            			//if its a relative address..
                 		if(type.equals("R")){
                 			//calculate abs address
                 			absAddr = moduleArray.get(i) + src.nextInt();
                 			// relative address  
                 			if(addr.get(moduleArray.get(i) + j).getAddress() % 1000 >= modSizes.get(i)){
                 				absAddr = addr.get(moduleArray.get(i) + j).getAddress() / 1000 * 1000;
                 				//
                 				addr.get(moduleArray.get(i) + j).setAddress(absAddr);
                 				addr.get(moduleArray.get(i) + j).setExceedModule(true);
                 			}
                 			addr.get(moduleArray.get(i) + j).setAddress(absAddr);
                 		}
                 		else{
                 			src.nextInt();
                 		}
                 		if(type.equals("R") || type.equals("A")){
                 			if(addr.get(moduleArray.get(i) + j).getAddress() % 1000 >= 200){
                 				absAddr = addr.get(moduleArray.get(i) + j).getAddress() / 1000 * 1000;
                 				addr.get(moduleArray.get(i) + j).setAddress(absAddr);
                 				addr.get(moduleArray.get(i) + j).setExceedMachine(true);
                 			}
                 			
                 			
                 		}
            			System.out.print(count + ": ");
            			if(count < 10){
            				System.out.print(" ");
            			}
            			System.out.print(addr.get(count).getAddress());
            			if(addr.get(count).getNoExist() != null){
            				System.out.println(" Error: " + addr.get(count).getNoExist().getName() + " is not defined; zero used.");
            			}
            			if(addr.get(count).getUsedTimes() > 1){
            				System.out.print(" Error: Multiple variables used in instruction; all but first ignored.");
            			}
            			if(type.equals("A") && addr.get(count).isExceedMachine()){
	            			System.out.println(" Error: Absolute address exceeds machine size; zero used.");
            			}
            			else if(type.equals("R") && addr.get(count).isExceedModule()){
            				System.out.println(" Error: Relative address exceeds module size; zero used.");
            			}
            			else{
            				if(addr.get(count).getNoExist() == null){
            					System.out.println();
            				}
            			}
            			
            			count++;
		            }	
            	}
            }
           
            
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void main(String args[]){
		File fileName = new File("input-1.txt");
            // FileReader reads text files in the default encoding.
		passOne(fileName);
        System.out.println("Symbol Table");
        for(int i = 0; i < sym.size(); i++){
    		System.out.print(sym.get(i).getName() + "=" + sym.get(i).getValue());
    		if(sym.get(i).isMultDef() || sym.get(i).isDefExceedsModSize()){
    			if(sym.get(i).isMultDef())
    				System.out.print(" Error: This variable is multiply defined;"
    					+ " first value used. ");
    			if(sym.get(i).isDefExceedsModSize()){
    				System.out.print(" Error: Definition exceeds module size;"
    						+ " first word in module used.");
    			}
    			System.out.println();
    		} else {
    			System.out.println();
    		}
        }
		System.out.println();
		System.out.println("Memory Map");
        passTwo(fileName);
		System.out.println();
		for(int i = 0; i < sym.size(); i++){
			if(sym.get(i).isUseExceedsModSize() == true)
				System.out.println("Error: Use of "+ sym.get(i).getName() + " in module " + sym.get(i).getDefinedIn() + " exceeds module size; use ignored");
			if(!sym.get(i).isUsed()){
				System.out.println("Warning: " + sym.get(i).getName()
						+ " was defined in module " + sym.get(i).getDefinedIn() + " but never used.");
			}
		}
		
	
	}
}
