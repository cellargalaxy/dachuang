package version2.auc;

import version2.dataSet.Id;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class My2DsCount implements DsCount {
	public double[] countDs(Id id, double[] chro) {
		if (id.getEvidences().size() == 0) {
			return null;
		} else if (id.getEvidences().size() == 1) {
			return id.getEvidences().getFirst();
		} else {
			LinkedList<double[]> evidences = new LinkedList<double[]>();
			int aCount = 0;
			int bCount = 0;
			double[] weights = new double[id.getEvidences().size()];
			int i = 0;
			for (double[] doubles : id.getEvidences()) {
				double[] evidence={doubles[0], doubles[1] * chro[i*2], doubles[2] * chro[(i*2)+1]};
				weights[i] = Math.pow(Math.pow(evidence[1], 2) + Math.pow(evidence[2], 2), 0.5) * Math.abs(evidence[1] - evidence[2]);
				if (evidence[1] > evidence[2]) {
					aCount++;
				} else if (evidence[1] < evidence[2]) {
					bCount++;
				}
				evidences.add(evidence);
				i++;
			}
			double count = 0;
			for (double weight : weights) {
				count += weight;
			}
			double[] ds = {0, 0, 0};
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * (weights[i] * count) * ((double) (aCount) / (double) (aCount + bCount));
				ds[2] += evidence[2] * (weights[i] * count) * ((double) (bCount) / (double) (aCount + bCount));
				i++;
			}
			return ds;
		}
	}
	
	public double[] countDs(Id id, double[] chro, Integer withoutEvidNum) {
		if (id.getEvidences().size() == 0) {
			return null;
		} else if (id.getEvidences().size() == 1) {
			return id.getEvidences().getFirst();
		} else {
			LinkedList<double[]> evidences = new LinkedList<double[]>();
			int aCount = 0;
			int bCount = 0;
			double[] weights = new double[id.getEvidences().size()];
			int i = 0;
			for (double[] doubles : id.getEvidences()) {
				if (withoutEvidNum.equals((int)(doubles[0]))) {
					continue;
				}
				double[] evidence={doubles[0], doubles[1] * chro[i*2], doubles[2] * chro[(i*2)+1]};
				weights[i] = Math.pow(Math.pow(evidence[1], 2) + Math.pow(evidence[2], 2), 0.5) * Math.abs(evidence[1] - evidence[2]);
				if (evidence[1] > evidence[2]) {
					aCount++;
				} else if (evidence[1] < evidence[2]) {
					bCount++;
				}
				evidences.add(evidence);
				i++;
			}
			double count = 0;
			for (double weight : weights) {
				count += weight;
			}
			double[] ds = {0, 0, 0};
			i = 0;
			for (double[] evidence : evidences) {
				ds[1] += evidence[1] * (weights[i] * count) * ((double) (aCount) / (double) (aCount + bCount));
				ds[2] += evidence[2] * (weights[i] * count) * ((double) (bCount) / (double) (aCount + bCount));
				i++;
			}
			return ds;
		}
	}
}
