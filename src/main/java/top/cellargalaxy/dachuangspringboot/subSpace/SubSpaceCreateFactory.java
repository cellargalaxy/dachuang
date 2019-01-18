package top.cellargalaxy.dachuangspringboot.subSpace;

import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.evaluation.EvaluationFactory;
import top.cellargalaxy.dachuangspringboot.feature.FeatureSplitFactory;
import top.cellargalaxy.dachuangspringboot.hereditary.ParentChrosChooseFactory;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * @author cellargalaxy
 * @time 2019/1/16
 */
public class SubSpaceCreateFactory {
	public static final String[] NAMES = {RandomSubSpaceCreate.NAME, SnRandomSubSpaceCreate.NAME, SnFeatureSelectionSubSpaceCreate.NAME};
	private static SubSpaceCreate subSpaceCreate;

	public static final SubSpaceCreate getSubSpaceCreate(RunParameter runParameter, DataSet dataSet) {
		if (subSpaceCreate == null) {
			subSpaceCreate = createSubSpaceCreate(runParameter, dataSet);
		}
		return subSpaceCreate;
	}

	public static void setSubSpaceCreate(SubSpaceCreate subSpaceCreate) {
		SubSpaceCreateFactory.subSpaceCreate = subSpaceCreate;
	}

	public static final SubSpaceCreate createSubSpaceCreate(RunParameter runParameter, DataSet dataSet) {
		String name = runParameter.getSubSpaceCreateName();
		subSpaceCreate = createSubSpaceCreate(name, runParameter, dataSet);
		return subSpaceCreate;
	}

	public static final SubSpaceCreate createSubSpaceCreate(String name, RunParameter runParameter, DataSet dataSet) {
		if (RandomSubSpaceCreate.NAME.equals(name)) {
			return new RandomSubSpaceCreate();
		}
		if (SnRandomSubSpaceCreate.NAME.equals(name)) {
			return new SnRandomSubSpaceCreate(runParameter.getSns());
		}
		if (SnFeatureSelectionSubSpaceCreate.NAME.equals(name)) {
			return new SnFeatureSelectionSubSpaceCreate(
					runParameter.getSns(),
					FeatureSplitFactory.getFeatureSplit(runParameter),
					runParameter.getFeatureSelectionDeviation(),
					runParameter.getHereditaryParameter(),
					ParentChrosChooseFactory.getParentChrosChoose(runParameter),
					EvaluationFactory.getEvaluation(runParameter, dataSet),
					ImprotenceAdjustFactory.getImprotenceAdjust(runParameter));
		}
		throw new RuntimeException("无效-SubSpaceCreate: " + name);
	}
}
