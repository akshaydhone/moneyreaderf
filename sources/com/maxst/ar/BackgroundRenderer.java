package com.maxst.ar;

public class BackgroundRenderer {
    private static BackgroundRenderer instance = null;

    public enum RenderingOption {
        FEATURE_RENDERER(1),
        PROGRESS_RENDERER(2),
        AXIS_RENDERER(4),
        SURFACE_MESH_RENDERER(8),
        VIEW_FINDER_RENDERER(16);
        
        private int value;

        private RenderingOption(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static BackgroundRenderer getInstance() {
        if (instance == null) {
            instance = new BackgroundRenderer();
        }
        return instance;
    }

    private BackgroundRenderer() {
    }

    public BackgroundTexture getBackgroundTexture() {
        long cPtr = MaxstARJNI.BackgroundRenderer_getBackgroundTexture();
        if (cPtr == 0) {
            return null;
        }
        return new BackgroundTexture(cPtr);
    }

    public void begin(BackgroundTexture backgroundTexture) {
        MaxstARJNI.BackgroundRenderer_begin(backgroundTexture.getcMemPtr());
    }

    public void renderBackgroundToTexture() {
        MaxstARJNI.BackgroundRenderer_renderBackgroundToTexture();
    }

    public void end() {
        MaxstARJNI.BackgroundRenderer_end();
    }

    public void setRenderingOption(RenderingOption... args) {
        int option = 0;
        for (RenderingOption renderingOption : args) {
            option |= renderingOption.getValue();
        }
        MaxstARJNI.BackgroundRenderer_setRenderingOption(option);
    }
}
