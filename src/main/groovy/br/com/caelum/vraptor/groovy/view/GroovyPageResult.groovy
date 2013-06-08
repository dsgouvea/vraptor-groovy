package br.com.caelum.vraptor.groovy.view

import javax.servlet.http.HttpServletResponse

import br.com.caelum.vraptor.core.MethodInfo
import br.com.caelum.vraptor.groovy.builder.HTMLBuilder
import br.com.caelum.vraptor.groovy.builder.XMLBuilder
import br.com.caelum.vraptor.http.MutableRequest
import br.com.caelum.vraptor.proxy.Proxifier
import br.com.caelum.vraptor.view.DefaultPageResult
import br.com.caelum.vraptor.view.PathResolver

class GroovyPageResult extends DefaultPageResult {
	
	MethodInfo requestInfo
	HttpServletResponse response	

	GroovyPageResult(MutableRequest req, HttpServletResponse res, MethodInfo requestInfo, PathResolver resolver, Proxifier proxifier) {
		super(req, res, requestInfo, resolver, proxifier)
		
		this.response = res
		this.requestInfo = requestInfo
	}

	@Override
	void defaultView() {
		if (requestInfo.getResult() instanceof HTMLBuilder) {
			response.setContentType "text/html"
			response.getWriter().print requestInfo.getResult().writer.toString()
		} else if (requestInfo.getResult() instanceof XMLBuilder) {
			response.setContentType "text/xml"
			response.setHeader "Content-type", "application/xhtml+xml"
			response.getWriter().print requestInfo.getResult().writer.toString()
		} else {
			super.defaultView()
		}
	}
	
}