package bdma.bigdata.aiwsbu.mapreduce;

import java.util.Comparator;
import java.util.HashMap;

public class ValueComparator implements Comparator<String>{
	HashMap<String, Double> base;
	public ValueComparator(HashMap<String, Double> base) {
	this.base = base;
	}

	@Override
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		}
		else return 1;
	}
}
