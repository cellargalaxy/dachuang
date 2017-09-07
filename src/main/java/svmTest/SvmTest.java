package svmTest;

import version1.dataSet.DataSet;

import java.io.File;
import java.io.IOException;

/**
 * Created by cellargalaxy on 17-9-7.
 * 试用SVM
 */
public class SvmTest {
	public static void main(String[] args) throws IOException {
		DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/trainAll.csv"),
				",", 0, 2, 3, 1, 5);
		MySvmTrain mySvmTrain=new MySvmTrain();
		File modelFile=new File("model.txt");
		mySvmTrain.run(trainDataSet,modelFile);
		
		File outputFile=new File("answer.txt");
		MySvmPredict.run(trainDataSet,modelFile,outputFile);
	}
}
