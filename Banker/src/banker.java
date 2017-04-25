import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class banker {

	/**
	 * performs optimistic manager with given data.
	 * @param tasks
	 * @param resources
	 */
    public static void optimisticManager(Task[] tasks, int[] resources){
    	int[][] allocated = new int[tasks.length][resources.length];
    	int[] available = new int[resources.length];
    	int[] updatedAvailable = new int[resources.length];
    	available = Arrays.copyOf(resources, resources.length);
    	updatedAvailable = Arrays.copyOf(resources, resources.length);
    	int finished = 0;
    	int time = 0;
    	int instructionNumber = 0;
    	String activity = "";
    	int delay = 0;
    	int rt = 0;
    	int needed = 0;
    	//keep track of blocked and not blocked tasks
    	Queue<Task> blocked = new LinkedList<Task>();
    	//at first all tasks are not blocked
    	int numWaiting = 0; 
    	while(finished < tasks.length){
    		//go through the blocked tasks if there are any
    		if(blocked.size() > 0){
    			int k = blocked.size();
    			for(int i = 0; i < k; i++){
    				int ind = 0;
        			activity = blocked.peek().getInstructions().get(0).getActivity();
        			delay = blocked.peek().getInstructions().get(0).getDelay();
        			rt = blocked.peek().getInstructions().get(0).getResourceType() - 1;
        			needed = blocked.peek().getInstructions().get(0).getNumNeeded();
        			for(int j = 0; j < tasks.length; j++){
        				if(tasks[j] == blocked.peek()){
        					ind = j;
        				}
        			}
        			if(blocked.peek().isAborted()){
        				//do nothing
        				blocked.remove();
        			}
        			else if (activity.equals("request")){
        				//check if request can be made. if it can do it.
        				if(needed <= available[rt]){
        					//System.out.println("hello from task " + i);
        					//success!
        					//decrement availability of that resource
        					available[rt] = available[rt] - needed;
        					updatedAvailable[rt] = updatedAvailable[rt] - needed;
        					//increment allocated for task of rt
        					allocated[ind][rt] = allocated[ind][rt] + needed;
        					//instruction finished-remove it
        					blocked.peek().getInstructions().remove(0);
        					//not blocked anymore
        					blocked.peek().setBlocked(1);
        					numWaiting--;
        					blocked.remove();
        				}
        				else{
        					//System.out.println("hello from task " + i);
        					//increment waiting time no matter what
        					blocked.peek().setWaitingTime(blocked.peek().getWaitingTime() + 1);
        					blocked.add(blocked.remove());
        				}
        			}
    			}
    		}
    		//looping through all tasks, skipping the blocked ones
    		for(int i = 0; i < tasks.length; i++){
    			if(tasks[i].getInstructions().size() > 0){
        			activity = tasks[i].getInstructions().get(0).getActivity();
        			delay = tasks[i].getInstructions().get(0).getDelay();
        			rt = tasks[i].getInstructions().get(0).getResourceType() - 1;
        			needed = tasks[i].getInstructions().get(0).getNumNeeded();
        			//if the task is blocked or aborted don't do anything
        			if(tasks[i].getBlocked() != 0 || tasks[i].isAborted()){
        				//do nothing
        			}
        			//first check for delay
        			else if(delay != 0){
        				tasks[i].getInstructions().get(0).setDelay(delay - 1);
        			}
        			else if(activity.equals("initiate")){
        				//instruction finished-remove it
        				tasks[i].getInstructions().remove(0);
        			}
        			else if (activity.equals("request")){
        				//check if request can be made. if it can do it.
        				if(needed <= available[rt]){
        					//System.out.println("hello from task " + i);
        					//success!
        					//decrement availability of that resource
        					available[rt] = available[rt] - needed;
        					updatedAvailable[rt] = updatedAvailable[rt] - needed;
        					//increment allocated for task of rt
        					allocated[i][rt] = allocated[i][rt] + needed;
        					//instruction finished-remove it
        					tasks[i].getInstructions().remove(0);
        				}
        				else{
        					//System.out.println("hello from task " + i);
        					numWaiting++;
        					//increment waiting time no matter what
    						tasks[i].setWaitingTime(tasks[i].getWaitingTime() + 1);
    						//checking for deadlock
        					if(numWaiting == tasks.length){
        						//deadlocked, must abort tasks
        						int j = 0;
        						//while the next lowest task cannot be fulfilled, abort
        						while(tasks[j].getInstructions().get(instructionNumber).getNumNeeded() > available[rt]){
        							//abort, give back resources
        							available[rt] = available[rt] + allocated[j][rt];
        							updatedAvailable[rt] = updatedAvailable[rt] + allocated[j][rt];
        							allocated[j][rt] = 0;
        							tasks[j].setAborted(true);
        							finished++;
        							j++;
        							while(tasks[j].getInstructions().size() == 0){
        								j++;
        							}
        						}
        					}
        					if(!tasks[i].isAborted()){
        						tasks[i].setBlocked(2);
        						blocked.add(tasks[i]);
        						
        					}
        				}
        			}
        			else if (activity.equals("release")){
        				//give back num of resources
        				allocated[i][rt] = allocated[i][rt] - needed;
        				updatedAvailable[rt] = updatedAvailable[rt] + needed;
        				tasks[i].getInstructions().remove(0);
        			}
        			else if (activity.equals("terminate")){
        				tasks[i].setTimeTaken(time);
        				tasks[i].getInstructions().remove(0);
        				finished++;
        				numWaiting++;
        			}
        			if(tasks[i].getBlocked() == 1){
        				tasks[i].setBlocked(0);
        			}
    			}
    		}
    		//resources are now actually available
    		available = Arrays.copyOf(updatedAvailable, updatedAvailable.length);
    		time++;
    	}
    }
   
    /**
     * calculates the "needed" matrix, which is just claimed-allocated for each element in the matrix
     * @param numtasks
     * @param numresources
     * @param need
     * @param claimed
     * @param allocated
     * @return
     */
    private static int[][] calculateNeeded(int numtasks, int numresources, int[][] need, int[][] claimed, int[][] allocated){
    	//calculate need matrix (claims - allocated)
        for(int i = 0; i < numtasks; i++){
        	for(int j = 0; j < numresources; j++){ 
                need[i][j] = claimed[i][j] - allocated[i][j];
        	}
        }
        return need;
     }
    /**
     * Checks for safety
     * @param available
     * @param need
     * @param numresources
     * @param i
     * @return
     */
    private static boolean isSafe(int[] available, int[][] need, int numresources, int i){
        //checking if all resources for ith process can be allocated
        for(int j = 0; j < numresources; j++) {
        	//if the available resources are less than claims-allocated, it's not safe
        	if(available[j] < need[i][j]){
            	return false;
            }
        }
     return true;
     }
    
    /**
     * function that does a naive implementation of bankersAlgorithm
     * @param tasks
     * @param resources
     */
    public static void bankersAlgorithm(Task[] tasks, int[] resources){
    	int[][] allocated = new int[tasks.length][resources.length];
    	int[][] claimed = new int[tasks.length][resources.length];
    	int[][] need = new int[tasks.length][resources.length];
    	int[] available = new int[resources.length];
    	int[] updatedAvailable = new int[resources.length];
    	available = Arrays.copyOf(resources, resources.length);
    	updatedAvailable = Arrays.copyOf(resources, resources.length);
    	int finished = 0;
    	int time = 0;
    	int instructionNumber = 0;
    	String activity = "";
    	int delay = 0;
    	int rt = 0;
    	int needed = 0;
    	//keep track of blocked and not blocked tasks
    	Queue<Task> blocked = new LinkedList<Task>();
    	//at figst all tasks are not blocked
    	int numWaiting = 0; 
    	while(finished < tasks.length){
    		//for detecting deadlock after its happened
    		if(blocked.size() > 0){
    			int k = blocked.size();
    			for(int i = 0; i < k; i++){
    				int ind = 0;
        			activity = blocked.peek().getInstructions().get(0).getActivity();
        			delay = blocked.peek().getInstructions().get(0).getDelay();
        			rt = blocked.peek().getInstructions().get(0).getResourceType() - 1;
        			needed = blocked.peek().getInstructions().get(0).getNumNeeded();
        			for(int j = 0; j < tasks.length; j++){
        				if(tasks[j] == blocked.peek()){
        					ind = j;
        				}
        			}
        			if(blocked.peek().isAborted()){
        				//do nothing
        				blocked.remove();
        			}
        			else if (activity.equals("request")){
        				//check if request can be made. if it can do it.
        				calculateNeeded(tasks.length, resources.length, need, claimed, allocated);
        				//checking for safety. if it is, unblock the task
        				if(isSafe(available, need, resources.length, ind)){
        					//System.out.println("hello from task " + i);
        					//success!
        					//decrement availability of that resource
        					available[rt] = available[rt] - needed;
        					updatedAvailable[rt] = updatedAvailable[rt] - needed;
        					//increment allocated for task of rt
        					allocated[ind][rt] = allocated[ind][rt] + needed;
        					//instruction finished-remove it
        					blocked.peek().getInstructions().remove(0);
        					//not blocked anymore
        					blocked.peek().setBlocked(1);
        					numWaiting--;
        					blocked.remove();
        				}
        				else{
        					//System.out.println("hello from task " + i);
        					//increment waiting time no matter what
        					blocked.peek().setWaitingTime(blocked.peek().getWaitingTime() + 1);
        					blocked.add(blocked.remove());
        				}
        			}
    			}
    		}
    		//loop through the actual tasks
    		for(int i = 0; i < tasks.length; i++){
    			if(tasks[i].getInstructions().size() > 0){
        			activity = tasks[i].getInstructions().get(0).getActivity();
        			delay = tasks[i].getInstructions().get(0).getDelay();
        			rt = tasks[i].getInstructions().get(0).getResourceType() - 1;
        			needed = tasks[i].getInstructions().get(0).getNumNeeded();
        			//if number of blocked processes is greater
        			if(tasks[i].getBlocked() != 0 || tasks[i].isAborted()){
        				//do nothing
        			}
        			//first check for delay
        			else if(delay != 0){
        				tasks[i].getInstructions().get(0).setDelay(delay - 1);
        			}
        			else if(activity.equals("initiate")){
        				if(needed > available[rt]){
        					tasks[i].setAborted(true);
        					finished++;
        				}
        				else{
        					//instruction finished-remove it
            				claimed[i][rt] = claimed[i][rt] + needed;
            				tasks[i].getInstructions().remove(0);
        				}
        				
        			}
        			else if (activity.equals("request")){
        				//check if request can be made. if it can do it.
        				calculateNeeded(tasks.length, resources.length, need, claimed, allocated);
        				//if request exceeds claim, abort and give back resources
        				if((allocated[i][rt] + needed) > (claimed[i][rt])){
        					//abort, give back resources
							available[rt] = available[rt] + allocated[i][rt];
							updatedAvailable[rt] = updatedAvailable[rt] + allocated[i][rt];
							allocated[i][rt] = 0;
							tasks[i].setAborted(true);
							finished++;
        				}
        				//checking for safety
        				else if(isSafe(available, need, resources.length, i)){
        					//System.out.println("hello from task " + i);
        					//success!
        					//decrement availability of that resource
        					available[rt] = available[rt] - needed;
        					updatedAvailable[rt] = updatedAvailable[rt] - needed;
        					//increment allocated for task of rt
        					allocated[i][rt] = allocated[i][rt] + needed;
        					//instruction finished-remove it
        					tasks[i].getInstructions().remove(0);
        				}
        				else{
        					//System.out.println("hello from task " + i);
        					numWaiting++;
        					//increment waiting time no matter what
    						tasks[i].setWaitingTime(tasks[i].getWaitingTime() + 1);
    						//checking for deadlock
        					if(numWaiting == tasks.length){
        						//deadlocked, must abort tasks
        						int j = 0;
        						//while the next lowest task cannot be fulfilled, abort
        						while(tasks[j].getInstructions().get(instructionNumber).getNumNeeded() > available[rt]){
        							//abort, give back resources
        							available[rt] = available[rt] + allocated[j][rt];
        							updatedAvailable[rt] = updatedAvailable[rt] + allocated[j][rt];
        							allocated[j][rt] = 0;
        							tasks[j].setAborted(true);
        							finished++;
        							j++;
        							while(tasks[j].getInstructions().size() == 0){
        								j++;
        							}
        						}
        					}
        					if(!tasks[i].isAborted()){
        						tasks[i].setBlocked(2);
        						blocked.add(tasks[i]);
        						
        					}
        				}
        			}
        			else if (activity.equals("release")){
        				//give back num of resources
        				allocated[i][rt] = allocated[i][rt] - needed;
        				updatedAvailable[rt] = updatedAvailable[rt] + needed;
        				tasks[i].getInstructions().remove(0);
        			}
        			else if (activity.equals("terminate")){
        				tasks[i].setTimeTaken(time);
        				tasks[i].getInstructions().remove(0);
        				finished++;
        				numWaiting++;
        			}
        			if(tasks[i].getBlocked() == 1){
        				tasks[i].setBlocked(0);
        			}
    			}
    		}
    		//resources r now actually available
    		available = Arrays.copyOf(updatedAvailable, updatedAvailable.length);
    		time++;
    	}
    }
    
    /**
     * main function
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException{
    	Scanner reader = new Scanner(System.in);  // Reading from System.in
    	reader.close(); 
        String name = args[0];
		File fileName = new File(name);
		Scanner src = new Scanner(fileName);
		//declare array of tasks
		Task[] tasks = new Task[src.nextInt()];
		Task[] tasks2 = new Task[tasks.length];
		//declare array of resources. index + 1 is the resource number, value is # of resources
	    int[] resources = new int[src.nextInt()];
	    int[] resources2 = new int[resources.length];
	    for(int i = 0; i < resources.length; i++){
	    	resources[i] = src.nextInt();
	    	resources2[i] = resources[i];
	    }
	    //getting tasks :^)
	    for(int i = 0; i < tasks.length; i++){
	    	//each task has an arraylist of instructs
	    	ArrayList<Instruction> instructs = new ArrayList<Instruction>();
	    	ArrayList<Instruction> instructs2 = new ArrayList<Instruction>();
	    	tasks[i] = new Task(0, 0, false, 0, instructs);
	    	tasks2[i] = new Task(0, 0, false, 0, instructs2);
	    }
	    	int currentIndex;
	    	while(src.hasNext()){
	    		//while the next activity is not terminate
	    		//create instruction object, put in all the parameters
	    		Instruction instr = new Instruction("", 0, 0, 0);
	    		Instruction instr2 = new Instruction("", 0, 0, 0);
	    		String activity = src.next();
	    		currentIndex = src.nextInt() - 1;
	    		int delay = src.nextInt();
	    		int rt = src.nextInt();
	    		int needed = src.nextInt();
	    		instr.setActivity(activity);
	    		instr.setDelay(delay);
	    		instr.setResourceType(rt);
	    		instr.setNumNeeded(needed);
	    		instr2.setActivity(activity);
	    		instr2.setDelay(delay);
	    		instr2.setResourceType(rt);
	    		instr2.setNumNeeded(needed);
	    		//add last instruction to arraylist
	    		tasks[currentIndex].getInstructions().add(instr);
	    		tasks2[currentIndex].getInstructions().add(instr2);
	    		//if last activity was 'terminate', stop populating arraylist
	    	}
	    	//add arraylist to tasks[i], as well as times = 0
	    	
	    optimisticManager(tasks, resources);
	    double totalTimeTaken = 0;
	    double totalWaitingTime = 0;
	    System.out.println("            FIFO");
	    for(int i = 0; i < tasks.length; i++){
	    	if(tasks[i].isAborted()){
	    		System.out.println("Task " + (i+1) + "      aborted");
	    	}
	    	else{
	    		double percentage = ((double)tasks[i].getWaitingTime()/(double)tasks[i].getTimeTaken()) * 100;
		    	System.out.println("Task " + (i+1) + "      " + tasks[i].getTimeTaken() + "   " + tasks[i].getWaitingTime() + "   "
		    			+ (int)Math.round(percentage) + "%");
		    	totalTimeTaken = totalTimeTaken + tasks[i].getTimeTaken();
		    	totalWaitingTime = totalWaitingTime + tasks[i].getWaitingTime();
	    	}
	    }
	    int totalPercentage = (int) Math.round((totalWaitingTime/totalTimeTaken) * 100);
	    System.out.println("total       " + (int)totalTimeTaken + "   " + (int)totalWaitingTime + "   " + totalPercentage + "%\n");
	    
	    

	    bankersAlgorithm(tasks2, resources2);
	    totalTimeTaken = 0;
	    totalWaitingTime = 0;
	    System.out.println("          BANKER'S");
	    for(int i = 0; i < tasks2.length; i++){
	    	if(tasks2[i].isAborted()){
	    		System.out.println("Task " + (i+1) + "      aborted");
	    	}
	    	else{
	    		double percentage = ((double)tasks2[i].getWaitingTime()/(double)tasks2[i].getTimeTaken()) * 100;
		    	System.out.println("Task " + (i+1) + "      " + tasks2[i].getTimeTaken() + "   " + tasks2[i].getWaitingTime() + "   "
		    			+ (int)Math.round(percentage) + "%");
		    	totalTimeTaken = totalTimeTaken + tasks2[i].getTimeTaken();
		    	totalWaitingTime = totalWaitingTime + tasks2[i].getWaitingTime();
	    	}
	    }
	    totalPercentage = (int) Math.round((totalWaitingTime/totalTimeTaken) * 100);
	    System.out.println("total       " + (int)totalTimeTaken + "   " + (int)totalWaitingTime + "   " + totalPercentage + "%\n");
	    
    }
}
