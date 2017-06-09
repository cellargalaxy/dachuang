package feature;

import auc.Id;
import dataSet.DataSet;
import util.CloneObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static feature.FeatureSelection.MEDIAN_MODEL;
import static feature.SubSpace.POWER_ADJUST;
import static feature.SubSpace.createSubSpace;

/**
 * Created by cellargalaxy on 2017/6/9.
 */
public class TestSetTest {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DataSet dataSet = new DataSet(new File("F:/xi/dachuang/特征选择 - 副本.csv"), ",", 0, 2, 3, 5, 6);
		FeatureSelection featureSelection = new FeatureSelection(MEDIAN_MODEL);
		double[][] improFeature = featureSelection.featureSelection(dataSet);
		int[] sn={1,2,3};
		int[][] subSpaces=createSubSpace(improFeature,POWER_ADJUST,0.5,sn);
		
		DataSet dataSet1= CloneObject.clone(dataSet);
		dataSet1.removeMissingId();
		double[][][] doubles=testSetTest(dataSet1,subSpaces);
		double[][] ds=averSynthesis(doubles);
		
		System.out.println("推理合成结果：");
		for (double[] d : ds) {
			System.out.println(Arrays.toString(d));
		}
	}
	
	private static double[][][] testSetTest(DataSet dataSet,int[][] subSpaces){
		double[][][] doubles=new double[dataSet.getIds().size()][subSpaces.length][];
		int i=0;
		for (Id id : dataSet.getIds()) {
			for (int j = 0; j < subSpaces.length; j++) {
				doubles[i][j]=id.countDSWiths(subSpaces[j]);
			}
			i++;
		}
		return doubles;
	}
	private static double[][] averSynthesis(double[][][] doubles){
		double[][] ds=new double[doubles.length][2];
		for (int i = 0; i < doubles.length; i++) {
			double d1Count=0;
			double d2Count=0;
			for (int j = 0; j < doubles[i].length; j++) {
				d1Count+=doubles[i][j][1];
				d2Count+=doubles[i][j][2];
			}
			ds[i][0]=d1Count/doubles[i].length;
			ds[i][1]=d2Count/doubles[i].length;
		}
		return ds;
	}
	private static double[][] voteSynthesis(double[][][] doubles){
		double[][] ds=new double[doubles.length][2];
		for (int i = 0; i < doubles.length; i++) {
			double d1Count=0;
			double d2Count=0;
			for (int j = 0; j < doubles[i].length; j++) {
				d1Count+=doubles[i][j][1];
				d2Count+=doubles[i][j][2];
			}
			ds[i][0]=d1Count/doubles[i].length;
			ds[i][1]=d2Count/doubles[i].length;
		}
		return ds;
	}
}
