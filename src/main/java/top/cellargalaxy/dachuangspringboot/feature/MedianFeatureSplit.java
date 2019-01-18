package top.cellargalaxy.dachuangspringboot.feature;

import java.util.List;
import java.util.Set;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class MedianFeatureSplit implements FeatureSplit {
	public static final String NAME = "中位数特征选择";

	public void splitFeature(List<FeatureImportance> featureImportances, Set<Integer> importanceEvidenceIds, Set<Integer> unImportanceEvidenceIds) {
		int count = featureImportances.size() / 2;
		int i = 0;
		for (FeatureImportance featureImportance : featureImportances) {
			if (i < count) {
				unImportanceEvidenceIds.add(featureImportance.getEvidenceId());
			} else {
				importanceEvidenceIds.add(featureImportance.getEvidenceId());
			}
			i++;
		}
	}

	@Override
	public String toString() {
		return "中位数特征选择";
	}
}
