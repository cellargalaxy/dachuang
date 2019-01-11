package top.cellargalaxy.dachuangspringboot.dataSet;

import java.io.File;
import java.io.IOException;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
public interface FromFileReadDataSet {
	DataSet readDataSetFromFile(File file, DataSetParameter dataSetParameter) throws IOException;
}
