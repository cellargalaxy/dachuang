package top.cellargalaxy.dachuangspringboot.hereditary;

import java.util.ArrayList;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class OrderParentChrosChoose implements ParentChrosChoose {
	public static final String NAME = "顺序父母染色体选择算法";

	@Override
	public Chromosome[][] chooseParentChros(ArrayList<HereditaryResult> hereditaryResults, int parentChrosNum) {
		Chromosome[][] parentChros = new Chromosome[parentChrosNum][];
		for (int i = 0; i < parentChros.length; i++) {
			parentChros[i] = new Chromosome[2];
			parentChros[i][0] = hereditaryResults.get((i * 2) % hereditaryResults.size()).getChromosome();
			parentChros[i][1] = hereditaryResults.get((i * 2 + 1) % hereditaryResults.size()).getChromosome();
		}
		return parentChros;
	}

	@Override
	public String toString() {
		return "顺序父母染色体选择算法";
	}


}
