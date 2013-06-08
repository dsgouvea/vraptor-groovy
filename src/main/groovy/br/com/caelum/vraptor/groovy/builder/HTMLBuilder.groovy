package br.com.caelum.vraptor.groovy.builder

import groovy.xml.MarkupBuilder

import javax.servlet.http.HttpServletResponse

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

class HTMLBuilder extends MarkupBuilder {
	
	Writer writer
	final def header = []
	
	private HTMLBuilder(Writer writer) {
		super(writer)
		
		this.writer = writer
		doubleQuotes = true
		expandEmptyElements = true
		omitEmptyAttributes = false
		omitNullAttributes = false
	}
	
	HTMLBuilder addHeader(def head) {
		header.add head
		return this
	}
	
	HTMLBuilder comment(def text) {
		mkp.comment text
		return this
	}
	
	static HTMLBuilder create() {
		return new HTMLBuilder(new StringWriter())
	}

	static HTMLBuilder create(MarkupBuilder builder) {
		return new HTMLBuilder(builder.out.out)
	}

	static HTMLBuilder create(HttpServletResponse response) {
		return new HTMLBuilder(response.getWriter())
	}

}