import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by muralikrish on 7/6/15.
 */
public class Circle {

    private FloatBuffer vertexBuffer;
   private int sides;
    // number of coordinates per vertex in this array
    final int COORDS_PER_VERTEX = 3;
    private final int mProgram;
    float triangleCoords[];

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.f, 0.76953125f, 0.22265625f, 1.0f };

    private final String vertexShaderCode =
            //"attribute vec4 vPosition;" +
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    //"  gl_Position = vPosition;" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    private int mPositionHandle;
    private int mColorHandle;
   // private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


    public Circle(int sides) {

        triangleCoords = new float[9*sides];
        this.sides = sides;
        int index = 0;
        float angle_1=0;
        float angle = (float)Math.PI*2/sides;
        for(int a =0;a<3;a++) {
            triangleCoords[index] = 0;
            index++;
        }
        triangleCoords[3]=0.5f;
        triangleCoords[4]=0.0f;
        triangleCoords[5]=0.0f;
        triangleCoords[6]=0.5f*((float)Math.cos(angle));
        triangleCoords[7]=0.5f*((float)Math.sin(angle));
        angle_1=angle_1+angle;
        triangleCoords[8]=0.0f;
        index=9;
        for(int i=0;i<sides-1;i++){
            for(int a =0;a<3;a++){
                triangleCoords[index] = 0;
                Log.d("MyTag",triangleCoords[index]+" "+index);
                index++;
            }
            Log.d("index",index+"");
            Log.d("6",triangleCoords[6]+"");
            for(int b=0;b<3;b++){
                triangleCoords[index]=triangleCoords[index-6];
                Log.d("MyTag",triangleCoords[index-6]+" "+index);
                index++;
            }
            angle_1 = angle_1+angle;
            for(int c=0;c<1;c++){
                triangleCoords[index] = 0.5f*((float)Math.cos(angle_1));
                Log.d("MyTag",triangleCoords[index]+" "+index);
                index++;
                triangleCoords[index] = 0.5f*((float)Math.sin(angle_1));
                Log.d("MyTag",triangleCoords[index]+" "+index);
                index++;
                triangleCoords[index] =0.0f;
                Log.d("MyTag",triangleCoords[index]+" "+index);
                index++;
            }

        }
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }
    public void draw(float[] mvpMatrix) {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,GLES20.GL_FLOAT,false, vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3*sides);

        // Disable vertex array
        //GLES20.glDisableVertexAttribArray(mPositionHandle);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3*sides);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
