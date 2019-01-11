package top.cellargalaxy.dachuangspringboot.feature;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public interface FeatureSplit {
	void separationFeature(LinkedList<FeatureImportance> featureImportances, LinkedList<Integer> importanceEvidenceId, LinkedList<Integer> unImportanceEvidenceId);
}
