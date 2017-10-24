//package version3.run;
//
//import version3.CreateSubDataSetWin;
//import version3.CreateSubSpacesWin;
//
///**
// * Created by cellargalaxy on 17-9-18.
// */
//public final class TestRunParameter extends RunParameter {
//	public void receiveCreateSubDataSet(int com0Count, int com1Count, int miss0Count, int miss1Count) {
////		CreateSubDataSetWin.receiveCreateSubDataSet(com0Count,com1Count,miss0Count,miss1Count,this);
//
//		setTest(0.5);
//		setMiss((double) (miss0Count + miss1Count) / (com0Count + com1Count + miss0Count + miss1Count));
//		setLabel1((double) (com1Count + miss1Count) / (com0Count + com1Count + miss0Count + miss1Count));
//	}
//
//	public void receiveCreateSubSpaces(double[] impros) {
////		CreateSubSpacesWin.receiveCreateSubSpaces(impros,this);
//
//		int[] sn = new int[impros.length - 1];
//		int fnMin = 0;
//		for (int i = 0; i < sn.length; i++) {
//			sn[i] = i + 1;
//			fnMin += countC(i + 1, impros.length);
//		}
//		setSn(sn);
//		setFnMin((int) (fnMin + 0.8));
//	}
//
//	private static final int countC(int n, int m) {
//		int mm = 1;
//		for (int i = 0; i < n; i++) {
//			mm *= (m - i);
//		}
//		int nn = 1;
//		for (int i = 0; i < n; i++) {
//			nn *= (i + 1);
//		}
//		return mm / nn;
//	}
//}
