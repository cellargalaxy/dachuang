package nFold;

import java.io.File;

/**
 * Created by cellargalaxy on 17-7-28.
 */
public interface NFold {
    /**
     *
     * @param dataSet 数据集源文件
     * @return 这个File[]长度为2，第一个File是训练集，第二个是测试集
     *          训练集和测试集的结构都如下：
     *          第一列是对象的ID，第二列是A，第三列是B，第四列是证据ID，第五列是标签
     *          分隔符用英文逗号
     */
    File[] nFold(File dataSet);
}
