package top.cellargalaxy.dachuangspringboot.evaluation;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;

import java.io.IOException;
import java.util.Set;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface Evaluation {
	double countEvaluation(DataSet dataSet) throws IOException;

	double countEvaluation(DataSet dataSet, Integer withoutEvidenceId) throws IOException;

	double countEvaluation(DataSet dataSet, Set<Integer> withEvidenceIds) throws IOException;

	double countEvaluation(DataSet dataSet, Chromosome chromosome) throws IOException;

	double countEvaluation(DataSet dataSet, Integer withoutEvidenceId, Chromosome chromosome) throws IOException;

	double countEvaluation(DataSet dataSet, Set<Integer> withEvidenceIds, Chromosome chromosome) throws IOException;
}
