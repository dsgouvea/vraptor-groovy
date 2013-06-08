package br.com.caelum.vraptor.groovy

import br.com.caelum.vraptor.Path
import br.com.caelum.vraptor.Resource
import br.com.caelum.vraptor.Result;

@Resource
class IndexController {

	Result result
	
	IndexController(Result result) {
		this.result = result
	}
	
	@Path("/")
	void index() {
		result.include "title", "This is My Title"
	}
		
}