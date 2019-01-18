package top.cellargalaxy.dachuangspringboot.dataSet;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * @author cellargalaxy
 * @time 2019/1/16
 */
public class DataSetSplitFactory {
	private static DataSetSplit dataSetSplit;

	public static final DataSetSplit createDataSetSplit(RunParameter runParameter) {
		dataSetSplit = new DataSetSplitImpl();
		return dataSetSplit;
	}

	public static final DataSetSplit getDataSetSplit() {
		return dataSetSplit;
	}
}
