package top.cellargalaxy.dachuangspringboot.run;

import lombok.Data;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSetParameter;
import top.cellargalaxy.dachuangspringboot.evaluation.Auc;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.DsEvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EuclideanDistanceEvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.feature.AverageFeatureSplit;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryParameter;
import top.cellargalaxy.dachuangspringboot.hereditary.RouletteParentChrosChoose;
import top.cellargalaxy.dachuangspringboot.subSpace.PowerImprotenceAdjust;
import top.cellargalaxy.dachuangspringboot.subSpace.RandomSubSpaceCreate;
import top.cellargalaxy.dachuangspringboot.subSpace.Sn;

/**
 * @author cellargalaxy
 * @time 2019/1/14
 */
@Data
public class RunParameter {
	private DataSetParameter dataSetParameter = new DataSetParameter();


	private String evidenceSynthesisName = DsEvidenceSynthesis.NAME;
	private double thrf;
	private double thrnf;
	private double d1;
	private double d2;

	private String evaluationName = Auc.NAME;

	private String featureSplitName = AverageFeatureSplit.NAME;
	private double splitValue;

	private String parentChrosChooseName = RouletteParentChrosChoose.NAME;

	private String improtenceAdjustName = PowerImprotenceAdjust.NAME;
	private double adjustD;

	private String subSpaceCreateName = RandomSubSpaceCreate.NAME;
	private Sn[] sns;
	private double featureSelectionDeviation;
	private HereditaryParameter hereditaryParameter = new HereditaryParameter();
}
