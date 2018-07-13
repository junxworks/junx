package io.github.junxworks.junx.test.spel.customize;

import io.github.junxworks.junx.spel.function.anno.Func;
import io.github.junxworks.junx.spel.function.anno.FuncMethod;
import io.github.junxworks.junx.spel.function.builtin.BuiltInFunctions;

@Func
public class MyFunc {

	public static final String FUNC_CUSTOM = "my";

	@FuncMethod(funcDesc = "自定义函数", funcGroup = { BuiltInFunctions.FUNC_GROUP_STRING }, funcName = FUNC_CUSTOM)
	public static String customizeFunc(String name) {
		System.out.println("Hello " + name);
		return name + "-";
	}
}
