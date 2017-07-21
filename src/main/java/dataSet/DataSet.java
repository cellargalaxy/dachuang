package dataSet;


import java.io.*;
import java.util.*;

/**
 * Created by cellargalaxy on 2017/4/22.
 * 数据集对象类
 * ids：嫌疑犯的链表
 * evidenceCount：对于某个嫌疑犯而言，最多有多少个证据
 * evidNameToId：各个证据的证据名与证据Id的Map
 */
public class DataSet implements Serializable {
    private LinkedList<Id> ids;
    private LinkedList<Integer> evidenceNums;
    private Map<String, Integer> evidNameToId;


    public DataSet(File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol) throws IOException {
        ids = new LinkedList<Id>();
        evidenceNums = new LinkedList<Integer>();
        evidNameToId = new HashMap<String, Integer>();
        createIds(dataSetFile, separator, idClo, ACol, BCol, evidCol, labelCol);
    }

    /**
     * 只保留evidences里指定的证据和全部包含evidences里指定的证据的对象
     *
     * @param evidences
     */
    public void allSaveEvidence(LinkedList<Integer> evidences) {
        Iterator<Id> iteratorId = ids.iterator();
        while (iteratorId.hasNext()) {
            Id id = iteratorId.next();
            int count = 0;
            Iterator<double[]> iteratorEvidence = id.getEvidences().iterator();
            while (iteratorEvidence.hasNext()) {
                double[] evidence = iteratorEvidence.next();
                if (evidences.contains((int) evidence[0])) {
                    count++;
                } else {
                    iteratorEvidence.remove();
                }
            }
            if (count != evidences.size()) {
                iteratorId.remove();
            }
        }
        evidenceNums = evidences;
        Iterator iterator = evidNameToId.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (!evidences.contains(entry.getValue())) {
                iterator.remove();
            }
        }
    }

    /**
     * 移除编号为evidenceNum的证据
     *
     * @param evidenceNum
     */
    public void removeEvidence(int evidenceNum) {
        for (Id id : ids) {
            Iterator<double[]> iterator = id.getEvidences().iterator();
            while (iterator.hasNext()) {
                double[] evidence = iterator.next();
                if ((int) evidence[0] == evidenceNum) {
                    iterator.remove();
                    break;
                }
            }
        }
        evidenceNums.removeFirstOccurrence(evidenceNum);
        Iterator iterator = evidNameToId.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getValue().equals(evidenceNum)) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * 数据集的各个对象乘以染色体
     *
     * @param chro
     */
    public void mulChro(double[] chro) {
        for (Id id : ids) {
            int i = 0;
            for (double[] evidence : id.getEvidences()) {
                evidence[1] *= chro[i * 2];
                evidence[2] *= chro[i * 2 + 1];
                i++;
            }
        }
    }


    private void createIds(File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(dataSetFile));

        String id = null;
        int label = -1;
        LinkedList<double[]> evidences = null;

        String string;
        while ((string = reader.readLine()) != null) {
            String[] strings = string.split(separator);

            if (id == null) {
                id = strings[idClo];
                label = new Integer(strings[labelCol]);
                evidences = new LinkedList<double[]>();
            } else if (!id.equals(strings[idClo])) {
                ids.add(new Id(id, evidences, label));

                id = strings[idClo];
                label = new Integer(strings[labelCol]);
                evidences = new LinkedList<double[]>();
            }

            double[] evidence = {createEvidNum(strings[evidCol]), new Double(strings[ACol]), new Double(strings[BCol])};
            evidences.add(evidence);
        }
        if (id != null) {
            ids.add(new Id(id, evidences, label));
        }
    }

    private int createEvidNum(String evidName) {
        Integer i = evidNameToId.get(evidName);
        if (i == null) {
            i = evidNameToId.size() + 1;
            evidNameToId.put(evidName, i);
            evidenceNums.add(i);
        }
        return i;
    }

    public LinkedList<Id> getIds() {
        return ids;
    }

    public void setIds(LinkedList<Id> ids) {
        this.ids = ids;
    }

    public LinkedList<Integer> getEvidenceNums() {
        return evidenceNums;
    }

    public void setEvidenceNums(LinkedList<Integer> evidenceNums) {
        this.evidenceNums = evidenceNums;
    }

    public Map<String, Integer> getEvidNameToId() {
        return evidNameToId;
    }

    public void setEvidNameToId(Map<String, Integer> evidNameToId) {
        this.evidNameToId = evidNameToId;
    }
}
