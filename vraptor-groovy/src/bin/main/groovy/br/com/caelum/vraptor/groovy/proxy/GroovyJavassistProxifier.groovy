package br.com.caelum.vraptor.groovy.proxy

import java.lang.reflect.Method

import javassist.util.proxy.MethodFilter
import javassist.util.proxy.MethodHandler
import javassist.util.proxy.ProxyFactory
import javassist.util.proxy.ProxyObject

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import br.com.caelum.vraptor.ioc.PrototypeScoped
import br.com.caelum.vraptor.proxy.InstanceCreator
import br.com.caelum.vraptor.proxy.MethodInvocation
import br.com.caelum.vraptor.proxy.Proxifier
import br.com.caelum.vraptor.proxy.ProxyInvocationException
import br.com.caelum.vraptor.proxy.SuperMethod

@PrototypeScoped
public class GroovyJavassistProxifier
	implements Proxifier {

	private static final Logger logger = LoggerFactory.getLogger(GroovyJavassistProxifier.class);

	/**
	 * Methods like toString and finalize will be ignored.
	 */
	private static final List<Method> OBJECT_METHODS = Arrays.asList(GroovyObject.class.getDeclaredMethods());

	/**
	 * Do not proxy these methods.
	 */
	private static final MethodFilter IGNORE_BRIDGE_AND_OBJECT_METHODS = new MethodFilter() {
		public boolean isHandled(Method method) {
			if (method.name == "getMetaClass" || method.name.endsWith("getStaticMetaClass"))
				return false
			return !method.isBridge() && !OBJECT_METHODS.contains(method);
		}
	};

	private final InstanceCreator instanceCreator;

	public GroovyJavassistProxifier(InstanceCreator instanceCreator) {
		this.instanceCreator = instanceCreator;
	}

	public <T> T proxify(Class<T> type, MethodInvocation<? super T> handler) {
		final ProxyFactory factory = new ProxyFactory();
		factory.setFilter(IGNORE_BRIDGE_AND_OBJECT_METHODS);

		if (type.isInterface()) {
			factory.setInterfaces([ type ]);
		} else {
			factory.setSuperclass(type);
		}

		Class<?> proxyClass = factory.createClass();

		Object proxyInstance = instanceCreator.instanceFor(proxyClass);
		setHandler(proxyInstance, handler);

		logger.debug("a proxy for {} is created as {}", type, proxyClass);

		return type.cast(proxyInstance);
	}

	public boolean isProxy(Object o) {
		return o != null && ProxyObject.class.isAssignableFrom(o.getClass());
	}
	
	private <T> void setHandler(Object proxyInstance, final MethodInvocation<? super T> handler) {
		ProxyObject proxyObject = (ProxyObject) proxyInstance;

		proxyObject.setHandler(new MethodHandler() {
			public Object invoke(final Object self, final Method thisMethod, final Method proceed, Object[] args)
				throws Throwable {

				return handler.intercept(self, thisMethod, args, new SuperMethod() {
					public Object invoke(Object proxy, Object[] arguments) {
						try {
							return proceed.invoke(proxy, arguments);
						} catch (Throwable throwable) {
							throw new ProxyInvocationException(throwable);
						}
					}
				});
			}
		});
	}
}