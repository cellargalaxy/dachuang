package version4.feature;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class MedianFeatureSeparation implements FeatureSeparation {
	
	public String getName() {
		return "中位数特征选择";
	}
	
	public final void separationFeature(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unImproEvid) {
		int count = aucImprotences.size() / 2;
		int i = 0;
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
			if (i <= count) {
				imroEvid.add(entry.getValue());
			} else {
				unImproEvid.add(entry.getValue());
			}
			i++;
		}
	}
	
	@Override
	public String toString() {
		return "MedianFeatureSeparation{中位数特征选择}";
	}
}
