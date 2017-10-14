package version4.feature;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public interface FeatureSeparation {
	String getName();
	void separationFeature(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unImproEvid);
}
