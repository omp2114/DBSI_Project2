import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Optimize extends Thread {
	private ArrayList<Double> selectivities = new ArrayList<Double>();
	private int r,t,l,m,f,a;
	
	public Optimize(ArrayList<Double> s, int t1,int l1,int m1,int r1,int f1,int  a1) {
		selectivities = s;
		r = r1;
		t = t1;
		l = l1;
		m = m1;
		f = f1;
		a = a1;
	}	
	public void run() {
		QueryOptimization optimizer = new QueryOptimization(selectivities, r,t,l,m,f,a);
		optimizer.partOne();
		optimizer.pruneConditionTwo();
		//System.out.println(optimizer.printOp());
		System.out.println(optimizer.outputQueryPlan());
	}
	

}
