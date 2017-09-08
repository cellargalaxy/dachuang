package version2.auc;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public interface AucCount {
	double countAuc(DsCount dsCount);
	double countAuc(DsCount dsCount,double[] chro);
	double countAuc(DsCount dsCount,Integer withoutEvidNum);
	double countAuc(DsCount dsCount,double[] chro,Integer withoutEvidNum);
}
