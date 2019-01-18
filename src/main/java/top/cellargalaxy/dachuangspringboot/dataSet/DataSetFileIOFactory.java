package top.cellargalaxy.dachuangspringboot.dataSet;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * @author cellargalaxy
 * @time 2019/1/16
 */
public class DataSetFileIOFactory {
	private static DataSetFileIO dataSetFileIO;

	public static final DataSetFileIO getDataSetFileIO(RunParameter runParameter) {
		if (dataSetFileIO == null) {
			dataSetFileIO = createDataSetFileIO(runParameter);
		}
		return dataSetFileIO;
	}

	public static void setDataSetFileIO(DataSetFileIO dataSetFileIO) {
		DataSetFileIOFactory.dataSetFileIO = dataSetFileIO;
	}

	public static final DataSetFileIO createDataSetFileIO(RunParameter runParameter) {
		dataSetFileIO = new DataSetCsvFileIO();
		return dataSetFileIO;
	}
}
