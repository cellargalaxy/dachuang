package testRun;

import auc.AUC;
import dataSet.DataSet;
import dataSet.Id;
import dataSet.OutputDataSet;
import dataSet.SubDataSet;
import feature.FeatureSelection;
import feature.SubSpace;
import feature.TestSetTest;
import hereditary.*;
import util.CloneObject;

import java.io.*;
import java.util.*;

/**
 * Created by cellargalaxy on 17-7-3.
 * 用于测试整个体系的静态类
 */
public class TestRun {//0.8255032462236143  0.8469250096962953

    public static void main(String[] args) throws IOException, ClassNotFoundException {//0.8452342454394693
        testRun(new ParameterImpl(), new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/trainAll.csv"), ",", 0, 2, 3, 1, 5, "/home/cellargalaxy", "gbk",
                new HereditaryParameter(), 0.95, 0.01, FeatureSelection.MEDIAN_MODEL, 0, HereditaryParameter.USE_ORDER, 5,
                SubSpace.POWER_ADJUST, 0,
                TestSetTest.AVER_SYNTHESIS, 0, 0, 0, 0);

//        Parameter parameter=new ParameterImpl();
//        SubDataSet subDataSet=new SubDataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/trainAll.csv"),",", 0, 2, 3, 1, 5);
//        parameter.receiveCreateSubDataSet(subDataSet.getCom0Count(),subDataSet.getCom1Count(),subDataSet.getMiss0Count(),subDataSet.getMiss1Count());
//        DataSet[] subDataSets=subDataSet.createSubDataSet(parameter.getTest(),parameter.getMiss(),parameter.getLabel1());
//        System.out.println(subDataSet.getIds().size());
//        System.out.println(subDataSets[0].getIds().size());
//        System.out.println(subDataSets[1].getIds().size());
    }

    private static void testRun() throws IOException, ClassNotFoundException {
        DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/trainAll.csv"),
                ",", 0, 2, 3, 1, 5);
        double dsAUC = AUC.countAUC(trainDataSet, Id.DS_METHOD);
        double myDSAUC = AUC.countAUC(trainDataSet, Id.MY_DS_METHOD);
        double myDSAUC2 = AUC.countAUC(trainDataSet, Id.MY_DS_METHOD2);
        int DSMethod = Id.MY_DS_METHOD2;
        System.out.println(myDSAUC2);
//        if (dsAUC > myDSAUC) {
//            if (dsAUC>myDSAUC2) {
//                DSMethod = Id.DS_METHOD;
//                System.out.println("训练集原始AUC:" + dsAUC);
//            }else {
//                DSMethod = Id.MY_DS_METHOD2;
//                System.out.println("训练集原始AUC:" + myDSAUC2);
//            }
//        } else {
//            if (myDSAUC>myDSAUC2) {
//                DSMethod = Id.MY_DS_METHOD;
//                System.out.println("训练集原始AUC:" + myDSAUC);
//            }else {
//                DSMethod = Id.MY_DS_METHOD2;
//                System.out.println("训练集原始AUC:" + myDSAUC2);
//            }
//        }
        System.out.println("----------------------------------");


        double[][] featureSelections = FeatureSelection.featureSelection(new HereditaryParameter(), DSMethod, trainDataSet,
                0.95, 0.01, FeatureSelection.MEDIAN_MODEL, 0, HereditaryParameter.USE_ORDER, 5);
        System.out.println("特征选择");
        for (double[] featureSelection : featureSelections) {
            System.out.println(Arrays.toString(featureSelection));
        }

        LinkedList<Integer> features = new LinkedList<Integer>();
        double[] impros = new double[featureSelections.length];
        for (int i = 0; i < featureSelections.length; i++) {
            features.add((int) featureSelections[i][0]);
            impros[i] = featureSelections[i][1];
        }

        DataSet trainDataSet1 = CloneObject.clone(trainDataSet);
        trainDataSet1.allSaveEvidence(features);
        double featureAUC = AUC.countAUC(trainDataSet1, DSMethod);
        System.out.println("特征选择训练集AUC:" + featureAUC);
        System.out.println("----------------------------------");

        int[] sn = {1, 2, 3, 4};
        LinkedList<LinkedList<Integer>> subSpaces = SubSpace.createSubSpaces(features, impros, sn, 15, SubSpace.POWER_ADJUST, 0);
        System.out.println("子空间:");
        for (LinkedList<Integer> subSpace : subSpaces) {
            System.out.println(subSpace);
        }

        LinkedList<DataSet> trainDataSets = new LinkedList<>();
        LinkedList<Hereditary> hereditarys = new LinkedList<>();
        Iterator<LinkedList<Integer>> iterator = subSpaces.iterator();
        while (iterator.hasNext()) {
            LinkedList<Integer> subSpace = iterator.next();
            DataSet dataSet = CloneObject.clone(trainDataSet);
            dataSet.allSaveEvidence(subSpace);
            Hereditary hereditary = Hereditary.superEvolution(new HereditaryParameter(), DSMethod, dataSet, HereditaryParameter.USE_ORDER, 5);
            if (hereditary.getMaxAUC() >= featureAUC) {
                trainDataSets.add(dataSet);
                hereditarys.add(hereditary);
                System.out.println("子空间训练集进化AUC:" + hereditary.getMaxAUC());
            } else {
                iterator.remove();
            }
        }
        if (subSpaces.size() == 0) {
            LinkedList<Integer> subSpace = trainDataSet.getEvidenceNums();
            DataSet dataSet = CloneObject.clone(trainDataSet);
            Hereditary hereditary = Hereditary.superEvolution(new HereditaryParameter(), DSMethod, dataSet, HereditaryParameter.USE_ORDER, 5);
            trainDataSets.add(dataSet);
            hereditarys.add(hereditary);
            System.out.println("全子空间训练集进化AUC:" + hereditary.getMaxAUC());
        }
        TestSetTest.DSSubSpace(TestSetTest.AVER_SYNTHESIS, DSMethod, trainDataSets, trainDataSet, 0, 0, 0, 0);

        double trainAUC = AUC.countAUC(trainDataSet, Id.TEST_DS_METHOD);
        System.out.println("训练集TrainAUC:" + trainAUC);

        OutputDataSet.outputDataSet(trainDataSet, "/home/cellargalaxy", "gbk", ",", OutputDataSet.TRAIN,
                DSMethod, featureSelections, subSpaces, hereditarys, trainAUC);

        ////////////////////////////////////////////////////////////////////////////////////////////////

        DataSet testDataSet = new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testAll.csv"),
                ",", 0, 2, 3, 1, 5);

        LinkedList<DataSet> testDataSets = new LinkedList<DataSet>();
        Iterator<LinkedList<Integer>> iteratorSubSpace = subSpaces.iterator();
        Iterator<Hereditary> hereditaryIterator = hereditarys.iterator();
        while (iteratorSubSpace.hasNext()) {
            DataSet dataSet = CloneObject.clone(testDataSet);
            LinkedList<Integer> subSpace = iteratorSubSpace.next();
            Hereditary hereditary = hereditaryIterator.next();
            dataSet.allSaveEvidence(subSpace);
            dataSet.mulChro(hereditary.getMaxChro());
            testDataSets.add(dataSet);
            hereditary.setMaxAUC(AUC.countAUC(dataSet, DSMethod));
            System.out.println("测试集子空间进化AUC:" + hereditary.getMaxAUC());
        }

        TestSetTest.DSSubSpace(TestSetTest.AVER_SYNTHESIS, DSMethod, testDataSets, testDataSet, 0, 0, 0, 0);

        double testAUC = AUC.countAUC(testDataSet, Id.TEST_DS_METHOD);
        System.out.println("测试集TestAUC:" + testAUC);

        OutputDataSet.outputDataSet(testDataSet, "/home/cellargalaxy", "gbk", ",", OutputDataSet.TEST,
                DSMethod, featureSelections, subSpaces, hereditarys, testAUC);
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------

    private static void testRun(Parameter parameter, File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol, String savePath, String coding,
                                HereditaryParameter hereditaryParameter, double stop1, double stop2, int featureSeparationMethodNum,
                                double separationValue, int evolutionMethodNum, int evolutionCount,
                                int adjustMethodNum, double d,
                                int resultSynthesisMethodNum, double thrf, double thrnf, double d1, double d2) throws IOException, ClassNotFoundException {
        SubDataSet subDataSet = new SubDataSet(dataSetFile, separator, idClo, ACol, BCol, evidCol, labelCol);
        parameter.receiveCreateSubDataSet(subDataSet.getCom0Count(), subDataSet.getCom1Count(), subDataSet.getMiss0Count(), subDataSet.getMiss1Count());
        DataSet[] subDataSets = subDataSet.createSubDataSet(parameter.getTest(), parameter.getMiss(), parameter.getLabel1());
        double dsAUC = AUC.countAUC(subDataSets[0], Id.DS_METHOD);
        double myDSAUC = AUC.countAUC(subDataSets[0], Id.MY_DS_METHOD);
        double myDSAUC2 = AUC.countAUC(subDataSets[0], Id.MY_DS_METHOD2);
        System.out.println("dsAUC:" + dsAUC + " myDSAUC:" + myDSAUC + " myDSAUC2:" + myDSAUC2);
        int DSMethod;
        if (dsAUC > myDSAUC) {
            if (dsAUC > myDSAUC2) {
                DSMethod = Id.DS_METHOD;
                System.out.println("训练集原始AUC:" + dsAUC);
            } else {
                DSMethod = Id.MY_DS_METHOD2;
                System.out.println("训练集原始AUC:" + myDSAUC2);
            }
        } else {
            if (myDSAUC > myDSAUC2) {
                DSMethod = Id.MY_DS_METHOD;
                System.out.println("训练集原始AUC:" + myDSAUC);
            } else {
                DSMethod = Id.MY_DS_METHOD2;
                System.out.println("训练集原始AUC:" + myDSAUC2);
            }
        }
        System.out.println("----------------------------------");

        double[][] featureSelections = FeatureSelection.featureSelection(hereditaryParameter, DSMethod, subDataSets[0],
                stop1, stop2, featureSeparationMethodNum, separationValue, evolutionMethodNum, evolutionCount);
        System.out.println("特征选择");
        for (double[] featureSelection : featureSelections) {
            System.out.println(Arrays.toString(featureSelection));
        }

        LinkedList<Integer> features = new LinkedList<Integer>();
        double[] impros = new double[featureSelections.length];
        for (int i = 0; i < featureSelections.length; i++) {
            features.add((int) featureSelections[i][0]);
            impros[i] = featureSelections[i][1];
        }

        DataSet trainDataSet1 = CloneObject.clone(subDataSets[0]);
        trainDataSet1.allSaveEvidence(features);
        double featureAUC = AUC.countAUC(trainDataSet1, DSMethod);
        System.out.println("特征选择训练集AUC:" + featureAUC);
        System.out.println("----------------------------------");

        parameter.receiveCreateSubSpaces(featureSelections);
        LinkedList<LinkedList<Integer>> subSpaces = SubSpace.createSubSpaces(features, impros, parameter.getSn(), parameter.getFnMin(), adjustMethodNum, d);
        System.out.println("子空间:");
        for (LinkedList<Integer> subSpace : subSpaces) {
            System.out.println(subSpace);
        }

        LinkedList<DataSet> trainDataSets = new LinkedList<>();
        LinkedList<Hereditary> hereditarys = new LinkedList<>();
        Iterator<LinkedList<Integer>> iterator = subSpaces.iterator();
        while (iterator.hasNext()) {
            LinkedList<Integer> subSpace = iterator.next();
            DataSet dataSet = CloneObject.clone(subDataSets[0]);
            dataSet.allSaveEvidence(subSpace);
            Hereditary hereditary = Hereditary.superEvolution(hereditaryParameter, DSMethod, dataSet, evolutionMethodNum, evolutionCount);
            if (hereditary.getMaxAUC() >= featureAUC) {
                trainDataSets.add(dataSet);
                hereditarys.add(hereditary);
                System.out.println("子空间训练集进化AUC:" + hereditary.getMaxAUC());
            } else {
                iterator.remove();
            }
        }
        if (subSpaces.size() == 0) {
            LinkedList<Integer> subSpace = subDataSets[0].getEvidenceNums();
            DataSet dataSet = CloneObject.clone(subDataSets[0]);
            Hereditary hereditary = Hereditary.superEvolution(hereditaryParameter, DSMethod, dataSet, evolutionMethodNum, evolutionCount);
            trainDataSets.add(dataSet);
            subSpaces.add(subSpace);
            hereditarys.add(hereditary);
            System.out.println("全子空间训练集进化AUC:" + hereditary.getMaxAUC());
        }
        TestSetTest.DSSubSpace(resultSynthesisMethodNum, DSMethod, trainDataSets, subDataSets[0], thrf, thrnf, d1, d2);

        double trainAUC = AUC.countAUC(subDataSets[0], Id.TEST_DS_METHOD);
        System.out.println("训练集TrainAUC:" + trainAUC);

        OutputDataSet.outputDataSet(subDataSets[0], savePath, coding, separator, OutputDataSet.TRAIN,
                DSMethod, featureSelections, subSpaces, hereditarys, trainAUC);

        ////////////////////////////////////////////////////////////////////////////////////////////////

        LinkedList<DataSet> testDataSets = new LinkedList<DataSet>();
        Iterator<LinkedList<Integer>> iteratorSubSpace = subSpaces.iterator();
        Iterator<Hereditary> hereditaryIterator = hereditarys.iterator();
        while (iteratorSubSpace.hasNext()) {
            DataSet dataSet = CloneObject.clone(subDataSets[1]);
            LinkedList<Integer> subSpace = iteratorSubSpace.next();
            Hereditary hereditary = hereditaryIterator.next();
            dataSet.allSaveEvidence(subSpace);
            dataSet.mulChro(hereditary.getMaxChro());
            testDataSets.add(dataSet);
            hereditary.setMaxAUC(AUC.countAUC(dataSet, DSMethod));
            System.out.println("测试集子空间进化AUC:" + hereditary.getMaxAUC());
        }

        TestSetTest.DSSubSpace(resultSynthesisMethodNum, DSMethod, testDataSets, subDataSets[1], thrf, thrnf, d1, d2);

        double testAUC = AUC.countAUC(subDataSets[1], Id.TEST_DS_METHOD);
        System.out.println("测试集TestAUC:" + testAUC);

        OutputDataSet.outputDataSet(subDataSets[1], savePath, coding, separator, OutputDataSet.TEST,
                DSMethod, featureSelections, subSpaces, hereditarys, testAUC);
    }

//	public static void main(String[] args) throws IOException {
//		File trainSet=new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testSet.csv");
//		File trainLabel=new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testLabel.csv");
//		File trainAll=new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testAll.csv");
//
//		BufferedReader trainSetRead=new BufferedReader(new FileReader(trainSet));
//		BufferedReader trainLabelRead=new BufferedReader(new FileReader(trainLabel));
//		BufferedWriter trainAllWriter=new BufferedWriter(new FileWriter(trainAll));
//		String id=null;
//		String label=null;
//		String string;
//		while ((string = trainSetRead.readLine()) != null) {
//			String[] strings=string.split(",");
//			if (!strings[0].equals(id)) {
//				id=strings[0];
//				String s=trainLabelRead.readLine();
//				String[] ss=s.split(",");
//				label=ss[1];
//			}
//			trainAllWriter.write(string+","+label+"\r\n");
//			trainAllWriter.flush();
//		}
//		trainSetRead.close();
//		trainLabelRead.close();
//		trainAllWriter.close();
//	}

}
