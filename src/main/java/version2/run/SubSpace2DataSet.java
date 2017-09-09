package version2.run;

import util.CloneObject;
import version2.dataSet.DataSet;
import version2.dataSet.Id;
import version2.ds.DsCount;
import version2.hereditary.Hereditary;

import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class SubSpace2DataSet {
	public static final DataSet subSpace2DataSet(Map<DataSet,double[]> subSpaceMap, DsCount dsCount) throws IOException, ClassNotFoundException {
		Map<String, Id> map = new HashMap<String, Id>();
		for (Map.Entry<DataSet, double[]> entry : subSpaceMap.entrySet()) {
			double[] chro=entry.getValue();
			for (Id id : entry.getKey().getIds()) {
				Id newId=map.get(id.getId());
				if (newId==null) {
					newId=new Id(id.getId(),new LinkedList<double[]>(),id.getLabel());
					map.put(id.getId(),newId);
				}
				newId.getEvidences().add(dsCount.countOrderDs(id,chro));
			}
		}
		
		LinkedList<Id> ids=new LinkedList<Id>();
		for (Map.Entry<String, Id> entry : map.entrySet()) {
			ids.add(entry.getValue());
		}
		return new DataSet(ids,null,null);
	}
}
