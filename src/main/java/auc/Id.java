package auc;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 关于嫌疑犯对象的类，包括嫌疑犯的id以及对嫌疑犯的各条指控证据
 * 证据的数据结构：evidences的double[] ds=new double[3]：
 * ds[0]=证据编号，ds[1]=是（A）证据，ds[2]=否（B）证据
 * Created by cellargalaxy on 2017/4/22.
 */
public class Id implements Serializable{
	private int id;
	private LinkedList<double[]> evidences;
	private int label;
	
	public Id(int id, LinkedList<double[]> evidences, int label) {
		this.id = id;
		this.evidences = evidences;
		this.label = label;
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
				if (ds1[0] != m) break;
			}
			while (iterator.hasNext()) {
				ds2 = iterator.next();
				if (ds2[0] != m) ds1 = countE_E2E(ds1, ds2);
			}
//			if (ds1[1]>1) ds1[1]=1;
//			if (ds1[2]>1) ds1[2]=1;
//			if (ds1[1]<0) ds1[1]=0;
//			if (ds1[2]<0) ds1[2]=0;
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
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
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
	
	@Override
	public String toString() {
		return "Id{" +
				"id=" + id +
				", evidences=" + evidences +
				", label=" + label +
				'}';
	}
}
