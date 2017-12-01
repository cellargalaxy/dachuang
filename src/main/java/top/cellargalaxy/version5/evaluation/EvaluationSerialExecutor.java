package top.cellargalaxy.version5.evaluation;


import top.cellargalaxy.version5.dataSet.DataSet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by cellargalaxy on 17-11-12.
 */
public class EvaluationSerialExecutor implements EvaluationExecutor {
	private final Evaluation evaluation;
	
	public EvaluationSerialExecutor(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
	
	public Future<Double> countEvaluation(DataSet dataSet) throws IOException {
		return new SerialFuture(evaluation.countEvaluation(dataSet));
	}
	
	public Future<Double> countEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum) throws IOException {
		return new SerialFuture(evaluation.countEvaluationWithoutEvidNum(dataSet, withoutEvidNum));
	}
	
	public Future<Double> countEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException {
		return new SerialFuture(evaluation.countEvaluationWithEvidNums(dataSet, withEvidNums));
	}
	
	public Future<Double> countIndexEvaluation(DataSet dataSet, double[] chro) throws IOException {
		return new SerialFuture(evaluation.countIndexEvaluation(dataSet, chro));
	}
	
	public Future<Double> countOrderEvaluation(DataSet cloneDataSet, double[] chro) throws IOException {
		return new SerialFuture(evaluation.countOrderEvaluation(cloneDataSet, chro));
	}
	
	public Future<Double> countIndexEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum, double[] chro) throws IOException {
		return new SerialFuture(evaluation.countIndexEvaluationWithoutEvidNum(dataSet, withoutEvidNum, chro));
	}
	
	public Future<Double> countOrderEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException {
		return new SerialFuture(evaluation.countOrderEvaluationWithEvidNums(dataSet, withEvidNums, chro));
	}
	
	public void shutdown() {
	
	}
}
