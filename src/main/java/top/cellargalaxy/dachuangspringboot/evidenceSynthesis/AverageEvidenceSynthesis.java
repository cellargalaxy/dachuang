package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;


import top.cellargalaxy.dachuangspringboot.dataSet.Evidence;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryUtils;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class AverageEvidenceSynthesis implements EvidenceSynthesis {
	public static final String NAME = "平均法证据合成";
	public static final int evidenceId = -AverageEvidenceSynthesis.class.getSimpleName().hashCode();
	public static final String evidenceName = "平均法合成证据";

	@Override
	public Evidence synthesisEvidence(Id id) {
		if (id.getEvidences().size() == 0) {
			throw new RuntimeException("特征数量为零，无法合成");
		}
		double fraud = 0;
		double unfraud = 0;
		for (Evidence evidence : id.getEvidences()) {
			fraud += evidence.getFraud();
			unfraud += evidence.getUnfraud();
		}
		fraud /= id.getEvidences().size();
		unfraud /= id.getEvidences().size();
		return new Evidence(evidenceId, evidenceName, new double[]{fraud, unfraud});
	}

	@Override
	public Evidence synthesisEvidence(Id id, Integer withoutEvidenceId) {
		if (id.getEvidences().size() == 0) {
			throw new RuntimeException("特征数量为零，无法合成");
		}
		double fraud = 0;
		double unfraud = 0;
		for (Evidence evidence : id.getEvidences()) {
			if (withoutEvidenceId.equals(evidence.getEvidenceId())) {
				continue;
			}
			fraud += evidence.getFraud();
			unfraud += evidence.getUnfraud();
		}
		fraud /= id.getEvidences().size();
		unfraud /= id.getEvidences().size();
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
		return "平均证据合成";
	}
}
