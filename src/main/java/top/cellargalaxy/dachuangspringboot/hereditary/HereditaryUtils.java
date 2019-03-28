package top.cellargalaxy.dachuangspringboot.hereditary;

import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.dataSet.Evidence;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
public class HereditaryUtils {
	public static final DataSet evolution(DataSet dataSet, Chromosome chromosome) {
		dataSet.getIds().forEach(id -> evolution(id, chromosome));
		return dataSet;
	}

	public static final Id evolution(Id id, Chromosome chromosome) {
		id.getEvidences().forEach(evidence -> evolution(evidence, chromosome.getGene(evidence.getEvidenceId())));
		return id;
	}

	public static final Evidence evolution(Evidence evidence, Gene gene) {
		if (gene == null) {
			return evidence;
		}
		for (int i = 0; i < evidence.getValues().length; i++) {
			evidence.getValues()[i] *= gene.getBase(i);
		}
		return evidence;
	}
}
