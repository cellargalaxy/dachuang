package version3.feature;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class AverageFeatureSeparation implements FeatureSeparation {
	public String getName() {
		return "平均数特征选择";
	}
	
	public final void separationFeature(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unImproEvid) {
		double count = 0;
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
			count += entry.getKey();
		}
		double avg = count / aucImprotences.size();
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
			if (entry.getKey() <= avg) {
				imroEvid.add(entry.getValue());
			} else {
				unImproEvid.add(entry.getValue());
			}
		}
	}
}