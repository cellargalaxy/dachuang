package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;


import top.cellargalaxy.dachuangspringboot.dataSet.Id;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-19.
 */
public final class DistanceEvidenceSynthesis implements EvidenceSynthesis {

	public double[] synthesisEvidence(Id id) {
		LinkedList<double[]> evidences = id.getEvidences();
		if (evidences.size() == 0) {
			return null;
		} else {
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				weights[i] = Math.pow(Math.pow(evidence[1], 2) + Math.pow(evidence[2], 2), 0.5);
				i++;
			}
			double count = 0;
			for (double weight : weights) {
				count += weight;
			}
			double[] ds = {0, 0, 0};
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * weights[i] / count;
				ds[2] += evidence[2] * weights[i] / count;
				i++;
			}
			return ds;
		}
	}

	public double[] synthesisEvidence(Id id, Integer withoutEvidNum) {
		LinkedList<double[]> evidences = id.getEvidences();
		if (evidences.size() == 0) {
			return null;
		} else {
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				if (!withoutEvidNum.equals((int) (evidence[0]))) {
					weights[i] = Math.pow(Math.pow(evidence[1], 2) + Math.pow(evidence[2], 2), 0.5);
				}
				i++;
			}
			double count = 0;
			for (double weight : weights) {
				count += weight;
			}
			double[] ds = {0, 0, 0};
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * weights[i] / count;
				ds[2] += evidence[2] * weights[i] / count;
				i++;
			}
			return ds;
		}
	}

	public double[] synthesisEvidenceIndex(Id id, double[] chro) {
		LinkedList<double[]> evidences = id.getEvidences();
		if (evidences.size() == 0) {
			return null;
		} else {
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				weights[i] = Math.pow(Math.pow(evidence[1] * chro[2 * (int) (evidence[0]) - 2], 2) + Math.pow(evidence[2] * chro[2 * (int) (evidence[0]) - 1], 2), 0.5);
				i++;
			}
			double count = 0;
			for (double weight : weights) {
				count += weight;
			}
			double[] ds = {0, 0, 0};
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * chro[2 * (int) (evidence[0]) - 2] * weights[i] / count;
				ds[2] += evidence[2] * chro[2 * (int) (evidence[0]) - 1] * weights[i] / count;
				i++;
			}
			return ds;
		}
	}

	public double[] synthesisEvidenceOrder(Id id, double[] chro) {
		LinkedList<double[]> evidences = id.getEvidences();
		if (evidences.size() == 0) {
			return null;
		} else {
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				weights[i] = Math.pow(Math.pow(evidence[1] * chro[2 * i], 2) + Math.pow(evidence[2] * chro[2 * i + 1], 2), 0.5);
				i++;
			}
			double count = 0;
			for (double weight : weights) {
				count += weight;
			}
			double[] ds = {0, 0, 0};
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * chro[2 * i] * weights[i] / count;
				ds[2] += evidence[2] * chro[2 * i + 1] * weights[i] / count;
				i++;
			}
			return ds;
		}
	}

	public double[] synthesisEvidenceIndex(Id id, Integer withoutEvidNum, double[] chro) {
		LinkedList<double[]> evidences = id.getEvidences();
		if (evidences.size() == 0) {
			return null;
		} else {
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				if (!withoutEvidNum.equals((int) (evidence[0]))) {
					weights[i] = Math.pow(Math.pow(evidence[1] * chro[2 * (int) (evidence[0]) - 2], 2) + Math.pow(evidence[2] * chro[2 * (int) (evidence[0]) - 1], 2), 0.5);
				}
				i++;
			}
			double count = 0;
			for (double weight : weights) {
				count += weight;
			}
			double[] ds = {0, 0, 0};
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * chro[2 * (int) (evidence[0]) - 2] * weights[i] / count;
				ds[2] += evidence[2] * chro[2 * (int) (evidence[0]) - 1] * weights[i] / count;
				i++;
			}
			return ds;
		}
	}

	@Override
	public String toString() {
		return "DistanceEvidenceSynthesis{距离证据合成}";
	}
}
