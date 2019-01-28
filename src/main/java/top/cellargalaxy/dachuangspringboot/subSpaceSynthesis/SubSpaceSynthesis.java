package top.cellargalaxy.dachuangspringboot.subSpaceSynthesis;

import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;
import top.cellargalaxy.dachuangspringboot.evaluation.Evaluation;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesis;
import top.cellargalaxy.dachuangspringboot.evidenceSynthesis.EvidenceSynthesisFactory;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;
import top.cellargalaxy.dachuangspringboot.run.RunParameter;
import top.cellargalaxy.dachuangspringboot.run.RunResult;
import top.cellargalaxy.dachuangspringboot.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cellargalaxy
 * @time 2019/1/15
 */
public class SubSpaceSynthesis {

	public static final SubSpaceSynthesisResult synthesisSubSpace(RunParameter runParameter, List<RunResult> runResults, Evaluation evaluation) throws IOException {
		SubSpaceSynthesisResult fitSubSpaceSynthesisResult = null;
		String subSpaceEvidenceSynthesisName = runParameter.getSubSpaceEvidenceSynthesisName();
		if (!StringUtils.isBlank(subSpaceEvidenceSynthesisName)) {
			EvidenceSynthesis evidenceSynthesis = EvidenceSynthesisFactory.createEvidenceSynthesis(subSpaceEvidenceSynthesisName, runParameter, null);
			DataSet dataSet = synthesisSubSpace(runResults, evidenceSynthesis);
			double evaluationValue = evaluation.countEvaluation(dataSet);
			fitSubSpaceSynthesisResult = new SubSpaceSynthesisResult(dataSet, evidenceSynthesis, evaluationValue);
			return fitSubSpaceSynthesisResult;
		}
		for (String name : EvidenceSynthesisFactory.NAMES) {
			try {
				EvidenceSynthesis evidenceSynthesis = EvidenceSynthesisFactory.createEvidenceSynthesis(name, runParameter, null);
				DataSet dataSet = synthesisSubSpace(runResults, evidenceSynthesis);
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

	public static final DataSet synthesisSubSpace(List<RunResult> runResults, EvidenceSynthesis evidenceSynthesis) {
		Map<String, Id> idMap = new HashMap<>();
		int i = 1;
		for (RunResult runResult : runResults) {
			Chromosome chromosome = runResult.getChromosome();
			DataSet dataSet = runResult.getDataSet();
			for (Id id : dataSet.getIds()) {
				Id newId = idMap.get(id.getId());
				if (newId == null) {
					newId = new Id(id.getId(), new HashMap<>(), id.getLabel());
					idMap.put(id.getId(), newId);
				}
				newId.getEvidenceMap().put(i, evidenceSynthesis.synthesisEvidence(id, chromosome));
			}
			i++;
		}
		return new DataSet(idMap);
	}
}
