package runTest;

import version5.dataSet.DataSetParameter;
import version5.dataSet.DataSetSeparation;
import version5.evaluation.EvaluationFactory;
import version5.evaluation.EvaluationThreadPoolExecutor;
import version5.evidenceSynthesis.SubSpaceEvidenceSynthesisFactory;
import version5.feature.FeatureSeparationFactory;
import version5.hereditary.HereditaryParameter;
import version5.hereditary.ParentChrosChooseFactory;
import version5.run.Run;
import version5.run.RunParameter;
import version5.subSpace.ImprotenceAdjustFactory;
import version5.subSpace.SubSpaceCreateFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by cellargalaxy on 17-11-3.
 * 用于测试Run类
 */
public class RunTest {
	//老师说需要会同一套参数调用多次，调用的次数的这个参数我并没有，调用次数由调用处决定，反复调用一下过程就好
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//首先创建一个RunParameter对象，这个对象要怎么用，详细请看RunParameter的注释，建议看完RunParameter的注释再往下看
		RunParameter runParameter = new RunParameter();
		
		//设置数据集以及数据集的参数
		DataSetParameter dataSetParameter = new DataSetParameter();
		File dataSetFile = new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/all.csv");//数据集
		runParameter.setDataSetParameter(dataSetParameter);
		runParameter.setDataSetFile(dataSetFile);
		
		//获取DataSetSeparation对象，这个对象能获取刚才输入的数据集的四个参数，这四个参数的参考名字如下
		//他返回的是个数，如需要可以除一下得到个百分百什么的
		DataSetSeparation dataSetSeparation = runParameter.getDataSetSeparation();
		System.out.println("完整0标签个数: " + dataSetSeparation.getCom0Count());
		System.out.println("完整1标签个数: " + dataSetSeparation.getCom1Count());
		System.out.println("缺失0标签个数: " + dataSetSeparation.getMiss0Count());
		System.out.println("缺失1标签个数: " + dataSetSeparation.getMiss1Count());
		
		//////////////////////////////////////////////////////////
		//将以上四个参数给用户看完之后，下面用户就要设置参数了，参数的参考名字及其值域在其后面
		//////////////////////////////////////////////////////////
		
		//子数据集参数
		runParameter.setTest(0.5);//测试集比例(0,1)
		runParameter.setTrainMiss(0.1);//训练集缺失比例(0,1)
		runParameter.setTestMiss(0.2);//测试集缺失比例(0,1)
		runParameter.setLabel1(0.1);//1标签比例(0,1)
		
		//训练集和测试集结果输出文件，由调用处决定
		File trainOutputFile = new File("/home/cellargalaxy/trainSubSpaceDataSet.csv");
		File testOutputFile = new File("/home/cellargalaxy/testSubSpaceDataSet.csv");
		runParameter.setTrainOutputFile(trainOutputFile);
		runParameter.setTestOutputFile(testOutputFile);
		//特征调整算法，传入的是其工厂方法的参数，不同参数对应的参考名字请查看其工厂方法
		runParameter.setImprotenceAdjustNum(ImprotenceAdjustFactory.POWER_IMPROTENCE_ADJUST_NUM);
		//子空间合成算法，传入的是其工厂方法的参数，不同参数对应的参考名字请查看其工厂方法
		runParameter.setSubSpaceEvidenceSynthesisNum(SubSpaceEvidenceSynthesisFactory.AVERAGE_EVIDENCE_SYNTHESIS_NUM);
		//父母染色体选择算法，传入的是其工厂方法的参数，不同参数对应的参考名字请查看其工厂方法
		runParameter.setParentChrosChooseNum(ParentChrosChooseFactory.ROULETTE_PARENT_CHROS_CHOOSE_NUM);
		//评价算法，传入的是其工厂方法的参数，不同参数对应的参考名字请查看其工厂方法
		runParameter.setEvaluationNum(EvaluationFactory.AUC_NUM);
		//遗传算法参数
		HereditaryParameter hereditaryParameter = new HereditaryParameter();
		runParameter.setHereditaryParameter(hereditaryParameter);
		//特征分离算法，传入的是其工厂方法的参数，不同参数对应的参考名字请查看其工厂方法
		runParameter.setFeatureSeparationNum(FeatureSeparationFactory.AVERAGE_FEATURE_SEPARATION_NUM);
		//子空间，传入的是其工厂方法的参数，不同参数对应的参考名字请查看其工厂方法
		runParameter.setSubSpaceCreateNum(SubSpaceCreateFactory.SN_RANDOM_SUB_SPACE_CREATE_NUM);
		
		//一些测试用的可选参数
		int[][] sns = {{1}, {1, 2}, {1, 2, 3}, {1, 2, 3, 4}};
		int[] fnMins = {1, 3, 6, 13};
		runParameter.setSns(sns);
		runParameter.setFnMins(fnMins);
		runParameter.setStop(0.01);
		
		//检查参数设置是否有问题
		String string = runParameter.check();
		if (string != null) {
			System.out.println("check: " + string);
			System.exit(0);
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		Run.run(runParameter);
		
		//如果只是调用完以上，请不要调用这个线程池的关闭方法，直到整个程序结束为止
		EvaluationThreadPoolExecutor.shutdown();
	}
}
