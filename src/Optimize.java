import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



public class Optimize extends Thread {
	private ArrayList<Double> selectivities = new ArrayList<Double>();
	
	
	public Optimize(ArrayList<Double> s) {
		selectivities = s;
	}
	public void addNodes(){
		Set<HashSet<Integer>> sort = new HashSet<HashSet<Integer>>();
		ArrayList<ArrayList<Double>> subsets = new ArrayList<ArrayList<Double>>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		// Create a set of indices
		for (int i = 0; i < selectivities.size(); i++) {
			indices.add(i);
		}
		
		
		int n = (int) Math.pow(2, selectivities.size());
		int size = Integer.toBinaryString(n).length()-1;
		System.out.println(Integer.toBinaryString(0).length());
		String[] temp = new String[n];
		
		for (int i = 0; i < n; i++) {
			ArrayList<Double> current = new ArrayList<Double>();
			temp[i] = Integer.toBinaryString(i);
			while (temp[i].length() < size) {
					temp[i] = '0' + temp[i];
			}
			for (int j = 0; j < temp[i].length(); j++) {
				int temp1 = Integer.valueOf(temp[i].substring(j, j+1));
				temp1 = (j +1) * temp1;
				if (temp1 > 0) {
					current.add(selectivities.get(temp1-1));
				}
			}
			subsets.add(current);
		}
		String output = "";
		for (int i = 0; i < subsets.size(); i++) {
			ArrayList<Double> current = subsets.get(i);
			for (int j = 0; j < current.size(); j++) {
				output += current.get(j);
			}
			output += '\n';
		}
		System.out.println(output);
	}
	
	public void run() {
//		System.out.println("Hey Look! A thread!");
//		String output = "";
//		for (int j = 0; j < selectivities.size(); j++) {
//			output += selectivities.get(j) + " ";
//		}
//		System.out.println(output);
//		
		addNodes();
	}
	

}
