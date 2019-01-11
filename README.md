test
## 对接api文档 v2.0
### 通用
1.约定返回格式为json

2.json基本格式 下面会给几个示例
```json
{
    "status": 0, // 0表示成功,1表示失败
    "data":{}//所有需要返回的数据全部放进去这个对象
}
```
###算法服务三个接口的请求

app.post('/checkDataSet', function (req, res)
```
//req包含文件
```
app.post('/checkConfig', function (req, res)

// req包含参数
```java
//使用req的getParameter对象获取名为runParameter的字符串，此字符串为json参数的字符串表示
String runParameter = req.getParameter("runParameter");
```
runParameter对应的json结构如下
```json
{
    "trainMiss": 0.1, //训练集缺失比例(0,1)
    "test": 0.5, //测试集比例(0,1)
    "subSpaceEvidenceSynthesisNum": 1, //多推理结果合成方法,1:平均法多推理结果合成;2:投票法多推理结果合成
    "evaluationExecutorNum": 2, //单多线程,1:单线程;2:多线程
    "featureSeparationNum": 1, //特征分离算法,1:平均数特征分割;2:中位数特征分割;3:用户指定值特征分割
    "testMiss": 0.2, //测试集缺失比例(0,1)
    "label1": 0.1, //标签1比例(0,1)
    "subSpaceCreateNum": 2, //子空间,1:完全随机子空间;2:sn随机子空间;3:sn特征选择子空间
    "hereditaryParameter": {
        "sameNum": 100, //最多相同最大解次数(0,+00)
        "chrosNum": 30, //染色体数(0,+00)
        "geneMutNumPro": 0.5, //基因突变比例(0,1)
        "baseMutPro": 0.3, //基因突变概率(0,1)
        "sameDeviation": 0.001, //相同解范围(0,1)
        "step": 0.01, //基因步长(0,1)
        "iterNum": 500, //迭代次数(0,+00)
        "saveChroPro": 0.5, //保留染色体比例(0,1)
        "geneExPro": 0.5 //基因交换比例(0,1)
    },
    "stop": 0.01, //特征选择停止参数(0,1)
    "improtenceAdjustNum": 1, //特征调整算法,1:平方调整算法;2:减法调整算法
    "evaluationNum": 1, //评价算法,1:auc;2:svm
    "parentChrosChooseNum": 2, //父母染色体选择算法,1:顺序法父母染色体选择;2:轮盘法父母染色体选择
    "fnMins": [ 1,3, 6, 13, 15], //sn子空间个数最小值，长度需要个sns一样
    "sns": [
        [1],
        [1,2],
        [1, 2, 3],
        [1,2, 3,4],
        [1, 2,3, 4, 5]
    ]
}
```
app.post('/solution', function (req, res)
```
//req包含文件和参数，参考app.post('/checkDataSet', function (req, res)和app.post('/checkConfig', function (req, res)
```

###算法服务三个接口的响应
```js
app.post('/checkDataSet', function (req, res) {
    // req 包含 文件
    if(check){
        res.json({
            status: 0,
            data:{
                info:{
                    com0:11,
                    com1:22,
                    miss0:33,
                    miss1:44
                },
                message:'成功'
            }
        })
    } else {
        res.json({
            status: 1,
            data:{
                message:'失败原因'
            }
        })
    }
});

app.post('/checkConfig', function (req, res) {
    // req 包含 参数
    if(check){
        res.json({
            status: 0,
            data:{
                message:'参数合法'
            }
        })
    } else {
        res.json({
            status: 1,
            data:{
                message:'某某某参数不合法'
            }
        })
    }
});

app.post('/solution', function (req, res) {
    // req 包含 文件和参数
    if(check){
        res.json({
            status: 0,
            data:{
                message:'已经开始计算'
            }
        })
    } else {
        res.json({
            status: 1,
            data:{
                message:'开始计算失败原因'
            }
        })
    }
});
```
###界面回调接口的请求
```js
app.post('/solutionResult', function (req, res) {
    // req 包含 文件和参数
    if(check){
        res.json({
            status: 0,
            data:{
                     "trainDataSetOutput": {
                         "trainMiss": 0.1, //训练集缺失比例
                         "test": 0.5, //测试集比例
                         "datas": [
                             {
                                 "A": 0.15750631069790627,
                                 "AB": 0.774374525857427,
                                 "B": 0.06811916344466667,
                                 "id": "1442",
                                 "label": 1
                             },
                             {
                                 "A": 0.09151763628348958,
                                 "AB": 0.8131397609279167,
                                 "B": 0.09534260278859377,
                                 "id": "2773",
                                 "label": 0
                             }
                         ],
                         "subSpaces": [
                             {
                                 "subSpace": [
                                     1,
                                     3,
                                     4,
                                     5
                                 ],
                                 "subSpaceAUC": 0.7572267441860465,
                                 "chro": "[0.08, 0.5, 0.9400000000000001, 0.68, 0.12, 0.060000000000000005, 0.86, 0.65]"
                             },
                             {
                                 "subSpace": [
                                     1,
                                     2,
                                     6
                                 ],
                                 "subSpaceAUC": 0.8272761627906976,
                                 "chro": "[0.21000000000000002, 0.15000000000000002, 0.060000000000000005, 0.26, 0.77, 0.29000000000000004]"
                             }
                         ],
                         "miss0Count": 0, //缺失0标签个数
                         "testMiss": 0.2, //测试集缺失比例
                         "subSpaceCreate": "SnRandomSubSpaceCreate(sn随机子空间创建){sns=[[I@207475a2, [I@6c90063f, [I@55c3222d, [I@456b12c3, [I@4631a6a9], fnMins=[1, 3, 6, 13, 15]}", //子空间创建方法
                         "com0Count": 2400, //完整0标签个数
                         "label1": 0.1, //1标签比例
                         "parentChrosChoose": "RouletteParentChrosChoose{轮盘父母染色体选择算法}", //父母染色体选择算法
                         "AUC": 0.8473953488372094, //最终auc值
                         "evaluation": DsEvidenceSynthesis, //评价算法
                         "miss1Count": 0, //缺失1标签个数
                         "evidNameToIds": [
                             {
                                 "evidId": 1,
                                 "evidName": "m1"
                             },
                             {
                                 "evidId": 2,
                                 "evidName": "m2"
                             }
                         ],
                         "counts": [
                             {
                                 "count": "7-1[1, 2, 3, 4, 5, 6, 7]",
                                 "pro": 0.12955465587044535
                             },
                             {
                                 "count": "7-0[1, 2, 3, 4, 5, 6, 7]",
                                 "pro": 0.8803986710963455
                             }
                         ],
                         "com1Count": 402 //完整1标签个数
                     },
                     "testDataSetOutput": {//与trainDataSetOutput一样的结构}
                 }
        })
    } else {
        res.json({
            status: 1,
            data:{
                message:'计算失败原因'
            }
        })
    }
});
```
###界面回调接口的响应
```js
app.post('/solutionResult', function (req, res) {
    // req 包含 文件和参数
    res.json({
        status: 0,
        data:{
            message:'成功解析返回结果，知道计算完毕之类的'
        }
    })
});
```
