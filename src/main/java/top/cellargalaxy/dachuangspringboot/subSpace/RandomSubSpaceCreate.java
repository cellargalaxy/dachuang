package top.cellargalaxy.dachuangspringboot.subSpace;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-10-1.
 */
public final class RandomSubSpaceCreate extends AbstractSubSpaceCreate {
	public static final String NAME = "完全随机子空间";

	@Override
	public List<List<Integer>> createSubSpaces(DataSet dataSet) {
		Collection<Integer> features = dataSet.getEvidenceName2EvidenceId().values();
		if (features.size() == 0) {
			throw new RuntimeException("证据数据为0，无法创建子空间");
		}
		int fn;
		do {
			fn = (int) ((Math.pow(2, features.size()) - 1) * Math.random());
		} while (fn == 0);
		List<List<Integer>> subSpaces = new LinkedList<>();
		for (int i = 0; i < fn; i++) {
			List<Integer> subSpace = createRandomSubSpace(features);
			if (!siContainSubSpace(subSpaces, subSpace)) {
				subSpaces.add(subSpace);
			} else {
				i--;
			}
		}
		return subSpaces;
	}

	private List<Integer> createRandomSubSpace(Collection<Integer> features) {
		int i;
		do {
			i = (int) (Math.pow(2, features.size()) * Math.random());
		} while (i == 0);
		String string = Integer.toBinaryString(i);
		char[] chars = string.toCharArray();
		List<Integer> subSpace = new LinkedList<>();
		int j = chars.length - features.size();
		for (Integer feature : features) {
			if (j >= 0 && chars[j] == '1') {
				subSpace.add(feature);
			}
			j++;
		}
		Collections.sort(subSpace);
		return subSpace;
	}

	@Override
	public String toString() {
		return "完全随机子空间";
	}
}
