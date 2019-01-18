package top.cellargalaxy.dachuangspringboot.dataSet;

import lombok.Data;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
@Data
public class DataSetSplitImpl implements DataSetSplit {
	public static void main(String[] args) throws IOException {
		RunParameter runParameter = new RunParameter();

		DataSetParameter dataSetParameter = runParameter.getDataSetParameter();
		dataSetParameter.setEvidenceColumnName("证据");
		dataSetParameter.setFraudColumnName("A");
		dataSetParameter.setUnfraudColumnName("B");
		DataSetFileIO dataSetFileIO = DataSetFileIOFactory.createFromFileReadDataSet(runParameter);
		DataSet dataSet = dataSetFileIO.readFileToDataSet(new File("D:/g/trainAll.csv"), dataSetParameter);
		DataSet[] dataSets = new DataSetSplitImpl().splitDataSet(dataSet, 0.2, 0.6, 0.8, 0.1, 0.4);

		File trainFile=new File("D:/trainFile.csv");
		File testFile=new File("D:/testFile.csv");

		dataSetFileIO.writeDataSetToFile(dataSetParameter,dataSets[0],trainFile);
		dataSetFileIO.writeDataSetToFile(dataSetParameter,dataSets[1],testFile);
	}

	@Override
	public DataSet[] splitDataSet(DataSet dataSet, double testPro, double trainMissPro, double testMissPro, double trainLabel1Pro, double testLabel1Pro) {
		int com0Count = 0;
		int com1Count = 0;
		int miss0Count = 0;
		int miss1Count = 0;
		int evidenceCount = dataSet.getEvidenceName2EvidenceId().size();
		for (Id id : dataSet.getIds()) {
			if (id.getEvidences().size() == evidenceCount) {
				if (id.getLabel() == Id.LABEL_0) {
					com0Count++;
				} else {
					com1Count++;
				}
			} else {
				if (id.getLabel() == Id.LABEL_0) {
					miss0Count++;
				} else {
					miss1Count++;
				}
			}
		}

		double trainPro = 1 - testPro;
		double trainComPro = 1 - trainMissPro;
		double testComPro = 1 - testMissPro;
		double trainLabel0Pro = 1 - trainLabel1Pro;
		double testLabel0Pro = 1 - testLabel1Pro;

		double trainCom0Pro = trainComPro * trainLabel0Pro * trainPro;
		double trainCom1Pro = trainComPro * trainLabel1Pro * trainPro;
		double trainMiss0Pro = trainMissPro * trainLabel0Pro * trainPro;
		double trainMiss1Pro = trainMissPro * trainLabel1Pro * trainPro;

		double testCom0Pro = testComPro * testLabel0Pro * testPro;
		double testCom1Pro = testComPro * testLabel1Pro * testPro;
		double testMiss0Pro = testMissPro * testLabel0Pro * testPro;
		double testMiss1Pro = testMissPro * testLabel1Pro * testPro;

		double com0Pro = trainCom0Pro + testCom0Pro;
		double com1Pro = trainCom1Pro + testCom1Pro;
		double miss0Pro = trainMiss0Pro + testMiss0Pro;
		double miss1Pro = trainMiss1Pro + testMiss1Pro;


		int minNum = Integer.MAX_VALUE;
		if (com0Count > 0 && com0Count < minNum) {
			minNum = com0Count;
		}
		if (com1Count > 0 && com1Count < minNum) {
			minNum = com1Count;
		}
		if (miss0Count > 0 && miss0Count < minNum) {
			minNum = miss0Count;
		}
		if (miss1Count > 0 && miss1Count < minNum) {
			minNum = miss1Count;
		}

		int com0Num = -1;
		int com1Num = -1;
		int miss0Num = -1;
		int miss1Num = -1;
		if (com0Count == minNum) {
			com0Num = com0Count;
			com1Num = (int) (com0Count * com1Pro / com0Pro);
			miss0Num = (int) (com0Count * miss0Pro / com0Pro);
			miss1Num = (int) (com0Count * miss1Pro / com0Pro);
		} else if (com1Count == minNum) {
			com0Num = (int) (com1Count * com0Pro / com1Pro);
			com1Num = com1Count;
			miss0Num = (int) (com1Count * miss0Pro / com1Pro);
			miss1Num = (int) (com1Count * miss1Pro / com1Pro);
		} else if (miss0Count == minNum) {
			com0Num = (int) (miss0Count * com0Pro / miss0Pro);
			com1Num = (int) (miss0Count * com1Pro / miss0Pro);
			miss0Num = miss0Count;
			miss1Num = (int) (miss0Count * miss1Pro / miss0Pro);
		} else if (miss1Count == minNum) {
			com0Num = (int) (miss1Count * com0Pro / miss1Pro);
			com1Num = (int) (miss1Count * com1Pro / miss1Pro);
			miss0Num = (int) (miss1Count * miss0Pro / miss1Pro);
			miss1Num = miss1Count;
		}

		int trainCom0Num = (int) (com0Num * trainPro);
		int trainCom1Num = (int) (com1Num * trainPro);
		int trainMiss0Num = (int) (miss0Num * trainPro);
		int trainMiss1Num = (int) (miss1Num * trainPro);

		int testCom0Num = (int) (com0Num * testPro);
		int testCom1Num = (int) (com1Num * testPro);
		int testMiss0Num = (int) (miss0Num * testPro);
		int testMiss1Num = (int) (miss1Num * testPro);

		int trainCom0Yet = 0;
		int trainCom1Yet = 0;
		int trainMiss0Yet = 0;
		int trainMiss1Yet = 0;

		int testCom0Yet = 0;
		int testCom1Yet = 0;
		int testMiss0Yet = 0;
		int testMiss1Yet = 0;

		Map<String, Id> trainIdMap = new HashMap<>();
		Map<String, Id> testIdMap = new HashMap<>();
		for (Id id : dataSet.getIds()) {

			if (id.getEvidences().size() == evidenceCount) {
				if (id.getLabel() == Id.LABEL_0) {
					if (trainCom0Yet < trainCom0Num && testCom0Yet >= testCom0Num) {
						trainIdMap.put(id.getId(), id);
						trainCom0Yet++;
					} else if (trainCom0Yet >= trainCom0Num && testCom0Yet < testCom0Num) {
						testIdMap.put(id.getId(), id);
						testCom0Yet++;
					} else if (trainCom0Yet < trainCom0Num && testCom0Yet < testCom0Num) {
						if (Math.random() < trainPro) {
							trainIdMap.put(id.getId(), id);
							trainCom0Yet++;
						} else {
							testIdMap.put(id.getId(), id);
							testCom0Yet++;
						}
					}
				} else {
					if (trainCom1Yet < trainCom1Num && testCom1Yet >= testCom1Num) {
						trainIdMap.put(id.getId(), id);
						trainCom1Yet++;
					} else if (trainCom1Yet >= trainCom1Num && testCom1Yet < testCom1Num) {
						testIdMap.put(id.getId(), id);
						testCom1Yet++;
					} else if (trainCom1Yet < trainCom1Num && testCom1Yet < testCom1Num) {
						if (Math.random() < trainPro) {
							trainIdMap.put(id.getId(), id);
							trainCom1Yet++;
						} else {
							testIdMap.put(id.getId(), id);
							testCom1Yet++;
						}
					}
				}
			} else {
				if (id.getLabel() == Id.LABEL_0) {
					if (trainMiss0Yet < trainMiss0Num && testMiss0Yet >= testMiss0Num) {
						trainIdMap.put(id.getId(), id);
						trainMiss0Yet++;
					} else if (trainMiss0Yet >= trainMiss0Num && testMiss0Yet < testMiss0Num) {
						testIdMap.put(id.getId(), id);
						testMiss0Yet++;
					} else if (trainMiss0Yet < trainMiss0Num && testMiss0Yet < testMiss0Num) {
						if (Math.random() < trainPro) {
							trainIdMap.put(id.getId(), id);
							trainMiss0Yet++;
						} else {
							testIdMap.put(id.getId(), id);
							testMiss0Yet++;
						}
					}
				} else {
					if (id.getLabel() == Id.LABEL_0) {
						if (trainMiss1Yet < trainMiss1Num && testMiss1Yet >= testMiss1Num) {
							trainIdMap.put(id.getId(), id);
							trainMiss1Yet++;
						} else if (trainMiss1Yet >= trainMiss1Num && testMiss1Yet < testMiss1Num) {
							testIdMap.put(id.getId(), id);
							testMiss1Yet++;
						} else if (trainMiss1Yet < trainMiss1Num && testMiss1Yet < testMiss1Num) {
							if (Math.random() < trainPro) {
								trainIdMap.put(id.getId(), id);
								trainMiss1Yet++;
							} else {
								testIdMap.put(id.getId(), id);
								testMiss1Yet++;
							}
						}
					}
				}
			}

//			if (Math.random() < trainPro) {
//				if (trainCom0Yet < trainCom0Num && id.getEvidences().size() == evidenceCount && id.getLabel() == Id.LABEL_0) {
//					trainIdMap.put(id.getId(), id);
//					trainCom0Yet++;
//					continue;
//				}
//				if (trainCom1Yet < trainCom1Num && id.getEvidences().size() == evidenceCount && id.getLabel() == Id.LABEL_1) {
//					trainIdMap.put(id.getId(), id);
//					trainCom1Yet++;
//					continue;
//				}
//				if (trainMiss0Yet < trainMiss0Num && id.getEvidences().size() < evidenceCount && id.getLabel() == Id.LABEL_0) {
//					trainIdMap.put(id.getId(), id);
//					trainMiss0Yet++;
//					continue;
//				}
//				if (trainMiss1Yet < trainMiss1Num && id.getEvidences().size() < evidenceCount && id.getLabel() == Id.LABEL_1) {
//					trainIdMap.put(id.getId(), id);
//					trainMiss1Yet++;
//					continue;
//				}
//			}
//			if (testCom0Yet < testCom0Num && id.getEvidences().size() == evidenceCount && id.getLabel() == Id.LABEL_0) {
//				testIdMap.put(id.getId(), id);
//				testCom0Yet++;
//			} else if (testCom1Yet < testCom1Num && id.getEvidences().size() == evidenceCount && id.getLabel() == Id.LABEL_1) {
//				testIdMap.put(id.getId(), id);
//				testCom1Yet++;
//			} else if (testMiss0Yet < testMiss0Num && id.getEvidences().size() < evidenceCount && id.getLabel() == Id.LABEL_0) {
//				testIdMap.put(id.getId(), id);
//				testMiss0Yet++;
//			} else if (testMiss1Yet < testMiss1Num && id.getEvidences().size() < evidenceCount && id.getLabel() == Id.LABEL_1) {
//				testIdMap.put(id.getId(), id);
//				testMiss1Yet++;
//			}

		}

		return new DataSet[]{new DataSet(trainIdMap), new DataSet(testIdMap)};
	}
}
