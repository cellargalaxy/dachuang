package top.cellargalaxy.version5.run;


import org.json.JSONArray;
import org.json.JSONObject;
import top.cellargalaxy.version5.dataSet.*;
import top.cellargalaxy.version5.evaluation.Evaluation;
import top.cellargalaxy.version5.evaluation.EvaluationExecutor;
import top.cellargalaxy.version5.evaluation.EvaluationExecutorFactory;
import top.cellargalaxy.version5.evaluation.EvaluationFactory;
import top.cellargalaxy.version5.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.version5.evidenceSynthesis.SubSpaceEvidenceSynthesisFactory;
import top.cellargalaxy.version5.feature.FeatureSeparation;
import top.cellargalaxy.version5.feature.FeatureSeparationFactory;
import top.cellargalaxy.version5.hereditary.Hereditary;
import top.cellargalaxy.version5.hereditary.HereditaryParameter;
import top.cellargalaxy.version5.hereditary.ParentChrosChoose;
import top.cellargalaxy.version5.hereditary.ParentChrosChooseFactory;
import top.cellargalaxy.version5.subSpace.ImprotenceAdjust;
import top.cellargalaxy.version5.subSpace.ImprotenceAdjustFactory;
import top.cellargalaxy.version5.subSpace.SubSpaceCreate;
import top.cellargalaxy.version5.subSpace.SubSpaceCreateFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

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
    private static final DataSet EMPTY_DATA_SET = new DataSet(new LinkedList<Id>(), new LinkedList<Integer>(), new HashMap<String, Integer>());

    static {
        EMPTY_DATA_SET.getEvidenceNums().add(1);
        EMPTY_DATA_SET.getEvidNameToId().put("1", 1);

        LinkedList<double[]> evidences1 = new LinkedList<double[]>();
        evidences1.add(new double[]{1, 1, 0});
        EMPTY_DATA_SET.getIds().add(new Id("1", evidences1, 0));

        LinkedList<double[]> evidences2 = new LinkedList<double[]>();
        evidences2.add(new double[]{1, 0, 1});
        EMPTY_DATA_SET.getIds().add(new Id("2", evidences2, 1));
    }

    private DataSetParameter dataSetParameter;//必须参数
    private String dataSetFilePath;//可选参数，与trainDataSetPath和testDataSetPath选一
    private File dataSetFile;//可选参数
    private DataSetSeparation dataSetSeparation;//依赖dataSetParameter，dataSetFile

    /////////////////////////////////////////////////
    private Integer improtenceAdjustNum;//必须参数
    private Double adjustD;//可选参数
    private ImprotenceAdjust improtenceAdjust;//依赖improtenceAdjustNum，adjustD

    ///////////////////////////////////////////////////
    private Integer subSpaceEvidenceSynthesisNum;//必须参数
    private Double thrf;//可选参数
    private Double thrnf;//可选参数
    private Double d1;//可选参数
    private Double d2;//可选参数
    private EvidenceSynthesis subSpaceEvidenceSynthesis;//依赖subSpaceEvidenceSynthesisNum，thrf，thrnf，d1，d2

    ////////////////////////////////////////////////////
    private Double test;//必须参数
    private Double trainMiss;//必须参数
    private Double testMiss;//必须参数
    private Double label1;//必须参数
    private String trainDataSetPath;//可选参数，与dataSetFilePath选一
    private String testDataSetPath;//可选参数，与dataSetFilePath选一
    private File trainDataSetFile;//trainDataSetPath
    private File testDataSetFile;//依赖testDataSetPath
    private DataSet trainDataSet;//依赖trainDataSetFile/dataSetSeparation，test，trainMiss，testMiss，label1
    private DataSet testDataSet;//依赖testDataSetFile/dataSetSeparation，test，trainMiss，testMiss，label1

    ///////////////////////////////////////////////////////
    private Hereditary hereditary;//依赖trainDataSet

    /////////////////////////////////////////////////////
    private Integer parentChrosChooseNum;//必须参数
    private ParentChrosChoose parentChrosChoose;//依赖parentChrosChooseNum

    ////////////////////////////////////////////////////////
    private Integer evaluationNum;//必须参数
    private HereditaryParameter hereditaryParameter;//必须参数
    private Evaluation evaluation;//依赖evaluationNum，hereditary，hereditaryParameter，parentChrosChoose

    ////////////////////////////////////////////////////////
    private Integer featureSeparationNum;//必须参数
    private Double separationValue;//可选参数
    private FeatureSeparation featureSeparation;//依赖featureSeparationNum，separationValue

    //////////////////////////////////////////////////////
    private Integer evaluationExecutorNum;//必须参数
    private EvaluationExecutor evaluationExecutor;//依赖evaluationExecutorNum，evaluation

    ///////////////////////////////////////////////////////
    private Integer subSpaceCreateNum;//必须参数
    private int[][] sns;//可选参数
    private int[] fnMins;//可选参数
    private Double stop;//可选参数
    private SubSpaceCreate subSpaceCreate;//依赖subSpaceCreateNum，improtenceAdjust，sns，fnMins，featureSeparation，stop，hereditary，hereditaryParameter，parentChrosChoose，evaluationExecutor


    public RunParameter(DataSetParameter dataSetParameter, File dataSetFile) {
        this.dataSetParameter = dataSetParameter;
        this.dataSetFile = dataSetFile;
    }

    public RunParameter(DataSetParameter dataSetParameter, JSONObject jsonObject) {
        this.dataSetParameter = dataSetParameter;
        dataSetFilePath = jsonObject.optString("dataSetFilePath");
        trainDataSetPath = jsonObject.optString("trainDataSetPath");
        testDataSetPath = jsonObject.optString("testDataSetPath");

        improtenceAdjustNum = jsonObject.getInt("improtenceAdjustNum");//必须参数
        adjustD = jsonObject.optDouble("adjustD");//可选参数

        subSpaceEvidenceSynthesisNum = jsonObject.getInt("subSpaceEvidenceSynthesisNum");//必须参数
        thrf = jsonObject.optDouble("thrf");//可选参数
        thrnf = jsonObject.optDouble("thrnf");//可选参数
        d1 = jsonObject.optDouble("d1");//可选参数
        d2 = jsonObject.optDouble("d2");//可选参数

        test = jsonObject.getDouble("test");//必须参数
        trainMiss = jsonObject.getDouble("trainMiss");//必须参数
        testMiss = jsonObject.getDouble("testMiss");//必须参数
        label1 = jsonObject.getDouble("label1");//必须参数

        parentChrosChooseNum = jsonObject.getInt("parentChrosChooseNum");//必须参数

        evaluationNum = jsonObject.getInt("evaluationNum");//必须参数
        hereditaryParameter = HereditaryParameter.createHereditaryParameter(jsonObject.getJSONObject("hereditaryParameter"));//必须参数

        featureSeparationNum = jsonObject.getInt("featureSeparationNum");//必须参数
        separationValue = jsonObject.optDouble("separationValue");//可选参数

        subSpaceCreateNum = jsonObject.getInt("subSpaceCreateNum");//必须参数
        sns = jsonArrayToSns(jsonObject.optJSONArray("sns"));//可选参数
        fnMins = jsonArrayToInts(jsonObject.optJSONArray("fnMins"));//可选参数
        stop = jsonObject.optDouble("stop");//可选参数

        evaluationExecutorNum = jsonObject.getInt("evaluationExecutorNum");//必须参数
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("improtenceAdjustNum", improtenceAdjustNum);
        jsonObject.put("adjustD", adjustD);

        jsonObject.put("subSpaceEvidenceSynthesisNum", subSpaceEvidenceSynthesisNum);
        jsonObject.put("thrf", thrf);
        jsonObject.put("thrnf", thrnf);
        jsonObject.put("d1", d1);
        jsonObject.put("d2", d2);

        jsonObject.put("test", test);
        jsonObject.put("trainMiss", trainMiss);
        jsonObject.put("testMiss", testMiss);
        jsonObject.put("label1", label1);

        jsonObject.put("parentChrosChooseNum", parentChrosChooseNum);

        jsonObject.put("evaluationNum", evaluationNum);
        jsonObject.put("hereditaryParameter", hereditaryParameter.toJSONObject());

        jsonObject.put("featureSeparationNum", featureSeparationNum);
        jsonObject.put("separationValue", separationValue);

        jsonObject.put("evaluationExecutorNum", evaluationExecutorNum);

        jsonObject.put("subSpaceCreateNum", subSpaceCreateNum);
        jsonObject.put("sns", snsToJSONArray(sns));
        jsonObject.put("fnMins", intsToJSONArray(fnMins));
        jsonObject.put("stop", stop);

        return jsonObject;
    }

    private static int[][] jsonArrayToSns(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        int[][] sns = new int[jsonArray.length()][];
        for (int i = 0; i < jsonArray.length(); i++) {
            sns[i] = jsonArrayToInts(jsonArray.getJSONArray(i));
        }
        return sns;
    }

    private static int[] jsonArrayToInts(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        int[] ints = new int[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            ints[i] = jsonArray.getInt(i);
        }
        return ints;
    }

    private static JSONArray snsToJSONArray(int[][] sns) {
        if (sns == null) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (int[] sn : sns) {
            jsonArray.put(intsToJSONArray(sn));
        }
        return jsonArray;
    }

    private static JSONArray intsToJSONArray(int[] ints) {
        if (ints == null) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (int i : ints) {
            jsonArray.put(i);
        }
        return jsonArray;
    }

    //////////////////////////////////////////////////////
    public String check() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        if (getTrainDataSet() == null || getTestDataSet() == null) {
            return "数据集异常";
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
        if (getEvaluationExecutor() == null) {
            return "线程参数异常";
        }
        return null;
    }

    public String checkOnlyParameter() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        if (getTrainDataSet() == null) {
            setTrainDataSet(EMPTY_DATA_SET);
        }
        if (getTestDataSet() == null) {
            setTestDataSet(EMPTY_DATA_SET);
        }
        return check();
    }

    ///////////////////////////////////////////////////////

    public Integer getEvaluationExecutorNum() {
        return evaluationExecutorNum;
    }

    public void setEvaluationExecutorNum(Integer evaluationExecutorNum) {
        this.evaluationExecutorNum = evaluationExecutorNum;
    }

    public EvaluationExecutor getEvaluationExecutor() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        if (evaluationExecutor == null && EvaluationExecutorFactory.check(getEvaluationExecutorNum(), getEvaluation())) {
            evaluationExecutor = EvaluationExecutorFactory.createEvaluationExecutor(getEvaluationExecutorNum(), getEvaluation());
        }
        return evaluationExecutor;
    }

    public void setEvaluationExecutor(EvaluationExecutor evaluationExecutor) {
        this.evaluationExecutor = evaluationExecutor;
    }

    public SubSpaceCreate getSubSpaceCreate() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        if (subSpaceCreate == null &&
                SubSpaceCreateFactory.check(getSubSpaceCreateNum(), getImprotenceAdjust(), getSns(), getFnMins(), getFeatureSeparation(),
                        getStop(), getHereditary(), getHereditaryParameter(), getParentChrosChoose(), getEvaluationExecutor())) {
            subSpaceCreate = SubSpaceCreateFactory.createSubSpaceCreate(getSubSpaceCreateNum(), getImprotenceAdjust(), getSns(), getFnMins(),
                    getFeatureSeparation(), getStop(), getHereditary(), getHereditaryParameter(), getParentChrosChoose(), getEvaluationExecutor());
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

    public Evaluation getEvaluation() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        if (evaluation == null && EvaluationFactory.check(getEvaluationNum(), getHereditary(), getHereditaryParameter(), getParentChrosChoose())) {
            evaluation = EvaluationFactory.createEvaluation(getEvaluationNum(), getHereditary(), getHereditaryParameter(), getParentChrosChoose());
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
        if (testDataSet == null && getTestDataSetFile() != null && getDataSetParameter() != null) {
            testDataSet = new DataSet(getTestDataSetFile(), getDataSetParameter());
        }
        if (testDataSet == null && getDataSetSeparation() != null &&
                getTest() != null && getTest() > 0 && getTest() < 1 &&
                getTrainMiss() != null && getTrainMiss() > 0 && getTrainMiss() < 1 &&
                getTestMiss() != null && getTestMiss() > 0 && getTestMiss() < 1 &&
                getLabel1() != null && getLabel1() > 0 && getLabel1() < 1) {
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
        if (trainDataSet == null && getTrainDataSetFile() != null && getDataSetParameter() != null) {
            trainDataSet = new DataSet(getTrainDataSetFile(), getDataSetParameter());
        } else if (trainDataSet == null && getDataSetSeparation() != null &&
                getTest() != null && getTest() > 0 && getTest() < 1 &&
                getTrainMiss() != null && getTrainMiss() > 0 && getTrainMiss() < 1 &&
                getTestMiss() != null && getTestMiss() > 0 && getTestMiss() < 1 &&
                getLabel1() != null && getLabel1() > 0 && getLabel1() < 1) {
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
        if (dataSetFile == null && getDataSetFilePath() != null) {
            dataSetFile = new File(getDataSetFilePath());
        }
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

    public String getDataSetFilePath() {
        return dataSetFilePath;
    }

    public void setDataSetFilePath(String dataSetFilePath) {
        this.dataSetFilePath = dataSetFilePath;
    }

    public String getTrainDataSetPath() {
        return trainDataSetPath;
    }

    public void setTrainDataSetPath(String trainDataSetPath) {
        this.trainDataSetPath = trainDataSetPath;
    }

    public String getTestDataSetPath() {
        return testDataSetPath;
    }

    public void setTestDataSetPath(String testDataSetPath) {
        this.testDataSetPath = testDataSetPath;
    }

    public File getTrainDataSetFile() {
        if (trainDataSetFile == null && getTrainDataSetPath() != null && getTrainDataSetPath().length() > 0) {
            trainDataSetFile = new File(getTrainDataSetPath());
        }
        return trainDataSetFile;
    }

    public void setTrainDataSetFile(File trainDataSetFile) {
        this.trainDataSetFile = trainDataSetFile;
    }

    public File getTestDataSetFile() {
        if (testDataSetFile == null && getTestDataSetPath() != null && getTestDataSetPath().length() > 0) {
            testDataSetFile = new File(getTestDataSetPath());
        }
        return testDataSetFile;
    }

    public void setTestDataSetFile(File testDataSetFile) {
        this.testDataSetFile = testDataSetFile;
    }
}
