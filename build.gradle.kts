/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.gradle.ext.*

plugins {
    `java-library`
    `maven-publish`
    id("de.marcphilipp.nexus-publish") version "0.4.0"
    signing
    id("org.jetbrains.gradle.plugin.idea-ext") version "0.7"
}

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "org.caffinitas.junitvintagetimeout"
version = "0.1.2-SNAPSHOT"
val readableName = "JUnit vintage-engine with timeouts"
description = "Adds a per-test timeout to the JUnit 5 vintage-engine"

val junitPlatformVersion by extra("1.6.2")
val junitJupiterVersion by extra("5.6.2")

dependencies {
    implementation("org.junit.platform:junit-platform-launcher:${junitPlatformVersion}")
    implementation("org.junit.jupiter:junit-jupiter-api:${junitJupiterVersion}")
    implementation("org.junit.vintage:junit-vintage-engine:${junitJupiterVersion}")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitJupiterVersion}")
}

publishing {
    publications {
        register<MavenPublication>("main") {
            from(components["java"])

            pom {
                name.set(readableName)
                description.set(project.description)
                inceptionYear.set("2018")
                url.set("https://github.com/snazy/junitvintage/")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("snazy")
                        name.set("Robert Stupp")
                        email.set("snazy@snazy.de")
                    }
                }
                scm {
                    connection.set("https://github.com/snazy/junitvintage.git")
                    developerConnection.set("https://github.com/snazy/junitvintage.git")
                    url.set("https://github.com/snazy/junitvintage/")
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["main"])
}

nexusPublishing {
    packageGroup.set("org.caffinitas")
    repositories {
        sonatype {
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        includeEngines("junit-jupiter")
    }
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
}

idea {
    module {
        isDownloadSources = true // this is the default BTW
        inheritOutputDirs = true
    }

    project {
        withGroovyBuilder {
            "settings" {
                val copyright: CopyrightConfiguration = getProperty("copyright") as CopyrightConfiguration
                val encodings: EncodingConfiguration = getProperty("encodings") as EncodingConfiguration
                val delegateActions: ActionDelegationConfig = getProperty("delegateActions") as ActionDelegationConfig

                delegateActions.testRunner = ActionDelegationConfig.TestRunner.CHOOSE_PER_TEST

                encodings.encoding = "UTF-8"
                encodings.properties.encoding = "UTF-8"

                copyright.useDefault = "Apache"
                copyright.profiles.create("Apache") {
                    notice = file("gradle/license.txt").readText()
                }
            }
        }
    }
}
