package top.cellargalaxy.dachuangspringboot.subSpace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cellargalaxy
 * @time 2019/1/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sn {
	private int[] sn;
	private int fnMin;

	public Sn(int fnMin, int... sn) {
		this.fnMin = fnMin;
		this.sn = sn;
	}
}
