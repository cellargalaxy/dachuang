package top.cellargalaxy.dachuangspringboot.evaluation;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cellargalaxy
 * @date 2019/3/8
 */
public abstract class AbstractEvaluation implements Evaluation {
	static final ExecutorService EXECUTOR_SERVICE;

	static {
		if (RunParameter.getThreadNum() > 0) {
			EXECUTOR_SERVICE = Executors.newFixedThreadPool(RunParameter.getThreadNum());
		} else if (Runtime.getRuntime().availableProcessors() - RunParameter.getThreadNum() > 0) {
			EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - RunParameter.getThreadNum());
		} else {
			EXECUTOR_SERVICE = Executors.newFixedThreadPool(1);
		}
	}

	public static final void shutdownExecutorService() {
		EXECUTOR_SERVICE.shutdown();
	}
}
