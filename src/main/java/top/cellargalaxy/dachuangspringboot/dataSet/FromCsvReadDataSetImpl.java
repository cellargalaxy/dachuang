package top.cellargalaxy.dachuangspringboot.dataSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import top.cellargalaxy.dachuangspringboot.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
public class FromCsvReadDataSetImpl implements FromFileReadDataSet {
	public static final int FRAUD_INDEX = 0;
	public static final int UNFRAUD_INDEX = 1;

	public static void main(String[] args) throws IOException {
		FromFileReadDataSet fromFileReadDataSet = new FromCsvReadDataSetImpl();
		DataSetParameter dataSetParameter = new DataSetParameter();
		dataSetParameter.setLabelColumnName("collusion_transaction");
		DataSet dataSet = fromFileReadDataSet.readDataSetFromFile(new File("D:/g/8000+交易 获取满证据 9证据-所有证据数据 - 副本.csv"), dataSetParameter);
		for (Id id : dataSet.getIds()) {
			System.out.println(id);
		}
	}

	@Override
	public DataSet readDataSetFromFile(File file, DataSetParameter dataSetParameter) throws IOException {
		try (CSVParser csvParser = CSVFormat.EXCEL.withHeader().parse(IOUtils.getReader(file))) {
			List<CSVRecord> csvRecords = csvParser.getRecords();
			Map<String, Id> idMap = new HashMap<>(csvRecords.size());
			Map<String, Integer> evidenceName2EvidenceId = new HashMap<>();

			for (CSVRecord csvRecord : csvRecords) {
				String idString = csvRecord.get(dataSetParameter.getIdColumnName());
				String evidenceNameString = csvRecord.get(dataSetParameter.getEvidenceColumnName());
				String fraudString = csvRecord.get(dataSetParameter.getFraudColumnName());
				String unfraudString = csvRecord.get(dataSetParameter.getUnfraudColumnName());
				String labelString = csvRecord.get(dataSetParameter.getLabelColumnName());
				if (dataSetParameter.getWithoutEvidences().contains(evidenceNameString)) {
					continue;
				}
				Integer evidenceId = evidenceName2EvidenceId.get(evidenceNameString);
				if (evidenceId == null) {
					evidenceId = evidenceName2EvidenceId.size() + 1;
					evidenceName2EvidenceId.put(evidenceNameString, evidenceId);
				}
				double fraud = Double.valueOf(fraudString);
				double unfraud = Double.valueOf(unfraudString);
				Evidence evidence = new Evidence(evidenceId, evidenceNameString, new double[]{fraud, unfraud});

				Id id = idMap.get(idString);
				if (id == null) {
					int label = Integer.valueOf(labelString);
					id = new Id(idString, new HashMap<>(), label);
					idMap.put(idString, id);
				}
				id.putEvidence(evidence);
			}

			return new DataSet(idMap, evidenceName2EvidenceId);
		}
	}
}
