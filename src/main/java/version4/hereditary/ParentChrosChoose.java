package version4.hereditary;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public interface ParentChrosChoose {
	String getName();
	double[][] chooseParentChros(double[] aucs, double[][] newChros);
}
