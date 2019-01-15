package top.cellargalaxy.dachuangspringboot.dataSet;


import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cellargalaxy on 17-9-7.
 */
@Data
public class Id implements Serializable {
	public static final int LABEL_1 = 1;
	public static final int LABEL_0 = 0;
	private final String id;
	private final Map<Integer, Evidence> evidenceMap;
	private final int label;

	public Evidence putEvidence(Evidence evidence) {
		return evidenceMap.put(evidence.getEvidenceId(), evidence);
	}

	public Evidence getEvidence(int evidenceId) {
		return evidenceMap.get(evidenceId);
	}

	public Collection<Evidence> getEvidences() {
		return evidenceMap.values();
	}

	public Id clone(Integer withoutEvidenceId) {
		Map<Integer, Evidence> evidenceMap = new HashMap<>();
		for (Map.Entry<Integer, Evidence> entry : this.evidenceMap.entrySet()) {
			if (withoutEvidenceId.equals(entry.getKey())) {
				continue;
			}
			evidenceMap.put(entry.getKey(), entry.getValue().clone());
		}
		return new Id(id, evidenceMap, label);
	}

	public Id clone(Collection<Integer> withEvidenceIds) {
		Map<Integer, Evidence> evidenceMap = new HashMap<>();
		for (Map.Entry<Integer, Evidence> entry : this.evidenceMap.entrySet()) {
			if (!withEvidenceIds.contains(entry.getKey())) {
				continue;
			}
			evidenceMap.put(entry.getKey(), entry.getValue().clone());
		}
		return new Id(id, evidenceMap, label);
	}

	@Override
	public Id clone() {
		Map<Integer, Evidence> evidenceMap = new HashMap<>();
		for (Map.Entry<Integer, Evidence> entry : this.evidenceMap.entrySet()) {
			evidenceMap.put(entry.getKey(), entry.getValue().clone());
		}
		return new Id(id, evidenceMap, label);
	}
}
