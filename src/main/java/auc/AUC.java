package auc;

import dataSet.DataSet;
import dataSet.Id;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/4/22.
 * 用于计算AUC的静态类
 */
public class AUC {

	public static double countTestAUC(DataSet dataSet) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = id.getEvidenceDS();
			if (is1(id)) ds1s.add(ds);
			else ds0s.add(ds);
		}
		return countAUC(ds1s, ds0s);
	}

//	public static double countAUCWiths(DataSet dataSet, LinkedList<Integer> evidenceNums) {
//		LinkedList<double[]> ds1s = new LinkedList<double[]>();
//		LinkedList<double[]> ds0s = new LinkedList<double[]>();
//		for (Id id : dataSet.getIds()) {
//			double[] ds = id.countDSWiths(evidenceNums);
//			if (is1(id)) ds1s.add(ds);
//			else ds0s.add(ds);
//		}
//		return countAUC(ds1s, ds0s);
//	}
//
//	public static double countAUCWithouts(DataSet dataSet, LinkedList<Integer> exceptEvidenceNums) {
//		LinkedList<double[]> ds1s = new LinkedList<double[]>();
//		LinkedList<double[]> ds0s = new LinkedList<double[]>();
//		for (Id id : dataSet.getIds()) {
//			double[] ds = id.countDSWithouts(exceptEvidenceNums);
//			if (is1(id)) ds1s.add(ds);
//			else ds0s.add(ds);
//		}
//		return countAUC(ds1s, ds0s);
//	}
//
//	public static double countAUCWithout(DataSet dataSet, int exceptEvidenceNum) {
//		LinkedList<double[]> ds1s = new LinkedList<double[]>();
//		LinkedList<double[]> ds0s = new LinkedList<double[]>();
//		for (Id id : dataSet.getIds()) {
//			double[] ds = id.countDSWithout(exceptEvidenceNum);
//			if (is1(id)) ds1s.add(ds);
//			else ds0s.add(ds);
//		}
//		return countAUC(ds1s, ds0s);
//	}

	public static double countAUCAll(DataSet dataSet) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = id.countDSAll();
			if (is1(id)) ds1s.add(ds);
			else ds0s.add(ds);
		}
		return countAUC(ds1s, ds0s);
	}

	private static double countAUC(LinkedList<double[]> ds1s, LinkedList<double[]> ds0s) {
		double auc = 0;
		for (double[] ds1 : ds1s) {
			for (double[] ds0 : ds0s) {
				if (ds1[1] > ds0[1]) {
					auc++;
				} else if (ds1[1] == ds0[1]) {
					auc = auc + 0.5;
				}
			}
		}
		return auc / ds1s.size() / ds0s.size();
	}

	private static boolean is1(Id id) {
		if (id.getLabel() == Id.LABEL_1) {
			return true;
		} else if (id.getLabel() == Id.LABEL_0) {
			return false;
		} else {
			throw new RuntimeException("嫌疑人(" + id.getId() + ")标签有误：" + id.getLabel());
		}
	}
}
