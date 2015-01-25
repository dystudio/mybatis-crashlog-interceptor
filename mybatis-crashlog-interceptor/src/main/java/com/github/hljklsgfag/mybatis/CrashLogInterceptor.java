package com.github.hljklsgfag.mybatis;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }) })
public class CrashLogInterceptor implements Interceptor {
	private Log log = LogFactory.getLog(getClass());

	@Override
	public Object intercept(Invocation invocation) throws Throwable {				
		try {
			return invocation.proceed();	
		} catch (Throwable e) {
			MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
			Object argObj = invocation.getArgs()[1];		
			BoundSql boundSql = ms.getBoundSql(argObj);
			
			StringBuilder builder = new StringBuilder(ms.getId() + ":" + boundSql.getSql());
			boundSql.getParameterMappings().stream().forEach(param -> {
				Object value;
				if (argObj instanceof Map) {
					value = ((Map<?,?>)argObj).get(param.getProperty());
				} else {
					try {
						value = FieldUtils.readField(argObj, param.getProperty());
					} catch (Exception e1) {
						log.warn("couln't find field:"+param.getProperty());
						value = null;
					}
				}
				
				builder.append("[property=");
				builder.append(param.getProperty());
				builder.append(" value=");
				builder.append(value);
				builder.append("]");				
			});
				
			log.warn(builder.toString());
			
			throw e;
		}			
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {
		//TODO in future.
	}

}
 