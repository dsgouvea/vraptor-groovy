package br.com.caelum.vraptor.groovy.ioc.groovy

import br.com.caelum.vraptor.ComponentRegistry
import br.com.caelum.vraptor.groovy.http.GroovyParameterNameProvider;
import br.com.caelum.vraptor.groovy.proxy.GroovyJavassistProxifier;
import br.com.caelum.vraptor.groovy.view.GSPPathResolver;
import br.com.caelum.vraptor.groovy.view.GroovyPageResult;
import br.com.caelum.vraptor.http.ParameterNameProvider
import br.com.caelum.vraptor.ioc.spring.SpringProvider
import br.com.caelum.vraptor.proxy.Proxifier
import br.com.caelum.vraptor.view.PageResult
import br.com.caelum.vraptor.view.PathResolver


class GroovyProvider extends SpringProvider {
	
	protected void registerCustomComponents(ComponentRegistry registry) {
		super.registerCustomComponents(registry)
		registry.register(ParameterNameProvider.class, GroovyParameterNameProvider.class);
		registry.register(PathResolver.class, GSPPathResolver.class);
		registry.register(PageResult.class, GroovyPageResult.class);
		registry.register(Proxifier.class, GroovyJavassistProxifier.class);
	}
	
}