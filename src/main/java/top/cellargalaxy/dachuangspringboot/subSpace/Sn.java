package top.cellargalaxy.dachuangspringboot.subSpace;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author cellargalaxy
 * @time 2019/1/14
 */
@Data
@AllArgsConstructor
public class Sn {
	private int[] sn;
	private int minFn;
	private int maxFn;

	public Sn(int minFn, int maxFn, int... sn) {
		this.minFn = minFn;
		this.maxFn = maxFn;
		this.sn = sn;
	}
}
