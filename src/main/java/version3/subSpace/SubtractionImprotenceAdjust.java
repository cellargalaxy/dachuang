package version3.subSpace;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-9.
 */
public final class SubtractionImprotenceAdjust implements ImprotenceAdjust {
	private final double adjustD;
	
	public SubtractionImprotenceAdjust(double adjustD) {
		this.adjustD = adjustD;
	}
	
	public final LinkedList<Double> adjustImprotence(double[] impros) {
		LinkedList<Double> listImpros = new LinkedList<Double>();
		double minImp = Double.MIN_VALUE;
		for (double impro : impros) {
			if (minImp < impro) {
				minImp = impro;
			}
		}
		minImp = Math.abs(minImp);
		for (double impro : impros) {
			listImpros.add(Math.abs(impro) - (minImp - adjustD));
		}
		return listImpros;
	}
}
