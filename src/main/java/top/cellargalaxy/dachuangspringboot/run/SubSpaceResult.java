package top.cellargalaxy.dachuangspringboot.run;

import lombok.Data;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryResult;

import java.util.Collection;

/**
 * @author cellargalaxy
 * @time 2019/1/22
 */
@Data
public class SubSpaceResult {
	private final Collection<Integer> subSpace;
	private final DataSet dataSet;
	private final Chromosome chromosome;
}
