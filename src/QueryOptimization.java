import java.util.ArrayList;
import java.util.Arrays;

public class QueryOptimization {
	private double[] selectivities;
	private SubsetNode[] S;
	private int k;
	private int current;

	public QueryOptimization(ArrayList<Double> s) {
		selectivities = new double[s.size()];
		for(int i=0; i<s.size(); i++){
			selectivities[i] = s.get(i);
		}
		k = (int) Math.pow(2, s.size());
		S = new SubsetNode[k];
		current = 0;
		for(int i=0; i<=selectivities.length; i++){
			double[] newSet = new double[i];
			createSubsets(selectivities, newSet, 0, 0);
		}
		/********************* PRINT S HERE TO TEST ********************/
		System.out.println(Arrays.toString(S));

	}

	private void createSubsets(double[] currentSet, double[] newSet,  int index, int length) {
		if (length < newSet.length) {
			for (int i = index; i < currentSet.length; i++) {
				newSet[length] = currentSet[i];
				createSubsets(currentSet, newSet, i + 1, length + 1);
			}
		} else {
			double p = 1;
			if(newSet.length==0){
				p = 0;
			}
			for(int i=0; i<newSet.length; i++){
				p = p * newSet[i];
			}
			S[current++] = new SubsetNode (newSet.length, p, false, 0);
		}
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
