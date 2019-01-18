package top.cellargalaxy.dachuangspringboot.run;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.cellargalaxy.dachuangspringboot.dataSet.*;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.evaluation.EvaluationFactory;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.hereditary.*;
import top.cellargalaxy.dachuangspringboot.subSpace.SubSpaceCreate;
import top.cellargalaxy.dachuangspringboot.subSpace.SubSpaceCreateFactory;
import top.cellargalaxy.dachuangspringboot.subSpaceSynthesis.SubSpaceSynthesis;
import top.cellargalaxy.dachuangspringboot.subSpaceSynthesis.SubSpaceSynthesisResult;
import top.cellargalaxy.dachuangspringboot.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class Run {
	private static final Logger logger = LoggerFactory.getLogger(Run.class);

	public static void main(String[] args) throws IOException {
		RunParameter runParameter = new RunParameter();

		DataSetParameter dataSetParameter = runParameter.getDataSetParameter();
		dataSetParameter.setIdColumnName("id");
		dataSetParameter.setEvidenceColumnName("evidence");
		dataSetParameter.setFraudColumnName("fraud");
		dataSetParameter.setUnfraudColumnName("unfraud");
		dataSetParameter.setLabelColumnName("collusion_transaction");
		dataSetParameter.setWithoutEvidences(Arrays.asList("total"));

		runParameter.setDataSetParameter(dataSetParameter);

		runParameter.setDataSetPath("D:/g/8000+交易 获取满证据 9证据-所有证据数据 - 副本.csv");

		run(runParameter);
	}

	public static final void run(RunParameter runParameter) throws IOException {
		if (!StringUtils.isBlank(runParameter.getTrainDataSetPath()) && !StringUtils.isBlank(runParameter.getTeatDataSettPath())) {
			run(runParameter, new File(runParameter.getTrainDataSetPath()), new File(runParameter.getTeatDataSettPath()));
		} else {
			run(runParameter, new  File(runParameter.getDataSetPath()));
		}
	}

	public static final void run(RunParameter runParameter, File file) throws IOException {
		DataSetFileIO dataSetFileIO = DataSetFileIOFactory.getDataSetFileIO(runParameter);
		DataSet dataSet = dataSetFileIO.readFileToDataSet(file, runParameter.getDataSetParameter());
		DataSet[] dataSets = DataSetSplitFactory.getDataSetSplit(runParameter).splitDataSet(dataSet, runParameter.getTestPro(), runParameter.getTrainMissPro(), runParameter.getTestMissPro(), runParameter.getTrainLabel1Pro(), runParameter.getTestLabel1Pro());
		DataSet trainDataSet = dataSets[0];
		DataSet teatDataSet = dataSets[1];
		run(runParameter, trainDataSet, teatDataSet);

	}

	public static final void run(RunParameter runParameter, File trainDataSetFile, File teatDataSettFile) throws IOException {
		DataSetFileIO dataSetFileIO = DataSetFileIOFactory.getDataSetFileIO(runParameter);
		DataSet trainDataSet = dataSetFileIO.readFileToDataSet(trainDataSetFile, runParameter.getDataSetParameter());
		DataSet testDataSet = dataSetFileIO.readFileToDataSet(teatDataSettFile, runParameter.getDataSetParameter());
		run(runParameter, trainDataSet, testDataSet);
	}

	public static final void run(RunParameter runParameter, DataSet trainDataSet, DataSet testDataSet) throws IOException {
		HereditaryParameter hereditaryParameter = runParameter.getHereditaryParameter();
		ParentChrosChoose parentChrosChoose = ParentChrosChooseFactory.getParentChrosChoose(runParameter);
		Evaluation evaluation = EvaluationFactory.getEvaluation(runParameter, trainDataSet);
		SubSpaceCreate subSpaceCreate = SubSpaceCreateFactory.getSubSpaceCreate(runParameter, trainDataSet);

		logger.info("使用的父母染色体选择算法: {}", parentChrosChoose);
		logger.info("使用的评价算法: {}", evaluation);
		logger.info("使用的子空间算法: {}", subSpaceCreate);

		HereditaryResult fullHereditaryResult = Hereditary.evolution(trainDataSet, hereditaryParameter, parentChrosChoose, evaluation);
		logger.info("训练集-完整数据集的AUC: {}", fullHereditaryResult.getEvaluationValue());

		List<List<Integer>> subSpaces = subSpaceCreate.createSubSpaces(trainDataSet);
		Map<DataSet, HereditaryResult> trainSubSpaceMap = new HashMap<>();
		for (List<Integer> subSpace : subSpaces) {
			DataSet dataSet = trainDataSet.clone(subSpace);
			HereditaryResult hereditaryResult = Hereditary.evolution(trainDataSet, hereditaryParameter, parentChrosChoose, evaluation);
			if (hereditaryResult.getEvaluationValue() >= fullHereditaryResult.getEvaluationValue() * 0.9) {
				trainSubSpaceMap.put(dataSet, hereditaryResult);
				logger.info("训练集-优秀子空间AUC: {},\t {}", hereditaryResult.getEvaluationValue(), subSpace);
			} else {
				logger.info("训练集-劣质子空间AUC: {},\t {}", hereditaryResult.getEvaluationValue(), subSpace);
			}
		}

		if (trainSubSpaceMap.size() == 0) {
			trainSubSpaceMap.put(trainDataSet, fullHereditaryResult);
			logger.info("训练集-没有优秀子空间，添加完整特征集作为默认");
		}

		SubSpaceSynthesisResult subSpaceSynthesisResult = SubSpaceSynthesis.synthesisSubSpace(runParameter, trainSubSpaceMap, evaluation);
		EvidenceSynthesis subSpaceEvidenceSynthesis = subSpaceSynthesisResult.getEvidenceSynthesis();
		logger.info("训练集-子空间合成所自动选择的合成算法: {}", subSpaceEvidenceSynthesis);
		logger.info("训练集-子空间合成所自动选择的合成算法的子空间AUC: {}", subSpaceSynthesisResult.getEvaluationValue());

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		Map<DataSet, HereditaryResult> testSubSpaceMap = new HashMap<>();
		for (Map.Entry<DataSet, HereditaryResult> entry : trainSubSpaceMap.entrySet()) {
			Collection<Integer> subSpace = entry.getKey().getEvidenceName2EvidenceId().values();
			DataSet dataSet = testDataSet.clone(subSpace);
			testSubSpaceMap.put(dataSet, entry.getValue());
			logger.info("测试集-使用优秀子空间AUC: {},\t {}", evaluation.countEvaluation(dataSet, entry.getValue().getChromosome()), subSpace);
		}
		DataSet testSubSpaceDataSet = SubSpaceSynthesis.synthesisSubSpace(testSubSpaceMap, subSpaceEvidenceSynthesis);
		logger.info("测试集-使用子空间合成所自动选择的合成算法的子空间AUC: {}", evaluation.countEvaluation(testSubSpaceDataSet));

	}

}