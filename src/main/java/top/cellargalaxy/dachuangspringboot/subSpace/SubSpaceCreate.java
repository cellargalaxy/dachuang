package top.cellargalaxy.dachuangspringboot.subSpace;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by cellargalaxy on 17-10-1.
 */
public interface SubSpaceCreate {
	List<List<Integer>> createSubSpaces(DataSet dataSet) throws IOException, ExecutionException, InterruptedException;
}
