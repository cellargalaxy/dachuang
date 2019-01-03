package top.cellargalaxy.dachuangspringboot.run;


import org.json.JSONObject;
import top.cellargalaxy.dachuangspringboot.util.CloneObject;
import top.cellargalaxy.dachuangspringboot.util.Printer;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSetParameter;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSetSeparation;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.evaluation.EvaluationExecutor;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.hereditary.Hereditary;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryParameter;
import top.cellargalaxy.dachuangspringboot.hereditary.ParentChrosChoose;
import top.cellargalaxy.dachuangspringboot.subSpace.SubSpaceCreate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class Run {
    public static final void runOutputFile(RunParameter runParameter, File trainOutputFile, File testOutputFile) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        run(runParameter.getDataSetParameter(), runParameter.getDataSetSeparation(), runParameter.getTrainDataSet(), runParameter.getTestDataSet(), trainOutputFile, testOutputFile,
                runParameter.getHereditary(), runParameter.getHereditaryParameter(), runParameter.getParentChrosChoose(),
                runParameter.getEvaluation(), runParameter.getEvaluationExecutor(),
                runParameter.getSubSpaceCreate(),
                runParameter.getSubSpaceEvidenceSynthesis());
    }

    public static final JSONObject runOutputJson(RunParameter runParameter) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        return run(runParameter.getDataSetSeparation(), runParameter.getTrainDataSet(), runParameter.getTestDataSet(),
                runParameter.getHereditary(), runParameter.getHereditaryParameter(), runParameter.getParentChrosChoose(),
                runParameter.getEvaluation(), runParameter.getEvaluationExecutor(),
                runParameter.getSubSpaceCreate(),
                runParameter.getSubSpaceEvidenceSynthesis());
    }

    public static final void run(DataSetParameter dataSetParameter, DataSetSeparation dataSetSeparation, DataSet trainDataSet, DataSet testDataSet, File trainOutputFile, File testOutputFile, //全局参数
                                 Hereditary hereditary, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose,//遗传算法
                                 Evaluation evaluation, EvaluationExecutor evaluationExecutor,
                                 SubSpaceCreate subSpaceCreate,//子空间
                                 EvidenceSynthesis subSpaceEvidenceSynthesis//子空间合成
    ) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        List<List<Integer>> subSpaces = subSpaceCreate.createSubSpaces(trainDataSet);
        Printer.print("子空间:");
        for (List<Integer> subSpace : subSpaces) {
            Printer.print(subSpace);
        }
        Printer.print("--------------------------------------------------------");
        Printer.print();

        hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluationExecutor);
        double allAuc = hereditary.getMaxAuc();
        double[] allChro = hereditary.getMaxChro();

        Map<DataSet, double[]> trainSubSpaceMap = new HashMap<DataSet, double[]>();
        Iterator<List<Integer>> iterator = subSpaces.iterator();
        while (iterator.hasNext()) {
            List<Integer> subSpace = iterator.next();
            DataSet dataSet = hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluationExecutor, subSpace);
            if (hereditary.getMaxAuc() >= allAuc * 0.9) {
                trainSubSpaceMap.put(dataSet, hereditary.getMaxChro());
                Printer.print("训练子空间训练集进化AUC: " + hereditary.getMaxAuc());
            }
        }
        if (trainSubSpaceMap.size() == 0) {
            trainSubSpaceMap.put(trainDataSet, allChro);
            Printer.print("全子空间训练集进化AUC: " + allAuc);
        }

        DataSet trainSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(trainSubSpaceMap, subSpaceEvidenceSynthesis);
        double trainSubSpaceAuc = evaluation.countEvaluation(trainSubSpaceDataSet);
        Printer.print("trainSubSpaceDataSet auc: " + trainSubSpaceAuc);

        DataSet.outputDataSetResutl(trainOutputFile, dataSetParameter, trainSubSpaceDataSet, dataSetSeparation, trainDataSet, subSpaceEvidenceSynthesis,
                evaluation, parentChrosChoose, subSpaceCreate, trainSubSpaceMap, trainSubSpaceAuc);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Map<DataSet, double[]> testSubSpaceMap = new HashMap<DataSet, double[]>();
        for (Map.Entry<DataSet, double[]> entry : trainSubSpaceMap.entrySet()) {
            List<Integer> subSpace = entry.getKey().getEvidenceNums();
            DataSet dataSet = CloneObject.clone(testDataSet);
            dataSet.removeNotEqual(subSpace);
            testSubSpaceMap.put(dataSet, entry.getValue());
            Printer.print("测试子空间训练集进化AUC: " + evaluation.countEvaluation(dataSet));
        }
        DataSet testSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(testSubSpaceMap, subSpaceEvidenceSynthesis);
        double testSubSpaceAuc = evaluation.countEvaluation(testSubSpaceDataSet);
        Printer.print("testSubSpaceDataSet auc: " + testSubSpaceAuc);

        DataSet.outputDataSetResutl(testOutputFile, dataSetParameter, testSubSpaceDataSet, dataSetSeparation, testDataSet, subSpaceEvidenceSynthesis,
                evaluation, parentChrosChoose, subSpaceCreate, testSubSpaceMap, testSubSpaceAuc);

        Printer.print("over");
    }

    public static final JSONObject run(DataSetSeparation dataSetSeparation, DataSet trainDataSet, DataSet testDataSet, //全局参数
                                       Hereditary hereditary, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose,//遗传算法
                                       Evaluation evaluation, EvaluationExecutor evaluationExecutor,
                                       SubSpaceCreate subSpaceCreate,//子空间
                                       EvidenceSynthesis subSpaceEvidenceSynthesis//子空间合成
    ) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject();

        List<List<Integer>> subSpaces = subSpaceCreate.createSubSpaces(trainDataSet);
        Printer.print("子空间:");
        for (List<Integer> subSpace : subSpaces) {
            Printer.print(subSpace);
        }
        Printer.print("--------------------------------------------------------");
        Printer.print();

        hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluationExecutor);
        double allAuc = hereditary.getMaxAuc();
        double[] allChro = hereditary.getMaxChro();

        Map<DataSet, double[]> trainSubSpaceMap = new HashMap<DataSet, double[]>();
        Iterator<List<Integer>> iterator = subSpaces.iterator();
        while (iterator.hasNext()) {
            List<Integer> subSpace = iterator.next();
            DataSet dataSet = hereditary.evolution(hereditaryParameter, parentChrosChoose, evaluationExecutor, subSpace);
            if (hereditary.getMaxAuc() >= allAuc * 0.9) {
                trainSubSpaceMap.put(dataSet, hereditary.getMaxChro());
                Printer.print("训练子空间训练集进化AUC: " + hereditary.getMaxAuc());
            }
        }
        if (trainSubSpaceMap.size() == 0) {
            trainSubSpaceMap.put(trainDataSet, allChro);
            Printer.print("全子空间训练集进化AUC: " + allAuc);
        }

        DataSet trainSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(trainSubSpaceMap, subSpaceEvidenceSynthesis);
        double trainSubSpaceAuc = evaluation.countEvaluation(trainSubSpaceDataSet);
        Printer.print("trainSubSpaceDataSet auc: " + trainSubSpaceAuc);

        jsonObject.put("trainDataSetOutput", DataSet.outputDataSetResutl(trainSubSpaceDataSet, dataSetSeparation, trainDataSet, subSpaceEvidenceSynthesis,
                evaluation, parentChrosChoose, subSpaceCreate, trainSubSpaceMap, trainSubSpaceAuc));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Map<DataSet, double[]> testSubSpaceMap = new HashMap<DataSet, double[]>();
        for (Map.Entry<DataSet, double[]> entry : trainSubSpaceMap.entrySet()) {
            List<Integer> subSpace = entry.getKey().getEvidenceNums();
            DataSet dataSet = CloneObject.clone(testDataSet);
            dataSet.removeNotEqual(subSpace);
            testSubSpaceMap.put(dataSet, entry.getValue());
            Printer.print("测试子空间训练集进化AUC: " + evaluation.countEvaluation(dataSet));
        }
        DataSet testSubSpaceDataSet = SubSpace2DataSet.subSpace2DataSet(testSubSpaceMap, subSpaceEvidenceSynthesis);
        double testSubSpaceAuc = evaluation.countEvaluation(testSubSpaceDataSet);
        Printer.print("testSubSpaceDataSet auc: " + testSubSpaceAuc);

        jsonObject.put("testDataSetOutput", DataSet.outputDataSetResutl(testSubSpaceDataSet, dataSetSeparation, testDataSet, subSpaceEvidenceSynthesis,
                evaluation, parentChrosChoose, subSpaceCreate, testSubSpaceMap, testSubSpaceAuc));

        Printer.print("over");
        return jsonObject;
    }
}
