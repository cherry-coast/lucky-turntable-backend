package com.cherry.lucky.domain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName BaseController
 * @Description
 * @createTime 2023年03月24日 18:15:23
 */
public class BaseController {

	protected HttpServletRequest request;

	protected HttpServletResponse response;

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Autowired
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getCurrentToken() {
		return request.getHeader("token");
	}
}
