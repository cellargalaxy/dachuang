package version5.run;

import util.CloneObject;
import util.Printer;
import version5.dataSet.DataSet;
import version5.dataSet.DataSetParameter;
import version5.dataSet.DataSetSeparation;
import version5.evaluation.Evaluation;
import version5.evidenceSynthesis.*;
import version5.hereditary.*;
import version5.subSpace.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class Run {
	public static final void run(RunParameter runParameter) throws IOException, ClassNotFoundException {
		run(runParameter.getDataSetParameter(), runParameter.getDataSetSeparation(), runParameter.getTrainDataSet(), runParameter.getTestDataSet(), runParameter.getTrainOutputFile(), runParameter.getTestOutputFile(),
				runParameter.getHereditary(), runParameter.getHereditaryParameter(), runParameter.getParentChrosChoose(),
				runParameter.getEvaluation(),
				runParameter.getSubSpaceCreate(),
				runParameter.getSubSpaceEvidenceSynthesis());
	}
	
	public static final void run(DataSetParameter dataSetParameter, DataSetSeparation dataSetSeparation, DataSet trainDataSet, DataSet testDataSet, File trainOutputFile, File testOutputFile, //全局参数
	                             Hereditary hereditary, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose,//遗传算法
	                             Evaluation evaluation,
	                             SubSpaceCreate subSpaceCreate,//子空间
	                             EvidenceSynthesis subSpaceEvidenceSynthesis//子空间合成
	) throws IOException, ClassNotFoundException {
		List<List<Integer>> subSpaces = subSpaceCreate.createSubSpaces(trainDataSet);
		Printer.print("子空间:");
		for (List<Integer> subSpace : subSpaces) {
			Printer.print(subSpace);
		}
		Printer.print("--------------------------------------------------------");
		Printer.print();
		
		hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation);
		double allAuc = hereditary.getMaxAuc();
		double[] allChro = hereditary.getMaxChro();
		
		Map<DataSet, double[]> trainSubSpaceMap = new HashMap<DataSet, double[]>();
		Iterator<List<Integer>> iterator = subSpaces.iterator();
		while (iterator.hasNext()) {
			List<Integer> subSpace = iterator.next();
			DataSet dataSet = hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluation, subSpace);
			if (hereditary.getMaxAuc() >= allAuc) {
				trainSubSpaceMap.put(dataSet, hereditary.getMaxChro());
				Printer.print("训练子空间训练集进化AUC:" + hereditary.getMaxAuc());
			}
		}
		if (trainSubSpaceMap.size() == 0) {
			trainSubSpaceMap.put(trainDataSet, allChro);
			Printer.print("全子空间训练集进化AUC:" + allAuc);
		}
		
		DataSet trainSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(trainSubSpaceMap, subSpaceEvidenceSynthesis);
		double trainSubSpaceAuc = evaluation.countEvaluation(trainSubSpaceDataSet);
		Printer.print("trainSubSpaceDataSet auc:" + trainSubSpaceAuc);
		
		DataSet.outputDataSet(trainOutputFile, dataSetParameter, trainSubSpaceDataSet, dataSetSeparation, trainDataSet, subSpaceEvidenceSynthesis,
				evaluation, parentChrosChoose, subSpaceCreate, trainSubSpaceMap, trainSubSpaceAuc);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		Map<DataSet, double[]> testSubSpaceMap = new HashMap<DataSet, double[]>();
		for (Map.Entry<DataSet, double[]> entry : trainSubSpaceMap.entrySet()) {
			List<Integer> subSpace = entry.getKey().getEvidenceNums();
			DataSet dataSet = CloneObject.clone(testDataSet);
			dataSet.removeNotEqual(subSpace);
			testSubSpaceMap.put(dataSet, entry.getValue());
			Printer.print("测试子空间训练集进化AUC:" + evaluation.countEvaluation(dataSet));
		}
		DataSet testSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(testSubSpaceMap, subSpaceEvidenceSynthesis);
		double testSubSpaceAuc = evaluation.countEvaluation(testSubSpaceDataSet);
		Printer.print("testSubSpaceDataSet auc:" + testSubSpaceAuc);
		
		DataSet.outputDataSet(testOutputFile, dataSetParameter, testSubSpaceDataSet, dataSetSeparation, testDataSet, subSpaceEvidenceSynthesis,
				evaluation, parentChrosChoose, subSpaceCreate, testSubSpaceMap, testSubSpaceAuc);
		
		Printer.print("over");
	}
}
