package dataSet;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/4/22.
 * 嫌疑犯对象的类
 * id：嫌疑犯的id
 * evidences：对嫌疑犯的各条指控证据
 * 证据的数据结构：evidences的double[] ds=new double[3]：
 * ds[0]=证据编号，ds[1]=是（A）证据，ds[2]=否（B）证据
 * label：嫌疑犯的标签
 */
public class Id implements Serializable {
    public static final int DS_METHOD = 1;
    public static final int MY_DS_METHOD = 2;
    public static final int TEST_DS_METHOD = 3;

    public static final int LABEL_1 = 1;
    public static final int LABEL_0 = 0;
    private String id;
    private LinkedList<double[]> evidences;
    private int label;

    ///////////////////////////////
    private double[] evidenceDS;

    public Id(String id, LinkedList<double[]> evidences, int label) {
        this.id = id;
        this.evidences = evidences;
        this.label = label;
    }


    public double[] countDS(int DSMethodNum) {
        if (DSMethodNum == DS_METHOD) {
            return countDS();
        } else if (DSMethodNum == MY_DS_METHOD) {
            return countMyDS();
        } else if (DSMethodNum == TEST_DS_METHOD) {
            return evidenceDS;
        } else {
            throw new RuntimeException("ds合成方法编号异常:" + DSMethodNum);
        }
    }


    private double[] countMyDS() {
        if (evidences.size() == 0) {
            return null;
        } else if (evidences.size() == 1) {
            return evidences.getFirst();
        } else {
            double[] weights = new double[evidences.size()];
            int i = 0;
            for (double[] evidence : evidences) {
                weights[i] = Math.pow(Math.pow(evidence[1], 2) + Math.pow(evidence[2], 2), 0.5);
                i++;
            }
            double count = 0;
            for (double weight : weights) {
                count += weight;
            }
            double[] ds = {0, 0, 0};
            i = 0;
            for (double[] evidence : evidences) {
                ds[1] += evidence[1] * weights[i] / count;
                ds[2] += evidence[2] * weights[i] / count;
                i++;
            }
            return ds;
        }
    }

    private double[] countDS() {
        if (evidences.size() == 0) {
            return null;
        } else {
            Iterator<double[]> iterator = evidences.iterator();
            double[] ds1 = iterator.next();
            double[] ds2;
            while (iterator.hasNext()) {
                ds2 = iterator.next();
                ds1 = countE_E2E(ds1, ds2);
            }
            return ds1;
        }
    }

    /**
     * 某两条证据合成
     *
     * @param d1
     * @param d2
     * @return
     */
    private double[] countE_E2E(double[] d1, double[] d2) {
        double[] ds = new double[3];
        double K = countK(d1, d2);
        ds[1] = countA(d1, d2, K);
        ds[2] = countB(d1, d2, K);
        return ds;
    }

    private double countA(double[] d1, double[] d2, double K) {
        return (d1[1] * d2[1] + d1[1] * (1 - d2[1] - d2[2]) + d2[1] * (1 - d1[1] - d1[2])) / K;
    }

    private double countB(double[] d1, double[] d2, double K) {
        return (d1[2] * d2[2] + d1[2] * (1 - d2[1] - d2[2]) + d2[2] * (1 - d1[1] - d1[2])) / K;
    }

    private double countK(double[] d1, double[] d2) {
        return 1 - d1[1] * d2[2] - d1[2] * d2[1];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedList<double[]> getEvidences() {
        return evidences;
    }

    public void setEvidences(LinkedList<double[]> evidences) {
        this.evidences = evidences;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public double[] getEvidenceDS() {
        return evidenceDS;
    }

    public void setEvidenceDS(double[] evidenceDS) {
        this.evidenceDS = evidenceDS;
    }
}
