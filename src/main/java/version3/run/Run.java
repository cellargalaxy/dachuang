//package version3.run;
//
//import util.CloneObject;
//import version3.Win;
//import version3.dataSet.DataSet;
//import version3.dataSet.DataSetParameter;
//import version3.dataSet.DataSetSeparation;
//import version3.dataSet.DataSetSeparationImpl;
//import version3.evaluation.Auc;
//import version3.evaluation.Evaluation;
//import version3.evaluation.EvaluationThreadPoolExecutor;
//import version3.evidenceSynthesis.*;
//import version3.feature.FeatureSelection;
//import version3.feature.FeatureSeparation;
//import version3.feature.MedianFeatureSeparation;
//import version3.hereditary.*;
//import version3.subSpace.*;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//
///**
// * Created by cellargalaxy on 17-9-9.
// */
//public class Run {
//
//
//	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		//全局参数
//		RunParameter runParameter = new TestRunParameter();
//		File dataSetFile = new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/subspacedata.csv");
//		DataSetParameter dataSetParameter = new DataSetParameter("utf-8", 0, 5, 4, 1, 2);
//		EvidenceSynthesis[] evidenceSyntheses = {new TeaEvidenceSynthesis(), new MyEvidenceSynthesis(), new My2EvidenceSynthesis()};
//		File trainOutputFile = new File("/home/cellargalaxy/trainSubSpaceDataSet.csv");
//		File testOutputFile = new File("/home/cellargalaxy/testSubSpaceDataSet.csv");
//		//遗传算法
//		HereditaryParameter hereditaryParameter = new HereditaryParameter();
//		hereditaryParameter.setIterNum(500);
//		hereditaryParameter.setSameNum(100);
//		ParentChrosChoose parentChrosChoose = new OrderParentChrosChoose();
//		//特征选择
//		FeatureSeparation featureSeparation = new MedianFeatureSeparation();
//		double stop = 0.01;
//		//子空间
//		ImprotenceAdjust improtenceAdjust = new PowerImprotenceAdjust();
//		SubSpaceCreate subSpaceCreate = new RandomSubSpaceCreate(runParameter);
//		//子空间合成
//		EvidenceSynthesis subSpaceEvidenceSynthesis = new AverageEvidenceSynthesis();
//
//		run(runParameter, dataSetFile, dataSetParameter, evidenceSyntheses, trainOutputFile, testOutputFile,
//				hereditaryParameter, parentChrosChoose,
//				featureSeparation, stop,
//				subSpaceCreate,
//				subSpaceEvidenceSynthesis);
//
//		EvaluationThreadPoolExecutor.shutdown();
//	}
//
//	public static final void run(RunParameter runParameter, File dataSetFile, DataSetParameter dataSetParameter, EvidenceSynthesis[] evidenceSyntheses, File trainOutputFile, File testOutputFile,//全局参数
//	                             HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose,//遗传算法
//	                             FeatureSeparation featureSeparation, double stop,//特征选择
//	                             SubSpaceCreate subSpaceCreate,//子空间
//	                             EvidenceSynthesis subSpaceEvidenceSynthesis//子空间合成
//	) throws IOException, ClassNotFoundException {
//		DataSetSeparation dataSetSeparation = new DataSetSeparationImpl(dataSetFile, dataSetParameter);
//		runParameter.receiveCreateSubDataSet(dataSetSeparation.getCom0Count(), dataSetSeparation.getCom1Count(), dataSetSeparation.getMiss0Count(), dataSetSeparation.getMiss1Count());
//		DataSet[] dataSets = dataSetSeparation.separationDataSet(runParameter.getTest(), runParameter.getMiss(), runParameter.getLabel1());
//		DataSet trainDataSet = dataSets[0];
//		DataSet testDataSet = dataSets[1];
//
//		run(runParameter, trainDataSet, testDataSet, evidenceSyntheses, trainOutputFile, testOutputFile,
//				hereditaryParameter, parentChrosChoose,
//				featureSeparation, stop,
//				subSpaceCreate,
//				subSpaceEvidenceSynthesis);
//	}
//
//	public static final void run(RunParameter runParameter, DataSet trainDataSet, DataSet testDataSet, EvidenceSynthesis[] evidenceSyntheses, File trainOutputFile, File testOutputFile,//全局参数
//	                             HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose,//遗传算法
//	                             FeatureSeparation featureSeparation, double stop,//特征选择
//	                             SubSpaceCreate subSpaceCreate,//子空间
//	                             EvidenceSynthesis subSpaceEvidenceSynthesis//子空间合成
//	) throws IOException, ClassNotFoundException {
//		EvidenceSynthesis evidenceSynthesis = null;
//		Evaluation evaluation = null;
//		double dsAuc = -1;
//		Hereditary hereditary = new Hereditary(trainDataSet);
//		for (EvidenceSynthesis evidenceSynthesis1 : evidenceSyntheses) {
//			Evaluation evaluation1 = new Auc(evidenceSynthesis1);
//			hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation1);
//
//			System.out.println(evidenceSynthesis1.getName() + "\tds auc:" + hereditary.getMaxAuc());
//
//			if (hereditary.getMaxAuc() > dsAuc) {
//				dsAuc = hereditary.getMaxAuc();
//				evidenceSynthesis = evidenceSynthesis1;
//				evaluation = evaluation1;
//			}
//		}
//		System.out.println("--------------------------------------------------------");
//		System.out.println();
//
//		Map<Double, Integer> featureMap = FeatureSelection.featureSelection(evaluation, hereditaryParameter, parentChrosChoose, featureSeparation, stop, hereditary);
//
//		int len = featureMap.get(Double.MAX_VALUE);
//		List<Integer> features = new LinkedList<Integer>();
//		double[] impros = new double[len];
//		int i = 0;
//		System.out.println("重要特征:");
//		for (Map.Entry<Double, Integer> entry : featureMap.entrySet()) {
//			features.add(entry.getValue());
//			impros[i] = entry.getKey();
//
//			System.out.println(entry.getValue() + " " + entry.getKey());
//			i++;
//			if (i >= len) {
//				break;
//			}
//		}
//		double featureAuc = evaluation.countEvaluationWithEvidNums(trainDataSet, features);
//		System.out.println("训练集特征选择 auc:" + featureAuc);
//		System.out.println("--------------------------------------------------------");
//		System.out.println();
//
//		List<List<Integer>> subSpaces = subSpaceCreate.createSubSpaces(features, impros);
//		System.out.println("子空间:");
//		for (List<Integer> subSpace : subSpaces) {
//			System.out.println(subSpace);
//		}
//		System.out.println("--------------------------------------------------------");
//		System.out.println();
//
//		Map<DataSet, double[]> trainSubSpaceMap = new HashMap<DataSet, double[]>();
//		Iterator<List<Integer>> iterator = subSpaces.iterator();
//		while (iterator.hasNext()) {
//			List<Integer> subSpace = iterator.next();
//			DataSet dataSet = hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation, subSpace);
//			if (hereditary.getMaxAuc() >= featureAuc) {
//				trainSubSpaceMap.put(dataSet, hereditary.getMaxChro());
//				System.out.println("训练子空间训练集进化AUC:" + hereditary.getMaxAuc());
//			}
//		}
//		if (trainSubSpaceMap.size() == 0) {
//			trainSubSpaceMap.put(hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation, features), hereditary.getMaxChro());
//			System.out.println("全子空间训练集进化AUC:" + featureAuc);
//		}
//
//		DataSet trainSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(trainSubSpaceMap, evidenceSynthesis);
//		Auc subSpaceAucCount = new Auc(subSpaceEvidenceSynthesis);
//		double trainSubSpaceAuc = subSpaceAucCount.countEvaluation(trainSubSpaceDataSet);
//		System.out.println("trainSubSpaceDataSet auc:" + trainSubSpaceAuc);
//
//		DataSet.outputDataSet(trainDataSet, trainSubSpaceDataSet, trainOutputFile, runParameter,
//				trainSubSpaceAuc, evidenceSynthesis, evaluation,
//				trainDataSet.getEvidNameToId(),
//				parentChrosChoose,
//				featureSeparation, stop, featureMap,
//				subSpaceCreate, trainSubSpaceMap);
//
//		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Map<DataSet, double[]> testSubSpaceMap = new HashMap<DataSet, double[]>();
//		for (Map.Entry<DataSet, double[]> entry : trainSubSpaceMap.entrySet()) {
//			List<Integer> subSpace = entry.getKey().getEvidenceNums();
//			DataSet dataSet = CloneObject.clone(testDataSet);
//			dataSet.removeNotEqual(subSpace);
//			testSubSpaceMap.put(dataSet, entry.getValue());
//			System.out.println("测试子空间训练集进化AUC:" + evaluation.countEvaluation(dataSet));
//		}
//		DataSet testSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(testSubSpaceMap, evidenceSynthesis);
//		double testSubSpaceAuc = subSpaceAucCount.countEvaluation(testSubSpaceDataSet);
//		System.out.println("testSubSpaceDataSet auc:" + testSubSpaceAuc);
//		DataSet.outputDataSet(testDataSet, testSubSpaceDataSet, testOutputFile, runParameter,
//				testSubSpaceAuc, evidenceSynthesis, evaluation,
//				testDataSet.getEvidNameToId(),
//				parentChrosChoose,
//				featureSeparation, stop, featureMap,
//				subSpaceCreate, testSubSpaceMap);
//	}
//
//
//}
