package moneyreader.virtualeye.io.util;

import com.maxst.ar.BackgroundRenderer;
import com.maxst.ar.BackgroundRenderer.RenderingOption;
import com.maxst.ar.BackgroundTexture;
import com.maxst.ar.CameraDevice;

public class BackgroundRenderHelper {
    private BackgroundQuad backgroundQuad;
    private BackgroundRenderer backgroundRenderer;

    public void init() {
        this.backgroundQuad = new BackgroundQuad();
        this.backgroundRenderer = BackgroundRenderer.getInstance();
    }

    public void setRenderingOption(RenderingOption... options) {
        this.backgroundRenderer.setRenderingOption(options);
    }

    public void drawBackground() {
        BackgroundTexture backgroundTexture = this.backgroundRenderer.getBackgroundTexture();
        if (backgroundTexture != null) {
            this.backgroundRenderer.begin(backgroundTexture);
            this.backgroundRenderer.renderBackgroundToTexture();
            this.backgroundRenderer.end();
            this.backgroundQuad.draw(backgroundTexture, CameraDevice.getInstance().getBackgroundPlaneProjectionMatrix());
        }
    }

    public void drawBackground(BackgroundTexture backgroundTexture) {
        if (backgroundTexture != null) {
            this.backgroundQuad.draw(backgroundTexture, CameraDevice.getInstance().getBackgroundPlaneProjectionMatrix());
        }
    }

    public BackgroundTexture drawBackgroundToTexture() {
        BackgroundTexture backgroundTexture = this.backgroundRenderer.getBackgroundTexture();
        if (backgroundTexture == null) {
            return null;
        }
        this.backgroundRenderer.begin(backgroundTexture);
        this.backgroundRenderer.renderBackgroundToTexture();
        this.backgroundRenderer.end();
        return backgroundTexture;
    }
}
