plugins {
    id("java-gradle-plugin")
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("ru.otus.logging.proxy.plugin") {
            id = "ru.otus.logging.proxy.plugin"
            implementationClass = "ru.otus.logging.proxy.plugin.LoggingProxyPlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())

    implementation("org.ow2.asm:asm-commons:9.6")
}