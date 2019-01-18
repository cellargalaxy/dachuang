package top.cellargalaxy.dachuangspringboot.hereditary;

import lombok.Data;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
@Data
public class Gene {
	private final int evidenceId;
	private final double[] bases;

	public double getBase(int index) {
		return bases[index];
	}

	@Override
	public Gene clone() {
		return new Gene(evidenceId, bases.clone());
	}
}
