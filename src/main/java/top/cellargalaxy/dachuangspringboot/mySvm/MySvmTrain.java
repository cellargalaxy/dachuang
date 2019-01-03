package top.cellargalaxy.dachuangspringboot.mySvm;


import top.cellargalaxy.dachuangspringboot.libsvm.*;
import top.cellargalaxy.dachuangspringboot.dataSet.DataSet;
import top.cellargalaxy.dachuangspringboot.dataSet.Id;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class MySvmTrain {
	/**
	 * 创建svm数据集的参数
	 *
	 * @return
	 */
	public static final svm_parameter createSvmDataSetParameter() {
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
	 * 数据集检查
	 *
	 * @param prob
	 * @param param
	 * @return
	 */
	public static final svm_problem checkDataSet(svm_problem prob, svm_parameter param) {
		//检查数据集与参数之间
		String error_msg = svm.svm_check_parameter(prob, param);
		//如果有问题
		if (error_msg != null) {
			throw new RuntimeException("ERROR: " + error_msg);
		}
		return prob;
	}
	
	/**
	 * 训练数据集
	 *
	 * @param prob
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static final Writer trainDataSet(svm_problem prob, svm_parameter param) throws IOException {
		//训练训练集
		svm_model model = svm.svm_train(prob, param);
		Writer writer = new StringWriter();
		//保存训练模型
		svm.svm_save_model(model, writer);
		return writer;
	}
	
	private static final svm_problem exchengeDataSetAfter(int max_index, svm_problem prob, svm_parameter param) {
		//根据最大特征编号做一些参数设置
		if (param.gamma == 0 && max_index > 0) {
			param.gamma = 1.0 / max_index;
		}
		
		if (param.kernel_type == svm_parameter.PRECOMPUTED) {
			for (int i = 0; i < prob.l; i++) {
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
	
	
	public static final svm_problem exchengeDataSet(DataSet dataSet, svm_parameter param) {
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
		return exchengeDataSetAfter(max_index, prob, param);
	}
	
	
	public static final svm_problem exchengeDataSetWithoutEvidNum(DataSet dataSet, svm_parameter param, Integer withoutEvidNum) {
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
			LinkedList<svm_node> nodes = new LinkedList<svm_node>();
			for (double[] doubles : id.getEvidences()) {
				if (withoutEvidNum.equals((int) (doubles[0]))) {
					continue;
				}
				svm_node node = new svm_node();
				node.index = (int) (doubles[0]);
				max_index = Math.max(max_index, node.index);
				node.value = doubles[1];
				nodes.add(node);
			}
			
			svm_node[] x = new svm_node[nodes.size()];
			int j = 0;
			for (svm_node node : nodes) {
				x[j] = node;
				j++;
			}
			prob.x[i] = x;
			
			prob.y[i] = id.getLabel();
			i++;
		}
		return exchengeDataSetAfter(max_index, prob, param);
	}
	
	public static final svm_problem exchengeDataSetIndexChro(DataSet dataSet, svm_parameter param, double[] chro) {
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
				x[j].value = doubles[1] * chro[2 * (int) (doubles[0]) - 2];
				j++;
			}
			prob.x[i] = x;
			
			prob.y[i] = id.getLabel();
			i++;
		}
		return exchengeDataSetAfter(max_index, prob, param);
	}
	
	public static final svm_problem exchengeDataSetOrderChro(DataSet dataSet, svm_parameter param, double[] chro) {
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
				x[j].value = doubles[1] * chro[2 * j];
				j++;
			}
			prob.x[i] = x;
			
			prob.y[i] = id.getLabel();
			i++;
		}
		return exchengeDataSetAfter(max_index, prob, param);
	}
	
	public static final svm_problem exchengeDataSetIndexChro(DataSet dataSet, svm_parameter param, Integer withoutEvidNum, double[] chro) {
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
			LinkedList<svm_node> nodes = new LinkedList<svm_node>();
			for (double[] doubles : id.getEvidences()) {
				if (withoutEvidNum.equals((int) (doubles[0]))) {
					continue;
				}
				svm_node node = new svm_node();
				node.index = (int) (doubles[0]);
				max_index = Math.max(max_index, node.index);
				node.value = doubles[1] * chro[2 * (int) (doubles[0]) - 2];
				nodes.add(node);
			}
			
			svm_node[] x = new svm_node[nodes.size()];
			int j = 0;
			for (svm_node node : nodes) {
				x[j] = node;
				j++;
			}
			prob.x[i] = x;
			
			prob.y[i] = id.getLabel();
			i++;
		}
		return exchengeDataSetAfter(max_index, prob, param);
	}
}
