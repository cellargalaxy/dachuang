package top.cellargalaxy.dachuangspringboot.evidenceSynthesis;


import top.cellargalaxy.dachuangspringboot.dataSet.Id;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface EvidenceSynthesis {
	double[] synthesisEvidence(Id id);

	double[] synthesisEvidence(Id id, Integer withoutEvidNum);

	double[] synthesisEvidenceIndex(Id id, double[] chro);

	double[] synthesisEvidenceOrder(Id id, double[] chro);

	double[] synthesisEvidenceIndex(Id id, Integer withoutEvidNum, double[] chro);
}
