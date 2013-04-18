
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;


public class QueryOptimization {
	private double[] selectivities;
	private ArrayList<SubsetNode> S;
	private int r,t,l,m,f,a,k, current;
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
		current = 0;
		createNodes(getSubsets());	

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
	
	public double computePlanCost(SubsetNode right) {
		if (right.getR() == null) {
			return this.calculateFixedCost(right);
		}
		double cost = this.calculateFixedCost(right.getL()) + m * Math.min(right.getL().getP(), 1-right.getL().getP()) +
				right.getL().getP() * this.computePlanCost(right.getR());
		return cost;
	}
	public void pruneConditionTwoA(SubsetNode left, SubsetNode right) {
		if (left.getP() >= right.getP() && cMetric(left) > cMetric(leftMost(right) )) {
			/* Do nothing */

		} else if (left.getP() <= 0.5 && dDominate(left, right, leftMost(right))) {
			
			/* Do nothing */
		} else {
			SubsetNode unionNode = nodeUnion(left, right);

			double cost = this.calculateFixedCost(left) + m * Math.min(left.getP(), 1-left.getP()) +
					left.getP() * right.getC();
			if (cost < unionNode.getC()) {
				unionNode.setC(cost);
				unionNode.setL(left);
				unionNode.setR(right);
			}
			
		}
		
	}
	public void pruneConditionTwo() {
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
	}
	public ArrayList<ArrayList<Double>> getSubsets(){
		ArrayList<ArrayList<Double>> subsets = new ArrayList<ArrayList<Double>>();
		this.indices = new ArrayList<ArrayList<Integer>>();

		/* 
		 * Weird algorithm. We had a lot of difficulty trying to come up with an algorithm. We realized
		 * That bitstrings could easily represent unique subsets and used it to our advantage.
		*/
		int n = (int) Math.pow(2, selectivities.length);
		int size = Integer.toBinaryString(n).length()-1;
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
		return subsets;
	}
	
	private void createNodes(ArrayList<ArrayList<Double>> sets) {
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
	public String outputQueryPlan() {
		current = 0;
		String out = "======================================\n";
		String ifs = "";
		String branch = "    answer[j] = i;  \n" + "    j += (";
		out += Arrays.toString(selectivities) + '\n';
		out += "--------------------------------------\n";
		out += recursivePrintOp(this.S.get(S.size()-1), ifs, branch) + '\n';
		out += "--------------------------------------\n";
		out += "cost = " + this.S.get(S.size()-1).getC() ;
		return out;
	}
	
	public String recursivePrintOp(SubsetNode optimal, String ifs, String branch) {
		String rstring = "";
		String ifstring = "";
		if (optimal.getL() == null) {
			Integer[] indices = optimal.getIndices().toArray(new Integer[optimal.getIndices().size()]);
			if (ifs.length() >= 2){
				rstring = "if " + ifs;
				for (int ps = 0; ps < current; ps ++) {
					rstring += ")";
				}
				
				rstring += "{ \n";
			} 
			if (optimal.getN() == 1) {
				ifstring += "(t" + (indices[0]) + "[o" + (indices[0]) + "[i]];" ;
			} else {
				for (int i = 0; i < optimal.getN(); i++) {
					
					// We dont want a final and so we do a condition for i = length-1;
					if (i == optimal.getN()-1) {
						ifstring +=  "t" +  (indices[i]) + "[o" + (indices[i]) +"[i]]);" ;
					} else {
						ifstring +=  "t" +  (indices[i]) + "[o" + (indices[i]) +"[i]] & " ;
					}
				}
				if (ifs.length() >= 2){
					ifstring +=  "\n }";
				} 
				String t=" ";
			
			
			}	
			
			return rstring + branch + ifstring;
		}
		Integer[] indices = optimal.getL().getIndices().toArray(new Integer[optimal.getL().getIndices().size()]);

		if (optimal.getL().getN() == 1) {
			if (optimal.getR().getL() != null) {
			
					ifstring += "(t" + + (indices[0]) + "[o" + (indices[0]) +"[i]] && " ;
				current ++;
			} else {
				ifstring += "t" + + (indices[0]) + "[o" + (indices[0]) +"[i]]" ;

			}
		} else {
			if (optimal.getR().getL() != null) {
				ifstring += "(";
			}
			
			for (int i = 0; i < optimal.getL().getN(); i++) {
				// We dont want a final and so we do a condition for i = length-1;
				if (i == optimal.getL().getN()-1) {
					ifstring +=  "t" + + (indices[i]) + "[o" + (indices[i]) +"[i]]" ;
				} else {
					ifstring +=  "(t" + + (indices[i]) + "[o" + (indices[i]) +"[i]] & " ;
				}
			}			
			ifstring += ")";
			if (optimal.getR().getL() != null) {
				ifstring += " && ";
				current ++;
			}
		}	
	
		return recursivePrintOp(optimal.getR(), ifs + ifstring , branch);
	}

}
