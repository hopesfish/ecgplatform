package com.ainia.ecgApi.core.web;

import java.util.Iterator;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.ainia.ecgApi.core.crud.Condition;
import com.ainia.ecgApi.core.crud.Query;

/**
 * <p>Dynamic Query Resolver</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * QueryArgumentResolver.java
 * @author pq
 * @createdDate 2013-6-23
 * @version
 */
public class QueryArgumentResolver implements WebArgumentResolver {
	
	public static final String CONDITION_SPLIT = ":";

	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {
		
		if (methodParameter.getParameterType() == Query.class) {
			Query query = new Query();
			for (Iterator<String> iter = webRequest.getParameterNames();iter.hasNext();) {
				String param = iter.next();
				if (param.indexOf(CONDITION_SPLIT) != -1) {
					String[] values = param.split(CONDITION_SPLIT);
					query.addCondition(new Condition(values[0] , Condition.Type.valueOf(values[1]),
											webRequest.getParameter(param)));
				}
				else {
					query.addCondition(Condition.eq(param , webRequest.getParameter(param)));
				}
			}
			return query;
		}
		else {
			return WebArgumentResolver.UNRESOLVED;
		}
	}

}
