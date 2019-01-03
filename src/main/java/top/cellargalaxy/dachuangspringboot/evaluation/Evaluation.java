package top.cellargalaxy.dachuangspringboot.evaluation;


import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;

import java.io.IOException;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface Evaluation {
	double countEvaluation(DataSet cloneDataSet) throws IOException;

	double countEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum) throws IOException;

	double countEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException;

	double countIndexEvaluation(DataSet dataSet, double[] chro) throws IOException;//下标

	double countOrderEvaluation(DataSet cloneDataSet, double[] chro) throws IOException;//顺序

	double countIndexEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum, double[] chro) throws IOException;//下标

	double countOrderEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException;//顺序
}
