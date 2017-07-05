package feature;

import auc.AUC;
import dataSet.DataSet;
import util.CloneObject;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cellargalaxy on 2017/5/13.
 * 特征选择算法对象类
 * stop1 停止条件1 默认是0.95
 * stop2 停止条件2 默认是0.01
 * methodNum 用户所选择的核特征条件算法
 * customizeSeparation 用户自定义核特征条件值
 */
public class FeatureSelection {
    //中位数
    public static final int MEDIAN_MODEL = 1;
    //平均数
    public static final int AVERAGE_MODEL = 2;
    //用户输入
    private static final int CUSTOMIZE_MODEL = 3;

//    private double stop1 = 0.95;
//    private double stop2 = 0.01;
//    private int methodNum;
//    private double customizeSeparation;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DataSet trainDataSet=new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/trainAll.csv"),
                ",",0,2,3,1,5);
        System.out.println("特征选择前AUC:"+AUC.countAUCWithout(trainDataSet,-1));

        double[][] ds = FeatureSelection.featureSelection(trainDataSet,0.95,0.01,MEDIAN_MODEL,0.5);
        LinkedList<Integer> improFeatureNums=new LinkedList<>();
        System.out.println("答案：");
        for (double[] doubles : ds) {
            System.out.println(doubles[0] + ":" + doubles[1]);
            improFeatureNums.add((int)doubles[0]);
        }
        System.out.println("特征选择后的AUC:"+AUC.countAUCWith(trainDataSet,improFeatureNums));
    }

//    public FeatureSelection(double stop1, double stop2, double customizeSeparation) {
//        this.stop1 = stop1;
//        this.stop2 = stop2;
//        this.customizeSeparation = customizeSeparation;
//        this.methodNum = CUSTOMIZE_MODEL;
//    }
//
//    public FeatureSelection(double stop1, double stop2, int methodNum) {
//        this.stop1 = stop1;
//        this.stop2 = stop2;
//        this.methodNum = methodNum;
//    }
//
//    public FeatureSelection(double customizeSeparation) {
//        this.customizeSeparation = customizeSeparation;
//        this.methodNum = CUSTOMIZE_MODEL;
//    }
//
//    public FeatureSelection(int methodNum) {
//        this.methodNum = methodNum;
//    }

    /**
     * 返回的double[][] ds=new double[][2];
     * 其中ds[i]={m,Imp}
     *
     * @param dataSet
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static double[][] featureSelection(DataSet dataSet,double stop1,double stop2,int methodNum,double separation) throws IOException, ClassNotFoundException {
        double aucFull = AUC.countAUCWithout(dataSet, -1);
        TreeMap<Double, Integer> aucImprotences = new TreeMap<Double, Integer>();
        for (int m = 0; m < dataSet.getEvidenceCount(); m++) {
            //这样减就负的越多越好，好的在前面
            double auc = AUC.countAUCWithout(dataSet, m+1) - aucFull;
            aucImprotences.put(auc, m+1);
        }

//		System.out.println("各个特征的重要程度：");
//		for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
//			System.out.println(entry.getValue()+":"+entry.getKey());
//		}
//		System.out.println("----------------------------");

        LinkedList<Integer> imroEvid = new LinkedList<Integer>();
        LinkedList<Integer> unInproEvid = new LinkedList<Integer>();
        separation(aucImprotences, imroEvid, unInproEvid,methodNum,separation);

        double auc = AUC.countAUCWithouts(dataSet, unInproEvid);
        while ((aucFull-auc) * stop1 > stop2) {
            Map<Double, Integer> aucJ = new TreeMap<Double, Integer>(new Comparator<Double>() {
                public int compare(Double a, Double b) {
                    if (a > b) {
                        return -1;
                    } else if (a < b) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            for (int i = 0; i < unInproEvid.size(); i++) {
                LinkedList<Integer> newUnInproEvid = CloneObject.clone(unInproEvid);
                Integer m = newUnInproEvid.remove(i);
                aucJ.put(AUC.countAUCWithouts(dataSet, newUnInproEvid), m);
            }

            for (Map.Entry<Double, Integer> entry : aucJ.entrySet()) {
//                System.out.println("添加回特征:"+entry.getValue()+",AUC:"+entry.getKey());

                imroEvid.add(entry.getValue());
                unInproEvid.remove(entry.getValue());
                auc = entry.getKey();
                break;
            }
        }

//		System.out.println("保留的重要特征：");
//		System.out.println(imroEvid);
//		System.out.println("aucFull："+aucFull);
//		System.out.println("aucGood："+auc);

        double[][] ds = new double[imroEvid.size()][2];
        int i = 0;
        main:
        for (Integer m : imroEvid) {
            for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
                if (entry.getValue().equals(m)) {
                    ds[i][0] = m;
                    ds[i][1] = entry.getKey();
                    i++;
                    continue main;
                }
            }
            throw new RuntimeException("特征异常丢失");
        }
        return ds;
    }

    /**
     * 分离重要特征与非重要特征
     *
     * @param aucImprotences
     * @param imroEvid
     * @param unInproEvid
     */
    private static void separation(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unInproEvid,int methodNum,double customizeSeparation) {
        if (methodNum == MEDIAN_MODEL) {
            medianSeparation(aucImprotences, imroEvid, unInproEvid);
        } else if (methodNum == AVERAGE_MODEL) {
            averageSeparation(aucImprotences, imroEvid, unInproEvid);
        } else if (methodNum == CUSTOMIZE_MODEL) {
            customizeSeparation(aucImprotences, imroEvid, unInproEvid, customizeSeparation);
        } else {
            throw new RuntimeException("核特征选择方法代号有误");
        }
    }

    /**
     * 用中位数分离重要特征与非重要特征
     *
     * @param aucImprotences
     * @param imroEvid
     * @param unInproEvid
     */
    private static void medianSeparation(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unInproEvid) {
        int point = aucImprotences.size() / 2;
        int i = 0;
        for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
            if (i <= point) {
                imroEvid.add(entry.getValue());
            } else {
                unInproEvid.add(entry.getValue());
            }
            i++;
        }
//		System.out.println("重要特征：");
//		System.out.println(imroEvid);
//		System.out.println("次要特征：");
//		System.out.println(unInproEvid);
    }

    /**
     * 用平均数分离重要特征与非重要特征
     *
     * @param aucImprotences
     * @param imroEvid
     * @param unInproEvid
     */
    private static void averageSeparation(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unInproEvid) {
        double count = 0;
        for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
            count += entry.getKey();
        }
        double avg = count / aucImprotences.size();
        customizeSeparation(aucImprotences, imroEvid, unInproEvid,avg);
//		System.out.println("重要特征：");
//		System.out.println(imroEvid);
//		System.out.println("次要特征：");
//		System.out.println(unInproEvid);
    }

    /**
     * 用某个值分离重要特征与非重要特征
     *
     * @param aucImprotences
     * @param separation
     * @param imroEvid
     * @param unInproEvid
     */
    private static void customizeSeparation(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unInproEvid, double separation) {
        for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
            if (entry.getKey() <= separation) {
                imroEvid.add(entry.getValue());
            } else {
                unInproEvid.add(entry.getValue());
            }
        }
//		System.out.println("重要特征：");
//		System.out.println(imroEvid);
//		System.out.println("次要特征：");
//		System.out.println(unInproEvid);
    }
}
