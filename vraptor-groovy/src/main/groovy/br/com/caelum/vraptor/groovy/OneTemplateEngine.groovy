package br.com.caelum.vraptor.groovy

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

import org.codehaus.groovy.control.CompilationFailedException

class OneTemplateEngine extends SimpleTemplateEngine {

	private static int counter = 1

	private GroovyShell groovyShell
	
	OneTemplateEngine() {
		this(GroovyShell.class.getClassLoader())
	}

	OneTemplateEngine(boolean verbose) {
		this(GroovyShell.class.getClassLoader())
		setVerbose(verbose);
	}

	OneTemplateEngine(ClassLoader parentLoader) {
		this(new GroovyShell(parentLoader))
	}

	OneTemplateEngine(GroovyShell groovyShell) {
		super(groovyShell);
		
		this.groovyShell = groovyShell
	}

	@Override
	public Template createTemplate(Reader reader) throws CompilationFailedException, IOException {
		OneTemplate template = new OneTemplate()
		String script = template.parse(reader)
		try {
			template.script = groovyShell.parse(script, "OneTemplateScript" + counter++ + ".groovy")
		} catch (Exception e) {
			throw new GroovyRuntimeException("Failed to parse template script (your template may contain an error or be trying to use expressions not currently supported): " + e.getMessage())
		}
		return template
	}

}