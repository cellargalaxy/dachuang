package version5.evaluation;

import version5.dataSet.DataSet;
import version5.evidenceSynthesis.Distance2EvidenceSynthesis;
import version5.evidenceSynthesis.DistanceEvidenceSynthesis;
import version5.evidenceSynthesis.EvidenceSynthesis;
import version5.evidenceSynthesis.TeaEvidenceSynthesis;
import version5.hereditary.Hereditary;
import version5.hereditary.HereditaryParameter;
import version5.hereditary.ParentChrosChoose;

import java.io.IOException;

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
	
	public static final Evaluation createEvaluation(int evaluationNum, DataSet dataSet, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose) throws IOException, ClassNotFoundException {
		if (evaluationNum == AUC_NUM) {
			return createAuc(dataSet, hereditaryParameter, parentChrosChoose);
		} else if (evaluationNum == SVM_NUM) {
			return createSvm();
		}
		throw new RuntimeException("无效evaluationNum: " + evaluationNum);
	}
	
	public static final boolean check(Integer evaluationNum, DataSet dataSet, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose) {
		if (evaluationNum==null) {
			return false;
		}
		if (evaluationNum.equals(AUC_NUM)) {
			return dataSet != null && hereditaryParameter != null && parentChrosChoose != null;
		} else if (evaluationNum.equals(SVM_NUM)) {
			return true;
		}
		return false;
	}
	
	private static final Evaluation createAuc(DataSet dataSet, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose) throws IOException, ClassNotFoundException {
		Evaluation evaluation = null;
		double dsAuc = -1;
		Hereditary hereditary = new Hereditary(dataSet);
		for (EvidenceSynthesis evidenceSynthesis1 : evidenceSyntheses) {
			Evaluation evaluation1 = new Auc(evidenceSynthesis1);
			hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation1);
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
