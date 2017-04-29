import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class paging {
	static Scanner randomSrc;
	public static int randomOS(int U){
        return randomSrc.nextInt();
    }
	public static void main(String[] args) throws FileNotFoundException{ 
    	//scanner for input file
        String name = args[0];
		File fileName = new File(name);
		Scanner src = new Scanner(fileName);
		//scanner for random numbers
		File randomFileName = new File("random-numbers.txt");
        randomSrc = new Scanner(randomFileName);
		int M = src.nextInt();
		int P = src.nextInt();
		int S = src.nextInt();
		int J = src.nextInt();
		int N = src.nextInt();
		String R = src.next();
		int time = 0;
		//int O = src.nextInt();
		System.out.println("The machine size is " + M + ".");
		System.out.println("The page size is " + P + ".");
		System.out.println("The process size is " + S + ".");
		System.out.println("The job mix number is " + J + ".");
		System.out.println("The number of references per process is " + N + ".");
		System.out.println("The replacement algorithm is " + R + ".");
		//System.out.println("The level of debugging output is " + O + ".");
		System.out.println();
		int numFrames = M / P;
		//create empty frame table
		Frame[] frameTable = new Frame[numFrames];
		int numPages = S / P;
		//create the processes
		ArrayList<Process> processes = new ArrayList<Process>();
		if(J == 1){
			processes.add(new Process(1, 0, 0, (111 * 1) % S));
		}
		else if(J == 2){
			processes.add(new Process(1, 0, 0, (111 * 1) % S));
			processes.add(new Process(1, 0, 0, (111 * 2) % S));
			processes.add(new Process(1, 0, 0, (111 * 3) % S));
			processes.add(new Process(1, 0, 0, (111 * 4) % S));
		}
		else if(J == 3){
			processes.add(new Process(0, 0, 0, (111 * 1) % S));
		}
		else{
			processes.add(new Process(.75, .25, 0, (111 * 1) % S));
			processes.add(new Process(.75, 0, .25, (111 * 2) % S));
			processes.add(new Process(.75, .125, .125, (111 * 3) % S));
			processes.add(new Process(.5, .125, .125, (111 * 4) % S));
		}
		int currentProcessNum = 0;
		int q = 3;
		int residencySum = 0;
		int numEvictions = 0;
		while(time < N * processes.size()){
			//make sure the processes do 3 checks and then pass it on to the next process
			if(q > 0){
				q--;
			}
			else{
				// move on to next process in rotation
				q = 3;
				if(currentProcessNum == processes.size() - 1){
					currentProcessNum = 0;
				}
				else{
					currentProcessNum++;
				}
			}
			Process currentProcess = processes.get(currentProcessNum);
			//get reference number, calculate page number
			//process number and page number have to match for a frame hit
			//check if there's a frame hit
			int hit = -1;
			int currentPageNum = currentProcess.getWordReferenced() / P;
			for(int i = 0; i < frameTable.length; i++){
				if(frameTable[i].getProcessID() == currentProcessNum && frameTable[i].getPageNum() == currentPageNum){
					hit = i;
					break;
				}
			}
			if(hit != -1){
				//hit, update end time
				frameTable[hit].setEndTime(time);
			} else {
				//miss
				//check if there's a free frame and put it in the free frame
				int frameToUse = -1;
				for(int i = 0; i < frameTable.length; i++){
					if(frameTable[i].getProcessID() == -1 && frameTable[i].getPageNum() == -1){
						frameToUse = i;
						break;
					}
				}
				//else, you must evict a frame with the policy specified, and then put it in that frame
				if(frameToUse == -1){
					// get the frame to use
					if(R.equals("lru")){
						int min = frameTable[0].getEndTime();
						for(int i = 0; i < frameTable.length; i++){
							if(frameTable[i].getEndTime() < min){
								min = frameTable[i].getEndTime();
							}
							frameToUse = i;
						}
					}
					else if(R.equals("fifo")){
						int min = frameTable[0].getEndTime();
						for(int i = 0; i < frameTable.length; i++){
							if(frameTable[i].getEndTime() < min){
								min = frameTable[i].getStartTime();
							}
							frameToUse = i;
						}
					}
					else if(R.equals("random")){
						frameToUse = randomSrc.nextInt() % frameTable.length;
					}
					
					//calculate residency time, increment evictions
					currentProcess.setEvictCount(currentProcess.getEvictCount() + 1);
					numEvictions++;
					int resTime = time - frameTable[frameToUse].getStartTime();
					residencySum = residencySum + resTime;
					currentProcess.setResidencyTime(currentProcess.getResidencyTime() + resTime);
					
				}
				frameTable[frameToUse].setProcessID(currentProcessNum);
				frameTable[frameToUse].setPageNum(currentPageNum);
				frameTable[frameToUse].setStartTime(time);
				frameTable[frameToUse].setEndTime(time);
			}
			//update reference number
			double rand = randomSrc.nextInt()/(Integer.MAX_VALUE + 1.0);
			if(rand < currentProcess.getA()){
				currentProcess.setWordReferenced((currentProcess.getWordReferenced() + 1) % S);
			}
			else if(rand < currentProcess.getA() + currentProcess.getB()){
				currentProcess.setWordReferenced((currentProcess.getWordReferenced() - 5 + S) % S);
			}
			else if(rand < currentProcess.getA() + currentProcess.getB() + currentProcess.getC()){
				currentProcess.setWordReferenced((currentProcess.getWordReferenced() + 4) % S);
			}
			else{
				currentProcess.setWordReferenced(randomSrc.nextInt() % S);
			}
			time++;
		}
		for(int i = 0; i < processes.size(); i++){
			int avgresidency = processes.get(i).getResidencyTime() / processes.get(i).getEvictCount();
			System.out.println("Process " + (i+1) + " had " + processes.get(i).getEvictCount() + " faults and " + avgresidency + " average residency.");
		}
    }
}

