JUnit test-engine implementation that wraps the standard JUnit vintage-engine and adds support for timeouts per
test-class.

Apply this engine instead of the junit-vintage-engine.

# Examples

## Gradle (Kotlin)

```(kotlin)
dependencies {
    testImplementation("org.junit.platform:junit-platform-launcher:1.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")

    testRuntimeOnly("org.caffinitas.junitvintagetimeout:junitvintagetimeout:0.1.1")
}

tasks.withType<Test>().configureEach {
    systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")
    useJUnitPlatform() {
        includeEngines("timeout-junit-vintage")
    }
}

```

## Maven

TODO (PR welcome)

# Configurables
* The config parameter `junit.vintage.execution.timeout.seconds` (also configurable as a system property) defines the
  timeout in seconds. A timeout of `<=0` means no timeout.

# Note

The (current) implementation invokes the vintage-engine via a separate thread (named `vintage-timeout-*`). If the
test runs too long (timeout), that thread is interrupted. This is a "best effort" approach that does not (and cannot)
guarantee that all resources occupied by a test will be freed upon a timeout. So the probably best approach to run
tests with a timeout setting is to never reuse a test-worker-JVM - in other words: spawn a new JVM for each test class.
