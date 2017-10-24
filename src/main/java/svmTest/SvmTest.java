package svmTest;


import libsvm.svm_problem;
import version4.dataSet.DataSet;
import version4.dataSet.DataSetParameter;

import java.io.*;

/**
 * Created by cellargalaxy on 17-9-7.
 * 试用SVM
 */
public class SvmTest {
	public static void main(String[] args) throws IOException {
		DataSetParameter dataSetParameter = new DataSetParameter("utf-8", 0, 5, 1, 2, 3);
		DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/trainAll.csv"), dataSetParameter);
		Writer writer = new StringWriter();
		svm_problem problem = MySvmTrain.run(trainDataSet, writer);
		System.out.println(MySvmPredict.run(problem, writer));
	}
}
