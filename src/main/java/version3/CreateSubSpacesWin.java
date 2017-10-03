package version3;

import version3.run.RunParameter;
import version3.run.TestRunParameter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

/**
 * Created by cellargalaxy on 17-10-2.
 */
public class CreateSubSpacesWin extends JFrame implements ActionListener {
	private JPanel panel;
	private JTextArea improsj;
	private JTextField fnMinj;
	private JTextField snj;
	private JButton button;
	
	private final RunParameter runParameter;
	private final CountDownLatch countDownLatch;
	
	public CreateSubSpacesWin(RunParameter runParameter ) {
		this.runParameter = runParameter;
		this.countDownLatch = new CountDownLatch(1);
	}
	
	public static void main(String[] args) throws InterruptedException {
		RunParameter runParameter=new TestRunParameter();
		double[] ints={0.1,0.2,0.3,0.4,0.5};
		receiveCreateSubSpaces(ints,runParameter);
	}
	
	public static void receiveCreateSubSpaces(double[] impros,RunParameter runParameter){
		CreateSubSpacesWin createSubSpacesWin=new CreateSubSpacesWin(runParameter);
		createSubSpacesWin.setTitle("CreateSubSpacesWin");
		createSubSpacesWin.setContentPane(createSubSpacesWin.panel);
		createSubSpacesWin.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		createSubSpacesWin.button.addActionListener(createSubSpacesWin);
		StringBuffer stringBuffer=new StringBuffer("证据的重要程度\n");
		for (int i = 0; i < impros.length; i++) {
			stringBuffer.append((i+1)+": "+impros[i]+"\n");
		}
		createSubSpacesWin.improsj.append(stringBuffer.toString());
		
		createSubSpacesWin.pack();
		createSubSpacesWin.setVisible(true);
		
//		try {
//			createSubSpacesWin.countDownLatch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	public void actionPerformed(ActionEvent e) {
		try{
			String[] strings=snj.getText().split(",");
			int[] sn=new int[strings.length];
			for (int i = 0; i < strings.length; i++) {
				sn[i]=new Integer(strings[i]);
			}
			runParameter.setSn(sn);
			runParameter.setFnMin(new Integer(fnMinj.getText()));
			countDownLatch.countDown();
			dispose();
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
}
