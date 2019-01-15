package top.cellargalaxy.dachuangspringboot.run;

import top.cellargalaxy.dachuangspringboot.evaluation.Auc;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EuclideanDistanceEvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.feature.AverageFeatureSplit;
import top.cellargalaxy.dachuangspringboot.hereditary.RouletteParentChrosChoose;
import top.cellargalaxy.dachuangspringboot.subSpace.PowerImprotenceAdjust;

/**
 * @author cellargalaxy
 * @time 2019/1/14
 */
public class RunParameter {
	public static String evidenceSynthesisName = EuclideanDistanceEvidenceSynthesis.NAME;
	public static double thrf;
	public static double thrnf;
	public static double d1;
	public static double d2;

	public static String evaluationName = Auc.NAME;

	public static String featureSplitName = AverageFeatureSplit.NAME;
	public static double splitValue;

	public static String parentChrosChooseName = RouletteParentChrosChoose.NAME;

	public static String improtenceAdjustName = PowerImprotenceAdjust.NAME;
	public static double adjustD;
}
