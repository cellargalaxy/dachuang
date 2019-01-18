package top.cellargalaxy.dachuangspringboot.run;


import top.cellargalaxy.dachuangspringboot.dataSet.*;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.evaluation.EvaluationFactory;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesisFactory;
import top.cellargalaxy.dachuangspringboot.hereditary.*;
import top.cellargalaxy.dachuangspringboot.subSpace.SubSpaceCreate;
import top.cellargalaxy.dachuangspringboot.subSpace.SubSpaceCreateFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class Run {

	public static void main(String[] args) throws IOException {
		RunParameter runParameter = new RunParameter();

		DataSetParameter dataSetParameter = runParameter.getDataSetParameter();
		dataSetParameter.setIdColumnName("id");
		dataSetParameter.setEvidenceColumnName("evidence");
		dataSetParameter.setFraudColumnName("fraud");
		dataSetParameter.setUnfraudColumnName("unfraud");
		dataSetParameter.setLabelColumnName("collusion_transaction");
		dataSetParameter.setWithoutEvidences(Arrays.asList("total"));
		DataSetFileIO dataSetFileIO = DataSetFileIOFactory.getDataSetFileIO(runParameter);
		DataSet dataSet = dataSetFileIO.readFileToDataSet(new File("D:/g/8000+交易 获取满证据 9证据-所有证据数据 - 副本.csv"), dataSetParameter);
		DataSet[] dataSets = DataSetSplitFactory.getDataSetSplit(runParameter).splitDataSet(dataSet, runParameter.getTestPro(), runParameter.getTrainMissPro(), runParameter.getTestMissPro(), runParameter.getTrainLabel1Pro(), runParameter.getTestLabel1Pro());

		DataSet trainDataSet = dataSets[0];
		DataSet teatDataSet = dataSets[1];


		runParameter.setEvidenceSynthesisName(null);

		run(runParameter, trainDataSet, teatDataSet);

	}

	public static final void run(RunParameter runParameter, DataSet trainDataSet, DataSet testDataSet) throws IOException {
		EvidenceSynthesisFactory.setEvidenceSynthesis(null);
		HereditaryParameter hereditaryParameter = runParameter.getHereditaryParameter();
		ParentChrosChoose parentChrosChoose = ParentChrosChooseFactory.getParentChrosChoose(runParameter);
		Evaluation evaluation = EvaluationFactory.getEvaluation(runParameter, trainDataSet);
		SubSpaceCreate subSpaceCreate = SubSpaceCreateFactory.getSubSpaceCreate(runParameter, trainDataSet);

		HereditaryResult fullHereditaryResult = Hereditary.evolution(trainDataSet, hereditaryParameter, parentChrosChoose, evaluation);

		List<List<Integer>> subSpaces = subSpaceCreate.createSubSpaces(trainDataSet);
		System.out.println("子空间：" + subSpaces.size());
		for (List<Integer> subSpace : subSpaces) {
			System.out.println(subSpace);
		}

		Map<DataSet, HereditaryResult> trainSubSpaceMap = new HashMap<>();
		for (List<Integer> subSpace : subSpaces) {
			DataSet dataSet = trainDataSet.clone(subSpace);
			HereditaryResult hereditaryResult = Hereditary.evolution(trainDataSet, hereditaryParameter, parentChrosChoose, evaluation);
			if (hereditaryResult.getEvaluationValue() >= fullHereditaryResult.getEvaluationValue() * 0.9) {
				trainSubSpaceMap.put(dataSet, hereditaryResult);
				System.out.println("train auc: " + hereditaryResult.getEvaluationValue());
			}
		}

		if (trainSubSpaceMap.size() == 0) {
			trainSubSpaceMap.put(trainDataSet, fullHereditaryResult);
		}

		SubSpace2DataSetResult subSpace2DataSetResult = SubSpace2DataSet.subSpace2DataSet(runParameter, trainSubSpaceMap, evaluation);
		EvidenceSynthesis subSpaceEvidenceSynthesis = subSpace2DataSetResult.getEvidenceSynthesis();
		System.out.println("trainSubSpaceAuc: " + subSpace2DataSetResult.getEvaluationValue());

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		Map<DataSet, HereditaryResult> testSubSpaceMap = new HashMap<>();
		for (Map.Entry<DataSet, HereditaryResult> entry : trainSubSpaceMap.entrySet()) {
			Collection<Integer> subSpace = entry.getKey().getEvidenceName2EvidenceId().values();
			DataSet dataSet = testDataSet.clone(subSpace);
			testSubSpaceMap.put(dataSet, entry.getValue());
			System.out.println("test auc: " + evaluation.countEvaluation(dataSet, entry.getValue().getChromosome()));
		}
		DataSet testSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(testSubSpaceMap, subSpaceEvidenceSynthesis);
		double testSubSpaceAuc = evaluation.countEvaluation(testSubSpaceDataSet);
		System.out.println("testSubSpaceAuc: " + testSubSpaceAuc);

		System.out.println();
		System.out.println("evaluation: " + evaluation);
		System.out.println("subSpaceEvidenceSynthesis: " + subSpaceEvidenceSynthesis);

	}

}