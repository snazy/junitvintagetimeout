JUnit test-engine implementation that wraps the standard JUnit vintage-engine and adds support for timeouts per
test-class.

Apply this engine instead of the junit-vintage-engine.

Configurables:
* The config parameter `junit.vintage.execution.timeout.seconds` (also configurable as a system property) defines the
  timeout in seconds. A timeout of `<=0` means no timeout.

The (current) implementation invokes the vintage-engine via a separate thread (named `vintage-timeout-*`). If the
test runs too long (timeout), that thread is interrupted. This is a "best effort" approach that does not (and cannot)
guarantee that all resources occupied by a test will be freed upon a timeout. So the probably best approach to run
tests with a timeout setting is to never reuse a test-worker-JVM - in other words: spawn a new JVM for each test class.
