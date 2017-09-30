package concurrentTest;

import java.util.concurrent.Callable;

/**
 * Created by cellargalaxy on 17-9-30.
 */
public class Task implements Callable<String>{
	private int time;
	
	public Task(int time) {
		this.time = time;
	}
	
	public String call() throws Exception {
		Thread.sleep(time);
		return "sleep:"+time;
	}
}
