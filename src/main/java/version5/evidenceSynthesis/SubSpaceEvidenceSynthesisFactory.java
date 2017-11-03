package version5.evidenceSynthesis;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class SubSpaceEvidenceSynthesisFactory {
	/**
	 * 平均法多推理结果合成
	 */
	public static final int AVERAGE_EVIDENCE_SYNTHESIS_NUM = 1;
	/**
	 * 投票法多推理结果合成
	 */
	public static final int VOTE_EVIDENCE_SYNTHESIS_NUM = 2;
	
	public static final EvidenceSynthesis createSubSpaceEvidenceSynthesis(int subSpaceEvidenceSynthesisNum, Double thrf, Double thrnf, Double d1, Double d2) {
		if (subSpaceEvidenceSynthesisNum == AVERAGE_EVIDENCE_SYNTHESIS_NUM) {
			return createAverageEvidenceSynthesis();
		}
		if (subSpaceEvidenceSynthesisNum == VOTE_EVIDENCE_SYNTHESIS_NUM) {
			return createVoteEvidenceSynthesis(thrf, thrnf, d1, d2);
		}
		throw new RuntimeException("无效subSpaceEvidenceSynthesisNum: " + subSpaceEvidenceSynthesisNum);
	}
	
	public static final boolean check(Integer subSpaceEvidenceSynthesisNum, Double thrf, Double thrnf, Double d1, Double d2) {
		if (subSpaceEvidenceSynthesisNum == null) {
			return false;
		}
		if (subSpaceEvidenceSynthesisNum.equals(AVERAGE_EVIDENCE_SYNTHESIS_NUM)) {
			return true;
		}
		if (subSpaceEvidenceSynthesisNum.equals(VOTE_EVIDENCE_SYNTHESIS_NUM)) {
			return thrf != null && thrf >= 0 && thrf <= 1 && thrnf != null && thrnf >= 0 && thrnf <= 1 && d1 != null && d2 != null;
		}
		return false;
	}
	
	private static final EvidenceSynthesis createVoteEvidenceSynthesis(double thrf, double thrnf, double d1, double d2) {
		return new VoteEvidenceSynthesis(thrf, thrnf, d1, d2);
	}
	
	private static final EvidenceSynthesis createAverageEvidenceSynthesis() {
		return new AverageEvidenceSynthesis();
	}
}
