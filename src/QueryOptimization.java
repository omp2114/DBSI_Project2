import java.util.ArrayList;


public class QueryOptimization {
	private ArrayList<Double> selectivities;
	private SubsetNode[] S;
	private int k;

	public QueryOptimization(ArrayList<Double> s) {
		selectivities = s;
		k = (int) Math.pow(2, s.size());
		S = new SubsetNode[k];
	}
	
	public void addNodes(){
		S[0] = null;
		int n=0;
		for(int i=1; i<k; i++){
			for(int j=i+1; j<k; j++){
				
			}
			
		}
	}
	

}
