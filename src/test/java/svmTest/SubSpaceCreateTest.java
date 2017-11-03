package svmTest;

import version5.dataSet.DataSet;
import version5.dataSet.DataSetParameter;
import version5.evaluation.Auc;
import version5.evaluation.Evaluation;
import version5.evaluation.EvaluationThreadPoolExecutor;
import version5.evidenceSynthesis.TeaEvidenceSynthesis;
import version5.feature.FeatureSeparation;
import version5.feature.MedianFeatureSeparation;
import version5.hereditary.Hereditary;
import version5.hereditary.HereditaryParameter;
import version5.hereditary.OrderParentChrosChoose;
import version5.hereditary.ParentChrosChoose;
import version5.subSpace.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by cellargalaxy on 17-11-2.
 */
public class SubSpaceCreateTest {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		File dataSetFile = new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/subspacedata.csv");
//		DataSetParameter dataSetParameter = new DataSetParameter("utf-8", 0, 5, 4, 1, 2);
//		DataSetSeparation dataSetSeparation=new DataSetSeparationImpl(dataSetFile,dataSetParameter);
//
//		System.out.println(dataSetSeparation);
//		DataSet[] dataSets = dataSetSeparation.separationDataSet(0.5,0.2,0.5,0.1);
//		DataSet trainDataSet = dataSets[0];
//		DataSet testDataSet = dataSets[1];
		
		DataSetParameter dataSetParameter = new DataSetParameter("utf-8", 0, 5, 1, 2, 3);
		DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/trainAll.csv"), dataSetParameter);
		
		int[][] sns={{1},{1,2},{1,2,3},{1,2,3,4}};
		int[] fnMins={1,3,6,13};
		HereditaryParameter hereditaryParameter = new HereditaryParameter();
		hereditaryParameter.setIterNum(500);
		hereditaryParameter.setSameNum(100);
		ParentChrosChoose parentChrosChoose = new OrderParentChrosChoose();
		//特征选择
		FeatureSeparation featureSeparation = new MedianFeatureSeparation();
		double stop = 0.01;
		//子空间
		ImprotenceAdjust improtenceAdjust = new PowerImprotenceAdjust();
		Hereditary hereditary = new Hereditary(trainDataSet);
		Evaluation evaluation=new Auc(new TeaEvidenceSynthesis());
		
		SubSpaceCreate subSpaceCreate=new SnFeatureSelectionSubSpaceCreate(improtenceAdjust,sns,fnMins,featureSeparation,stop,hereditary,hereditaryParameter,parentChrosChoose,evaluation);
//		SubSpaceCreate subSpaceCreate=new SnRandomSubSpaceCreate(sns,fnMins);
		List<List<Integer>> lists=subSpaceCreate.createSubSpaces(trainDataSet);
		System.out.println("子空间");
		for (List<Integer> list : lists) {
			System.out.println(list);
		}
		
		EvaluationThreadPoolExecutor.shutdown();
	}
}
