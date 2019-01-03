package top.cellargalaxy.dachuangspringboot.evaluation;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;

import java.util.concurrent.Callable;

/**
 * Created by cellargalaxy on 17-9-30.
 */
public final class EvaluationWithoutEvidNumCallable implements Callable<Double> {
	private final Evaluation evaluation;
	private final DataSet dataSet;
	private final Integer withoutEvidNum;
	
	public EvaluationWithoutEvidNumCallable(Evaluation evaluation, DataSet dataSet, Integer withoutEvidNum) {
		this.evaluation = evaluation;
		this.dataSet = dataSet;
		this.withoutEvidNum = withoutEvidNum;
	}
	
	public Double call() throws Exception {
		return evaluation.countEvaluationWithoutEvidNum(dataSet, withoutEvidNum);
	}
}
