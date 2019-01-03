package top.cellargalaxy.dachuangspringboot.hereditary;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class OrderParentChrosChoose implements ParentChrosChoose {
	public final double[][] chooseParentChros(double[] aucs, double[][] newChros) {
		double[][] parentChros;
		if ((newChros.length - aucs.length) % 2 == 0) {
			parentChros = new double[newChros.length - aucs.length][];
		} else {
			parentChros = new double[newChros.length - aucs.length + 1][];
		}
		for (int i = 0; i < parentChros.length; i++) {
			parentChros[i] = newChros[i % aucs.length];
		}
		return parentChros;
	}
	
	@Override
	public String toString() {
		return "OrderParentChrosChoose{顺序父母染色体选择算法}";
	}
}
