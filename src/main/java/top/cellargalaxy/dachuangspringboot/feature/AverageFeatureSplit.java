package top.cellargalaxy.dachuangspringboot.feature;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class AverageFeatureSplit implements FeatureSplit {
	public static final String NAME = "平均数特征选择";

	@Override
	public void splitFeature(LinkedList<FeatureImportance> featureImportances, Set<Integer> importanceEvidenceIds, Set<Integer> unImportanceEvidenceIds) {
		double count = 0;
		for (FeatureImportance featureImportance : featureImportances) {
			count += featureImportance.getEvaluationD();
		}
		double avg = count / featureImportances.size();
		for (FeatureImportance featureImportance : featureImportances) {
			if (featureImportance.getEvaluationD() <= avg) {
				importanceEvidenceIds.add(featureImportance.getEvidenceId());
			} else {
				unImportanceEvidenceIds.add(featureImportance.getEvidenceId());
			}
		}
	}

	@Override
	public String toString() {
		return "平均数特征选择";
	}
}