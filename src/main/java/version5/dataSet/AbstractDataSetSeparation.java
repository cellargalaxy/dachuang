package version5.dataSet;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by cellargalaxy on 17-11-2.
 */
public abstract class AbstractDataSetSeparation extends DataSet implements DataSetSeparation {
	private int com0Count;
	private int com1Count;
	private int miss0Count;
	private int miss1Count;
	private double test;
	private double trainMiss;
	private double testMiss;
	private double label1;
	
	public AbstractDataSetSeparation(File dataSetFile, DataSetParameter dataSetParameter) throws IOException {
		super(new LinkedList<Id>(), new LinkedList<Integer>(), new LinkedHashMap<String, Integer>());
		createIds(dataSetFile, dataSetParameter);
		initCount();
	}
	
	abstract void createIds(File dataSetFile, DataSetParameter dataSetParameter) throws IOException;
	
	private final void initCount() {
		LinkedList<Id> ids = getIds();
		LinkedList<Integer> evidenceNums = getEvidenceNums();
		for (Id id : ids) {
			if (id.getEvidences().size() == evidenceNums.size()) {
				if (id.getLabel() == Id.LABEL_0) {
					com0Count++;
				} else if (id.getLabel() == Id.LABEL_1) {
					com1Count++;
				} else {
					throw new RuntimeException("数据集存在第三类标签:" + id.getLabel());
				}
			} else {
				if (id.getLabel() == Id.LABEL_0) {
					miss0Count++;
				} else if (id.getLabel() == Id.LABEL_1) {
					miss1Count++;
				} else {
					throw new RuntimeException("数据集存在第三类标签:" + id.getLabel());
				}
			}
		}
	}
	
	final int createEvidNum(String evidName) {
		LinkedList<Integer> evidenceNums = getEvidenceNums();
		Map<String, Integer> evidNameToId = getEvidNameToId();
		Integer i = evidNameToId.get(evidName);
		if (i == null) {
			i = evidNameToId.size() + 1;
			evidNameToId.put(evidName, i);
			evidenceNums.add(i);
		}
		return i;
	}
	
	public final DataSet[] separationDataSet(double test, double trainMiss, double testMiss, double label1) {
		this.test=test;
		this.trainMiss=trainMiss;
		this.testMiss=testMiss;
		this.label1=label1;
		return separationDataSet();
	}
	
	abstract DataSet[] separationDataSet();
	
	public int getCom0Count() {
		return com0Count;
	}
	
	public void setCom0Count(int com0Count) {
		this.com0Count = com0Count;
	}
	
	public int getCom1Count() {
		return com1Count;
	}
	
	public void setCom1Count(int com1Count) {
		this.com1Count = com1Count;
	}
	
	public int getMiss0Count() {
		return miss0Count;
	}
	
	public void setMiss0Count(int miss0Count) {
		this.miss0Count = miss0Count;
	}
	
	public int getMiss1Count() {
		return miss1Count;
	}
	
	public void setMiss1Count(int miss1Count) {
		this.miss1Count = miss1Count;
	}
	
	public double getTest() {
		return test;
	}
	
	public void setTest(double test) {
		this.test = test;
	}
	
	public double getTrainMiss() {
		return trainMiss;
	}
	
	public void setTrainMiss(double trainMiss) {
		this.trainMiss = trainMiss;
	}
	
	public double getTestMiss() {
		return testMiss;
	}
	
	public void setTestMiss(double testMiss) {
		this.testMiss = testMiss;
	}
	
	public double getLabel1() {
		return label1;
	}
	
	public void setLabel1(double label1) {
		this.label1 = label1;
	}
	
	@Override
	public String toString() {
		return "AbstractDataSetSeparation{" +
				"com0Count=" + com0Count +
				", com1Count=" + com1Count +
				", miss0Count=" + miss0Count +
				", miss1Count=" + miss1Count +
				'}';
	}
}
