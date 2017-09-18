package version2.ds;

import version2.dataSet.Id;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public class AverageDsCount implements DsCount{
	public String getName() {
		return "平均证据合成";
	}
	
	public double[] countDs(Id id) {
		double[] ds={0,0,0};
		for (double[] doubles : id.getEvidences()) {
			ds[1]+=doubles[1];
			ds[2]+=doubles[2];
		}
		ds[1]/=id.getEvidences().size();
		ds[2]/=id.getEvidences().size();
		return ds;
	}
	
	public double[] countDs(Id id, Integer withoutEvidNum) {
		double[] ds={0,0,0};
		for (double[] doubles : id.getEvidences()) {
			if (withoutEvidNum.equals((int)(doubles[0]))) {
				continue;
			}
			ds[1]+=doubles[1];
			ds[2]+=doubles[2];
		}
		ds[1]/=id.getEvidences().size();
		ds[2]/=id.getEvidences().size();
		return ds;
	}
	
	public double[] countIndexDs(Id id, double[] chro) {
		double[] ds={0,0,0};
		for (double[] doubles : id.getEvidences()) {
			ds[1]+=doubles[1]*chro[2*(int)(doubles[0])-2];
			ds[2]+=doubles[2]*chro[2*(int)(doubles[0])-1];
		}
		ds[1]/=id.getEvidences().size();
		ds[2]/=id.getEvidences().size();
		return ds;
	}
	
	public double[] countOrderDs(Id id, double[] chro) {
		double[] ds={0,0,0};
		int i=0;
		for (double[] doubles : id.getEvidences()) {
			ds[1]+=doubles[1]*chro[2*i];
			ds[2]+=doubles[2]*chro[2*i+1];
			i++;
		}
		ds[1]/=id.getEvidences().size();
		ds[2]/=id.getEvidences().size();
		return ds;
	}
	
	public double[] countIndexDs(Id id, Integer withoutEvidNum, double[] chro) {
		double[] ds={0,0,0};
		for (double[] doubles : id.getEvidences()) {
			if (withoutEvidNum.equals((int)(doubles[0]))) {
				continue;
			}
			ds[1]+=doubles[1]*chro[2*(int)(doubles[0])-2];
			ds[2]+=doubles[2]*chro[2*(int)(doubles[0])-1];
		}
		ds[1]/=id.getEvidences().size();
		ds[2]/=id.getEvidences().size();
		return ds;
	}

	

}
