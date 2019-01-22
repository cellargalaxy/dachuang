package top.cellargalaxy.dachuangspringboot.subSpace;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.run.Run;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cellargalaxy on 17-11-1.
 */
public class SnRandomSubSpaceCreate extends AbstractSubSpaceCreate {
	public static final String NAME = "SN随机子空间";
	private final Sn[] sns;

	public SnRandomSubSpaceCreate(Sn[] sns) {
		this.sns = sns;
	}

	@Override
	public List<List<Integer>> createSubSpaces(DataSet dataSet) {
		Collection<Integer> features = dataSet.getEvidenceName2EvidenceId().values();
		Sn fitSn = null;
		for (Sn sn : sns) {
			if (sn.getSn().length > features.size() || (fitSn != null && sn.getSn().length < fitSn.getSn().length)) {
				continue;
			}
			int max = Arrays.stream(sn.getSn()).max().getAsInt();
			if (max <= features.size()) {
				int count = 0;
				for (Integer integer : sn.getSn()) {
					count += countC(integer, features.size());
				}
				if (sn.getMinFn() <= count && sn.getMaxFn() <= count) {
					fitSn = sn;
				}
			}
		}
		int fn = fitSn.getMinFn() + (int) (Math.random() * (fitSn.getMaxFn() - fitSn.getMinFn()));
		Run.logger.info("子空间数量: {}", fn);
		int[] ints = fitSn.getSn();
		List<List<Integer>> subSpaces = new LinkedList<>();
		for (int i = 0; i < fn; i++) {
			List<Integer> subSpace = createSnRandomSubSpace(features, ints[(int) (ints.length * Math.random())]);
			if (!siContainSubSpace(subSpaces, subSpace)) {
				subSpaces.add(subSpace);
				Run.logger.info("子空间: {}", subSpace);
			} else {
				i--;
			}
		}
		return subSpaces;
	}

	private List<Integer> createSnRandomSubSpace(Collection<Integer> features, int len) {
		List<Integer> newFeatures = features.stream().collect(Collectors.toList());
		List<Integer> subSpace = new LinkedList<>();
		int i = 0;
		while (i < len) {
			Iterator<Integer> iterator = newFeatures.iterator();
			while (i < len && iterator.hasNext()) {
				if (Math.random() < 1.0 / features.size()) {
					subSpace.add(iterator.next());
					iterator.remove();
					i++;
				} else {
					iterator.next();
				}
			}
		}
		Collections.sort(subSpace);
		return subSpace;
	}

	@Override
	public String toString() {
		return "SN随机子空间{" +
				"sns=" + Arrays.toString(sns) +
				'}';
	}
}
