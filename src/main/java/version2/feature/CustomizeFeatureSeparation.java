package version2.feature;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class CustomizeFeatureSeparation implements FeatureSeparation{
	private double separationValue;
	
	public CustomizeFeatureSeparation(double separationValue) {
		this.separationValue = separationValue;
	}
	
	public final void separationFeature(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unImproEvid) {
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
			if (entry.getKey() <= separationValue) {
				imroEvid.add(entry.getValue());
			} else {
				unImproEvid.add(entry.getValue());
			}
		}
	}
}