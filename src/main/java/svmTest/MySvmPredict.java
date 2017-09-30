package svmTest;

import libsvm.*;
import version3.dataSet.DataSet;
import version3.dataSet.Id;

import java.io.*;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class MySvmPredict {
	private static svm_print_interface svm_print_null = new svm_print_interface() {
		public void print(String s) {}
	};
	
	private static svm_print_interface svm_print_stdout = new svm_print_interface()
	{
		public void print(String s)
		{
			System.out.print(s);
		}
	};
	
	private static svm_print_interface svm_print_string = svm_print_stdout;
	
	static void info(String s)
	{
		svm_print_string.print(s);
	}
	
	private static double atof(String s)
	{
		return Double.valueOf(s).doubleValue();
	}
	
	private static int atoi(String s)
	{
		return Integer.parseInt(s);
	}
	
	private static void predict(DataSet dataSet, DataOutputStream output, svm_model model, int predict_probability) throws IOException {
		int correct = 0;
		int total = 0;
		double error = 0;
		double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
		
		int svm_type=svm.svm_get_svm_type(model);
		int nr_class=svm.svm_get_nr_class(model);
		double[] prob_estimates=null;
		
		if(predict_probability == 1) {
			if(svm_type == svm_parameter.EPSILON_SVR || svm_type == svm_parameter.NU_SVR) {
				svm_predict.info("Prob. model for svmTest data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="+svm.svm_get_svr_probability(model)+"\n");
			}else {
				int[] labels=new int[nr_class];
				svm.svm_get_labels(model,labels);
				prob_estimates = new double[nr_class];
				output.writeBytes("labels");
				for(int j=0;j<nr_class;j++)
					output.writeBytes(" "+labels[j]);
				output.writeBytes("\n");
			}
		}
		for (Id id : dataSet.getIds()) {
			double target = id.getLabel();
			svm_node[] x = new svm_node[id.getEvidences().size()];
			int i=0;
			for (double[] doubles : id.getEvidences()) {
				x[i]=new svm_node();
				x[i].index=(int)(doubles[0]);
				x[i].value=doubles[1];
				i++;
			}
			
			double v;
			if (predict_probability==1 && (svm_type==svm_parameter.C_SVC || svm_type==svm_parameter.NU_SVC)) {
				v = svm.svm_predict_probability(model,x,prob_estimates);
				output.writeBytes(v+" ");
				for(int j=0;j<nr_class;j++)
					output.writeBytes(prob_estimates[j]+" ");
				output.writeBytes("\n");
			}else {
				v = svm.svm_predict(model,x);
				output.writeBytes(v+"\n");
			}
			
			if(v == target){
				++correct;
			}
			error += (v-target)*(v-target);
			sumv += v;
			sumy += target;
			sumvv += v*v;
			sumyy += target*target;
			sumvy += v*target;
			++total;
		}
		
		if(svm_type == svm_parameter.EPSILON_SVR || svm_type == svm_parameter.NU_SVR) {
			svm_predict.info("Mean squared error = "+error/total+" (regression)\n");
			svm_predict.info("Squared correlation coefficient = "+
					((total*sumvy-sumv*sumy)*(total*sumvy-sumv*sumy))/
							((total*sumvv-sumv*sumv)*(total*sumyy-sumy*sumy))+
					" (regression)\n");
		}else {
			svm_predict.info("Accuracy = "+(double)correct/total*100+ "% ("+correct+"/"+total+") (classification)\n");
		}
	}
	
	/**
	 * 命令行输出帮助
	 */
	private static void exit_with_help() {
		System.err.print("usage: svm_predict [options] test_file model_file output_file\n"
				+"options:\n"
				+"-b probability_estimates: whether to predict probability estimates, 0 or 1 (default 0); one-class SVM not supported yet\n"
				+"-q : quiet mode (no outputs)\n");
		System.exit(1);
	}
	
	
	/**
	 * 参数，文件读取以及检查模型，检查完调用预测方法
	 * @throws IOException
	 */
	public static void run(DataSet testDataSet,File modelFile,File outputFile) throws IOException {
		int i, predict_probability=0;
		svm_print_string = svm_print_stdout;
		
//		// 读取参数输入
//		for(i=0;i<argv.length;i++) {
//			if(argv[i].charAt(0) != '-') {
//				break;
//			}
//			++i;
//			switch(argv[i-1].charAt(1)) {
//				case 'b':
//					predict_probability = atoi(argv[i]);
//					break;
//				case 'q':
//					svm_print_string = svm_print_null;
//					i--;
//					break;
//				default:
//					System.err.print("Unknown option: " + argv[i-1] + "\n");
//					exit_with_help();
//			}
//		}
//		if(i>=argv.length-2){
//			exit_with_help();
//		}
		
		//argv第一个是测试集，第二个是模型，第三个是结果输出路径
		try {
			svm_model model = svm.svm_load_model(modelFile.getAbsolutePath());
			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile.getAbsolutePath())));
			if (model == null) {//模型读取是否成功
				System.err.print("can't open model file "+modelFile.getAbsolutePath()+"\n");
				System.exit(1);
			}
			if(predict_probability == 1) {
				if(svm.svm_check_probability_model(model)==0) {//模型不支持概率估计
					System.err.print("Model does not support probabiliy estimates\n");
					System.exit(1);
				}
			} else {
				if(svm.svm_check_probability_model(model)!=0) {//模型支持概率估计，但预测失效
					svm_predict.info("Model supports probability estimates, but disabled in prediction.\n");
				}
			}
			predict(testDataSet,output,model,predict_probability);
			output.close();
		}catch(FileNotFoundException e)  {
			exit_with_help();
		}catch(ArrayIndexOutOfBoundsException e)  {
			exit_with_help();
		}
	}
}
