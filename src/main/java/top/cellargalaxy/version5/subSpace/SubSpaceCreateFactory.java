package top.cellargalaxy.version5.subSpace;


import top.cellargalaxy.version5.evaluation.EvaluationExecutor;
import top.cellargalaxy.version5.feature.FeatureSeparation;
import top.cellargalaxy.version5.hereditary.Hereditary;
import top.cellargalaxy.version5.hereditary.HereditaryParameter;
import top.cellargalaxy.version5.hereditary.ParentChrosChoose;

/**
 * Created by cellargalaxy on 17-11-2.
 */
public class SubSpaceCreateFactory {
	/**
	 * 完全随机子空间
	 */
	public static final int RANDOM_SUB_SPACE_CREATE_NUM = 1;
	/**
	 * sn随机子空间
	 */
	public static final int SN_RANDOM_SUB_SPACE_CREATE_NUM = 2;
	/**
	 * sn特征选择子空间
	 */
	public static final int SN_FEATURE_SELECTION_SUB_SPACE_CREATE_NUM = 3;
	
	public static final SubSpaceCreate createSubSpaceCreate(int subSpaceCreateNum, ImprotenceAdjust improtenceAdjust, int[][] sns, int[] fnMins,
	                                                        FeatureSeparation featureSeparation, Double stop, Hereditary hereditary,
	                                                        HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, EvaluationExecutor evaluationExecutor) {
		if (subSpaceCreateNum == RANDOM_SUB_SPACE_CREATE_NUM) {
			return createRandomSubSpaceCreate();
		}
		if (subSpaceCreateNum == SN_RANDOM_SUB_SPACE_CREATE_NUM) {
			return createSnRandomSubSpaceCreate(sns, fnMins);
		}
		if (subSpaceCreateNum == SN_FEATURE_SELECTION_SUB_SPACE_CREATE_NUM) {
			return createSnFeatureSelectionSubSpaceCreate(improtenceAdjust, sns, fnMins, featureSeparation, stop, hereditary, hereditaryParameter, parentChrosChoose, evaluationExecutor);
		}
		throw new RuntimeException("无效subSpaceCreateNum: " + subSpaceCreateNum);
	}
	
	public static final boolean check(Integer subSpaceCreateNum, ImprotenceAdjust improtenceAdjust, int[][] sns, int[] fnMins,
	                                  FeatureSeparation featureSeparation, Double stop, Hereditary hereditary, HereditaryParameter hereditaryParameter,
	                                  ParentChrosChoose parentChrosChoose, EvaluationExecutor evaluationExecutor) {
		if (subSpaceCreateNum == null) {
			return false;
		}
		if (subSpaceCreateNum.equals(RANDOM_SUB_SPACE_CREATE_NUM)) {
			return true;
		}
		if (subSpaceCreateNum.equals(SN_RANDOM_SUB_SPACE_CREATE_NUM)) {
			return sns != null && fnMins != null;
		}
		if (subSpaceCreateNum.equals(SN_FEATURE_SELECTION_SUB_SPACE_CREATE_NUM)) {
			return improtenceAdjust != null && sns != null && fnMins != null && sns.length==fnMins.length && featureSeparation != null && stop != null && stop >= 0 && stop < 1 && hereditary != null &&
					hereditaryParameter != null && parentChrosChoose != null && evaluationExecutor != null;
		}
		return false;
	}
	
	private static final SubSpaceCreate createSnFeatureSelectionSubSpaceCreate(ImprotenceAdjust improtenceAdjust, int[][] sns, int[] fnMins, FeatureSeparation featureSeparation, double stop, Hereditary hereditary, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, EvaluationExecutor evaluationExecutor) {
		return new SnFeatureSelectionSubSpaceCreate(improtenceAdjust, sns, fnMins, featureSeparation, stop, hereditary, hereditaryParameter, parentChrosChoose, evaluationExecutor);
	}
	
	private static final SubSpaceCreate createSnRandomSubSpaceCreate(int[][] sns, int[] fnMins) {
		return new SnRandomSubSpaceCreate(sns, fnMins);
	}
	
	private static final SubSpaceCreate createRandomSubSpaceCreate() {
		return new RandomSubSpaceCreate();
	}
}
