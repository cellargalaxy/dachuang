package top.cellargalaxy.dachuangspringboot.evaluation;

import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesisFactory;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * @author cellargalaxy
 * @time 2019/1/14
 */
public class EvaluationFactory {
	public static final Evaluation createEvaluation() {
		String name = RunParameter.evaluationName;
		if (Auc.NAME.equals(name)) {
			return new Auc(EvidenceSynthesisFactory.createEvidenceSynthesis());
		}
		if (Svm.NAME.equals(name)) {
			return new Svm();
		}
		throw new RuntimeException("无效-Evaluation: " + name);
	}
}
