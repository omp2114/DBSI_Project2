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
			
			// Splits the queries into threads to handle them in parallel
			//for (int i = 0; i < threadNumber; i++) {
			Thread newThread = new Optimize(selectivities.get(1));
			newThread.start();
//			/
//				int n = (int) Math.pow(2, selectivities.get(1).size());
//				int size = Integer.toBinaryString(n).length()-1;
//				System.out.println(Integer.toBinaryString(0).length());
//				String[] temp = new String[n];
//				
//				for (int i = 0; i < n; i++) {
//					String outputstr ="";
//					temp[i] = Integer.toBinaryString(i);
//					while (temp[i].length() < size) {
//							temp[i] = '0' + temp[i];
//					}
//					for (int j = 0; j < temp[i].length(); j++) {
//						int temp1 = Integer.valueOf(temp[i].substring(j, j+1));
//						temp1 = (j +1) * temp1;
//						outputstr += temp1;
//					}
//					
//					System.out.println(outputstr);
//				}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		

	}

}
