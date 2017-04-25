import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class paging {
	static Scanner randomSrc;
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
		int O = src.nextInt();
		System.out.println("The machine size is " + M + ".");
		System.out.println("The page size is " + P + ".");
		System.out.println("The process size is " + S + ".");
		System.out.println("The job mix number is " + J + ".");
		System.out.println("The number of references per process is " + N + ".");
		System.out.println("The replacement algorithm is " + R + ".");
		System.out.println("The level of debugging output is " + O + ".");
		System.out.println();
		System.out.print(processNum + "references word " + wordNum + "(page " + pageNum +
				") at time " + time + ": ");
		//if hit
			System.out.println("Hit in frame " + frameNum);
		//else
			System.out.print("Fault, ");
			//if there's a free frame
			System.out.println("using free frame " + frameNum);
			//else
			System.out.println("evicting page " + " of " + "from frame " + frameNum);
    }
}

