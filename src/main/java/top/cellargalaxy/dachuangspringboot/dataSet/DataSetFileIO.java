package top.cellargalaxy.dachuangspringboot.dataSet;

import java.io.File;
import java.io.IOException;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
public interface DataSetFileIO {
	DataSet readFileToDataSet(File file, DataSetParameter dataSetParameter) throws IOException;

	void writeDataSetToFile(DataSetParameter dataSetParameter, DataSet dataSet, File file) throws IOException;
}
