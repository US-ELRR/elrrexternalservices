package com.deloitte.elrr;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;



@Component
public class SanatizingFilter implements Filter {
	

	private boolean invalidParam;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		WrappedHttp httpRequest;
		invalidParam = false;
		
		StringBuilder body = new StringBuilder();
		for(String line : request.getReader().lines().toList()) {
			if(InputSanatizer.isValidInput(line)) {
				body.append(line);
				body.append('\n');
				
			}
			else {
				//need to log bad request. Might be best to continue processing and report all bad lines. / complete body
				httpResponse.sendError(HttpStatus.BAD_REQUEST.value(), "Illegal line in request body: " + line);
			}
		}
		if(httpResponse.isCommitted())
			return;
		
		
		
		httpRequest = new WrappedHttp((HttpServletRequest) request, body.toString());
		httpRequest.getParameterMap(); //might help to cache parameters for future filter chain

		//below we check each parameter string for any invalid values
		httpRequest.getParameterNames().asIterator().forEachRemaining((param) -> { 
			String paramVal = request.getParameter(param);
			if(!InputSanatizer.isValidInput(paramVal)) {
				invalidParam = true;
				}
			});
		
		if(invalidParam) {
			httpResponse.sendError(HttpStatus.BAD_REQUEST.value(), "Illegal Parameter Value");
			return;
		}

		chain.doFilter(httpRequest, response);

	}

}


