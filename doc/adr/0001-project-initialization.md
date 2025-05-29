# 1. Project Initialization

Date: 2025-05-29

## Status

Accepted

## Context

Project is Demo of Java skills for Java Candidate.

Major Requirements:
1. Architecture and Implementation
2. Testing Approach
3. Continues Delivery and DevOps
4. Documentation and Code Quality
5. Personal Reflection

Bonus Points:
- test coverage
- using togglz for gradula rollout, A/B testing, or rollback
- clean code
- open-source libraries in use
- mocking technique demo
- edge cases
- documentation and onboarding

## Decision

1. Keep it simple, focus on major points:
 - keep it simplified, without deep modifications in configurations, failure protections, monitoring etc
 - demonstrate the concept, not the full solution
 - document descision via ADRs

2. Architecture and Implementation - covered by SpringBoot framework and project structure
 - Use Gradle Kotlin for builds configuration
 - Dockerize the solution
 - Use Docker-Compose to run all locally
 - use https://start.spring.io - as a bootstrap for the project: Gradle Kotlin, Java, Spring Boot v3.5, Java v24 (Amazon Corrito)
 - Use OpenAPI generator tool for converting API definitions to Client code
 - Use Togglz (https://togglz.org) as a feature toggle solution (alternatives: Firebase Runtime Configuration, many 3rd party solutions available)
 - Use PostgreSQL as a permanent storage for feature toggles, a/b testing and etc.
 
3. Testing:
 - JUnit5 - unit testing, Isolation, Mockito
 - JUnit5 - e2e testing with help of TestContainer
 - Performance testings - prepare Stubs for Isolated Performance Testing (potentially K6 tool usage)
  
4. Continues Delivery:
 - GitHub Actions (cons: hard to run locally, ACT tool is not the easiest)
 - GitHub Docker Images Repo
 - GitHub Releases for Source Code, Documentation and Binaries
 - Versioning Strategy (Semver, git-cz, semnatic git commits)
 - (Optional) Native releases. Enable Native images building with Quarkus and GraalVM
 
5. Documentation and Code Quality:
 - connect static analysis tools
 - connect publishig of the JavaDoc to github pages
 - use CodeRabitt.ai for automatic code reviews
 - connect automatic code formatting, Spotless
 - enable DIRENV, for forcing project configuration
 - enable Git Hooks, for automatic executions on commit/push
 - enable Swagger/OpenAPI definitions for project

## Consequences

What becomes easier or more difficult to do and any risks introduced by the change that will need to be mitigated:

- [ ] extended scope, will be not enough time for everything (reduce risks by: deliver in chunks)
- [ ] solve one task in a time, keep one commit per feature
- 
