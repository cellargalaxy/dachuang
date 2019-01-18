package top.cellargalaxy.dachuangspringboot.dataSet;

/**
 * Created by cellargalaxy on 17-9-28.
 */
public interface DataSetSplit {
	DataSet[] splitDataSet(DataSet dataSet, double testPro, double trainMissPro, double testMissPro, double trainLabel1Pro, double testLabel1Pro);
}
