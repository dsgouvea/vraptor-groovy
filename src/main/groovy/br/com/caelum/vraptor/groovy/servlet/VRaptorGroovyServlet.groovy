package br.com.caelum.vraptor.groovy.servlet

import br.com.caelum.vraptor.groovy.engine.VRaptorScriptEngine;
import groovy.servlet.GroovyServlet
import groovy.servlet.ServletBinding


/**
 * Used to add request attributes in scope of groovy view files
 * 
 * @author dgouvea
 */
class VRaptorGroovyServlet extends GroovyServlet {
	
	@Override
	protected void setVariables(ServletBinding binding) {
		super.setVariables(binding);
		
		def request = binding.getVariable("request")
		
		def session = [:]
		binding.setVariable("session", session)
		request.session.attributeNames.each { name ->
			def val = request.session.getAttribute(name)
			session[name] = val
		}
		
		def param = [:]
		binding.setVariable("param", param)

		request.parameterNames.each { name ->
			def val = request.getParameterValues(name)
			if (val instanceof String[] && val.length == 1)
				val = val[0]
			param[name] = val
		}
		
		def e = request.getAttributeNames()
		while (e.hasMoreElements()) {
			def attributeName = e.nextElement()
			def attr = request.getAttribute(attributeName)
			binding.setVariable(attributeName, attr)
		}
		
		binding.setVariable("scope", binding.variables)
	}
	
	@Override
	protected GroovyScriptEngine createGroovyScriptEngine() {
		return new VRaptorScriptEngine(this)
	}
	
}