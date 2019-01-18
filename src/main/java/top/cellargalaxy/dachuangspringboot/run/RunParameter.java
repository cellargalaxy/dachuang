package top.cellargalaxy.dachuangspringboot.run;

import lombok.Data;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSetParameter;
import top.cellargalaxy.dachuangspringboot.evaluation.Auc;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.DsEvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.feature.AverageFeatureSplit;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryParameter;
import top.cellargalaxy.dachuangspringboot.hereditary.RouletteParentChrosChoose;
import top.cellargalaxy.dachuangspringboot.subSpace.PowerImprotenceAdjust;
import top.cellargalaxy.dachuangspringboot.subSpace.Sn;
import top.cellargalaxy.dachuangspringboot.subSpace.SnFeatureSelectionSubSpaceCreate;

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

	private String subSpaceCreateName = SnFeatureSelectionSubSpaceCreate.NAME;
	private Sn[] sns = new Sn[]{
			new Sn(1, 1),
			new Sn(3, 1, 2),
			new Sn(6, 1, 2, 3),
			new Sn(12, 1, 2, 3, 4),
			new Sn(15, 1, 2, 3, 4, 5)};
	private double featureSelectionDeviation;
	private HereditaryParameter hereditaryParameter = new HereditaryParameter();


	private double testPro = 0.5;
	private double trainMissPro = 0.2;
	private double testMissPro = 0.2;
	private double trainLabel1Pro = 0.2;
	private double testLabel1Pro = 0.2;
}
