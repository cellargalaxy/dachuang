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

		System.out.println("训练集原始AUC:"+AUC.countAUCWithout(trainDataSet,-1));

		double[][] featureSelections=FeatureSelection.featureSelection(trainDataSet,0.95,0.01,FeatureSelection.MEDIAN_MODEL,0);
		LinkedList<Integer> features=new LinkedList<>();
		System.out.println("特征选择");
		for (double[] featureSelection : featureSelections) {
			System.out.println(Arrays.toString(featureSelection));
			features.add((int)featureSelection[0]);
		}
		DataSet trainDataSet1=CloneObject.clone(trainDataSet);
		trainDataSet1.allSaveEvidence(features);
		System.out.println("特征选择训练集AUC:"+AUC.countAUCWithout(trainDataSet1,-1));

		int[] sn={1,2,3,4,5};
		LinkedList<LinkedList<Integer>> subSpaces=SubSpace.createSubSpaces(featureSelections,sn,15,SubSpace.POWER_ADJUST,0);
		System.out.println("子空间:");
		for (LinkedList<Integer> subSpace : subSpaces) {
			System.out.println(subSpace);
		}

		DataSet[] trainDataSets=new DataSet[subSpaces.size()];
		double[][] chros=new double[subSpaces.size()][];
		int i=0;
		for (LinkedList<Integer> subSpace : subSpaces) {
			trainDataSets[i]=CloneObject.clone(trainDataSet);
			trainDataSets[i].allSaveEvidence(subSpace);
			double[] chro=Hereditary.superEvolution(trainDataSets[i],-1,Hereditary.USE_ORDER,1);
			chros[i]=chro;
			trainDataSets[i].mulChro(chro);
			System.out.println("子空间训练集进化AUC:"+AUC.countAUCWithout(trainDataSets[i],-1));
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
				dss.add(id.countDSWithout(-1));
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
		i=0;
		for (LinkedList<Integer> subSpace : subSpaces) {
			testDataSets[i]=CloneObject.clone(testDataSet);
			testDataSets[i].allSaveEvidence(subSpace);
			testDataSets[i].mulChro(chros[i]);
			System.out.println("测试集子空间进化AUC:"+AUC.countAUCWithout(testDataSets[i],-1));
			i++;
		}

		Map<String,LinkedList<double[]>> testDataSetMap=new HashMap<>();
		for (DataSet dataSet : testDataSets) {
			for (Id id : dataSet.getIds()) {
				LinkedList<double[]> dss=testDataSetMap.get(id.getId());
				if (dss==null) {
					dss=new LinkedList<>();
					testDataSetMap.put(id.getId(),dss);
				}
				dss.add(id.countDSWithout(-1));
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
