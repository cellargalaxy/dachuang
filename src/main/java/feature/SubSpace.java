package feature;

import auc.AUC;
import dataSet.DataSet;
import hereditary.Roulette;
import util.CloneObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static feature.FeatureSelection.MEDIAN_MODEL;

/**
 * Created by cellargalaxy on 2017/6/8.
 * 特征子空间静态类
 */
public class SubSpace {
	public static final int POWER_ADJUST = 1;
	public static final int SUBTRACTION_ADJUST = 2;

	public static LinkedList<LinkedList<Integer>> createSubSpaces(int[] featureNums) {
		LinkedList<LinkedList<Integer>> featureSubSpaces = new LinkedList<LinkedList<Integer>>();
		int len = (int) (Math.pow(2, featureNums.length));
		for (int i = 1; i < len; i++) {
			LinkedList<Integer> featureSubSpace = new LinkedList<Integer>();
			char[] chars = Integer.toBinaryString(i).toCharArray();
			for (int j = 0; j < chars.length && j < featureNums.length; j++) {
				if (chars[chars.length - j - 1] == '1') {
					featureSubSpace.add(featureNums[featureNums.length - j - 1]);
				}
			}
			featureSubSpaces.add(featureSubSpace);
		}
		return featureSubSpaces;
	}


	public static LinkedList<LinkedList<Integer>> createSubSpaces(double[][] improFeature, int[] sn, int fnMin, int adjustMethodNum, double d) throws IOException, ClassNotFoundException {
		int fnMax = 0;
		for (int i : sn) {
			fnMax += countC(i, improFeature.length);
		}
		if (fnMin > fnMax) {
			throw new RuntimeException("最小子空间数fnMin:" + fnMin + ",不得大于最大子空间数fnMax:" + fnMax);
		}
		int fn = fnMin + (int) (Math.random() * (fnMax - fnMin));

		improFeature = adjust(adjustMethodNum, improFeature, d);

		LinkedList<Integer> features = new LinkedList<Integer>();
		LinkedList<Double> impros = new LinkedList<Double>();
		for (double[] doubles : improFeature) {
			features.add((int) doubles[0]);
			impros.add(doubles[1]);
		}
		LinkedList<LinkedList<Integer>> subSpaces = new LinkedList<LinkedList<Integer>>();
		for (int i = 0; i < fn; i++) {
			LinkedList<Integer> subSpace = createSubSpace(features, impros, sn[(int) (sn.length * Math.random())]);
			if (!yetContainSubSpace(subSpaces, subSpace)) {
				subSpaces.add(subSpace);
			} else {
				i--;
			}
		}
		return subSpaces;
	}

	private static int countC(int n, int m) {
		int mm = 1;
		for (int i = 0; i < n; i++) {
			mm *= (m - i);
		}
		int nn = 1;
		for (int i = 0; i < n; i++) {
			nn *= (i + 1);
		}
		return mm / nn;
	}

	private static LinkedList<Integer> createSubSpace(LinkedList<Integer> oldFeatures, LinkedList<Double> oldImpros, int len) throws IOException, ClassNotFoundException {
		LinkedList<Integer> newFeatures = CloneObject.clone(oldFeatures);
		LinkedList<Double> newImpros = CloneObject.clone(oldImpros);
		LinkedList<Integer> subSpace = new LinkedList<Integer>();
		for (int i = 0; i < len; i++) {
			int point = Roulette.roulette(newImpros);
			subSpace.add(newFeatures.get(point));
			newFeatures.remove(point);
			newImpros.remove(point);
		}
		return subSpace;
	}

	private static boolean yetContainSubSpace(LinkedList<LinkedList<Integer>> subSpaces, LinkedList<Integer> features) {
		main:
		for (LinkedList<Integer> subSpace : subSpaces) {
			if (subSpace.size() == features.size()) {
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
	 * @param d			   如果是选择平方调整，此参数无效
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
