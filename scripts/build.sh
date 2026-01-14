#!/bin/bash
# ========================================================
# Build Script - Compagnie Aerienne
# ========================================================
# Configure paths below, then run: ./build.sh
# ========================================================

# === CONFIGURATION (Edit these paths) ===
PROJECT_NAME="projet"
TOMCAT_HOME="/opt/tomcat"
JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"

# === Build Process ===
cd "$(dirname "$0")/.."

echo ""
echo "[1/5] Cleaning previous build..."
rm -rf build
rm -f ${PROJECT_NAME}.war

echo "[2/5] Creating directories..."
mkdir -p build/classes
mkdir -p build/web/WEB-INF/classes
mkdir -p build/web/WEB-INF/lib
mkdir -p build/web/jsp

echo "[3/5] Compiling Java sources..."
find src -name "*.java" > build/sources.txt
"$JAVA_HOME/bin/javac" -encoding UTF-8 -d build/classes -cp "lib/*:$TOMCAT_HOME/lib/*" @build/sources.txt

if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed!"
    exit 1
fi

echo "[4/5] Packaging WAR..."
cp -r build/classes/* build/web/WEB-INF/classes/
cp lib/*.jar build/web/WEB-INF/lib/
cp WebContent/WEB-INF/web.xml build/web/WEB-INF/
cp -r WebContent/jsp/* build/web/jsp/
[ -f WebContent/*.html ] && cp WebContent/*.html build/web/ 2>/dev/null || true

cd build/web
"$JAVA_HOME/bin/jar" -cvf ../../${PROJECT_NAME}.war * > /dev/null
cd ../..

echo "[5/5] Deploying to Tomcat..."
rm -rf "$TOMCAT_HOME/webapps/$PROJECT_NAME"
rm -f "$TOMCAT_HOME/webapps/${PROJECT_NAME}.war"
cp ${PROJECT_NAME}.war "$TOMCAT_HOME/webapps/"

echo ""
echo "========================================"
echo "  BUILD SUCCESSFUL"
echo "========================================"
echo "WAR: ${PROJECT_NAME}.war"
echo "Deployed to: $TOMCAT_HOME/webapps/"
echo ""
echo "Start Tomcat: $TOMCAT_HOME/bin/startup.sh"
echo "Access: http://localhost:8080/$PROJECT_NAME/home"
echo ""
