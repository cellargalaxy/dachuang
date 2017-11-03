package svmTest;

import libsvm.svm_parameter;
import libsvm.svm_problem;
import mySvm.MySvmPredict;
import mySvm.MySvmTrain;
import version5.dataSet.DataSet;
import version5.dataSet.DataSetParameter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by cellargalaxy on 17-10-31.
 */
public class SvmTest {
	public static void main(String[] args) throws IOException {
		DataSetParameter dataSetParameter = new DataSetParameter("utf-8", 0, 5, 1, 2, 3);
		DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/trainAll.csv"), dataSetParameter);
		
		long t1=System.currentTimeMillis();
		svm_parameter parameter= MySvmTrain.createSvmDataSetParameter();
		svm_problem problem=MySvmTrain.exchengeDataSet(trainDataSet,parameter);
		problem=MySvmTrain.checkDataSet(problem,parameter);
		long tt1=System.currentTimeMillis();
		Writer writer=MySvmTrain.trainDataSet(problem,parameter);
		long tt2=System.currentTimeMillis();
		System.out.println(MySvmPredict.predict(problem,writer));
		long t2=System.currentTimeMillis();
		
		System.out.println(tt1-t1);
		System.out.println(tt2-tt1);
		System.out.println(t2-tt2);
		
//		long t3=System.currentTimeMillis();
//		EvidenceSynthesis evidenceSynthesis = new TeaEvidenceSynthesis();
//		Evaluation evaluation = new Auc(evidenceSynthesis);
//		System.out.println(evaluation.countEvaluation(trainDataSet));
//		long t4=System.currentTimeMillis();
//
//		System.out.println("svm time: "+(t2-t1));
//		System.out.println("auc time: "+(t4-t3));
	}
}
