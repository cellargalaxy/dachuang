package feature;

import dataSet.DataSet;
import hereditary.Roulette;
import util.CloneObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import static feature.FeatureSelection.MEDIAN_MODEL;

/**
 * Created by cellargalaxy on 2017/6/8.
 * 特征子空间静态类
 */
public class SubSpace {
    public static final int POWER_ADJUST = 1;
    public static final int SUBTRACTION_ADJUST = 2;

//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        DataSet dataSet = new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/特征选择 - 副本.csv"), ",", 0, 2, 3, 5, 6);
//        FeatureSelection featureSelection = new FeatureSelection(MEDIAN_MODEL);
//        double[][] improFeature = featureSelection.featureSelection(dataSet);
//        System.out.println("重要特征：");
//        for (double[] doubles : improFeature) {
//            System.out.println(Arrays.toString(doubles));
//        }
//        int[] sn = {1, 2};
//        LinkedList<LinkedList<Integer>> subSpaces = createSubSpaces(improFeature, sn,1,POWER_ADJUST, 0.5);
//
//        System.out.println("生成子空间：");
//        for (LinkedList<Integer> subSpace : subSpaces) {
//            System.out.println(subSpace);
//        }
//    }

    public static LinkedList<LinkedList<Integer>> createSubSpaces(double[][] improFeature, int[] sn, int fnMin, int adjustMethodNum, double d) throws IOException, ClassNotFoundException {
        int fnMax=(int)Math.pow(2, improFeature.length);
        int fn=fnMin+(int)(Math.random()*(fnMax-fnMin));
        fn=15;

        improFeature = adjust(adjustMethodNum, improFeature, d);

        LinkedList<Integer> features=new LinkedList<Integer>();
        LinkedList<Double> impros=new LinkedList<Double>();
        for (double[] doubles : improFeature) {
            features.add((int)doubles[0]);
            impros.add(doubles[1]);
        }
        LinkedList<LinkedList<Integer>> subSpaces=new LinkedList<LinkedList<Integer>>();
        for (int i = 0; i < fn; i++) {
            LinkedList<Integer> subSpace=createSubSpace(features,impros,sn[(int)(sn.length*Math.random())]);
            if (!yetContainSubSpace(subSpaces,subSpace)) {
                subSpaces.add(subSpace);
            }else {
                i--;
            }
        }
        return subSpaces;
    }

    private static LinkedList<Integer> createSubSpace(LinkedList<Integer> oldFeatures,LinkedList<Double> oldImpros,int len) throws IOException, ClassNotFoundException {
        LinkedList<Integer> newFeatures= CloneObject.clone(oldFeatures);
        LinkedList<Double> newImpros=CloneObject.clone(oldImpros);
        LinkedList<Integer> subSpace=new LinkedList<Integer>();
        for (int i = 0; i < len; i++) {
            int point= Roulette.roulette(newImpros);
            subSpace.add(newFeatures.get(point));
            newFeatures.remove(point);
            newImpros.remove(point);
        }
        return subSpace;
    }
    private static boolean yetContainSubSpace(LinkedList<LinkedList<Integer>> subSpaces,LinkedList<Integer> features){
        main:for (LinkedList<Integer> subSpace : subSpaces) {
            if (subSpace.size()==features.size()) {
                for (Integer feature : features) {
                    if (!subSpace.contains(feature)) {
                        continue main;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 调整Imp
     *
     * @param adjustMethodNum
     * @param improFeature
     * @param d 如果是选择平方调整，此参数无效
     * @return
     */
    private static double[][] adjust(int adjustMethodNum, double[][] improFeature, double d) {
        if (adjustMethodNum == POWER_ADJUST) {
            return powerAdjust(improFeature);
        } else if (adjustMethodNum == SUBTRACTION_ADJUST) {
            return subtractionAdjust(improFeature, d);
        } else {
            throw new RuntimeException("Imp调整方法编号错误：" + adjustMethodNum);
        }
    }

    /**
     * 平方调整Imp
     *
     * @param improFeature
     * @return
     */
    private static double[][] powerAdjust(double[][] improFeature) {
        for (int i = 0; i < improFeature.length; i++) {
            improFeature[i][1] *= improFeature[i][1];
        }
        return improFeature;
    }

    /**
     * 减法调整Imp
     *
     * @param improFeature
     * @param d
     * @return
     */
    private static double[][] subtractionAdjust(double[][] improFeature, double d) {
        double minImp = Double.MIN_VALUE;
        for (double[] doubles : improFeature) {
            if (minImp < doubles[1]) {
                minImp = doubles[1];
            }
        }
        minImp = Math.abs(minImp);
        for (int i = 0; i < improFeature.length; i++) {
            improFeature[i][1] = Math.abs(improFeature[i][1]) - (minImp - d);
        }
        return improFeature;
    }
}
