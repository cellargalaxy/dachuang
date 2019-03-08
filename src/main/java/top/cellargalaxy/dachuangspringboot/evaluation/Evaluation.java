package top.cellargalaxy.dachuangspringboot.evaluation;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Future;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface Evaluation {
	Future<Double> countEvaluation(DataSet dataSet) throws IOException;

	Future<Double> countEvaluation(DataSet dataSet, Integer withoutEvidenceId) throws IOException;

	Future<Double> countEvaluation(DataSet dataSet, Collection<Integer> withEvidenceIds) throws IOException;

	Future<Double> countEvaluation(DataSet dataSet, Chromosome chromosome) throws IOException;

	Future<Double> countEvaluation(DataSet dataSet, Integer withoutEvidenceId, Chromosome chromosome) throws IOException;

	Future<Double> countEvaluation(DataSet dataSet, Collection<Integer> withEvidenceIds, Chromosome chromosome) throws IOException;
}
