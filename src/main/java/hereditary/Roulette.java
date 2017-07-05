package hereditary;

import java.util.List;

/**
 * Created by cellargalaxy on 2017/5/7.
 * 轮盘算法静态类
 */
public class Roulette {
    public static int roulette(List<Double> ds) {
        double count = 0;
        for (double d : ds) {
            count += d;
        }
        return roulette(ds, count);
    }

    public static int roulette(double[] ds) {
        double count = 0;
        for (double d : ds) {
            count += d;
        }
        return roulette(ds, count);
    }

    public static int roulette(List<Double> ds, double count) {
        double point = count * Math.random();
        double floor = 0;
        int i = 0;
        for (double d : ds) {
            floor += d;
            if (floor > point) {
                return i;
            }
            i++;
        }
        throw new RuntimeException("轮盘异常");
    }

    public static int roulette(double[] ds, double count) {
        double point = count * Math.random();
        double floor = 0;
        int i = 0;
        for (double d : ds) {
            floor += d;
            if (floor > point) {
                return i;
            }
            i++;
        }
        throw new RuntimeException("轮盘异常");
    }
}
