package br.com.caelum.vraptor.groovy.servlet

import groovy.servlet.ServletBinding
import groovy.servlet.TemplateServlet


/**
 * Used to add request and session attributes in gsp scope 
 *  
 * @author dgouvea
 */
class VRaptorGroovyTemplateServlet extends TemplateServlet {
	
	VRaptorGroovyTemplateServlet() {
		
	}
	
	@Override
	protected void setVariables(ServletBinding binding) {
		super.setVariables(binding);
		
		def request = binding.getVariable("request")
		binding.setVariable("session", request.session)
		
		def sessionScope = [:]
		binding.setVariable("sessionScope", sessionScope)
		request.session.attributeNames.each { name ->
			def val = request.session.getAttribute(name)
			sessionScope[name] = val
		}
		
		def param = [:]
		binding.setVariable("param", param)

		request.parameterNames.each { name ->
			def val = request.getParameterValues(name)
			if (val instanceof String[] && val.length == 1)
				val = val[0]
			param[name] = val
		}
		
		def requestScope = [:]
		binding.setVariable("requestScope", requestScope)
		
		def e = request.getAttributeNames()
		while (e.hasMoreElements()) {
			def attributeName = e.nextElement()
			def attr = request.getAttribute(attributeName)
			binding.setVariable(attributeName, attr)
			requestScope[attributeName] = attr
		}
	}
	
}