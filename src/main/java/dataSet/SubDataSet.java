package dataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by cellargalaxy on 17-8-15.
 */
public class SubDataSet {
    private LinkedList<Id> ids;
    private LinkedList<Integer> evidenceNums;
    private Map<String, Integer> evidNameToId;
    private int com0Count;
    private int com1Count;
    private int miss0Count;
    private int miss1Count;

    public SubDataSet(File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol) throws IOException {
        ids = new LinkedList<Id>();
        evidenceNums = new LinkedList<Integer>();
        evidNameToId = new HashMap<String, Integer>();
        com0Count = 0;
        com1Count = 0;
        miss0Count = 0;
        miss1Count = 0;
        createIds(dataSetFile, separator, idClo, ACol, BCol, evidCol, labelCol);
        initCount();
    }

    public DataSet[] createSubDataSet(double test, double miss, double label1) {
        int addCom0Count=com0Count;
        int addCom1Count=com1Count;
        int addMiss0Count=miss0Count;
        int addMiss1Count=miss1Count;
        if ((double) (com0Count + miss0Count) / ids.size() > (1 - label1)) {
            addMiss0Count = (int) ((1 - label1) * ids.size() - com0Count);
            if (addMiss0Count < 0) {
                addCom0Count = com0Count + addMiss0Count;
                addMiss0Count = 0;
            }else {

            }
        } else {
            addMiss1Count = (int) (label1 * ids.size() - com1Count);
            if (addMiss1Count < 0) {
                addCom1Count = com1Count + addMiss1Count;
                addMiss1Count = 0;
            }
        }
        if (((double)(addMiss0Count+addMiss1Count)/ids.size())>miss) {
            addMiss0Count=(int)((addMiss0Count/(addMiss0Count+addMiss1Count))*miss*ids.size());
            addMiss1Count=(int)((addMiss1Count/(addMiss0Count+addMiss1Count))*miss*ids.size());
        }

        int addTestCom0Count=(int)(test*addCom0Count);
        int addTestCom1Count=(int)(test*addCom1Count);
        int addTestMiss0Count=(int)(test*addMiss0Count);
        int addTestMiss1Count=(int)(test*addMiss1Count);

        int addTrainCom0Count=addCom0Count-addTestCom0Count;
        int addTrainCom1Count=addCom1Count-addTestCom1Count;
        int addTrainMiss0Count=addMiss0Count-addTestMiss0Count;
        int addTrainMiss1Count=addMiss1Count-addTestMiss1Count;

        int yetTestCom0Count=0;
        int yetTestCom1Count=0;
        int yetTestMiss0Count=0;
        int yetTestMiss1Count=0;

        int yetTrainCom0Count=0;
        int yetTrainCom1Count=0;
        int yetTrainMiss0Count=0;
        int yetTrainMiss1Count=0;

        LinkedList<Id> trainIds=new LinkedList<Id>();
        LinkedList<Id> testIds=new LinkedList<Id>();
        for (Id id : ids) {
            if (id.getEvidences().size() == evidenceNums.size()) {
                if (id.getLabel() == Id.LABEL_0) {  //com0
                    if (yetTrainCom0Count<=addTrainCom0Count) {
                        trainIds.add(id);
                        yetTrainCom0Count++;
                    }else if (yetTestCom0Count<=addTestCom0Count){
                        testIds.add(id);
                        yetTestCom0Count++;
                    }
                } else {    //com1
                    if (yetTrainCom1Count<=addTrainCom1Count) {
                        trainIds.add(id);
                        yetTrainCom1Count++;
                    }else if (yetTestCom1Count<=addTestCom1Count){
                        testIds.add(id);
                        yetTestCom1Count++;
                    }
                }
            } else {
                if (id.getLabel() == Id.LABEL_0) {  //miss0
                    if (yetTrainMiss0Count<=addTrainMiss0Count) {
                        trainIds.add(id);
                        yetTrainMiss0Count++;
                    }else if (yetTestMiss0Count<=addTestMiss0Count){
                        testIds.add(id);
                        yetTestMiss0Count++;
                    }
                } else {    //miss1
                    if (yetTrainMiss1Count<=addTrainMiss1Count) {
                        trainIds.add(id);
                        yetTrainMiss1Count++;
                    }else if (yetTestMiss1Count<=addTestMiss1Count){
                        testIds.add(id);
                        yetTestMiss1Count++;
                    }
                }
            }
        }
        return new DataSet[]{new DataSet(trainIds,evidenceNums,evidNameToId),new DataSet(testIds,evidenceNums,evidNameToId)};
    }

    private void initCount() {
        for (Id id : ids) {
            if (id.getEvidences().size() == evidenceNums.size()) {
                if (id.getLabel() == Id.LABEL_0) {
                    com0Count++;
                } else if (id.getLabel() == Id.LABEL_1) {
                    com1Count++;
                } else {
                    throw new RuntimeException("数据集存在第三类标签:" + id.getLabel());
                }
            } else {
                if (id.getLabel() == Id.LABEL_0) {
                    miss0Count++;
                } else if (id.getLabel() == Id.LABEL_1) {
                    miss1Count++;
                } else {
                    throw new RuntimeException("数据集存在第三类标签:" + id.getLabel());
                }
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

    public int getCom0Count() {
        return com0Count;
    }

    public void setCom0Count(int com0Count) {
        this.com0Count = com0Count;
    }

    public int getCom1Count() {
        return com1Count;
    }

    public void setCom1Count(int com1Count) {
        this.com1Count = com1Count;
    }

    public int getMiss0Count() {
        return miss0Count;
    }

    public void setMiss0Count(int miss0Count) {
        this.miss0Count = miss0Count;
    }

    public int getMiss1Count() {
        return miss1Count;
    }

    public void setMiss1Count(int miss1Count) {
        this.miss1Count = miss1Count;
    }
}
