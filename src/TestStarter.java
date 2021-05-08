import testing.AfterSuite;
import testing.BeforeSuite;
import testing.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestStarter {

	private TestStarter() {
	}

	public static void start(Class testClass) {
		Object obj = initObject(testClass);
		List<Method> testMethods = new ArrayList<>();

		if (findTestMethods(testClass, BeforeSuite.class).size() == 1 && findTestMethods(testClass, AfterSuite.class).size() == 1) {
			executeMethod(findTestMethods(testClass, BeforeSuite.class).get(0), obj);

			testMethods = findTestMethods(testClass, Test.class);
			if (testMethods.isEmpty()) {
				System.out.printf("%s has no any test methods", testClass.getName());
				return;
			} else {
				testMethods.sort(new Comparator<Method>() {
					@Override
					public int compare(Method o1, Method o2) {
						return o1.getAnnotation(Test.class).priority() - o2.getAnnotation(Test.class).priority();
					}
				});
				for (Method method : testMethods) {
					executeMethod(method, obj);
				}
			}

			executeMethod(findTestMethods(testClass, AfterSuite.class).get(0), obj);
		} else {
			throw new RuntimeException("Class must have one annotation @BeforeSuite");
		}
	}

	private static void executeMethod(Method testMethod, Object obj, Object... args) {
		try {
			testMethod.setAccessible(true);
			testMethod.invoke(obj, args);
			testMethod.setAccessible(false);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private static List<Method> findTestMethods(Class testClass, Class<? extends Annotation> annotationClass) {
		List<Method> testMethods = new ArrayList<>();
		for (Method method : testClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(annotationClass)) {
				testMethods.add(method);
			}
		}
		return testMethods;
	}

	public static void start(String testClassName) {
		try {
			start(Class.forName(testClassName));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Object initObject(Class aClass) {
		try {
			return aClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException("RE", e);
		}
	}
}
