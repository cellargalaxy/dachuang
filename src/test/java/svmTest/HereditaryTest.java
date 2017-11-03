package svmTest;

import version5.dataSet.DataSet;
import version5.dataSet.DataSetParameter;
import version5.evaluation.Auc;
import version5.evaluation.Evaluation;
import version5.evaluation.EvaluationThreadPoolExecutor;
import version5.evidenceSynthesis.TeaEvidenceSynthesis;
import version5.hereditary.Hereditary;
import version5.hereditary.HereditaryParameter;
import version5.hereditary.OrderParentChrosChoose;
import version5.hereditary.ParentChrosChoose;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by cellargalaxy on 17-11-2.
 */
public class HereditaryTest {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		File dataSetFile = new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/subspacedata.csv");
//		DataSetParameter dataSetParameter = new DataSetParameter("utf-8", 0, 5, 4, 1, 2);
//		DataSetSeparation dataSetSeparation=new DataSetSeparationImpl(dataSetFile,dataSetParameter);
//
//		DataSet[] dataSets = dataSetSeparation.separationDataSet(0.5,0.2,0.5,0.1);
//		DataSet trainDataSet = dataSets[0];
//		DataSet testDataSet = dataSets[1];
		
		DataSetParameter dataSetParameter = new DataSetParameter("utf-8", 0, 5, 1, 2, 3);
		DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/trainAll.csv"), dataSetParameter);
		
		Hereditary hereditary=new Hereditary(trainDataSet);
		HereditaryParameter hereditaryParameter = new HereditaryParameter();
		hereditaryParameter.setIterNum(500);
		hereditaryParameter.setSameNum(100);
		ParentChrosChoose parentChrosChoose = new OrderParentChrosChoose();
		Evaluation evaluation=new Auc(new TeaEvidenceSynthesis());
		
		hereditary.evolution(hereditaryParameter,parentChrosChoose,evaluation,1);
		System.out.println(hereditary.getMaxAuc()+"\t"+Arrays.toString(hereditary.getMaxChro()));
//		System.out.println("---------------------------");
		
		hereditary.evolution(hereditaryParameter,parentChrosChoose,evaluation,1);
		System.out.println(hereditary.getMaxAuc()+"\t"+Arrays.toString(hereditary.getMaxChro()));
//		System.out.println("---------------------------");
		
		hereditary.evolution(hereditaryParameter,parentChrosChoose,evaluation,1);
		System.out.println(hereditary.getMaxAuc()+"\t"+Arrays.toString(hereditary.getMaxChro()));
//		System.out.println("---------------------------");
		
		hereditary.evolution(hereditaryParameter,parentChrosChoose,evaluation,1);
		System.out.println(hereditary.getMaxAuc()+"\t"+Arrays.toString(hereditary.getMaxChro()));
//		System.out.println("---------------------------");
		
		hereditary.evolution(hereditaryParameter,parentChrosChoose,evaluation,1);
		System.out.println(hereditary.getMaxAuc()+"\t"+Arrays.toString(hereditary.getMaxChro()));
//		System.out.println("---------------------------");
		
		EvaluationThreadPoolExecutor.shutdown();
	}
}
