//package version2.ds;
//
//import version2.dataSet.Id;
//
//import java.util.LinkedList;
//
///**
// * Created by cellargalaxy on 17-9-19.
// */
//public class VoteEvidenceSynthesis implements EvidenceSynthesis{
//	private final double thrf;
//	private final double thrnf;
//	private final double d1;
//	private final double d2;
//
//
//	public VoteEvidenceSynthesis(double thrf, double thrnf, double d1, double d2) {
//		this.thrf = thrf;
//		this.thrnf = thrnf;
//		this.d1 = d1;
//		this.d2 = d2;
//	}
//
//	public String getName() {
//		return "投票法证据合成";
//	}
//
//	public double[] synthesisEvidence(Id id) {
//		LinkedList<double[]> evidences = id.getEvidences();
//		double a = 0;
//		double b = 0;
//		for (double[] evidence : evidences) {
//			if (evidence[1] > thrf) {
//				a++;
//			} else if (evidence[2] < thrnf) {
//				b++;
//			}
//		}
//		double[] ds = {0, 0, 0};
//		if (a > b) {
//			ds[1]=(a - b) / (a + b) * d1;
//		} else if (a < b) {
//			ds[2]=(b - a) / (a + b) * d2;
//		}
//		return ds;
//	}
//
//	public double[] synthesisEvidence(Id id, Integer withoutEvidNum) {
//		LinkedList<double[]> evidences = id.getEvidences();
//		double a = 0;
//		double b = 0;
//		for (double[] evidence : evidences) {
//			if (withoutEvidNum.equals((int)(evidence[0]))) {
//				continue;
//			}
//			if (evidence[1] > thrf) {
//				a++;
//			} else if (evidence[2] < thrnf) {
//				b++;
//			}
//		}
//		double[] ds = {0, 0, 0};
//		if (a > b) {
//			ds[1]=(a - b) / (a + b) * d1;
//		} else if (a < b) {
//			ds[2]=(b - a) / (a + b) * d2;
//		}
//		return ds;
//	}
//
//	public double[] synthesisEvidenceIndex(Id id, double[] chro) {
//		LinkedList<double[]> evidences = id.getEvidences();
//		double a = 0;
//		double b = 0;
//		for (double[] evidence : evidences) {
//			if (evidence[1] * chro[2 * (int) (evidence[0]) - 2] > thrf) {
//				a++;
//			} else if (evidence[2] * chro[2 * (int) (evidence[0]) - 1] < thrnf) {
//				b++;
//			}
//		}
//		double[] ds = {0, 0, 0};
//		if (a > b) {
//			ds[1]=(a - b) / (a + b) * d1;
//		} else if (a < b) {
//			ds[2]=(b - a) / (a + b) * d2;
//		}
//		return ds;
//	}
//
//	public double[] synthesisEvidenceOrder(Id id, double[] chro) {
//		LinkedList<double[]> evidences = id.getEvidences();
//		double a = 0;
//		double b = 0;
//		int i=0;
//		for (double[] evidence : evidences) {
//			if (evidence[1] * chro[2 * i] > thrf) {
//				a++;
//			} else if (evidence[2] * chro[2 * i + 1] < thrnf) {
//				b++;
//			}
//			i++;
//		}
//		double[] ds = {0, 0, 0};
//		if (a > b) {
//			ds[1]=(a - b) / (a + b) * d1;
//		} else if (a < b) {
//			ds[2]=(b - a) / (a + b) * d2;
//		}
//		return ds;
//	}
//
//	public double[] synthesisEvidenceIndex(Id id, Integer withoutEvidNum, double[] chro) {
//		LinkedList<double[]> evidences = id.getEvidences();
//		double a = 0;
//		double b = 0;
//		for (double[] evidence : evidences) {
//			if (withoutEvidNum.equals((int)(evidence[0]))) {
//				continue;
//			}
//			if (evidence[1] * chro[2 * (int) (evidence[0]) - 2] > thrf) {
//				a++;
//			} else if (evidence[2] * chro[2 * (int) (evidence[0]) - 1] < thrnf) {
//				b++;
//			}
//		}
//		double[] ds = {0, 0, 0};
//		if (a > b) {
//			ds[1]=(a - b) / (a + b) * d1;
//		} else if (a < b) {
//			ds[2]=(b - a) / (a + b) * d2;
//		}
//		return ds;
//	}
//}
