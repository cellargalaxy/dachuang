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
	
	public static void main(String[] args) throws IOException {
		DataSet trainDataSet=new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/特征选择.csv"),
				",",0,2,3,5,6);
//		LinkedList<Id> ids=trainDataSet.getIds();
//		for (Id id : ids) {
//			System.out.println(id.getId()+":"+Arrays.toString(id.countDSWithout(-1))+" , "+id.getLabel());
//		}
		System.out.println("AUC:"+AUC.countAUCWithout(trainDataSet,-1));
	}

	public static double countAUCTestSet(DataSet dataSet){
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = id.getSubSpaceDS();
			if (ds==null) {
				ds=id.countDSWithout(-1);
			}
			if (is1(id)) ds1s.add(ds);
			else ds0s.add(ds);
		}
//		System.out.println("????????/");
//		for (double[] ds1 : ds0s) {
//			if (ds1[1]>1) {
//				System.out.println("??????????"+Arrays.toString(ds1));
//			}
//		}
		return countAUC(ds1s,ds0s);
	}

	public static double countAUCWith(DataSet dataSet, LinkedList<Integer> evidenceNums){
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = id.countDSWiths(evidenceNums);
			if (is1(id)) ds1s.add(ds);
			else ds0s.add(ds);
		}
		return countAUC(ds1s,ds0s);
	}

	public static double countAUCWithouts(DataSet dataSet, LinkedList<Integer> exceptEvidenceNums) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = id.countDSWithouts(exceptEvidenceNums);
			if (is1(id)) ds1s.add(ds);
			else ds0s.add(ds);
		}
		return countAUC(ds1s,ds0s);
	}
	
	public static double countAUCWithout(DataSet dataSet, int exceptEvidenceNum) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = id.countDSWithout(exceptEvidenceNum);
			if (is1(id)) ds1s.add(ds);
			else ds0s.add(ds);
		}
		return countAUC(ds1s,ds0s);
	}

	private static double countAUC(LinkedList<double[]> ds1s,LinkedList<double[]> ds0s){
//		for (double[] ds1 : ds1s) {
//			if (ds1[1]>1) {
//				System.out.println("!!!!!!!"+Arrays.toString(ds1));
//			}
//		}
		double auc = 0;
		boolean b=true;
		for (double[] ds1 : ds1s) {
			for (double[] ds0 : ds0s) {
//				if (b) {
//					System.out.println(ds1[1]+","+ds0[1]+"\t\t"+(ds1[1]>ds0[1]));
//					b=false;
//				}
				if (ds1[1]>ds0[1]) {
					auc++;
				}else if (ds1[1]==ds0[1]){
					auc=auc+0.5;
				}
			}
		}
//		System.out.println();
		return auc/ds1s.size()/ds0s.size();
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
