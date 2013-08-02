<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@include file="/views/include/pub.jsp" %>
<html>
<head>
<title>卡号上传失败</title>
</head>
<body>


<p><strong>上传失败! 如下卡号无法导入:</strong></p>
<c:forEach items="${errors}" var="serial">
<p><strong>${serial}</strong></p>
</c:forEach>

<a href="${path}/views/card/upload.jsp">返回</a>

</body>

</html>