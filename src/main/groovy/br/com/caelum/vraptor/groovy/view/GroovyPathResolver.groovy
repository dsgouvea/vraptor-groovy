package br.com.caelum.vraptor.groovy.view

import javax.servlet.ServletContext

import br.com.caelum.vraptor.http.FormatResolver
import br.com.caelum.vraptor.ioc.Component
import br.com.caelum.vraptor.resource.ResourceMethod
import br.com.caelum.vraptor.view.DefaultPathResolver

@Component
class GroovyPathResolver extends DefaultPathResolver {
	
	ServletContext context

	GroovyPathResolver(ServletContext context, FormatResolver resolver) {
		super(resolver)
		this.context = context
	}
	
	@Override
	protected String getPrefix() {
		return "/WEB-INF/gsp/";
	}
	
	@Override
	protected String getExtension() {
		return "gsp";
	}
	
	@Override
	String pathFor(ResourceMethod method) {
		def path = super.pathFor(method)
		def realPathToViewFile = context.getRealPath(path)
		
		if (!new File(realPathToViewFile).exists()) {
			path = path.replace("/WEB-INF/gsp", "/WEB-INF/groovy").replace(".gsp", ".groovy");
			realPathToViewFile = context.getRealPath(path)
			if (!new File(realPathToViewFile).exists()) {
				path = path.replace("/WEB-INF/groovy", "/WEB-INF/jsp").replace(".groovy", ".jsp");
			}
		}
		
		return path
	}
	
}