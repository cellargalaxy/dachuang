package svmTest;

import libsvm.*;
import version4.dataSet.DataSet;
import version4.dataSet.Id;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class MySvmTrain {
	
	/**
	 * 运行
	 *
	 * @throws IOException
	 */
	public static svm_problem run(DataSet dataSet, Writer writer) throws IOException {
		svm_parameter param = parse_command_line();
		//读取数据集
		svm_problem prob = read_problem(dataSet, param);
		//检查数据集与参数之间
		String error_msg = svm.svm_check_parameter(prob, param);
		
		//如果有问题
		if (error_msg != null) {
			throw new RuntimeException("ERROR: " + error_msg);
		}
		
		//训练训练集
		svm_model model = svm.svm_train(prob, param);
		//保存训练模型
		svm.svm_save_model(model, writer);
		return prob;
	}
	
	
	/**
	 * 参数初始化
	 */
	private static svm_parameter parse_command_line() {
		svm.svm_set_print_string_function(null);
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0;    // 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		return param;
	}
	
	
	/**
	 * 读取数据集
	 *
	 * @throws IOException
	 */
	private static svm_problem read_problem(DataSet dataSet, svm_parameter param) {
		//最大的特征编号
		int max_index = 0;
		
		svm_problem prob = new svm_problem();
		//数据集对象的个数
		prob.l = dataSet.getIds().size();
		//数据集特征的二维表
		prob.x = new svm_node[prob.l][];
		//数据集的标签
		prob.y = new double[prob.l];
		int i = 0;
		for (Id id : dataSet.getIds()) {
			svm_node[] x = new svm_node[id.getEvidences().size()];
			int j = 0;
			for (double[] doubles : id.getEvidences()) {
				x[j] = new svm_node();
				x[j].index = (int) (doubles[0]);
				max_index = Math.max(max_index, x[j].index);
				x[j].value = doubles[1];
				j++;
			}
			prob.x[i] = x;
			
			prob.y[i] = id.getLabel();
			i++;
		}
		//根据最大特征编号做一些参数设置
		if (param.gamma == 0 && max_index > 0)
			param.gamma = 1.0 / max_index;
		
		if (param.kernel_type == svm_parameter.PRECOMPUTED) {
			for (i = 0; i < prob.l; i++) {
				if (prob.x[i][0].index != 0) {
					throw new RuntimeException("Wrong kernel matrix: first column must be 0:sample_serial_number");
				}
				if ((int) prob.x[i][0].value <= 0 || (int) prob.x[i][0].value > max_index) {
					throw new RuntimeException("Wrong input format: sample_serial_number out of range");
				}
			}
		}
		return prob;
	}
}
