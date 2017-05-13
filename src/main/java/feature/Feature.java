package feature;

import auc.AUC;
import auc.Id;
import dataSet.DataSet;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cellargalaxy on 2017/5/13.
 */
public class Feature {
	public static final int medianModel=1;
	public static final int averageModel=2;
	
	private double stop1;
	private double stop2;
//	private double importance;
	private int model;
	
	
	public void test(DataSet dataSet){
		double aucFull= AUC.countAUC(dataSet,-1);
		TreeMap<Double,Integer> aucImprotences=new TreeMap<Double,Integer>();
		for (int m = 0; m < dataSet.getEvidenceCount(); m++) {
			//这样减就负的越多越好
			aucImprotences.put(aucFull-AUC.countAUC(dataSet,m),m);
		}
		
	}
	private void separation(TreeMap<Double,Integer> aucImprotences,LinkedList<Integer> imroEvid,LinkedList<Integer> unInproEvid,int methodNum){
		if (methodNum==medianModel) {
			medianSeparation(aucImprotences,imroEvid,unInproEvid);
		}else if (methodNum==averageModel){
			averageSeparation(aucImprotences,imroEvid,unInproEvid);
		}else {
			throw new RuntimeException("核特征选择方法代号有误");
		}
	}
	private void medianSeparation(TreeMap<Double,Integer> aucImprotences,LinkedList<Integer> imroEvid,LinkedList<Integer> unInproEvid){
		int point=aucImprotences.size()/2;
		int i=0;
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
			if (i<=point){
				imroEvid.add(entry.getValue());
			}else {
				unInproEvid.add(entry.getValue());
			}
		}
	}
	private void averageSeparation(TreeMap<Double,Integer> aucImprotences,LinkedList<Integer> imroEvid,LinkedList<Integer> unInproEvid){
		double count=0;
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
			count+=entry.getKey();
		}
		double avg=count/aucImprotences.size();
		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
		
		}
	}
}
