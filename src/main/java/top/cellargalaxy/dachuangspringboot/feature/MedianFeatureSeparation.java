package top.cellargalaxy.dachuangspringboot.feature;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class MedianFeatureSeparation implements FeatureSeparation {

	public void separationFeature(LinkedList<AucImprotence> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unImproEvid) {
		int count = aucImprotences.size() / 2;
		int i = 0;
		for (AucImprotence aucImprotence : aucImprotences) {
			if (i <= count) {
				imroEvid.add(aucImprotence.getEvidenceNum());
			} else {
				unImproEvid.add(aucImprotence.getEvidenceNum());
			}
			i++;
		}
	}

	@Override
	public String toString() {
		return "MedianFeatureSeparation{中位数特征选择}";
	}
}
