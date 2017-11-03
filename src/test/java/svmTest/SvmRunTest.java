//package svmTest;
//
//import version5.dataSet.DataSet;
//import version5.dataSet.DataSetParameter;
//import version5.dataSet.DataSetSeparation;
//import version5.dataSet.DataSetSeparationImpl;
//import version5.evaluation.Evaluation;
//import version5.evaluation.Svm;
//import version5.evidenceSynthesis.EvidenceSynthesis;
//import version5.evidenceSynthesis.TeaEvidenceSynthesis;
//import version5.feature.AucImprotence;
//import version5.feature.FeatureSelection;
//import version5.feature.FeatureSeparation;
//import version5.feature.MedianFeatureSeparation;
//import version5.hereditary.Hereditary;
//import version5.hereditary.HereditaryParameter;
//import version5.hereditary.OrderParentChrosChoose;
//import version5.hereditary.ParentChrosChoose;
//import version5.run.RunParameter;
//import version5.run.TestRunParameter;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * Created by cellargalaxy on 17-11-1.
// */
//public class SvmRunTest {
//	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		//全局参数
//		RunParameter runParameter = new TestRunParameter();
//		File dataSetFile = new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/subspacedata.csv");
//		DataSetParameter dataSetParameter = new DataSetParameter("utf-8", 0, 5, 4, 1, 2);
//
//		DataSetSeparation dataSetSeparation = new DataSetSeparationImpl(dataSetFile, dataSetParameter);
//		runParameter.receiveCreateSubDataSet(dataSetSeparation.getCom0Count(), dataSetSeparation.getCom1Count(), dataSetSeparation.getMiss0Count(), dataSetSeparation.getMiss1Count());
//		DataSet[] dataSets = dataSetSeparation.separationDataSet(runParameter.getTest(), runParameter.getMiss(), runParameter.getLabel1());
//		DataSet trainDataSet = dataSets[0];
//		DataSet testDataSet = dataSets[1];
//
//		EvidenceSynthesis evidenceSynthesis = new TeaEvidenceSynthesis();
//		Evaluation evaluation = new Svm();
//		Hereditary hereditary = new Hereditary(trainDataSet);
//		System.out.println("--------------------------------------------------------");
//		System.out.println();
//
//		HereditaryParameter hereditaryParameter = new HereditaryParameter();
//		hereditaryParameter.setIterNum(500);
//		hereditaryParameter.setSameNum(100);
//		ParentChrosChoose parentChrosChoose = new OrderParentChrosChoose();
//		FeatureSeparation featureSeparation = new MedianFeatureSeparation();
//		double stop = 0.01;
//		LinkedList<AucImprotence> aucImprotences = FeatureSelection.featureSelection(evaluation, hereditaryParameter, parentChrosChoose, featureSeparation, stop, hereditary);
//
//		int len = aucImprotences.getLast().getEvidenceNum();
//		List<Integer> features = new LinkedList<Integer>();
//		double[] impros = new double[len];
//		int i = 0;
//		System.out.println("重要特征:");
//		for (AucImprotence aucImprotence : aucImprotences) {
//			features.add(aucImprotence.getEvidenceNum());
//			impros[i]=aucImprotence.getAucD();
//
//			System.out.println(aucImprotence.getEvidenceNum() + " " + aucImprotence.getAucD());
//			i++;
//			if (i >= len) {
//				break;
//			}
//		}
//		double featureAuc = evaluation.countEvaluationWithEvidNums(trainDataSet, features);
//		System.out.println("训练集特征选择 auc:" + featureAuc);
//		System.out.println("--------------------------------------------------------");
//		System.out.println();
//	}
//}
