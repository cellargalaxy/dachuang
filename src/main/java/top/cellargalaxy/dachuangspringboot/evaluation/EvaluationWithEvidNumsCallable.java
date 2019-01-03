package top.cellargalaxy.dachuangspringboot.evaluation;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by cellargalaxy on 17-9-30.
 */
public final class EvaluationWithEvidNumsCallable implements Callable<Double> {
	private final Evaluation evaluation;
	private final DataSet dataSet;
	private final List<Integer> withEvidNums;
	
	public EvaluationWithEvidNumsCallable(Evaluation evaluation, DataSet dataSet, List<Integer> withEvidNums) {
		this.evaluation = evaluation;
		this.dataSet = dataSet;
		this.withEvidNums = withEvidNums;
	}
	
	public Double call() throws Exception {
		return evaluation.countEvaluationWithEvidNums(dataSet, withEvidNums);
	}
}
