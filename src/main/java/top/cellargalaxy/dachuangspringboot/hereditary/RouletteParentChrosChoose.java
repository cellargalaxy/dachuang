package top.cellargalaxy.dachuangspringboot.hereditary;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class RouletteParentChrosChoose implements ParentChrosChoose {
	public static final String NAME = "轮盘父母染色体选择算法";

	@Override
	public Chromosome[][] chooseParentChros(ArrayList<HereditaryResult> hereditaryResults, int parentChrosNum) {
		Chromosome[][] parentChros = new Chromosome[parentChrosNum][];
		double sum = hereditaryResults.stream().mapToDouble(HereditaryResult::getEvaluationValue).sum();
		for (int i = 0; i < parentChros.length; i++) {
			parentChros[i] = new Chromosome[2];
			parentChros[i][0] = hereditaryResults.get(roulette(hereditaryResults, sum)).getChromosome();
			parentChros[i][1] = hereditaryResults.get(roulette(hereditaryResults, sum)).getChromosome();
		}
		return parentChros;
	}

	private int roulette(List<HereditaryResult> hereditaryResults, double sum) {
		if (sum == 0) {
			return (int) (hereditaryResults.size() * Math.random());
		}
		double point = sum * Math.random();
		double floor = 0;
		int i = 0;
		for (HereditaryResult hereditaryResult : hereditaryResults) {
			floor += hereditaryResult.getEvaluationValue();
			if (floor >= point) {
				return i;
			}
			i++;
		}
		throw new RuntimeException("轮盘异常");
	}

	@Override
	public String toString() {
		return "轮盘父母染色体选择算法";
	}
}
