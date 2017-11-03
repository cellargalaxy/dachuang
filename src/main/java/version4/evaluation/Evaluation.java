package version4.evaluation;

import version4.dataSet.DataSet;

import java.io.IOException;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface Evaluation {
	double countEvaluation(DataSet cloneDataSet);
	
	double countEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum);
	
	double countEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException;
	
	double countIndexEvaluation(DataSet dataSet, double[] chro);//下标
	
	double countOrderEvaluation(DataSet cloneDataSet, double[] chro);//顺序
	
	double countIndexEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum, double[] chro);//下标
	
	double countOrderEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException;//顺序
}