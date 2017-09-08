package version2.feature;

import util.CloneObject;
import version2.auc.MyDsCount;
import version2.dataSet.DataSet;
import version2.dataSet.DataSetParameter;
import version2.hereditary.Hereditary;
import version2.auc.DsCount;
import version2.hereditary.HereditaryParameter;
import version2.hereditary.ParentChrosChoose;
import version2.hereditary.RouletteParentChrosChoose;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class FeatureSelection {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DataSetParameter dataSetParameter=new DataSetParameter();
		dataSetParameter.setCoding("utf-8");
		dataSetParameter.setIdClo(0);
		dataSetParameter.setACol(2);
		dataSetParameter.setBCol(3);
		dataSetParameter.setEvidCol(1);
		dataSetParameter.setLabelCol(5);
		
		DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/trainAll.csv"),dataSetParameter);
		DsCount dsCount=new MyDsCount();
		HereditaryParameter hereditaryParameter=new HereditaryParameter();
		hereditaryParameter.setIterNum(500);
		hereditaryParameter.setSameNum(100);
		ParentChrosChoose parentChrosChoose=new RouletteParentChrosChoose();
		FeatureSeparation featureSeparation=new MedianFeatureSeparation();
		
		Map<Double, Integer> map=featureSelection(trainDataSet,dsCount,hereditaryParameter,parentChrosChoose,featureSeparation,0.01);
		int len=map.get(Double.MAX_VALUE);
		int i=0;
		for (Map.Entry<Double, Integer> entry : map.entrySet()) {
			System.out.println(entry.getValue()+" "+entry.getKey());
			i++;
			if (i==len) {
				System.out.println("---------------------------------");
			}
		}
	}
	
	public static Map<Double, Integer> featureSelection(DataSet dataSet, DsCount dsCount, HereditaryParameter hereditaryParameter,
	                                          ParentChrosChoose parentChrosChoose,FeatureSeparation featureSeparation,double stop) throws IOException, ClassNotFoundException {
		Hereditary hereditary=new Hereditary(hereditaryParameter,dataSet,dsCount);
		hereditary.evolution(parentChrosChoose);
		double aucFull = hereditary.getMaxAuc();
		
		TreeMap<Double, Integer> aucImprotences = new TreeMap<Double, Integer>();
		for (Integer integer : dataSet.getEvidenceNums()) {
			//这样减就负的越多越好，好的在前面
			hereditary.evolution(parentChrosChoose,integer);
			double auc = hereditary.getMaxAuc() - aucFull;
			aucImprotences.put(auc, integer);
		}
		
		LinkedList<Integer> imroEvid = new LinkedList<Integer>();
		LinkedList<Integer> unInproEvid = new LinkedList<Integer>();
		featureSeparation.separationFeature(aucImprotences, imroEvid, unInproEvid);
		
		DataSet dataSet1=CloneObject.clone(dataSet);
		dataSet1.removeNotEqual(imroEvid);
		Hereditary hereditary1=new Hereditary(hereditaryParameter,dataSet1,dsCount);
		hereditary1.evolution(parentChrosChoose);
		double auc = hereditary1.getMaxAuc();
		while (Math.abs(aucFull - auc) > stop) {
			double aucJ = -1;
			int evidenceNum = -1;
			for (Integer integer : unInproEvid) {
				LinkedList<Integer> newImroEvid = CloneObject.clone(imroEvid);
				newImroEvid.add(integer);
				dataSet1=CloneObject.clone(dataSet);
				dataSet1.removeNotEqual(imroEvid);
				hereditary1=new Hereditary(hereditaryParameter,dataSet1,dsCount);
				hereditary1.evolution(parentChrosChoose);
				if (hereditary.getMaxAuc() > aucJ) {
					aucJ = hereditary.getMaxAuc();
					evidenceNum = integer;
				}
			}
			
			imroEvid.add(evidenceNum);
			unInproEvid.remove(new Integer(evidenceNum));
			auc = aucJ;
		}
		
		aucImprotences.put(Double.MAX_VALUE,imroEvid.size());
		return aucImprotences;
	}
}
