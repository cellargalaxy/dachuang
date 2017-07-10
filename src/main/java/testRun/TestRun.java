package testRun;

import auc.AUC;
import dataSet.DataSet;
import dataSet.Id;
import feature.FeatureSelection;
import feature.SubSpace;
import hereditary.Hereditary;
import util.CloneObject;

import java.io.*;
import java.util.*;

import static feature.FeatureSelection.MEDIAN_MODEL;

/**
 * Created by cellargalaxy on 17-7-3.
 * 用于测试整个体系的静态类
 */
public class TestRun {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DataSet trainDataSet=new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/trainAll.csv"),
			",",0,2,3,1,5);

		System.out.println("训练集原始AUC:"+AUC.countAUCAll(trainDataSet));

//		double[][] featureSelections=FeatureSelection.featureSelection(trainDataSet,0.95,0.01,FeatureSelection.MEDIAN_MODEL,0,
//				Hereditary.USE_ORDER,5);
//		LinkedList<Integer> features=new LinkedList<>();
//		System.out.println("特征选择");
//		for (double[] featureSelection : featureSelections) {
//			System.out.println(Arrays.toString(featureSelection));
//			features.add((int)featureSelection[0]);
//		}
//		DataSet trainDataSet1=CloneObject.clone(trainDataSet);
//		trainDataSet1.allSaveEvidence(features);
//		double featureAUC=AUC.countAUCAll(trainDataSet1);
//		System.out.println("特征选择训练集AUC:"+featureAUC);

		double featureAUC=0.7070667495854063;
		double[][] featureSelections={
				{6,-0.04507048092868993},
				{5,-0.02431177446102828},
				{1,-0.00696517412935338},
				{7,-0.00509121061359874},
				{3,-0.001504975124378083}
		};

		int[] sn={1,2,3,4,5};
		LinkedList<LinkedList<Integer>> subSpaces=SubSpace.createSubSpaces(featureSelections,sn,20,SubSpace.POWER_ADJUST,0);
		System.out.println("子空间:");
		for (LinkedList<Integer> subSpace : subSpaces) {
			System.out.println(subSpace);
		}

		LinkedList<DataSet> trainDataSets=new LinkedList<>();
		LinkedList<double[]> chros=new LinkedList<>();
		int i=0;
		Iterator<LinkedList<Integer>> iterator=subSpaces.iterator();
		while (iterator.hasNext()) {
			LinkedList<Integer> subSpace=iterator.next();
			DataSet dataSet=CloneObject.clone(trainDataSet);
			dataSet.allSaveEvidence(subSpace);
			double[] chro=Hereditary.superEvolutionChro(dataSet,Hereditary.USE_ORDER,5);
			dataSet.mulChro(chro);
			double d=AUC.countAUCAll(dataSet);
			if (d>=featureAUC) {
				trainDataSets.add(dataSet);
				chros.add(chro);
				System.out.println("子空间训练集进化AUC:"+d);
			}else {
				iterator.remove();
			}
			i++;
		}

		Map<String,LinkedList<double[]>> trainDataSetMap=new HashMap<>();
		for (DataSet dataSet : trainDataSets) {
			for (Id id : dataSet.getIds()) {
				LinkedList<double[]> dss=trainDataSetMap.get(id.getId());
				if (dss==null) {
					dss=new LinkedList<>();
					trainDataSetMap.put(id.getId(),dss);
				}
				dss.add(id.countDSAll());
			}
		}
		for (Id id : trainDataSet.getIds()) {
			LinkedList<double[]> dss=trainDataSetMap.get(id.getId());
			if (dss==null) {
				double[] ds={0,0,0};
				id.setEvidenceDS(ds);
			}else {
				double a=0;
				double b=0;
				for (double[] doubles : dss) {
					a+=doubles[1];
					b+=doubles[2];
				}
				double[] ds={0,a/dss.size(),b/dss.size()};
				id.setEvidenceDS(ds);
			}
		}
		System.out.println("训练集TestAUC:"+AUC.countTestAUC(trainDataSet));

		////////////////////////////////////////////////////////////////////////////////////////////////

		DataSet testDataSet=new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testAll.csv"),
				",",0,2,3,1,5);

		DataSet[] testDataSets=new DataSet[subSpaces.size()];
		Iterator<LinkedList<Integer>> iteratorSubSpace=subSpaces.iterator();
		Iterator<double[]> iteratorChros=chros.iterator();
		for (int j = 0; j < testDataSets.length; j++) {
			testDataSets[j]=CloneObject.clone(testDataSet);
			LinkedList<Integer> subSpace=iteratorSubSpace.next();
			double[] chro=iteratorChros.next();
			testDataSets[j].allSaveEvidence(subSpace);
			testDataSets[j].mulChro(chro);
			System.out.println("测试集子空间进化AUC:"+AUC.countAUCAll(testDataSets[j]));
		}

		Map<String,LinkedList<double[]>> testDataSetMap=new HashMap<>();
		for (DataSet dataSet : testDataSets) {
			for (Id id : dataSet.getIds()) {
				LinkedList<double[]> dss=testDataSetMap.get(id.getId());
				if (dss==null) {
					dss=new LinkedList<>();
					testDataSetMap.put(id.getId(),dss);
				}
				dss.add(id.countDSAll());
			}
		}
		for (Id id : testDataSet.getIds()) {
			LinkedList<double[]> dss=testDataSetMap.get(id.getId());
			if (dss==null) {
				double[] ds={0,0,0};
				id.setEvidenceDS(ds);
			}else {
				double a=0;
				double b=0;
				for (double[] doubles : dss) {
					a+=doubles[1];
					b+=doubles[2];
				}
				double[] ds={0,a/dss.size(),b/dss.size()};
				id.setEvidenceDS(ds);
			}
		}
		System.out.println("测试集TestAUC:"+AUC.countTestAUC(testDataSet));
	}



//	public static void main(String[] args) throws IOException {
//		File trainSet=new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testSet.csv");
//		File trainLabel=new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testLabel.csv");
//		File trainAll=new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testAll.csv");
//
//		BufferedReader trainSetRead=new BufferedReader(new FileReader(trainSet));
//		BufferedReader trainLabelRead=new BufferedReader(new FileReader(trainLabel));
//		BufferedWriter trainAllWriter=new BufferedWriter(new FileWriter(trainAll));
//		String id=null;
//		String label=null;
//		String string;
//		while ((string = trainSetRead.readLine()) != null) {
//			String[] strings=string.split(",");
//			if (!strings[0].equals(id)) {
//				id=strings[0];
//				String s=trainLabelRead.readLine();
//				String[] ss=s.split(",");
//				label=ss[1];
//			}
//			trainAllWriter.write(string+","+label+"\r\n");
//			trainAllWriter.flush();
//		}
//		trainSetRead.close();
//		trainLabelRead.close();
//		trainAllWriter.close();
//	}

}
