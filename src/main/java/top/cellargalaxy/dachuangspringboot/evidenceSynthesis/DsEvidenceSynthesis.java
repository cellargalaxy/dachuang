package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;


import top.cellargalaxy.dachuangspringboot.dataSet.Evidence;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryUtils;

import java.util.Iterator;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class DsEvidenceSynthesis implements EvidenceSynthesis {
	public static final String NAME = "DS证据合成";
	public static final int evidenceId = -DsEvidenceSynthesis.class.getSimpleName().hashCode();
	public static final String evidenceName = "DS合成证据";

	@Override
	public Evidence synthesisEvidence(Id id) {
		if (id.getEvidences().size() == 0) {
			return null;
		}
		Iterator<Evidence> iterator = id.getEvidences().iterator();
		Evidence ds1 = iterator.next();
		Evidence ds2;
		while (iterator.hasNext()) {
			ds2 = iterator.next();
			ds1 = countEvidence(ds1, ds2);
		}
		return ds1;
	}

	@Override
	public Evidence synthesisEvidence(Id id, Integer withoutEvidenceId) {
		if (id.getEvidences().size() == 0) {
			return null;
		}
		Iterator<Evidence> iterator = id.getEvidences().iterator();
		Evidence ds1;
		Evidence ds2;
		do {
			ds1 = iterator.next();
			if (!withoutEvidenceId.equals(ds1.getEvidenceId())) {
				break;
			}
		} while (iterator.hasNext());
		if (!iterator.hasNext()) {
			return null;
		}
		do {
			ds2 = iterator.next();
			if (withoutEvidenceId.equals(ds2.getEvidenceId())) {
				continue;
			}
			ds1 = countEvidence(ds1, ds2);
		} while (iterator.hasNext());
		return ds1;
	}

	@Override
	public Evidence synthesisEvidence(Id id, Chromosome chromosome) {
		return synthesisEvidence(HereditaryUtils.evolution(id.clone(), chromosome));
	}

	@Override
	public Evidence synthesisEvidence(Id id, Integer withoutEvidenceId, Chromosome chromosome) {
		return synthesisEvidence(HereditaryUtils.evolution(id.clone(withoutEvidenceId), chromosome));
	}

	private final Evidence countEvidence(Evidence evidence1, Evidence evidence2) {
		double k = countK(evidence1.getFraud(), evidence1.getUnfraud(), evidence2.getFraud(), evidence2.getUnfraud());
		double fraud = countFraud(evidence1.getFraud(), evidence1.getUnfraud(), evidence2.getFraud(), evidence2.getUnfraud(), k);
		double unfraud = countUnfraud(evidence1.getFraud(), evidence1.getUnfraud(), evidence2.getFraud(), evidence2.getUnfraud(), k);
		return new Evidence(evidenceId, evidenceName, new double[]{fraud, unfraud});
	}


	private final double countFraud(double fraud1, double unfraud1, double fraud2, double unfraud2, double k) {
		return (fraud1 * fraud2 + fraud1 * (1 - fraud2 - unfraud2) + fraud2 * (1 - fraud1 - unfraud1)) / k;
	}

	private final double countUnfraud(double fraud1, double unfraud1, double fraud2, double unfraud2, double k) {
		return (unfraud1 * unfraud2 + unfraud1 * (1 - fraud2 - unfraud2) + unfraud2 * (1 - fraud1 - unfraud1)) / k;
	}

	private final double countK(double fraud1, double unfraud1, double fraud2, double unfraud2) {
		return 1 - fraud1 * unfraud2 - unfraud1 * fraud2;
	}

	@Override
	public String toString() {
		return "DS证据合成";
	}
}
