package version2.auc;

import version2.dataSet.Id;

import java.util.Iterator;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class TeaDsCount implements DsCount {
	
	public double[] countDs(Id id, double[] chro, Integer withoutEvidNum) {
		if (id.getEvidences().size() == 0) {
			return null;
		} else {
			Iterator<double[]> iterator = id.getEvidences().iterator();
			int i = 0;
			double[] ds;
			do {
				ds = iterator.next();
			}while (!withoutEvidNum.equals(ds[0]));
			double[] ds1 = {ds[0], ds[1] * chro[i*2], ds[2] * chro[(i*2)+1]};
			while (iterator.hasNext()) {
				i++;
				do {
					ds = iterator.next();
				}while (!withoutEvidNum.equals(ds[0]));
				double[] ds2={ds[0], ds[1] * chro[i*2], ds[2] * chro[(i*2)+1]};
				ds1 = countEvidence(ds1, ds2);
			}
			return ds1;
		}
	}
	
	public double[] countDs(Id id, double[] chro) {
		if (id.getEvidences().size() == 0) {
			return null;
		} else {
			Iterator<double[]> iterator = id.getEvidences().iterator();
			int i = 0;
			double[] ds = iterator.next();
			double[] ds1 = {ds[0], ds[1] * chro[i*2], ds[2] * chro[(i*2)+1]};
			while (iterator.hasNext()) {
				i++;
				ds = iterator.next();
				double[] ds2={ds[0], ds[1] * chro[i*2], ds[2] * chro[(i*2)+1]};
				ds1 = countEvidence(ds1, ds2);
			}
			return ds1;
		}
	}
	
	
	
	/**
	 * 某两条证据合成
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	private double[] countEvidence(double[] d1, double[] d2) {
		double[] ds = new double[3];
		double k = countK(d1, d2);
		ds[1] = countA(d1, d2, k);
		ds[2] = countB(d1, d2, k);
		return ds;
	}
	
	private double countA(double[] d1, double[] d2, double k) {
		return (d1[1] * d2[1] + d1[1] * (1 - d2[1] - d2[2]) + d2[1] * (1 - d1[1] - d1[2])) / k;
	}
	
	private double countB(double[] d1, double[] d2, double k) {
		return (d1[2] * d2[2] + d1[2] * (1 - d2[1] - d2[2]) + d2[2] * (1 - d1[1] - d1[2])) / k;
	}
	
	private double countK(double[] d1, double[] d2) {
		return 1 - d1[1] * d2[2] - d1[2] * d2[1];
	}
}
