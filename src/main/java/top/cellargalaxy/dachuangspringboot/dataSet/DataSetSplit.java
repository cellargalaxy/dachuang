package top.cellargalaxy.dachuangspringboot.dataSet;

/**
 * Created by cellargalaxy on 17-9-28.
 */
public interface DataSetSplit {
	DataSetSplit splitDataSet(double test, double trainMiss, double testMiss, double label1);

	DataSet getTrainDataSet();

	DataSet getTestDataSet();
}
