package feature;

import dataSet.DataSet;
import dataSet.Id;

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
                double a = 0;
                double b = 0;
                for (double[] doubles : dss) {
                    a += doubles[1];
                    b += doubles[2];
                }
                double[] ds = {0, a / dss.size(), b / dss.size()};
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
