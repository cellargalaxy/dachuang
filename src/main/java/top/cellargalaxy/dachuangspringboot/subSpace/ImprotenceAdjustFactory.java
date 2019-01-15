package top.cellargalaxy.dachuangspringboot.subSpace;

import top.cellargalaxy.dachuangspringboot.run.RunParameter;

/**
 * Created by cellargalaxy on 17-11-3.
 */
public class ImprotenceAdjustFactory {

	public static final ImprotenceAdjust createImprotenceAdjust() {
		String name = RunParameter.improtenceAdjustName;
		if (PowerImprotenceAdjust.NAME.equals(name)) {
			return new PowerImprotenceAdjust();
		}
		if (SubtractionImprotenceAdjust.NAME.equals(name)) {
			return new SubtractionImprotenceAdjust(RunParameter.adjustD);
		}
		throw new RuntimeException("无效-ImprotenceAdjust: " + name);
	}

}
