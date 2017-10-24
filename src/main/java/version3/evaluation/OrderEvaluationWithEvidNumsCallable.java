//package version3.evaluation;
//
//import version3.dataSet.DataSet;
//
//import java.util.List;
//import java.util.concurrent.Callable;
//
///**
// * Created by cellargalaxy on 17-9-30.
// */
//public final class OrderEvaluationWithEvidNumsCallable implements Callable<Double> {
//	private final Evaluation evaluation;
//	private final DataSet dataSet;
//	private final List<Integer> withEvidNums;
//	private final double[] chro;
//
//	public OrderEvaluationWithEvidNumsCallable(Evaluation evaluation, DataSet dataSet, List<Integer> withEvidNums, double[] chro) {
//		this.evaluation = evaluation;
//		this.dataSet = dataSet;
//		this.withEvidNums = withEvidNums;
//		this.chro = chro;
//	}
//
//	public Double call() throws Exception {
//		return evaluation.countOrderEvaluationWithEvidNums(dataSet, withEvidNums, chro);
//	}
//}
