package feature;

import dataSet.DataSet;
import hereditary.Hereditary;
import hereditary.HereditaryParameter;
import util.CloneObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 2017/5/13.
 * 特征选择算法静态类
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

    /**
     * 返回的double[][] ds=new double[][2];
     * 其中ds[i]={m,Imp}
     *
     * @param dataSet
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static double[][] featureSelection(HereditaryParameter hereditaryParameter, int DSMethodNum,
                                              DataSet dataSet, double stop1, double stop2, int featureSeparationMethodNum, double separationValue,
                                              int evolutionMethodNum, int evolutionCount) throws IOException, ClassNotFoundException {
        double aucFull = Hereditary.superEvolutionAUC(hereditaryParameter, DSMethodNum, dataSet, evolutionMethodNum, evolutionCount);

        TreeMap<Double, Integer> aucImprotences = new TreeMap<Double, Integer>();
        for (Integer integer : dataSet.getEvidenceNums()) {
            //这样减就负的越多越好，好的在前面
            double auc = Hereditary.superEvolutionAUC(hereditaryParameter, DSMethodNum, dataSet, integer, evolutionMethodNum, evolutionCount) - aucFull;
            aucImprotences.put(auc, integer);
        }

        LinkedList<Integer> imroEvid = new LinkedList<Integer>();
        LinkedList<Integer> unInproEvid = new LinkedList<Integer>();
        separation(aucImprotences, imroEvid, unInproEvid, featureSeparationMethodNum, separationValue);

        double auc = Hereditary.superEvolutionAUC(hereditaryParameter, DSMethodNum, dataSet, imroEvid, evolutionMethodNum, evolutionCount);
        while (Math.abs(aucFull - auc) * stop1 > stop2 && unInproEvid.size() > 0) {
            double aucJ = -1;
            int evidenceNum = -1;
            for (Integer integer : unInproEvid) {
                LinkedList<Integer> newImroEvid = CloneObject.clone(imroEvid);
                newImroEvid.add(integer);
                double d = Hereditary.superEvolutionAUC(hereditaryParameter, DSMethodNum, dataSet, newImroEvid, evolutionMethodNum, evolutionCount);
                if (d > aucJ) {
                    aucJ = d;
                    evidenceNum = integer;
                }
            }

            imroEvid.add(evidenceNum);
            unInproEvid.removeFirstOccurrence(evidenceNum);
            auc = aucJ;
        }

        double[][] ds = new double[imroEvid.size()][2];
        int i = 0;
        for (Map.Entry<Double, Integer> entry : aucImprotences.entrySet()) {
            if (imroEvid.contains(entry.getValue())) {
                ds[i][0] = entry.getValue();
                ds[i][1] = entry.getKey();
                i++;
            }
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
    private static void separation(TreeMap<Double, Integer> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unInproEvid, int methodNum, double customizeSeparation) {
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
        customizeSeparation(aucImprotences, imroEvid, unInproEvid, avg);
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
    }
}
