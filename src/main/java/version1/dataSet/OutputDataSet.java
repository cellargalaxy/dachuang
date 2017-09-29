//package version1.dataSet;
//
//import version1.hereditary.Hereditary;
//
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.LinkedList;
//
///**
// * Created by cellargalaxy on 17-7-28.
// */
//public class OutputDataSet {
//    private static final SimpleDateFormat SIMPLE_DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//    public static final String TRAIN="train";
//    public static final String TEST= "svmTest";
//
//
//    public static void outputDataSet(DataSet dataSet,String savePath,String coding,String separator,String name,
//                                     int DSMethod,double[][] featureSelections,LinkedList<LinkedList<Integer>> subSpaces,
//                                     LinkedList<Hereditary> hereditarys,double auc) throws IOException {
//        File saveFile=new File(savePath,SIMPLE_DATE_FORMAT.format(new Date())+"("+name+").csv");
//        System.out.println(saveFile.getAbsolutePath());
//        saveFile.getParentFile().mkdirs();
//        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile),coding));
//        for (Id id : dataSet.getIds()) {
//            double[] ds=id.countDS(1);
//            String string=id.getId()+separator+ds[1]+separator+ds[2]+separator+(1-ds[1]-ds[2])+"\n";
//            bufferedWriter.write(string);
//        }
//        bufferedWriter.write("\n");
//        bufferedWriter.write("DS合成方法"+separator+DSMethod+"\n");
//        bufferedWriter.write("特征选择\n");
//        for (double[] featureSelection : featureSelections) {
//            bufferedWriter.write(featureSelection[0]+separator+featureSelection[1]+"\n");
//        }
//        bufferedWriter.write("子空间以及其参数\n");
//        Iterator<LinkedList<Integer>> subSpaceIterator=subSpaces.iterator();
//        Iterator<Hereditary> hereditaryIterator=hereditarys.iterator();
//        while (subSpaceIterator.hasNext()) {
//            LinkedList<Integer> subSpace=subSpaceIterator.next();
//            Hereditary hereditary=hereditaryIterator.next();
//            for (Integer integer : subSpace) {
//                bufferedWriter.write(integer+separator);
//            }
//            bufferedWriter.write("\n");
//            for (double v : hereditary.getMaxChro()) {
//                bufferedWriter.write(v+separator);
//            }
//            bufferedWriter.write("\n");
//            bufferedWriter.write(hereditary.getMaxAUC()+"\n");
//        }
//        bufferedWriter.write("AUC"+separator+auc);
//
//        bufferedWriter.close();
//    }
//}
