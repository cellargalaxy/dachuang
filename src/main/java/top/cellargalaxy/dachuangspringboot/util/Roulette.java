package top.cellargalaxy.dachuangspringboot.util;

import java.util.List;

/**
 * 轮盘算法
 * Created by cellargalaxy on 2017/5/7.
 */
public class Roulette {

	private static int roulette(List<Double> weights, double sum) {
		if (sum == 0) {
			return (int) (weights.size() * Math.random());
		}
		double point = sum * Math.random();
		double floor = 0;
		int i = 0;
		for (double d : weights) {
			floor += d;
			if (floor >= point) {
				return i;
			}
			i++;
		}
		throw new RuntimeException("轮盘异常");
	}

	private static int roulette(double[] weights, double sum) {
		if (sum == 0) {
			return (int) (weights.length * Math.random());
		}
		double point = sum * Math.random();
		double floor = 0;
		int i = 0;
		for (double d : weights) {
			floor += d;
			if (floor >= point) {
				return i;
			}
			i++;
		}
		throw new RuntimeException("轮盘异常");
	}
}
