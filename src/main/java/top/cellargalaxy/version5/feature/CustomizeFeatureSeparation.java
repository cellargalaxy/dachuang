package top.cellargalaxy.version5.feature;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class CustomizeFeatureSeparation implements FeatureSeparation {
	private double separationValue;
	
	public CustomizeFeatureSeparation(double separationValue) {
		this.separationValue = separationValue;
	}
	
	public void separationFeature(LinkedList<AucImprotence> aucImprotences, LinkedList<Integer> imroEvid, LinkedList<Integer> unImproEvid) {
		for (AucImprotence aucImprotence : aucImprotences) {
			if (aucImprotence.getAucD() <= separationValue) {
				imroEvid.add(aucImprotence.getEvidenceNum());
			} else {
				unImproEvid.add(aucImprotence.getEvidenceNum());
			}
		}
	}
	
	@Override
	public String toString() {
		return "CustomizeFeatureSeparation(用户自定义特征选择){" +
				"separationValue=" + separationValue +
				'}';
	}
}
