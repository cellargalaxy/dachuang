package top.cellargalaxy.dachuangspringboot.feature;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class CustomizeFeatureSplit implements FeatureSplit {
	private double splitValue;

	public CustomizeFeatureSplit(double splitValue) {
		this.splitValue = splitValue;
	}

	public void separationFeature(LinkedList<FeatureImportance> featureImportances, LinkedList<Integer> importanceEvidenceId, LinkedList<Integer> unImportanceEvidenceId) {
		for (FeatureImportance featureImportance : featureImportances) {
			if (featureImportance.getEvaluationD() < splitValue) {
				importanceEvidenceId.add(featureImportance.getEvidenceId());
			} else {
				unImportanceEvidenceId.add(featureImportance.getEvidenceId());
			}
		}
	}

	@Override
	public String toString() {
		return "用户自定义特征选择{" +
				"splitValue=" + splitValue +
				'}';
	}
}
