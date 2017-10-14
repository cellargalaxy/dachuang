package version4.evaluation;

import version4.dataSet.DataSet;

import java.util.concurrent.Callable;

/**
 * Created by cellargalaxy on 17-9-30.
 */
public final class IndexEvaluationWithoutEvidNumCallable implements Callable<Double> {
	private final Evaluation evaluation;
	private final DataSet dataSet;
	private final Integer withoutEvidNum;
	private final double[] chro;
	
	public IndexEvaluationWithoutEvidNumCallable(Evaluation evaluation, DataSet dataSet, Integer withoutEvidNum, double[] chro) {
		this.evaluation = evaluation;
		this.dataSet = dataSet;
		this.withoutEvidNum = withoutEvidNum;
		this.chro = chro;
	}
	
	public Double call() throws Exception {
		return evaluation.countIndexEvaluationWithoutEvidNum(dataSet, withoutEvidNum, chro);
	}
}
