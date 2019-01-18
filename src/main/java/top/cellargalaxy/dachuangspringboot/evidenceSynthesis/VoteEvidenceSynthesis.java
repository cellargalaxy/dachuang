package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;


import lombok.Data;
import top.cellargalaxy.dachuangspringboot.dataSet.Evidence;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryUtils;

/**
 * Created by cellargalaxy on 17-9-19.
 */
@Data
public final class VoteEvidenceSynthesis implements EvidenceSynthesis {
	public static final String NAME = "投票法证据合成";
	public static final int evidenceId = -VoteEvidenceSynthesis.class.getSimpleName().hashCode();
	public static final String evidenceName = "投票法合成证据";
	private final double thrf;
	private final double thrnf;
	private final double d1;
	private final double d2;

	@Override
	public Evidence synthesisEvidence(Id id) {
		if (id.getEvidences().size() == 0) {
			throw new RuntimeException("特征数量为零，无法合成");
		}
		double a = 0;
		double b = 0;
		for (Evidence evidence : id.getEvidences()) {
			if (evidence.getFraud() > thrf) {
				a++;
			} else if (evidence.getUnfraud() < thrnf) {
				b++;
			}
		}
		double fraud = 0;
		double unfraud = 0;
		if (a > b) {
			fraud = (a - b) / (a + b) * d1;
		} else if (a < b) {
			unfraud = (b - a) / (a + b) * d2;
		}
		return new Evidence(evidenceId, evidenceName, new double[]{fraud, unfraud});
	}

	@Override
	public Evidence synthesisEvidence(Id id, Integer withoutEvidenceId) {
		if (id.getEvidences().size() == 0) {
			throw new RuntimeException("特征数量为零，无法合成");
		}
		double a = 0;
		double b = 0;
		for (Evidence evidence : id.getEvidences()) {
			if (withoutEvidenceId.equals(evidence.getEvidenceId())) {
				continue;
			}
			if (evidence.getFraud() > thrf) {
				a++;
			} else if (evidence.getUnfraud() < thrnf) {
				b++;
			}
		}
		double fraud = 0;
		double unfraud = 0;
		if (a > b) {
			fraud = (a - b) / (a + b) * d1;
		} else if (a < b) {
			unfraud = (b - a) / (a + b) * d2;
		}
		return new Evidence(evidenceId, evidenceName, new double[]{fraud, unfraud});
	}

	@Override
	public Evidence synthesisEvidence(Id id, Chromosome chromosome) {
		return synthesisEvidence(HereditaryUtils.evolution(id.clone(), chromosome));
	}

	@Override
	public Evidence synthesisEvidence(Id id, Integer withoutEvidenceId, Chromosome chromosome) {
		return synthesisEvidence(HereditaryUtils.evolution(id.clone(withoutEvidenceId), chromosome));
	}

	@Override
	public String toString() {
		return "投票法证据合成{" +
				"thrf=" + thrf +
				", thrnf=" + thrnf +
				", d1=" + d1 +
				", d2=" + d2 +
				'}';
	}
}
