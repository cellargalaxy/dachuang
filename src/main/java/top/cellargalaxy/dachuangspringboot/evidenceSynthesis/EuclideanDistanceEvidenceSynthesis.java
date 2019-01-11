package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;


import top.cellargalaxy.dachuangspringboot.dataSet.Evidence;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryUtils;

/**
 * Created by cellargalaxy on 17-9-19.
 */
public final class EuclideanDistanceEvidenceSynthesis implements EvidenceSynthesis {
	public static final int evidenceId = -EuclideanDistanceEvidenceSynthesis.class.getSimpleName().hashCode();
	public static final String evidenceName = "欧几距离合成证据";
	@Override
	public Evidence synthesisEvidence(Id id) {
		if (id.getEvidences().size() == 0) {
			return null;
		}
		double[] weights = new double[id.getEvidences().size()];
		int i = 0;
		for (Evidence evidence : id.getEvidences()) {
			weights[i] = Math.pow(Math.pow(evidence.getFraud(), 2) + Math.pow(evidence.getUnfraud(), 2), 0.5);
			i++;
		}
		double count = 0;
		for (double weight : weights) {
			count += weight;
		}
		double fraud = 0;
		double unfraud = 0;
		for (Evidence evidence : id.getEvidences()) {
			fraud += evidence.getFraud() * weights[i] / count;
			unfraud += evidence.getUnfraud() * weights[i] / count;
			i++;
		}
		return new Evidence(evidenceId, evidenceName, new double[]{fraud, unfraud});
	}
	@Override
	public Evidence synthesisEvidence(Id id, Integer withoutEvidenceId) {
		if (id.getEvidences().size() == 0) {
			return null;
		}
		double[] weights = new double[id.getEvidences().size()];
		int i = 0;
		for (Evidence evidence : id.getEvidences()) {
			if (withoutEvidenceId.equals(evidence.getEvidenceId())) {
				i++;
				continue;
			}
			weights[i] = Math.pow(Math.pow(evidence.getFraud(), 2) + Math.pow(evidence.getUnfraud(), 2), 0.5);
			i++;
		}
		double count = 0;
		for (double weight : weights) {
			count += weight;
		}
		double fraud = 0;
		double unfraud = 0;
		for (Evidence evidence : id.getEvidences()) {
			if (withoutEvidenceId.equals(evidence.getEvidenceId())) {
				i++;
				continue;
			}
			fraud += evidence.getFraud() * weights[i] / count;
			unfraud += evidence.getUnfraud() * weights[i] / count;
			i++;
		}
		return new Evidence(evidenceId, evidenceName, new double[]{fraud, unfraud});
	}

	@Override
	public Evidence synthesisEvidence(Id id, Chromosome chromosome) {
		return synthesisEvidence(HereditaryUtils.evolution(id.clone(), chromosome));
	}

	@Override
	public Evidence synthesisEvidence(Id id, Integer withoutEvidenceId, Chromosome chromosome) {
		return synthesisEvidence(HereditaryUtils.evolution(id.clone(), chromosome), withoutEvidenceId);
	}

	@Override
	public String toString() {
		return "欧几距离合成证据";
	}
}
