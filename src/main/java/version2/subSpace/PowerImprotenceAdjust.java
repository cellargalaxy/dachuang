package version2.subSpace;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public class PowerImprotenceAdjust implements ImprotenceAdjust{
	public LinkedList<Double> adjustImprotence(double[] impros) {
		LinkedList<Double> listImpros = new LinkedList<Double>();
		for (double impro : impros) {
			listImpros.add(impro * impro);
		}
		return listImpros;
	}
}
