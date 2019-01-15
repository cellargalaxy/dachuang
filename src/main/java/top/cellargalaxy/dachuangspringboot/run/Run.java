package top.cellargalaxy.dachuangspringboot.run;


import top.cellargalaxy.dachuangspringboot.dataSet.*;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.evaluation.EvaluationFactory;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesisFactory;
import top.cellargalaxy.dachuangspringboot.hereditary.*;
import top.cellargalaxy.dachuangspringboot.subSpace.RandomSubSpaceCreate;
import top.cellargalaxy.dachuangspringboot.subSpace.SubSpaceCreate;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class Run {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, ClassNotFoundException {
		FromFileReadDataSet fromFileReadDataSet = new FromCsvReadDataSetImpl();
		DataSetParameter dataSetParameter = new DataSetParameter();
		dataSetParameter.setEvidenceColumnName("证据");
		dataSetParameter.setFraudColumnName("A");
		dataSetParameter.setUnfraudColumnName("B");
		DataSet trainDataSet = fromFileReadDataSet.readDataSetFromFile(new File("D:/g/trainAll.csv"), dataSetParameter);
		DataSet teatDataSet = fromFileReadDataSet.readDataSetFromFile(new File("D:/g/testAll.csv"), dataSetParameter);
		run(trainDataSet,teatDataSet,new HereditaryParameter(),ParentChrosChooseFactory.createParentChrosChoose(),new RandomSubSpaceCreate(), EvidenceSynthesisFactory.createEvidenceSynthesis(), EvaluationFactory.createEvaluation());

	}

	public static final void run(DataSet trainDataSet, DataSet testDataSet,//全局参数
	                             HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose,//遗传算法
	                             SubSpaceCreate subSpaceCreate, EvidenceSynthesis subSpaceEvidenceSynthesis,//子空间合成
	                             Evaluation evaluation
	) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
		List<List<Integer>> subSpaces = subSpaceCreate.createSubSpaces(trainDataSet);

		HereditaryResult fullHereditaryResult = Hereditary.evolution(trainDataSet, hereditaryParameter, parentChrosChoose, evaluation);

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

		DataSet trainSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(trainSubSpaceMap, subSpaceEvidenceSynthesis);
		double trainSubSpaceAuc = evaluation.countEvaluation(trainSubSpaceDataSet);
		System.out.println("trainSubSpaceAuc: " + trainSubSpaceAuc);

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
	}

}