vraptor-groovy
==============

vRaptor Groovy Plugin

Controller
````
@Resource
class IndexController {

  Result result

  IndexController(Result result) {
    this.result = result
  }

  @Path("/")
  void index() {
    result.include "title", "Hello World"
  }

}
````

The request attributes can be access directly $title or with scope $requestScope.title.

If you try access directly $title and your value is null, will be throwed a NullPointerException. Another way, if you access using requestScope.title will be called without exception.

GSP = /WEB-INF/gsp/index/index.gsp
````
<html>
  <head>
		<title>vRaptor Groovy - $title</title>
	</head>
	<body>
		<h1>$requestScope.title</h1>
	</body>
</html>
````
