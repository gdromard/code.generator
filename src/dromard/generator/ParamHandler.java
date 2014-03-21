package dromard.generator;

import java.util.Map;

public abstract class ParamHandler<T> {
	public abstract void handle(T object, Map<String, Object> params);
}
