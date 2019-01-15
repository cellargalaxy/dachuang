package top.cellargalaxy.dachuangspringboot.feature;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class CustomizeFeatureSplit implements FeatureSplit {
	public static final String NAME = "用户自定义特征选择";
	private double splitValue;

	public CustomizeFeatureSplit(double splitValue) {
		this.splitValue = splitValue;
	}

	@Override
	public void splitFeature(LinkedList<FeatureImportance> featureImportances, Set<Integer> importanceEvidenceIds, Set<Integer> unImportanceEvidenceIds) {
		for (FeatureImportance featureImportance : featureImportances) {
			if (featureImportance.getEvaluationD() < splitValue) {
				importanceEvidenceIds.add(featureImportance.getEvidenceId());
			} else {
				unImportanceEvidenceIds.add(featureImportance.getEvidenceId());
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
