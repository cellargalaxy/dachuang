package top.cellargalaxy.dachuangspringboot.run;

import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cellargalaxy
 * @time 2019/1/15
 */
public class SubSpace2DataSet {
	public static final DataSet subSpace2DataSet(Map<DataSet, HereditaryResult> subSpaceMap, EvidenceSynthesis evidenceSynthesis) throws IOException, ClassNotFoundException {
		Map<String, Id> idMap = new HashMap<>();
		int i = 1;
		for (Map.Entry<DataSet, HereditaryResult> entry : subSpaceMap.entrySet()) {
			HereditaryResult hereditaryResult = entry.getValue();
			for (Id id : entry.getKey().getIds()) {
				Id newId = idMap.get(id.getId());
				if (newId == null) {
					newId = new Id(id.getId(), new HashMap<>(), id.getLabel());
					idMap.put(id.getId(), newId);
				}
				newId.getEvidenceMap().put(i, evidenceSynthesis.synthesisEvidence(id, hereditaryResult.getChromosome()));
			}
		}
		return new DataSet(idMap);
	}
}
