
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;


public class QueryOptimization {
	private double[] selectivities;
	private ArrayList<SubsetNode> S;
	private int r,t,l,m,f,a,k;
	private ArrayList<ArrayList<Integer>> indices;
	public QueryOptimization(ArrayList<Double> s,int R, int T,int L,int M,int F,int A) {
		selectivities = new double[s.size()];
		for(int i=0; i<s.size(); i++){
			selectivities[i] = s.get(i);
		}
		k = (int) Math.pow(2, s.size());
		S = new ArrayList<SubsetNode>();
		r = R;
		t = T;
		l = L;
		m = M;
		f = F;
		a = A;
		createNodes(getSubsets());
		/********************* PRINT S HERE TO TEST ********************/
	
		System.out.println(Arrays.toString(S.toArray()));

	}
	
	/*
	 * Calculates the fixed cost for and & term
	 */
	public double calculateFixedCost(SubsetNode node) {
		double fcost = node.getN() * r + (node.getN()-1) * r + node.getN()*f + t;
		return fcost;
	}
	
	/*
	 * Calculates the C-metric for an & term
	 */
	public double cMetric(SubsetNode node) {
		return (node.getP() -1)/(calculateFixedCost(node));
	}
	
	/*
	 * Calculates the D-metric for an & term
	 */
	public double dMetric(SubsetNode node) {
		return calculateFixedCost(node);
	}
	
	/*
	 * Find the leftmost node.
	 */
	public SubsetNode leftMost(SubsetNode node) {
		if (node.getL() == null) {
			return node;
		} else {
			return leftMost(node.getL());
		}
	}
	
	/*
	 * Checks for d-metric domination on nodes other than the leftmost of the right. 
	 * Left is the left node, right is the right node and leftmost is the leftmost of the right.
	 */
	public boolean dDominate(SubsetNode left, SubsetNode right, SubsetNode leftMost) {
		Set<Integer> difference = new HashSet<Integer>(leftMost.getIndices());
		difference.removeAll(right.getIndices());
		
		// If the set difference of the leftmost and right is 0, then we have the leftmost node and return false.
		if (difference.size() == 0) {
			return false;
		} else if (dMetric(right) > dMetric(left)) {
			return (true); 
		} else {
			return (false || dDominate(left, right.getR(), leftMost) || dDominate(left, right.getL(), leftMost));
		}
	}
	
	/*
	 * Finds the node that corresponds to the union of both nodes.
	 */
	public SubsetNode nodeUnion(SubsetNode left, SubsetNode right) {
		
		// Union is done via a java hashset
		Set<Integer> union = new HashSet<Integer>(left.getIndices());
		union.addAll(right.getIndices());
		SubsetNode unionNode = null;
		for (int i = 0; i < S.size(); i++) {
			Set<Integer> difference = new HashSet<Integer>(S.get(i).getIndices());
			difference.removeAll(union);
			if (difference.size() == 0 ) {
				unionNode = S.get(i);
			}
		}
		return unionNode;
	}
	
	/*
	 * The first pruning step of the algorithm after the sets are created.
	 */
	public void partOne(){
		for(int i = 0; i< S.size(); i++){
			double cost1 = calcualteLogicalAndCost(S.get(i));
			double cost2 = calculateNoBranchCost(S.get(i));
			if(cost2<cost1){
				S.get(i).setC(cost2);
				S.get(i).setB(true);
			}
			else
				S.get(i).setC(cost1);
		}
		System.out.println("PART ONE-----------------------------------------");
		System.out.println(this.nodeUnion(S.get(1), S.get(2)));
		//System.out.println(S);
	}
	public double calculateNoBranchCost(SubsetNode node) {
		double cost = node.getN() * r + (node.getN()-1) * r + node.getN()*f + a;
		return cost;
	}
	public double calcualteLogicalAndCost(SubsetNode s){
		double p = 0; 
		double q = 0;
		if (s.getP() <= 0.5){
			q = s.getP(); 
		}else{
			q = 1 - s.getP();
		}
		double cost = s.getN() * r + (s.getN() - 1)* l + s.getN()*f + t + m * q + p* a;
		return cost;
	}
	
	
	public void pruneConditionTwoA(SubsetNode left, SubsetNode right) {
		if (left.getP() >= right.getP() && cMetric(left) > cMetric(leftMost(right) )) {
			/* Do nothing */

		} else if (left.getP() <= 0.5 && dDominate(left, right, leftMost(right))) {
			System.out.println("PRUNED!");
			/* Do nothing */
		} else {
			SubsetNode unionNode = nodeUnion(left, right);

			double cost = this.calculateFixedCost(left) + m * Math.min(left.getP(), 1-left.getP()) +
					left.getP() * this.calculateFixedCost(right);
			if (cost < unionNode.getC()) {
				unionNode.setC(cost);
				unionNode.setL(left);
				unionNode.setR(right);
			}
			
		}
		
	}
	public void pruneConditionTwo() {
		System.out.println("Prunning");
		for (int i = 1; i < S.size(); i ++) {
			SubsetNode right = S.get(i);
			for (int j = 1; j < S.size(); j++) {
				SubsetNode left = S.get(j);
				Set<Integer> intersection = new HashSet<Integer>(right.getIndices());
				intersection.retainAll(left.getIndices());
				if (intersection.size() == 0) {
					pruneConditionTwoA(left, right);
				}
			}
		}
		System.out.println("PART TWO-----------------------------------------");
		System.out.println(S);
	}
	public ArrayList<ArrayList<Double>> getSubsets(){
		System.out.println("Creating substests");
		ArrayList<ArrayList<Double>> subsets = new ArrayList<ArrayList<Double>>();
		this.indices = new ArrayList<ArrayList<Integer>>();

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
			ArrayList<Integer> currentIndices = new ArrayList<Integer>();

			temp[i] = Integer.toBinaryString(i);
			while (temp[i].length() < size) {
					temp[i] = '0' + temp[i];
			}
			for (int j = 0; j < temp[i].length(); j++) {
				int temp1 = Integer.valueOf(temp[i].substring(j, j+1));
				temp1 = (j +1) * temp1;
				if (temp1 > 0) {
					current.add(selectivities[temp1-1]);
					currentIndices.add(temp1);
				}
			}
			subsets.add(current);
			this.indices.add(currentIndices);
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
		System.out.println("Creating Nodes");
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
			Double[] sarray = Arrays.copyOf(temp, temp.length, Double[].class);
			double[] rarray = new double[sarray.length];
			for (int m = 0; m < sarray.length; m++) {
				rarray[m] = (double)sarray[m];
			}
			Set<Integer> indiceSet = new HashSet<Integer>();
			for (int k = 0; k < newSet.size(); k++) {
				indiceSet.addAll(this.indices.get(i));
			}
			S.add(new SubsetNode (newSet.size(), p, false, 0, rarray, indiceSet));	
		}
		Collections.sort(S);
	}
	
	public String printOp() {
		return S.get(S.size()-1).printOptimalPlan();
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
