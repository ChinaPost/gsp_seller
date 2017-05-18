<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
import="java.util.*" %>
<html>
<head>
<script type="text/javascript" src="ue/ueditor.config.js"></script>
<script type="text/javascript" src="ue/ueditor.all.min.js"></script>
<link rel="styleSheet" href="ue/themes/default/css/ueditor.css">
</head>
<body>
<h2>Hello World!</h2>
<textarea id="newsEditor" name="content">百度编辑器</textarea>
<script type="text/javascript">
var editor = new baidu.editor.ui.Editor();
editor.render('newsEditor');
</script>
</body>
</html>
