set -o allexport

echo "Type Archiva username:"
read -r MAVEN_PUBLISH_USERNAME
echo "Type Archiva password:"
read -s -r MAVEN_PUBLISH_PASSWORD

echo "Publishing..."

./gradlew :declarative_adapter_kt:publish
./gradlew :sticky_headers:publish

set +o allexport
