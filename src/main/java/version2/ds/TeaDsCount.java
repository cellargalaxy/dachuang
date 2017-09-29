//package version2.ds;
//
//import version2.dataSet.Id;
//
//import java.util.Iterator;
//
///**
// * Created by cellargalaxy on 17-9-9.
// */
//public class TeaEvidenceSynthesis implements EvidenceSynthesis {
//
//	public String getName() {
//		return "DS证据合成";
//	}
//
//	public double[] synthesisEvidence(Id id) {
//		if (id.getEvidences().size() == 0) {
//			return null;
//		} else {
//			Iterator<double[]> iterator = id.getEvidences().iterator();
//			double[] ds1 = iterator.next();
//			double[] ds2;
//			while (iterator.hasNext()) {
//				ds2 = iterator.next();
//				ds1 = countEvidence(ds1, ds2);
//			}
//			return ds1;
//		}
//	}
//
//	public double[] synthesisEvidence(Id id, Integer withoutEvidNum) {
//		if (id.getEvidences().size() == 0) {
//			return null;
//		} else {
//			Iterator<double[]> iterator = id.getEvidences().iterator();
//			double[] ds1;
//			double[] ds2;
//			do {
//				ds1 = iterator.next();
//				if (!withoutEvidNum.equals((int) (ds1[0]))) {
//					break;
//				}
//			}while (iterator.hasNext());
//			if (!iterator.hasNext()) {
//				return null;
//			}
//			do{
//				ds2 = iterator.next();
//				if (withoutEvidNum.equals((int) (ds2[0]))) {
//					continue;
//				}
//				ds1 = countEvidence(ds1, ds2);
//			}while (iterator.hasNext());
//			return ds1;
//		}
//	}
//
//	public double[] synthesisEvidenceIndex(Id id, double[] chro) {
//		if (id.getEvidences().size() == 0) {
//			return null;
//		} else {
//			Iterator<double[]> iterator = id.getEvidences().iterator();
//			double[] ds = iterator.next();
//			double[] ds1 = {ds[0], ds[1] * chro[2 * (int) (ds[0]) - 2], ds[2] * chro[2 * (int) (ds[0]) - 1]};
//			while (iterator.hasNext()) {
//				ds = iterator.next();
//				double[] ds2 = {ds[0], ds[1] * chro[2 * (int) (ds[0]) - 2], ds[2] * chro[2 * (int) (ds[0]) - 1]};
//				ds1 = countEvidence(ds1, ds2);
//			}
//			return ds1;
//		}
//	}
//
//	public double[] synthesisEvidenceOrder(Id id, double[] chro) {
//		if (id.getEvidences().size() == 0) {
//			return null;
//		} else {
//			Iterator<double[]> iterator = id.getEvidences().iterator();
//			int i = 0;
//			double[] ds = iterator.next();
//			double[] ds1 = {ds[0], ds[1] * chro[2 * i], ds[2] * chro[2 * i + 1]};
//			while (iterator.hasNext()) {
//				i++;
//				ds = iterator.next();
//				double[] ds2 = {ds[0], ds[1] * chro[2 * i], ds[2] * chro[2 * i + 1]};
//				ds1 = countEvidence(ds1, ds2);
//			}
//			return ds1;
//		}
//	}
//
//	public double[] synthesisEvidenceIndex(Id id, Integer withoutEvidNum, double[] chro) {
//		if (id.getEvidences().size() == 0) {
//			return null;
//		} else {
//			Iterator<double[]> iterator = id.getEvidences().iterator();
//			double[] ds;
//			do {
//				ds = iterator.next();
//				if (!withoutEvidNum.equals((int) (ds[0]))) {
//					break;
//				}
//			}while (iterator.hasNext());
//			if (!iterator.hasNext()) {
//				return null;
//			}
//			double[] ds1 = {ds[0], ds[1] * chro[2 * (int) (ds[0]) - 2], ds[2] * chro[2 * (int) (ds[0]) - 1]};
//			do{
//				ds = iterator.next();
//				if (withoutEvidNum.equals((int) (ds[0]))) {
//					continue;
//				}
//				double[] ds2 = {ds[0], ds[1] * chro[2 * (int) (ds[0]) - 2], ds[2] * chro[2 * (int) (ds[0]) - 1]};
//				ds1 = countEvidence(ds1, ds2);
//			}while (iterator.hasNext());
//			return ds1;
//		}
//	}
//
//	private final double[] countEvidence(double[] d1, double[] d2) {
//		double[] ds = new double[3];
//		double k = countK(d1, d2);
//		ds[1] = countA(d1, d2, k);
//		ds[2] = countB(d1, d2, k);
//		return ds;
//	}
//
//	private final double countA(double[] d1, double[] d2, double k) {
//		return (d1[1] * d2[1] + d1[1] * (1 - d2[1] - d2[2]) + d2[1] * (1 - d1[1] - d1[2])) / k;
//	}
//
//	private final double countB(double[] d1, double[] d2, double k) {
//		return (d1[2] * d2[2] + d1[2] * (1 - d2[1] - d2[2]) + d2[2] * (1 - d1[1] - d1[2])) / k;
//	}
//
//	private final double countK(double[] d1, double[] d2) {
//		return 1 - d1[1] * d2[2] - d1[2] * d2[1];
//	}
//}
