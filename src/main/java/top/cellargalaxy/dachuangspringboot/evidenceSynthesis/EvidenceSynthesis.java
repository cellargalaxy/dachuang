package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;


import top.cellargalaxy.dachuangspringboot.dataSet.Evidence;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface EvidenceSynthesis {
	Evidence synthesisEvidence(Id id);

	Evidence synthesisEvidence(Id id, Integer withoutEvidenceId);

	Evidence synthesisEvidence(Id id, Chromosome chromosome);

	Evidence synthesisEvidence(Id id, Integer withoutEvidenceId, Chromosome chromosome);
}
