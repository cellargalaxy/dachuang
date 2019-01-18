package top.cellargalaxy.dachuangspringboot.evaluation;

import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.hereditary.Chromosome;
import top.cellargalaxy.dachuangspringboot.hereditary.HereditaryUtils;
import top.cellargalaxy.dachuangspringboot.libsvm.svm_parameter;
import top.cellargalaxy.dachuangspringboot.libsvm.svm_problem;
import top.cellargalaxy.dachuangspringboot.mySvm.MySvmPredict;
import top.cellargalaxy.dachuangspringboot.mySvm.MySvmTrain;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

/**
 * Created by cellargalaxy on 17-10-24.
 */
public class Svm implements Evaluation {
	public static final String NAME = "svm";

	@Override
	public double countEvaluation(DataSet dataSet) throws IOException {
		svm_parameter parameter = MySvmTrain.createSvmDataSetParameter();
		svm_problem problem = MySvmTrain.exchangeDataSet(dataSet, parameter);
		problem = MySvmTrain.checkDataSet(problem, parameter);
		Writer writer = MySvmTrain.trainDataSet(problem, parameter);
		return MySvmPredict.predict(problem, writer);
	}

	@Override
	public double countEvaluation(DataSet dataSet, Integer withoutEvidenceId) throws IOException {
		return countEvaluation(dataSet.clone(withoutEvidenceId));
	}

	@Override
	public double countEvaluation(DataSet dataSet, Collection<Integer> withEvidenceIds) throws IOException {
		return countEvaluation(dataSet.clone(withEvidenceIds));
	}

	@Override
	public double countEvaluation(DataSet dataSet, Chromosome chromosome) throws IOException {
		return countEvaluation(HereditaryUtils.evolution(dataSet.clone(), chromosome));
	}

	@Override
	public double countEvaluation(DataSet dataSet, Integer withoutEvidenceId, Chromosome chromosome) throws IOException {
		return countEvaluation(HereditaryUtils.evolution(dataSet.clone(withoutEvidenceId), chromosome));
	}

	@Override
	public double countEvaluation(DataSet dataSet, Collection<Integer> withEvidenceIds, Chromosome chromosome) throws IOException {
		return countEvaluation(HereditaryUtils.evolution(dataSet.clone(withEvidenceIds), chromosome));
	}

	@Override
	public String toString() {
		return "SVM";
	}
}
