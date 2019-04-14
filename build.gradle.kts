import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "tech.loucianus.im"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        val kotlinVersion = "1.3.20"
        val springBootVersion = "2.1.2.RELEASE"

        val springGroup = "org.springframework.boot"

        // kotlin
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath(kotlin("allopen", kotlinVersion))
        classpath(kotlin("noarg", kotlinVersion))

        // spring-boot
        classpath("$springGroup:spring-boot-gradle-plugin:$springBootVersion")
    }
}

plugins {
    `build-scan`

    kotlin("jvm") version("1.3.21")
}

apply {

    // kotlin plugins
    plugin("kotlin")
    plugin("kotlin-spring")

    // spring plugins
    plugin("org.springframework.boot")
    plugin("io.spring.dependency-management")
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}

repositories {
    jcenter()
}

dependencies {

    compile("org.springframework.boot:spring-boot-starter-web")

    compile("org.springframework.boot:spring-boot-starter-validation")

    compile("org.springframework.boot:spring-boot-starter-thymeleaf")

    compile ("org.springframework.boot:spring-boot-starter-websocket:2.1.3.RELEASE")

    compile(kotlin("stdlib-jdk8"))

    compile(kotlin("reflect"))

    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    
    compile ("com.google.code.gson:gson:2.8.5")

    compile ("com.alibaba:fastjson:1.2.44")

    compile("org.apache.shiro:shiro-spring:1.4.0")

    compile("org.apache.shiro:shiro-ehcache:1.4.0")

    compile("com.auth0:java-jwt:3.5.0")

    compile("org.mariadb.jdbc:mariadb-java-client:2.1.2")

    compile("org.mybatis.spring.boot:mybatis-spring-boot-starter:1.3.2")

    compile("com.github.ulisesbocchio:jasypt-spring-boot-starter:2.1.0")

    compile("com.alibaba:druid-spring-boot-starter:1.1.9")
    
    compile("org.apache.commons:commons-lang3:3.8.1")

    compile("com.github.pagehelper:pagehelper-spring-boot-starter:1.2.10")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.mockito:mockito-core:2.8.8")
}

// The corresponding sourceSets should be updated
// Set the Java Dir
sourceSets["main"].java.srcDir("src/main/java")
// Set the Kotlin dir
sourceSets["main"].withConvention(KotlinSourceSet::class) {
    kotlin.srcDir("src/main/kotlin")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Wrapper> {
    gradleVersion = "5.1"
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}