package testRun;

import auc.AUC;
import dataSet.DataSet;
import dataSet.Id;
import feature.FeatureSelection;
import feature.SubSpace;
import feature.TestSetTest;
import hereditary.Hereditary;
import util.CloneObject;

import java.io.*;
import java.util.Arrays;

import static feature.FeatureSelection.MEDIAN_MODEL;

/**
 * Created by cellargalaxy on 17-7-3.
 * 用于测试整个体系的静态类
 */
public class TestRun {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DataSet trainDataSet=new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/trainAll.csv"),
                ",",0,2,3,1,5);

        //特征选择
        FeatureSelection featureSelection = new FeatureSelection(MEDIAN_MODEL);
        double[][] improFeature = featureSelection.featureSelection(trainDataSet);
        System.out.println("特征选择答案：");
        for (double[] doubles : improFeature) {
            System.out.println(doubles[0] + ":" + doubles[1]);
        }

        //特征子空间
        int[] sn = {1, 2};
        int[][] subSpaces = SubSpace.createSubSpace(improFeature, sn,3,SubSpace.POWER_ADJUST, 0.5);
        System.out.println("生成子空间：");
        for (int[] subSpace : subSpaces) {
            System.out.println(Arrays.toString(subSpace));
        }

        //生成子空间testSets
        DataSet testDataSet=new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testAll.csv"),
                ",",0,2,3,1,5);
        DataSet[] testDataSets=new DataSet[subSpaces.length];
        for (int i = 0; i < testDataSets.length; i++) {
            DataSet dataSet=CloneObject.clone(testDataSet);
            dataSet.saveEvidence(subSpaces[i]);
            testDataSets[i]=dataSet;
        }

        //子空间各进化算法
        double[][] maxChros=new double[testDataSets.length][];
        for (int i = 0; i < maxChros.length; i++) {
            System.out.println("计算子空间："+Arrays.toString(subSpaces[i])+"进化算法");
            Hereditary hereditary = new Hereditary(testDataSets[i]);
            hereditary.evolution(-1, Hereditary.USE_Roulette);
            System.out.println("=========================");
            System.out.println("maxAUC:" + hereditary.getMaxAUC());
            System.out.println("maxChro:" + Arrays.toString(hereditary.getMaxChro()));
        }




        //testSet乘以染色体
        DataSet testDataSet1= CloneObject.clone(testDataSet);
        testDataSet1.removeMissingId();
        testDataSet1.mulChro(hereditary.getMaxChro());

        //对testSet进行测试
        testDataSet1=TestSetTest.testSetTest(testDataSet1,subSpaces,TestSetTest.AVER_SYNTHESIS,0,0,0,0);
        System.out.println("推理合成结果：");
        for (Id id : testDataSet1.getIds()) {
            System.out.println(Arrays.toString(id.getSubSpaceDS()));
        }

        System.out.println("AUC:"+AUC.countTestSetAUC(testDataSet1));
    }



//    public static void main(String[] args) throws IOException {
//        File trainSet=new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testSet.csv");
//        File trainLabel=new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testLabel.csv");
//        File trainAll=new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testAll.csv");
//
//        BufferedReader trainSetRead=new BufferedReader(new FileReader(trainSet));
//        BufferedReader trainLabelRead=new BufferedReader(new FileReader(trainLabel));
//        BufferedWriter trainAllWriter=new BufferedWriter(new FileWriter(trainAll));
//        String id=null;
//        String label=null;
//        String string;
//        while ((string = trainSetRead.readLine()) != null) {
//            String[] strings=string.split(",");
//            if (!strings[0].equals(id)) {
//                id=strings[0];
//                String s=trainLabelRead.readLine();
//                String[] ss=s.split(",");
//                label=ss[1];
//            }
//            trainAllWriter.write(string+","+label+"\r\n");
//            trainAllWriter.flush();
//        }
//        trainSetRead.close();
//        trainLabelRead.close();
//        trainAllWriter.close();
//    }

}
