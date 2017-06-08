package feature;

import auc.AUC;
import dataSet.DataSet;
import util.CloneObject;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cellargalaxy on 2017/5/13.
 * 特征选择算法
 */
public class FeatureSelection {
	//中位数
	public static final int MEDIAN_MODEL = 1;
	//平均数
	public static final int AVERAGE_MODEL = 2;
	//用户输入
	private static final int CUSTOMIZE_MODEL = 3;
	
	private double stop1;
	private double stop2;
	private int methodNum;
	private double customizeSeparation;
	
	public FeatureSelection(double customizeSeparation) {
		this.customizeSeparation = customizeSeparation;
		this.methodNum = CUSTOMIZE_MODEL;
		stop1 = 0.95;
		stop2 = 0.01;
	}
	
	public FeatureSelection(int methodNum) {
		this.methodNum = methodNum;
		stop1 = 0.95;
		stop2 = 0.01;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DataSet dataSet = new DataSet(new File("F:/xi/dachuang/特征选择 - 副本.csv"), ",", 0, 2, 3, 5, 6);
		FeatureSelection featureSelection = new FeatureSelection(MEDIAN_MODEL);
		double[][] ds = featureSelection.featureSelection(dataSet);
		System.out.println("答案：");
		for (double[] doubles : ds) {
			System.out.println(doubles[0] + ":" + doubles[1]);
		}
	}
	
	/**
	 * 返回的double[][] ds=new double[][2];
	 * 其中ds[i]={m,Imp}
	 *
	 * @param dataSet
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public double[][] featureSelection(DataSet dataSet) throws IOException, ClassNotFoundException {
		double aucFull = AUC.countAUC(dataSet, -1);
		TreeMap<Double, Integer> aucImprotences = new TreeMap<Double, Integer>();
		for (int m = 0; m < dataSet.getEvidenceCount(); m++) {
			//这样减就负的越多越好，好的在前面
			double auc = AUC.countAUC(dataSet, m) - aucFull;
			if (auc < 0) {
				aucImprotences.put(auc, m);
			}
		}

//		System.out.println("各个特征的重要程度：");
//		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
//			System.out.println(entry.getValue()+":"+entry.getKey());
//		}
//		System.out.println("----------------------------");
		
		LinkedList<Integer> imroEvid = new LinkedList<Integer>();
		LinkedList<Integer> unInproEvid = new LinkedList<Integer>();
		separation(aucImprotences, imroEvid, unInproEvid);
		
		double auc = AUC.countAUC(dataSet, unInproEvid);
		while (Math.abs(auc - aucFull) * stop1 > stop2) {
			Map<Double, Integer> aucJ = new TreeMap<Double, Integer>(new Comparator<Double>() {
				public int compare(Double a, Double b) {
					if (a > b) {
						return -1;
					} else if (a < b) {
						return 1;
					} else {
						return 0;
					}
				}
			});
			
			for (int i = 0; i < unInproEvid.size(); i++) {
				LinkedList<Integer> newUnInproEvid = CloneObject.clone(unInproEvid);
				Integer m = newUnInproEvid.remove(i);
				aucJ.put(AUC.countAUC(dataSet, newUnInproEvid), m);
			}
			
			for (Map.Entry<Double, Integer> entry : aucJ.entrySet()) {
				imroEvid.add(entry.getValue());
				unInproEvid.remove(entry.getValue());
				auc = entry.getKey();
				break;
			}
		}

//		System.out.println("保留的重要特征：");
//		System.out.println(imroEvid);
//		System.out.println("aucFull："+aucFull);
//		System.out.println("aucGood："+auc);
		
		double[][] ds = new double[imroEvid.size()][2];
		int i = 0;
		main:
		for (Integer m : imroEvid) {
			for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
				if (entry.getValue().equals(m)) {
					ds[i][0] = m;
					ds[i][1] = entry.getKey();
					i++;
					continue main;
				}
			}
			throw new RuntimeException("特征异常丢失");
		}
		return ds;
	}
	
	/**
	 * 分离重要特征与非重要特征
	 *
	 * @param aucImprotences
	 * @param imroEvid
	 * @param unInproEvid
	 */
	private void separation(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unInproEvid) {
		if (methodNum == MEDIAN_MODEL) {
			medianSeparation(aucImprotences, imroEvid, unInproEvid);
		} else if (methodNum == AVERAGE_MODEL) {
			averageSeparation(aucImprotences, imroEvid, unInproEvid);
		} else if (methodNum == CUSTOMIZE_MODEL) {
			customizeSeparation(aucImprotences, customizeSeparation, imroEvid, unInproEvid);
		} else {
			throw new RuntimeException("核特征选择方法代号有误");
		}
	}
	
	/**
	 * 用中位数分离重要特征与非重要特征
	 *
	 * @param aucImprotences
	 * @param imroEvid
	 * @param unInproEvid
	 */
	private void medianSeparation(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unInproEvid) {
		int point = aucImprotences.size() / 2;
		int i = 0;
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
			if (i <= point) {
				imroEvid.add(entry.getValue());
			} else {
				unInproEvid.add(entry.getValue());
			}
			i++;
		}
//		System.out.println("重要特征：");
//		System.out.println(imroEvid);
//		System.out.println("次要特征：");
//		System.out.println(unInproEvid);
	}
	
	/**
	 * 用平均数分离重要特征与非重要特征
	 *
	 * @param aucImprotences
	 * @param imroEvid
	 * @param unInproEvid
	 */
	private void averageSeparation(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unInproEvid) {
		double count = 0;
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
			count += entry.getKey();
		}
		double avg = count / aucImprotences.size();
		customizeSeparation(aucImprotences, avg, imroEvid, unInproEvid);
//		System.out.println("重要特征：");
//		System.out.println(imroEvid);
//		System.out.println("次要特征：");
//		System.out.println(unInproEvid);
	}
	
	/**
	 * 用用户输入值分离重要特征与非重要特征
	 *
	 * @param aucImprotences
	 * @param separation
	 * @param imroEvid
	 * @param unInproEvid
	 */
	private void customizeSeparation(TreeMap<Double, Integer> aucImprotences, double separation, LinkedList<Integer> imroEvid, LinkedList<Integer> unInproEvid) {
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
			if (entry.getKey() <= separation) {
				imroEvid.add(entry.getValue());
			} else {
				unInproEvid.add(entry.getValue());
			}
		}
//		System.out.println("重要特征：");
//		System.out.println(imroEvid);
//		System.out.println("次要特征：");
//		System.out.println(unInproEvid);
	}
}