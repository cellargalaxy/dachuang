package auc;

import dataSet.DataSet;
import dataSet.LabelSet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/4/22.
 */
public class AUC {
	
	public static void main(String[] args) throws IOException {
		LinkedList<Id> ids = DataSet.createIds(new File("F:/xi/dachuang/test 合成与AUC 去除totle.csv"), ",",0,1,2,4,6);
		

		
		System.out.println("答案:" + AUC.countAUC(ids,-1));
	}
	
	
	
	public static double countAUC(LinkedList<Id> ids, int exceptEvidenceNum) {
		LinkedList<double[]> As = new LinkedList<double[]>();
		LinkedList<double[]> Bs = new LinkedList<double[]>();
		for (Id id : ids) {
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
		if (id.getLabel() == 1) {
			return true;
		} else if (id.getLabel() == 0) {
			return false;
		} else {
			throw new RuntimeException("嫌疑人(" + id.getId() + ")标签有误：" + id.getLabel());
		}
	}
}
