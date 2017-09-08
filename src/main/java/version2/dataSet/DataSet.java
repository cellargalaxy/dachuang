package version2.dataSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import version2.auc.My2DsCount;
import version2.auc.MyDsCount;
import version2.hereditary.Hereditary;
import version2.hereditary.HereditaryParameter;
import version2.auc.AucCount;
import version2.auc.DsCount;
import version2.hereditary.ParentChrosChoose;
import version2.hereditary.RouletteParentChrosChoose;

import java.io.*;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class DataSet implements AucCount,Serializable{
	private final LinkedList<Id> ids;
	private final LinkedList<Integer> evidenceNums;
	private final Map<String, Integer> evidNameToId;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DataSetParameter dataSetParameter=new DataSetParameter();
		dataSetParameter.setCoding("utf-8");
		dataSetParameter.setIdClo(0);
		dataSetParameter.setACol(2);
		dataSetParameter.setBCol(3);
		dataSetParameter.setEvidCol(1);
		dataSetParameter.setLabelCol(5);
		
		DataSet trainDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/trainAll.csv"),dataSetParameter);
		DsCount dsCount=new MyDsCount();
		HereditaryParameter hereditaryParameter=new HereditaryParameter();
		hereditaryParameter.setIterNum(500);
		hereditaryParameter.setSameNum(100);
		Hereditary hereditary=new Hereditary(hereditaryParameter,trainDataSet,dsCount);
		ParentChrosChoose parentChrosChoose=new RouletteParentChrosChoose();
		hereditary.evolution(parentChrosChoose);
		System.out.println("max auc:"+hereditary.getMaxAuc());
		System.out.println("max chro:");
		System.out.println(Arrays.toString(hereditary.getMaxChro()));
		
		DataSet testDataSet = new DataSet(new File("/media/cellargalaxy/根/内/大学/xi/dachuang/dataSet/testAll.csv"),dataSetParameter);
		System.out.println("orign test auc:"+testDataSet.countAuc(dsCount,new double[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}));
		System.out.println("h test auc:"+testDataSet.countAuc(dsCount,hereditary.getMaxChro()));
	}
	
	public DataSet(File dataSetFile,DataSetParameter dataSetParameter) throws IOException {
		ids = new LinkedList<Id>();
		evidenceNums = new LinkedList<Integer>();
		evidNameToId = new HashMap<String, Integer>();
		createIds(dataSetFile,dataSetParameter);
	}
	
	public DataSet(LinkedList<Id> ids, LinkedList<Integer> evidenceNums, Map<String, Integer> evidNameToId) {
		this.ids = ids;
		this.evidenceNums = evidenceNums;
		this.evidNameToId = evidNameToId;
	}
	
	/**
	 * 只保留evidences里指定的证据和全部包含evidences里指定的证据的对象
	 *
	 * @param evidences
	 */
	public void removeNotEqual(LinkedList<Integer> evidences) {
		Iterator<Id> iteratorId = ids.iterator();
		while (iteratorId.hasNext()) {
			Id id = iteratorId.next();
			if (id.getEvidences().size()<evidences.size()) {
				iteratorId.remove();
				continue;
			}
			Iterator<double[]> iteratorEvi=id.getEvidences().iterator();
			while (iteratorEvi.hasNext()) {
				if (!evidences.contains((int)(iteratorEvi.next()[0]))) {
					iteratorEvi.remove();
				}
				if (id.getEvidences().size()<evidences.size()) {
					iteratorId.remove();
					break;
				}
			}
		}
		Iterator<Integer> iteratorEvidenceNums=evidenceNums.iterator();
		while (iteratorEvidenceNums.hasNext()) {
			int evi=iteratorEvidenceNums.next();
			if (!evidences.contains(evi)) {
				iteratorEvidenceNums.remove();
			}
		}
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
				if ((int) (iterator.next()[0]) == evidenceNum) {
					iterator.remove();
					break;
				}
			}
		}
		evidenceNums.remove(new Integer(evidenceNum));/////////////////////////
		Iterator iterator = evidNameToId.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (entry.getValue().equals(evidenceNum)) {
				iterator.remove();
				break;
			}
		}
	}
	
	public double countAuc(DsCount dsCount) {
		double[] chro=new double[evidenceNums.size()*2];
		for (int i = 0; i < chro.length; i++) {
			chro[i]=1;
		}
		return countAuc(dsCount,chro);
	}
	
	public double countAuc(DsCount dsCount, double[] chro) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : ids) {
			double[] ds = dsCount.countDs(id,chro);
			if (id.getLabel()==Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		double auc = 0;
		for (double[] ds1 : ds1s) {
			for (double[] ds0 : ds0s) {
				if (ds1[1] > ds0[1]) {
					auc++;
				} else if (ds1[1] == ds0[1]) {
					auc = auc + 0.5;
				}
			}
		}
		return auc / ds1s.size() / ds0s.size();
	}
	
	public double countAuc(DsCount dsCount, Integer withoutEvidNum) {
		double[] chro=new double[evidenceNums.size()*2];
		for (int i = 0; i < chro.length; i++) {
			chro[i]=1;
		}
		return countAuc(dsCount,chro,withoutEvidNum);
	}
	
	public double countAuc(DsCount dsCount, double[] chro, Integer withoutEvidNum) {
		LinkedList<double[]> ds1s = new LinkedList<double[]>();
		LinkedList<double[]> ds0s = new LinkedList<double[]>();
		for (Id id : ids) {
			double[] ds = dsCount.countDs(id,chro,withoutEvidNum);
			if (id.getLabel()==Id.LABEL_0) {
				ds0s.add(ds);
			} else {
				ds1s.add(ds);
			}
		}
		double auc = 0;
		for (double[] ds1 : ds1s) {
			for (double[] ds0 : ds0s) {
				if (ds1[1] > ds0[1]) {
					auc++;
				} else if (ds1[1] == ds0[1]) {
					auc = auc + 0.5;
				}
			}
		}
		return auc / ds1s.size() / ds0s.size();
	}
	
	private void createIds(File dataSetFile, DataSetParameter dataSetParameter) throws IOException {
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(new BufferedReader(new InputStreamReader(new FileInputStream(dataSetFile),dataSetParameter.getCoding())));
		String id = null;
		int label = -1;
		LinkedList<double[]> evidences = null;
		for (CSVRecord record : records) {
			if (id == null) {
				id = record.get(dataSetParameter.getIdClo());
				label = new Integer(record.get(dataSetParameter.getLabelCol()));
				evidences = new LinkedList<double[]>();
			} else if (!id.equals(record.get(dataSetParameter.getIdClo()))) {
				ids.add(new Id(id, evidences, label));
				
				id = record.get(dataSetParameter.getIdClo());
				label = new Integer(record.get(dataSetParameter.getLabelCol()));
				evidences = new LinkedList<double[]>();
			}
			
			double[] evidence = {createEvidNum(record.get(dataSetParameter.getEvidCol())), new Double(record.get(dataSetParameter.getACol())), new Double(record.get(dataSetParameter.getBCol()))};
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
	
	public LinkedList<Integer> getEvidenceNums() {
		return evidenceNums;
	}
	
	public Map<String, Integer> getEvidNameToId() {
		return evidNameToId;
	}

}
