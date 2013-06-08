package br.com.caelum.vraptor.groovy.servlet

import br.com.caelum.vraptor.groovy.engine.VRaptorScriptEngine;
import groovy.servlet.GroovyServlet
import groovy.servlet.ServletBinding

class VRaptorGroovyServlet extends GroovyServlet {
	
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
	}
	
	@Override
	protected GroovyScriptEngine createGroovyScriptEngine() {
		return new VRaptorScriptEngine(this)
	}
	
}