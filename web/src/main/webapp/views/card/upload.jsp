<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@include file="/views/include/pub.jsp" %>
<html>
<head>
<title>卡号上传</title>
</head>

<body>

<form id="form" enctype="multipart/form-data" action="/api/card/upload" method="post" style="display:block;">
	<input type="hidden" id="token" name="token" value="operator1_Employee">
	<input id="file" name="file" type="file">
	<input type="submit" value="提交">
</form>
<div id="notoken" style="display:none;"> 
	您无法访问该页面
</div>
<div id="loading" style="display:none;"> 
	正在上传，请稍后
</div>

<script>

(function(){

	function toggle(node, flag) {
		node.style.display = flag ? 'block' : 'none';
	}

	var token = window.location.href.split("=");
	if (token.length !== 2) {
		toggle(document.getElementById("form"), false);
		toggle(document.getElementById("notoken"), true);
		return;
	}

	token = token[1];

	document.getElementById("token").value = token;

	var loading = false;
	window.upload = function () {

		if (loading) {
			return;
		}
		loading = true;

		if (!document.getElementById("token").value) {
			alert("token无效!");
			return;
		}
		if (!document.getElementById("file").value) {
			alert("请选择上传文件!");
			return;
		}
		document.getElementById("form").submit();

		toggle(document.getElementById("form"), false);
		toggle(document.getElementById("loading"), true);
	};
})();

</script>

</body>

</html>