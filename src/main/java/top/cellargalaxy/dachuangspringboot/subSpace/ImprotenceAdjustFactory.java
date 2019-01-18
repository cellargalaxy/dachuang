package top.cellargalaxy.dachuangspringboot.subSpace;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class ImprotenceAdjustFactory {
	private static ImprotenceAdjust improtenceAdjust;

	public static final ImprotenceAdjust getImprotenceAdjust() {
		return improtenceAdjust;
	}

	public static final ImprotenceAdjust createImprotenceAdjust(RunParameter runParameter) {
		String name = runParameter.getImprotenceAdjustName();
		if (PowerImprotenceAdjust.NAME.equals(name)) {
			improtenceAdjust= new PowerImprotenceAdjust();
			return improtenceAdjust;
		}
		if (SubtractionImprotenceAdjust.NAME.equals(name)) {
			improtenceAdjust= new SubtractionImprotenceAdjust(runParameter.getAdjustD());
			return improtenceAdjust;
		}
		throw new RuntimeException("无效-ImprotenceAdjust: " + name);
	}

}
