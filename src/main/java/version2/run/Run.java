package version2.run;

import util.CloneObject;
import version2.dataSet.DataSet;
import version2.dataSet.DataSetParameter;
import version2.ds.*;
import version2.feature.FeatureSelection;
import version2.feature.FeatureSeparation;
import version2.feature.MedianFeatureSeparation;
import version2.hereditary.Hereditary;
import version2.hereditary.HereditaryParameter;
import version2.hereditary.ParentChrosChoose;
import version2.hereditary.RouletteParentChrosChoose;
import version2.subSpace.ImprotenceAdjust;
import version2.subSpace.PowerImprotenceAdjust;
import version2.subSpace.SubSpace;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class Run {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		run();
	}
	
	public static final void run() throws IOException, ClassNotFoundException {
		DataSetParameter dataSetParameter=new DataSetParameter("utf-8",0,5,1,2,3);
		DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/trainAll.csv"),dataSetParameter);
		
		DsCount dsCount=null;
		AucCount aucCount=null;
		double dsAuc=-1;
		DsCount[] dsCounts={new TeaDsCount()};
		Hereditary hereditary=new Hereditary(trainDataSet);
		HereditaryParameter hereditaryParameter=new HereditaryParameter();
		hereditaryParameter.setIterNum(500);
		hereditaryParameter.setSameNum(100);
		ParentChrosChoose parentChrosChoose=new RouletteParentChrosChoose();
		for (DsCount dsCount1 : dsCounts) {
			AucCount aucCount1=new Auc(dsCount1);
			hereditary.evolution(hereditaryParameter,parentChrosChoose,aucCount1);
			System.out.println("ds auc:"+hereditary.getMaxAuc());/////////////////////////////////////
			if (hereditary.getMaxAuc()>dsAuc) {
				dsAuc=hereditary.getMaxAuc();
				dsCount=dsCount1;
				aucCount=aucCount1;
			}
		}
		System.out.println("--------------------------------------------------------");
		System.out.println();
		
		FeatureSeparation featureSeparation=new MedianFeatureSeparation();
		Map<Double, Integer> map= FeatureSelection.featureSelection(aucCount,hereditaryParameter,parentChrosChoose,featureSeparation,0.01,hereditary);
		
		int len=map.get(Double.MAX_VALUE);
		List<Integer> features=new LinkedList<Integer>();
		double[] impros=new double[len];
		int i=0;
		System.out.println("重要特征:");
		for (Map.Entry<Double, Integer> entry : map.entrySet()) {
			features.add(entry.getValue());
			impros[i]=entry.getKey();
			
			System.out.println(entry.getValue()+" "+entry.getKey());
			i++;
			if (i>=len) {
				break;
			}
		}
		DataSet featureDataSet=hereditary.evolution(hereditaryParameter,parentChrosChoose,aucCount,features);
		double featureAuc=hereditary.getMaxAuc();
		double[] featureChro=hereditary.getMaxChro();
		System.out.println("特征选择 auc:"+featureAuc);
		System.out.println("--------------------------------------------------------");
		System.out.println();
		
		int[] sn = {1, 2, 3, 4};
		ImprotenceAdjust improtenceAdjust=new PowerImprotenceAdjust();
		List<List<Integer>> subSpaces = SubSpace.createSubSpaces(features,impros,sn,25,improtenceAdjust);
		System.out.println("子空间:");
		for (List<Integer> subSpace : subSpaces) {
			System.out.println(subSpace);
		}
		System.out.println("--------------------------------------------------------");
		System.out.println();
		
		Map<DataSet,double[]> subSpaceMap=new HashMap<DataSet, double[]>();
		Iterator<List<Integer>> iterator = subSpaces.iterator();
		while (iterator.hasNext()) {
			List<Integer> subSpace = iterator.next();
			DataSet dataSet=hereditary.evolution(hereditaryParameter,parentChrosChoose,aucCount,subSpace);
			if (hereditary.getMaxAuc() >= featureAuc) {
				subSpaceMap.put(dataSet,hereditary.getMaxChro());
				System.out.println("子空间训练集进化AUC:" + hereditary.getMaxAuc());
			}
			
		}
		if (subSpaceMap.size() == 0) {
			subSpaceMap.put(featureDataSet,featureChro);
			System.out.println("全子空间训练集进化AUC:" + featureAuc);
		}
		
		DataSet subSpaceDataSet=SubSpace2DataSet.subSpace2DataSet(subSpaceMap,dsCount);
		System.out.println("subSpaceDataSet auc:"+new Auc(new AverageDsCount()).countAuc(subSpaceDataSet));
	}
	
}
