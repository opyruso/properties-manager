#!/usr/bin/env bash
# Build all project modules in dependency order
set -euo pipefail

mvn -q -f propertiesmanager-toolbox-file-lib/pom.xml install
mvn -q -f propertiesmanager-database-lib/pom.xml install
mvn -q -f propertiesmanager-api-lib/pom.xml install
mvn -q -f propertiesmanager-security-lib/pom.xml install
mvn -q -f propertiesmanager-lib/pom.xml install
mvn -q -f connector-java/pom.xml install
mvn -q -f propertiesmanager-api/pom.xml install

# Uncomment to build the UI with npm
# npm install --prefix propertiesmanager-ui
