# 12. Establish Quality Standards

Date: 2025-06-01

## Status

Accepted

## Context

Quality Assurance:

- [ ] Code coverage (JaCoCo)
- [ ] Static code analysis (Checkstyle, PMD)
- [ ] Code quality (SonarQube)
- [ ] Code style/formatting (Spotless)
- [ ] Git leaks (Gitleaks)

Best practices:

- [ ] Human codereview process - the best way to share the knowledge;

## Decision

- Enable the latest JaCoCo version (0.8.13)
- Enable gradle jacoco plugin
- Configure JaCoCo to exclude the unit tests classes from code coverage
- Enable PMD (Latest Version: 7.14.0 (30-May-2025))
- Enable gradle pmd plugin
- Enable Checkstyle (Release 10.25.0)
- Enable gradle checkstyle plugin
- Enable gradle spotless plugin
- Enable gitleaks via githooks and github actions

## Consequences

- Gradle Plugins take all hard work of integration the tools into the build process simple and transparent (instrumentation, configuration, reporting, build graph, etc)
- Require parallel execution of all quality assurance tasks
- JaCoCo may need data merge to calculate the total code coverage of different tests, modules
- JaCoCo excludes are important, for keeping focus on important parts of the code.
- all tools should be configured to generate HTML reports that can be a part of release artifacts

## References

- https://www.eclemma.org/jacoco/ - JaCoCo is a free, open source code coverage library for Java. It is used to measure the percentage of code that is executed when running tests. It is based on the Java Virtual Machine (JVM) and can be used with any Java application.
- https://docs.gradle.org/current/userguide/jacoco_plugin.html
- https://pmd.github.io/ - PMD is an extensible multilanguage static code analyzer. It finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, and so forth. It’s mainly concerned with Java and Apex, but supports 16 other languages. It comes with 400+ built-in rules. It can be extended with custom rules. It uses JavaCC and Antlr to parse source files into abstract syntax trees (AST) and runs rules against them to find violations. Rules can be written in Java or using a XPath query.
- https://docs.gradle.org/current/userguide/pmd_plugin.html#header
- https://checkstyle.org/ - Checkstyle is a development tool to help programmers write Java code that adheres to a coding standard. It automates the process of checking Java code to spare humans of this boring (but important) task. This makes it ideal for projects that want to enforce a coding standard. Checkstyle is highly configurable and can be made to support almost any coding standard.
- https://docs.gradle.org/current/userguide/checkstyle_plugin.html#header
- https://github.com/diffplug/spotless - Spotless can format <antlr | c | c# | c++ | css | flow | graphql | groovy | html | java | javascript | json | jsx | kotlin | less | license headers | markdown | objective-c | protobuf | python | scala | scss | shell | sql | typeScript | vue | yaml | anything> using <gradle | maven | sbt | anything>.
- https://gitleaks.io/ - Gitleaks is an open source (MIT licensed) secret scanner for git repositories, files, directories, and stdin. With over 20 million docker downloads, 19k GitHub stars, 14 million GitHub downloads, thousands of weekly clones, and over 850k homebrew installs, gitleaks is the most trusted open source secret scanner among security professionals, enterprises, and developers.
- https://github.com/SonarSource/sonar-scanner-gradle - Sonar's Clean Code solutions help developers deliver high-quality, efficient code standards that benefit the entire team or organization.
- https://docs.sonarsource.com/sonarqube-community-build/ , https://docs.sonarsource.com/sonarqube-community-build/devops-platform-integration/github-integration/introduction/, the only one commercial solution is SonarQube (https://www.sonarsource.com/plans-and-pricing/, $32 per team member)

---

[Prev](./0011-green-and-blue-deployment-strategy.md) | [Next]()