package auc;

import dataSet.DataSet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/4/22.
 * 用于计算AUC的静态类
 */
public class AUC {
	
	public static void main(String[] args) throws IOException {
		DataSet dataSet = new DataSet(new File("F:/xi/dachuang/test 合成与AUC 去除totle.csv"), ",", 0, 1, 2, 4, 6);
		LinkedList<Integer> ms = new LinkedList<Integer>();
		ms.add(-1);
		System.out.println("答案:" + AUC.countAUC(dataSet, ms));
		
	}
	
	public static double countAUC(DataSet dataSet, LinkedList<Integer> exceptEvidenceNums) {
		LinkedList<double[]> As = new LinkedList<double[]>();
		LinkedList<double[]> Bs = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = id.countDSWithouts(exceptEvidenceNums);
			if (isA(id)) As.add(ds);
			else Bs.add(ds);
		}
		double auc = 0;
		for (double[] a : As) {
			for (double[] b : Bs) {
				if (a[1] > b[1]) auc++;
				else if (a[1] == b[1]) auc += 0.5;
			}
		}
		return auc / As.size() / Bs.size();
	}
	
	public static double countAUC(DataSet dataSet, int exceptEvidenceNum) {
		LinkedList<double[]> As = new LinkedList<double[]>();
		LinkedList<double[]> Bs = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = id.countDSWithout(exceptEvidenceNum);
			if (isA(id)) As.add(ds);
			else Bs.add(ds);
		}
		double auc = 0;
		for (double[] a : As) {
			for (double[] b : Bs) {
				if (a[1] > b[1]) auc++;
				else if (a[1] == b[1]) auc += 0.5;
			}
		}
		return auc / As.size() / Bs.size();
	}
	
	private static boolean isA(Id id) {
		if (id.getLabel() == Id.LABEL_A) {
			return true;
		} else if (id.getLabel() == Id.LABEL_B) {
			return false;
		} else {
			throw new RuntimeException("嫌疑人(" + id.getId() + ")标签有误：" + id.getLabel());
		}
	}
}
