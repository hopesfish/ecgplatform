<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@include file="/views/include/pub.jsp" %>
<html>
<head>
<title>卡号上传成功</title>
</head>

<body>

上传${count}条数据成功! 5秒后关闭本窗口!

<script>
(function() {
	setTimeout(function() {
		if(window.parent.closeUploadCardDialog) {
			window.parent.closeUploadCardDialog();
		}
	}, 5000);
})();
</script>

</body>

</html>