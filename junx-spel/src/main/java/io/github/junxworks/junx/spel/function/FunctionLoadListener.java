/*
 ***************************************************************************************
 * 
 * @Title:  FunctionLoadListener.java   
 * @Package io.github.junxworks.junx.spel.function   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 10:23:27   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.spel.function;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.google.common.collect.Maps;

import io.github.junxworks.junx.core.util.ClockUtils;
import io.github.junxworks.junx.core.util.ClockUtils.Clock;
import io.github.junxworks.junx.core.util.ObjectUtils;
import io.github.junxworks.junx.spel.function.anno.Func;
import io.github.junxworks.junx.spel.function.anno.FuncMethod;

/**
 * The listener interface for receiving functionLoad events.
 * The class that is interested in processing a functionLoad
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addFunctionLoadListener<code> method. When
 * the functionLoad event occurs, that object's appropriate
 * method is invoked.
 *
 * @see FunctionLoadEvent
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class FunctionLoadListener implements ApplicationListener<ContextRefreshedEvent> {
	private Logger log = LoggerFactory.getLogger(FunctionLoadListener.class);
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("Loading functions in classpath.");
		Map<String, MethodDescriptor> methods = Maps.newHashMap();
		Clock clock = ClockUtils.createClock();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Arrays.asList((Object[]) ObjectUtils.mirror().on(ObjectUtils.mirror().on(cl).get().field("classes")).get().field("elementData")).stream().forEach(_c -> {
			try {
				Class<?> c = (Class<?>) _c;
				Func fc = c.getAnnotation(Func.class);
				if (fc == null)
					return;
				ObjectUtils.mirror().on(c).reflectAll().methods().forEach(m -> {
					try {
						FuncMethod fm = m.getAnnotation(FuncMethod.class);
						if (fm != null) {
							MethodDescriptor md = new MethodDescriptor();
							md.setName(fm.funcName());
							md.setDescription(fm.funcDesc());
							md.setMethod(m);
							md.setDefineClass(c);
							md.setGroups(md.getGroups());
							if (!methods.containsKey(md.getName())) {
								methods.put(md.getName(), md);
							} else {
								log.error("Duplicated function is not allowed.", new DuplicateFunctionException("%s and %s", methods.get(md.getName()).toString(), md.toString()));
							}
						}
					} catch (Throwable e) {
					}
				});
			} catch (Exception e) {
			}
		});
		log.info("Load functions finished.Find {} functions,cost {} millis.", methods.size(), clock.countMillis());
		final StringBuilder sb = new StringBuilder(System.lineSeparator());
		methods.values().stream().map(m -> {
			return m.toString() + System.lineSeparator();
		}).forEach(sb::append);
		log.info("Loaded function list:{}", sb.toString());
		FunctionRepository.putAllMethods(methods);
	}

}
