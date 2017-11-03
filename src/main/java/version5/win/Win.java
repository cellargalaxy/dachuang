//package version4.win;
//
//import version5.dataSet.DataSetParameter;
//import version5.evidenceSynthesis.*;
//import version5.feature.AverageFeatureSeparation;
//import version5.feature.CustomizeFeatureSeparation;
//import version5.feature.FeatureSeparation;
//import version5.feature.MedianFeatureSeparation;
//import version5.hereditary.HereditaryParameter;
//import version5.hereditary.OrderParentChrosChoose;
//import version5.hereditary.ParentChrosChoose;
//import version5.hereditary.RouletteParentChrosChoose;
//import version5.run.RunFeatureSelectionAndSubSpace;
//import version5.run.RunParameter;
//import version5.run.TestRunParameter;
//import version5.subSpace.*;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.*;
//import java.util.Properties;
//
///**
// * Created by cellargalaxy on 17-10-14.
// */
//public class Win extends JFrame implements ActionListener {
//	private static Win win;
//
//	private static JPanel panel;
//	private static JButton createConfButton;
//	private static JButton readConfButton;
//	private static JButton dataSetButton;
//	private static JFileChooser dataSetChooser;
//	private static JButton outputButton;
//	private static JFileChooser outputFolderChooser;
//	private static JButton startButton;
//
//	private static JLabel createSubDataSetl;
//	private static JLabel testl;
//	private static JTextField testt;
//	private static JLabel missl;
//	private static JTextField misst;
//	private static JLabel label1l;
//	private static JTextField label1t;
//	private static JButton createSubDataSetButton;
//
//	private static JTextArea textArea;
//	private static JLabel fnMinl;
//	private static JTextField fnMint;
//	private static JLabel snl;
//	private static JTextField snt;
//	private static JButton createSubSpacesButton;
//
//
//	private static File dataSetFile;
//	private static File outputFolder;
//	private static RunParameter runParameter=new TestRunParameter();
//	private static DataSetParameter dataSetParameter;
//	private static EvidenceSynthesis[] evidenceSyntheses = {new TeaEvidenceSynthesis(), new DistanceEvidenceSynthesis(), new Distance2EvidenceSynthesis()};
//
//	private static HereditaryParameter hereditaryParameter;
//	private static ParentChrosChoose parentChrosChoose;
//
//	private static FeatureSeparation featureSeparation;
//	private static double stop;
//
//	private static ImprotenceAdjust improtenceAdjust;
//	private static SubSpaceCreate subSpaceCreate;
//	private static EvidenceSynthesis subSpaceEvidenceSynthesis;
//
//
//
//	public static void main(String[] args) {
//		Win.win=new Win();
//	}
//
//	public Win() throws HeadlessException {
//		setBounds(0, 0, 600, 300);
//		panel=new JPanel();
//		setContentPane(panel);
//
//		createConfButton=new JButton("生成配置文件");
//		createConfButton.addActionListener(this);
//		createConfButton.setEnabled(false);
//		panel.add(createConfButton);
//
//		readConfButton=new JButton("读取配置文件");
//		readConfButton.addActionListener(this);
//		panel.add(readConfButton);
//
//		dataSetButton=new JButton("选择数据集");
//		dataSetButton.addActionListener(this);
//		panel.add(dataSetButton);
//
//		dataSetChooser=new JFileChooser();
//		dataSetChooser.setCurrentDirectory(new File("."));
//		dataSetChooser.setMultiSelectionEnabled(false);
//		dataSetChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//
//		outputButton=new JButton("选择输出文件夹");
//		outputButton.addActionListener(this);
//		panel.add(outputButton);
//
//		outputFolderChooser=new JFileChooser();
//		outputFolderChooser.setCurrentDirectory(new File("."));
//		outputFolderChooser.setMultiSelectionEnabled(false);
//		outputFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//
//		startButton=new JButton("开始计算");
//		startButton.addActionListener(this);
//		panel.add(startButton);
//
//
//		createSubDataSetl =new JLabel("----------------------------------------------------------------------------------");
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
//		createSubDataSetButton =new JButton("设置子数据集参数");
//		createSubDataSetButton.addActionListener(this);
//		createSubDataSetButton.setEnabled(false);
//		panel.add(createSubDataSetButton);
//
//		textArea=new JTextArea();
//		textArea.setColumns(100);
//		textArea.setRows(10);
//		panel.add(textArea);
//
//		fnMinl=new JLabel("fnMin");
//		panel.add(fnMinl);
//
//		fnMint=new JTextField(10);
//		panel.add(fnMint);
//
//		snl=new JLabel("sn(用英文逗号分割)");
//		panel.add(snl);
//
//		snt=new JTextField(30);
//		panel.add(snt);
//
//		createSubSpacesButton=new JButton("设置子空间集参数");
//		createSubSpacesButton.addActionListener(this);
//		createSubSpacesButton.setEnabled(false);
//		panel.add(createSubSpacesButton);
//
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////		pack();
//		setVisible(true);
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		if (e.getSource()==createConfButton) {
//			try {
//				createConfFile();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}else if (e.getSource()==readConfButton){
//			try {
//				readConf();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}else if (e.getSource()==dataSetButton){
//			dataSetChooser.showOpenDialog(null);
//			dataSetFile=dataSetChooser.getSelectedFile();
//			System.out.println("选择了数据集："+dataSetFile.getAbsolutePath());
//		}else if (e.getSource()==outputButton){
//			outputFolderChooser.showOpenDialog(null);
//			outputFolder=outputFolderChooser.getSelectedFile();
//			System.out.println("选择了输出文件夹："+outputFolder.getAbsolutePath());
//		}else if (e.getSource()==startButton){
//			try {
//				setTitle("正在计算:"+dataSetFile.getName());
//				createConfButton.setEnabled(false);
//				readConfButton.setEnabled(false);
//				dataSetButton.setEnabled(false);
//				outputButton.setEnabled(false);
//				startButton.setEnabled(false);
//				start();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			} catch (ClassNotFoundException e1) {
//				e1.printStackTrace();
//			}
//		}else if (e.getSource()==createSubDataSetButton){
//			runParameter.setTest(new Double(testt.getText()));
//			runParameter.setMiss(new Double(misst.getText()));
//			runParameter.setLabel1(new Double(label1t.getText()));
//			runParameter.doneReceiveCreateSubDataSet();
//			createSubDataSetButton.setEnabled(false);
//			setTitle("正在计算:"+dataSetFile.getName());
//		}else if (e.getSource()==createSubSpacesButton){
//			runParameter.setFnMin(new Integer(fnMint.getText()));
//			String[] strings=snt.getText().split(",");
//			int[] sn=new int[strings.length];
//			for (int i = 0; i < strings.length; i++) {
//				sn[i]=new Integer(strings[i]);
//			}
//			runParameter.setSn(sn);
//			runParameter.donereceiveCreateSubSpaces();
//			createSubSpacesButton.setEnabled(false);
//			setTitle("正在计算:"+dataSetFile.getName());
//		}
//	}
//
//	public static void createSubDataSet(int com0Count, int com1Count, int miss0Count, int miss1Count){
//		win.setTitle("请设置子数据集参数");
//		testt.setText("");
//		misst.setText("");
//		label1t.setText("");
//		createSubDataSetButton.setEnabled(true);
//		createSubDataSetl.setText("完整0标签数量:"+com0Count+"  完整1标签数量:"+com1Count+"  缺失0标签数量:"+miss0Count+"  缺失1标签数量:"+miss1Count);
//	}
//
//	public static void createSubSpaces(double[] impros){
//		win.setTitle("请设置子空间参数");
//		fnMint.setText("");
//		snt.setText("");
//		createSubSpacesButton.setEnabled(true);
//		StringBuffer stringBuffer=new StringBuffer();
//		for (double impro : impros) {
//			stringBuffer.append(impro+"\n");
//		}
//		textArea.setText(stringBuffer.toString());
//	}
//
//	private void start() throws IOException, ClassNotFoundException {
//		final File trainOutputFile=new File(outputFolder.getAbsolutePath()+"/train-out-"+dataSetFile.getName());
//		final File testOutputFile=new File(outputFolder.getAbsolutePath()+"/test-out-"+dataSetFile.getName());
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					System.out.println();
//					RunFeatureSelectionAndSubSpace.run(runParameter, dataSetFile, dataSetParameter, evidenceSyntheses, trainOutputFile, testOutputFile,
//							hereditaryParameter, parentChrosChoose,
//							featureSeparation, stop,
//							subSpaceCreate,
//							subSpaceEvidenceSynthesis);
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}finally {
//					setTitle("计算完成:"+dataSetFile.getName());
////					createConfButton.setEnabled(true);
//					readConfButton.setEnabled(true);
//					dataSetButton.setEnabled(true);
//					outputButton.setEnabled(true);
//					startButton.setEnabled(true);
//				}
//			}
//		}).start();
//	}
//
//	private void readConf() throws IOException {
//		Properties properties = new Properties();
//		File confFile=new File("conf.properties");
//		properties.load(new InputStreamReader(new FileInputStream(confFile)));
//
//		String coding=properties.getProperty("coding");
//		String idCloString=properties.getProperty("idClo");
//		String labelColString=properties.getProperty("labelCol");
//		String evidColString=properties.getProperty("evidCol");
//		String AColString=properties.getProperty("ACol");
//		String BColString=properties.getProperty("BCol");
//		dataSetParameter = new DataSetParameter(coding, new Integer(idCloString), new Integer(labelColString), new Integer(evidColString), new Integer(AColString), new Integer(BColString));
//
//		String chrosNumString=properties.getProperty("chrosNum");
//		String saveChroProString=properties.getProperty("saveChroPro");
//		String geneExProString=properties.getProperty("geneExPro");
//		String geneMutNumProString=properties.getProperty("geneMutNumPro");
//		String geneMutProString=properties.getProperty("geneMutPro");
//		String stepString=properties.getProperty("step");
//		String iterNumString=properties.getProperty("iterNum");
//		String sameNumString=properties.getProperty("sameNum");
//		String sameDeviationString=properties.getProperty("sameDeviation");
//		String parentChrosChooseString=properties.getProperty("parentChrosChoose");
//		hereditaryParameter=new HereditaryParameter(new Integer(chrosNumString), new Double(saveChroProString), new Double(geneExProString),new Double(geneMutNumProString),
//				new Double(geneMutProString), new Double(stepString), new Integer(iterNumString), new Integer(sameNumString), new Double(sameDeviationString));
//		parentChrosChoose=null;
//		if (parentChrosChooseString.equals("RouletteParentChrosChoose")) {
//			parentChrosChoose=new RouletteParentChrosChoose();
//		}else if (parentChrosChooseString.equals("OrderParentChrosChoose")){
//			parentChrosChoose=new OrderParentChrosChoose();
//		}
//
//		String featureSeparationString=properties.getProperty("featureSeparation");
//		String stopString=properties.getProperty("stop");
//		String separationValueString=properties.getProperty("separationValue");
//		featureSeparation=null;
//		if (featureSeparationString.equals("MedianFeatureSeparation")) {
//			featureSeparation=new MedianFeatureSeparation();
//		}else if (featureSeparationString.equals("AverageFeatureSeparation")){
//			featureSeparation=new AverageFeatureSeparation();
//		}else if (featureSeparationString.equals("CustomizeFeatureSeparation")){
//			featureSeparation=new CustomizeFeatureSeparation(new Double(separationValueString));
//		}
//		stop=new Double(stopString);
//
//		String improtenceAdjustString=properties.getProperty("improtenceAdjust");
//		String adjustDString=properties.getProperty("adjustD");
//		String subSpaceCreateString=properties.getProperty("subSpaceCreate");
//		String subSpaceEvidenceSynthesisString=properties.getProperty("subSpaceEvidenceSynthesis");
//		String thrfString=properties.getProperty("thrf");
//		String thrnfString=properties.getProperty("thrnf");
//		String d1String=properties.getProperty("d1");
//		String d2String=properties.getProperty("d2");
//		improtenceAdjust=null;
//		if (improtenceAdjustString.equals("PowerImprotenceAdjust")) {
//			improtenceAdjust=new PowerImprotenceAdjust();
//		}else if (improtenceAdjustString.equals("SubtractionImprotenceAdjust")){
//			improtenceAdjust=new SubtractionImprotenceAdjust(new Double(adjustDString));
//		}
//		subSpaceCreate=null;
//		if (subSpaceCreateString.equals("SnFeatureSelectionSubSpaceCreate")) {
//			subSpaceCreate=new SnFeatureSelectionSubSpaceCreate(runParameter,improtenceAdjust);
//		}else if (subSpaceCreateString.equals("RandomSubSpaceCreate")){
//			subSpaceCreate=new RandomSubSpaceCreate(runParameter);
//		}
//		subSpaceEvidenceSynthesis=null;
//		if (subSpaceEvidenceSynthesisString.equals("AverageEvidenceSynthesis")) {
//			subSpaceEvidenceSynthesis=new AverageEvidenceSynthesis();
//		}else if (subSpaceEvidenceSynthesisString.equals("VoteEvidenceSynthesis")){
//			subSpaceEvidenceSynthesis=new VoteEvidenceSynthesis(new Double(thrfString),new Double(thrnfString),new Double(d1String),new Double(d2String));
//		}
//
//		System.out.println("数据集参数");
//		System.out.println(dataSetParameter);
//		System.out.println("遗传算法参数");
//		System.out.println(hereditaryParameter);
//		System.out.println(parentChrosChoose);
//		System.out.println("特征选择");
//		System.out.println(featureSeparation);
//		System.out.println("stop: "+stop);
//		System.out.println("子空间");
//		System.out.println(improtenceAdjust);
//		System.out.println(subSpaceCreate);
//		System.out.println(subSpaceEvidenceSynthesis);
//	}
//
//	private void createConfFile() throws IOException {
//		File confFile=new File(new File(Win.class.getResource("").getPath()).getParentFile().getParentFile().getAbsolutePath() + "/conf.properties");
//		File file=new File("conf.properties");
//		BufferedInputStream inputStream=new BufferedInputStream(new FileInputStream(confFile));
//		BufferedOutputStream outputStream=new BufferedOutputStream(new FileOutputStream(file));
//		byte[] bytes=new byte[1024];
//		int len;
//		while ((len = inputStream.read(bytes)) != -1) {
//			outputStream.write(bytes,0,len);
//		}
//		inputStream.close();
//		outputStream.close();
//	}
//}
