package br.com.caelum.vraptor.groovy.engine

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

import org.codehaus.groovy.control.CompilationFailedException

class VRaptorTemplateEngine extends SimpleTemplateEngine {

	private static int counter = 1

	private GroovyShell groovyShell
	
	VRaptorTemplateEngine() {
		this(GroovyShell.class.getClassLoader())
	}

	VRaptorTemplateEngine(boolean verbose) {
		this(GroovyShell.class.getClassLoader())
		setVerbose(verbose);
	}

	VRaptorTemplateEngine(ClassLoader parentLoader) {
		this(new GroovyShell(parentLoader))
	}

	VRaptorTemplateEngine(GroovyShell groovyShell) {
		super(groovyShell);
		
		this.groovyShell = groovyShell
	}

	@Override
	public Template createTemplate(Reader reader) throws CompilationFailedException, IOException {
		VRaptorTemplate template = new VRaptorTemplate()
		String script = template.parse(reader)
		try {
			template.script = groovyShell.parse(script, "OneTemplateScript" + counter++ + ".groovy")
		} catch (Exception e) {
			throw new GroovyRuntimeException("Failed to parse template script (your template may contain an error or be trying to use expressions not currently supported): " + e.getMessage())
		}
		return template
	}

}