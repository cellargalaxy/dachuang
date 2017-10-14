package version4.run;

import version4.win.Win;

import java.util.concurrent.CountDownLatch;

/**
 * Created by cellargalaxy on 17-9-18.
 */
public final class TestRunParameter extends RunParameter {
	private CountDownLatch createSubDataSetCountDownLatch;
	private CountDownLatch createSubSpacesCountDownLatch;
	
	
	public void receiveCreateSubDataSet(int com0Count, int com1Count, int miss0Count, int miss1Count) {
		Win.createSubDataSet(com0Count,com1Count,miss0Count,miss1Count);
		try {
			createSubDataSetCountDownLatch=new CountDownLatch(1);
			createSubDataSetCountDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		setTest(0.5);
//		setMiss((double) (miss0Count + miss1Count) / (com0Count + com1Count + miss0Count + miss1Count));
//		setLabel1((double) (com1Count + miss1Count) / (com0Count + com1Count + miss0Count + miss1Count));
	}
	
	public void doneReceiveCreateSubDataSet() {
		createSubDataSetCountDownLatch.countDown();
	}
	
	public void receiveCreateSubSpaces(double[] impros) {
		Win.createSubSpaces(impros);
		try {
			createSubSpacesCountDownLatch=new CountDownLatch(1);
			createSubSpacesCountDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		int[] sn = new int[impros.length - 1];
//		int fnMin = 0;
//		for (int i = 0; i < sn.length; i++) {
//			sn[i] = i + 1;
//			fnMin += countC(i + 1, impros.length);
//		}
//		setSn(sn);
//		setFnMin((int) (fnMin + 0.8));
	}
	
	public void donereceiveCreateSubSpaces() {
		createSubSpacesCountDownLatch.countDown();
	}
	
	private static final int countC(int n, int m) {
		int mm = 1;
		for (int i = 0; i < n; i++) {
			mm *= (m - i);
		}
		int nn = 1;
		for (int i = 0; i < n; i++) {
			nn *= (i + 1);
		}
		return mm / nn;
	}
}
