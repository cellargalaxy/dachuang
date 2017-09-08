package version2.auc;


import version2.dataSet.Id;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface DsCount {
	double[] countDs(Id id,double[] chro);
	double[] countDs(Id id,double[] chro,Integer withoutEvidNum);
}
