plugins {
	java

	alias(libs.plugins.spring.boot) apply false
	alias(libs.plugins.spring.dependency.management) apply false
	alias(libs.plugins.native) apply false

	alias(libs.plugins.task.tree)
}