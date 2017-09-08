package version2.hereditary;

import util.Roulette;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class RouletteParentChrosChoose implements ParentChrosChoose {
	public double[][] chooseParentChros(double[] aucs, double[][] newChros) {
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
}
