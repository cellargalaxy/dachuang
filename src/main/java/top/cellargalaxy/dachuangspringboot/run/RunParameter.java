package top.cellargalaxy.dachuangspringboot.run;

import lombok.Data;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSetParameter;
import top.cellargalaxy.dachuangspringboot.evaluation.Auc;
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
	private String dataSetPath = "";
	private String trainDataSetPath = "";
	private String teatDataSettPath = "";

	private DataSetParameter dataSetParameter = new DataSetParameter();

	private String evidenceSynthesisName = "";
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
			new Sn(1, 1, 1),//1
			new Sn(3, 3, 1, 2),//4
			new Sn(3, 6, 1, 2, 3),//8
			new Sn(6, 12, 1, 2, 3, 4),//16
			new Sn(12, 26, 1, 2, 3, 4, 5),//32
			new Sn(26, 40, 1, 2, 3, 4, 5, 6),//64
			new Sn(40, 60, 1, 2, 3, 4, 5, 6, 7),//128
			new Sn(60, 100, 1, 2, 3, 4, 5, 6, 7, 8),//256
			new Sn(100, 200, 1, 2, 3, 4, 5, 6, 7, 8, 9),//512
			new Sn(200, 400, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10),//1024
	};
	private double featureSelectionDeviation;
	private HereditaryParameter hereditaryParameter = new HereditaryParameter();

	private double testPro = 0.5;
	private double trainMissPro = 0.2;
	private double testMissPro = 0.2;
	private double trainLabel1Pro = 0.2;
	private double testLabel1Pro = 0.2;
}
