import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Read in the Query Files
		File infile = new File(args[0]);
		File configFile = new File(args[1]);
		
		// Create an array list to store an array list of selectivities
		ArrayList<ArrayList<Double>> selectivities = new ArrayList<ArrayList<Double>>();
		int t=0, l=0, m=0, r=0, f=0, a =0;
		// Counting the number of threads
		int threadNumber = 0;
		try {
			
			Scanner input = new Scanner(infile);
			Scanner configInput = new Scanner(configFile);
			
			// Read in the query selectivities
			while (input.hasNextLine()) {
				ArrayList<Double> s = new ArrayList<Double>(); 
				String line = input.nextLine();
				Scanner lineScanner = new Scanner(line);
				while (lineScanner.hasNextDouble()) {
					s.add(lineScanner.nextDouble());
				}
				selectivities.add(s);
				threadNumber ++;
			}
			
			// Read in the cost values
			for (int i = 0; i < 6; i++) {
				if (i == 0) {
					configInput.next();
					configInput.next();
					r = configInput.nextInt();
					System.out.println("r is: " + r);
				} else if (i == 1) {
					configInput.next();
					configInput.next();
					t = configInput.nextInt();
					System.out.println("t is: " + t);

				} else if (i == 2) {
					configInput.next();
					configInput.next();
					l = configInput.nextInt();
					System.out.println("l is: " + l);

				} else if (i == 3) {
					configInput.next();
					configInput.next();
					m = configInput.nextInt();
					System.out.println("m is: " + m);

				} else if (i == 4) {
					configInput.next();
					configInput.next();
					a = configInput.nextInt();
					System.out.println("a is: " + a);

				} else if (i == 5) {
					configInput.next();
					configInput.next();
					f = configInput.nextInt();
					System.out.println("f is: " + f);

				}
			}	
			// Splits the queries into threads to handle them in parallel
			for (int i = 0; i < threadNumber; i++) {
				Thread newThread = new Optimize(selectivities.get(i),t, l, m, r, f, a);
				newThread.start();
			}	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
