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
		
		// Create an array list to store an array list of selectivities
		ArrayList<ArrayList<Double>> selectivities = new ArrayList<ArrayList<Double>>();
		
		// Counting the number of threads
		int threadNumber = 0;
		try {
			
			Scanner input = new Scanner(infile);

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
//			for (int i = 0; i < selectivities.size(); i++) {
//				ArrayList<Double> current = selectivities.get(i);
//				for (int j = 0; j < current.size(); j++) {
//					System.out.print(current.get(j) + " ");
//				}
//				System.out.println();
//			}
//			
			for (int i = 0; i < threadNumber; i++) {
				Thread newThread = new Optimize(selectivities.get(i));
				newThread.start();
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		

	}

}
