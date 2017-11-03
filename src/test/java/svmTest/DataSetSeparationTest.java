package svmTest;

import version5.dataSet.DataSet;
import version5.dataSet.DataSetParameter;
import version5.dataSet.DataSetSeparation;
import version5.dataSet.DataSetSeparationImpl;

import java.io.File;
import java.io.IOException;

/**
 * Created by cellargalaxy on 17-11-1.
 */
public class DataSetSeparationTest {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DataSetParameter dataSetParameter = new DataSetParameter();
		File dataSetFile = new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/all.csv");
		DataSetSeparation dataSetSeparation=new DataSetSeparationImpl(dataSetFile,dataSetParameter);
		
		DataSet[] dataSets=dataSetSeparation.separationDataSet(0.3,0.1,0.2,0.3);
		System.out.println(dataSets[0].getIds().size());
	}
}
