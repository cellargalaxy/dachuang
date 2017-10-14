package version4.dataSet;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class DataSetParameter {
	private String coding;
	private int idClo;
	private int labelCol;
	private int evidCol;
	private int ACol;
	private int BCol;
	
	public DataSetParameter() {
		coding = "utf-8";
		idClo = 0;
		labelCol = 1;
		evidCol = 2;
		ACol = 3;
		BCol = 4;
	}
	
	public DataSetParameter(String coding, int idClo, int labelCol, int evidCol, int ACol, int BCol) {
		this.coding = coding;
		this.idClo = idClo;
		this.labelCol = labelCol;
		this.evidCol = evidCol;
		this.ACol = ACol;
		this.BCol = BCol;
	}
	
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
	
	@Override
	public String toString() {
		return "DataSetParameter{" +
				"coding='" + coding + '\'' +
				", idClo=" + idClo +
				", labelCol=" + labelCol +
				", evidCol=" + evidCol +
				", ACol=" + ACol +
				", BCol=" + BCol +
				'}';
	}
}
