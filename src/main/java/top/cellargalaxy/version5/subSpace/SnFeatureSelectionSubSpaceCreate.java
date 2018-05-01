package top.cellargalaxy.version5.subSpace;


import top.cellargalaxy.util.Printer;
import top.cellargalaxy.version5.dataSet.DataSet;
import top.cellargalaxy.version5.evaluation.EvaluationExecutor;
import top.cellargalaxy.version5.feature.AucImprotence;
import top.cellargalaxy.version5.feature.FeatureSelection;
import top.cellargalaxy.version5.feature.FeatureSeparation;
import top.cellargalaxy.version5.hereditary.Hereditary;
import top.cellargalaxy.version5.hereditary.HereditaryParameter;
import top.cellargalaxy.version5.hereditary.ParentChrosChoose;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class SnFeatureSelectionSubSpaceCreate extends AbstractSubSpaceCreate {
    private final ImprotenceAdjust improtenceAdjust;
    private final int[][] sns;
    private final int[] fnMins;
    private final FeatureSeparation featureSeparation;
    private final double stop;
    private final Hereditary hereditary;
    private final HereditaryParameter hereditaryParameter;
    private final ParentChrosChoose parentChrosChoose;
    private final EvaluationExecutor evaluationExecutor;

    public SnFeatureSelectionSubSpaceCreate(ImprotenceAdjust improtenceAdjust, int[][] sns, int[] fnMins, FeatureSeparation featureSeparation, double stop, Hereditary hereditary, HereditaryParameter hereditaryParameter, ParentChrosChoose parentChrosChoose, EvaluationExecutor evaluationExecutor) {
        this.improtenceAdjust = improtenceAdjust;
        this.sns = sns;
        this.fnMins = fnMins;
        this.featureSeparation = featureSeparation;
        this.stop = stop;
        this.hereditary = hereditary;
        this.hereditaryParameter = hereditaryParameter;
        this.parentChrosChoose = parentChrosChoose;
        this.evaluationExecutor = evaluationExecutor;
    }

    public List<List<Integer>> createSubSpaces(DataSet dataSet) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        LinkedList<AucImprotence> aucImprotences = FeatureSelection.featureSelection(featureSeparation, stop, hereditary, hereditaryParameter, parentChrosChoose, evaluationExecutor);
        int len = aucImprotences.getLast().getEvidenceNum();
        List<Integer> features = new LinkedList<Integer>();
        double[] impros = new double[len];
        int i = 0;
        Printer.print("重要特征:");
        for (AucImprotence aucImprotence : aucImprotences) {
            features.add(aucImprotence.getEvidenceNum());
            impros[i] = aucImprotence.getAucD();

            Printer.print(aucImprotence.getEvidenceNum() + " " + aucImprotence.getAucD());
            i++;
            if (i >= len) {
                break;
            }
        }
        List<Double> listImpros = improtenceAdjust.adjustImprotence(impros);

        int[] sn = null;
        int fnMax = 0;
        int fnMin = -1;
        for (i = 0; i < sns.length; i++) {
            if (sns[i].length > features.size() || (sn != null && sns[i].length <= sn.length)) {
                continue;
            }
            int max = Integer.MIN_VALUE;
            for (int j = 0; j < sns[i].length; j++) {
                if (sns[i][j] > max) {
                    max = sns[i][j];
                }
            }
            if (max <= features.size()) {
                int count = 0;
                for (int j : sns[i]) {
                    count += countC(j, features.size());
                }
                if (fnMins[i] <= count) {
                    sn = sns[i];
                    fnMin = fnMins[i];
                    fnMax = count;
                }
            }
        }
        int fn = fnMin + (int) (Math.random() * (fnMax - fnMin));

        List<List<Integer>> subSpaces = new LinkedList<List<Integer>>();
        for (i = 0; i < fn; i++) {
            List<Integer> subSpace = createFeatureSelectionSubSpace(features, listImpros, sn[(int) (sn.length * Math.random())]);
            if (!siContainSubSpace(subSpaces, subSpace)) {
                subSpaces.add(subSpace);
            } else {
                i--;
            }
        }
        return subSpaces;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] sn : sns) {
            stringBuilder.append(Arrays.toString(sn));
        }
        return "SnFeatureSelectionSubSpaceCreate(sn特征选择子空间创建){" +
                "improtenceAdjust=" + improtenceAdjust +
                ", sns=" + stringBuilder.toString() +
                ", fnMins=" + Arrays.toString(fnMins) +
                ", featureSeparation=" + featureSeparation +
                ", stop=" + stop +
                '}';
    }
}
