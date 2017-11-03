package version5.evaluation;

import libsvm.svm_parameter;
import libsvm.svm_problem;
import mySvm.MySvmPredict;
import mySvm.MySvmTrain;
import util.CloneObject;
import version5.dataSet.DataSet;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by cellargalaxy on 17-10-24.
 */
public class Svm implements Evaluation {
	
	public double countEvaluation(DataSet cloneDataSet) throws IOException {
		svm_parameter parameter= MySvmTrain.createSvmDataSetParameter();
		svm_problem problem=MySvmTrain.exchengeDataSet(cloneDataSet,parameter);
		problem=MySvmTrain.checkDataSet(problem,parameter);
		Writer writer=MySvmTrain.trainDataSet(problem,parameter);
		return MySvmPredict.predict(problem,writer);
	}
	
	public double countEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum) throws IOException {
		svm_parameter parameter= MySvmTrain.createSvmDataSetParameter();
		svm_problem problem=MySvmTrain.exchengeDataSetWithoutEvidNum(dataSet,parameter,withoutEvidNum);
		problem=MySvmTrain.checkDataSet(problem,parameter);
		Writer writer=MySvmTrain.trainDataSet(problem,parameter);
		return MySvmPredict.predict(problem,writer);
	}
	
	public double countEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums) throws IOException, ClassNotFoundException {
		DataSet newDataSet = CloneObject.clone(dataSet);
		newDataSet.removeNotEqual(withEvidNums);
		return countEvaluation(newDataSet);
	}
	
	public double countIndexEvaluation(DataSet dataSet, double[] chro) throws IOException {
		svm_parameter parameter= MySvmTrain.createSvmDataSetParameter();
		svm_problem problem=MySvmTrain.exchengeDataSetIndexChro(dataSet,parameter,chro);
		problem=MySvmTrain.checkDataSet(problem,parameter);
		Writer writer=MySvmTrain.trainDataSet(problem,parameter);
		return MySvmPredict.predict(problem,writer);
	}
	
	public double countOrderEvaluation(DataSet cloneDataSet, double[] chro) throws IOException {
		svm_parameter parameter= MySvmTrain.createSvmDataSetParameter();
		svm_problem problem=MySvmTrain.exchengeDataSetOrderChro(cloneDataSet,parameter,chro);
		problem=MySvmTrain.checkDataSet(problem,parameter);
		Writer writer=MySvmTrain.trainDataSet(problem,parameter);
		return MySvmPredict.predict(problem,writer);
	}
	
	public double countIndexEvaluationWithoutEvidNum(DataSet dataSet, Integer withoutEvidNum, double[] chro) throws IOException {
		svm_parameter parameter= MySvmTrain.createSvmDataSetParameter();
		svm_problem problem=MySvmTrain.exchengeDataSetIndexChro(dataSet,parameter,withoutEvidNum,chro);
		problem=MySvmTrain.checkDataSet(problem,parameter);
		Writer writer=MySvmTrain.trainDataSet(problem,parameter);
		return MySvmPredict.predict(problem,writer);
	}
	
	public double countOrderEvaluationWithEvidNums(DataSet dataSet, List<Integer> withEvidNums, double[] chro) throws IOException, ClassNotFoundException {
		DataSet newDataSet = CloneObject.clone(dataSet);
		newDataSet.removeNotEqual(withEvidNums);
		return countOrderEvaluation(newDataSet, chro);
	}
	
	@Override
	public String toString() {
		return "Svm{}";
	}
}
