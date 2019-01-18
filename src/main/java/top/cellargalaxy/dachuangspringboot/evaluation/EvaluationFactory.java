package top.cellargalaxy.dachuangspringboot.evaluation;

import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesisFactory;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * @author cellargalaxy
 * @time 2019/1/14
 */
public class EvaluationFactory {
	private static Evaluation evaluation;

	public static final Evaluation createEvaluation(RunParameter runParameter) {
		String name = runParameter.getEvaluationName();
		if (Auc.NAME.equals(name)) {
			evaluation = new Auc(EvidenceSynthesisFactory.getEvidenceSynthesis());
			return evaluation;
		}
		if (Svm.NAME.equals(name)) {
			evaluation = new Svm();
			return evaluation;
		}
		throw new RuntimeException("无效-Evaluation: " + name);
	}

	public static final Evaluation getEvaluation() {
		return evaluation;
	}
}
