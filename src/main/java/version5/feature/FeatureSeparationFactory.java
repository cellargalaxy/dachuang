package version5.feature;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class FeatureSeparationFactory {
	/**
	 * 平均数特征分割
	 */
	public static final int AVERAGE_FEATURE_SEPARATION_NUM = 1;
	/**
	 * 中位数特征分割
	 */
	public static final int MEDIAN_FEATURE_SEPARATION_NUM = 2;
	/**
	 * 用户指定值特征分割
	 */
	public static final int CUSTOMIZE_FEATURE_SEPARATION_NUM = 3;
	
	
	public static final FeatureSeparation createFeatureSeparation(int featureSeparationNum, Double separationValue) {
		if (featureSeparationNum == AVERAGE_FEATURE_SEPARATION_NUM) {
			return createAverageFeatureSeparation();
		}
		if (featureSeparationNum == MEDIAN_FEATURE_SEPARATION_NUM) {
			return createMedianFeatureSeparation();
		}
		if (featureSeparationNum == CUSTOMIZE_FEATURE_SEPARATION_NUM) {
			return createCustomizeFeatureSeparation(separationValue);
		}
		throw new RuntimeException("无效featureSeparationNum: " + featureSeparationNum);
	}
	
	public static final boolean check(Integer featureSeparationNum, Double separationValue) {
		if (featureSeparationNum==null) {
			return false;
		}
		if (featureSeparationNum.equals(AVERAGE_FEATURE_SEPARATION_NUM) || featureSeparationNum.equals(MEDIAN_FEATURE_SEPARATION_NUM)) {
			return true;
		}
		if (featureSeparationNum.equals(CUSTOMIZE_FEATURE_SEPARATION_NUM)) {
			return separationValue != null;
		}
		return false;
	}
	
	private static final FeatureSeparation createCustomizeFeatureSeparation(double separationValue) {
		return new CustomizeFeatureSeparation(separationValue);
	}
	
	private static final FeatureSeparation createMedianFeatureSeparation() {
		return new MedianFeatureSeparation();
	}
	
	private static final FeatureSeparation createAverageFeatureSeparation() {
		return new AverageFeatureSeparation();
	}
}
