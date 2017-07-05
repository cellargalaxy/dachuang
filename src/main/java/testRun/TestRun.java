package testRun;

import auc.AUC;
import dataSet.DataSet;
import dataSet.Id;
import feature.FeatureSelection;
import feature.SubSpace;
import hereditary.Hereditary;
import util.CloneObject;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

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
        double[][] improFeature = FeatureSelection.featureSelection(trainDataSet,0.95,0.01,MEDIAN_MODEL,0.5);
        System.out.println("特征选择答案：");
        for (double[] doubles : improFeature) {
            System.out.println(doubles[0] + ":" + doubles[1]);
        }

        //特征子空间,111111111111111
        int[] sn = {3,4,5};
        LinkedList<LinkedList<Integer>> subSpaces = SubSpace.createSubSpaces(improFeature, sn,10,SubSpace.POWER_ADJUST, 0.5);
        System.out.println("生成子空间：");
        for (LinkedList<Integer> subSpace : subSpaces) {
            System.out.println(subSpace);
        }

        ///////////////////////////////////////////////////////////////////////////////////

        //生成特征testSet
        DataSet testDataSet=new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/testAll.csv"),
                ",",0,2,3,1,5);

        System.out.println("testDataSetAUC:"+AUC.countAUCWithout(testDataSet,-1));

        LinkedList<Integer> features=new LinkedList<Integer>();
        for (double[] doubles : improFeature) {
            features.add((int)doubles[0]);
        }
        testDataSet.allSaveEvidence(features);

        //生成子空间testSets
        DataSet[] testDataSets=new DataSet[subSpaces.size()];
        int i=0;
        for (LinkedList<Integer> subSpace : subSpaces) {
            DataSet dataSet=CloneObject.clone(testDataSet);
            dataSet.allSaveEvidence(subSpace);
            testDataSets[i]=dataSet;
            i++;
        }

        for (int j = 0; j < testDataSets.length; j++) {
            //子空间testSets遗传算法
            Hereditary hereditary = new Hereditary(testDataSets[j]);
            hereditary.evolution(-1, Hereditary.USE_Roulette);
            System.out.println("=========================");
            System.out.println("maxAUC:" + hereditary.getMaxAUC());
            System.out.println("maxChro:" + Arrays.toString(hereditary.getMaxChro()));

//            System.out.println("trainDataSetAUC:"+AUC.countAUCWithout(trainDataSet,-1));
//            trainDataSet.allSaveEvidence(features);
//            System.out.println("特征trainDataSetAUC:"+AUC.countAUCWithout(trainDataSet,-1));
//            System.out.println("特征testDataSetAUC:"+AUC.countAUCWithout(testDataSet,-1));
//            System.out.println("AUC1:"+ AUC.countAUCWithout(testDataSets[0],-1));

            //进化testSets
            testDataSets[j].mulChro(hereditary.getMaxChro());
            System.out.println("进化testSetAUC:"+AUC.countAUCWithout(testDataSets[j],-1));
        }

        Iterator<Id>[] iterators=new Iterator[testDataSets.length];
        for (int j = 0; j < testDataSets.length; j++) {
            iterators[j]=testDataSets[j].getIds().iterator();
        }
        Iterator<Id> iter=testDataSet.getIds().iterator();
        while (iterators[0].hasNext()) {
            double countA=0;
            double countB=0;
            for (Iterator<Id> iterator : iterators) {
                Id id=iterator.next();
                id.setSubSpaceDS(id.countDSWithout(-1));
                countA+=id.getSubSpaceDS()[1];
                countB+=id.getSubSpaceDS()[2];
            }
            double[] ds={0,countA/iterators.length,countB/iterators.length};
            Id id=iter.next();
            id.setSubSpaceDS(ds);
        }

        System.out.println(AUC.countAUCTestSet(testDataSet));



//        System.out.println("AUC2:"+ AUC.countAUCWithout(testDataSets[0],-1));
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
