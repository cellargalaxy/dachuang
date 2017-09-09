package version2.dataSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import version2.ds.DsCount;

import java.io.*;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class DataSet implements Serializable{
	private final LinkedList<Id> ids;
	private final LinkedList<Integer> evidenceNums;
	private final Map<String, Integer> evidNameToId;
	
	public DataSet(File dataSetFile,DataSetParameter dataSetParameter) throws IOException {
		ids = new LinkedList<Id>();
		evidenceNums = new LinkedList<Integer>();
		evidNameToId = new HashMap<String, Integer>();
		createIds(dataSetFile,dataSetParameter);
	}
	
	public DataSet(LinkedList<Id> ids, LinkedList<Integer> evidenceNums, Map<String, Integer> evidNameToId) {
		this.ids = ids;
		this.evidenceNums = evidenceNums;
		this.evidNameToId = evidNameToId;
	}
	
	public final void removeNotEqual(List<Integer> evidences) {
		Iterator<Id> iteratorId = ids.iterator();
		while (iteratorId.hasNext()) {
			Id id = iteratorId.next();
			if (id.getEvidences().size()<evidences.size()) {
				iteratorId.remove();
				continue;
			}
			Iterator<double[]> iteratorEvi=id.getEvidences().iterator();
			while (iteratorEvi.hasNext()) {
				if (!evidences.contains((int)(iteratorEvi.next()[0]))) {
					iteratorEvi.remove();
				}
				if (id.getEvidences().size()<evidences.size()) {
					iteratorId.remove();
					break;
				}
			}
		}
		Iterator<Integer> iteratorEvidenceNums=evidenceNums.iterator();
		while (iteratorEvidenceNums.hasNext()) {
			int evi=iteratorEvidenceNums.next();
			if (!evidences.contains(evi)) {
				iteratorEvidenceNums.remove();
			}
		}
		Iterator iterator = evidNameToId.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (!evidences.contains(entry.getValue())) {
				iterator.remove();
			}
		}
	}
	
	public final void removeEvidence(int evidenceNum) {
		for (Id id : ids) {
			Iterator<double[]> iterator = id.getEvidences().iterator();
			while (iterator.hasNext()) {
				if ((int) (iterator.next()[0]) == evidenceNum) {
					iterator.remove();
					break;
				}
			}
		}
		evidenceNums.remove(new Integer(evidenceNum));/////////////////////////
		Iterator iterator = evidNameToId.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (entry.getValue().equals(evidenceNum)) {
				iterator.remove();
				break;
			}
		}
	}
	
	private final void createIds(File dataSetFile, DataSetParameter dataSetParameter) throws IOException {
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(new BufferedReader(new InputStreamReader(new FileInputStream(dataSetFile),dataSetParameter.getCoding())));
		String id = null;
		int label = -1;
		LinkedList<double[]> evidences = null;
		for (CSVRecord record : records) {
			if (id == null) {
				id = record.get(dataSetParameter.getIdClo());
				label = new Integer(record.get(dataSetParameter.getLabelCol()));
				evidences = new LinkedList<double[]>();
			} else if (!id.equals(record.get(dataSetParameter.getIdClo()))) {
				ids.add(new Id(id, evidences, label));
				
				id = record.get(dataSetParameter.getIdClo());
				label = new Integer(record.get(dataSetParameter.getLabelCol()));
				evidences = new LinkedList<double[]>();
			}
			
			double[] evidence = {createEvidNum(record.get(dataSetParameter.getEvidCol())), new Double(record.get(dataSetParameter.getACol())), new Double(record.get(dataSetParameter.getBCol()))};
			evidences.add(evidence);
		}
		if (id != null) {
			ids.add(new Id(id, evidences, label));
		}
	}
	
	private final int createEvidNum(String evidName) {
		Integer i = evidNameToId.get(evidName);
		if (i == null) {
			i = evidNameToId.size() + 1;
			evidNameToId.put(evidName, i);
			evidenceNums.add(i);
		}
		return i;
	}
	
	public LinkedList<Id> getIds() {
		return ids;
	}
	
	public LinkedList<Integer> getEvidenceNums() {
		return evidenceNums;
	}
	
	public Map<String, Integer> getEvidNameToId() {
		return evidNameToId;
	}

}
