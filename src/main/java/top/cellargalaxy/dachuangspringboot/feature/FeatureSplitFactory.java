package top.cellargalaxy.dachuangspringboot.feature;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class FeatureSplitFactory {
	public static final String[] NAMES = {AverageFeatureSplit.NAME, CustomizeFeatureSplit.NAME, MedianFeatureSplit.NAME};
	private static FeatureSplit featureSplit;

	public static final FeatureSplit getFeatureSplit(RunParameter runParameter) {
		if (featureSplit == null) {
			featureSplit = createFeatureSeparation(runParameter);
		}
		return featureSplit;
	}

	public static void setFeatureSplit(FeatureSplit featureSplit) {
		FeatureSplitFactory.featureSplit = featureSplit;
	}

	public static final FeatureSplit createFeatureSeparation(RunParameter runParameter) {
		String name = runParameter.getFeatureSplitName();
		featureSplit = createFeatureSeparation(name, runParameter);
		return featureSplit;
	}

	public static final FeatureSplit createFeatureSeparation(String name, RunParameter runParameter) {
		if (AverageFeatureSplit.NAME.equals(name)) {
			return new AverageFeatureSplit();
		}
		if (CustomizeFeatureSplit.NAME.equals(name)) {
			return new CustomizeFeatureSplit(runParameter.getSplitValue());
		}
		if (MedianFeatureSplit.NAME.equals(name)) {
			return new MedianFeatureSplit();
		}
		throw new RuntimeException("无效-FeatureSplit: " + name);
	}
}
