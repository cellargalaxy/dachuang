//package version3.evaluation;
//
//import version3.dataSet.DataSet;
//
//import java.util.concurrent.Callable;
//
///**
// * Created by cellargalaxy on 17-9-30.
// */
//public final class OrderEvaluationCallable implements Callable<Double> {
//	private final Evaluation evaluation;
//	private final DataSet cloneDataSet;
//	private final double[] chro;
//
//	public OrderEvaluationCallable(Evaluation evaluation, DataSet cloneDataSet, double[] chro) {
//		this.evaluation = evaluation;
//		this.cloneDataSet = cloneDataSet;
//		this.chro = chro;
//	}
//
//	public Double call() throws Exception {
//		return evaluation.countOrderEvaluation(cloneDataSet, chro);
//	}
//}
