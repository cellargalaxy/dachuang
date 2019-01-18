package top.cellargalaxy.dachuangspringboot.evaluation;

import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesisFactory;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * @author cellargalaxy
 * @time 2019/1/14
 */
public class EvaluationFactory {
	public static final String[] NAMES = {Auc.NAME, Svm.NAME};
	private static Evaluation evaluation;

	public static final Evaluation getEvaluation(RunParameter runParamete, DataSet dataSet) {
		if (evaluation == null) {
			evaluation = createEvaluation(runParamete, dataSet);
		}
		return evaluation;
	}

	public static void setEvaluation(Evaluation evaluation) {
		EvaluationFactory.evaluation = evaluation;
	}

	public static final Evaluation createEvaluation(RunParameter runParameter, DataSet dataSet) {
		String name = runParameter.getEvaluationName();
		evaluation = createEvaluation(name, runParameter, dataSet);
		return evaluation;
	}

	public static final Evaluation createEvaluation(String name, RunParameter runParameter, DataSet dataSet) {
		if (Auc.NAME.equals(name)) {
			return new Auc(EvidenceSynthesisFactory.getEvidenceSynthesis(runParameter, dataSet));
		}
		if (Svm.NAME.equals(name)) {
			return new Svm();
		}
		throw new RuntimeException("无效-Evaluation: " + name);
	}
}
