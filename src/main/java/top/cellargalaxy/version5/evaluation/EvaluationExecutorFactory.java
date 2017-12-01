package top.cellargalaxy.version5.evaluation;

/**
 * Created by cellargalaxy on 17-11-12.
 */
public class EvaluationExecutorFactory {
	/**
	 * 单线程
	 */
	public static final int EVALUATION_SERIAL_EXECUTOR_NUM = 1;
	/**
	 * 多线程
	 */
	public static final int EVALUATION_THREAD_POOL_EXECUTOR_NUM = 2;
	
	private static EvaluationSerialExecutor evaluationSerialExecutor;
	private static EvaluationThreadPoolExecutor evaluationThreadPoolExecutor;
	
	public static final EvaluationExecutor createEvaluationExecutor(int evaluationExecutorNum, Evaluation evaluation) {
		if (evaluationExecutorNum == EVALUATION_SERIAL_EXECUTOR_NUM) {
			if (evaluationSerialExecutor == null) {
				evaluationSerialExecutor = new EvaluationSerialExecutor(evaluation);
			}
			return evaluationSerialExecutor;
		} else if (evaluationExecutorNum == EVALUATION_THREAD_POOL_EXECUTOR_NUM) {
			if (evaluationThreadPoolExecutor == null) {
				evaluationThreadPoolExecutor = new EvaluationThreadPoolExecutor(evaluation);
			}
			return evaluationThreadPoolExecutor;
		}
		throw new RuntimeException("无效evaluationExecutorNum: " + evaluationExecutorNum);
	}
	
	public static final boolean check(Integer evaluationExecutorNum, Evaluation evaluation) {
		if (evaluationExecutorNum == null) {
			return false;
		}
		if (evaluationExecutorNum.equals(EVALUATION_SERIAL_EXECUTOR_NUM)) {
			return evaluation != null;
		} else if (evaluationExecutorNum.equals(EVALUATION_THREAD_POOL_EXECUTOR_NUM)) {
			return evaluation != null;
		}
		return false;
	}
	
	public static final void shutdown() {
		if (evaluationSerialExecutor != null) {
			evaluationSerialExecutor.shutdown();
		}
		if (evaluationThreadPoolExecutor != null) {
			evaluationThreadPoolExecutor.shutdown();
		}
	}
}
