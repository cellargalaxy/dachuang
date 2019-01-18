package top.cellargalaxy.dachuangspringboot.subSpace;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.feature.FeatureImportance;
import top.cellargalaxy.dachuangspringboot.feature.FeatureSelection;
import top.cellargalaxy.dachuangspringboot.feature.FeatureSplit;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryParameter;
import top.cellargalaxy.dachuangspringboot.hereditary.ParentChrosChoose;

import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class SnFeatureSelectionSubSpaceCreate extends AbstractSubSpaceCreate {
	public static final String NAME = "SN特征选择子空间";
	private final Sn[] sns;
	private final FeatureSplit featureSplit;
	private final double featureSelectionDeviation;
	private final HereditaryParameter hereditaryParameter;
	private final ParentChrosChoose parentChrosChoose;
	private final Evaluation evaluation;
	private final ImprotenceAdjust improtenceAdjust;

	public SnFeatureSelectionSubSpaceCreate(Sn[] sns, FeatureSplit featureSplit, double featureSelectionDeviation, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, Evaluation evaluation, ImprotenceAdjust improtenceAdjust) {
		this.sns = sns;
		this.featureSplit = featureSplit;
		this.featureSelectionDeviation = featureSelectionDeviation;
		this.hereditaryParameter = hereditaryParameter;
		this.parentChrosChoose = parentChrosChoose;
		this.evaluation = evaluation;
		this.improtenceAdjust = improtenceAdjust;
	}

	public List<List<Integer>> createSubSpaces(DataSet dataSet) throws IOException {
		ArrayList<FeatureImportance> featureImportances = FeatureSelection.featureSelection(dataSet, featureSplit, featureSelectionDeviation, hereditaryParameter, parentChrosChoose, evaluation);

		Collection<Integer> features = dataSet.getEvidenceName2EvidenceId().values();
		int fnMax = 0;
		Sn fitSn = null;
		for (Sn sn : sns) {
			if (sn.getSn().size() > features.size() || (fitSn != null && sn.getSn().size() < fitSn.getSn().size())) {
				continue;
			}
			int max = sn.getSn().stream().max(Comparator.comparing(Integer::valueOf)).get();
			if (max <= features.size()) {
				int count = 0;
				for (Integer integer : sn.getSn()) {
					count += countC(integer, features.size());
				}
				if (sn.getFnMin() <= count) {
					fitSn = sn;
					fnMax = count;
				}
			}
		}
		int fn = fitSn.getFnMin() + (int) (Math.random() * (fnMax - fitSn.getFnMin()));
		Integer[] ints = (Integer[]) fitSn.getSn().toArray();

		featureImportances = improtenceAdjust.adjustImportance(featureImportances);
		List<List<Integer>> subSpaces = new LinkedList<>();
		for (int i = 0; i < fn; i++) {
			List<Integer> subSpace = createFeatureSelectionSubSpace(featureImportances, ints[(int) (ints.length * Math.random())]);
			if (!siContainSubSpace(subSpaces, subSpace)) {
				subSpaces.add(subSpace);
			} else {
				i--;
			}
		}
		return subSpaces;
	}

	private List<Integer> createFeatureSelectionSubSpace(ArrayList<FeatureImportance> featureImportances, int len) {
		List<Integer> subSpace = new LinkedList<>();
		for (int i = 0; i < len; i++) {
			double sum = featureImportances.stream().mapToDouble(FeatureImportance::getEvaluationD).sum();
			int point = roulette(featureImportances, sum);
			FeatureImportance featureImportance = featureImportances.get(point);
			subSpace.add(featureImportance.getEvidenceId());
			featureImportances.remove(point);
		}
		Collections.sort(subSpace);
		return subSpace;
	}

	private int roulette(List<FeatureImportance> featureImportances, double sum) {
		if (sum == 0) {
			return (int) (featureImportances.size() * Math.random());
		}
		double point = sum * Math.random();
		double floor = 0;
		int i = 0;
		for (FeatureImportance featureImportance : featureImportances) {
			floor += featureImportance.getEvaluationD();
			if (floor >= point) {
				return i;
			}
			i++;
		}
		throw new RuntimeException("轮盘异常");
	}

	@Override
	public String toString() {
		return "SN特征选择子空间{" +
				"improtenceAdjust=" + improtenceAdjust +
				", sns=" + Arrays.toString(sns) +
				", featureSplit=" + featureSplit +
				", featureSelectionDeviation=" + featureSelectionDeviation +
				", parentChrosChoose=" + parentChrosChoose +
				", evaluation=" + evaluation +
				'}';
	}
}
