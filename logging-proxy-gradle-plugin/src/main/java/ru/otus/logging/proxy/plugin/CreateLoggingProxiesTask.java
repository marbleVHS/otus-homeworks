package ru.otus.logging.proxy.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.JavaCompile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public abstract class CreateLoggingProxiesTask extends DefaultTask {

    @Input
    abstract Property<TaskProvider<JavaCompile>> getJavaCompileTaskProvider();

    @TaskAction
    public void performTask() {
        JavaCompile javaCompile = getJavaCompileTaskProvider().get().get();
        if (!javaCompile.getDidWork()) {
            setDidWork(false);
            return;
        }
        javaCompile.getDestinationDirectory().get().getAsFileTree().forEach(it -> {
            final InputStream is;
            try {
                is = new BufferedInputStream(new FileInputStream(it));
                var classTransformationResult = transform(is);
                if (classTransformationResult.classHasBeenTransformed()) {
                    try (FileOutputStream fos = new FileOutputStream(it, false)) {
                        fos.write(classTransformationResult.classBytes());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ClassTransformationResult transform(InputStream is) throws Exception {
        ClassReader classReader = new ClassReader(is);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
        LogAnnotationClassTransformer classTransformer = new LogAnnotationClassTransformer(classWriter);
        classReader.accept(classTransformer, ClassReader.EXPAND_FRAMES);
        return new ClassTransformationResult(
                classTransformer.classHasBeenTransformed(),
                classWriter.toByteArray()
        );
    }

}
