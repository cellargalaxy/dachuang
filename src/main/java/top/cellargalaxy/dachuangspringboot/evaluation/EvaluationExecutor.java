package top.cellargalaxy.dachuangspringboot.evaluation;



import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by cellargalaxy on 17-11-12.
 */
public interface EvaluationExecutor {
	Future<Double> countEvaluation(DataSet dataSet) throws IOException;
	
	Future<Double> countEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum) throws IOException;
	
	Future<Double> countEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException;
	
	Future<Double> countIndexEvaluation(DataSet dataSet, double[] chro) throws IOException;
	
	Future<Double> countOrderEvaluation(DataSet cloneDataSet, double[] chro) throws IOException;
	
	Future<Double> countIndexEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum, double[] chro) throws IOException;
	
	Future<Double> countOrderEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException;
	
	void shutdown();
}
