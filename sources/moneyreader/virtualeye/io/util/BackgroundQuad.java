package moneyreader.virtualeye.io.util;

import android.opengl.GLES20;
import com.maxst.ar.BackgroundTexture;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class BackgroundQuad {
    private static final String FRAGMENT_SHADER_SRC = "precision mediump float;\nvarying vec2 v_texCoord;\nuniform sampler2D u_texture;\nvoid main(void)\n{\n\tgl_FragColor = texture2D(u_texture, v_texCoord);\n}\n";
    private static final byte[] INDEX_BUF = new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 2, (byte) 3, (byte) 0};
    private static final float[] TEXTURE_COORD_BUF = new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f};
    private static final float[] VERTEX_BUF = new float[]{-0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f};
    private static final String VERTEX_SHADER_SRC = "attribute vec4 a_position;\nattribute vec2 a_texCoord;\nvarying vec2 v_texCoord;\nuniform mat4 u_mvpMatrix;\nvoid main()\t\t\t\t\t\t\t\n{\t\t\t\t\t\t\t\t\t\t\n\tgl_Position = u_mvpMatrix * a_position;\n\tv_texCoord = a_texCoord; \t\t\t\n}\t\t\t\t\t\t\t\t\t\t\n";
    private ByteBuffer indexBuffer;
    private int mvpMatrixHandle;
    private int positionHandle;
    private int shaderProgramId = 0;
    private FloatBuffer textureCoordBuff;
    private int textureCoordHandle;
    private int textureHandle;
    private FloatBuffer vertexBuffer;

    BackgroundQuad() {
        ByteBuffer bb = ByteBuffer.allocateDirect((VERTEX_BUF.length * 32) / 8);
        bb.order(ByteOrder.nativeOrder());
        this.vertexBuffer = bb.asFloatBuffer();
        this.vertexBuffer.put(VERTEX_BUF);
        this.vertexBuffer.position(0);
        bb = ByteBuffer.allocateDirect((INDEX_BUF.length * 8) / 8);
        bb.order(ByteOrder.nativeOrder());
        this.indexBuffer = bb;
        this.indexBuffer.put(INDEX_BUF);
        this.indexBuffer.position(0);
        bb = ByteBuffer.allocateDirect((TEXTURE_COORD_BUF.length * 32) / 8);
        bb.order(ByteOrder.nativeOrder());
        this.textureCoordBuff = bb.asFloatBuffer();
        this.textureCoordBuff.put(TEXTURE_COORD_BUF);
        this.textureCoordBuff.position(0);
        this.shaderProgramId = ShaderUtil.createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);
        this.positionHandle = GLES20.glGetAttribLocation(this.shaderProgramId, "a_position");
        this.textureCoordHandle = GLES20.glGetAttribLocation(this.shaderProgramId, "a_texCoord");
        this.mvpMatrixHandle = GLES20.glGetUniformLocation(this.shaderProgramId, "u_mvpMatrix");
        this.textureHandle = GLES20.glGetUniformLocation(this.shaderProgramId, "u_texture");
    }

    void draw(BackgroundTexture texture, float[] projectionMatrix) {
        if (texture != null) {
            GLES20.glUseProgram(this.shaderProgramId);
            GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 0, this.vertexBuffer);
            GLES20.glEnableVertexAttribArray(this.positionHandle);
            GLES20.glVertexAttribPointer(this.textureCoordHandle, 2, 5126, false, 0, this.textureCoordBuff);
            GLES20.glEnableVertexAttribArray(this.textureCoordHandle);
            GLES20.glUniformMatrix4fv(this.mvpMatrixHandle, 1, false, projectionMatrix, 0);
            GLES20.glActiveTexture(33984);
            GLES20.glUniform1i(this.textureHandle, 0);
            GLES20.glBindTexture(3553, texture.getTextureId());
            GLES20.glDrawElements(4, INDEX_BUF.length, 5121, this.indexBuffer);
            GLES20.glDisableVertexAttribArray(this.positionHandle);
            GLES20.glDisableVertexAttribArray(this.textureCoordHandle);
            GLES20.glBindTexture(3553, 0);
        }
    }
}
