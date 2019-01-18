package top.cellargalaxy.dachuangspringboot.subSpace;

import top.cellargalaxy.dachuangspringboot.evaluation.EvaluationFactory;
import top.cellargalaxy.dachuangspringboot.feature.FeatureSplitFactory;
import top.cellargalaxy.dachuangspringboot.hereditary.ParentChrosChooseFactory;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * @author cellargalaxy
 * @time 2019/1/16
 */
public class SubSpaceCreateFactory {
	private static SubSpaceCreate subSpaceCreate;

	public static final SubSpaceCreate getSubSpaceCreate() {
		return subSpaceCreate;
	}

	public static final SubSpaceCreate createSubSpaceCreate(RunParameter runParameter) {
		String name = runParameter.getSubSpaceCreateName();
		if (RandomSubSpaceCreate.NAME.equals(name)) {
			subSpaceCreate = new RandomSubSpaceCreate();
			return subSpaceCreate;
		}
		if (SnRandomSubSpaceCreate.NAME.equals(name)) {
			subSpaceCreate = new SnRandomSubSpaceCreate(runParameter.getSns());
			return subSpaceCreate;
		}
		if (SnFeatureSelectionSubSpaceCreate.NAME.equals(name)) {
			subSpaceCreate = new SnFeatureSelectionSubSpaceCreate(
					runParameter.getSns(),
					FeatureSplitFactory.createFeatureSeparation(runParameter),
					runParameter.getFeatureSelectionDeviation(),
					runParameter.getHereditaryParameter(),
					ParentChrosChooseFactory.createParentChrosChoose(runParameter),
					EvaluationFactory.createEvaluation(runParameter),
					ImprotenceAdjustFactory.createImprotenceAdjust(runParameter));
			return subSpaceCreate;
		}
		throw new RuntimeException("无效-SubSpaceCreate: " + name);
	}
}
