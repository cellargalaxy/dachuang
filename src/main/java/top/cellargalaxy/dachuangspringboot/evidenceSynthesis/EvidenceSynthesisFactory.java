package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;

import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.evaluation.Auc;
import top.cellargalaxy.dachuangspringboot.hereditary.Hereditary;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryResult;
import top.cellargalaxy.dachuangspringboot.hereditary.ParentChrosChoose;
import top.cellargalaxy.dachuangspringboot.hereditary.ParentChrosChooseFactory;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;
import top.cellargalaxy.dachuangspringboot.util.StringUtils;

/**
 * @author cellargalaxy
 * @time 2019/1/14
 */
public class EvidenceSynthesisFactory {
	private static EvidenceSynthesis evidenceSynthesis;

	public static final EvidenceSynthesis getEvidenceSynthesis() {
		return evidenceSynthesis;
	}

	public static final EvidenceSynthesis createEvidenceSynthesis(RunParameter runParameter, DataSet dataSet) {
		String name = runParameter.getEvidenceSynthesisName();
		if (StringUtils.isBlank(name)) {
			evidenceSynthesis = createEvidenceSynthesis(runParameter, dataSet, ParentChrosChooseFactory.createParentChrosChoose(runParameter));
			return evidenceSynthesis;
		}
		if (AverageEvidenceSynthesis.NAME.equals(name)) {
			evidenceSynthesis = new AverageEvidenceSynthesis();
			return evidenceSynthesis;
		}
		if (DsEvidenceSynthesis.NAME.equals(name)) {
			evidenceSynthesis = new DsEvidenceSynthesis();
			return evidenceSynthesis;
		}
		if (EuclideanDistanceEvidenceSynthesis.NAME.equals(name)) {
			evidenceSynthesis = new EuclideanDistanceEvidenceSynthesis();
			return evidenceSynthesis;
		}
		if (EuclideanDistanceWeightEvidenceSynthesis.NAME.equals(name)) {
			evidenceSynthesis = new EuclideanDistanceWeightEvidenceSynthesis();
			return evidenceSynthesis;
		}
		if (VoteEvidenceSynthesis.NAME.equals(name)) {
			evidenceSynthesis = new VoteEvidenceSynthesis(runParameter.getThrf(), runParameter.getThrnf(), runParameter.getD1(), runParameter.getD2());
			return evidenceSynthesis;
		}
		throw new RuntimeException("无效-EvidenceSynthesis: " + name);
	}

	private static final EvidenceSynthesis createEvidenceSynthesis(RunParameter runParameter, DataSet dataSet, ParentChrosChoose parentChrosChoose) {
		EvidenceSynthesis fitEvidenceSynthesis = null;
		HereditaryResult fitHereditaryResult = null;

		EvidenceSynthesis evidenceSynthesis;
		Auc auc;
		HereditaryResult hereditaryResult;

		try {
			evidenceSynthesis = new AverageEvidenceSynthesis();
			auc = new Auc(evidenceSynthesis);
			hereditaryResult = Hereditary.evolution(dataSet, runParameter.getHereditaryParameter(), parentChrosChoose, auc);
			if (fitHereditaryResult == null || hereditaryResult.getEvaluationValue() > fitHereditaryResult.getEvaluationValue()) {
				fitEvidenceSynthesis = evidenceSynthesis;
				fitHereditaryResult = hereditaryResult;
			}
		} catch (Exception e) {
		}

		try {
			evidenceSynthesis = new DsEvidenceSynthesis();
			auc = new Auc(evidenceSynthesis);
			hereditaryResult = Hereditary.evolution(dataSet, runParameter.getHereditaryParameter(), parentChrosChoose, auc);
			if (fitHereditaryResult == null || hereditaryResult.getEvaluationValue() > fitHereditaryResult.getEvaluationValue()) {
				fitEvidenceSynthesis = evidenceSynthesis;
				fitHereditaryResult = hereditaryResult;
			}
		} catch (Exception e) {
		}

		try {
			evidenceSynthesis = new EuclideanDistanceEvidenceSynthesis();
			auc = new Auc(evidenceSynthesis);
			hereditaryResult = Hereditary.evolution(dataSet, runParameter.getHereditaryParameter(), parentChrosChoose, auc);
			if (fitHereditaryResult == null || hereditaryResult.getEvaluationValue() > fitHereditaryResult.getEvaluationValue()) {
				fitEvidenceSynthesis = evidenceSynthesis;
				fitHereditaryResult = hereditaryResult;
			}
		} catch (Exception e) {
		}

		try {
			evidenceSynthesis = new EuclideanDistanceWeightEvidenceSynthesis();
			auc = new Auc(evidenceSynthesis);
			hereditaryResult = Hereditary.evolution(dataSet, runParameter.getHereditaryParameter(), parentChrosChoose, auc);
			if (fitHereditaryResult == null || hereditaryResult.getEvaluationValue() > fitHereditaryResult.getEvaluationValue()) {
				fitEvidenceSynthesis = evidenceSynthesis;
				fitHereditaryResult = hereditaryResult;
			}
		} catch (Exception e) {
		}

		try {
			evidenceSynthesis = new VoteEvidenceSynthesis(runParameter.getThrf(), runParameter.getThrnf(), runParameter.getD1(), runParameter.getD2());
			auc = new Auc(evidenceSynthesis);
			hereditaryResult = Hereditary.evolution(dataSet, runParameter.getHereditaryParameter(), parentChrosChoose, auc);
			if (fitHereditaryResult == null || hereditaryResult.getEvaluationValue() > fitHereditaryResult.getEvaluationValue()) {
				fitEvidenceSynthesis = evidenceSynthesis;
				fitHereditaryResult = hereditaryResult;
			}
		} catch (Exception e) {
		}

		return fitEvidenceSynthesis;
	}
}
