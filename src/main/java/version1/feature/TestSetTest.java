package version1.feature;

import version1.dataSet.DataSet;
import version1.dataSet.Id;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by cellargalaxy on 2017/6/9.
 * 对测试数据集进行测试
 */
public class TestSetTest {
    public final static int AVER_SYNTHESIS = 1;
    public final static int VOTE_SYNTHESIS = 2;

    public static void DSSubSpace(int resultSynthesisMethodNum, int DSMethonNum, LinkedList<DataSet> dataSets, DataSet dataSet1, double thrf, double thrnf, double d1, double d2) {
        Map<String, LinkedList<double[]>> map = new HashMap<String, LinkedList<double[]>>();
        for (DataSet dataSet : dataSets) {
            for (Id id : dataSet.getIds()) {
                LinkedList<double[]> dss = map.get(id.getId());
                if (dss == null) {
                    dss = new LinkedList<double[]>();
                    map.put(id.getId(), dss);
                }
                dss.add(id.countDS(DSMethonNum));
            }
        }
        if (resultSynthesisMethodNum == AVER_SYNTHESIS) {
            averSynthesis(dataSet1, map);
        } else if (resultSynthesisMethodNum == VOTE_SYNTHESIS) {
            voteSynthesis(dataSet1, map, thrf, thrnf, d1, d2);
        } else {
            throw new RuntimeException("多个推理结果的合成方法编号异常:" + resultSynthesisMethodNum);
        }
    }

    private static void averSynthesis(DataSet dataSet, Map<String, LinkedList<double[]>> map) {
        for (Id id : dataSet.getIds()) {
            LinkedList<double[]> dss = map.get(id.getId());
            if (dss == null) {
                double[] ds = {0, 0, 0};
                id.setEvidenceDS(ds);
            } else {
//                double a = 0;
//                double b = 0;
//                for (double[] doubles : dss) {
//                    a += doubles[1];
//                    b += doubles[2];
//                }
//                double[] ds = {0, a / dss.size(), b / dss.size()};
                int aCount=0;
                int bCount=0;
                double[] weights = new double[dss.size()];
                int i = 0;
                for (double[] evidence : dss) {
                    weights[i] = Math.pow(Math.pow(evidence[1], 2) + Math.pow(evidence[2], 2), 0.5)*Math.abs(evidence[1]-evidence[2]);
                    if (evidence[1]>evidence[2]) {
                        aCount++;
                    }else if (evidence[1]<evidence[2]){
                        bCount++;
                    }
                    i++;
                }
                double[] ds={0,0,0};
                double count = (aCount+bCount);
                for (double weight : weights) {
                    count += weight;
                }
                i=0;
                for (double[] evidence : dss) {
                    ds[1] += evidence[1] * weights[i] * aCount / count / count;
                    ds[2] += evidence[2] * weights[i] * bCount / count / count;
                    i++;
                }
                id.setEvidenceDS(ds);
            }
        }
    }

    private static void voteSynthesis(DataSet dataSet, Map<String, LinkedList<double[]>> map, double thrf, double thrnf, double d1, double d2) {
        for (Id id : dataSet.getIds()) {
            LinkedList<double[]> dss = map.get(id.getId());
            if (dss == null) {
                double[] ds = {0, 0, 0};
                id.setEvidenceDS(ds);
            } else {
                double a = 0;
                double b = 0;
                for (double[] doubles : dss) {
                    if (doubles[1] > thrf) {
                        a++;
                    } else if (doubles[2] < thrnf) {
                        b++;
                    }
                }
                if (a > b) {
                    double[] ds = {0, (a - b) / (a + b) * d1, 0};
                    id.setEvidenceDS(ds);
                } else if (a < b) {
                    double[] ds = {0, 0, (b - a) / (a + b) * d2};
                    id.setEvidenceDS(ds);
                } else {
                    double[] ds = {0, 0, 0};
                    id.setEvidenceDS(ds);
                }
            }
        }
    }
}
