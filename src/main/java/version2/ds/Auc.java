package version2.ds;

import util.CloneObject;
import version2.dataSet.DataSet;
import version2.dataSet.Id;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class Auc implements AucCount{
	private final DsCount dsCount;
	
	public Auc(DsCount dsCount) {
		this.dsCount = dsCount;
	}
	
	public double countAuc(DataSet dataSet) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = dsCount.countDs(id);
			if (id.getLabel()== Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s,ds1s);
	}
	
	public double countAuc(DataSet dataSet, Integer withoutEvidNum) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = dsCount.countDs(id,withoutEvidNum);
			if (id.getLabel()==Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s,ds1s);
	}
	
	public double countAuc(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException {
		DataSet newDataSet= CloneObject.clone(dataSet);
		newDataSet.removeNotEqual(withEvidNums);
		return countAuc(newDataSet);
	}
	
	public double countIndexAuc(DataSet dataSet, double[] chro) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = dsCount.countIndexDs(id,chro);
			if (id.getLabel()== Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s,ds1s);
	}
	
	public double countOrderAuc(DataSet cloneDataSet, double[] chro) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : cloneDataSet.getIds()) {
			double[] ds = dsCount.countOrderDs(id,chro);
			if (id.getLabel()== Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s,ds1s);
	}
	
	public double countIndexAuc(DataSet dataSet, Integer withoutEvidNum, double[] chro) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : dataSet.getIds()) {
			double[] ds = dsCount.countIndexDs(id,withoutEvidNum,chro);
			if (id.getLabel()== Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		return doCountAuc(ds0s,ds1s);
	}
	
	public double countOrderAuc(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException {
		DataSet newDataSet= CloneObject.clone(dataSet);
		newDataSet.removeNotEqual(withEvidNums);
		return countOrderAuc(newDataSet,chro);
	}
	
	
	private final double doCountAuc(LinkedList<double[]> ds0s, LinkedList<double[]> ds1s){
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
}
