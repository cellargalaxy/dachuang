package top.cellargalaxy.dachuangspringboot.evaluation;

import top.cellargalaxy.dachuangspringboot.run.Run;
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
		int threadNum;
		if (RunParameter.getThreadNum() > 0) {
			threadNum = RunParameter.getThreadNum();
		} else if (Runtime.getRuntime().availableProcessors() + RunParameter.getThreadNum() > 0) {
			threadNum = Runtime.getRuntime().availableProcessors() + RunParameter.getThreadNum();
		} else {
			threadNum = 1;
		}
		Run.logger.info("CPU核数: {}", Runtime.getRuntime().availableProcessors());
		Run.logger.info("线程池线程数: {}", threadNum);
		EXECUTOR_SERVICE = Executors.newFixedThreadPool(threadNum);
	}

	public static final void shutdownExecutorService() {
		EXECUTOR_SERVICE.shutdown();
	}
}
