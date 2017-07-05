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
public class DataSet implements Serializable{
	private LinkedList<Id> ids;
	private int evidenceCount;
	private Map<String, Integer> evidNameToId;

	public static void main(String[] args) throws IOException {
		double[] ds11={1,1,1};
		double[] ds12={2,1,1};
		double[] ds13={3,1,1};
		LinkedList<double[]> evidences1=new LinkedList<>();
		evidences1.add(ds11);
		evidences1.add(ds12);
		evidences1.add(ds13);
		Id id1=new Id(null,evidences1,1);

		double[] ds21={1,2,2};
//		double[] ds22={2,2,2};
		double[] ds23={3,2,2};
		LinkedList<double[]> evidences2=new LinkedList<>();
		evidences2.add(ds21);
//		evidences2.add(ds22);
		evidences2.add(ds23);
		Id id2=new Id(null,evidences2,2);

//		double[] ds31={1,3,3};
		double[] ds32={2,3,3};
		double[] ds33={3,3,3};
		LinkedList<double[]> evidences3=new LinkedList<>();
//		evidences3.add(ds31);
		evidences3.add(ds32);
		evidences3.add(ds33);
		Id id3=new Id(null,evidences3,3);

		LinkedList<Id> ids=new LinkedList<>();
		ids.add(id1);
		ids.add(id2);
		ids.add(id3);

		DataSet dataSet=new DataSet(ids,3,null);
		for (Id id : dataSet.getIds()) {
			System.out.println(id.getLabel());
			for (double[] doubles : id.getEvidences()) {
				System.out.println(Arrays.toString(doubles));
			}
		}
		LinkedList<Integer> evidenceNums=new LinkedList<Integer>();
		evidenceNums.add(2);
		evidenceNums.add(3);
		dataSet.allSaveEvidence(evidenceNums);
		System.out.println("==========================");
		for (Id id : dataSet.getIds()) {
			System.out.println(id.getLabel());
			for (double[] doubles : id.getEvidences()) {
				System.out.println(Arrays.toString(doubles));
			}
		}
	}
	
	public DataSet(File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol) throws IOException {
		evidenceCount = -1;
		evidNameToId = new HashMap<String, Integer>();
		ids = createIds(dataSetFile, separator, idClo, ACol, BCol, evidCol, labelCol);
	}
	
	public DataSet(LinkedList<Id> ids, int evidenceCount, Map<String, Integer> evidNameToId) {
		this.ids = ids;
		this.evidenceCount = evidenceCount;
		this.evidNameToId = evidNameToId;
	}

	public void allSaveEvidence(LinkedList<Integer> evidenceNums){
		Iterator<Id> iteratorId=ids.iterator();
		while (iteratorId.hasNext()) {
			Id id=iteratorId.next();
			int count=0;
			Iterator<double[]> iteratorEvidence=id.getEvidences().iterator();
			w2:while (iteratorEvidence.hasNext()) {
				double[] evidence=iteratorEvidence.next();
				for (Integer evidenceNum : evidenceNums) {
					if (evidenceNum.equals((int)evidence[0])) {
						count++;
						continue w2;
					}
				}
				iteratorEvidence.remove();
			}
			if (count!=evidenceNums.size()) {
				iteratorId.remove();
			}
		}
		evidenceCount=evidenceNums.size();
	}

	/**
	 * 数据集的各个对象乘以染色体
	 * @param chro
	 */
	public void mulChro(double[] chro){
		for (Id id : ids) {
			int i=0;
			for (double[] evidence : id.getEvidences()) {
				evidence[1] *= chro[i*2];
				evidence[2] *= chro[i*2+1];
				i++;
			}
		}
	}

//	public void removeMissingId(){
//		Iterator<Id> iterator=ids.iterator();
//		while (iterator.hasNext()) {
//			if (iterator.next().getEvidences().size()<evidenceCount) {
//				iterator.remove();
//			}
//		}
//	}
	
	private LinkedList<Id> createIds(File dataSetFile, String separator, int idClo, int ACol, int BCol, int evidCol, int labelCol) throws IOException {
		LinkedList<Id> ids = new LinkedList<Id>();
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
				if (evidences.size() > evidenceCount) evidenceCount = evidences.size();
				ids.add(new Id(id, evidences, label));
				
				id = strings[idClo];
				label = new Integer(strings[labelCol]);
				evidences = new LinkedList<double[]>();
			}
			
			double[] evidence = {createEvidNum(strings[evidCol]), new Double(strings[ACol]), new Double(strings[BCol])};
			evidences.add(evidence);
		}
		if (id != null) {
			if (evidences.size() > evidenceCount) evidenceCount = evidences.size();
			ids.add(new Id(id, evidences, label));
		}
		return ids;
	}
	
	private int createEvidNum(String evidName) {
		Integer i = evidNameToId.get(evidName);
		if (i == null) {
			i = evidNameToId.size()+1;
			evidNameToId.put(evidName, i);
		}
		return i;
	}
	
	public LinkedList<Id> getIds() {
		return ids;
	}
	
	public void setIds(LinkedList<Id> ids) {
		this.ids = ids;
	}
	
	public int getEvidenceCount() {
		return evidenceCount;
	}
	
	public void setEvidenceCount(int evidenceCount) {
		this.evidenceCount = evidenceCount;
	}
	
	public Map<String, Integer> getEvidNameToId() {
		return evidNameToId;
	}
	
	public void setEvidNameToId(Map<String, Integer> evidNameToId) {
		this.evidNameToId = evidNameToId;
	}
}
