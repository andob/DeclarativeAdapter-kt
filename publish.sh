set -o allexport

echo "Publishing..."

./gradlew :declarative_adapter_kt:publishToMavenLocal
./gradlew :sticky_headers:publishToMavenLocal
./gradlew :viewbinding_compat:publishToMavenLocal
