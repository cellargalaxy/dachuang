package svmTest;

import libsvm.*;

import java.io.*;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class MySvmPredict {
	
	/**
	 * 参数，文件读取以及检查模型，检查完调用预测方法
	 *
	 * @throws IOException
	 */
	public static double run(svm_problem problem, Writer writer) throws IOException {
		svm_model model = svm.svm_load_model(new BufferedReader(new StringReader(writer.toString())));
		if (model == null) {//模型读取是否成功
			throw new RuntimeException("can't open model");
		}
		if (svm.svm_check_probability_model(model) != 0) {//模型支持概率估计，但预测失效
			System.out.println("Model supports probability estimates, but disabled in prediction.");
		}
		return predict(problem, model);
	}
	
	private static double predict(svm_problem problem, svm_model model) {
		int correct = 0;
		int total = 0;
		double error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		
		int svm_type = svm.svm_get_svm_type(model);
		
		for (int i = 0; i < problem.x.length; i++) {
			svm_node[] x = problem.x[i];
			double target = problem.y[i];
			double v = svm.svm_predict(model, x);
			
			if (v == target) {
				++correct;
			}
			error += (v - target) * (v - target);
			sumv += v;
			sumy += target;
			sumvv += v * v;
			sumyy += target * target;
			sumvy += v * target;
			++total;
		}
		
		if (svm_type == svm_parameter.EPSILON_SVR || svm_type == svm_parameter.NU_SVR) {
			System.out.println("Mean squared error = " + error / total + " (regression)");
			System.out.println("Squared correlation coefficient = " +
					((total * sumvy - sumv * sumy) * (total * sumvy - sumv * sumy)) / ((total * sumvv - sumv * sumv) * (total * sumyy - sumy * sumy)) + " (regression)");
			throw new RuntimeException("Mean squared error = " + error / total + " (regression)");
		} else {
			System.out.println("Accuracy = " + (double) correct / total * 100 + "% (" + correct + "/" + total + ") (classification)");
			return (double) correct / total;
		}
	}

//	private static double predict(DataSet dataSet, svm_model model) {
//		int correct = 0;
//		int total = 0;
//		double error = 0;
//		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
//
//		int svm_type=svm.svm_get_svm_type(model);
//
//		for (Id id : dataSet.getIds()) {
//			double target = id.getLabel();
//			svm_node[] x = new svm_node[id.getEvidences().size()];
//			int i=0;
//			for (double[] doubles : id.getEvidences()) {
//				x[i]=new svm_node();
//				x[i].index=(int)(doubles[0]);
//				x[i].value=doubles[1];
//				i++;
//			}
//
//			double v=svm.svm_predict(model,x);
//
//			if(v == target){
//				++correct;
//			}
//			error += (v-target)*(v-target);
//			sumv += v;
//			sumy += target;
//			sumvv += v*v;
//			sumyy += target*target;
//			sumvy += v*target;
//			++total;
//		}
//
//		if(svm_type == svm_parameter.EPSILON_SVR || svm_type == svm_parameter.NU_SVR) {
//			System.out.println("Mean squared error = "+error/total+" (regression)");
//			System.out.println("Squared correlation coefficient = "+
//					((total*sumvy-sumv*sumy)*(total*sumvy-sumv*sumy))/ ((total*sumvv-sumv*sumv)*(total*sumyy-sumy*sumy))+ " (regression)");
//			throw new RuntimeException("Mean squared error = "+error/total+" (regression)");
//		}else {
//			System.out.println("Accuracy = "+(double)correct/total*100+ "% ("+correct+"/"+total+") (classification)");
//			return (double)correct/total;
//		}
//	}
//
//	/**
//	 * 参数，文件读取以及检查模型，检查完调用预测方法
//	 *
//	 * @throws IOException
//	 */
//	public static double run(DataSet testDataSet, Writer writer) throws IOException {
//		svm_model model = svm.svm_load_model(new BufferedReader(new StringReader(writer.toString())));
//		if (model == null) {//模型读取是否成功
//			throw new RuntimeException("can't open model");
//		}
//		if (svm.svm_check_probability_model(model) != 0) {//模型支持概率估计，但预测失效
//			System.out.println("Model supports probability estimates, but disabled in prediction.");
//		}
//		return predict(testDataSet, model);
//	}
}