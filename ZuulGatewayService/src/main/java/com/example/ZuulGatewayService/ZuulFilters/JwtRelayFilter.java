package com.example.ZuulGatewayService.ZuulFilters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

//This filter helps in putting the jwt token in the headers of the request before routing to the backend micro-services 
@Component
public class JwtRelayFilter extends ZuulFilter {

	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public boolean shouldFilter() {

		RequestContext reqctx = RequestContext.getCurrentContext();

		HttpServletRequest request = reqctx.getRequest();

		//Avoiding filter to run if the request is for UI resources
		return !request.getServletPath().startsWith("/ui");
	}

	@Override
	public Object run() throws ZuulException {
		try {
			logger.info("Running JwtRelayFilter");

			// This context holds all nececssary information like request,response , state
			// info
			RequestContext reqctx = RequestContext.getCurrentContext();

			// get the current httpservletrequest
			// This is the original request zuul received for forwarding
			HttpServletRequest request = reqctx.getRequest();

			logger.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

			// Get the session for the request
			HttpSession session = request.getSession();

			// Get the JWT token for the current session
			String JWT = session.getAttribute("JWT").toString();
			logger.info(String.format("Relaying JWT Token %s", JWT));

			// insert the jwt token into the header of request that the zuul is going to
			// preform
			reqctx.addZuulRequestHeader("JWT", JWT);
		} 
		catch (Exception ex) {
			logger.error(ex);
		}
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
