package top.cellargalaxy.dachuangspringboot.feature;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class AverageFeatureSplit implements FeatureSplit {

	@Override
	public void separationFeature(LinkedList<FeatureImportance> featureImportances, LinkedList<Integer> importanceEvidenceId, LinkedList<Integer> unImportanceEvidenceId) {
		double count = 0;
		for (FeatureImportance featureImportance : featureImportances) {
			count += featureImportance.getEvaluationD();
		}
		double avg = count / featureImportances.size();
		for (FeatureImportance featureImportance : featureImportances) {
			if (featureImportance.getEvaluationD() <= avg) {
				importanceEvidenceId.add(featureImportance.getEvidenceId());
			} else {
				unImportanceEvidenceId.add(featureImportance.getEvidenceId());
			}
		}
	}

	@Override
	public String toString() {
		return "平均数特征选择";
	}
}