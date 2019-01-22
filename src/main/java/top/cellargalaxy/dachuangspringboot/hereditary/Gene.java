package top.cellargalaxy.dachuangspringboot.hereditary;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
@Data
@AllArgsConstructor
public class Gene {
	private int evidenceId;
	private double[] bases;

	public double getBase(int index) {
		return bases[index];
	}

	@Override
	public Gene clone() {
		return new Gene(evidenceId, bases.clone());
	}
}
