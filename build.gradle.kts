plugins {
    java
    eclipse
    idea
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("jacoco")
}

repositories {
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.26")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.4")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.5.4")
    implementation("org.javamoney:moneta:1.3")
    implementation("com.jsoniter:jsoniter:0.9.23")

    implementation(files("libs/gpsUtil.jar"))
    implementation(files("libs/RewardCentral.jar"))
    implementation(files("libs/TripPricer.jar"))

    implementation("org.locationtech.jts:jts-core:1.18.1")

    testImplementation("junit:junit:4.12")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.4")
}

jacoco {
    toolVersion = "0.8.4"
}

tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
        html.setDestination(file("${buildDir}/jacocoHtml"))
    }
}

tasks.test {
    finalizedBy("jacocoTestReport")
}

tasks.named("check") {
    dependsOn("jacocoTestCoverageVerification")
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal("0.5")
            }
        }
    }
}

tasks.named("bootJar", org.springframework.boot.gradle.tasks.bundling.BootJar::class) {
    archiveFileName.set("tourGuide-1.0.0.jar")
}
