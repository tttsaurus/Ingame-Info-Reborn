package com.tttsaurus.ingameinfo.common.core.render;

import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class GlResourceManager
{
    private static final List<GlDisposable> disposables = new CopyOnWriteArrayList<>();

    public static void addDisposable(GlDisposable disposable)
    {
        disposables.add(disposable);
    }
    public static void removeDisposable(GlDisposable disposable)
    {
        disposables.remove(disposable);
    }

    public static void disposeAll(Logger logger)
    {
        for (GlDisposable disposable: disposables)
        {
            logger.info("Disposing " + disposable.getClass().getSimpleName());
            disposable.dispose();
            checkGLError(logger);
        }
    }

    private static void checkGLError(Logger logger)
    {
        int error;
        while ((error = GL11.glGetError()) != GL11.GL_NO_ERROR)
            logger.warn("OpenGL Error: " + getErrorString(error));
    }

    private static String getErrorString(int error)
    {
        return switch (error)
        {
            case GL11.GL_INVALID_ENUM -> "GL_INVALID_ENUM";
            case GL11.GL_INVALID_VALUE -> "GL_INVALID_VALUE";
            case GL11.GL_INVALID_OPERATION -> "GL_INVALID_OPERATION";
            case GL11.GL_STACK_OVERFLOW -> "GL_STACK_OVERFLOW";
            case GL11.GL_STACK_UNDERFLOW -> "GL_STACK_UNDERFLOW";
            case GL11.GL_OUT_OF_MEMORY -> "GL_OUT_OF_MEMORY";
            default -> "Unknown Error (code: " + error + ")";
        };
    }
}
