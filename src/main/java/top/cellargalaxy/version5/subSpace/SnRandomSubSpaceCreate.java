package top.cellargalaxy.version5.subSpace;


import top.cellargalaxy.version5.dataSet.DataSet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-11-1.
 */
public class SnRandomSubSpaceCreate extends AbstractSubSpaceCreate {
	private final int[][] sns;
	private final int[] fnMins;
	
	public SnRandomSubSpaceCreate(int[][] sns, int[] fnMins) {
		this.sns = sns;
		this.fnMins = fnMins;
	}
	
	public List<List<Integer>> createSubSpaces(DataSet dataSet) throws IOException, ClassNotFoundException {
		LinkedList<Integer> features = dataSet.getEvidenceNums();
		int[] sn = null;
		int fnMax = 0;
		int fnMin = -1;
		for (int i = 0; i < sns.length; i++) {
			if (sns[i].length > features.size() || (sn != null && sns[i].length <= sn.length)) {
				continue;
			}
			int max = Integer.MIN_VALUE;
			for (int j = 0; j < sns[i].length; j++) {
				if (sns[i][j] > max) {
					max = sns[i][j];
				}
			}
			if (max <= features.size()) {
				int count = 0;
				for (int j : sns[i]) {
					count += countC(j, features.size());
				}
				if (fnMins[i] <= count) {
					sn = sns[i];
					fnMin = fnMins[i];
					fnMax = count;
				}
			}
		}
		int fn = fnMin + (int) (Math.random() * (fnMax - fnMin));
		
		List<List<Integer>> subSpaces = new LinkedList<List<Integer>>();
		for (int i = 0; i < fn; i++) {
			List<Integer> subSpace = createSnRandomSubSpace(features, sn[(int) (sn.length * Math.random())]);
			if (!siContainSubSpace(subSpaces, subSpace)) {
				subSpaces.add(subSpace);
			} else {
				i--;
			}
		}
		return subSpaces;
	}
	
	@Override
	public String toString() {
		return "SnRandomSubSpaceCreate(sn随机子空间创建){" +
				"sns=" + Arrays.toString(sns) +
				", fnMins=" + Arrays.toString(fnMins) +
				'}';
	}
}
