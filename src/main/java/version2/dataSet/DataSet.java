//package version2.dataSet;
//
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVPrinter;
//import org.apache.commons.csv.CSVRecord;
//import version2.ds.Evaluation;
//import version2.ds.EvidenceSynthesis;
//import version2.run.RunParameter;
//import version4.dataSet.DataSetParameter;
//import version4.dataSet.Id;
//
//import java.io.*;
//import java.util.*;
//
///**
// * Created by cellargalaxy on 17-9-7.
// */
//public class DataSet implements Serializable{
//	public static final String CSV_Record_SEPARATOR="\n";
//
//	private final LinkedList<Id> ids;
//	private final LinkedList<Integer> evidenceNums;
//	private final Map<String, Integer> evidNameToId;
//
//	public DataSet(File dataSetFile,DataSetParameter dataSetParameter) throws IOException {
//		ids = new LinkedList<Id>();
//		evidenceNums = new LinkedList<Integer>();
//		evidNameToId = new HashMap<String, Integer>();
//		createIds(dataSetFile,dataSetParameter);
//	}
//
//	private final void createIds(File dataSetFile, DataSetParameter dataSetParameter) throws IOException {
//		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(new BufferedReader(new InputStreamReader(new FileInputStream(dataSetFile),dataSetParameter.getCoding())));
//		String id = null;
//		int label = -1;
//		LinkedList<double[]> evidences = null;
//		for (CSVRecord record : records) {
//			if (id == null) {
//				id = record.get(dataSetParameter.getIdClo());
//				label = new Integer(record.get(dataSetParameter.getLabelCol()));
//				evidences = new LinkedList<double[]>();
//			} else if (!id.equals(record.get(dataSetParameter.getIdClo()))) {
//				ids.add(new Id(id, evidences, label));
//
//				id = record.get(dataSetParameter.getIdClo());
//				label = new Integer(record.get(dataSetParameter.getLabelCol()));
//				evidences = new LinkedList<double[]>();
//			}
//
//			double[] evidence = {createEvidNum(record.get(dataSetParameter.getEvidCol())), new Double(record.get(dataSetParameter.getACol())), new Double(record.get(dataSetParameter.getBCol()))};
//			evidences.add(evidence);
//		}
//		if (id != null) {
//			ids.add(new Id(id, evidences, label));
//		}
//	}
//
//	private final int createEvidNum(String evidName) {
//		Integer i = evidNameToId.get(evidName);
//		if (i == null) {
//			i = evidNameToId.size() + 1;
//			evidNameToId.put(evidName, i);
//			evidenceNums.add(i);
//		}
//		return i;
//	}
//
//	public DataSet(LinkedList<Id> ids, LinkedList<Integer> evidenceNums, Map<String, Integer> evidNameToId) {
//		this.ids = ids;
//		this.evidenceNums = evidenceNums;
//		this.evidNameToId = evidNameToId;
//	}
//
//	public final void removeNotEqual(List<Integer> evidences) {
//		Iterator<Id> iteratorId = ids.iterator();
//		while (iteratorId.hasNext()) {
//			Id id = iteratorId.next();
//			if (id.getEvidences().size()<evidences.size()) {
//				iteratorId.remove();
//				continue;
//			}
//			Iterator<double[]> iteratorEvi=id.getEvidences().iterator();
//			while (iteratorEvi.hasNext()) {
//				if (!evidences.contains((int)(iteratorEvi.next()[0]))) {
//					iteratorEvi.remove();
//				}
//				if (id.getEvidences().size()<evidences.size()) {
//					iteratorId.remove();
//					break;
//				}
//			}
//		}
//		Iterator<Integer> iteratorEvidenceNums=evidenceNums.iterator();
//		while (iteratorEvidenceNums.hasNext()) {
//			int evi=iteratorEvidenceNums.next();
//			if (!evidences.contains(evi)) {
//				iteratorEvidenceNums.remove();
//			}
//		}
//		Iterator iterator = evidNameToId.entrySet().iterator();
//		while (iterator.hasNext()) {
//			Map.Entry entry = (Map.Entry) iterator.next();
//			if (!evidences.contains(entry.getValue())) {
//				iterator.remove();
//			}
//		}
//	}
//
//	public final void removeEvidence(int evidenceNum) {
//		for (Id id : ids) {
//			Iterator<double[]> iterator = id.getEvidences().iterator();
//			while (iterator.hasNext()) {
//				if ((int) (iterator.next()[0]) == evidenceNum) {
//					iterator.remove();
//					break;
//				}
//			}
//		}
//		evidenceNums.remove(new Integer(evidenceNum));/////////////////////////
//		Iterator iterator = evidNameToId.entrySet().iterator();
//		while (iterator.hasNext()) {
//			Map.Entry entry = (Map.Entry) iterator.next();
//			if (entry.getValue().equals(evidenceNum)) {
//				iterator.remove();
//				break;
//			}
//		}
//	}
//
//	public LinkedList<Id> getIds() {
//		return ids;
//	}
//
//	public LinkedList<Integer> getEvidenceNums() {
//		return evidenceNums;
//	}
//
//	public Map<String, Integer> getEvidNameToId() {
//		return evidNameToId;
//	}
//
//	public static final void outputDataSet(DataSet dataSet,DataSet subDataSet, File outputDataSet,RunParameter runParameter,
//	                                       double subSpaceAuc,EvidenceSynthesis dsCount,Evaluation aucCount,
//	                                       Map<String, Integer> evidNameToId,
//	                                       Map<Double, Integer> featureMap,
//	                                       Map<DataSet,double[]> subSpaceMap) throws IOException {
//		outputDataSet.getParentFile().mkdirs();
//		CSVFormat csvFormat =  CSVFormat.DEFAULT.withRecordSeparator(CSV_Record_SEPARATOR);
//		CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputDataSet), csvFormat);
//		csvPrinter.printRecord("id","label","A","B","AB");
//		for (Id id : subDataSet.getIds()) {
//			double[] doubles=dsCount.synthesisEvidence(id);
//			csvPrinter.printRecord(id.getId(),id.getLabel(),doubles[1],doubles[2],1-doubles[1]-doubles[2]);
//		}
//
//		csvPrinter.printRecord();
//		csvPrinter.printRecord("fnMin","sn","测试集比例","缺失集比例","标签1比例");
//		csvPrinter.printRecord(runParameter.getFnMin(),Arrays.toString(runParameter.getSn()),runParameter.getTest(),runParameter.getMiss(),runParameter.getLabel1());
//
//		Map<String,Integer> evidenceCountMap=new HashMap<String, Integer>();
//		for (Id id : dataSet.getIds()) {
//			List<Integer> evidNums=new LinkedList<Integer>();
//			for (double[] doubles : id.getEvidences()) {
//				evidNums.add((int)doubles[0]);
//			}
//			Collections.sort(evidNums);
//			String string;
//			if (id.getLabel()== Id.LABEL_0) {
//				string=evidNums.size()+"个特征0标签"+evidNums.toString();
//			}else {
//				string=evidNums.size()+"个特征1标签"+evidNums.toString();
//			}
//			Integer count=evidenceCountMap.get(string);
//			if (count==null) {
//				count=new Integer(0);
//			}
//			evidenceCountMap.put(string,count+1);
//		}
//		csvPrinter.printRecord();
//		for (Map.Entry<String, Integer> entry : evidenceCountMap.entrySet()) {
//			csvPrinter.printRecord(entry.getKey(),(double)entry.getValue()/dataSet.getIds().size());
//		}
//
//		csvPrinter.printRecord();
//		csvPrinter.printRecord("AUC",subSpaceAuc);
//
//		csvPrinter.printRecord();
//		csvPrinter.printRecord("证据编号","证据名");
//		for (Map.Entry<String, Integer> entry : evidNameToId.entrySet()) {
//			csvPrinter.printRecord(entry.getValue(),entry.getKey());
//		}
//
//		csvPrinter.printRecord();
//		csvPrinter.printRecord("证据合成",dsCount.getName());
//
//		csvPrinter.printRecord();
//		int len=featureMap.get(Double.MAX_VALUE);
//		int i=0;
//		csvPrinter.printRecord("重要特征");
//		for (Map.Entry<Double, Integer> entry : featureMap.entrySet()) {
//			csvPrinter.printRecord(entry.getValue(),entry.getKey());
//			i++;
//			if (i==len) {
//				csvPrinter.printRecord("不重要特征");
//			}
//		}
//
//		csvPrinter.printRecord();
//		csvPrinter.printRecord("子空间","子空间AUC","染色体");
//		for (Map.Entry<DataSet, double[]> entry : subSpaceMap.entrySet()) {
//			csvPrinter.printRecord(entry.getKey().getEvidenceNums(),aucCount.countOrderEvaluation(entry.getKey(),entry.getValue()),Arrays.toString(entry.getValue()));
//		}
//
//		csvPrinter.close();
//	}
//}
