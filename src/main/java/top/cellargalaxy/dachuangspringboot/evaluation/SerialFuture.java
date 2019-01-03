package top.cellargalaxy.dachuangspringboot.evaluation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by cellargalaxy on 17-11-12.
 */
public class SerialFuture implements Future<Double> {
	private final double d;
	
	public SerialFuture(double d) {
		this.d = d;
	}
	
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}
	
	public boolean isCancelled() {
		return false;
	}
	
	public boolean isDone() {
		return false;
	}
	
	public Double get() throws InterruptedException, ExecutionException {
		return d;
	}
	
	public Double get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return d;
	}
}
