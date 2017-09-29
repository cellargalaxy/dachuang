//package version1.feature;
//
//import util.Roulette;
//import util.CloneObject;
//
//import java.io.IOException;
//import java.util.LinkedList;
//
///**
// * Created by cellargalaxy on 2017/6/8.
// * 特征子空间静态类
// */
//public class SubSpace {
//    public static final int POWER_ADJUST = 1;
//    public static final int SUBTRACTION_ADJUST = 2;
//
//    public static LinkedList<LinkedList<Integer>> createAllSubSpaces(int[] featureNums) {
//        LinkedList<LinkedList<Integer>> featureSubSpaces = new LinkedList<LinkedList<Integer>>();
//        int len = (int) (Math.pow(2, featureNums.length));
//        for (int i = 1; i < len; i++) {
//            LinkedList<Integer> featureSubSpace = new LinkedList<Integer>();
//            char[] chars = Integer.toBinaryString(i).toCharArray();
//            for (int j = 0; j < chars.length && j < featureNums.length; j++) {
//                if (chars[chars.length - j - 1] == '1') {
//                    featureSubSpace.add(featureNums[featureNums.length - j - 1]);
//                }
//            }
//            featureSubSpaces.add(featureSubSpace);
//        }
//        return featureSubSpaces;
//    }
//
//
//    public static LinkedList<LinkedList<Integer>> createSubSpaces(LinkedList<Integer> features, double[] impros, int[] sn, int fnMin, int adjustMethodNum, double d) throws IOException, ClassNotFoundException {
//        int fnMax = 0;
//        for (int i : sn) {
//            fnMax += countC(i, features.size());
//        }
//        if (fnMin > fnMax) {
//            throw new RuntimeException("最小子空间数fnMin:" + fnMin + ",不得大于最大子空间数fnMax:" + fnMax);
//        }
//        int fn = fnMin + (int) (Math.random() * (fnMax - fnMin));
//
//        LinkedList<Double> listImpros = adjust(adjustMethodNum, impros, d);
//
//        LinkedList<LinkedList<Integer>> subSpaces = new LinkedList<LinkedList<Integer>>();
//        for (int i = 0; i < fn; i++) {
//            LinkedList<Integer> subSpace = createSubSpace(features, listImpros, sn[(int) (sn.length * Math.random())]);
//            if (!siContainSubSpace(subSpaces, subSpace)) {
//                subSpaces.add(subSpace);
//            } else {
//                i--;
//            }
//        }
//        return subSpaces;
//    }
//
//    private static int countC(int n, int m) {
//        int mm = 1;
//        for (int i = 0; i < n; i++) {
//            mm *= (m - i);
//        }
//        int nn = 1;
//        for (int i = 0; i < n; i++) {
//            nn *= (i + 1);
//        }
//        return mm / nn;
//    }
//
//    private static LinkedList<Integer> createSubSpace(LinkedList<Integer> oldFeatures, LinkedList<Double> oldImpros, int len) throws IOException, ClassNotFoundException {
//        LinkedList<Integer> newFeatures = CloneObject.clone(oldFeatures);
//        LinkedList<Double> newImpros = CloneObject.clone(oldImpros);
//        LinkedList<Integer> subSpace = new LinkedList<Integer>();
//        for (int i = 0; i < len; i++) {
//            int point = Roulette.roulette(newImpros);
//            subSpace.add(newFeatures.get(point));
//            newFeatures.remove(point);
//            newImpros.remove(point);
//        }
//        return subSpace;
//    }
//
//    private static boolean siContainSubSpace(LinkedList<LinkedList<Integer>> subSpaces, LinkedList<Integer> features) {
//        main:
//        for (LinkedList<Integer> subSpace : subSpaces) {
//            if (subSpace.size() == features.size()) {
//                for (Integer feature : features) {
//                    if (!subSpace.contains(feature)) {
//                        continue main;
//                    }
//                }
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 调整Imp
//     *
//     * @param adjustMethodNum
//     * @param impros
//     * @param d               如果是选择平方调整，此参数无效
//     * @return
//     */
//    private static LinkedList<Double> adjust(int adjustMethodNum, double[] impros, double d) {
//        if (adjustMethodNum == POWER_ADJUST) {
//            return powerAdjust(impros);
//        } else if (adjustMethodNum == SUBTRACTION_ADJUST) {
//            return subtractionAdjust(impros, d);
//        } else {
//            throw new RuntimeException("Imp调整方法编号错误：" + adjustMethodNum);
//        }
//    }
//
//    /**
//     * 平方调整Imp
//     *
//     * @param impros
//     * @return
//     */
//    private static LinkedList<Double> powerAdjust(double[] impros) {
//        LinkedList<Double> listImpros = new LinkedList<Double>();
//        for (double impro : impros) {
//            listImpros.add(impro * impro);
//        }
//        return listImpros;
//    }
//
//    /**
//     * 减法调整Imp
//     *
//     * @param impros
//     * @param d
//     * @return
//     */
//    private static LinkedList<Double> subtractionAdjust(double[] impros, double d) {
//        LinkedList<Double> listImpros = new LinkedList<Double>();
//        double minImp = Double.MIN_VALUE;
//        for (double impro : impros) {
//            if (minImp < impro) {
//                minImp = impro;
//            }
//        }
//        minImp = Math.abs(minImp);
//        for (double impro : impros) {
//            listImpros.add(Math.abs(impro) - (minImp - d));
//        }
//        return listImpros;
//    }
//}
