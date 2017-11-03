package version5.dataSet;

/**
 * Created by cellargalaxy on 17-9-28.
 */
public interface DataSetSeparation {
	DataSet[] separationDataSet(double test, double trainMiss, double testMiss, double label1);
	
	int getCom0Count();
	
	int getCom1Count();
	
	int getMiss0Count();
	
	int getMiss1Count();
	
	double getTest();
	
	double getTrainMiss();
	
	double getTestMiss();
	
	double getLabel1();
	
}
