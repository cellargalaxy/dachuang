package top.cellargalaxy.dachuangspringboot.run;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import top.cellargalaxy.dachuangspringboot.dataSet.*;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.evaluation.EvaluationFactory;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.DsEvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.hereditary.*;
import top.cellargalaxy.dachuangspringboot.subSpace.SubSpaceCreate;
import top.cellargalaxy.dachuangspringboot.subSpace.SubSpaceCreateFactory;
import top.cellargalaxy.dachuangspringboot.subSpaceSynthesis.SubSpaceSynthesis;
import top.cellargalaxy.dachuangspringboot.subSpaceSynthesis.SubSpaceSynthesisResult;
import top.cellargalaxy.dachuangspringboot.util.IOUtils;
import top.cellargalaxy.dachuangspringboot.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class Run {
	public static final Yaml YAML = new Yaml();
	public static Logger logger = (Logger) LoggerFactory.getLogger(Run.class);

	public static void main(String[] args) throws IOException {


		toTab();

//		AbstractEvaluation.shutdownExecutorService();
	}

	public static void run() throws IOException {
		int i = 1;
		for (double j = 0.1; j < 0.6; j = j + 0.1) {
			for (int k = 0; k < 11; k++) {
				String name = "实验" + i;

				logger = (Logger) LoggerFactory.getLogger(name);

				RunParameter runParameter = new RunParameter();

				DataSetParameter dataSetParameter = runParameter.getDataSetParameter();
				dataSetParameter.setIdColumnName("id");
				dataSetParameter.setEvidenceColumnName("evidence");
				dataSetParameter.setFraudColumnName("fraud");
				dataSetParameter.setUnfraudColumnName("unfraud");
				dataSetParameter.setLabelColumnName("collusion_transaction");
				dataSetParameter.setWithoutEvidences(Arrays.asList("total"));

				runParameter.setDataSetParameter(dataSetParameter);

				runParameter.setDataSetPath("E:/g/transaction.csv");

				runParameter.setTestPro(j);
				runParameter.setTrainMissPro(0);
				runParameter.setTestMissPro(0);
				runParameter.setTrainLabel1Pro(0.2);
				runParameter.setTestLabel1Pro(0.2);

				runParameter.setEvidenceSynthesisName(DsEvidenceSynthesis.NAME);
				runParameter.setSubSpaceEvidenceSynthesisName(DsEvidenceSynthesis.NAME);

				run(runParameter, name);

				i++;
			}
		}
	}

	public static final void toTab() throws IOException {
		List<String> keys = Arrays.asList(
				"测试集比例",
				"训练集-缺失比例",
				"测试集-缺失比例",
				"训练集-1标签比例",
				"测试集-1标签比例",
				"完整-0标签-数量",
				"完整-1标签-数量",
				"缺失-0标签-数量",
				"缺失-1标签-数量",
				"训练集-完整-0标签-实际数量",
				"训练集-完整-1标签-实际数量",
				"训练集-缺失-0标签-实际数量",
				"训练集-缺失-1标签-实际数量",
				"测试集-完整-0标签-实际数量",
				"测试集-完整-1标签-实际数量",
				"测试集-缺失-0标签-实际数量",
				"测试集-缺失-1标签-实际数量",
				"训练集-证据统计-实际数量",
				"测试集-证据统计-实际数量",
				"训练集-原生数据集AUC",
				"训练集-原生数据集遗传AUC",
				"训练集-子空间合成的合成算法的子空间AUC",
				"训练集-子空间合成的合成算法的子空间遗传AUC",
				"测试集-原生数据集AUC",
				"测试集-原生数据集遗传AUC",
				"测试集-使用子空间合成的合成算法的子空间AUC",
				"测试集-使用子空间合成的合成算法的子空间遗传AUC"
		);
		Map<String, Map<String, String>> data = new HashMap<>();
		File folder = new File("E:\\g\\实验结果 - 副本");
		for (File file : folder.listFiles()) {
			if (file.isDirectory() && file.getName().contains("实验")) {
				File log = new File(file.getAbsolutePath(), "log.log");
				Map<String, String> map = new HashMap<>();
				try (BufferedReader reader = IOUtils.getReader(log)) {
					String string;
					while ((string = reader.readLine()) != null) {
						for (String key : keys) {
							if (map.get(key) != null) {
								continue;
							}
							if (string.contains(key)) {
								String[] strings = string.split(key);
								map.put(key, strings[1].replaceFirst(":", "").trim());
							}
						}
					}
				}
				data.put(log.getParentFile().getName(), map);
			}
		}

		System.out.print("序号\t");
		for (String string : keys) {
			System.out.print(string + "\t");
		}
		System.out.println();
		for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
			System.out.print(entry.getKey() + "\t");
			Map<String, String> map = entry.getValue();
			for (String key : keys) {
				System.out.print(map.get(key) + "\t");
			}
			System.out.println();
		}
	}

	public static final void run(RunParameter runParameter) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String path = dateFormat.format(new Date());
		run(runParameter, path);
	}

	public static final void run(File runParameterFile, String path) throws IOException {
		String config = String.valueOf(IOUtils.readFile(runParameterFile));
		RunParameter runParameter = YAML.loadAs(config, RunParameter.class);
		run(runParameter, path);
	}

	public static final void run(RunParameter runParameter, String path) {
		LoggerContext loggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setCharset(Charset.forName("UTF-8"));
		encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
		encoder.setImmediateFlush(true);
		encoder.setContext(loggerFactory);
		FileAppender appender = new FileAppender();
		appender.setEncoder(encoder);
		appender.setFile(path + File.separator + "log.log");
		appender.setAppend(false);
		appender.setContext(loggerFactory);
		encoder.start();
		appender.start();
		logger.addAppender(appender);

		try {
			String config = YAML.dump(runParameter);
			File runParameterFile = new File(path, "config.yml");
			IOUtils.writeFile(config.getBytes(), runParameterFile);
			logger.info("配置文件保存在: {}", runParameterFile);

			if (!StringUtils.isBlank(runParameter.getTrainDataSetPath()) && !StringUtils.isBlank(runParameter.getTeatDataSettPath())) {
				run(runParameter, path, new File(runParameter.getTrainDataSetPath()), new File(runParameter.getTeatDataSettPath()));
			} else {
				run(runParameter, path, new File(runParameter.getDataSetPath()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("\n" + StringUtils.printException(e));
		}
	}

	private static final void run(RunParameter runParameter, String path, File dataSetFile) throws IOException, ExecutionException, InterruptedException {
		logger.info("数据集文件: {}", dataSetFile);
		DataSetFileIO dataSetFileIO = DataSetFileIOFactory.getDataSetFileIO(runParameter);
		logger.info("文件IO类型: {}", dataSetFileIO);
		DataSet dataSet = dataSetFileIO.readFileToDataSet(dataSetFile, runParameter.getDataSetParameter());
		DataSet[] dataSets = DataSetSplitFactory.getDataSetSplit(runParameter).splitDataSet(dataSet, runParameter.getTestPro(), runParameter.getTrainMissPro(), runParameter.getTestMissPro(), runParameter.getTrainLabel1Pro(), runParameter.getTestLabel1Pro(), runParameter.getK());
		DataSet trainDataSet = dataSets[0];
		DataSet testDataSet = dataSets[1];

		DataSetParameter dataSetParameter = runParameter.getDataSetParameter();
		logger.info("数据集参数: {}", dataSetParameter);
		File trainDataSetFile = new File(path, "dataSet-train.csv");
		File testDataSetFile = new File(path, "dataSet-test.csv");
		dataSetFileIO.writeDataSetToFile(dataSetParameter, trainDataSet, trainDataSetFile);
		logger.info("训练集-数据集保存在: {}", trainDataSetFile);
		dataSetFileIO.writeDataSetToFile(dataSetParameter, testDataSet, testDataSetFile);
		logger.info("测试集-数据集保存在: {}", testDataSetFile);

		run(runParameter, path, trainDataSetFile, testDataSetFile);
	}

	private static final void run(RunParameter runParameter, String path, File trainDataSetFile, File teatDataSetFile) throws IOException, ExecutionException, InterruptedException {
		logger.info("训练集文件: {}", trainDataSetFile);
		logger.info("测试集文件: {}", teatDataSetFile);
		DataSetFileIO dataSetFileIO = DataSetFileIOFactory.getDataSetFileIO(runParameter);
		DataSet trainDataSet = dataSetFileIO.readFileToDataSet(trainDataSetFile, runParameter.getDataSetParameter());
		DataSet testDataSet = dataSetFileIO.readFileToDataSet(teatDataSetFile, runParameter.getDataSetParameter());
		File trainSubSpaceDataSetFile = new File(path, "subSpaceDataSet-train.csv");
		File testSubSpaceDataSetFile = new File(path, "subSpaceDataSet-test.csv");
		File runResultFile = new File(path, "runResult.yml");
		run(runParameter, trainDataSet, testDataSet, trainSubSpaceDataSetFile, testSubSpaceDataSetFile, runResultFile);
	}

	private static final void run(RunParameter runParameter, DataSet trainDataSet, DataSet testDataSet, File trainSubSpaceDataSetFile, File testSubSpaceDataSetFile, File runResultFile) throws IOException, ExecutionException, InterruptedException {
		DataSetFileIO dataSetFileIO = DataSetFileIOFactory.getDataSetFileIO(runParameter);
		DataSetParameter dataSetParameter = runParameter.getDataSetParameter();
		HereditaryParameter hereditaryParameter = runParameter.getHereditaryParameter();
		ParentChrosChoose parentChrosChoose = ParentChrosChooseFactory.getParentChrosChoose(runParameter);
		Evaluation evaluation = EvaluationFactory.getEvaluation(runParameter, trainDataSet);
		SubSpaceCreate subSpaceCreate = SubSpaceCreateFactory.getSubSpaceCreate(runParameter, trainDataSet);

		logger.info("文件IO类型: {}", dataSetFileIO);
		logger.info("数据集参数: {}", dataSetParameter);
		logger.info("遗传算法参数: {}", hereditaryParameter);
		logger.info("使用的父母染色体选择算法: {}", parentChrosChoose);
		logger.info("使用的评价算法: {}", evaluation);
		logger.info("使用的子空间算法: {}", subSpaceCreate);

		List<RunResult> runResults = run(runParameter, dataSetParameter, dataSetFileIO, trainDataSet, testDataSet,
				hereditaryParameter, parentChrosChoose,
				evaluation,
				subSpaceCreate,
				trainSubSpaceDataSetFile, testSubSpaceDataSetFile);

		String runResultString = YAML.dump(runResults);
		IOUtils.writeFile(runResultString.getBytes(), runResultFile);
		logger.info("运行结果文件保存在: {}", runResultFile);
	}

	private static final List<RunResult> run(RunParameter runParameter, DataSetParameter dataSetParameter, DataSetFileIO dataSetFileIO, DataSet trainDataSet, DataSet testDataSet,
	                                         HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose,
	                                         Evaluation evaluation,
	                                         SubSpaceCreate subSpaceCreate,
	                                         File trainSubSpaceDataSetFile, File testSubSpaceDataSetFile) throws IOException, ExecutionException, InterruptedException {

		logger.info("训练集-原生数据集AUC: {}", evaluation.countEvaluation(trainDataSet).get());
		HereditaryResult fullHereditaryResult = Hereditary.evolution(trainDataSet, hereditaryParameter, parentChrosChoose, evaluation);
		logger.info("训练集-原生数据集遗传AUC: {}", fullHereditaryResult.getEvaluationValue());

		List<RunResult> trainRunResults = new LinkedList<>();
		List<List<Integer>> subSpaces = subSpaceCreate.createSubSpaces(trainDataSet);
		for (List<Integer> subSpace : subSpaces) {
			DataSet dataSet = trainDataSet.clone(subSpace);
			HereditaryResult hereditaryResult = Hereditary.evolution(dataSet, hereditaryParameter, parentChrosChoose, evaluation);
			if (hereditaryResult.getEvaluationValue() >= fullHereditaryResult.getEvaluationValue() * 0.9) {
				trainRunResults.add(new RunResult(subSpace, dataSet, hereditaryResult.getChromosome()));
				logger.info("训练集-优秀子空间AUC: {},\t {}", hereditaryResult.getEvaluationValue(), subSpace);
			} else {
				logger.info("训练集-劣质子空间AUC: {},\t {}", hereditaryResult.getEvaluationValue(), subSpace);
			}
		}

		if (trainRunResults.size() == 0) {
			trainRunResults.add(new RunResult(trainDataSet.getEvidenceName2EvidenceId().values(), trainDataSet, fullHereditaryResult.getChromosome()));
			logger.info("训练集-没有优秀子空间，添加完整特征集作为默认");
		}

		SubSpaceSynthesisResult subSpaceSynthesisResult = SubSpaceSynthesis.synthesisSubSpace(runParameter, trainRunResults, evaluation);
		DataSet trainSubSpaceDataSet = subSpaceSynthesisResult.getDataSet();
		dataSetFileIO.writeDataSetToFile(dataSetParameter, trainSubSpaceDataSet, trainSubSpaceDataSetFile);
		logger.info("训练集-子空间合成数据集保存在: {}", trainSubSpaceDataSetFile);
		EvidenceSynthesis subSpaceEvidenceSynthesis = subSpaceSynthesisResult.getEvidenceSynthesis();
		logger.info("训练集-子空间合成的合成算法: {}", subSpaceEvidenceSynthesis);
		logger.info("训练集-子空间合成的合成算法的子空间AUC: {}", subSpaceSynthesisResult.getEvaluationValue());
		HereditaryResult trainSubSpaceHereditaryResult = Hereditary.evolution(trainSubSpaceDataSet, hereditaryParameter, parentChrosChoose, evaluation);
		logger.info("训练集-子空间合成的合成算法的子空间遗传AUC: {}", trainSubSpaceHereditaryResult.getEvaluationValue());

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		logger.info("测试集-原生数据集AUC: {}", evaluation.countEvaluation(testDataSet).get());
		logger.info("测试集-原生数据集遗传AUC: {}", evaluation.countEvaluation(testDataSet, fullHereditaryResult.getChromosome()).get());

		List<RunResult> testRunResults = new LinkedList<>();
		for (RunResult trainRunResult : trainRunResults) {
			Collection<Integer> subSpace = trainRunResult.getSubSpace();
			DataSet dataSet = testDataSet.clone(subSpace);
			testRunResults.add(new RunResult(subSpace, dataSet, trainRunResult.getChromosome()));
			logger.info("测试集-使用优秀子空间AUC: {},\t {}", evaluation.countEvaluation(dataSet, trainRunResult.getChromosome()).get(), subSpace);
		}
		DataSet testSubSpaceDataSet = SubSpaceSynthesis.synthesisSubSpace(testRunResults, subSpaceEvidenceSynthesis);
		dataSetFileIO.writeDataSetToFile(dataSetParameter, testSubSpaceDataSet, testSubSpaceDataSetFile);
		logger.info("测试集-子空间合成数据集保存在: {}", testSubSpaceDataSetFile);
		logger.info("测试集-使用子空间合成的合成算法的子空间AUC: {}", evaluation.countEvaluation(testSubSpaceDataSet).get());
		logger.info("测试集-使用子空间合成的合成算法的子空间遗传AUC: {}", evaluation.countEvaluation(testSubSpaceDataSet, trainSubSpaceHereditaryResult.getChromosome()).get());

		return trainRunResults;
	}

}