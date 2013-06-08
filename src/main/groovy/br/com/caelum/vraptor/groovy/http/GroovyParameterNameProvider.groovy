package br.com.caelum.vraptor.groovy.http

import java.lang.reflect.AccessibleObject
import java.lang.reflect.Constructor
import java.lang.reflect.Method

import br.com.caelum.vraptor.http.DefaultParameterNameProvider
import br.com.caelum.vraptor.interceptor.TypeNameExtractor
import br.com.caelum.vraptor.ioc.ApplicationScoped
import br.com.caelum.vraptor.ioc.Component

import com.thoughtworks.paranamer.AdaptiveParanamer
import com.thoughtworks.paranamer.Paranamer

@Component
@ApplicationScoped
class GroovyParameterNameProvider extends DefaultParameterNameProvider {

	private final Paranamer paranamer = new AdaptiveParanamer()
	
	GroovyParameterNameProvider(TypeNameExtractor extrator) {
		super(extrator)
	}
	
	@Override
	String[] parameterNamesFor(AccessibleObject method) {
		if (method instanceof Method || method instanceof Constructor) {
			try {
				return paranamer.lookupParameterNames(method)
			} catch (e) {
				return getParameterNames(method)
			}			
		} else {
			return getParameterNames(method)
		}
	}
	
	private String[] getParameterNames(AccessibleObject method) {
		def args = Arrays.asList( super.parameterNamesFor(method) )
		args.remove "setProperty"

		String[] array = new String[args.size()]
		args.toArray(array)
		return array
	}
	
}