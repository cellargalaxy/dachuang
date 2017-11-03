//package version4.win;
//
//import version5.run.RunParameter;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
///**
// * Created by cellargalaxy on 17-10-14.
// */
//public class CreateSubDataSetWin extends JFrame implements ActionListener {
//	private JPanel panel;
//
//	private JLabel createSubDataSetl;
//	private JLabel testl;
//	private JTextField testt;
//	private JLabel missl;
//	private JTextField misst;
//	private JLabel label1l;
//	private JTextField label1t;
//	private JButton createSubDataSetButton;
//
//	private final RunParameter runParameter;
//
//	public CreateSubDataSetWin(RunParameter runParameter,int com0Count, int com1Count, int miss0Count, int miss1Count) throws HeadlessException {
//		this.runParameter=runParameter;
//		panel=new JPanel();
//		setContentPane(panel);
//
//		createSubDataSetl =new JLabel("完整0标签数量:"+com0Count+"  完整1标签数量:"+com1Count+"  缺失0标签数量:"+miss0Count+"  缺失1标签数量:"+miss1Count+"  ");
//		panel.add(createSubDataSetl);
//
//		testl=new JLabel("测试集比例");
//		panel.add(testl);
//
//		testt=new JTextField(10);
//		panel.add(testt);
//
//		missl=new JLabel("缺失对象比例");
//		panel.add(missl);
//
//		misst=new JTextField(10);
//		panel.add(misst);
//
//		label1l=new JLabel("标签1比例");
//		panel.add(label1l);
//
//		label1t=new JTextField(10);
//		panel.add(label1t);
//
//		createSubDataSetButton =new JButton("确定");
//		createSubDataSetButton.addActionListener(this);
//		panel.add(createSubDataSetButton);
//
//		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//		setVisible(true);
//		pack();
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		runParameter.setTest(new Double(testt.getText()));
//		runParameter.setMiss(new Double(misst.getText()));
//		runParameter.setLabel1(new Double(label1t.getText()));
//		dispose();
//		runParameter.doneReceiveCreateSubDataSet();
//	}
//}
