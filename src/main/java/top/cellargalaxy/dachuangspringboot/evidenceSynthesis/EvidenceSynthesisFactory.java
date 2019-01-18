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
	public static final String[] NAMES = {AverageEvidenceSynthesis.NAME, DsEvidenceSynthesis.NAME, EuclideanDistanceEvidenceSynthesis.NAME, EuclideanDistanceWeightEvidenceSynthesis.NAME, VoteEvidenceSynthesis.NAME};
	private static EvidenceSynthesis evidenceSynthesis;

	public static final EvidenceSynthesis getEvidenceSynthesis(RunParameter runParameter, DataSet dataSet) {
		if (evidenceSynthesis == null) {
			evidenceSynthesis = createEvidenceSynthesis(runParameter, dataSet);
		}
		return evidenceSynthesis;
	}

	public static void setEvidenceSynthesis(EvidenceSynthesis evidenceSynthesis) {
		EvidenceSynthesisFactory.evidenceSynthesis = evidenceSynthesis;
	}

	public static final EvidenceSynthesis createEvidenceSynthesis(RunParameter runParameter, DataSet dataSet) {
		String name = runParameter.getEvidenceSynthesisName();
		evidenceSynthesis = createEvidenceSynthesis(name, runParameter, dataSet);
		return evidenceSynthesis;
	}

	public static final EvidenceSynthesis createEvidenceSynthesis(String name, RunParameter runParameter, DataSet dataSet) {
		if (StringUtils.isBlank(name)) {
			if (dataSet != null) {
				return createEvidenceSynthesis(runParameter, dataSet, ParentChrosChooseFactory.getParentChrosChoose(runParameter));
			}
		}
		if (AverageEvidenceSynthesis.NAME.equals(name)) {
			return new AverageEvidenceSynthesis();
		}
		if (DsEvidenceSynthesis.NAME.equals(name)) {
			return new DsEvidenceSynthesis();
		}
		if (EuclideanDistanceEvidenceSynthesis.NAME.equals(name)) {
			return new EuclideanDistanceEvidenceSynthesis();
		}
		if (EuclideanDistanceWeightEvidenceSynthesis.NAME.equals(name)) {
			return new EuclideanDistanceWeightEvidenceSynthesis();
		}
		if (VoteEvidenceSynthesis.NAME.equals(name)) {
			return new VoteEvidenceSynthesis(runParameter.getThrf(), runParameter.getThrnf(), runParameter.getD1(), runParameter.getD2());
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
