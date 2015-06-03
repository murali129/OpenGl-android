package com.example.muralikrish.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class MainActivity extends ActionBarActivity {

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }
}

class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }
}
