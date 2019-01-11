package top.cellargalaxy.dachuangspringboot.dataSet;


import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cellargalaxy on 17-9-7.
 */
@Data
public class DataSet implements Serializable {
	private final Map<String, Id> idMap;
	private final Map<String, Integer> evidenceName2EvidenceId;

	public DataSet(Map<String, Id> idMap) {
		this.idMap = idMap;
		evidenceName2EvidenceId = new HashMap<>();
		for (Id id : idMap.values()) {
			for (Evidence evidence : id.getEvidences()) {
				evidenceName2EvidenceId.put(evidence.getEvidenceName(), evidence.getEvidenceId());
			}
		}
	}

	public DataSet(Map<String, Id> idMap, Map<String, Integer> evidenceName2EvidenceId) {
		this.idMap = idMap;
		this.evidenceName2EvidenceId = evidenceName2EvidenceId;
	}

	public Id putId(Id id) {
		return idMap.put(id.getId(), id);
	}

	public Collection<Id> getIds() {
		return idMap.values();
	}

	public DataSet clone(Integer withoutEvidenceId) {
		Map<String, Id> idMap = new HashMap<>();
		for (Map.Entry<String, Id> entry : this.idMap.entrySet()) {
			idMap.put(entry.getKey(), entry.getValue().clone(withoutEvidenceId));
		}
		Map<String, Integer> evidenceName2EvidenceId = new HashMap<>();
		for (Map.Entry<String, Integer> entry : this.evidenceName2EvidenceId.entrySet()) {
			if (withoutEvidenceId.equals(entry.getValue())) {
				continue;
			}
			evidenceName2EvidenceId.put(entry.getKey(), entry.getValue());
		}
		return new DataSet(idMap, evidenceName2EvidenceId);
	}

	public DataSet clone(List<Integer> withEvidenceIds) {
		Map<String, Id> idMap = new HashMap<>();
		for (Map.Entry<String, Id> entry : this.idMap.entrySet()) {
			idMap.put(entry.getKey(), entry.getValue().clone(withEvidenceIds));
		}
		Map<String, Integer> evidenceName2EvidenceId = new HashMap<>();
		for (Map.Entry<String, Integer> entry : this.evidenceName2EvidenceId.entrySet()) {
			if (!withEvidenceIds.contains(entry.getValue())) {
				continue;
			}
			evidenceName2EvidenceId.put(entry.getKey(), entry.getValue());
		}
		return new DataSet(idMap, evidenceName2EvidenceId);
	}

	@Override
	public DataSet clone() {
		Map<String, Id> idMap = new HashMap<>();
		for (Map.Entry<String, Id> entry : this.idMap.entrySet()) {
			idMap.put(entry.getKey(), entry.getValue().clone());
		}
		Map<String, Integer> evidenceName2EvidenceId = new HashMap<>();
		for (Map.Entry<String, Integer> entry : this.evidenceName2EvidenceId.entrySet()) {
			evidenceName2EvidenceId.put(entry.getKey(), entry.getValue());
		}
		return new DataSet(idMap, evidenceName2EvidenceId);
	}
}
