<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@include file="/views/include/pub.jsp" %>
<html>
<head>
<title>APK上传</title>
</head>

<body>

<form id="form" enctype="multipart/form-data" action="${path}/api/apk/upload" method="post" style="display:block;">
	<input type="hidden" id="token" name="token" value="">
	
	<table class="ecgprops">
        <tr>
        <td width="200px"><label>版本号:</label></td>
        <td><input type="text" id="version" name="version" value=""></td>
        </tr>
        
        <tr>
        <td><label>是否激活:</label></td>
        <td>
            <select name="enabled">
                <option value="true">是</option>
                <option value="false">否</option>
            </select>
        </td>
        </tr>
        
        <tr>
        <td ><label>APK文件:</label></td>
        <td><input id="file" name="file" type="file" value="请选择APK"></td>
        </tr>
        
        <tr>
        <td valign="top"><label>外部下载地址(可空):</label></td>
        <td>
        <textarea name="externalUrl" style="width:350px" rows=6></textarea>
        <p>注意：即使使用外站链接，也必须在本站上传同版本的APK安装文件，以作备份。</p>
        </td>
        </tr>
    </table>
</form>
<div id="notoken" style="display:none;"> 
	您无法访问该页面
</div>
<div id="loading" style="display:none;"> 
	正在上传，请稍候
</div>

<script>

(function(){

	function toggle(node, flag) {
		node.style.display = flag ? 'block' : 'none';
	}

	var idx = window.location.href.indexOf("token=");
	if (idx < 0) {
		toggle(document.getElementById("form"), false);
		toggle(document.getElementById("notoken"), true);
		return;
	}

	token = window.location.href.substring(idx + 6);

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
		if (!document.getElementById("version").value) {
            alert("请填写版本号!");
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