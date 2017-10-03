package version3.dataSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import version3.evaluation.Evaluation;
import version3.evidenceSynthesis.EvidenceSynthesis;
import version3.feature.FeatureSeparation;
import version3.hereditary.ParentChrosChoose;
import version3.run.RunParameter;
import version3.subSpace.SubSpaceCreate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
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
	
	public static final void outputDataSet(DataSet dataSet, DataSet subDataSet, File outputDataSet, RunParameter runParameter,
	                                       double subSpaceAuc, EvidenceSynthesis evidenceSynthesis, Evaluation evaluation,
	                                       Map<String, Integer> evidNameToId,
	                                       ParentChrosChoose parentChrosChoose,
	                                       FeatureSeparation featureSeparation, double stop, Map<Double, Integer> featureMap,
	                                       SubSpaceCreate subSpaceCreate, Map<DataSet, double[]> subSpaceMap) throws IOException {
		outputDataSet.getParentFile().mkdirs();
		CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(CSV_Record_SEPARATOR);
		CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputDataSet), csvFormat);
		csvPrinter.printRecord("id", "label", "A", "B", "AB");
		for (Id id : subDataSet.getIds()) {
			double[] doubles = evidenceSynthesis.synthesisEvidence(id);
			csvPrinter.printRecord(id.getId(), id.getLabel(), doubles[1], doubles[2], 1 - doubles[1] - doubles[2]);
		}
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("fnMin", "sn", "测试集比例", "缺失集比例", "标签1比例");
		csvPrinter.printRecord(runParameter.getFnMin(), Arrays.toString(runParameter.getSn()), runParameter.getTest(), runParameter.getMiss(), runParameter.getLabel1());
		
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
		csvPrinter.printRecord("AUC", subSpaceAuc);
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("证据编号", "证据名");
		for (Map.Entry<String, Integer> entry : evidNameToId.entrySet()) {
			csvPrinter.printRecord(entry.getValue(), entry.getKey());
		}
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("证据合成", evidenceSynthesis.getName());
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("父母染色体选择算法", parentChrosChoose.getName());
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("特征选择", featureSeparation.getName());
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("特征选择stop", stop);
		
		csvPrinter.printRecord();
		int len = featureMap.get(Double.MAX_VALUE);
		int i = 0;
		csvPrinter.printRecord("重要特征");
		for (Map.Entry<Double, Integer> entry : featureMap.entrySet()) {
			csvPrinter.printRecord(entry.getValue(), entry.getKey());
			i++;
			if (i == len) {
				csvPrinter.printRecord("不重要特征");
			}
		}
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("子空间创建", subSpaceCreate.getName());
		
		csvPrinter.printRecord();
		csvPrinter.printRecord("子空间", "子空间AUC", "染色体");
		for (Map.Entry<DataSet, double[]> entry : subSpaceMap.entrySet()) {
			csvPrinter.printRecord(entry.getKey().getEvidenceNums(), evaluation.countOrderEvaluation(entry.getKey(), entry.getValue()), Arrays.toString(entry.getValue()));
		}
		
		csvPrinter.close();
	}
}
