package version3.dataSet;


import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-7.
 */
public class Id implements Serializable {
	public static final int LABEL_1 = 1;
	public static final int LABEL_0 = 0;
	private final String id;
	private final LinkedList<double[]> evidences;
	private final int label;
	
	public Id(String id, LinkedList<double[]> evidences, int label) {
		this.id = id;
		this.evidences = evidences;
		this.label = label;
	}
	
	public String getId() {
		return id;
	}
	
	public LinkedList<double[]> getEvidences() {
		return evidences;
	}
	
	public int getLabel() {
		return label;
	}
}
