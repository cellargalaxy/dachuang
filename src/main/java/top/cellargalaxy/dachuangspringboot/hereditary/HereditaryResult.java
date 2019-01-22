package top.cellargalaxy.dachuangspringboot.hereditary;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author cellargalaxy
 * @time 2019/1/11
 */
@Data
@AllArgsConstructor
public class HereditaryResult {
	private double evaluationValue;
	private Chromosome chromosome;
}
