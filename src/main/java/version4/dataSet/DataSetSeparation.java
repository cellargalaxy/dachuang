package version4.dataSet;

/**
 * Created by cellargalaxy on 17-9-28.
 */
public interface DataSetSeparation {
	DataSet[] separationDataSet(double test, double miss, double label1);
	
	int getCom0Count();
	
	
	int getCom1Count();
	
	
	int getMiss0Count();
	
	
	int getMiss1Count();
	
}
