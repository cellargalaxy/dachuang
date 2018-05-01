package top.cellargalaxy.version5.subSpace;


import top.cellargalaxy.version5.dataSet.DataSet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by cellargalaxy on 17-10-1.
 */
public interface SubSpaceCreate {
	List<List<Integer>> createSubSpaces(DataSet dataSet) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException;
}
