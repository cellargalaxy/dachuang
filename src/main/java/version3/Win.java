//package version3;
//
//import version4.dataSet.DataSetParameter;
//import version4.evidenceSynthesis.*;
//import version4.feature.AverageFeatureSeparation;
//import version4.feature.CustomizeFeatureSeparation;
//import version4.feature.FeatureSeparation;
//import version4.feature.MedianFeatureSeparation;
//import version4.hereditary.HereditaryParameter;
//import version4.hereditary.OrderParentChrosChoose;
//import version4.hereditary.ParentChrosChoose;
//import version4.hereditary.RouletteParentChrosChoose;
//import version4.run.Run;
//import version4.run.RunParameter;
//import version4.run.TestRunParameter;
//import version4.subSpace.*;
//import version4.win.CreateSubDataSetWin;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//
///**
// * Created by cellargalaxy on 17-10-1.
// */
//public class Win extends JFrame implements ActionListener {
//
//	public Win(String title) throws HeadlessException {
//		super(title);
//	}
//
//	private JPanel jPanel;
//	private JPanel dataSetPanel;
//	private JPanel hereditaryPanel;
//	private JPanel subSpacePanel;
//	private JTextField dataSetj;
//	private JTextField outputj;
//	private JTextField dataSetCodingj;
//	private JTextField idj;
//	private JTextField labelj;
//	private JTextField evij;
//	private JTextField Aj;
//	private JTextField Bj;
//	private JTextField chrosNumj;
//	private JTextField saveChroProj;
//	private JTextField geneExProj;
//	private JTextField geneMutNumProj;
//	private JTextField geneMutProj;
//	private JTextField stepj;
//	private JTextField iterNumj;
//	private JTextField sameNumj;
//	private JTextField sameDeviationj;
//	private JRadioButton rouletteParentChrosChoosej;
//	private JRadioButton orderParentChrosChoosej;
//	private JPanel featurePanel;
//	private JRadioButton randomSubSpaceCreatej;
//	private JRadioButton snSubSpaceCreatej;
//	private JRadioButton powerImprotenceAdjustj;
//	private JRadioButton subtractionImprotenceAdjustj;
//	private JTextField adjustDj;
//	private JRadioButton averageEvidenceSynthesisj;
//	private JRadioButton voteEvidenceSynthesisj;
//	private JTextField thrfj;
//	private JTextField thrnfj;
//	private JTextField d1j;
//	private JTextField d2j;
//	private JRadioButton medianFeatureSeparationj;
//	private JRadioButton averageFeatureSeparationj;
//	private JRadioButton customizeFeatureSeparationj;
//	private JTextField separationValuej;
//	private JButton button;
//	private JPanel buttonPanel;
//
//	private ButtonGroup parentChrosChooseButtonGroup = new ButtonGroup();
//	private ButtonGroup featureButtonGroup = new ButtonGroup();
//	private ButtonGroup subSpaceCreateButtonGroup = new ButtonGroup();
//	private ButtonGroup improtenceAdjustButtonGroup = new ButtonGroup();
//	private ButtonGroup subSpaceEvidenceSynthesisButtonGroup = new ButtonGroup();
//
//	private RunParameter runParameter=new TestRunParameter();
//	private File dataSetFile;
//	private DataSetParameter dataSetParameter;
//	private File outputFolder;
//	private HereditaryParameter hereditaryParameter;
//	private ParentChrosChoose parentChrosChoose;
//	private FeatureSeparation featureSeparation;
//	private double stop;
//	private ImprotenceAdjust improtenceAdjust;
//	private SubSpaceCreate subSpaceCreate;
//	private EvidenceSynthesis subSpaceEvidenceSynthesis;
//
//
//	public static void main(String[] args) {
//		Win win = new Win("Win");
//		win.setContentPane(win.jPanel);
//		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		win.pack();
//		win.setVisible(true);
//		win.button.addActionListener(win);
//
//		win.parentChrosChooseButtonGroup.add(win.rouletteParentChrosChoosej);
//		win.parentChrosChooseButtonGroup.add(win.orderParentChrosChoosej);
//
//		win.featureButtonGroup.add(win.medianFeatureSeparationj);
//		win.featureButtonGroup.add(win.averageFeatureSeparationj);
//		win.featureButtonGroup.add(win.customizeFeatureSeparationj);
//
//		win.subSpaceCreateButtonGroup.add(win.randomSubSpaceCreatej);
//		win.subSpaceCreateButtonGroup.add(win.snSubSpaceCreatej);
//
//		win.improtenceAdjustButtonGroup.add(win.powerImprotenceAdjustj);
//		win.improtenceAdjustButtonGroup.add(win.subtractionImprotenceAdjustj);
//
//		win.subSpaceEvidenceSynthesisButtonGroup.add(win.averageEvidenceSynthesisj);
//		win.subSpaceEvidenceSynthesisButtonGroup.add(win.voteEvidenceSynthesisj);
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		try{
//			setParameter();
//			EvidenceSynthesis[] evidenceSyntheses = {new TeaEvidenceSynthesis(), new MyEvidenceSynthesis(), new My2EvidenceSynthesis()};
//			File trainOutputFile=new File(outputFolder.getAbsolutePath()+"/train-"+dataSetFile.getName());
//			File testOutputFile=new File(outputFolder.getAbsolutePath()+"/test-"+dataSetFile.getName());
//
//			Run.run(runParameter, dataSetFile, dataSetParameter, evidenceSyntheses, trainOutputFile, testOutputFile,
//					hereditaryParameter, parentChrosChoose,
//					featureSeparation, stop,
//					subSpaceCreate,
//					subSpaceEvidenceSynthesis);
//		}catch (Exception ex){
//			ex.printStackTrace();
//		}
//
//
//	}
//
//	private void setParameter() {
//		dataSetFile = new File(dataSetj.getText());
//		dataSetParameter = new DataSetParameter(dataSetCodingj.getText(), new Integer(idj.getText()), new Integer(labelj.getText()),
//				new Integer(evij.getText()), new Integer(Aj.getText()), new Integer(Bj.getText()));
//		outputFolder = new File(outputj.getText());
//		hereditaryParameter = new HereditaryParameter(new Integer(chrosNumj.getText()), new Double(saveChroProj.getText()), new Double(geneExProj.getText()),
//				new Double(geneMutNumProj.getText()), new Double(geneMutProj.getText()), new Double(stepj.getText()), new Integer(iterNumj.getText()),
//				new Integer(sameNumj.getText()), new Double(sameDeviationj.getText()));
//
//		if (rouletteParentChrosChoosej.isSelected()) {
//			parentChrosChoose=new RouletteParentChrosChoose();
//		} else if (orderParentChrosChoosej.isSelected()) {
//			parentChrosChoose=new OrderParentChrosChoose();
//		}
//		if (medianFeatureSeparationj.isSelected()) {
//			featureSeparation=new MedianFeatureSeparation();
//		} else if (averageFeatureSeparationj.isSelected()) {
//			featureSeparation=new AverageFeatureSeparation();
//		} else if (customizeFeatureSeparationj.isSelected()) {
//			featureSeparation=new CustomizeFeatureSeparation(new Double(separationValuej.getText()));
//		}
//
//		stop=new Double(stepj.getText());
//
//		if (powerImprotenceAdjustj.isSelected()) {
//			improtenceAdjust=new PowerImprotenceAdjust();
//		} else if ( subtractionImprotenceAdjustj.isSelected()) {
//			improtenceAdjust=new SubtractionImprotenceAdjust(new Double(adjustDj.getText()));
//		}
//		if (randomSubSpaceCreatej.isSelected()) {
//			subSpaceCreate=new RandomSubSpaceCreate(runParameter);
//		} else if (snSubSpaceCreatej.isSelected()) {
//			subSpaceCreate=new SnSubSpaceCreate(runParameter,improtenceAdjust);
//		}
//		if ( averageEvidenceSynthesisj.isSelected()) {
//			subSpaceEvidenceSynthesis=new AverageEvidenceSynthesis();
//		} else if (voteEvidenceSynthesisj.isSelected()) {
//			subSpaceEvidenceSynthesis=new VoteEvidenceSynthesis(new Double(thrfj.getText()),new Double(thrnfj.getText()),new Double(d1j.getText()),new Double(d2j.getText()));
//		}
//	}
//
//
//	class RunParameterWin extends RunParameter{
//
//		public void receiveCreateSubDataSet(int com0Count, int com1Count, int miss0Count, int miss1Count) {
//			CreateSubDataSetWin.receiveCreateSubDataSet(com0Count,com1Count,miss0Count,miss1Count,this);
//		}
//
//		public void doneReceiveCreateSubDataSet() {
//
//		}
//
//		public void receiveCreateSubSpaces(double[] impros) {
//			CreateSubSpacesWin.receiveCreateSubSpaces(impros,this);
//		}
//
//		public void donereceiveCreateSubSpaces() {
//
//		}
//	}
//}
