<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@include file="/views/include/pub.jsp" %>
<html>
<head>
<title>APK上传成功</title>
</head>

<body>

<p><strong>APK上传成功，5秒后关闭本窗口! </strong>

<script>
(function() {
	setTimeout(function() {
		if(window.parent.closeUploadAPKDialog) {
			window.parent.closeUploadAPKDialog();
		}
	}, 5000);
})();
</script>

</body>

</html>