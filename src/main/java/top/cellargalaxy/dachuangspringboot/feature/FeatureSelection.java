package top.cellargalaxy.dachuangspringboot.feature;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.hereditary.Hereditary;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryParameter;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryResult;
import top.cellargalaxy.dachuangspringboot.hereditary.ParentChrosChoose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class FeatureSelection {

	public static final ArrayList<FeatureImportance> featureSelection(FeatureSplit featureSplit, double featureSelectionDeviation,
	                                                                  DataSet dataSet, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, Evaluation evaluation) throws IOException {

		HereditaryResult hereditaryResult = Hereditary.evolution(dataSet, hereditaryParameter, parentChrosChoose, evaluation);
		double fullEvaluation = hereditaryResult.getEvaluationValue();
		LinkedList<FeatureImportance> featureImportances = new LinkedList<>();
		for (Integer evidenceId : dataSet.getEvidenceName2EvidenceId().values()) {
			hereditaryResult = Hereditary.evolution(dataSet.clone(evidenceId), hereditaryParameter, parentChrosChoose, evaluation);
			double evaluationValueD = fullEvaluation - hereditaryResult.getEvaluationValue();
			featureImportances.add(new FeatureImportance(evaluationValueD, evidenceId));
		}
		featureImportances.sort(FeatureImportance::sortByEvaluationD);

		Set<Integer> importanceEvidenceIds = new HashSet<>();
		Set<Integer> unImportanceEvidenceIds = new HashSet<>();
		featureSplit.splitFeature(featureImportances, importanceEvidenceIds, unImportanceEvidenceIds);

		hereditaryResult = Hereditary.evolution(dataSet.clone(importanceEvidenceIds), hereditaryParameter, parentChrosChoose, evaluation);
		double evaluationValueD = hereditaryResult.getEvaluationValue();
		while (fullEvaluation - evaluationValueD > featureSelectionDeviation && unImportanceEvidenceIds.size() > 0) {
			double aucJ = -1;
			int evidenceId = -1;
			for (Integer unImportanceEvidenceId : unImportanceEvidenceIds) {
				Set<Integer> newImportanceEvidenceIds = importanceEvidenceIds.stream().collect(Collectors.toSet());
				newImportanceEvidenceIds.add(unImportanceEvidenceId);
				hereditaryResult = Hereditary.evolution(dataSet.clone(newImportanceEvidenceIds), hereditaryParameter, parentChrosChoose, evaluation);
				if (hereditaryResult.getEvaluationValue() > aucJ) {
					aucJ = hereditaryResult.getEvaluationValue();
					evidenceId = unImportanceEvidenceId;
				}
			}

			if (evidenceId > 0) {
				importanceEvidenceIds.add(evidenceId);
				unImportanceEvidenceIds.remove(evidenceId);
				evaluationValueD = aucJ;
			}
		}
		ArrayList<FeatureImportance> arrayList = new ArrayList<>();
		arrayList.addAll(featureImportances.stream().filter(featureImportance -> importanceEvidenceIds.contains(featureImportance.getEvidenceId())).collect(Collectors.toList()));
		return arrayList;
	}
}
