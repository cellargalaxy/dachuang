package feature;

import dataSet.DataSet;
import dataSet.Id;
import util.CloneObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static feature.FeatureSelection.MEDIAN_MODEL;
import static feature.SubSpace.POWER_ADJUST;
import static feature.SubSpace.createSubSpace;

/**
 * Created by cellargalaxy on 2017/6/9.
 * 对测试数据集进行测试
 */
public class TestSetTest {
	public final static int AVER_SYNTHESIS=1;
	public final static int VOTE_SYNTHESIS=2;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DataSet dataSet = new DataSet(new File("/media/cellargalaxy/根/内/办公/xi/dachuang/dataSet/特征选择 - 副本.csv"), ",", 0, 2, 3, 5, 6);
		FeatureSelection featureSelection = new FeatureSelection(MEDIAN_MODEL);
		double[][] improFeature = featureSelection.featureSelection(dataSet);
		int[] sn={1,2,3};
		int[][] subSpaces=createSubSpace(improFeature,POWER_ADJUST,0.5,sn);

		DataSet dataSet1= CloneObject.clone(dataSet);
		dataSet1.removeMissingId();
		dataSet1=subSpaceDSs(dataSet1,subSpaces);
		dataSet1=averSynthesis(dataSet1);

		System.out.println("推理合成结果：");
		for (Id id : dataSet1.getIds()) {
			System.out.println(Arrays.toString(id.getSubSpaceDS()));
		}
	}

	/**
	 * 检验测试数据集
	 * @param dataSet 测试数据集
	 * @param subSpaces 子空间
	 * @param synthesisNum 子空间DS合成方法
	 * @param thrf 投票法合成A最低阈值，若选平均法合成，此参数无效
	 * @param thrnf 投票法合成B最高阈值，若选平均法合成，此参数无效
	 * @param d1 投票法合成A最高值，若选平均法合成，此参数无效
	 * @param d2 投票法合成B最高值，若选平均法合成，此参数无效
	 * @return
	 */
	public static DataSet testSetTest(DataSet dataSet, int[][] subSpaces,int synthesisNum,double thrf,double thrnf,double d1,double d2){
		if (synthesisNum==AVER_SYNTHESIS) {
			return averSynthesis(subSpaceDSs(dataSet,subSpaces));
		}else if (synthesisNum==VOTE_SYNTHESIS){
			return voteSynthesis(subSpaceDSs(dataSet,subSpaces),thrf,thrnf,d1,d2);
		}else {
			throw new RuntimeException("子空间DS合成方法编号错误："+synthesisNum);
		}
	}

	/**
	 * 计算各个对象，各个子空间的DS合成证据
	 * y:嫌疑人 x:子空间 (x1,y1):某个嫌疑人算某个子空间的DS的合成证据
	 * 数据结构：(x1,y1)=new double[3]={无意义,A,B}
	 * @param dataSet
	 * @param subSpaces
	 * @return
	 */
	private static DataSet subSpaceDSs(DataSet dataSet, int[][] subSpaces){
		for (Id id : dataSet.getIds()) {
			double[][] subSpaceDSs=new double[subSpaces.length][];
			for (int i = 0; i < subSpaces.length; i++) {
				subSpaceDSs[i]=id.countDSWiths(subSpaces[i]);
			}
			id.setSubSpaceDSs(subSpaceDSs);
		}
		return dataSet;
	}

	/**
	 * 平均法合成对个子空间的DS证据成一个证据
	 * @param dataSet
	 * @return
	 */
	private static DataSet averSynthesis(DataSet dataSet){
		for (Id id : dataSet.getIds()) {
			double[][] subSpaceDSs=id.getSubSpaceDSs();
			double d1Count=0;
			double d2Count=0;
			for (int i = 0; i < subSpaceDSs.length; i++) {
				d1Count+=subSpaceDSs[i][1];
				d2Count+=subSpaceDSs[i][2];
			}
			double[] subSpaceDS={0,d1Count/subSpaceDSs.length,d2Count/subSpaceDSs.length};
			id.setSubSpaceDS(subSpaceDS);
		}
		return dataSet;
	}

	/**
	 * 投票法合成对个子空间的DS证据成一个证据
	 * @param dataSet
	 * @param thrf
	 * @param thrnf
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static DataSet voteSynthesis(DataSet dataSet,double thrf,double thrnf,double d1,double d2){
		for (Id id : dataSet.getIds()) {
			int f=0;
			int nf=0;
			double[][] subSpaceDSs=id.getSubSpaceDSs();
			for (int i = 0; i < subSpaceDSs.length; i++) {
				if (subSpaceDSs[i][1]>thrf) {
					f++;
				}else if (subSpaceDSs[i][2]<thrnf){
					nf++;
				}
			}
			double[] subSpaceDS=new double[3];
			if (f>nf) {
				subSpaceDS[1]=d1*(f-nf)/(f+nf);
				subSpaceDS[2]=0;
			}else if(f<nf){
				subSpaceDS[1]=0;
				subSpaceDS[2]=d2*(nf-f)/(f+nf);
			}else {
				subSpaceDS[1]=0;
				subSpaceDS[2]=0;
			}
			id.setSubSpaceDS(subSpaceDS);
		}
		return dataSet;
	}
}
