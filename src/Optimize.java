import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



public class Optimize extends Thread {
	private ArrayList<Double> selectivities = new ArrayList<Double>();
	
	
	public Optimize(ArrayList<Double> s) {
		selectivities = s;
	}
	
	
	public void run() {
		QueryOptimization optimizer = new QueryOptimization(selectivities);
//		System.out.println("Hey Look! A thread!");
//		String output = "";
//		for (int j = 0; j < selectivities.size(); j++) {
//			output += selectivities.get(j) + " ";
//		}
//		System.out.println(output);
//		
	}
	

}
