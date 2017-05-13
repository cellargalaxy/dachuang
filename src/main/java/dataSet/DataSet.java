package dataSet;

import auc.Id;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/4/22.
 */
public class DataSet {
	
	
	public static LinkedList<Id> createIds(File dataSetFile, String separator,int idClo,int ACol,int BCol,int evidCol,int labelCol) throws IOException {
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
				if (lastIdString != null) ids.add(new Id(id, evidences,label));
				
				id++;
				lastIdString=strings[idClo];
				label=new Integer(strings[labelCol]);
				evidences=new LinkedList<double[]>();
			}
			double[] evidence={new Double(strings[evidCol].substring(1)),new Double(strings[ACol]),new Double(strings[BCol])};
			evidences.add(evidence);
		}
		if (lastIdString != null) ids.add(new Id(id, evidences,label));
		return ids;
	}
	
	
}
