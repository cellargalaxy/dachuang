package top.cellargalaxy.version5.hereditary;


import top.cellargalaxy.util.Roulette;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class RouletteParentChrosChoose implements ParentChrosChoose {
	public final double[][] chooseParentChros(double[] aucs, double[][] newChros) {
		double[][] parentChros;
		if ((newChros.length - aucs.length) % 2 == 0) {
			parentChros = new double[newChros.length - aucs.length][];
		} else {
			parentChros = new double[newChros.length - aucs.length + 1][];
		}
		double aucCount = 0;
		for (double auc : aucs) {
			aucCount += auc;
		}
		for (int i = 0; i < parentChros.length; i++) {
			parentChros[i] = newChros[Roulette.roulette(aucs, aucCount)];
		}
		return parentChros;
	}
	
	@Override
	public String toString() {
		return "RouletteParentChrosChoose{轮盘父母染色体选择算法}";
	}
}
