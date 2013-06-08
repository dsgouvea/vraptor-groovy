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
		def e = request.getAttributeNames()
		while (e.hasMoreElements()) {
			def attributeName = e.nextElement()
			def attr = request.getAttribute(attributeName)
			binding.setVariable(attributeName, attr)
		}
		
		def param = [:]
		binding.setVariable("param", param)

		request.parameterNames.each { name ->
			def val = request.getParameterValues(name)
			if (val instanceof String[] && val.length == 1)
				val = val[0]
			param[name] = val
		}
		
		def session = [:]
		binding.setVariable("session", session)
		request.session.attributeNames.each { name ->
			def val = request.session.getAttribute(name)
			session[name] = val
		}
		
		binding.setVariable("scope", binding.variables)
	}
	
}