package version2.run;

import util.CloneObject;
import version2.dataSet.DataSet;
import version2.dataSet.DataSetParameter;
import version2.dataSet.SubDataSet;
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
		//全局参数
		RunParameter runParameter=new TestRunParameter();
		File dataSetFile=new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/subspacedata.csv");
		DataSetParameter dataSetParameter=new DataSetParameter("utf-8",0,5,4,1,2);
		DsCount[] dsCounts={new TeaDsCount(),new MyDsCount(),new My2DsCount()};
		File trainOutputFile=new File("/home/cellargalaxy/trainSubSpaceDataSet.csv");
		File testOutputFile=new File("/home/cellargalaxy/testSubSpaceDataSet.csv");
		//遗传算法
		HereditaryParameter hereditaryParameter=new HereditaryParameter();
		hereditaryParameter.setIterNum(500);
		hereditaryParameter.setSameNum(100);
		ParentChrosChoose parentChrosChoose=new RouletteParentChrosChoose();
		//特征选择
		FeatureSeparation featureSeparation=new MedianFeatureSeparation();
		double stop=0.01;
		//子空间
		ImprotenceAdjust improtenceAdjust=new PowerImprotenceAdjust();
		//子空间合成
		DsCount subSpaceDsCount=new AverageDsCount();

		run(runParameter,dataSetFile,dataSetParameter,dsCounts,trainOutputFile,testOutputFile,
				hereditaryParameter,parentChrosChoose,
				featureSeparation,stop,
				improtenceAdjust,
				subSpaceDsCount);
	}
	
	public static final void run(RunParameter runParameter,File dataSetFile,DataSetParameter dataSetParameter,DsCount[] dsCounts,File trainOutputFile,File testOutputFile,//全局参数
	                             HereditaryParameter hereditaryParameter,ParentChrosChoose parentChrosChoose,//遗传算法
	                             FeatureSeparation featureSeparation,double stop,//特征选择
	                             ImprotenceAdjust improtenceAdjust,//子空间
	                             DsCount subSpaceDsCount//子空间合成
	) throws IOException, ClassNotFoundException {
		SubDataSet subDataSet=new SubDataSet(dataSetFile,dataSetParameter);
		runParameter.receiveCreateSubDataSet(subDataSet.getCom0Count(),subDataSet.getCom1Count(),subDataSet.getMiss0Count(),subDataSet.getMiss1Count());
		DataSet[] dataSets=subDataSet.createSubDataSet(runParameter.getTest(),runParameter.getMiss(),runParameter.getLabel1());
		DataSet trainDataSet = dataSets[0];
		DataSet testDataSet = dataSets[1];
		
		run(runParameter,trainDataSet,testDataSet,dsCounts,trainOutputFile,testOutputFile,
				hereditaryParameter,parentChrosChoose,
				featureSeparation,stop,
				improtenceAdjust,
				subSpaceDsCount);
	}
	
	public static final void run(RunParameter runParameter,DataSet trainDataSet,DataSet testDataSet,DsCount[] dsCounts,File trainOutputFile,File testOutputFile,//全局参数
	                             HereditaryParameter hereditaryParameter,ParentChrosChoose parentChrosChoose,//遗传算法
	                             FeatureSeparation featureSeparation,double stop,//特征选择
	                             ImprotenceAdjust improtenceAdjust,//子空间
	                             DsCount subSpaceDsCount//子空间合成
	) throws IOException, ClassNotFoundException {
		DsCount dsCount=null;
		AucCount aucCount=null;
		double dsAuc=-1;
		Hereditary hereditary=new Hereditary(trainDataSet);
		for (DsCount dsCount1 : dsCounts) {
			AucCount aucCount1=new Auc(dsCount1);
			hereditary.evolution(hereditaryParameter,parentChrosChoose,aucCount1);
			
			System.out.println(dsCount1.getName()+" ds auc:"+hereditary.getMaxAuc());/////////////////////////////////////
			
			if (hereditary.getMaxAuc()>dsAuc) {
				dsAuc=hereditary.getMaxAuc();
				dsCount=dsCount1;
				aucCount=aucCount1;
			}
		}
		System.out.println("--------------------------------------------------------");
		System.out.println();
		
		Map<Double, Integer> featureMap= FeatureSelection.featureSelection(aucCount,hereditaryParameter,parentChrosChoose,featureSeparation,stop,hereditary);
		
		int len=featureMap.get(Double.MAX_VALUE);
		List<Integer> features=new LinkedList<Integer>();
		double[] impros=new double[len];
		int i=0;
		System.out.println("重要特征:");
		for (Map.Entry<Double, Integer> entry : featureMap.entrySet()) {
			features.add(entry.getValue());
			impros[i]=entry.getKey();
			
			System.out.println(entry.getValue()+" "+entry.getKey());
			i++;
			if (i>=len) {
				break;
			}
		}
		double featureAuc=aucCount.countAuc(trainDataSet,features);
		System.out.println("训练集特征选择 auc:"+featureAuc);
		System.out.println("--------------------------------------------------------");
		System.out.println();
		
		runParameter.receiveCreateSubSpaces(impros);
		int[] sn = runParameter.getSn();
		int fnMin=runParameter.getFnMin();
		List<List<Integer>> subSpaces = SubSpace.createSubSpaces(features,impros,sn,fnMin,improtenceAdjust);
		System.out.println("子空间:");
		for (List<Integer> subSpace : subSpaces) {
			System.out.println(subSpace);
		}
		System.out.println("--------------------------------------------------------");
		System.out.println();
		
		Map<DataSet,double[]> trainSubSpaceMap=new HashMap<DataSet, double[]>();
		Iterator<List<Integer>> iterator = subSpaces.iterator();
		while (iterator.hasNext()) {
			List<Integer> subSpace = iterator.next();
			DataSet dataSet=hereditary.evolution(hereditaryParameter,parentChrosChoose,aucCount,subSpace);
			if (hereditary.getMaxAuc() >= featureAuc) {
				trainSubSpaceMap.put(dataSet,hereditary.getMaxChro());
				System.out.println("训练子空间训练集进化AUC:" + hereditary.getMaxAuc());
			}
		}
		if (trainSubSpaceMap.size() == 0) {
			trainSubSpaceMap.put(hereditary.evolution(hereditaryParameter,parentChrosChoose,aucCount,features),hereditary.getMaxChro());
			System.out.println("全子空间训练集进化AUC:" + featureAuc);
		}
		
		DataSet trainSubSpaceDataSet=SubSpace2DataSet.subSpace2DataSet(trainSubSpaceMap,dsCount);
		Auc subSpaceAucCount=new Auc(subSpaceDsCount);
		double trainSubSpaceAuc=subSpaceAucCount.countAuc(trainSubSpaceDataSet);
		System.out.println("trainSubSpaceDataSet auc:"+trainSubSpaceAuc);
		
		DataSet.outputDataSet(trainSubSpaceDataSet,trainOutputFile,trainSubSpaceAuc,dsCount,trainDataSet.getEvidNameToId(),featureMap,sn,trainSubSpaceMap);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		Map<DataSet,double[]> testSubSpaceMap=new HashMap<DataSet, double[]>();
		for (Map.Entry<DataSet, double[]> entry : trainSubSpaceMap.entrySet()) {
			List<Integer> subSpace=entry.getKey().getEvidenceNums();
			DataSet dataSet=CloneObject.clone(testDataSet);
			dataSet.removeNotEqual(subSpace);
			testSubSpaceMap.put(dataSet,entry.getValue());
			System.out.println("测试子空间训练集进化AUC:" + aucCount.countAuc(dataSet));
		}
		DataSet testSubSpaceDataSet=SubSpace2DataSet.subSpace2DataSet(testSubSpaceMap,dsCount);
		double testSubSpaceAuc=subSpaceAucCount.countAuc(testSubSpaceDataSet);
		System.out.println("testSubSpaceDataSet auc:"+testSubSpaceAuc);
		DataSet.outputDataSet(testSubSpaceDataSet,testOutputFile,testSubSpaceAuc,dsCount,testDataSet.getEvidNameToId(),featureMap,sn,testSubSpaceMap);
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
		Map<Double, Integer> featureMap= FeatureSelection.featureSelection(aucCount,hereditaryParameter,parentChrosChoose,featureSeparation,0.01,hereditary);
		
		int len=featureMap.get(Double.MAX_VALUE);
		List<Integer> features=new LinkedList<Integer>();
		double[] impros=new double[len];
		int i=0;
		System.out.println("重要特征:");
		for (Map.Entry<Double, Integer> entry : featureMap.entrySet()) {
			features.add(entry.getValue());
			impros[i]=entry.getKey();
			
			System.out.println(entry.getValue()+" "+entry.getKey());
			i++;
			if (i>=len) {
				break;
			}
		}
		double trainFeatureAuc=aucCount.countAuc(trainDataSet,features);
		System.out.println("训练集特征选择 auc:"+trainFeatureAuc);
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
		
		Map<DataSet,double[]> trainSubSpaceMap=new HashMap<DataSet, double[]>();
		Iterator<List<Integer>> iterator = subSpaces.iterator();
		while (iterator.hasNext()) {
			List<Integer> subSpace = iterator.next();
			DataSet dataSet=hereditary.evolution(hereditaryParameter,parentChrosChoose,aucCount,subSpace);
			if (hereditary.getMaxAuc() >= trainFeatureAuc) {
				trainSubSpaceMap.put(dataSet,hereditary.getMaxChro());
				System.out.println("训练子空间训练集进化AUC:" + hereditary.getMaxAuc());
			}
		}
		if (trainSubSpaceMap.size() == 0) {
			trainSubSpaceMap.put(hereditary.evolution(hereditaryParameter,parentChrosChoose,aucCount,features),hereditary.getMaxChro());
			System.out.println("全子空间训练集进化AUC:" + trainFeatureAuc);
		}
		
		DataSet trainSubSpaceDataSet=SubSpace2DataSet.subSpace2DataSet(trainSubSpaceMap,dsCount);
		Auc subSpaceAucCount=new Auc(new AverageDsCount());
		double trainSubSpaceAuc=subSpaceAucCount.countAuc(trainSubSpaceDataSet);
		System.out.println("trainSubSpaceDataSet auc:"+trainSubSpaceAuc);
		
		DataSet.outputDataSet(trainSubSpaceDataSet,new File("/home/cellargalaxy/trainSubSpaceDataSet.csv"),trainSubSpaceAuc,dsCount,trainDataSet.getEvidNameToId(),featureMap,sn,trainSubSpaceMap);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		DataSet testDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/testAll.csv"),dataSetParameter);
		Map<DataSet,double[]> testSubSpaceMap=new HashMap<DataSet, double[]>();
		for (Map.Entry<DataSet, double[]> entry : trainSubSpaceMap.entrySet()) {
			List<Integer> subSpace=entry.getKey().getEvidenceNums();
			DataSet dataSet=CloneObject.clone(testDataSet);
			dataSet.removeNotEqual(subSpace);
			testSubSpaceMap.put(dataSet,entry.getValue());
			System.out.println("测试子空间训练集进化AUC:" + aucCount.countAuc(dataSet));
		}
		DataSet testSubSpaceDataSet=SubSpace2DataSet.subSpace2DataSet(testSubSpaceMap,dsCount);
		double testSubSpaceAuc=subSpaceAucCount.countAuc(testSubSpaceDataSet);
		System.out.println("testSubSpaceDataSet auc:"+testSubSpaceAuc);
		DataSet.outputDataSet(testSubSpaceDataSet,new File("/home/cellargalaxy/testSubSpaceDataSet.csv"),testSubSpaceAuc,dsCount,testDataSet.getEvidNameToId(),featureMap,sn,testSubSpaceMap);
	}
	
}