package dataSet;


import auc.AUC;

import java.io.*;
import java.util.*;

/**
 * Created by cellargalaxy on 2017/4/22.
 * 数据集对象类
 * ids：嫌疑犯的链表
 * evidenceCount：对于某个嫌疑犯而言，最多有多少个证据
 * evidNameToId：各个证据的证据名与证据Id的Map
 */
public class DataSet implements Serializable{
	private LinkedList<Id> ids;
	private int evidenceCount;
	private Map<String, Integer> evidNameToId;

	public static void main(String[] args) throws IOException {
		DataSet dataSet=new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/特征选择.csv"),
				",",0,2,3,5,6);
		System.out.println(AUC.countAUCWithout(dataSet,-1));
	}
	
	public DataSet(File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol) throws IOException {
		evidenceCount = -1;
		evidNameToId = new HashMap<String, Integer>();
		ids = createIds(dataSetFile, separator, idClo, ACol, BCol, evidCol, labelCol);
	}
	
	public DataSet(LinkedList<Id> ids, int evidenceCount, Map<String, Integer> evidNameToId) {
		this.ids = ids;
		this.evidenceCount = evidenceCount;
		this.evidNameToId = evidNameToId;
	}

	public void allSaveEvidence(LinkedList<Integer> evidenceNums){
		Iterator<Id> iteratorId=ids.iterator();
		while (iteratorId.hasNext()) {
			Id id=iteratorId.next();
			int count=0;
			Iterator<double[]> iteratorEvidence=id.getEvidences().iterator();
			w2:while (iteratorEvidence.hasNext()) {
				double[] evidence=iteratorEvidence.next();
				for (Integer evidenceNum : evidenceNums) {
					if (evidenceNum.equals((int)evidence[0])) {
						count++;
						continue w2;
					}
				}
				iteratorEvidence.remove();
			}
			if (count!=evidenceNums.size()) {
				iteratorId.remove();
			}
		}
		evidenceCount=evidenceNums.size();
	}

	/**
	 * 数据集的各个对象乘以染色体
	 * @param chro
	 */
	public void mulChro(double[] chro){
		for (Id id : ids) {
			int i=0;
			for (double[] evidence : id.getEvidences()) {
				evidence[1] *= chro[i*2];
				evidence[2] *= chro[i*2+1];
				i++;
			}
		}
	}

//	public void removeMissingId(){
//		Iterator<Id> iterator=ids.iterator();
//		while (iterator.hasNext()) {
//			if (iterator.next().getEvidences().size()<evidenceCount) {
//				iterator.remove();
//			}
//		}
//	}
	
	private LinkedList<Id> createIds(File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol) throws IOException {
		LinkedList<Id> ids = new LinkedList<Id>();
		BufferedReader reader = new BufferedReader(new FileReader(dataSetFile));
		
		String id = null;
		int label = -1;
		LinkedList<double[]> evidences = null;
		
		String string;
		while ((string = reader.readLine()) != null) {
			String[] strings = string.split(separator);
			
			if (id == null) {
				id = strings[idClo];
				label = new Integer(strings[labelCol]);
				evidences = new LinkedList<double[]>();
			} else if (!id.equals(strings[idClo])) {
				if (evidences.size() > evidenceCount) evidenceCount = evidences.size();
				ids.add(new Id(id, evidences, label));
				
				id = strings[idClo];
				label = new Integer(strings[labelCol]);
				evidences = new LinkedList<double[]>();
			}
			
			double[] evidence = {createEvidNum(strings[evidCol]), new Double(strings[ACol]), new Double(strings[BCol])};
			evidences.add(evidence);
		}
		if (id != null) {
			if (evidences.size() > evidenceCount) evidenceCount = evidences.size();
			ids.add(new Id(id, evidences, label));
		}
		return ids;
	}
	
	private int createEvidNum(String evidName) {
		Integer i = evidNameToId.get(evidName);
		if (i == null) {
			i = evidNameToId.size()+1;
			evidNameToId.put(evidName, i);
		}
		return i;
	}
	
	public LinkedList<Id> getIds() {
		return ids;
	}
	
	public void setIds(LinkedList<Id> ids) {
		this.ids = ids;
	}
	
	public int getEvidenceCount() {
		return evidenceCount;
	}
	
	public void setEvidenceCount(int evidenceCount) {
		this.evidenceCount = evidenceCount;
	}
	
	public Map<String, Integer> getEvidNameToId() {
		return evidNameToId;
	}
	
	public void setEvidNameToId(Map<String, Integer> evidNameToId) {
		this.evidNameToId = evidNameToId;
	}
}
