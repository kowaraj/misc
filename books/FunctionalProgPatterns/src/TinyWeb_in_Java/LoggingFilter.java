/***
 * Excerpted from "Functional Programming Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/mbfpp for more book information.
***/
package tinyweb;

//import tinyweb.Filter;
//import tinyweb.HttpRequest;

public class LoggingFilter implements Filter {

	@Override
	public HttpRequest doFilter(HttpRequest request) {
		System.out.println("In Logging Filter - request for path: "
		    + request.getPath());
		return request;
	}

}
