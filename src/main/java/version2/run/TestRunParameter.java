package version2.run;

/**
 * Created by cellargalaxy on 17-9-18.
 */
public class TestRunParameter extends RunParameter {
	public void receiveCreateSubDataSet(int com0Count, int com1Count, int miss0Count, int miss1Count) {
		setTest(0.5);
		setMiss((double) (miss0Count+miss1Count)/(com0Count+com1Count+miss0Count+miss1Count));
		setLabel1((double) (com1Count+miss1Count)/(com0Count+com1Count+miss0Count+miss1Count));
	}
	
	public void receiveCreateSubSpaces(double[] impros) {
		setSn(new int[]{1,2,3,4});
		setFnMin(25);
	}
}
