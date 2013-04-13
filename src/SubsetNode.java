/** 
 *  The array elements, SubsetNodes, are records containing: 
 *	The number n of basic terms in the corresponding subset; 
 *	the product p of the selectivities of all terms in the subset; 
 *	a bit b determining whether the no-branch optimization was used to get the best cost, initialized to 0; 
 *	the current best cost c for the subset; 
 *	the left child Land right child R of the subplans giving the best cost. L and R range over indexes for A[], and are initialized to null;
 */
public class SubsetNode {
	
	/** The number n of basic terms in the corresponding subset */
	private int n; 
	
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
	
	public SubsetNode(int n, double p, boolean b, double c, SubsetNode l, SubsetNode r){
		this.n = n;
		this.b = b;
		this.p = p;
		this.c = c;
		L = l;
		R = r;
	}
	
	public SubsetNode(int n, double p, boolean b, double c){
		this.n = n;
		this.b = b;
		this.p = p;
		this.c = c;
		L = null;
		R = null;
	}
	
	public String toString(){
		return "n: " + n + "| b: " + b + "| p: " + p + " | c: " + c + " | L: " + L + " | R: " + R + "\n" ;
	}
	public int compare(SubsetNode s){
		if(n > s.n)
			return 1;
		else if( n < s.n)
			return -1;
		else
			return 0;
	}
	
	/**set the number of elements in subset */
	public void setN(int N){
		n = N;
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
	
	/** get left child */
	public SubsetNode getL(){
		return L;
	}
	/** get right child */
	public SubsetNode getR(){
		return R;
	}
}
