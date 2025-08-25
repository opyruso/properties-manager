#!/usr/bin/env bash
# Build all project modules in dependency order
set -euo pipefail

CONFIG_ENV="${1:-dev}"

mvn -q -f propertiesmanager-toolbox-file-lib/pom.xml install
mvn -q -f propertiesmanager-database-lib/pom.xml install
mvn -q -f propertiesmanager-api-lib/pom.xml install
mvn -q -f propertiesmanager-security-lib/pom.xml install
mvn -q -f propertiesmanager-lib/pom.xml install
mvn -q -f connector-java/pom.xml install
mvn -q -f propertiesmanager-api/pom.xml install

npm install --prefix propertiesmanager-ui
npm --prefix propertiesmanager-ui run build:${CONFIG_ENV}

DIST_DIR="dist"
rm -rf "${DIST_DIR}"
mkdir -p "${DIST_DIR}/propertiesmanager-ui"
mkdir -p "${DIST_DIR}/propertiesmanager-api"
mkdir -p "${DIST_DIR}/connector"

cp -r propertiesmanager-ui/build/* "${DIST_DIR}/propertiesmanager-ui/"

API_JAR=$(ls propertiesmanager-api/target/*.jar | head -n1)
cp "${API_JAR}" "${DIST_DIR}/propertiesmanager-api/"

CONNECTOR_JAR=$(ls connector-java/target/*.jar | head -n1)
cp "${CONNECTOR_JAR}" "${DIST_DIR}/connector/"

ZIP_NAME="propertiesmanager.zip"
(
  cd "${DIST_DIR}"
  zip -qr "../${ZIP_NAME}" .
)

mkdir -p propertiesmanager-ui/download
cp "${ZIP_NAME}" propertiesmanager-ui/download/propertiesmanager_latest.zip
