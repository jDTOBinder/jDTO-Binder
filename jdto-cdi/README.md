jDTO Binder :: CDI Integration Module
=====================================


This module provides integration with JSR 330 Dependency injection for Java, by including this module as a Maven dependency into your project, you will be able to use jDTO Binder in CDI-enabled application containers, example usage:

```java

class MyServiceImpl {

	@Inject
	private DTOBinder binder;

	... //service methods
}

```
