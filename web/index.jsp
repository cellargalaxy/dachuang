<%--
  Created by IntelliJ IDEA.
  User: cellargalaxy
  Date: 17-11-15
  Time: 上午8:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: cellargalaxy
  Date: 17-11-14
  Time: 下午2:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>$Title$</title>
</head>
<body>
<form action="/dachuang/uploadDataSet" method="post" enctype="multipart/form-data">
  <input type="file" name="dataSet" required>
  <button type="submit" >上传数据集</button>
</form>

<form action="/dachuang/checkRunParameter" method="post">
  <input type="text" name="runParameter" required>
  <button type="submit" >上传参数</button>
</form>

<form action="/dachuang/run" method="post" enctype="multipart/form-data">
  <input type="file" name="dataSet" required>
  <input type="text" name="runParameter" required>
  <button type="submit" >上传数据集和参数</button>
</form>
</body>
</html>
