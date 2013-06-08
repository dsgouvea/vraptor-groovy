package br.com.caelum.vraptor.groovy.builder

import groovy.xml.MarkupBuilder

import javax.servlet.http.HttpServletResponse

class XMLBuilder extends MarkupBuilder {
	
	Writer writer
	
	private XMLBuilder(Writer writer) {
		super(writer)
		
		this.writer = writer
	}
	
	static XMLBuilder create() {
		create "UTF-8"
	}
	
	static XMLBuilder create(String encoding) {
		def builder = new XMLBuilder(new StringWriter())
		builder.mkp.pi(xml:[version:"1.0", encoding:encoding]) 
		return builder
	}

	static XMLBuilder create(HttpServletResponse response) {
		create response, "UTF-8"
	}
	
	static XMLBuilder create(HttpServletResponse response, String encoding) {
		def builder = new XMLBuilder(response.getWriter())
		builder.mkp.pi(xml:[version:"1.0", encoding:encoding])
		return builder
	}

}