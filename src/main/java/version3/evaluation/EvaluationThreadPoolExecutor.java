package version3.evaluation;

import version3.dataSet.DataSet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by cellargalaxy on 17-9-30.
 */
public class EvaluationThreadPoolExecutor {
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	private final Evaluation evaluation;
	
	public EvaluationThreadPoolExecutor(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
	
	public final Future<Double> countEvaluation(DataSet dataSet) {
		return EXECUTOR_SERVICE.submit(new EvaluationCallable(evaluation, dataSet));
	}
	
	public final Future<Double> countEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum) {
		return EXECUTOR_SERVICE.submit(new EvaluationWithoutEvidNumCallable(evaluation, dataSet, withoutEvidNum));
	}
	
	public final Future<Double> countEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException {
		return EXECUTOR_SERVICE.submit(new EvaluationWithEvidNumsCallable(evaluation, dataSet, withEvidNums));
	}
	
	public final Future<Double> countIndexEvaluation(DataSet dataSet, double[] chro) {//下标
		return EXECUTOR_SERVICE.submit(new IndexEvaluationCallable(evaluation, dataSet, chro));
	}
	
	public final Future<Double> countOrderEvaluation(DataSet cloneDataSet, double[] chro) {//顺序
		return EXECUTOR_SERVICE.submit(new OrderEvaluationCallable(evaluation, cloneDataSet, chro));
	}
	
	public final Future<Double> countIndexEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum, double[] chro) {//下标
		return EXECUTOR_SERVICE.submit(new IndexEvaluationWithoutEvidNumCallable(evaluation, dataSet, withoutEvidNum, chro));
	}
	
	public final Future<Double> countOrderEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException {//顺序
		return EXECUTOR_SERVICE.submit(new OrderEvaluationWithEvidNumsCallable(evaluation, dataSet, withEvidNums, chro));
	}
	
	public static final void shutdown() {
		EXECUTOR_SERVICE.shutdown();
	}
	
}
