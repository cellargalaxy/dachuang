package version3.evaluation;

import version3.dataSet.DataSet;

import java.util.concurrent.Callable;

/**
 * Created by cellargalaxy on 17-9-30.
 */
public final class IndexEvaluationCallable implements Callable<Double> {
	private final Evaluation evaluation;
	private final DataSet dataSet;
	private final double[] chro;
	
	public IndexEvaluationCallable(Evaluation evaluation, DataSet dataSet, double[] chro) {
		this.evaluation = evaluation;
		this.dataSet = dataSet;
		this.chro = chro;
	}
	
	public Double call() throws Exception {
		return evaluation.countIndexEvaluation(dataSet, chro);
	}
}
