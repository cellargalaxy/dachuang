package top.cellargalaxy.dachuangspringboot.subSpace;

import top.cellargalaxy.dachuangspringboot.feature.FeatureImportance;

import java.util.ArrayList;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class PowerImprotenceAdjust implements ImprotenceAdjust {
	public static final String NAME = "平方调整算法";

	@Override
	public ArrayList<FeatureImportance> adjustImportance(ArrayList<FeatureImportance> featureImportances) {
		for (FeatureImportance featureImportance : featureImportances) {
			featureImportance.setEvaluationD(featureImportance.getEvaluationD() * featureImportance.getEvaluationD());
		}
		return featureImportances;
	}

	@Override
	public String toString() {
		return "平方调整算法";
	}
}
