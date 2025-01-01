#!/bin/bash

# To use this script, initialize the following variables
AXIS2_PATH="/Users/inomera/Documents/axis2-1.8.2"
WS_PROJECT_HOME="/Users/inomera/Documents/workspace/adapters/ws-country-client"


# Do not change package name if you do not need to.
PACKAGE_NAME="com.inomera.country.mapping.axis2.generated"
WSDL_PATH="${WS_PROJECT_HOME}/src/main/resources/CountryInfoService.WSDL"
JAVA_SRC_DIR="${WS_PROJECT_HOME}/gen_src/main/java"

sh "${AXIS2_PATH}"/bin/wsdl2java.sh -uri "${WSDL_PATH}" -p "${PACKAGE_NAME}" -d adb -s "${JAVA_SRC_DIR}"

echo "Finished..."
exit 0

