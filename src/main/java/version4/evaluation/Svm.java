package version4.evaluation;

import version4.dataSet.DataSet;

import java.io.IOException;
import java.util.List;

/**
 * Created by cellargalaxy on 17-10-24.
 */
public class Svm implements Evaluation {
	
	public double countEvaluation(DataSet cloneDataSet) {
		return 0;
	}
	
	public double countEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum) {
		return 0;
	}
	
	public double countEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException {
		return 0;
	}
	
	public double countIndexEvaluation(DataSet dataSet, double[] chro) {
		return 0;
	}
	
	public double countOrderEvaluation(DataSet cloneDataSet, double[] chro) {
		return 0;
	}
	
	public double countIndexEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum, double[] chro) {
		return 0;
	}
	
	public double countOrderEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException {
		return 0;
	}
}
