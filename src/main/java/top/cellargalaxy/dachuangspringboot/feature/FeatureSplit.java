package top.cellargalaxy.dachuangspringboot.feature;

import java.util.List;
import java.util.Set;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public interface FeatureSplit {
	void splitFeature(List<FeatureImportance> featureImportances, Set<Integer> importanceEvidenceIds, Set<Integer> unImportanceEvidenceIds);
}
