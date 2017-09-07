package version2.dataSet;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class DataSetParameter {
	private String coding="utf-8";
	private int idClo=0;
	private int labelCol=1;
	private int evidCol=2;
	private int ACol=3;
	private int BCol=4;
	
	public String getCoding() {
		return coding;
	}
	
	public void setCoding(String coding) {
		this.coding = coding;
	}
	
	public int getIdClo() {
		return idClo;
	}
	
	public void setIdClo(int idClo) {
		this.idClo = idClo;
	}
	
	public int getLabelCol() {
		return labelCol;
	}
	
	public void setLabelCol(int labelCol) {
		this.labelCol = labelCol;
	}
	
	public int getEvidCol() {
		return evidCol;
	}
	
	public void setEvidCol(int evidCol) {
		this.evidCol = evidCol;
	}
	
	public int getACol() {
		return ACol;
	}
	
	public void setACol(int ACol) {
		this.ACol = ACol;
	}
	
	public int getBCol() {
		return BCol;
	}
	
	public void setBCol(int BCol) {
		this.BCol = BCol;
	}
}
