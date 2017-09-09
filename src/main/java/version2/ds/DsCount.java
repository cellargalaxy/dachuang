package version2.ds;


import version2.dataSet.Id;


/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface DsCount {
	double[] countDs(Id id);
	
	double[] countDs(Id id, Integer withoutEvidNum);
	
	double[] countIndexDs(Id id, double[] chro);
	
	double[] countOrderDs(Id id, double[] chro);
	
	double[] countIndexDs(Id id, Integer withoutEvidNum, double[] chro);
}
