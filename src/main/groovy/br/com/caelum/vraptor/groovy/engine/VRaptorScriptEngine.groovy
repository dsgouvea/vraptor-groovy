package br.com.caelum.vraptor.groovy.engine

import groovy.util.GroovyScriptEngine.ScriptCacheEntry

import org.codehaus.groovy.runtime.IOGroovyMethods

class VRaptorScriptEngine extends GroovyScriptEngine {

	private final def lazyAttributes = [:]
	
	VRaptorScriptEngine(ResourceConnector rc, ClassLoader parentClassLoader) {
		super(rc, parentClassLoader);
	}

	VRaptorScriptEngine(ResourceConnector rc) {
		super(rc);
	}

	VRaptorScriptEngine(String url, ClassLoader parentClassLoader) throws IOException {
		super(url, parentClassLoader);
	}

	VRaptorScriptEngine(String url) throws IOException {
		super(url);
	}

	VRaptorScriptEngine(String[] urls, ClassLoader parentClassLoader) throws IOException {
		super(urls, parentClassLoader);
	}

	VRaptorScriptEngine(String[] urls) throws IOException {
		super(urls);
	}

	VRaptorScriptEngine(URL[] roots, ClassLoader parentClassLoader) {
		super(roots, parentClassLoader);
	}

	VRaptorScriptEngine(URL[] roots) {
		super(roots);
	}

	Class loadScriptByName(String scriptName) throws ResourceException, ScriptException {
		def rc = getLazyAttribute("rc")
		def scriptCache = getLazyAttribute("scriptCache")
		def groovyLoader = getLazyAttribute("groovyLoader")
		
		URLConnection conn = rc.getResourceConnection(scriptName)
		String path = conn.getURL().getPath()
		def entry = scriptCache.get(path)
		Class clazz = null
		if (entry != null) clazz = entry.scriptClass
		try {
			if (isSourceNewer(entry)) {
				try {
					String encoding = conn.getContentEncoding() != null ? conn.getContentEncoding() : "UTF-8"
					String content = IOGroovyMethods.getText(conn.getInputStream(), encoding)
					clazz = groovyLoader.parseClass(content, path)
				} catch (IOException e) {
					throw new ResourceException(e)
				}
			}
		} finally {
			super.forceClose(conn)
		}
		return clazz
	}
	
	private def getLazyAttribute(String name) {
		def val = lazyAttributes.get(name)
		if (!val) {
			val = metaClass.getAttribute(GroovyScriptEngine, this, name, false, true)
			lazyAttributes.put name, val
		}
		return val
	}
	
}