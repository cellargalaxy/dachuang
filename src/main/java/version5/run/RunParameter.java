package version5.run;

import version5.dataSet.DataSet;
import version5.dataSet.DataSetParameter;
import version5.dataSet.DataSetSeparation;
import version5.dataSet.DataSetSeparationImpl;
import version5.evaluation.Evaluation;
import version5.evaluation.EvaluationFactory;
import version5.evidenceSynthesis.EvidenceSynthesis;
import version5.evidenceSynthesis.SubSpaceEvidenceSynthesisFactory;
import version5.feature.FeatureSeparation;
import version5.feature.FeatureSeparationFactory;
import version5.hereditary.Hereditary;
import version5.hereditary.HereditaryParameter;
import version5.hereditary.ParentChrosChoose;
import version5.hereditary.ParentChrosChooseFactory;
import version5.subSpace.ImprotenceAdjust;
import version5.subSpace.ImprotenceAdjustFactory;
import version5.subSpace.SubSpaceCreate;
import version5.subSpace.SubSpaceCreateFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by cellargalaxy on 17-9-18.
 * 这个类是全部参数的bean类
 * 这些参数存在一定的依赖关系，即某些参数的生成依赖与其他参数，若依赖参数不被需要的，被依赖的参数也不需要了，但这个类会处理这些依赖关系
 * <p>
 * 一、但是还是需要解释一下这些参数的依赖关系，才方便调用
 * 1.以下的参数分为好几组，用/////来分割
 * 2.大多数组的组内的参数存在依赖关系
 * 3.组间的参数也可能存在依赖关系
 * 4.组内的参数可能存在三类
 * 5.第一类参数叫必须参数，表示在创建此类的对象之后，需要通过其setter方法设置参数
 * 6.第二类参数叫可选参数，当必须参数为某些值的时候需要依赖这些可选参数
 * 7.第三类参数是自动生成的，没有做标识，当必须参数和可选参数填写正确的时候可以直接通过getter方法来获取
 * <p>
 * 二、如何知道必须参数和可选参数是否填写正确
 * 1.通过此类的String check()方法，如果返回null则填写正确，否者会返回错误提示
 * <p>
 * 三、String check()方法的提示不够详细，还是不知道缺了哪个参数怎么办
 * 1.用户会知道的了
 * <p>
 * 四、怎么有些必须参数是个不知道什么类
 * 1.dataSetParameter，DataSetParameter类是关于数据集参数的bean，由于到时候会有一个数据集结构的模板给用户下载参考的，
 * 所以直接通过无参的构造方法创建一个就好了，构造方法有各个参数的默认值，
 * 除非数据集的结构不一样，就需要通过有参的构造方法来创建
 * 2.hereditaryParameter，HereditaryParameter类是遗传算法参数的bean，里面有个无参的构造方法，有各个参数的默认值，
 * 但是这个默认值只是为了方便调试，这些参数应当由用户来设置
 */
public class RunParameter {
	private DataSetParameter dataSetParameter;//必须参数
	private File dataSetFile;//必须参数
	private DataSetSeparation dataSetSeparation;
	//////////////////////////////////////////////////
	private File trainOutputFile;//必须参数
	private File testOutputFile;//必须参数
	/////////////////////////////////////////////////
	private Integer improtenceAdjustNum;//必须参数
	private Double adjustD;//可选参数
	private ImprotenceAdjust improtenceAdjust;
	///////////////////////////////////////////////////
	private Integer subSpaceEvidenceSynthesisNum;//必须参数
	private Double thrf;//可选参数
	private Double thrnf;//可选参数
	private Double d1;//可选参数
	private Double d2;//可选参数
	private EvidenceSynthesis subSpaceEvidenceSynthesis;
	////////////////////////////////////////////////////
	private Double test;//必须参数
	private Double trainMiss;//必须参数
	private Double testMiss;//必须参数
	private Double label1;//必须参数
	private DataSet trainDataSet;
	private DataSet testDataSet;
	///////////////////////////////////////////////////////
	private Hereditary hereditary;
	/////////////////////////////////////////////////////
	private Integer parentChrosChooseNum;//必须参数
	private ParentChrosChoose parentChrosChoose;
	////////////////////////////////////////////////////////
	private Integer evaluationNum;//必须参数
	private HereditaryParameter hereditaryParameter;//必须参数
	private Evaluation evaluation;
	////////////////////////////////////////////////////////
	private Integer featureSeparationNum;//必须参数
	private Double separationValue;//可选参数
	private FeatureSeparation featureSeparation;
	///////////////////////////////////////////////////////
	private Integer subSpaceCreateNum;//必须参数
	private int[][] sns;//可选参数
	private int[] fnMins;//可选参数
	private Double stop;//可选参数
	private SubSpaceCreate subSpaceCreate;
	
	//////////////////////////////////////////////////////
	public String check() throws IOException, ClassNotFoundException {
		if (getTrainDataSet() == null || getTestDataSet() == null) {
			return "数据集异常";
		}
		if (getTrainOutputFile() == null || getTestOutputFile() == null) {
			return "数据集输出文件为空";
		}
		if (getHereditary() == null) {
			return "遗传算法异常";
		}
		if (getHereditaryParameter() == null) {
			return "遗传算法参数异常";
		}
		if (getParentChrosChoose() == null) {
			return "父母染色体选择算法参数异常";
		}
		if (getEvaluation() == null) {
			return "评价指标参数异常";
		}
		if (getSubSpaceCreate() == null) {
			return "子空间参数异常";
		}
		if (getSubSpaceEvidenceSynthesis() == null) {
			return "子空间合成参数异常";
		}
		return null;
	}
	
	///////////////////////////////////////////////////////
	public File getTrainOutputFile() {
		return trainOutputFile;
	}
	
	public void setTrainOutputFile(File trainOutputFile) {
		this.trainOutputFile = trainOutputFile;
	}
	
	public File getTestOutputFile() {
		return testOutputFile;
	}
	
	public void setTestOutputFile(File testOutputFile) {
		this.testOutputFile = testOutputFile;
	}
	
	public SubSpaceCreate getSubSpaceCreate() throws IOException, ClassNotFoundException {
		if (subSpaceCreate == null &&
				SubSpaceCreateFactory.check(getSubSpaceCreateNum(), getImprotenceAdjust(), getSns(), getFnMins(), getFeatureSeparation(),
						getStop(), getHereditary(), getHereditaryParameter(), getParentChrosChoose(), getEvaluation())) {
			subSpaceCreate = SubSpaceCreateFactory.createSubSpaceCreate(getSubSpaceCreateNum(), getImprotenceAdjust(), getSns(), getFnMins(),
					getFeatureSeparation(), getStop(), getHereditary(), getHereditaryParameter(), getParentChrosChoose(), getEvaluation());
		}
		return subSpaceCreate;
	}
	
	public void setSubSpaceCreate(SubSpaceCreate subSpaceCreate) {
		this.subSpaceCreate = subSpaceCreate;
	}
	
	public Integer getSubSpaceCreateNum() {
		return subSpaceCreateNum;
	}
	
	public void setSubSpaceCreateNum(Integer subSpaceCreateNum) {
		this.subSpaceCreateNum = subSpaceCreateNum;
	}
	
	public int[][] getSns() {
		return sns;
	}
	
	public void setSns(int[][] sns) {
		this.sns = sns;
	}
	
	public int[] getFnMins() {
		return fnMins;
	}
	
	public void setFnMins(int[] fnMins) {
		this.fnMins = fnMins;
	}
	
	public Double getStop() {
		return stop;
	}
	
	public void setStop(Double stop) {
		this.stop = stop;
	}
	
	public FeatureSeparation getFeatureSeparation() {
		if (featureSeparation == null && FeatureSeparationFactory.check(getFeatureSeparationNum(), getSeparationValue())) {
			featureSeparation = FeatureSeparationFactory.createFeatureSeparation(getFeatureSeparationNum(), getSeparationValue());
		}
		return featureSeparation;
	}
	
	public void setFeatureSeparation(FeatureSeparation featureSeparation) {
		this.featureSeparation = featureSeparation;
	}
	
	public Double getSeparationValue() {
		return separationValue;
	}
	
	public void setSeparationValue(Double separationValue) {
		this.separationValue = separationValue;
	}
	
	public Integer getFeatureSeparationNum() {
		return featureSeparationNum;
	}
	
	public void setFeatureSeparationNum(Integer featureSeparationNum) {
		this.featureSeparationNum = featureSeparationNum;
	}
	
	public Evaluation getEvaluation() throws IOException, ClassNotFoundException {
		if (evaluation == null && EvaluationFactory.check(getEvaluationNum(), getTrainDataSet(), getHereditaryParameter(), getParentChrosChoose())) {
			evaluation = EvaluationFactory.createEvaluation(getEvaluationNum(), getTrainDataSet(), getHereditaryParameter(), getParentChrosChoose());
		}
		return evaluation;
	}
	
	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
	
	public Integer getEvaluationNum() {
		return evaluationNum;
	}
	
	public void setEvaluationNum(Integer evaluationNum) {
		this.evaluationNum = evaluationNum;
	}
	
	public HereditaryParameter getHereditaryParameter() {
		return hereditaryParameter;
	}
	
	public void setHereditaryParameter(HereditaryParameter hereditaryParameter) {
		this.hereditaryParameter = hereditaryParameter;
	}
	
	public ParentChrosChoose getParentChrosChoose() {
		if (parentChrosChoose == null && ParentChrosChooseFactory.check(getParentChrosChooseNum())) {
			parentChrosChoose = ParentChrosChooseFactory.createParentChrosChoose(getParentChrosChooseNum());
		}
		return parentChrosChoose;
	}
	
	public void setParentChrosChoose(ParentChrosChoose parentChrosChoose) {
		this.parentChrosChoose = parentChrosChoose;
	}
	
	public Integer getParentChrosChooseNum() {
		return parentChrosChooseNum;
	}
	
	public void setParentChrosChooseNum(Integer parentChrosChooseNum) {
		this.parentChrosChooseNum = parentChrosChooseNum;
	}
	
	public Hereditary getHereditary() throws IOException {
		if (hereditary == null && getTrainDataSet() != null) {
			hereditary = new Hereditary(getTrainDataSet());
		}
		return hereditary;
	}
	
	public void setHereditary(Hereditary hereditary) {
		this.hereditary = hereditary;
	}
	
	public DataSet getTestDataSet() throws IOException {
		if (testDataSet == null && getDataSetSeparation() != null && getTest() != null && getTrainMiss() != null && getTestMiss() != null && getLabel1() != null) {
			DataSet[] dataSets = getDataSetSeparation().separationDataSet(getTest(), getTrainMiss(), getTestMiss(), getLabel1());
			trainDataSet = dataSets[0];
			testDataSet = dataSets[1];
		}
		return testDataSet;
	}
	
	public void setTestDataSet(DataSet testDataSet) {
		this.testDataSet = testDataSet;
	}
	
	public DataSet getTrainDataSet() throws IOException {
		if (trainDataSet == null && getDataSetSeparation() != null && getTest() != null && getTrainMiss() != null && getTestMiss() != null && getLabel1() != null) {
			DataSet[] dataSets = getDataSetSeparation().separationDataSet(getTest(), getTrainMiss(), getTestMiss(), getLabel1());
			trainDataSet = dataSets[0];
			testDataSet = dataSets[1];
		}
		return trainDataSet;
	}
	
	public void setTrainDataSet(DataSet trainDataSet) {
		this.trainDataSet = trainDataSet;
	}
	
	public Double getTest() {
		return test;
	}
	
	public void setTest(Double test) {
		this.test = test;
	}
	
	public Double getTrainMiss() {
		return trainMiss;
	}
	
	public void setTrainMiss(Double trainMiss) {
		this.trainMiss = trainMiss;
	}
	
	public Double getTestMiss() {
		return testMiss;
	}
	
	public void setTestMiss(Double testMiss) {
		this.testMiss = testMiss;
	}
	
	public Double getLabel1() {
		return label1;
	}
	
	public void setLabel1(Double label1) {
		this.label1 = label1;
	}
	
	public DataSetSeparation getDataSetSeparation() throws IOException {
		if (dataSetSeparation == null && getDataSetFile() != null && getDataSetParameter() != null) {
			dataSetSeparation = new DataSetSeparationImpl(getDataSetFile(), getDataSetParameter());
		}
		return dataSetSeparation;
	}
	
	public void setDataSetSeparation(DataSetSeparation dataSetSeparation) {
		this.dataSetSeparation = dataSetSeparation;
	}
	
	public DataSetParameter getDataSetParameter() {
		return dataSetParameter;
	}
	
	public void setDataSetParameter(DataSetParameter dataSetParameter) {
		this.dataSetParameter = dataSetParameter;
	}
	
	public File getDataSetFile() {
		return dataSetFile;
	}
	
	public void setDataSetFile(File dataSetFile) {
		this.dataSetFile = dataSetFile;
	}
	
	public EvidenceSynthesis getSubSpaceEvidenceSynthesis() {
		if (subSpaceEvidenceSynthesis == null && SubSpaceEvidenceSynthesisFactory.check(getSubSpaceEvidenceSynthesisNum(), getThrf(), getThrnf(), getD1(), getD2())) {
			subSpaceEvidenceSynthesis = SubSpaceEvidenceSynthesisFactory.createSubSpaceEvidenceSynthesis(getSubSpaceEvidenceSynthesisNum(), getThrf(), getThrnf(), getD1(), getD2());
		}
		return subSpaceEvidenceSynthesis;
	}
	
	public void setSubSpaceEvidenceSynthesis(EvidenceSynthesis subSpaceEvidenceSynthesis) {
		this.subSpaceEvidenceSynthesis = subSpaceEvidenceSynthesis;
	}
	
	public Integer getSubSpaceEvidenceSynthesisNum() {
		return subSpaceEvidenceSynthesisNum;
	}
	
	public void setSubSpaceEvidenceSynthesisNum(Integer subSpaceEvidenceSynthesisNum) {
		this.subSpaceEvidenceSynthesisNum = subSpaceEvidenceSynthesisNum;
	}
	
	public Double getThrf() {
		return thrf;
	}
	
	public void setThrf(Double thrf) {
		this.thrf = thrf;
	}
	
	public Double getThrnf() {
		return thrnf;
	}
	
	public void setThrnf(Double thrnf) {
		this.thrnf = thrnf;
	}
	
	public Double getD1() {
		return d1;
	}
	
	public void setD1(Double d1) {
		this.d1 = d1;
	}
	
	public Double getD2() {
		return d2;
	}
	
	public void setD2(Double d2) {
		this.d2 = d2;
	}
	
	public ImprotenceAdjust getImprotenceAdjust() {
		if (improtenceAdjust == null && ImprotenceAdjustFactory.check(getImprotenceAdjustNum(), getAdjustD())) {
			improtenceAdjust = ImprotenceAdjustFactory.createImprotenceAdjust(getImprotenceAdjustNum(), getAdjustD());
		}
		return improtenceAdjust;
	}
	
	public void setImprotenceAdjust(ImprotenceAdjust improtenceAdjust) {
		this.improtenceAdjust = improtenceAdjust;
	}
	
	public Integer getImprotenceAdjustNum() {
		return improtenceAdjustNum;
	}
	
	public void setImprotenceAdjustNum(Integer improtenceAdjustNum) {
		this.improtenceAdjustNum = improtenceAdjustNum;
	}
	
	public Double getAdjustD() {
		return adjustD;
	}
	
	public void setAdjustD(Double adjustD) {
		this.adjustD = adjustD;
	}
}
