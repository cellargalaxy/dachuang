package top.cellargalaxy.dachuangspringboot.dataSet;

import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
@Data
public class DataSetSplitImpl implements DataSetSplit {
	private final DataSet dataSet;
	private final int com0Count;
	private final int com1Count;
	private final int miss0Count;
	private final int miss1Count;
	private DataSet trainDataSet;
	private DataSet testDataSet;

	public DataSetSplitImpl(DataSet dataSet) {
		this.dataSet = dataSet;
		int com0Count = 0;
		int com1Count = 0;
		int miss0Count = 0;
		int miss1Count = 0;
		Collection<Id> ids = dataSet.getIds();
		int evidenceCount = dataSet.getEvidenceName2EvidenceId().size();
		for (Id id : ids) {
			if (id.getEvidences().size() == evidenceCount) {
				if (id.getLabel() == Id.LABEL_0) {
					com0Count++;
				} else if (id.getLabel() == Id.LABEL_1) {
					com1Count++;
				} else {
					throw new RuntimeException("数据集存在第三类标签:" + id.getLabel());
				}
			} else {
				if (id.getLabel() == Id.LABEL_0) {
					miss0Count++;
				} else if (id.getLabel() == Id.LABEL_1) {
					miss1Count++;
				} else {
					throw new RuntimeException("数据集存在第三类标签:" + id.getLabel());
				}
			}
		}
		this.com0Count = com0Count;
		this.com1Count = com1Count;
		this.miss0Count = miss0Count;
		this.miss1Count = miss1Count;
	}

	@Override
	public DataSetSplit splitDataSet(double test, double trainMiss, double testMiss, double label1) {
		Collection<Id> ids = dataSet.getIds();
		int evidenceCount = dataSet.getEvidenceName2EvidenceId().size();

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

		Map<String, Id> trainIdMaps = new HashMap<>();
		Map<String, Id> testIdMaps = new HashMap<>();
		for (Id id : ids) {
			if (id.getEvidences().size() == evidenceCount) {
				if (id.getLabel() == Id.LABEL_0) {  //com0
					if (yetTrainCom0Count < addTrainCom0Count && (yetTestCom0Count >= addTestCom0Count || Math.random() > addTrainCom0Pro)) {
						trainIdMaps.put(id.getId(), id);
						yetTrainCom0Count++;
					} else if (yetTestCom0Count < addTestCom0Count) {
						testIdMaps.put(id.getId(), id);
						yetTestCom0Count++;
					}
				} else {    //com1
					if (yetTrainCom1Count < addTrainCom1Count && (yetTestCom1Count >= addTestCom1Count || Math.random() > addTrainCom1Pro)) {
						trainIdMaps.put(id.getId(), id);
						yetTrainCom1Count++;
					} else if (yetTestCom1Count < addTestCom1Count) {
						testIdMaps.put(id.getId(), id);
						yetTestCom1Count++;
					}
				}
			} else {
				if (id.getLabel() == Id.LABEL_0) {  //miss0
					if (yetTrainMiss0Count < addTrainMiss0Count && (yetTestMiss0Count >= addTestMiss0Count || Math.random() > addTrainMiss0Pro)) {
						trainIdMaps.put(id.getId(), id);
						yetTrainMiss0Count++;
					} else if (yetTestMiss0Count < addTestMiss0Count) {
						testIdMaps.put(id.getId(), id);
						yetTestMiss0Count++;
					}
				} else {    //miss1
					if (yetTrainMiss1Count < addTrainMiss1Count && (yetTestMiss1Count >= addTestMiss1Count || Math.random() > addTrainMiss1Pro)) {
						trainIdMaps.put(id.getId(), id);
						yetTrainMiss1Count++;
					} else if (yetTestMiss1Count < addTestMiss1Count) {
						testIdMaps.put(id.getId(), id);
						yetTestMiss1Count++;
					}
				}
			}
		}
		trainDataSet = new DataSet(trainIdMaps);
		testDataSet = new DataSet(testIdMaps);
		return this;
	}
}
