package com.wirsztj.jsonparser.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wirsztj.jsonparser.R;


public class MainActivity extends Activity {

    Button getInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(this).build());

        setContentView(R.layout.activity_main);

        getInfos = (Button)findViewById(R.id.getInfos);

        getInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GetInformations.class));
            }
        });
    }
}
