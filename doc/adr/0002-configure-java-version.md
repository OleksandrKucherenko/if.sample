# 2. Configure Java Version

Date: 2025-05-29

## Status

Accepted

## Context

Fix the development version of JAVA for project. it should be Corretto v24.x

## Decision

1. use DIRENV as enforcer of tools setup
2. use JENV for java version setup
3. use e-bash dependency script for validating the setup

```bash
# install e-bash scripts
curl -sSL https://git.new/e-bash | bash -s --

# install DIRENV, https://formulae.brew.sh/formula/direnv#default
brew install direnv

# install CORRETTO, https://formulae.brew.sh/cask/corretto
brew install corretto

# install JENV, https://formulae.brew.sh/formula/jenv#default
brew install jenv

# list installed Java Runtimes
/usr/libexec/java_home -V

# Add Corretto 24 into JENV list of versions
jenv add /Library/Java/JavaVirtualMachines/amazon-corretto-24.jdk/Contents/Home
```

Major Project dependencies: GIT, DOCKER, JAVA, DIRENV, JENV.

`.envrc` file guaranty proper setup.

## Consequences

- pre-configuration is required, and developer should know at least about DIRENV tool and it SHELL integration
