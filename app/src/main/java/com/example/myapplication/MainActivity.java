package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private boolean isInverted = false;
    private boolean hasVideo = false;
    private float scale = 2f;
    private ImageView imgView;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        imgView = findViewById(R.id.imgLogo);
        TextView txtHello = findViewById(R.id.txtHello);
        txtHello.setText("Hello world, but edited in runtime!");

        Button btnChange = findViewById(R.id.btnChange);
        Button btnDelete = findViewById(R.id.btnDelete);
        btnChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isInverted)
                    imgView.setImageResource(R.drawable.logo);
                else
                    imgView.setImageResource(R.drawable.logoinvert);
                isInverted = !isInverted;
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                imgView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Deleting current view and creating a new ImageView!",
                        Toast.LENGTH_LONG).show();
                if(hasVideo) {
                    videoView.setVisibility(View.GONE);
                    hasVideo = false;
                }
                ImageView newImgView = new ImageView(getApplicationContext());
                int pixels = (int) (300 * scale + 0.5f);
                RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(pixels,pixels);
                params.addRule(RelativeLayout.BELOW, R.id.line);
                params.setMargins(0,25,0,0);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                newImgView.setLayoutParams(params);
                newImgView.setId(R.id.imgLogo);
                if(!isInverted)
                    newImgView.setImageResource(R.drawable.logo);
                else
                    newImgView.setImageResource(R.drawable.logoinvert);

                RelativeLayout relativeLayout = findViewById(R.id.pai);
                relativeLayout.addView(newImgView);
                setContentView(relativeLayout);
                imgView = newImgView;
            }
        });


        Button btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show The Image in a ImageView
                new DownloadImageTask(imgView).execute("https://upload.wikimedia.org/wikipedia/en/thumb/3/30/Java_programming_language_logo.svg/1200px-Java_programming_language_logo.svg.png");

            }
        });


        Button btnVideo = findViewById(R.id.btnVideo);

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgView.setVisibility(View.GONE);
                videoView = new VideoView(getApplicationContext());
                int pixels = (int) (300 * scale + 0.5f);
                RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(pixels,pixels);
                params.addRule(RelativeLayout.BELOW, R.id.line);
                params.setMargins(0,50,0,0);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                videoView.setLayoutParams(params);
                videoView.setId(R.id.imgLogo);

                RelativeLayout relativeLayout = findViewById(R.id.pai);
                relativeLayout.addView(videoView);
                setContentView(relativeLayout);

                videoView.setVideoURI(getMedia("white_noise"));
                videoView.start();
            }
        });

    }
    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() +
                "/raw/" + mediaName);
    }

}


class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
