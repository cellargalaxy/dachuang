package top.cellargalaxy.dachuangspringboot.feature;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by cellargalaxy on 17-11-1.
 */
@Data
@AllArgsConstructor
public class FeatureImportance {
	private double evaluationD;
	private int evidenceId;

	public static final int sortAscByEvaluationD(FeatureImportance featureImportance1, FeatureImportance featureImportance2) {
		if (featureImportance1.getEvaluationD() < featureImportance2.getEvaluationD()) {
			return -1;
		} else if (featureImportance1.getEvaluationD() > featureImportance2.getEvaluationD()) {
			return 1;
		} else {
			return 0;
		}
	}
}
