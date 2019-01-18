package top.cellargalaxy.dachuangspringboot.subSpace;

import top.cellargalaxy.dachuangspringboot.feature.FeatureImportance;

import java.util.ArrayList;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class SubtractionImprotenceAdjust implements ImprotenceAdjust {
	public static final String NAME = "减法调整算法";
	private final double adjustD;

	public SubtractionImprotenceAdjust(double adjustD) {
		this.adjustD = adjustD;
	}

	@Override
	public ArrayList<FeatureImportance> adjustImportance(ArrayList<FeatureImportance> featureImportances) {
		double min = Math.abs(featureImportances.stream().min(FeatureImportance::sortAscByEvaluationD).get().getEvaluationD());
		for (FeatureImportance featureImportance : featureImportances) {
			featureImportance.setEvaluationD(Math.abs(featureImportance.getEvaluationD()) - (min - adjustD));
		}
		return featureImportances;
	}

	@Override
	public String toString() {
		return "减法调整算法{" +
				"adjustD=" + adjustD +
				'}';
	}

}
