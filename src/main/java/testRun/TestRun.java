package testRun;

import auc.AUC;
import dataSet.DataSet;
import dataSet.Id;
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
public class TestRun {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/trainAll.csv"),
                ",", 0, 2, 3, 1, 5);
        double dsAUC = AUC.countAUC(trainDataSet, Id.DS_METHOD);
        double myDSAUC = AUC.countAUC(trainDataSet, Id.MY_DS_METHOD);
        int DSMethod;
        if (dsAUC > myDSAUC) {
            DSMethod = Id.DS_METHOD;
            System.out.println("训练集原始AUC:" + dsAUC);
        } else {
            DSMethod = Id.MY_DS_METHOD;
            System.out.println("训练集原始AUC:" + myDSAUC);
        }
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

        int[] sn = {1, 2, 3, 4, 5};
        LinkedList<LinkedList<Integer>> subSpaces = SubSpace.createSubSpaces(features, impros, sn, 20, SubSpace.POWER_ADJUST, 0);
        System.out.println("子空间:");
        for (LinkedList<Integer> subSpace : subSpaces) {
            System.out.println(subSpace);
        }

        LinkedList<DataSet> trainDataSets = new LinkedList<>();
        LinkedList<double[]> chros = new LinkedList<>();
        Iterator<LinkedList<Integer>> iterator = subSpaces.iterator();
        while (iterator.hasNext()) {
            LinkedList<Integer> subSpace = iterator.next();
            DataSet dataSet = CloneObject.clone(trainDataSet);
            dataSet.allSaveEvidence(subSpace);
            Hereditary hereditary = Hereditary.superEvolution(new HereditaryParameter(), DSMethod, dataSet, HereditaryParameter.USE_ORDER, 5);
            if (hereditary.getMaxAUC() >= featureAUC) {
                trainDataSets.add(dataSet);
                chros.add(hereditary.getMaxChro());
                System.out.println("子空间训练集进化AUC:" + hereditary.getMaxAUC());
            } else {
                iterator.remove();
            }
        }
        TestSetTest.DSSubSpace(TestSetTest.AVER_SYNTHESIS, DSMethod, trainDataSets, trainDataSet, 0, 0, 0, 0);

        System.out.println("训练集TestAUC:" + AUC.countAUC(trainDataSet, Id.TEST_DS_METHOD));

        ////////////////////////////////////////////////////////////////////////////////////////////////

        DataSet testDataSet = new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testAll.csv"),
                ",", 0, 2, 3, 1, 5);

        LinkedList<DataSet> testDataSets = new LinkedList<DataSet>();
        Iterator<LinkedList<Integer>> iteratorSubSpace = subSpaces.iterator();
        Iterator<double[]> iteratorChros = chros.iterator();
        while (iteratorSubSpace.hasNext() && iteratorChros.hasNext()) {
            DataSet dataSet = CloneObject.clone(testDataSet);
            LinkedList<Integer> subSpace = iteratorSubSpace.next();
            double[] chro = iteratorChros.next();
            dataSet.allSaveEvidence(subSpace);
            dataSet.mulChro(chro);
            testDataSets.add(dataSet);
            System.out.println("测试集子空间进化AUC:" + AUC.countAUC(dataSet, DSMethod));
        }

        TestSetTest.DSSubSpace(TestSetTest.AVER_SYNTHESIS, DSMethod, testDataSets, testDataSet, 0, 0, 0, 0);
        System.out.println("测试集TestAUC:" + AUC.countAUC(testDataSet, Id.TEST_DS_METHOD));
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
