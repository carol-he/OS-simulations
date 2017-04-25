
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
//ƒimport static javafx.scene.input.KeyCode.T;
public class Scheduling {
    static Scanner randomSrc;
    static int IOburst ;  
    static String type;
    static int RRnumburst=2;//****<1><1> for RR
    static Queue<Process> pq = null;
    static Stack<Process> ps = null; //****<1><1>for LCFS
    static int rrFlag = -1;
    public static int randomOS(int U){
        //File fileName = new File("random-numbers.txt");
        //Scanner src;
        //try {
        //src = new Scanner(fileName);
        int X = randomSrc.nextInt();
        return (1 + (X % U));
        //} catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        //	e.printStackTrace();
        //}
        //System.out.println("Random-numbers file not found");
        //return 0;
    }   
    public static String runningState(Process process){
        //subtract CPU time left
        Cycle cycle;
        process.getCopy()[2] = process.getCopy()[2] - 1;
        //calculate running burst using randomOS, if it's the first run
        if(process.getCopy()[1] == 0){
            process.getCopy()[1] = randomOS(process.getABCIO()[1]);                       
        }               
        cycle = new Cycle("    running", process.getCopy()[1]);            
        //subtract running burst left                   
        process.getCopy()[1]--;                
        process.getCycles().add(cycle);
        // if its now 0, change the state to ready or something
        // change
        if(process.getCopy()[1] == 0 || process.getCopy()[2] == 0 ){
            process.setState("    blocked");
            return "false";
        }
        return "true";
    }
    public static boolean blockedState(Process process){
    	//calculate IO burst with randomOS if it's the first 1
        if(process.getCopy()[3] == 0){
            process.getCopy()[3] = IOburst;  //change
        }
        Cycle cycle = new Cycle("    blocked", process.getCopy()[3]);
        //subtract remained burst left
        process.getCopy()[3]--;
        process.getCycles().add(cycle);
        process.setIOTime(process.getIOTime()+1); //set I/O Time
        // if its now 0, change the state to ready or something
        //change
        if(process.getCopy()[3] == 0 && process.getCopy()[1] == 0){
            process.setState("      ready");
            return false;
        }
        return true;
    }
    public static void readyState(Process process){
    	Cycle cycle;
    	if(rrFlag > -1){
    		cycle = new Cycle("      ready", rrFlag);
    		rrFlag = -1;
    	} else {
    		cycle = new Cycle("      ready", process.getCopy()[1]);
    	}
		process.getCycles().add(cycle);
        process.setState("      ready");
        process.setWaitingTime(process.getWaitingTime() + 1);
    }
    public static void unstartedState(Process process, int frameNum){
    	//create cycle for each time this function is called
    	//to add a cycle you need state, remaining burst, and random used
    	Cycle cycle = new Cycle("  unstarted", 0);
		process.getCycles().add(cycle);
        process.setStartingTime(process.getStartingTime()+1); //set start time
    }	
    public static void terminatedState(Process process){
    	Cycle cycle = new Cycle(" terminated", 0);
    	process.getCycles().add(cycle); 
        //set FinishingTime                    
        if (process.getFinishingTime() == 0){
        	process.setFinishingTime(process.getCycles().size()-2);
        }
    }	
    public static void SCFSschdule (String printType, Process[] processes) {
        pq = new LinkedList<Process>();               
		boolean allTerminated = false;
		int frameNumber = 0;		
		boolean rem = false; //running continue
		//loop while all processes aren't terminated
		while(allTerminated == false){
            //change calculate IOburst time
            for(int i = 0; i < processes.length; i++){
                if(processes[i].getCopy()[3] == 0 && processes[i].getState() == "    blocked" && processes[i].getCopy()[2] != 0){
                    IOburst = randomOS(processes[i].getABCIO()[3]);                              
                }
            }
            //loop through all processes to add a cycle
            for(int i = 0; i < processes.length; i++){
            	//if arrival time is not 0 yet, make it unstarted
            	if(processes[i].getCopy()[0] != -1){
                    unstartedState(processes[i], frameNumber);
                    processes[i].setState("  unstarted");
                    processes[i].getCopy()[0]--;
            	}
                else if(processes[i].getCopy()[2] != 0){
                    //if process is not running or blocked, add to priority queue
                    //change
                    if(processes[i].getState() == "      ready" ||( processes[i].getState() == "  unstarted" && processes[i].getCopy()[0] == -1) ){
                        if(!(pq.contains(processes[i]))){
                            pq.add(processes[i]);
                        }
                        if(processes[i].getState() == "  unstarted"){
                            processes[i].setState("      ready");
                        }
                    }
                    //if blocked, do blocked state
                    if(processes[i].getState() == "    blocked"){
                    	blockedState(processes[i]);
                    }
                    //if head of queue is not current process, go to ready state
                    else if(!pq.isEmpty() && pq.peek() != processes[i]){
                    	readyState(processes[i]);			
                    }						
                    else{
                        //chang                                                   
                        if(!pq.isEmpty() && pq.peek() == processes[i]) {
                        //System.out.println("TEST");
                            rem = false;
                            if(runningState(processes[i]).equals("false")){
                            //remove the head once running has finished
                            //this is fine except more than one can run at once
                                rem = true;
                            //if i remove it, it will only account for 2 processes at a time?
                            }
                        }
                    }
                }
				//terminate process
                else{
                    //System.out.println("TEST3");
                    processes[i].setState(" terminated");                 
                    terminatedState(processes[i]);
                }
            }
            //if another process already in the queue, make it ready
            //check if they're all terminated
            boolean allTerm = true;
            for(int i = 0; i < processes.length; i++){
				//if one isn't terminated, set to false
				if(processes[i].getState() != " terminated"){
					allTerm = false;
				}
			}
            if(allTerm == true){
            	allTerminated = true;
            }
            //change
            if(rem == true && !pq.isEmpty()){
            	pq.remove();
            	rem = false;
            	//  System.out.println("test");
            }
            frameNumber++;
		}
        if(printType.equals("-verbose"))
	    printDetailed(processes);	
        printOutput(processes);
    }       
    public static void RRschdule (String printType, Process[] processes) {
        pq = new LinkedList<Process>();
        boolean allTerminated = false;
        int frameNumber = 0;		
        boolean rem = false;
        //loop while all processes aren't terminated
        while(allTerminated == false){
            //change
            for(int i = 0; i < processes.length; i++){
                if(processes[i].getCopy()[3] == 0 && processes[i].getState() == "    blocked" && processes[i].getCopy()[2] != 0){
                    
                	IOburst = randomOS(processes[i].getABCIO()[3]);                              
                }
            }
            //loop through all processes to add a cycle
            for(int i = 0; i < processes.length; i++){
            //if arrival time is not 0 yet, make it unstarted
            	if(processes[i].getCopy()[0] != -1){
                    unstartedState(processes[i], frameNumber);
                    processes[i].setState("   unstarted");
                    processes[i].getCopy()[0]--;
            	}
            	else if(processes[i].getCopy()[2] != 0){
                    //if process is not running or blocked, add to priority queue
                    //change
                    if(processes[i].getState() == "      ready" ||( processes[i].getState() == "   unstarted" && processes[i].getCopy()[0] == -1) ){
                    	if(!(pq.contains(processes[i]))){
                            pq.add(processes[i]);                                                     
                    	}
                    	if(processes[i].getState() == "   unstarted"){
                            processes[i].setState("      ready");
                    	}
                    }
                    //if blocked, do blocked state
                    if(processes[i].getState() == "    blocked"){
                    	blockedState(processes[i]);
                    }
                    //if head of queue is not current process, go to ready state
                    //chang
                    else if(!pq.isEmpty() && pq.peek() != processes[i]){
                    	int whatever = processes[i].getCycles().get(frameNumber - 1).getRandomUsed();
                    	if(whatever == 0 || whatever == 1){
                    		rrFlag = 0;
                    	}
                    	else if(whatever == 2){
                    		rrFlag = 1;
                    	}
                        readyState(processes[i]);                              
                    }						
                    else if(!pq.isEmpty() && pq.peek() == processes[i]) {
                        //System.out.println("TEST");								
                        rem = false;
                        if(runningState(processes[i]) == "false"){
			//remove the head once running has finished
			//this is fine except more than one can run at once			      
                        rem = true;
                        processes[i].getCycles().get(processes[i].getCycles().size()-1).setRandomUsed((RRnumburst));
                        RRnumburst = 2; //****<2><2>for type RR
                        //if i remove it, it will only account for 2 processes at a time		
                        }  //for type RR
                        else if (RRnumburst== 1 && pq.peek() == processes[i]){  //****<3>fore RR stop running
                            //processes[i].setState("      ready"); 
                            rem =true;
                            processes[i].getCycles().get(processes[i].getCycles().size()-1).setRandomUsed((RRnumburst));
                            RRnumburst = 2;
                        }
                        else{
                        	 processes[i].getCycles().get(processes[i].getCycles().size()-1).setRandomUsed((RRnumburst));
                            RRnumburst--;                                                                                                         
                        } //****<3> end RR  
                        //processes[i].getCycles().get(processes[i].getCycles().size()-1).setRandomUsed((RRnumburst % 2 )+1);
                    }          
                }
		//terminate process
                else{
                    processes[i].setState(" terminated");
                    terminatedState(processes[i]);
                }
            }
            //if another process already in the queue, make it ready
			//check if they're all terminated
            boolean allTerm = true;
            for(int i = 0; i < processes.length; i++){
            //if one isn't terminated, set to false
                if(processes[i].getState() != " terminated"){
                    allTerm = false;
                }
            }
            if(allTerm == true){
                allTerminated = true;
            }
            //change                        
            if(rem == true && !pq.isEmpty()){
                pq.remove();                           
            }
            frameNumber++;
        }
        for(int i =0; i< processes.length; i++){	
        	for(int j = 0; j < processes[i].getCycles().size()-1; j++){			
        		if(processes[i].getCycles().get(j).getState() == "    running" && processes[i].getCycles().get(j).getRemainingBurst() > processes[i].getCycles().get(j).getRandomUsed())			
        			processes[i].getCycles().get(j).setRemainingBurst(processes[i].getCycles().get(j).getRandomUsed());			
        	}			  			
        }			      
	if(printType.equals("-verbose"))
	    printDetailed( processes);	
        printOutput(processes);		
     }
    public static void LCFSschdule (String printType, Process[] processes) {                   
        ps = new Stack<Process>(); //****<2><2>change for LCFS	
        boolean allTerminated = false;
        int frameNumber = 0;				                                           
        boolean rem = false;        
        //loop while all processes aren't terminated
        while(allTerminated == false){  
        	Stack<Process> ts = new Stack<Process>();    
            //change IOburst
            for(int i = 0; i < processes.length; i++){
                if(processes[i].getCopy()[3] == 0 && processes[i].getState() == "    blocked" && processes[i].getCopy()[2] != 0){
                    IOburst = randomOS(processes[i].getABCIO()[3]);                              
                }
            } //put ready in stack 
            for(int i = 0; i < processes.length; i++){
                if((processes[i].getState() == "   unstarted" && processes[i].getCopy()[0] == -1)|| (processes[i].getState() == "      ready" && processes[i].getCycles().get(processes[i].getCycles().size()-1).getState()=="    blocked")){
                    if(processes[i].getState() == "   unstarted")
                       processes[i].setState("      ready"); 
                    ts.push(processes[i]);               
                }
            }
            while(!ts.empty()){
                if(!ps.isEmpty() && ps.peek().getCycles().get(ps.peek().getCycles().size()-1).getState() == "    running")
                    ps.insertElementAt(ts.pop(),ps.size()-1);
                else
                    ps.push(ts.pop());
            }
            
            //loop through all processes to add a cycle
            for(int i = 0; i < processes.length; i++){
             //   int index =-1;
                //if arrival time is not 0 yet, make it unstarted
            	if(processes[i].getCopy()[0] != -1){
                    unstartedState(processes[i], frameNumber);
                    processes[i].setState("   unstarted");
                    processes[i].getCopy()[0]--;
                }                
                else if(processes[i].getCopy()[2] != 0){                    
                
                    //if blocked, do blocked state
                    if(processes[i].getState() == "    blocked"){
                        blockedState(processes[i]);
                    }
                    //if head of queue is not current process, go to ready stage
                    //chang
                    else if(!ps.isEmpty() && ps.peek() != processes[i]){ //****<4><4> for LCFS
                        readyState(processes[i]);                        
                    }                                                                                                   
                    if(!ps.isEmpty() && ps.peek() == processes[i]) {   //****<5><5> for LCFS
                        //System.out.println("TEST");
                        rem = false;
                        if(runningState(processes[i]).equals("false")){
                            //remove the head once running has finished
                         //this is fine except more than one can run at once									
                         rem = true;
                        //if i remove it, it will only account for 2 processes at a time?
                        }
                    }
                }					
            	//terminate process
                else{
                	//System.out.println("TEST3");
                    processes[i].setState(" terminated");
                    terminatedState(processes[i]);
                }
            }
            //if another process already in the queue, make it ready
            //check if they're all terminated
            boolean allTerm = true;
            for(int i = 0; i < processes.length; i++){
            	//if one isn't terminated, set to false
            	if(processes[i].getState() != " terminated"){
                    allTerm = false;
            	}
            }
            if(allTerm == true){
            	allTerminated = true;
            }
                //change                       
                //  System.out.println("test")
                if(rem == true && !ps.isEmpty()){ //****<7><7> for LCFS
                    ps.pop(); //****<6><6> for LCFS
                    rem = false;
                //  System.out.println("test");
                }
            frameNumber++;
        }
        if(printType.equals("-verbose"))
	    printDetailed(processes);	
        printOutput(processes);             
      }    
    public static void PSJFschdule (String printType, Process[] processes) {
                              
        pq = new LinkedList<Process>();                
	boolean rem = false;
        boolean allTerminated = false;
	int frameNumber = 0;
	//loop while all processes aren't terminated
	while(allTerminated == false){
        //change
        for(int i = 0; i < processes.length; i++){
            if(processes[i].getCopy()[3] == 0 && processes[i].getState() == "    blocked" && processes[i].getCopy()[2] != 0){
                IOburst = randomOS(processes[i].getABCIO()[3]);                              
            }
        }
        for(int i = 0; i < processes.length; i++){
            processes[i].setcsequence(i);
        }
        for(int i = 0; i < processes.length; i++){
        	if((processes[i].getState() == "   unstarted" && processes[i].getCopy()[0] == -1)|| (processes[i].getState() == "      ready" && processes[i].getCycles().get(processes[i].getCycles().size()-1).getState()=="    blocked")){
        		if(processes[i].getState() == "   unstarted")
        			processes[i].setState("      ready");    
                    pq.add(processes[i]);                     
        	}
        }
         //Use compareto in Process to sort
         Collections.sort((List<Process>) pq,Process.SequenceComparator);    
         Collections.sort((List<Process>) pq,Process.TimeComparator); 
         for(int i = 0; i < processes.length; i++){
            //if arrival time is not 0 yet, make it unstarted
            if(processes[i].getCopy()[0] != -1){
            	unstartedState(processes[i], frameNumber);
            	processes[i].setState("   unstarted");
            	processes[i].getCopy()[0]--;
            }
            else if(processes[i].getCopy()[2] != 0){
	            //if blocked, do blocked state
	            if(processes[i].getState() == "    blocked"){
	            	blockedState(processes[i]);
	            }
	            //if head of queue is not current process, go to ready stage
	            //chang                                                
	            else if(!pq.isEmpty() && pq.peek() != processes[i]){
//	            	if(processes[i].getCopy()[1] == 1){
//                		zeroFlag = true;
//                	}
	                readyState(processes[i]);
	            }                                                                                     						
	            else if(!pq.isEmpty() && pq.peek() == processes[i]) {
	                rem = false;
	                if(runningState(processes[i]).equals("false")){
	                    //remove the head once running has finished
	                    //this is fine except more than one can run at once									
	                    rem = true;								
	                }
	            }		    
            }
                //terminate process
            else{
            	processes[i].setState(" terminated");
            	terminatedState(processes[i]);
            }
         }
            //if another process already in the queue, make it ready
            //check if they're all terminated
            boolean allTerm = true;
            for(int i = 0; i < processes.length; i++){
		//if one isn't terminated, set to false
		if(processes[i].getState() != " terminated"){
			allTerm = false;
		}
            }
            if(allTerm == true){
            	allTerminated = true;
            }
            //change
            if(rem == true && !pq.isEmpty()){
                pq.remove();
                rem = false;
            }
		frameNumber++;
	}
       if(printType.equals("-verbose"))
	    printDetailed(processes);	
        printOutput(processes);	
    }       
    public static void main(String[] args) throws FileNotFoundException{
    	Scanner reader = new Scanner(System.in);  // Reading from System.in
    	type = "";
    	while(!(type.equals("FCFS")) && !(type.equals("RR")) && !(type.equals("LCFS")) && !(type.equals("PSJF"))){
    		System.out.print("Which algorithm are you using? (FCFS, RR, LCFS, PSJF): ");
        	type = reader.next();
        	type = type.toUpperCase();
    	}
    	reader.close(); 
        String name;
        String printType = "o";
       if (args[0].equals("--verbose")){
            printType = "-verbose";
            name = args[1];
        } else {
            name = args[0];
        }
        
	File fileName = new File(name);
	Scanner src = new Scanner(fileName);
        //change
        File randomFileName = new File("random-numbers.txt");
        randomSrc = new Scanner(randomFileName);     
	Process processes[] = new Process[src.nextInt()]; 
        //get all these things
	for(int i = 0; i < processes.length; i++){
            int[] arr = new int[4];
            for(int j = 0; j <= 3; j++){
		arr[j] = src.nextInt();
            }
            processes[i] = new Process() {};
            processes[i].setABCIO(arr);
	//Copy is set to A 0 C 0
	//System.out.println(processes[i].getABCIO()[0] + " " + processes[i].getABCIO()[1] + " " +  processes[i].getABCIO()[2] + " " + processes[i].getABCIO()[3]);
            int[] arr2 = new int[4];
            for(int j = 0; j < arr.length; j++){
                arr2[j] = arr[j];
            }
            processes[i].setCopy(arr2);
            processes[i].getCopy()[1] = 0;
            processes[i].getCopy()[3] = 0;        
	}
        printInput("The original input was: " ,processes);
        Arrays.sort(processes);//Use compareto in Process to sort
        printInput("The (sorted) input is : ", processes);
        if (type.equals("FCFS"))
            SCFSschdule (printType, processes);
        if (type.equals("RR"))
            RRschdule (printType, processes);
        if (type.equals("LCFS"))
            LCFSschdule (printType, processes);
        if (type.equals("PSJF"))
            PSJFschdule (printType, processes);
    }    
    public static void printInput(String s,Process[] processes){
        System.out.print("\n"+s + processes.length +"  ");
        for(int i = 0; i < processes.length ; i++){
            for(int j= 0; j < processes[i].getABCIO().length ; j++){
		System.out.print(processes[i].getABCIO()[j] + " ");
            }
           System.out.print("  ");  
        }    
    }
    public static void printDetailed(Process[] processes){
	    System.out.println("\nThis detailed printout gives the state and remaining burst for each process\n");
	    for(int i = 0; i < processes[0].getCycles().size() - 1; i++){
	        System.out.print("Before cycle ");
	        if(i < 10){
	        	System.out.print("   ");
	        }
	        else if(i < 100){
	        	System.out.print("  ");
	        }
	        else if(i < 1000){
	        	System.out.print(" ");
	        }
	        System.out.print( i + ": ");
	        for(int j = 0; j < processes.length; j++){
	            System.out.print(processes[j].getCycles().get(i).getState() + "  " + processes[j].getCycles().get(i).getRemainingBurst());
	        }
			System.out.println(".");
		}
    }
    public static void printOutput(Process[] processes){
    	System.out.print("The scheduling algorithm used was "+ type +"\n\n" );
        for(int i = 0; i < processes.length; i++){
            System.out.println("Process " + i + ":" );
            System.out.println("\t(A,B,C,IO) = "+ Arrays.toString(processes[i].getABCIO()));
            System.out.println("\tFinishing time: " + processes[i].getFinishingTime());
            System.out.println("\tTurnaround time: "+ (processes[i].getFinishingTime()-processes[i].getStartingTime()));
            System.out.println("\tI/O time: " + processes[i].getIOTime());
            System.out.println("\tWaiting time: "+processes[i].getWaitingTime() + "\n");                    
        }    
        System.out.println("Summary Data:");  
        System.out.println("\tFinishing time: " +(processes[0].getCycles().size()-2));
        float tot = 0;
        for(int i = 0; i < processes.length; i++){
            tot += processes[i].getABCIO()[2];    
        }
        System.out.println("\tCPU Utilization: " + (tot /(processes[0].getCycles().size()-2) ));
        tot = 0;
        for(int i = 0; i < processes.length; i++){
            tot += processes[i].getIOTime();    
        }
        tot = 0;//get total blocked time. one cycle count 1s
        for(int i = 0; i < processes[0].getCycles().size()-1; i++){
            for(int j = 0; j < processes.length; j++){
                if(processes[j].getCycles().get(i).getState() == "    blocked"){
                    tot ++; 
                    j = processes.length;
                }    
            }
        }
        System.out.println("\tI/O Utilization: " + ( tot /(processes[0].getCycles().size()-2)));
        System.out.println("\tThroughput: " + (((float)processes.length)/(processes[0].getCycles().size()-2)*100) + " processes per hundred cycles" );
        tot =0;
        for(int i = 0; i < processes.length; i++){
            tot += (processes[i].getFinishingTime() - processes[i].getStartingTime());  
        }
        System.out.println("\tAverage turnaround time: " + tot/processes.length);
        tot =0;
        for(int i = 0; i < processes.length; i++){
            tot += (processes[i].getWaitingTime());  
        }
        System.out.println("\tAverage waiting time: "+ tot/processes.length);
    }
}


