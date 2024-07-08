/* 
 * Copyright 2016, Emanuel Rabina (http://www.ultraq.net.nz/)
 * 
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

plugins {
	`java-library`
	jacoco
	`maven-publish`
	id("org.jetbrains.kotlin.jvm") version "1.9.23"
}

description = "A simplified API for working with Thymeleaf expressions"
version = "3.3.0"
group = "nz.net.ultraq.thymeleaf"
//year = "2016"

val thymeleafVersion = "3.1.2.RELEASE"

repositories {
	mavenCentral()
	maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
	implementation("org.thymeleaf:thymeleaf:${thymeleafVersion}")

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.3")
	testRuntimeOnly("org.slf4j:slf4j-simple:2.0.9")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

java {
	withJavadocJar()
	withSourcesJar()
}

tasks.kotlinSourcesJar {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
	useJUnitPlatform()
}

tasks.jar {
	manifest {
		attributes["Automatic-Module-Name"] = "nz.net.ultraq.thymeleaf.expressionprocessor"
	}
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
		html.required = true
	}
}

kotlin {
	jvmToolchain(8)
}