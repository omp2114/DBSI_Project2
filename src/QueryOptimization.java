import java.util.ArrayList;
import java.util.Arrays;

public class QueryOptimization {
	private double[] selectivities;
	private SubsetNode[] S;
	private int k;
	private int current;

	public QueryOptimization(ArrayList<Double> s) {
		for(int i=0; i<=s.size(); i++){
			selectivities[i] = s.get(i);
		}
		k = (int) Math.pow(2, s.size());
		S = new SubsetNode[k];
		current = 0;
	    for(int i=0; i<=selectivities.length; i++){
	    	addSubsetsToS(selectivities, i);
	    }
	    /********************* PRINT S HERE TO TEST ********************/
	    System.out.println(Arrays.toString(S));
		
	}

	private void addSubsetsToS(double[] selectivities, int k) {
	    double[] newSet = new double[k];
	    createSubsets(selectivities, newSet, 0, 0);
	}

	private void createSubsets(double[] currentSet, double[] newSet,  int index, int length) {
	    if (length != newSet.length) {
	    	 for (int i = index; i < currentSet.length; i++) {
		            newSet[length] = currentSet[i];
		            createSubsets(currentSet, newSet, i++, length + 1);
		        }
	    
	    } else {
	    	double p = 1;
			for(int i=0; i<=newSet.length; i++){
					p = p * newSet[i];
			}
		    S[current++] = new SubsetNode (newSet.length, p, false, 0);
	    }
	}

	
	



	
}
