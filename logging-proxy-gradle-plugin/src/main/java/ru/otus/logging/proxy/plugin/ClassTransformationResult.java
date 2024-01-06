package ru.otus.logging.proxy.plugin;

@SuppressWarnings("java:S6218")
public record ClassTransformationResult(boolean classHasBeenTransformed, byte[] classBytes){
}
