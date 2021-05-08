import testing.AfterSuite;
import testing.BeforeSuite;
import testing.Test;

public class TestClass {

	@Test(priority = 1)
	private void shouldTestSomething1 () {
		System.out.println("shouldTestSomething1 is run");
	}

	@Test(priority = 6)
	private void shouldTestSomething3 () {
		System.out.println("shouldTestSomething3 is run");
	}

	@BeforeSuite
	private void shouldStartBeforeAll() {
		System.out.println("beforeSuiteMethod is run");
	}

	@AfterSuite
	private void shouldStartAfterAll() {
		System.out.println("afterSuiteMethod is run");
	}

	@Test(priority = 2)
	private void shouldTestSomething2 () {
		System.out.println("shouldTestSomething2 is run");

	}
}
