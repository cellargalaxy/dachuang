package version3.evaluation;

import util.CloneObject;
import version3.dataSet.DataSet;
import version3.dataSet.Id;
import version3.evidenceSynthesis.EvidenceSynthesis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class Auc implements Evaluation {
	private EvidenceSynthesis evidenceSynthesis;
	
	public Auc(EvidenceSynthesis evidenceSynthesis) {
		this.evidenceSynthesis = evidenceSynthesis;
	}
	
	public double countEvaluation(DataSet cloneDataSet) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : cloneDataSet.getIds()) {
			double[] ds = evidenceSynthesis.synthesisEvidence(id);
			if (id.getLabel() == Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s, ds1s);
	}
	
	public double countEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = evidenceSynthesis.synthesisEvidence(id, withoutEvidNum);
			if (id.getLabel() == Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s, ds1s);
	}
	
	public double countEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException {
		DataSet newDataSet = CloneObject.clone(dataSet);
		newDataSet.removeNotEqual(withEvidNums);
		return countEvaluation(newDataSet);
	}
	
	public double countIndexEvaluation(DataSet dataSet, double[] chro) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = evidenceSynthesis.synthesisEvidenceIndex(id, chro);
			if (id.getLabel() == Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s, ds1s);
	}
	
	public double countOrderEvaluation(DataSet cloneDataSet, double[] chro) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : cloneDataSet.getIds()) {
			double[] ds = evidenceSynthesis.synthesisEvidenceOrder(id, chro);
			if (id.getLabel() == Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s, ds1s);
	}
	
	public double countIndexEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum, double[] chro) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = evidenceSynthesis.synthesisEvidenceIndex(id, withoutEvidNum, chro);
			if (id.getLabel() == Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s, ds1s);
	}
	
	public double countOrderEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException {
		DataSet newDataSet = CloneObject.clone(dataSet);
		newDataSet.removeNotEqual(withEvidNums);
		return countOrderEvaluation(newDataSet, chro);
	}
	
	
	private final double doCountAuc(LinkedList<double[]> ds0s, LinkedList<double[]> ds1s) {
		double auc = 0;
		for (double[] ds1 : ds1s) {
			for (double[] ds0 : ds0s) {
				if (ds1[1] > ds0[1]) {
					auc++;
				} else if (ds1[1] == ds0[1]) {
					auc = auc + 0.5;
				}
			}
		}
		return auc / ds1s.size() / ds0s.size();
	}
	
	public EvidenceSynthesis getEvidenceSynthesis() {
		return evidenceSynthesis;
	}
	
	public void setEvidenceSynthesis(EvidenceSynthesis evidenceSynthesis) {
		this.evidenceSynthesis = evidenceSynthesis;
	}
}
