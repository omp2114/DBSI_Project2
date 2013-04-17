
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
	public double calculateFixedCost(SubsetNode node) {
		double fcost = node.getN() * r + (node.getN()-1) * r + node.getN()*f + t;
		return fcost;
	}
	public double cMetric(SubsetNode node) {
		return (node.getP() -1)/(calculateFixedCost(node));
	}
	public double dMetric(SubsetNode node) {
		return calculateFixedCost(node);
	}
	
	public SubsetNode leftMost(SubsetNode node) {
		if (node.getL() == null) {
			return node;
		} else {
			return leftMost(node.getL());
		}
	}
	
	/*
	 * Starts at the leftmost node and moves right. If the leftmost is the only node
	 * there then it returns false. Else if another node dominates left, it will return true.
	 */
	public boolean dDominate(SubsetNode left, SubsetNode leftMost) {
		if (leftMost.getL() == null && leftMost.getR() == null) {
			return false;
		} else if (dMetric(leftMost) > dMetric(left)) {
			return (true || dDominate(left, leftMost.getR())); 
		} else {
			return (false || dDominate(left, leftMost.getR()));
		}
	}
	
	/*
	 * Finds the node that corresponds to the union of both nodes.
	 */
	public SubsetNode nodeUnion(SubsetNode left, SubsetNode right) {
		double newp = left.getP() * right.getP();
		double newn = left.getN() + right.getN();
		Set<Integer> union = new HashSet<Integer>(left.getIndices());
		union.addAll(right.getIndices());
		SubsetNode unionNode = null;
		for (int i = 0; i < S.size(); i++) {
			Set<Integer> difference = new HashSet<Integer>(union);
			difference.removeAll(S.get(i).getIndices());
			if (difference.size() == 0 ) {
				unionNode = S.get(i);
			}
		}

		
		return unionNode;
	}
	
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
		System.out.println(S);
	}
	public double calculateNoBranchCost(SubsetNode node) {
		double cost = node.getN() * r + (node.getN()-1) * r + node.getN()*f + a;
		return cost;
	}
	public double calcualteLogicalAndCost(SubsetNode s){
		double p = 0; 
		double q = 0;
		if (s.getN() * s.getP() <= 0.5){
			q = s.getP() * s.getN(); 
		}else{
			q = 1 - s.getP() * s.getN();
		}
		double cost = s.getN() * r + (s.getN() - 1)* l + s.getN()*f + t + m * q + p* s.getN() * a;
		return cost;
	}
	
	
	public void pruneConditionTwoA(SubsetNode left, SubsetNode right) {
		if (cMetric(left) < cMetric(leftMost(right) )) {
			/* Do nothing */
		} else if (left.getP() <= 0.5 && dDominate(left, leftMost(right))) {
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
