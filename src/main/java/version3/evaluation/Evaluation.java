package version3.evaluation;

import version3.dataSet.DataSet;

import java.io.IOException;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface Evaluation {
	double countEvaluation(DataSet dataSet);
	
	double countEvaluation(DataSet dataSet, Integer withoutEvidNum);
	
	double countEvaluation(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException;
	
	double countIndexEvaluation(DataSet dataSet, double[] chro);//下标
	
	double countOrderEvaluation(DataSet cloneDataSet, double[] chro);//顺序
	
	double countIndexEvaluation(DataSet dataSet, Integer withoutEvidNum, double[] chro);//下标
	
	double countOrderEvaluation(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException;//顺序
}
