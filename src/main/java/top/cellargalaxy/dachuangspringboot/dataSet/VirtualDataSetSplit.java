package top.cellargalaxy.dachuangspringboot.dataSet;

import top.cellargalaxy.dachuangspringboot.run.Run;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lixinzhao
 * @date 2019/4/16
 */
public class VirtualDataSetSplit implements DataSetSplit {
    public static final String NAME = "虚拟数据分割";

    public static void main(String[] args) throws IOException {

        RunParameter runParameter = new RunParameter();

        DataSetParameter dataSetParameter = runParameter.getDataSetParameter();
        dataSetParameter.setIdColumnName("id");
        dataSetParameter.setEvidenceColumnName("evidence");
        dataSetParameter.setFraudColumnName("fraud");
        dataSetParameter.setUnfraudColumnName("unfraud");
        dataSetParameter.setLabelColumnName("collusion_transaction");
        dataSetParameter.setWithoutEvidences(Arrays.asList("total"));

        runParameter.setDataSetParameter(dataSetParameter);

        runParameter.setTestPro(0.2);
        runParameter.setTrainMissPro(0);
        runParameter.setTestMissPro(0);
        runParameter.setTrainLabel1Pro(0.2);
        runParameter.setTestLabel1Pro(0.4);
        runParameter.setK(0.4);

        File dataSetFile = new File("E:/g/实验/buyer.csv");
        DataSetFileIO dataSetFileIO = DataSetFileIOFactory.getDataSetFileIO(runParameter);

        DataSet dataSet = dataSetFileIO.readFileToDataSet(dataSetFile, runParameter.getDataSetParameter());

        new VirtualDataSetSplit().splitDataSet(dataSet, runParameter.getTestPro(), runParameter.getTrainMissPro(), runParameter.getTestMissPro(), runParameter.getTrainLabel1Pro(), runParameter.getTestLabel1Pro(), runParameter.getK());

    }

    @Override
    public DataSet[] splitDataSet(DataSet dataSet, double testPro, double trainMissPro, double testMissPro, double trainLabel1Pro, double testLabel1Pro, double k) {
        Run.logger.info("测试集比例: {}", testPro);
        Run.logger.info("训练集-缺失比例: {}", trainMissPro);
        Run.logger.info("测试集-缺失比例: {}", testMissPro);
        Run.logger.info("训练集-1标签比例: {}", trainLabel1Pro);
        Run.logger.info("测试集-1标签比例: {}", testLabel1Pro);
        Run.logger.info("缺失偏差: {}", k);

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
        double trainLabel0Pro = 1 - trainLabel1Pro;
        double testLabel0Pro = 1 - testLabel1Pro;

        double label0Pro = (trainPro * trainLabel0Pro) + (testPro * testLabel0Pro);
        double label1Pro = (trainPro * trainLabel1Pro) + (testPro * testLabel1Pro);

        Run.logger.info("0标签-预计比例: {}", label0Pro);
        Run.logger.info("1标签-预计比例: {}", label1Pro);

        int minNum = Integer.MAX_VALUE;
        if (com0Count + miss0Count > 0 && com0Count + miss0Count < minNum && label0Pro > 0) {
            minNum = com0Count + miss0Count;
        }
        if (com1Count + miss1Count > 0 && com1Count + miss1Count < minNum && label1Pro > 0) {
            minNum = com1Count + miss1Count;
        }

        int label0Num = -1;
        int label1Num = -1;
        if (com0Count + miss0Count == minNum) {
            label0Num = com0Count + miss0Count;
            label1Num = (int) (label0Num * label1Pro / label0Pro);
        } else if (com1Count + miss1Count == minNum) {
            label1Num = com1Count + miss1Count;
            label0Num = (int) (label1Num * label0Pro / label1Pro);
        }

        Run.logger.info("0标签-预计数量: {}", label0Num);
        Run.logger.info("1标签-预计数量: {}", label1Num);

        int train0Num = (int) (trainPro * label0Num);
        int train1Num = (int) (trainPro * label1Num);

        int test0Num = (int) (testPro * label0Num);
        int test1Num = (int) (testPro * label1Num);

        Run.logger.info("训练集-0标签-预计数量: {}", train0Num);
        Run.logger.info("训练集-1标签-预计数量: {}", train1Num);
        Run.logger.info("测试集-0标签-预计数量: {}", test0Num);
        Run.logger.info("测试集-1标签-预计数量: {}", test1Num);

        int train0Yet = 0;
        int train1Yet = 0;

        int test0Yet = 0;
        int test1Yet = 0;

        Map<String, Id> trainIdMap = new HashMap<>();
        Map<String, Id> testIdMap = new HashMap<>();
        for (Id id : com0List) {
            if (train0Yet < train0Num && test0Yet >= test0Num) {
                trainIdMap.put(id.getId(), id);
                train0Yet++;
            } else if (train0Yet >= train0Num && test0Yet < test0Num) {
                testIdMap.put(id.getId(), id);
                test0Yet++;
            } else if (train0Yet < train0Num && test0Yet < test0Num) {
                if (Math.random() < trainPro) {
                    trainIdMap.put(id.getId(), id);
                    train0Yet++;
                } else {
                    testIdMap.put(id.getId(), id);
                    test0Yet++;
                }
            }
        }
        for (Id id : com1List) {
            if (train1Yet < train1Num && test1Yet >= test1Num) {
                trainIdMap.put(id.getId(), id);
                train1Yet++;
            } else if (train1Yet >= train1Num && test1Yet < test1Num) {
                testIdMap.put(id.getId(), id);
                test1Yet++;
            } else if (train1Yet < train1Num && test1Yet < test1Num) {
                if (Math.random() < trainPro) {
                    trainIdMap.put(id.getId(), id);
                    train1Yet++;
                } else {
                    testIdMap.put(id.getId(), id);
                    test1Yet++;
                }
            }
        }

        Run.logger.info("训练集-0标签-实际数量: {}", train0Yet);
        Run.logger.info("训练集-1标签-实际数量: {}", train1Yet);
        Run.logger.info("测试集-0标签-实际数量: {}", test0Yet);
        Run.logger.info("测试集-1标签-实际数量: {}", test1Yet);

        int missEvidenceNum = (int) (evidenceCount * k);
        while (missEvidenceNum * 2 > evidenceCount) {
            missEvidenceNum--;
        }

        Run.logger.info("证据数量: {}", evidenceCount);
        Run.logger.info("虚拟缺失证据数量: {}", missEvidenceNum);

        List<Integer> evidenceIds = dataSet.getEvidenceName2EvidenceId().values().stream().collect(Collectors.toList());
        List<Integer> trainMissEvidenceIds = new LinkedList<>();
        List<Integer> testMissEvidenceIds = new LinkedList<>();
        for (int i = 0; i < missEvidenceNum; i++) {
            int point = (int) (evidenceIds.size() * Math.random());
            trainMissEvidenceIds.add(evidenceIds.remove(point));
        }
        for (int i = 0; i < missEvidenceNum; i++) {
            int point = (int) (evidenceIds.size() * Math.random());
            testMissEvidenceIds.add(evidenceIds.remove(point));
        }

        Run.logger.info("训练集-虚拟缺失证据: {}", trainMissEvidenceIds);
        Run.logger.info("测试集-虚拟缺失证据: {}", testMissEvidenceIds);

        for (Id id : trainIdMap.values()) {
            List<Integer> missEvidenceIds = new LinkedList<>();
            if (Math.random() < k * 0.5) {
                for (Integer missEvidenceId : trainMissEvidenceIds) {
                    if (Math.random() > k) {
                        missEvidenceIds.add(missEvidenceId);
                    }
                }
            } else {
                missEvidenceIds.addAll(trainMissEvidenceIds);
            }
            if (Math.random() < k * 0.1) {
                for (Evidence evidence : id.getEvidences()) {
                    if (Math.random() < k && !trainMissEvidenceIds.contains(evidence.getEvidenceId())) {
                        missEvidenceIds.add(evidence.getEvidenceId());
                    }
                }
            }
            for (Integer missEvidenceId : missEvidenceIds) {
                id.getEvidenceMap().remove(missEvidenceId);
            }
        }
        for (Id id : testIdMap.values()) {
            List<Integer> missEvidenceIds = new LinkedList<>();
            if (Math.random() < k * 0.5) {
                for (Integer missEvidenceId : testMissEvidenceIds) {
                    if (Math.random() > k) {
                        missEvidenceIds.add(missEvidenceId);
                    }
                }
            } else {
                missEvidenceIds.addAll(testMissEvidenceIds);
            }
            if (Math.random() < k * 0.1) {
                for (Evidence evidence : id.getEvidences()) {
                    if (Math.random() < k && !testMissEvidenceIds.contains(evidence.getEvidenceId())) {
                        missEvidenceIds.add(evidence.getEvidenceId());
                    }
                }
            }
            for (Integer missEvidenceId : missEvidenceIds) {
                id.getEvidenceMap().remove(missEvidenceId);
            }
        }

        int[] trainEvidenceCounts = new int[evidenceCount];
        int[] testEvidenceCounts = new int[evidenceCount];
        for (Id id : trainIdMap.values()) {
            for (Evidence evidence : id.getEvidences()) {
                trainEvidenceCounts[evidence.getEvidenceId() - 1] = trainEvidenceCounts[evidence.getEvidenceId() - 1] + 1;
            }
        }
        for (Id id : testIdMap.values()) {
            for (Evidence evidence : id.getEvidences()) {
                testEvidenceCounts[evidence.getEvidenceId() - 1] = testEvidenceCounts[evidence.getEvidenceId() - 1] + 1;
            }
        }

        Run.logger.info("训练集-证据统计-实际数量: {}", Arrays.toString(trainEvidenceCounts));
        Run.logger.info("测试集-证据统计-实际数量: {}", Arrays.toString(testEvidenceCounts));

        return new DataSet[]{new DataSet(trainIdMap), new DataSet(testIdMap)};
    }

    @Override
    public String toString() {
        return NAME;
    }
}
