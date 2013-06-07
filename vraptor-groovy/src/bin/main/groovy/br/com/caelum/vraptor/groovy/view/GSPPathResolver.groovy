package br.com.caelum.vraptor.groovy.view

import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.view.DefaultPathResolver;

@Component
class GSPPathResolver extends DefaultPathResolver {
	
	GSPPathResolver(FormatResolver resolver) {
		super(resolver)
	}
	
	@Override
	protected String getPrefix() {
		return "/WEB-INF/gsp/";
	}
	
	@Override
	protected String getExtension() {
		return "gsp";
	}
	
}