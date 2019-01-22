package top.cellargalaxy.dachuangspringboot.subSpace;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.feature.FeatureImportance;
import top.cellargalaxy.dachuangspringboot.feature.FeatureSelection;
import top.cellargalaxy.dachuangspringboot.feature.FeatureSplit;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryParameter;
import top.cellargalaxy.dachuangspringboot.hereditary.ParentChrosChoose;
import top.cellargalaxy.dachuangspringboot.run.Run;

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
		featureImportances = improtenceAdjust.adjustImportance(featureImportances);
		List<List<Integer>> subSpaces = new LinkedList<>();
		for (int i = 0; i < fn; i++) {
			List<Integer> subSpace = createFeatureSelectionSubSpace(featureImportances, ints[(int) (ints.length * Math.random())]);
			if (!siContainSubSpace(subSpaces, subSpace)) {
				subSpaces.add(subSpace);
				Run.logger.info("子空间: {}", subSpace);
			} else {
				i--;
			}
		}
		return subSpaces;
	}

	private List<Integer> createFeatureSelectionSubSpace(ArrayList<FeatureImportance> featureImportances, int len) {
		List<FeatureImportance> list = featureImportances;
		featureImportances = new ArrayList<>();
		featureImportances.addAll(list);
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
