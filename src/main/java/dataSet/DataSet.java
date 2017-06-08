package dataSet;

import auc.Id;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by cellargalaxy on 2017/4/22.
 * 数据集对象类
 * ids：嫌疑犯的链表
 * evidenceCount：对于某个嫌疑犯而言，最多有多少个证据
 * evidNameToId：各个证据的证据名与证据Id的Map
 */
public class DataSet {
	private LinkedList<Id> ids;
	private int evidenceCount;
	private Map<String, Integer> evidNameToId;
	
	
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

//			if (id==null||!id.equals(strings[idClo])) {
//				if (id != null) {
//					if(evidences.size()>evidenceCount) evidenceCount=evidences.size();
//					ids.add(new Id(id, evidences,label));
//				}
//
//				id++;
//				id=strings[idClo];
//				label=new Integer(strings[labelCol]);
//				evidences=new LinkedList<double[]>();
//			}
//			double[] evidence={new Double(strings[evidCol].substring(1)),new Double(strings[ACol]),new Double(strings[BCol])};
//			evidences.add(evidence);
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
			i = evidNameToId.size();
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
