package top.cellargalaxy.dachuangspringboot.subSpaceSynthesis;

import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesisFactory;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryResult;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cellargalaxy
 * @time 2019/1/15
 */
public class SubSpaceSynthesis {

	public static final SubSpaceSynthesisResult synthesisSubSpace(RunParameter runParameter, Map<DataSet, HereditaryResult> subSpaceMap, Evaluation evaluation) {
		SubSpaceSynthesisResult fitSubSpaceSynthesisResult = null;
		for (String name : EvidenceSynthesisFactory.NAMES) {
			EvidenceSynthesis evidenceSynthesis = EvidenceSynthesisFactory.createEvidenceSynthesis(name, runParameter, null);
			try {
				DataSet dataSet = synthesisSubSpace(subSpaceMap, evidenceSynthesis);
				double evaluationValue = evaluation.countEvaluation(dataSet);
				if (fitSubSpaceSynthesisResult == null || evaluationValue > fitSubSpaceSynthesisResult.getEvaluationValue()) {
					fitSubSpaceSynthesisResult = new SubSpaceSynthesisResult(dataSet, evidenceSynthesis, evaluationValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fitSubSpaceSynthesisResult;
	}

	public static final DataSet synthesisSubSpace(Map<DataSet, HereditaryResult> subSpaceMap, EvidenceSynthesis evidenceSynthesis) {
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
			i++;
		}
		return new DataSet(idMap);
	}
}
