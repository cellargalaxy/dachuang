package top.cellargalaxy.dachuangspringboot.dataSet;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * @author cellargalaxy
 * @time 2019/1/16
 */
public class DataSetSplitFactory {
	private static DataSetSplit dataSetSplit;

	public static final DataSetSplit getDataSetSplit(RunParameter runParameter) {
		if (dataSetSplit == null) {
			dataSetSplit = createDataSetSplit(runParameter);
		}
		return dataSetSplit;
	}

	public static void setDataSetSplit(DataSetSplit dataSetSplit) {
		DataSetSplitFactory.dataSetSplit = dataSetSplit;
	}

	public static final DataSetSplit createDataSetSplit(RunParameter runParameter) {
		if (DataSetSplitImpl.NAME.equals(runParameter.getDataSetSplitName())) {
			dataSetSplit = new DataSetSplitImpl();
		} else if (VirtualDataSetSplit.NAME.equals(runParameter.getDataSetSplitName())) {
			dataSetSplit = new VirtualDataSetSplit();
		} else {
			dataSetSplit = new DataSetSplitImpl();
		}
		return dataSetSplit;
	}
}
