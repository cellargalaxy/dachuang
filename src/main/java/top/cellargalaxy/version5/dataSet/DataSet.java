package top.cellargalaxy.version5.dataSet;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import top.cellargalaxy.version5.evaluation.Evaluation;
import top.cellargalaxy.version5.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.version5.hereditary.ParentChrosChoose;
import top.cellargalaxy.version5.subSpace.SubSpaceCreate;

import java.io.*;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class DataSet implements Serializable {
	public static final String CSV_Record_SEPARATOR = "\n";
	
	private final LinkedList<Id> ids;
	private final LinkedList<Integer> evidenceNums;
	private final Map<String, Integer> evidNameToId;
	
	public DataSet(LinkedList<Id> ids, LinkedList<Integer> evidenceNums, Map<String, Integer> evidNameToId) {
		this.ids = ids;
		this.evidenceNums = evidenceNums;
		this.evidNameToId = evidNameToId;
	}
	
	public DataSet(File dataSetFile, DataSetParameter dataSetParameter) throws IOException {
		ids = new LinkedList<Id>();
		evidenceNums = new LinkedList<Integer>();
		evidNameToId = new HashMap<String, Integer>();
		createIds(dataSetFile, dataSetParameter);
	}
	
	private final void createIds(File dataSetFile, DataSetParameter dataSetParameter) throws IOException {
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(new BufferedReader(new InputStreamReader(new FileInputStream(dataSetFile), dataSetParameter.getCoding())));
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
	
	public final void removeNotEqual(List<Integer> evidences) {
		Iterator<Id> iteratorId = ids.iterator();
		while (iteratorId.hasNext()) {
			Id id = iteratorId.next();
			if (id.getEvidences().size() < evidences.size()) {
				iteratorId.remove();
				continue;
			}
			Iterator<double[]> iteratorEvi = id.getEvidences().iterator();
			while (iteratorEvi.hasNext()) {
				if (!evidences.contains((int) (iteratorEvi.next()[0]))) {
					iteratorEvi.remove();
				}
				if (id.getEvidences().size() < evidences.size()) {
					iteratorId.remove();
					break;
				}
			}
		}
		Iterator<Integer> iteratorEvidenceNums = evidenceNums.iterator();
		while (iteratorEvidenceNums.hasNext()) {
			int evi = iteratorEvidenceNums.next();
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
	
	public LinkedList<Id> getIds() {
		return ids;
	}
	
	public LinkedList<Integer> getEvidenceNums() {
		return evidenceNums;
	}
	
	public Map<String, Integer> getEvidNameToId() {
		return evidNameToId;
	}
	
	public static final JSONObject outputDataSet(DataSetParameter dataSetParameter, DataSet subDataSet,
	                                             DataSetSeparation dataSetSeparation, DataSet dataSet, EvidenceSynthesis evidenceSynthesis,
	                                             Evaluation evaluation,
	                                             ParentChrosChoose parentChrosChoose,
	                                             SubSpaceCreate subSpaceCreate,
	                                             Map<DataSet, double[]> subSpaceMap,
	                                             double subSpaceAuc) throws IOException {
		JSONObject outputDataSet = new JSONObject();
		
		JSONArray datas = new JSONArray();
		for (Id id : subDataSet.getIds()) {
			double[] doubles = evidenceSynthesis.synthesisEvidence(id);
			JSONObject data = new JSONObject();
			data.put("id", id.getId());
			data.put("A", doubles[1]);
			data.put("B", doubles[2]);
			data.put("AB", 1 - doubles[1] - doubles[2]);
			data.put("label", id.getLabel());
			datas.put(data);
		}
		outputDataSet.put("datas", datas);
		
		outputDataSet.put("com0Count", dataSetSeparation.getCom0Count());
		outputDataSet.put("com1Count", dataSetSeparation.getCom1Count());
		outputDataSet.put("miss0Count", dataSetSeparation.getMiss0Count());
		outputDataSet.put("miss1Count", dataSetSeparation.getMiss1Count());
		
		outputDataSet.put("test", dataSetSeparation.getTest());
		outputDataSet.put("trainMiss", dataSetSeparation.getTrainMiss());
		outputDataSet.put("testMiss", dataSetSeparation.getTestMiss());
		outputDataSet.put("label1", dataSetSeparation.getLabel1());
		
		Map<String, Integer> evidenceCountMap = new HashMap<String, Integer>();
		for (Id id : dataSet.getIds()) {
			List<Integer> evidNums = new LinkedList<Integer>();
			for (double[] doubles : id.getEvidences()) {
				evidNums.add((int) doubles[0]);
			}
			Collections.sort(evidNums);
			String string;
			if (id.getLabel() == Id.LABEL_0) {
				string = evidNums.size() + "-0" + evidNums.toString();
			} else {
				string = evidNums.size() + "-1" + evidNums.toString();
			}
			Integer count = evidenceCountMap.get(string);
			if (count == null) {
				count = new Integer(0);
			}
			evidenceCountMap.put(string, count + 1);
		}
		JSONArray counts=new JSONArray();
		for (Map.Entry<String, Integer> entry : evidenceCountMap.entrySet()) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("count",entry.getKey());
			jsonObject.put("pro",(double) entry.getValue() / dataSet.getIds().size());
			counts.put(jsonObject);
		}
		outputDataSet.put("counts", counts);
		
		JSONArray evidNameToIds = new JSONArray();
		for (Map.Entry<String, Integer> entry : dataSet.evidNameToId.entrySet()) {
			JSONObject evidNameToId = new JSONObject();
			evidNameToId.put("evidId", entry.getValue());
			evidNameToId.put("evidName", entry.getKey());
			evidNameToIds.put(evidNameToId);
		}
		outputDataSet.put("evidNameToIds", evidNameToIds);
		
		outputDataSet.put("evaluation", evaluation.toString());
		
		outputDataSet.put("parentChrosChoose", parentChrosChoose.toString());
		
		outputDataSet.put("subSpaceCreate", subSpaceCreate.toString());
		
		JSONArray subSpaces = new JSONArray();
		for (Map.Entry<DataSet, double[]> entry : subSpaceMap.entrySet()) {
			JSONObject subSpace = new JSONObject();
			subSpace.put("subSpace", entry.getKey().getEvidenceNums());
			subSpace.put("subSpaceAUC", evaluation.countOrderEvaluation(entry.getKey(), entry.getValue()));
			subSpace.put("chro", Arrays.toString(entry.getValue()));
			subSpaces.put(subSpace);
		}
		outputDataSet.put("subSpaces", subSpaces);
		
		outputDataSet.put("AUC", subSpaceAuc);
		
		return outputDataSet;
	}
	
	public static final void outputDataSet(File outputDataSet, DataSetParameter dataSetParameter, DataSet subDataSet,
	                                       DataSetSeparation dataSetSeparation, DataSet dataSet, EvidenceSynthesis evidenceSynthesis,
	                                       Evaluation evaluation,
	                                       ParentChrosChoose parentChrosChoose,
	                                       SubSpaceCreate subSpaceCreate,
	                                       Map<DataSet, double[]> subSpaceMap,
	                                       double subSpaceAuc) throws IOException {
		outputDataSet.getParentFile().mkdirs();
		CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(CSV_Record_SEPARATOR);
		CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(outputDataSet), dataSetParameter.getCoding()), csvFormat);
		
		csvPrinter.printRecord("id", "A", "B", "AB", "label");
		for (Id id : subDataSet.getIds()) {
			double[] doubles = evidenceSynthesis.synthesisEvidence(id);
			csvPrinter.printRecord(id.getId(), doubles[1], doubles[2], 1 - doubles[1] - doubles[2], id.getLabel());
		}
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("完整0标签个数", "完整1标签个数", "缺失0标签个数", "缺失1标签个数");
		csvPrinter.printRecord(dataSetSeparation.getCom0Count(), dataSetSeparation.getCom1Count(), dataSetSeparation.getMiss0Count(), dataSetSeparation.getMiss1Count());
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("测试集比例", "训练集缺失比例", "测试集缺失比例", "1标签比例");
		csvPrinter.printRecord(dataSetSeparation.getTest(), dataSetSeparation.getTrainMiss(), dataSetSeparation.getTestMiss(), dataSetSeparation.getLabel1());
		
		Map<String, Integer> evidenceCountMap = new HashMap<String, Integer>();
		for (Id id : dataSet.getIds()) {
			List<Integer> evidNums = new LinkedList<Integer>();
			for (double[] doubles : id.getEvidences()) {
				evidNums.add((int) doubles[0]);
			}
			Collections.sort(evidNums);
			String string;
			if (id.getLabel() == Id.LABEL_0) {
				string = evidNums.size() + "个特征0标签" + evidNums.toString();
			} else {
				string = evidNums.size() + "个特征1标签" + evidNums.toString();
			}
			Integer count = evidenceCountMap.get(string);
			if (count == null) {
				count = new Integer(0);
			}
			evidenceCountMap.put(string, count + 1);
		}
		csvPrinter.printRecord();
		for (Map.Entry<String, Integer> entry : evidenceCountMap.entrySet()) {
			csvPrinter.printRecord(entry.getKey(), (double) entry.getValue() / dataSet.getIds().size());
		}
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("证据编号", "证据名");
		for (Map.Entry<String, Integer> entry : dataSet.evidNameToId.entrySet()) {
			csvPrinter.printRecord(entry.getValue(), entry.getKey());
		}
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("评估方法", evaluation.toString());
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("父母染色体选择算法", parentChrosChoose.toString());
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("子空间创建", subSpaceCreate.toString());
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("子空间", "子空间AUC", "染色体");
		for (Map.Entry<DataSet, double[]> entry : subSpaceMap.entrySet()) {
			csvPrinter.printRecord(entry.getKey().getEvidenceNums(), evaluation.countOrderEvaluation(entry.getKey(), entry.getValue()), Arrays.toString(entry.getValue()));
		}
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("AUC", subSpaceAuc);
		
		csvPrinter.close();
	}
}
