package version4.subSpace;

import java.util.LinkedList;

/**
 * Created by cellargalaxy on 17-9-8.
 */
public final class PowerImprotenceAdjust implements ImprotenceAdjust {
	
	public String getName() {
		return "平方调整算法";
	}
	
	public final LinkedList<Double> adjustImprotence(double[] impros) {
		LinkedList<Double> listImpros = new LinkedList<Double>();
		for (double impro : impros) {
			listImpros.add(impro * impro);
		}
		return listImpros;
	}
	
	@Override
	public String toString() {
		return "PowerImprotenceAdjust{平方调整算法}";
	}
}
