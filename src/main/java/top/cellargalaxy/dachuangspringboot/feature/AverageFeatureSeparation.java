package top.cellargalaxy.dachuangspringboot.feature;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class AverageFeatureSeparation implements FeatureSeparation {
	public void separationFeature(LinkedList<AucImprotence> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unImproEvid) {
		double count = 0;
		for (AucImprotence aucImprotence : aucImprotences) {
			count += aucImprotence.getAucD();
		}
		double avg = count / aucImprotences.size();
		for (AucImprotence aucImprotence : aucImprotences) {
			if (aucImprotence.getAucD() <= avg) {
				imroEvid.add(aucImprotence.getEvidenceNum());
			} else {
				unImproEvid.add(aucImprotence.getEvidenceNum());
			}
		}
	}

	@Override
	public String toString() {
		return "AverageFeatureSeparation{平均数特征选择}";
	}
}