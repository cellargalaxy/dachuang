package version2.ds;

import version2.dataSet.DataSet;
import version2.dataSet.Id;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface AucCount {
	double countAuc(DataSet dataSet);
	
	double countAuc(DataSet dataSet, Integer withoutEvidNum);
	
	double countAuc(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException;
	
	double countIndexAuc(DataSet dataSet, double[] chro);//下标
	
	double countOrderAuc(DataSet cloneDataSet, double[] chro);//顺序
	
	double countIndexAuc(DataSet dataSet, Integer withoutEvidNum, double[] chro);//下标
	
	double countOrderAuc(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException;//顺序
}
