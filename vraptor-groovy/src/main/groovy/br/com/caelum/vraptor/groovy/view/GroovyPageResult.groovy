package br.com.caelum.vraptor.groovy.view

import groovy.definity.one.web.Html;
import groovy.definity.one.web.Json
import groovy.definity.one.web.Xml;
import groovy.definity.one.web.builder.HTMLBuilder;
import groovy.definity.one.web.builder.XMLBuilder;
import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletResponse

import br.com.caelum.vraptor.core.MethodInfo
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
		if (requestInfo.getResourceMethod().getMethod().isAnnotationPresent(Json)) {
			def obj = requestInfo.getResult()
			JsonBuilder builder = new JsonBuilder()
			builder.call obj
			response.setContentType "application/json"
			response.getWriter().print builder.toPrettyString()
		} else if (requestInfo.getResourceMethod().getMethod().isAnnotationPresent(Html)) {
			response.setContentType "text/html"
		} else if (requestInfo.getResourceMethod().getMethod().isAnnotationPresent(Xml)) {
			response.setContentType "text/xml"
			response.setHeader "Content-type", "application/xhtml+xml"
		} else if (requestInfo.getResult() instanceof HTMLBuilder) {
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