<html>
<head>
  <title>Welcome!</title>
</head>
<body>
  <#list tests as test>
	  <h1>Welcome ${test.name}!</h1>
  </#list>
  <p>Our latest product:
  <a href="${testObj.url}">${testObj.name}</a>!
</body>
</html>