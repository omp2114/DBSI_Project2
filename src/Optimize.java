import java.util.ArrayList;


public class Optimize extends Thread {
	private ArrayList<Double> selectivities = new ArrayList<Double>();
	
	
	public Optimize(ArrayList<Double> s) {
		selectivities = s;
	}
	
	public void run() {
		System.out.println("Hey Look! A thread!");
		String output = "";
		for (int j = 0; j < selectivities.size(); j++) {
			output += selectivities.get(j) + " ";
		}
		System.out.println(output);
		
	}
	

}
