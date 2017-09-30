package concurrentTest;

import java.util.concurrent.*;

/**
 * Created by cellargalaxy on 17-9-30.
 */
public class Test {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		Task[] tasks=new Task[3];
		for (int i = 0; i < tasks.length; i++) {
			tasks[i]=new Task(2000*(i+1));
		}
		
		Future<String>[] futures=new Future[tasks.length];
		ExecutorService executorService= Executors.newFixedThreadPool(3);
		
		System.out.println("提交任务开始");
		for (int i = 0; i < tasks.length; i++) {
			futures[i]=executorService.submit(tasks[i]);
		}
		System.out.println("提交任务结束");
		
		for (Future<String> future : futures) {
			System.out.println(future.get());
		}
		
		executorService.shutdown();
	}
}
