package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * @author cellargalaxy
 * @time 2019/1/14
 */
public class EvidenceSynthesisFactory {

	public static final EvidenceSynthesis createEvidenceSynthesis() {
		String name = RunParameter.evidenceSynthesisName;
		if (AverageEvidenceSynthesis.NAME.equals(name)) {
			return new AverageEvidenceSynthesis();
		}
		if (DsEvidenceSynthesis.NAME.equals(name)) {
			return new DsEvidenceSynthesis();
		}
		if (EuclideanDistanceEvidenceSynthesis.NAME.equals(name)) {
			return new EuclideanDistanceEvidenceSynthesis();
		}
		if (EuclideanDistanceWeightEvidenceSynthesis.NAME.equals(name)) {
			return new EuclideanDistanceWeightEvidenceSynthesis();
		}
		if (VoteEvidenceSynthesis.NAME.equals(name)) {
			return new VoteEvidenceSynthesis(RunParameter.thrf, RunParameter.thrnf, RunParameter.d1, RunParameter.d2);
		}
		throw new RuntimeException("无效-EvidenceSynthesis: " + name);
	}
}
