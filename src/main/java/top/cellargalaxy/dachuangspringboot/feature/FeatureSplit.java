package top.cellargalaxy.dachuangspringboot.feature;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public interface FeatureSplit {
	void splitFeature(LinkedList<FeatureImportance> featureImportances, Set<Integer> importanceEvidenceIds, Set<Integer> unImportanceEvidenceIds);
}
