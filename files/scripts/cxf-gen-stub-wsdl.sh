#!/bin/bash

export JAVA_HOME="/Users/inomera/Library/Java/JavaVirtualMachines/corretto-17.0.9/Contents/Home"
# To use this script, initialize the following variables
CXF_PATH="/Users/inomera/Documents/Documents/workspace/tools/apache-cxf-4.0.3"
WS_PROJECT_HOME="/Users/inomera/Documents/workspace/scm/adapters/ws-country-client"


# Do not change package name if you do not need to.
PACKAGE_NAME="com.inomera.country.mapping.generated"
WSDL_PATH="${WS_PROJECT_HOME}/src/main/resources/CountryInfoService.WSDL"
JAVA_SRC_DIR="${WS_PROJECT_HOME}/src/main/java"

"${CXF_PATH}"/bin/wsdl2java -autoNameResolution -d "${JAVA_SRC_DIR}" -p "${PACKAGE_NAME}"  "${WSDL_PATH}"

echo "Finished..."
exit 0

