package dataSet;

import auc.Id;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/4/22.
 */
public class DataSet {
	private LinkedList<Id> ids;
	private int evidenceCount;
	
	public DataSet(File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol) throws IOException {
		evidenceCount=-1;
		ids=createIds(dataSetFile,separator,idClo,ACol,BCol,evidCol,labelCol);
	}
	
	public DataSet(LinkedList<Id> ids, int evidenceCount) {
		this.ids = ids;
		this.evidenceCount = evidenceCount;
	}
	
	private LinkedList<Id> createIds(File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol) throws IOException {
		LinkedList<Id> ids = new LinkedList<Id>();
		BufferedReader reader = new BufferedReader(new FileReader(dataSetFile));
		
		int id = 0;
		String lastIdString=null;
		int label=-1;
		LinkedList<double[]> evidences = null;
		
		String string;
		while ((string = reader.readLine()) != null) {
			String[] strings = string.split(separator);
			
			if (lastIdString==null||!lastIdString.equals(strings[idClo])) {
				if (lastIdString != null) {
					if(evidences.size()>evidenceCount) evidenceCount=evidences.size();
					ids.add(new Id(id, evidences,label));
				}
				
				id++;
				lastIdString=strings[idClo];
				label=new Integer(strings[labelCol]);
				evidences=new LinkedList<double[]>();
			}
			double[] evidence={new Double(strings[evidCol].substring(1)),new Double(strings[ACol]),new Double(strings[BCol])};
			evidences.add(evidence);
		}
		if (lastIdString != null) {
			if(evidences.size()>evidenceCount) evidenceCount=evidences.size();
			ids.add(new Id(id, evidences,label));
		}
		return ids;
	}
	
	public int getEvidenceCount() {
		return evidenceCount;
	}
	
	public void setEvidenceCount(int evidenceCount) {
		this.evidenceCount = evidenceCount;
	}
	
	public LinkedList<Id> getIds() {
		return ids;
	}
	
	public void setIds(LinkedList<Id> ids) {
		this.ids = ids;
	}
}
