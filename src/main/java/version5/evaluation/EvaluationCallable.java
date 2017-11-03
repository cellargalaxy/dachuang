package version5.evaluation;

import version5.dataSet.DataSet;

import java.util.concurrent.Callable;

/**
 * Created by cellargalaxy on 17-9-30.
 */
public final class EvaluationCallable implements Callable<Double> {
	private final Evaluation evaluation;
	private final DataSet dataSet;
	
	public EvaluationCallable(Evaluation evaluation, DataSet dataSet) {
		this.evaluation = evaluation;
		this.dataSet = dataSet;
	}
	
	public Double call() throws Exception {
		return evaluation.countEvaluation(dataSet);
	}
}
