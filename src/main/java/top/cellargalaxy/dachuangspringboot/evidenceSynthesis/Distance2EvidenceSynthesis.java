package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;


import top.cellargalaxy.dachuangspringboot.dataSet.Id;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-19.
 */
public final class Distance2EvidenceSynthesis implements EvidenceSynthesis {

	public double[] synthesisEvidence(Id id) {
		LinkedList<double[]> evidences = id.getEvidences();
		if (evidences.size() == 0) {
			return null;
		} else {
			int aCount = 0;
			int bCount = 0;
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				weights[i] = Math.pow(Math.pow(evidence[1], 2) + Math.pow(evidence[2], 2), 0.5) * Math.abs(evidence[1] - evidence[2]);
				if (evidence[1] > evidence[2]) {
					aCount++;
				} else if (evidence[1] < evidence[2]) {
					bCount++;
				} else {
					aCount++;
					bCount++;
				}
				i++;
			}
			double[] ds = {0, 0, 0};
			double ab = aCount + bCount;
			double w = 0;
			for (double weight : weights) {
				w += weight;
			}
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * (weights[i] / w) * (aCount / ab);
				ds[2] += evidence[2] * (weights[i] / w) * (bCount / ab);
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
			int aCount = 0;
			int bCount = 0;
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				if (!withoutEvidNum.equals((int) (evidence[0]))) {
					weights[i] = Math.pow(Math.pow(evidence[1], 2) + Math.pow(evidence[2], 2), 0.5) * Math.abs(evidence[1] - evidence[2]);
					if (evidence[1] > evidence[2]) {
						aCount++;
					} else if (evidence[1] < evidence[2]) {
						bCount++;
					} else {
						aCount++;
						bCount++;
					}
				}
				i++;
			}
			double[] ds = {0, 0, 0};
			double ab = aCount + bCount;
			double w = 0;
			for (double weight : weights) {
				w += weight;
			}
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * (weights[i] / w) * (aCount / ab);
				ds[2] += evidence[2] * (weights[i] / w) * (bCount / ab);
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
			int aCount = 0;
			int bCount = 0;
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				weights[i] = Math.pow(Math.pow(evidence[1] * chro[2 * (int) (evidence[0]) - 2], 2) + Math.pow(evidence[2] * chro[2 * (int) (evidence[0]) - 1], 2), 0.5) * Math.abs(evidence[1] * chro[2 * (int) (evidence[0]) - 2] - evidence[2] * chro[2 * (int) (evidence[0]) - 1]);
				if (evidence[1] > evidence[2]) {
					aCount++;
				} else if (evidence[1] < evidence[2]) {
					bCount++;
				} else {
					aCount++;
					bCount++;
				}
				i++;
			}
			double[] ds = {0, 0, 0};
			double ab = aCount + bCount;
			double w = 0;
			for (double weight : weights) {
				w += weight;
			}
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * chro[2 * (int) (evidence[0]) - 2] * (weights[i] / w) * (aCount / ab);
				ds[2] += evidence[2] * chro[2 * (int) (evidence[0]) - 1] * (weights[i] / w) * (bCount / ab);
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
			int aCount = 0;
			int bCount = 0;
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				weights[i] = Math.pow(Math.pow(evidence[1] * chro[2 * i], 2) + Math.pow(evidence[2] * chro[2 * i + 1], 2), 0.5) * Math.abs(evidence[1] * chro[2 * i] - evidence[2] * chro[2 * i + 1]);
				if (evidence[1] > evidence[2]) {
					aCount++;
				} else if (evidence[1] < evidence[2]) {
					bCount++;
				} else {
					aCount++;
					bCount++;
				}
				i++;
			}
			double[] ds = {0, 0, 0};
			double ab = aCount + bCount;
			double w = 0;
			for (double weight : weights) {
				w += weight;
			}
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * chro[2 * i] * (weights[i] / w) * (aCount / ab);
				ds[2] += evidence[2] * chro[2 * i + 1] * (weights[i] / w) * (bCount / ab);
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
			int aCount = 0;
			int bCount = 0;
			double[] weights = new double[evidences.size()];
			int i = 0;
			for (double[] evidence : evidences) {
				if (!withoutEvidNum.equals((int) (evidence[0]))) {
					weights[i] = Math.pow(Math.pow(evidence[1] * chro[2 * (int) (evidence[0]) - 2], 2) + Math.pow(evidence[2] * chro[2 * (int) (evidence[0]) - 1], 2), 0.5) * Math.abs(evidence[1] * chro[2 * (int) (evidence[0]) - 2] - evidence[2] * chro[2 * (int) (evidence[0]) - 1]);
					if (evidence[1] > evidence[2]) {
						aCount++;
					} else if (evidence[1] < evidence[2]) {
						bCount++;
					} else {
						aCount++;
						bCount++;
					}
				}
				i++;
			}
			double[] ds = {0, 0, 0};
			double ab = aCount + bCount;
			double w = 0;
			for (double weight : weights) {
				w += weight;
			}
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * chro[2 * (int) (evidence[0]) - 2] * (weights[i] / w) * (aCount / ab);
				ds[2] += evidence[2] * chro[2 * (int) (evidence[0]) - 1] * (weights[i] / w) * (bCount / ab);
				i++;
			}
			return ds;
		}
	}

	@Override
	public String toString() {
		return "Distance2EvidenceSynthesis{距离2istance}";
	}
}
