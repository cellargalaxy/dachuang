package dataSet;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/4/22.
 * 嫌疑犯对象的类
 * id：嫌疑犯的id
 * evidences：对嫌疑犯的各条指控证据
 * 证据的数据结构：evidences的double[] ds=new double[3]：
 * ds[0]=证据编号，ds[1]=是（A）证据，ds[2]=否（B）证据
 * label：嫌疑犯的标签
 */
public class Id implements Serializable {
	public static final int LABEL_1 = 1;
	public static final int LABEL_0 = 0;
	private String id;
	private LinkedList<double[]> evidences;
	private int label;

	///////////////////////////////////////////
	private double[] subSpaceDS;
	
	public Id(String id, LinkedList<double[]> evidences, int label) {
		this.id = id;
		this.evidences = evidences;
		this.label = label;
	}
	
	/**
	 * 计算ms包含的证据的DS合成证据
	 * @param ms
	 * @return
	 */
	public double[] countDSWiths(LinkedList<Integer> ms) {
		if (evidences.size() == 0) {
			return null;
		} else if (evidences.size() == 1) {
			return evidences.getFirst();
		} else {
			Iterator<double[]> iterator = evidences.iterator();
			double[] ds1 = null;
			double[] ds2;
			while (iterator.hasNext()) {
				ds1 = iterator.next();
				if (!ms.contains((int)ds1[0])) {
					continue;
				}
				break;
			}
			while (iterator.hasNext()) {
				ds2 = iterator.next();
				if (!ms.contains((int)ds2[0])) {
					continue;
				}
				ds1 = countE_E2E(ds1, ds2);
			}
			return ds1;
		}
	}
	
	/**
	 * 计算ms包含的证据的DS合成证据
	 * @param ms
	 * @return
	 */
	public double[] countDSWiths(int[] ms) {
		if (evidences.size() == 0) {
			return null;
		} else if (evidences.size() == 1) {
			return evidences.getFirst();
		} else {
			Iterator<double[]> iterator = evidences.iterator();
			double[] ds1 = null;
			double[] ds2;
			main:while (iterator.hasNext()) {
				ds1 = iterator.next();
				for (int m : ms) {
					if (m==(int)ds1[0]) {
						break main;
					}
				}
			}
			while (iterator.hasNext()) {
				ds2 = iterator.next();
				for (int m : ms) {
					if (m==(int)ds2[0]) {
						ds1 = countE_E2E(ds1, ds2);
						break;
					}
				}
			}
			return ds1;
		}
	}

	/**
	 * 计算除了ms包含的证据的DS合成证据
	 * @param ms
	 * @return
	 */
	public double[] countDSWithouts(int[] ms) {
		if (evidences.size() == 0) {
			return null;
		} else if (evidences.size() == 1) {
			return evidences.getFirst();
		} else {
			Iterator<double[]> iterator = evidences.iterator();
			double[] ds1 = null;
			double[] ds2;
			main:while (iterator.hasNext()) {
				ds1 = iterator.next();
				for (int m : ms) {
					if (m==(int)ds1[0]) {
						continue main;
					}
				}
				break;
			}
			main:while (iterator.hasNext()) {
				ds2 = iterator.next();
				for (int m : ms) {
					if (m==(int)ds2[0]) {
						continue main;
					}
				}
				ds1 = countE_E2E(ds1, ds2);
			}
			return ds1;
		}
	}

	/**
	 * 计算除了ms包含的证据的DS合成证据
	 * @param ms
	 * @return
	 */
	public double[] countDSWithouts(LinkedList<Integer> ms) {
		if (evidences.size() == 0) {
			return null;
		} else if (evidences.size() == 1) {
			return evidences.getFirst();
		} else {
			Iterator<double[]> iterator = evidences.iterator();
			double[] ds1 = null;
			double[] ds2;
			while (iterator.hasNext()) {
				ds1 = iterator.next();
				if (ms.contains((int)ds1[0])) {
					continue;
				}
				break;
			}
			while (iterator.hasNext()) {
				ds2 = iterator.next();
				if (ms.contains((int)ds2[0])) {
					continue;
				}
				ds1 = countE_E2E(ds1, ds2);
			}
			return ds1;
		}
	}
	
	/**
	 * 计算除了证据m的DS合成证据
	 *
	 * @param m
	 * @return
	 */
	public double[] countDSWithout(int m) {
		if (evidences.size() == 0) {
			return null;
		} else if (evidences.size() == 1) {
			return evidences.getFirst();
		} else {
			Iterator<double[]> iterator = evidences.iterator();
			double[] ds1 = null;
			double[] ds2;
			while (iterator.hasNext()) {
				ds1 = iterator.next();
				if ((int)ds1[0] != m) break;
			}
			while (iterator.hasNext()) {
				ds2 = iterator.next();
				if ((int)ds2[0] != m) ds1 = countE_E2E(ds1, ds2);
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
	private double[] countE_E2E(double[] d1, double[] d2) {
		double[] ds = new double[3];
		double K = countK(d1, d2);
		ds[1] = countA(d1, d2, K);
		ds[2] = countB(d1, d2, K);
		return ds;
	}
	
	private double countA(double[] d1, double[] d2, double K) {
		return (d1[1] * d2[1] + d1[1] * (1 - d2[1] - d2[2]) + d2[1] * (1 - d1[1] - d1[2])) / K;
	}
	
	private double countB(double[] d1, double[] d2, double K) {
		return (d1[2] * d2[2] + d1[2] * (1 - d2[1] - d2[2]) + d2[2] * (1 - d1[1] - d1[2])) / K;
	}
	
	private double countK(double[] d1, double[] d2) {
		return 1 - d1[1] * d2[2] - d1[2] * d2[1];
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public LinkedList<double[]> getEvidences() {
		return evidences;
	}
	
	public void setEvidences(LinkedList<double[]> evidences) {
		this.evidences = evidences;
	}
	
	public int getLabel() {
		return label;
	}
	
	public void setLabel(int label) {
		this.label = label;
	}

	public double[] getSubSpaceDS() {
		return subSpaceDS;
	}

	public void setSubSpaceDS(double[] subSpaceDS) {
		this.subSpaceDS = subSpaceDS;
	}

	@Override
	public String toString() {
		return "Id{" +
				"id='" + id + '\'' +
				", evidences=" + evidences +
				", label=" + label +
				'}';
	}
}
