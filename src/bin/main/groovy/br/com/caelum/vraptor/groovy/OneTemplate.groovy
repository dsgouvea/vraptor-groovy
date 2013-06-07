package br.com.caelum.vraptor.groovy

import java.io.IOException;
import java.io.Reader;

import org.codehaus.groovy.runtime.InvokerHelper;

import groovy.text.SimpleTemplateEngine.SimpleTemplate;
import groovy.text.Template;

class OneTemplate implements Template {

	protected Script script

	OneTemplate() {
	}

	public Writable make() {
		return make(null)
	}

	public Writable make(final Map map) {
		return new Writable() {
			public Writer writeTo(Writer writer) {
				Binding binding;
				if (map == null)
					binding = new Binding();
				else
					binding = new Binding(map);
				Script scriptObject = InvokerHelper.createScript(script.getClass(), binding);
				PrintWriter pw = new PrintWriter(writer);
				scriptObject.setProperty("out", pw);
				scriptObject.run();
				pw.flush();
				return writer;
			}

			public String toString() {
				StringWriter sw = new StringWriter();
				writeTo(sw);
				return sw.toString();
			}
		};
	}

	protected String parse(Reader reader) throws IOException {
		if (!reader.markSupported()) {
			reader = new BufferedReader(reader);
		}
		StringWriter sw = new StringWriter();
		startScript(sw);
		int c;
		while ((c = reader.read()) != -1) {
			if (c == '<') {
				reader.mark(1);
				c = reader.read();
				if (c != '%') {
					sw.write('<');
					reader.reset();
				} else {
					reader.mark(1);
					c = reader.read();
					if (c == '=') {
						groovyExpression(reader, sw);
					} else {
						reader.reset();
						groovySection(reader, sw);
					}
				}
				continue;
			}
			if (c == '\$') {
				reader.mark(1);
				c = reader.read();
				if (c != '{') {
					sw.write('\$');
					reader.reset();
				} else {
					reader.mark(1);
					sw.write("\${");
					processGSstring(reader, sw);
				}
				continue;
			}
			if (c == '\"') {
				sw.write('\\');
			}
			
			if (c == '\n' || c == '\r') {
				if (c == '\r') {
					reader.mark(1);
					c = reader.read();
					if (c != '\n') {
						reader.reset();
					}
				}
				sw.write("\n");
				continue;
			}
			sw.write(c);
		}
		endScript(sw);
		return sw.toString();
	}

	private void startScript(StringWriter sw) {
		sw.write("out.print(\"\"\"");
	}

	private void endScript(StringWriter sw) {
		sw.write("\"\"\");\n");
		sw.write("\n/* Generated by OneTemplateEngine */");
	}

	private void processGSstring(Reader reader, StringWriter sw) throws IOException {
		int c;
		while ((c = reader.read()) != -1) {
			if (c != '\n' && c != '\r') {
				sw.write(c);
			}
			if (c == '}') {
				break;
			}
		}
	}

	private void groovyExpression(Reader reader, StringWriter sw) throws IOException {
		sw.write("\${");
		int c;
		while ((c = reader.read()) != -1) {
			if (c == '%') {
				c = reader.read();
				if (c != '>') {
					sw.write('%');
				} else {
					break;
				}
			}
			if (c != '\n' && c != '\r') {
				sw.write(c);
			}
		}
		sw.write("}");
	}

	private void groovySection(Reader reader, StringWriter sw) throws IOException {
		sw.write("\"\"\");");
		int c;
		while ((c = reader.read()) != -1) {
			if (c == '%') {
				c = reader.read();
				if (c != '>') {
					sw.write('%');
				} else {
					break;
				}
			}
			sw.write(c);
		}
		sw.write(";\nout.print(\"\"\"");
	}

}