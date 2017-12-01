package top.cellargalaxy.version5.feature;

/**
 * Created by cellargalaxy on 17-11-1.
 */
public class AucImprotence implements Comparable<AucImprotence>{
	private final double aucD;
	private final int evidenceNum;
	
	public AucImprotence(double aucD, int evidenceNum) {
		this.aucD = aucD;
		this.evidenceNum = evidenceNum;
	}
	
	public double getAucD() {
		return aucD;
	}
	
	public int getEvidenceNum() {
		return evidenceNum;
	}
	
	@Override
	public String toString() {
		return "AucImprotence{" +
				"aucD=" + aucD +
				", evidenceNum=" + evidenceNum +
				'}';
	}
	
	public int compareTo(AucImprotence o) {
		if (aucD >o.aucD) {
			return 1;
		}else if(aucD <o.aucD){
			return -1;
		}else {
			return 0;
		}
	}
}
