package top.cellargalaxy.dachuangspringboot.subSpace;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class ImprotenceAdjustFactory {
	public static final String[] NAMES = {PowerImprotenceAdjust.NAME, SubtractionImprotenceAdjust.NAME};
	private static ImprotenceAdjust improtenceAdjust;

	public static final ImprotenceAdjust getImprotenceAdjust(RunParameter runParameter) {
		if (improtenceAdjust == null) {
			improtenceAdjust = createImprotenceAdjust(runParameter);
		}
		return improtenceAdjust;
	}

	public static void setImprotenceAdjust(ImprotenceAdjust improtenceAdjust) {
		ImprotenceAdjustFactory.improtenceAdjust = improtenceAdjust;
	}

	public static final ImprotenceAdjust createImprotenceAdjust(RunParameter runParameter) {
		String name = runParameter.getImprotenceAdjustName();
		improtenceAdjust = createImprotenceAdjust(name, runParameter);
		return improtenceAdjust;
	}

	public static final ImprotenceAdjust createImprotenceAdjust(String name, RunParameter runParameter) {
		if (PowerImprotenceAdjust.NAME.equals(name)) {
			return new PowerImprotenceAdjust();
		}
		if (SubtractionImprotenceAdjust.NAME.equals(name)) {
			return new SubtractionImprotenceAdjust(runParameter.getAdjustD());
		}
		throw new RuntimeException("无效-ImprotenceAdjust: " + name);
	}
}
