package com.example.scsebuddy;


import androidx.appcompat.app.AppCompatActivity;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PanoramaViewActivity extends AppCompatActivity {

    private static VrPanoramaView panoramaView;
    private String[] path;
    private int pageNum = 0;
    TextView pageNumTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paranoma_view);
        panoramaView = findViewById(R.id.viewPanaroma);
        pageNumTextView = findViewById(R.id.pageNumTextView);
        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            path = (String[]) b.get("path");
            if (path != null)
                loadPanoramaImage(path[pageNum]);
            else
                loadPanoramaImage("a1");
        }
    }

    private void loadPanoramaImage(String photoID) {
        pageNumTextView.setText(pageNum + "");
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        try {
            options.inputType = VrPanoramaView.Options.TYPE_MONO;
            panoramaView.loadImageFromBitmap(BitmapFactory.decodeResource(getResources(), chooseDrawable(photoID)), options);
        } catch (Exception e) {
            Log.e("TEST", e.toString());
        }
    }

    public void firstMap(View v) {
        if (path != null) {
            pageNum = 0;
            loadPanoramaImage(path[pageNum]);
        } else
            loadPanoramaImage("a1");
    }

    public void backMap(View v) {
        if (path != null) {
            if (pageNum != 0) {
                pageNum--;
            }
            loadPanoramaImage(path[pageNum]);
        } else
            loadPanoramaImage("a1");
    }

    public void nextMap(View v) {
        if (path != null) {
            if (pageNum != path.length - 1) {
                pageNum++;
            }
            loadPanoramaImage(path[pageNum]);
        } else
            loadPanoramaImage("a1");
    }

    public void lastMap(View v) {

        if (path != null) {
            pageNum = path.length - 1;
            loadPanoramaImage(path[pageNum]);
        } else
            loadPanoramaImage("a1");
    }

    private int chooseDrawable(String drawableName) {
        switch (drawableName) {
            case "a1":
                return R.drawable.a1;
            case "a2":
                return R.drawable.a2;
            case "a3":
                return R.drawable.a3;
            case "a4":
                return R.drawable.a4;
            case "a5":
                return R.drawable.a5;
            case "a6":
                return R.drawable.a6;
            case "a7":
                return R.drawable.a7;
            case "a8":
                return R.drawable.a8;
            case "a9":
                return R.drawable.a9;
            case "a10":
                return R.drawable.a10;
            case "a11":
                return R.drawable.a11;
            case "a12":
                return R.drawable.a12;
            case "a13":
                return R.drawable.a13;
            case "a14":
                return R.drawable.a14;
            case "a15":
                return R.drawable.a15;
            case "a16":
                return R.drawable.a16;
            case "a17":
                return R.drawable.a17;
            case "a18":
                return R.drawable.a18;
            case "a19":
                return R.drawable.a19;
            case "a20":
                return R.drawable.a20;
            case "a21":
                return R.drawable.a21;
            case "a22":
                return R.drawable.a22;
            case "a23":
                return R.drawable.a23;
            case "a24":
                return R.drawable.a24;
            case "a25":
                return R.drawable.a25;
            case "a26":
                return R.drawable.a26;
            case "a27":
                return R.drawable.a27;
            case "a28":
                return R.drawable.a28;
            case "a29":
                return R.drawable.a29;
            case "a30":
                return R.drawable.a30;
            case "a31":
                return R.drawable.a31;
            case "a32":
                return R.drawable.a32;
            case "a33":
                return R.drawable.a33;
            case "a34":
                return R.drawable.a34;
            case "a35":
                return R.drawable.a35;
            case "a36":
                return R.drawable.a36;
            case "a37":
                return R.drawable.a37;
            case "a38":
                return R.drawable.a38;
            case "a39":
                return R.drawable.a39;
            case "a40":
                return R.drawable.a40;
            case "a41":
                return R.drawable.a41;
            case "a42":
                return R.drawable.a42;
            case "a43":
                return R.drawable.a43;
            default:
                break;
        }
        return -1;
    }
}