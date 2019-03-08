package top.cellargalaxy.dachuangspringboot.evaluation;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.dataSet.Evidence;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class Auc extends AbstractEvaluation {
	public static final String NAME = "auc";
	private final EvidenceSynthesis evidenceSynthesis;

	public Auc(EvidenceSynthesis evidenceSynthesis) {
		this.evidenceSynthesis = evidenceSynthesis;
	}

	@Override
	public Future<Double> countEvaluation(DataSet dataSet) {
		return EXECUTOR_SERVICE.submit(new Callable<Double>() {
			@Override
			public Double call() {
				Collection<Evidence> ds1s = new LinkedList<>();
				Collection<Evidence> ds0s = new LinkedList<>();
				for (Id id : dataSet.getIds()) {
					Evidence evidence = evidenceSynthesis.synthesisEvidence(id);
					if (id.getLabel() == Id.LABEL_0) {
						ds0s.add(evidence);
					} else if (id.getLabel() == Id.LABEL_1) {
						ds1s.add(evidence);
					}
				}
				return countAuc(ds0s, ds1s);
			}
		});
	}

	@Override
	public Future<Double> countEvaluation(DataSet dataSet, Integer withoutEvidenceId) {
		return EXECUTOR_SERVICE.submit(new Callable<Double>() {
			@Override
			public Double call() {
				Collection<Evidence> ds1s = new LinkedList<>();
				Collection<Evidence> ds0s = new LinkedList<>();
				for (Id id : dataSet.getIds()) {
					Evidence evidence = evidenceSynthesis.synthesisEvidence(id, withoutEvidenceId);
					if (id.getLabel() == Id.LABEL_0) {
						ds0s.add(evidence);
					} else if (id.getLabel() == Id.LABEL_1) {
						ds1s.add(evidence);
					}
				}
				return countAuc(ds0s, ds1s);
			}
		});
	}

	@Override
	public Future<Double> countEvaluation(DataSet dataSet, Collection<Integer> withEvidenceIds) {
		return countEvaluation(dataSet.clone(withEvidenceIds));
	}

	@Override
	public Future<Double> countEvaluation(DataSet dataSet, Chromosome chromosome) {
		return countEvaluation(HereditaryUtils.evolution(dataSet.clone(), chromosome));
	}

	@Override
	public Future<Double> countEvaluation(DataSet dataSet, Integer withoutEvidenceId, Chromosome chromosome) {
		return countEvaluation(HereditaryUtils.evolution(dataSet.clone(withoutEvidenceId), chromosome));
	}

	@Override
	public Future<Double> countEvaluation(DataSet dataSet, Collection<Integer> withEvidenceIds, Chromosome chromosome) {
		return countEvaluation(HereditaryUtils.evolution(dataSet.clone(withEvidenceIds), chromosome));
	}

	private final double countAuc(Collection<Evidence> ds0s, Collection<Evidence> ds1s) {
		double auc = 0;
		for (Evidence ds1 : ds1s) {
			for (Evidence ds0 : ds0s) {
				if (ds1.getFraud() > ds0.getFraud()) {
					auc++;
				} else if (ds1.getFraud() == ds0.getFraud()) {
					auc = auc + 0.5;
				}
			}
		}
		return auc / ds1s.size() / ds0s.size();
	}


	@Override
	public String toString() {
		return "AUC{" +
				"evidenceSynthesis=" + evidenceSynthesis +
				'}';
	}
}
