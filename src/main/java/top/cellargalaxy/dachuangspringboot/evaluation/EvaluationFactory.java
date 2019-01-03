package top.cellargalaxy.dachuangspringboot.evaluation;


import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.Distance2EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.DistanceEvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.TeaEvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.hereditary.Hereditary;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryParameter;
import top.cellargalaxy.dachuangspringboot.hereditary.ParentChrosChoose;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by cellargalaxy on 17-11-2.
 */
public class EvaluationFactory {
	/**
	 * auc
	 */
	public static final int AUC_NUM = 1;
	/**
	 * svm
	 */
	public static final int SVM_NUM = 2;
	private static final EvidenceSynthesis[] evidenceSyntheses = {new TeaEvidenceSynthesis(), new DistanceEvidenceSynthesis(), new Distance2EvidenceSynthesis()};

	public static final Evaluation createEvaluation(int evaluationNum, Hereditary hereditary, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
		if (evaluationNum == AUC_NUM) {
			return createAuc(hereditary, hereditaryParameter, parentChrosChoose);
		} else if (evaluationNum == SVM_NUM) {
			return createSvm();
		}
		throw new RuntimeException("无效evaluationNum: " + evaluationNum);
	}

	public static final boolean check(Integer evaluationNum, Hereditary hereditary, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose) {
		if (evaluationNum == null) {
			return false;
		}
		if (evaluationNum.equals(AUC_NUM)) {
			return hereditary != null && hereditaryParameter != null && parentChrosChoose != null;
		} else if (evaluationNum.equals(SVM_NUM)) {
			return true;
		}
		return false;
	}

	private static final Evaluation createAuc(Hereditary hereditary, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
		Evaluation evaluation = null;
		double dsAuc = -1;
		for (EvidenceSynthesis evidenceSynthesis1 : evidenceSyntheses) {
			Evaluation evaluation1 = new Auc(evidenceSynthesis1);
			hereditary.evolution(hereditaryParameter, parentChrosChoose, EvaluationExecutorFactory.createEvaluationExecutor(EvaluationExecutorFactory.EVALUATION_SERIAL_EXECUTOR_NUM, evaluation1));
			if (hereditary.getMaxAuc() > dsAuc) {
				dsAuc = hereditary.getMaxAuc();
				evaluation = evaluation1;
			}
		}
		return evaluation;
	}

	private static final Evaluation createSvm() {
		return new Svm();
	}
}
