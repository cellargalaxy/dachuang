package top.cellargalaxy.dachuangspringboot.hereditary;

import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
@Data
public class Chromosome {
	private final Map<Integer, Gene> geneMap;

	public Gene getGene(int evidenceId) {
		return geneMap.get(evidenceId);
	}

	public Collection<Gene> getGenes() {
		return geneMap.values();
	}

	@Override
	public Chromosome clone() {
		Map<Integer, Gene> geneMap = new HashMap<>();
		for (Map.Entry<Integer, Gene> entry : this.geneMap.entrySet()) {
			geneMap.put(entry.getKey(), entry.getValue().clone());
		}
		return new Chromosome(geneMap);
	}
}
