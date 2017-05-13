package dataSet;


import java.io.*;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/4/22.
 */
public class LabelSet {


	
	/**
	 * LinkedList<int[]>的int[] ints=new int[2]:
	 * ints[0]=嫌疑人的id，ints[1]=实际是否有罪
	 *
	 * @param labelSetFile
	 * @param separator
	 * @return
	 * @throws IOException
	 */
	public static LinkedList<int[]> readLabelSet(File labelSetFile, String separator) throws IOException {
		LinkedList<int[]> labelSet = new LinkedList<int[]>();
		BufferedReader reader = new BufferedReader(new FileReader(labelSetFile));
		String string;
		while ((string = reader.readLine()) != null) {
			String[] strings = string.split(separator);
			int[] ints = new int[2];
			ints[0] = new Integer(strings[0]);
			ints[1] = new Integer(strings[1]);
			labelSet.add(ints);
		}
		return labelSet;
	}
	
}
