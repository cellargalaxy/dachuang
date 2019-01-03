package top.cellargalaxy.dachuangspringboot.subSpace;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-10-1.
 */
public final class RandomSubSpaceCreate extends AbstractSubSpaceCreate {
	public List<List<Integer>> createSubSpaces(DataSet dataSet) throws IOException, ClassNotFoundException {
		LinkedList<Integer> features = dataSet.getEvidenceNums();
		int fn;
		do {
			fn = (int) ((Math.pow(2, features.size()) - 1) * Math.random());
		} while (fn == 0);
		List<List<Integer>> subSpaces = new LinkedList<List<Integer>>();
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
	
	
	@Override
	public String toString() {
		return "RandomSubSpaceCreate{完全随机子空间创建}";
	}
}
