package top.cellargalaxy.dachuangspringboot.feature;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class MedianFeatureSplit implements FeatureSplit {

	public void separationFeature(LinkedList<FeatureImportance> featureImportances, LinkedList<Integer> importanceEvidenceId, LinkedList<Integer> unImportanceEvidenceId) {
		featureImportances.sort((FeatureImportance o1, FeatureImportance o2) -> {
			if (o1.getEvaluationD() < o2.getEvaluationD()) {
				return -1;
			} else if (o1.getEvaluationD() > o2.getEvaluationD()) {
				return 1;
			} else {
				return 0;
			}
		});
		int count = featureImportances.size() / 2;
		int i = 0;
		for (FeatureImportance featureImportance : featureImportances) {
			if (i < count) {
				unImportanceEvidenceId.add(featureImportance.getEvidenceId());
			} else {
				importanceEvidenceId.add(featureImportance.getEvidenceId());
			}
			i++;
		}
	}

	@Override
	public String toString() {
		return "中位数特征选择";
	}
}
