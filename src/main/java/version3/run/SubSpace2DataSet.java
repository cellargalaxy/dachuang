//package version3.run;
//
//import version3.dataSet.DataSet;
//import version3.dataSet.Id;
//import version3.evidenceSynthesis.EvidenceSynthesis;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//
///**
// * Created by cellargalaxy on 17-9-9.
// */
//public class SubSpace2DataSet {
//	public static final DataSet subSpace2DataSet(Map<DataSet, double[]> subSpaceMap, EvidenceSynthesis evidenceSynthesis) throws IOException, ClassNotFoundException {
//		Map<String, Id> map = new HashMap<String, Id>();
//		for (Map.Entry<DataSet, double[]> entry : subSpaceMap.entrySet()) {
//			double[] chro = entry.getValue();
//			for (Id id : entry.getKey().getIds()) {
//				Id newId = map.get(id.getId());
//				if (newId == null) {
//					newId = new Id(id.getId(), new LinkedList<double[]>(), id.getLabel());
//					map.put(id.getId(), newId);
//				}
//				newId.getEvidences().add(evidenceSynthesis.synthesisEvidenceOrder(id, chro));
//			}
//		}
//
//		LinkedList<Id> ids = new LinkedList<Id>();
//		for (Map.Entry<String, Id> entry : map.entrySet()) {
//			ids.add(entry.getValue());
//		}
//		return new DataSet(ids, null, null);
//	}
//}
