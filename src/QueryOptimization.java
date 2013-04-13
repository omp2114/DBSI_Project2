
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;


public class QueryOptimization {
	private double[] selectivities;
	private ArrayList<SubsetNode> S;
	private int k;

	public QueryOptimization(ArrayList<Double> s) {
		selectivities = new double[s.size()];
		for(int i=0; i<s.size(); i++){
			selectivities[i] = s.get(i);
		}
		k = (int) Math.pow(2, s.size());
		S = new ArrayList<SubsetNode>();
		createNodes(getSubsets());
		
		/********************* PRINT S HERE TO TEST ********************/
		Collections.sort(S);
		System.out.println(Arrays.toString(S.toArray()));

	}
	public ArrayList<ArrayList<Double>> getSubsets(){
		System.out.println("Creating substests");
		ArrayList<ArrayList<Double>> subsets = new ArrayList<ArrayList<Double>>();
	
		/* 
		 * Weird algorithm. We had a lot of difficulty trying to come up with an algorithm. We realized
		 * That bitstrings could easily represent unique subsets and used it to our advantage.
		*/
		int n = (int) Math.pow(2, selectivities.length);
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
					current.add(selectivities[temp1-1]);
				}
			}
			subsets.add(current);
		}
		System.out.println("Finished");
		return subsets;
// Code to test subset code
//		String output = "";
//		for (int i = 0; i < subsets.size(); i++) {
//			ArrayList<Double> current = subsets.get(i);
//			for (int j = 0; j < current.size(); j++) {
//				output += current.get(j);
//			}
//			output += '\n';
//		}
//		System.out.println(output);
	}

	private void createNodes(ArrayList<ArrayList<Double>> sets) {
		System.out.println("Creting Nodes");
		for (int i = 0; i < sets.size(); i++) {
			ArrayList<Double> newSet = sets.get(i);
			double p = 1;
			if(newSet.size()==0){
				p = 0;
			}
			for(int k=0; k<newSet.size(); k++){
				p = p * newSet.get(k);
			}
			Object[] temp = newSet.toArray();	
			//ouble[] sarray = new double[temp.length];
			Double[] sarray = Arrays.copyOf(temp, temp.length, Double[].class);
			double[] rarray = new double[sarray.length];
			for (int m = 0; m < sarray.length; m++) {
				rarray[m] = (double)sarray[m];
			}
			S.add(new SubsetNode (newSet.size(), p, false, 0, rarray));	
		}
		System.out.println("Finished Creating NOdes");
		//System.out.println(output);
	}

	/**	Test class here
	 * public static void main(String[] args) {
		ArrayList<Double> d = new ArrayList<Double>();
		d.add(0.3);
		d.add(0.4);
		d.add(0.6);
		d.add(0.7);
		d.add(0.9);
		QueryOptimization q = new QueryOptimization(d);
	} 

	 **/
}
