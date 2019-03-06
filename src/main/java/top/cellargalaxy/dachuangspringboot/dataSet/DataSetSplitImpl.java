package top.cellargalaxy.dachuangspringboot.dataSet;

import lombok.Data;
import top.cellargalaxy.dachuangspringboot.run.Run;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
@Data
public class DataSetSplitImpl implements DataSetSplit {
// 1  2  3  4  5  6  7  8  9
// 33 31 29 27 25 23 21 19 17		225
// 67 69 71 73 75 77 79 81 83		675

	public static void main(String[] args) {
		System.out.println(65.0 + 55.0 + 45.0 + 35.0 + 25.0 + 15.0 + 5.0);
		System.out.println(35.0 + 45.0 + 55.0 + 65.0 + 75.0 + 85.0 + 95.0 + 100.0 + 100.0);

		double trainPro = 0.75;
		double testPro = 0.25;
		int evidenceCount = 9;
		double k = 20;

		double x = (100 / ((trainPro / testPro) + 1)) + ((evidenceCount - 1) / 2 * k);
		double[] trainEvidencePros = new double[evidenceCount];
		double[] testEvidencePros = new double[evidenceCount];
		for (int i = 0; i < testEvidencePros.length; i++) {
			testEvidencePros[i] = (x - i * k);
			if (testEvidencePros[i] < 0) {
				testEvidencePros[i] = 0;
			}
			if (testEvidencePros[i] > 100) {
				testEvidencePros[i] = 100;
			}
			testEvidencePros[i]=testEvidencePros[i]/100;
		}
		for (int i = 0; i < trainEvidencePros.length; i++) {
			trainEvidencePros[i] = 1 - testEvidencePros[i];
		}

		double sum = 0;
		for (double testEvidencePro : testEvidencePros) {
			sum += testEvidencePro;
			System.out.print(testEvidencePro + "\t");
		}
		System.out.println(sum);
		sum = 0;
		for (double trainEvidencePro : trainEvidencePros) {
			sum += trainEvidencePro;
			System.out.print(trainEvidencePro + "\t");
		}
		System.out.println(sum);
	}

	@Override
	public DataSet[] splitDataSet(DataSet dataSet, double testPro, double trainMissPro, double testMissPro, double trainLabel1Pro, double testLabel1Pro, int k) {
		Run.logger.info("测试集比例: {}", testPro);
		Run.logger.info("训练集-缺失比例: {}", trainMissPro);
		Run.logger.info("测试集-缺失比例: {}", testMissPro);
		Run.logger.info("训练集-1标签比例: {}", trainLabel1Pro);
		Run.logger.info("测试集-1标签比例: {}", testLabel1Pro);

		List<Id> com0List = new LinkedList<>();
		List<Id> com1List = new LinkedList<>();
		List<Id> miss0List = new LinkedList<>();
		List<Id> miss1List = new LinkedList<>();
		int evidenceCount = dataSet.getEvidenceName2EvidenceId().size();
		for (Id id : dataSet.getIds()) {
			if (id.getEvidences().size() == evidenceCount) {
				if (id.getLabel() == Id.LABEL_0) {
					com0List.add(id);
				} else {
					com1List.add(id);
				}
			} else {
				if (id.getLabel() == Id.LABEL_0) {
					miss0List.add(id);
				} else {
					miss1List.add(id);
				}
			}
		}
		int com0Count = com0List.size();
		int com1Count = com1List.size();
		int miss0Count = miss0List.size();
		int miss1Count = miss1List.size();

		Run.logger.info("完整-0标签-数量: {}", com0Count);
		Run.logger.info("完整-1标签-数量: {}", com1Count);
		Run.logger.info("缺失-0标签-数量: {}", miss0Count);
		Run.logger.info("缺失-1标签-数量: {}", miss1Count);

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

		Run.logger.info("完整-0标签-预计比例: {}", com0Pro);
		Run.logger.info("完整-1标签-预计比例: {}", com1Pro);
		Run.logger.info("缺失-0标签-预计比例: {}", miss0Pro);
		Run.logger.info("缺失-1标签-预计比例: {}", miss1Pro);

		int minNum = Integer.MAX_VALUE;
		if (com0Count > 0 && com0Count < minNum && com0Pro > 0) {
			minNum = com0Count;
		}
		if (com1Count > 0 && com1Count < minNum && com1Pro > 0) {
			minNum = com1Count;
		}
		if (miss0Count > 0 && miss0Count < minNum && miss0Pro > 0) {
			minNum = miss0Count;
		}
		if (miss1Count > 0 && miss1Count < minNum && miss1Pro > 0) {
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

		Run.logger.info("完整-0标签-预计数量: {}", com0Num);
		Run.logger.info("完整-1标签-预计数量: {}", com1Num);
		Run.logger.info("缺失-0标签-预计数量: {}", miss0Num);
		Run.logger.info("缺失-1标签-预计数量: {}", miss1Num);

		int trainCom0Num = (int) (com0Num * trainCom0Pro / com0Pro);
		int trainCom1Num = (int) (com1Num * trainCom1Pro / com1Pro);
		int trainMiss0Num = (int) (miss0Num * trainMiss0Pro / com1Pro);
		int trainMiss1Num = (int) (miss1Num * trainMiss1Pro / com1Pro);

		int testCom0Num = (int) (com0Num * testCom0Pro / com0Pro);
		int testCom1Num = (int) (com1Num * testCom1Pro / com1Pro);
		int testMiss0Num = (int) (miss0Num * testMiss0Pro / com1Pro);
		int testMiss1Num = (int) (miss1Num * testMiss1Pro / com1Pro);

		Run.logger.info("训练集-完整-0标签-预计数量: {}", trainCom0Num);
		Run.logger.info("训练集-完整-1标签-预计数量: {}", trainCom1Num);
		Run.logger.info("训练集-缺失-0标签-预计数量: {}", trainMiss0Num);
		Run.logger.info("训练集-缺失-1标签-预计数量: {}", trainMiss1Num);

		Run.logger.info("测试集-完整-0标签-预计数量: {}", testCom0Num);
		Run.logger.info("测试集-完整-1标签-预计数量: {}", testCom1Num);
		Run.logger.info("测试集-缺失-0标签-预计数量: {}", testMiss0Num);
		Run.logger.info("测试集-缺失-1标签-预计数量: {}", testMiss1Num);

		//测试机
		double x = (100 / ((trainPro / testPro) + 1)) + ((evidenceCount - 1) / 2 * k);
		double[] trainEvidencePros = new double[evidenceCount];
		double[] testEvidencePros = new double[evidenceCount];
		for (int i = 0; i < testEvidencePros.length; i++) {
			testEvidencePros[i] = (x - i * k);
			if (testEvidencePros[i] < 0) {
				testEvidencePros[i] = 0;
			}
			if (testEvidencePros[i] > 100) {
				testEvidencePros[i] = 100;
			}
			testEvidencePros[i]=testEvidencePros[i]/100;
		}
		for (int i = 0; i < trainEvidencePros.length; i++) {
			trainEvidencePros[i] = 1 - testEvidencePros[i];
		}

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

		for (Id id : com0List) {
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
		}
		for (Id id : com1List) {
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
		for (Id id : miss0List) {
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
		}
		for (Id id : miss1List) {
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

//		for (Id id : dataSet.getIds()) {
//
//			if (id.getEvidences().size() == evidenceCount) {
//				if (id.getLabel() == Id.LABEL_0) {
//					if (trainCom0Yet < trainCom0Num && testCom0Yet >= testCom0Num) {
//						trainIdMap.put(id.getId(), id);
//						trainCom0Yet++;
//					} else if (trainCom0Yet >= trainCom0Num && testCom0Yet < testCom0Num) {
//						testIdMap.put(id.getId(), id);
//						testCom0Yet++;
//					} else if (trainCom0Yet < trainCom0Num && testCom0Yet < testCom0Num) {
//						if (Math.random() < trainPro) {
//							trainIdMap.put(id.getId(), id);
//							trainCom0Yet++;
//						} else {
//							testIdMap.put(id.getId(), id);
//							testCom0Yet++;
//						}
//					}
//				} else {
//					if (trainCom1Yet < trainCom1Num && testCom1Yet >= testCom1Num) {
//						trainIdMap.put(id.getId(), id);
//						trainCom1Yet++;
//					} else if (trainCom1Yet >= trainCom1Num && testCom1Yet < testCom1Num) {
//						testIdMap.put(id.getId(), id);
//						testCom1Yet++;
//					} else if (trainCom1Yet < trainCom1Num && testCom1Yet < testCom1Num) {
//						if (Math.random() < trainPro) {
//							trainIdMap.put(id.getId(), id);
//							trainCom1Yet++;
//						} else {
//							testIdMap.put(id.getId(), id);
//							testCom1Yet++;
//						}
//					}
//				}
//			} else {
//				if (id.getLabel() == Id.LABEL_0) {
//					if (trainMiss0Yet < trainMiss0Num && testMiss0Yet >= testMiss0Num) {
//						trainIdMap.put(id.getId(), id);
//						trainMiss0Yet++;
//					} else if (trainMiss0Yet >= trainMiss0Num && testMiss0Yet < testMiss0Num) {
//						testIdMap.put(id.getId(), id);
//						testMiss0Yet++;
//					} else if (trainMiss0Yet < trainMiss0Num && testMiss0Yet < testMiss0Num) {
//						if (Math.random() < trainPro) {
//							trainIdMap.put(id.getId(), id);
//							trainMiss0Yet++;
//						} else {
//							testIdMap.put(id.getId(), id);
//							testMiss0Yet++;
//						}
//					}
//				} else {
//					if (id.getLabel() == Id.LABEL_0) {
//						if (trainMiss1Yet < trainMiss1Num && testMiss1Yet >= testMiss1Num) {
//							trainIdMap.put(id.getId(), id);
//							trainMiss1Yet++;
//						} else if (trainMiss1Yet >= trainMiss1Num && testMiss1Yet < testMiss1Num) {
//							testIdMap.put(id.getId(), id);
//							testMiss1Yet++;
//						} else if (trainMiss1Yet < trainMiss1Num && testMiss1Yet < testMiss1Num) {
//							if (Math.random() < trainPro) {
//								trainIdMap.put(id.getId(), id);
//								trainMiss1Yet++;
//							} else {
//								testIdMap.put(id.getId(), id);
//								testMiss1Yet++;
//							}
//						}
//					}
//				}
//			}
//
//
//		}

		Run.logger.info("训练集-完整-0标签-实际数量: {}", trainCom0Yet);
		Run.logger.info("训练集-完整-1标签-实际数量: {}", trainCom1Yet);
		Run.logger.info("训练集-缺失-0标签-实际数量: {}", trainMiss0Yet);
		Run.logger.info("训练集-缺失-1标签-实际数量: {}", trainMiss1Yet);

		Run.logger.info("测试集-完整-0标签-实际数量: {}", testCom0Yet);
		Run.logger.info("测试集-完整-1标签-实际数量: {}", testCom1Yet);
		Run.logger.info("测试集-缺失-0标签-实际数量: {}", testMiss0Yet);
		Run.logger.info("测试集-缺失-1标签-实际数量: {}", testMiss1Yet);

		return new DataSet[]{new DataSet(trainIdMap), new DataSet(testIdMap)};
	}
}
