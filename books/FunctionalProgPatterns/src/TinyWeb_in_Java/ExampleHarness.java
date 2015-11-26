/***
 * Excerpted from "Functional Programming Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/mbfpp for more book information.
***/
package tinyweb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import tinyweb.StrategyView;
//import tinyweb.Controller;
//import tinyweb.Filter;
//import tinyweb.HttpRequest;
//import tinyweb.HttpResponse;
//import tinyweb.TinyWeb;

public class ExampleHarness {
	
	public static void main(String[] args){
		
		TinyWeb tinyWeb = new TinyWeb(makeRoutes(), makeFilters());
		
		HttpRequest testRequest = HttpRequest.Builder.newBuilder()
				.path("greeting/")
				.body("Vasya,Petya,Mike,Joe,John,Steve")
				.addHeader("X-Example", "exampleHeader")
				.build();
		
		HttpResponse testResponse = tinyWeb.handleRequest(testRequest);
		
		System.out.println("responseCode: " + testResponse.getResponseCode());
		System.out.println("responseBody: ");
		System.out.println(testResponse.getBody());
	}
	
	private static Map<String, Controller> makeRoutes(){
		GreetingRenderingStrategy viewRenderer = new GreetingRenderingStrategy();
		StrategyView greetingView = new StrategyView(viewRenderer);
		GreetingController greetingController = new GreetingController(greetingView);
		
		Map<String, Controller> controllers = new HashMap<String, Controller>();
		controllers.put("greeting/", greetingController);
		return Collections.unmodifiableMap(controllers);
	}
	
	private static List<Filter> makeFilters(){
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new LoggingFilter());
		return filters;
	}
	
}
