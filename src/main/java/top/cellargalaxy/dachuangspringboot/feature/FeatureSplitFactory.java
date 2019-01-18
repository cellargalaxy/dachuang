package top.cellargalaxy.dachuangspringboot.feature;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class FeatureSplitFactory {
	private static FeatureSplit featureSplit;

	public static final FeatureSplit getFeatureSplit() {
		return featureSplit;
	}

	public static final FeatureSplit createFeatureSeparation(RunParameter runParameter) {
		String name = runParameter.getFeatureSplitName();
		if (AverageFeatureSplit.NAME.equals(name)) {
			featureSplit = new AverageFeatureSplit();
			return featureSplit;
		}
		if (CustomizeFeatureSplit.NAME.equals(name)) {
			featureSplit = new CustomizeFeatureSplit(runParameter.getSplitValue());
			return featureSplit;
		}
		if (MedianFeatureSplit.NAME.equals(name)) {
			featureSplit = new MedianFeatureSplit();
			return featureSplit;
		}
		throw new RuntimeException("无效-FeatureSplit: " + name);
	}

}
