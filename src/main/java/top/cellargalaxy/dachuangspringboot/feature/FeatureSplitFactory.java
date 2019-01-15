package top.cellargalaxy.dachuangspringboot.feature;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class FeatureSplitFactory {

	public static final FeatureSplit createFeatureSeparation() {
		String name = RunParameter.featureSplitName;
		if (AverageFeatureSplit.NAME.equals(name)) {
			return new AverageFeatureSplit();
		}
		if (CustomizeFeatureSplit.NAME.equals(name)) {
			return new CustomizeFeatureSplit(RunParameter.splitValue);
		}
		if (MedianFeatureSplit.NAME.equals(name)) {
			return new MedianFeatureSplit();
		}
		throw new RuntimeException("无效-FeatureSplit: " + name);
	}

}
