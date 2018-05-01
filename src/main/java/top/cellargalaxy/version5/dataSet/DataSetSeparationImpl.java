package top.cellargalaxy.version5.dataSet;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by cellargalaxy on 17-9-28.
 */
public final class DataSetSeparationImpl extends AbstractDataSetSeparation {
	
	public DataSetSeparationImpl(File dataSetFile, DataSetParameter dataSetParameter) throws IOException {
		super(dataSetFile, dataSetParameter);
	}
	
	public void createIds(File dataSetFile, DataSetParameter dataSetParameter) throws IOException {
		LinkedList<Id> ids = getIds();
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
	
	public DataSet[] separationDataSet() {
		double test = getTest();
		double trainMiss = getTrainMiss();
		double testMiss = getTestMiss();
		double label1 = getLabel1();
		
		LinkedList<Id> ids = getIds();
		LinkedList<Integer> evidenceNums = getEvidenceNums();
		Map<String, Integer> evidNameToId = getEvidNameToId();
		
		double com0Pro = (1 - (trainMiss * (1 - test) + testMiss * test)) * (1 - label1);
		double com1Pro = (1 - (trainMiss * (1 - test) + testMiss * test)) * label1;
		double miss0Pro = (trainMiss * (1 - test) + testMiss * test) * (1 - label1);
		double miss1Pro = (trainMiss * (1 - test) + testMiss * test) * label1;
		
		double com0ProD = 1.0 * getCom0Count() / ids.size() - com0Pro;
		double com1ProD = 1.0 * getCom1Count() / ids.size() - com1Pro;
		double miss0ProD = 1.0 * getMiss0Count() / ids.size() - miss0Pro;
		double miss1ProD = 1.0 * getMiss1Count() / ids.size() - miss1Pro;
		
		double minProD = Double.MAX_VALUE;
		if (getCom0Count() != 0 && com0ProD < minProD) {
			minProD = com1ProD;
		}
		if (getCom1Count() != 0 && com1ProD < minProD) {
			minProD = com1ProD;
		}
		if (getMiss0Count() != 0 && miss0ProD < minProD) {
			minProD = miss0ProD;
		}
		if (getMiss1Count() != 0 && miss1ProD < minProD) {
			minProD = miss1ProD;
		}
		
		int addCom0Count = -1;
		int addCom1Count = -1;
		int addMiss0Count = -1;
		int addMiss1Count = -1;
		if (com0ProD == minProD) {
			addCom0Count = getCom0Count();
			addCom1Count = (int) (addCom0Count / com0Pro * com1Pro);
			addMiss0Count = (int) (addCom0Count / com0Pro * miss0Pro);
			addMiss1Count = (int) (addCom0Count / com0Pro * miss1Pro);
		} else if (com1ProD == minProD) {
			addCom1Count = getCom1Count();
			addCom0Count = (int) (addCom1Count / com1Pro * com0Pro);
			addMiss0Count = (int) (addCom1Count / com1Pro * miss0Pro);
			addMiss1Count = (int) (addCom1Count / com1Pro * miss1Pro);
		} else if (miss0ProD == minProD) {
			addMiss0Count = getMiss0Count();
			addCom0Count = (int) (addMiss0Count / miss0Pro * com0Pro);
			addCom1Count = (int) (addMiss0Count / miss0Pro * com1Pro);
			addMiss1Count = (int) (addMiss0Count / miss0Pro * miss0Pro);
		} else if (minProD == minProD) {
			addMiss1Count = getMiss1Count();
			addCom0Count = (int) (addMiss1Count / miss1Pro * com0Pro);
			addCom1Count = (int) (addMiss1Count / miss1Pro * com1Pro);
			addMiss0Count = (int) (addMiss1Count / miss1Pro * miss0Pro);
		}
		int count = 0;
		if (getCom0Count() != 0) {
			count += addCom0Count;
		}
		if (getCom1Count() != 0) {
			count += addCom1Count;
		}
		if (getMiss0Count() != 0) {
			count += addMiss0Count;
		}
		if (getMiss1Count() != 0) {
			count += addMiss1Count;
		}
		
		int addTestCom0Count = (int) (count * test * (1 - testMiss) * (1 - label1));
		int addTestCom1Count = (int) (count * test * (1 - testMiss) * label1);
		int addTestMiss0Count = (int) (count * test * testMiss * (1 - label1));
		int addTestMiss1Count = (int) (count * test * testMiss * label1);
		
		int addTrainCom0Count = (int) (count * (1 - test) * (1 - trainMiss) * (1 - label1));
		int addTrainCom1Count = (int) (count * (1 - test) * (1 - trainMiss) * label1);
		int addTrainMiss0Count = (int) (count * (1 - test) * trainMiss * (1 - label1));
		int addTrainMiss1Count = (int) (count * (1 - test) * trainMiss * label1);
		
		double addTrainCom0Pro = 1.0 * addTrainCom0Count / addCom0Count;
		double addTrainCom1Pro = 1.0 * addTrainCom1Count / addCom1Count;
		double addTrainMiss0Pro = 1.0 * addTrainMiss0Count / addMiss0Count;
		double addTrainMiss1Pro = 1.0 * addTrainMiss1Count / addMiss1Count;
		
		int yetTestCom0Count = 0;
		int yetTestCom1Count = 0;
		int yetTestMiss0Count = 0;
		int yetTestMiss1Count = 0;
		
		int yetTrainCom0Count = 0;
		int yetTrainCom1Count = 0;
		int yetTrainMiss0Count = 0;
		int yetTrainMiss1Count = 0;
		
		LinkedList<Id> trainIds = new LinkedList<Id>();
		LinkedList<Id> testIds = new LinkedList<Id>();
		for (Id id : ids) {
			if (id.getEvidences().size() == evidenceNums.size()) {
				if (id.getLabel() == Id.LABEL_0) {  //com0
					if (yetTrainCom0Count < addTrainCom0Count && (yetTestCom0Count >= addTestCom0Count || Math.random() > addTrainCom0Pro)) {
						trainIds.add(id);
						yetTrainCom0Count++;
					} else if (yetTestCom0Count < addTestCom0Count) {
						testIds.add(id);
						yetTestCom0Count++;
					}
				} else {    //com1
					if (yetTrainCom1Count < addTrainCom1Count && (yetTestCom1Count >= addTestCom1Count || Math.random() > addTrainCom1Pro)) {
						trainIds.add(id);
						yetTrainCom1Count++;
					} else if (yetTestCom1Count < addTestCom1Count) {
						testIds.add(id);
						yetTestCom1Count++;
					}
				}
			} else {
				if (id.getLabel() == Id.LABEL_0) {  //miss0
					if (yetTrainMiss0Count < addTrainMiss0Count && (yetTestMiss0Count >= addTestMiss0Count || Math.random() > addTrainMiss0Pro)) {
						trainIds.add(id);
						yetTrainMiss0Count++;
					} else if (yetTestMiss0Count < addTestMiss0Count) {
						testIds.add(id);
						yetTestMiss0Count++;
					}
				} else {    //miss1
					if (yetTrainMiss1Count < addTrainMiss1Count && (yetTestMiss1Count >= addTestMiss1Count || Math.random() > addTrainMiss1Pro)) {
						trainIds.add(id);
						yetTrainMiss1Count++;
					} else if (yetTestMiss1Count < addTestMiss1Count) {
						testIds.add(id);
						yetTestMiss1Count++;
					}
				}
			}
		}
		return new DataSet[]{new DataSet(trainIds, evidenceNums, evidNameToId), new DataSet(testIds, evidenceNums, evidNameToId)};
	}
}
