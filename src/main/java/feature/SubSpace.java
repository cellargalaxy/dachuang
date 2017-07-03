package feature;

import dataSet.DataSet;
import hereditary.Roulette;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static feature.FeatureSelection.MEDIAN_MODEL;

/**
 * Created by cellargalaxy on 2017/6/8.
 * 特征子空间静态类
 */
public class SubSpace {
    public static final int POWER_ADJUST = 1;
    public static final int SUBTRACTION_ADJUST = 2;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DataSet dataSet = new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/特征选择 - 副本.csv"), ",", 0, 2, 3, 5, 6);
        FeatureSelection featureSelection = new FeatureSelection(MEDIAN_MODEL);
        double[][] improFeature = featureSelection.featureSelection(dataSet);
        int[] sn = {1, 2, 3};
        int[][] subSpaces = createSubSpace(improFeature, POWER_ADJUST, 0.5, sn);

        System.out.println("生成子空间：");
        for (int[] subSpace : subSpaces) {
            System.out.println(Arrays.toString(subSpace));
        }
    }

    public static int[][] createSubSpace(double[][] improFeature, int adjustMethodNum, double d, int[] sn) {
        for (int i : sn) {
            if (i > improFeature.length) {
                throw new RuntimeException("sn的取值：" + i + "不得大于重要特征数：" + improFeature.length);
            }
        }
        int fn = (int) Math.pow(2, improFeature.length);
        int[][] subSpaces = new int[fn][];

        improFeature = adjust(adjustMethodNum, improFeature, d);

        double impCount = 0;
        double[] imps = new double[improFeature.length];
        for (int i = 0; i < improFeature.length; i++) {
            imps[i] = improFeature[i][1];
            impCount += improFeature[i][1];
        }

        for (int i = 0; i < subSpaces.length; i++) {
            int num = sn[(int) (Math.random() * sn.length)];
            int[] features = new int[num];
            for (int j = 0; j < features.length; j++) {
                features[j] = -1;
            }
            for (int j = 0; j < features.length; j++) {
                int point;
                boolean b;
                do {
                    b = false;
                    point = (int) improFeature[Roulette.roulette(imps, impCount)][0];
                    for (int feature : features) {
                        if (feature == point) {
                            b = true;
                            break;
                        }
                    }
                } while (b);
                features[j] = point;
            }
            subSpaces[i] = features;
        }
        return subSpaces;
    }

    /**
     * 调整Imp
     *
     * @param adjustMethodNum
     * @param improFeature
     * @param d               如果是选择平方调整，此参数无效
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
