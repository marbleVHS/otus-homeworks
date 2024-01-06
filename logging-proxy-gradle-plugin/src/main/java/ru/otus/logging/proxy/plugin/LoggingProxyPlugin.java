package ru.otus.logging.proxy.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.compile.JavaCompile;

public class LoggingProxyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        var javaCompileTaskProvider = project.getTasks().named("compileJava", JavaCompile.class);
        var createLoggingProxiesTaskProvider = project
                .getTasks()
                .register(
                        "createLoggingProxies",
                        CreateLoggingProxiesTask.class,
                        task -> {
                            task.dependsOn(javaCompileTaskProvider);
                            task.getJavaCompileTaskProvider().set(javaCompileTaskProvider);
                        }
                );
        project.getTasks().named("classes").configure(it -> {
            it.dependsOn(createLoggingProxiesTaskProvider);
        });
    }
}
