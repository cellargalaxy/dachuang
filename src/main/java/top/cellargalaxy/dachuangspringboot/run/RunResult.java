package top.cellargalaxy.dachuangspringboot.run;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;

import java.util.Collection;

/**
 * @author cellargalaxy
 * @time 2019/1/22
 */
@Data
@AllArgsConstructor
public class RunResult {
	private Collection<Integer> subSpace;
	private DataSet dataSet;
	private Chromosome chromosome;
}
