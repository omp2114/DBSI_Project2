/** 
 *  The array elements, SubsetNodes, are records containing: 
 *	The number n of basic terms in the corresponding subset; 
 *	the product p of the selectivities of all terms in the subset; 
 *	a bit b determining whether the no-branch optimization was used to get the best cost, initialized to 0; 
 *	the current best cost c for the subset; 
 *	the left child Land right child R of the subplans giving the best cost. L and R range over indexes for A[], and are initialized to null;
 */
import java.util.Arrays;
import java.util.Set;

public class SubsetNode implements Comparable {
	
	/** The number n of basic terms in the corresponding subset */
	private int n; 
	
	private Set<Integer> indices;
	
	/** The ArrayList representing the selectivities */
	private double[] selectivities;
	
	/** The product p of the selectivities of all terms in the subset */
	private double p;
	
	/** a bit or boolean b determining whether the no-branch optimization was used to get the best cost, initialized to false */
	private boolean b;

	/** the current best cost c for the subset */
	private double c;
	
	/**	the left child L */ 
	private SubsetNode L;
	
	/** the right child R */
	private SubsetNode R;
	
	public SubsetNode(){
		n = 0;
		b = false;
		p = 0;
		c = Double.POSITIVE_INFINITY;
		L = null;
		R = null;
		
	}
	
	public SubsetNode(int n, double p, boolean b, double c, SubsetNode l, SubsetNode r, double[] s, Set<Integer> I){
		this.n = n;
		this.b = b;
		this.p = p;
		//this.c = c;
		this.c = Double.POSITIVE_INFINITY;
		L = l;
		R = r;
		selectivities = s;
		this.indices = I;
	}
	
	public SubsetNode(int n, double p, boolean b, double c, double[] s,  Set<Integer> I){
		this.n = n;
		this.b = b;
		this.p = p;
//		this.c = c;
		this.c = Double.POSITIVE_INFINITY;
		L = null;
		R = null;
		selectivities = s;
		indices = I;
	}
	
	public String toString(){
		return "n: " + n + "| b: " + b + "| p: " + p + " | c: " + c + " | L: " + L + " | R: " + R + Arrays.toString(this.selectivities) + Arrays.toString(indices.toArray(new Integer[indices.size()])) + "\n" ;
	}
	
	/**set the number of elements in subset */
	public void setN(int N){
		n = N;
	}
	
	public void setC(double C) {
		this.c = C;
	}

	/** set no-branching boolean */
	public void setB(boolean B){
		b = B;
	}
	
	/** set the product p of the selectivities */ 
	public void setP(double P){
		p = P;
	}
	
	/** set the left child */
	public void setL(SubsetNode l){
		L = l;
	}
	
	/**set the right child */
	public void setR(SubsetNode r){
		R = r;
	}
	
	/**get number of elements in subset */
	public double getN(){
		return n;
	}
	
	/** get the current cost */
	public double getC(){
		return c;
	}
	
	/** get the whether no-branching algorithm was used */
	public boolean getB(){
		return b;
	}
	
	/**get the product of selectivites */
	public double getP(){
		return p;
	}
	
	public Set<Integer> getIndices(){
		return indices;
	}
	
	
	/** get left child */
	public SubsetNode getL(){
		return L;
	}
	/** get right child */
	public SubsetNode getR(){
		return R;
	}
	/** get right child */
	public double[] getSelectivities(){
		return selectivities;
	}
	
	public String printOptimalPlan() {
		String out = "======================================\n";
		String ifs = "if(";
		String branch = "answer[j] = i; + '\n'" + "j += ";
		out += Arrays.toString(selectivities) + '\n';
		out += "--------------------------------------\n";
		out += recursivePrint(this, out, 1, branch, ifs);
		out += "--------------------------------------\n";
		out += "cost =" + this.c + '\n';
		return out;
	}
	
	public SubsetNode leftMost(SubsetNode node) {
		if (node.getL() == null) {
			return node;
		} else {
			return node.getL();
		}
	}
	
	private String recursivePrint(SubsetNode optimal, String out, int current, String branch, String ifs) {
		if (optimal.getR() == null) {
			String temp = "answer[j] = i; + '\n'" + "j += ";
			String cString = "";
			if (ifs.length() > 3) {
				cString += ifs + ") {" + '\n';
			}
			if (branch.length() > temp.length()) {
				cString += branch + '\n' + "}";
			} else {
				cString += '\n' + "answer[j++] = i; '\n'" + "}"; 
			}
			System.out.println("IF STRINGGFGGGGG" + ifs);
			return cString;
		}
		String ifstring = "";
		String bstring = "";
		if (this.getB() == true) {
			for (int i = 0; i < optimal.getN(); i++) {
				bstring += "t" + (current + i + 1) + "[o" + (current + i + 1) + "[i]] & ";  
			}
		} else {
			System.out.println("MAKING IF");
			if (optimal.getN() == 1) {
				ifstring += "t" + + (current + 1) + "[o" + (current + 1) +"[i]] && " ;
			} else {
				ifstring += "(";
				for (int i = 0; i < optimal.getN(); i++) {
					
					// We dont want a final and so we do a condition for i = length-1;
					if (i == optimal.getN()-1) {
						ifstring +=  "t" + + (current + i+ 1) + "[o" + (current + i + 1) +"[i]]" ;
					} else {
						ifstring +=  "t" + + (current + i+ 1) + "[o" + (current + i + 1) +"[i]] & " ;
					}
				}
				ifstring += ") &&";
			}	
		}
		String rif = ifs + ifstring;
		String rb = branch + bstring;
		return recursivePrint(optimal.getR(), out, (int)(current + optimal.getN()), rb, rif) ;
	}

	@Override
	public int compareTo(Object o) {
		SubsetNode s = (SubsetNode)o;
		// TODO Auto-generated method stub
		if(n > s.n)
			return 1;
		else if( n < s.n)
			return -1;
		else
			return 0;
	}
}
