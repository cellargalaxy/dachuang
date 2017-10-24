//package version3;
//
//
//
//import version3.run.RunParameter;
//import version3.run.TestRunParameter;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.concurrent.CountDownLatch;
//
///**
// * Created by cellargalaxy on 17-10-2.
// */
//public class CreateSubDataSetWin extends JFrame implements ActionListener,Runnable {
//	private JTextField testj;
//	private JTextField missj;
//	private JTextField label1j;
//	private JButton button;
//	private JPanel createSubDataSetPanel;
//	private JPanel buttonPanel;
//	private JPanel panel;
//	private JLabel com0Countj;
//	private JLabel com1Countj;
//	private JLabel miss0Countj;
//	private JLabel miss1Countj;
//
//	private final RunParameter runParameter;
//	private final CountDownLatch countDownLatch;
//
//	final int com0Count;
//	final int com1Count;
//	final int miss0Count;
//	final int miss1Count;
//
//	public CreateSubDataSetWin(RunParameter runParameter, int com0Count, int com1Count, int miss0Count, int miss1Count) throws HeadlessException {
//		this.runParameter = runParameter;
//		this.countDownLatch = new CountDownLatch(1);
//		this.com0Count = com0Count;
//		this.com1Count = com1Count;
//		this.miss0Count = miss0Count;
//		this.miss1Count = miss1Count;
//	}
//
//	public static void main(String[] args) throws InterruptedException {
//		RunParameter runParameter=new TestRunParameter();
//		receiveCreateSubDataSet(1,2,3,4,runParameter);
//		System.out.println("goon");
//	}
//
//	public static void receiveCreateSubDataSet(int com0Count, int com1Count, int miss0Count, int miss1Count,RunParameter runParameter){
//		CreateSubDataSetWin createSubDataSetWin=new CreateSubDataSetWin(runParameter,com0Count,com1Count,miss0Count,miss1Count);
//
//		new Thread(createSubDataSetWin).start();
//
////		try {
////			createSubDataSetWin.countDownLatch.await();
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//	}
//
//
//	public void actionPerformed(ActionEvent e) {
//		try{
//			runParameter.setTest(new Double(testj.getText()));
//			runParameter.setMiss(new Double(missj.getText()));
//			runParameter.setLabel1(new Double(label1j.getText()));
//			countDownLatch.countDown();
//			dispose();
//			System.out.println("testj.getText():"+testj.getText());
//			System.out.println("missj.getText():"+missj.getText());
//			System.out.println("label1j.getText():"+label1j.getText());
//		}catch (Exception ex){
//			ex.printStackTrace();
//		}
//	}
//
//	public void run() {
//		this.setTitle("CreateSubDataSetWin");
//		this.setContentPane(this.panel);
//		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//
//		this.button.addActionListener(this);
//		this.com0Countj.setText(com0Count+"");
//		this.com1Countj.setText(com1Count+"");
//		this.miss0Countj.setText(miss0Count+"");
//		this.miss1Countj.setText(miss1Count+"");
//
//		this.pack();
//		this.setVisible(true);
//		repaint();
//	}
//}
