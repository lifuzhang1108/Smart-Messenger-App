package com.webianks.scintaxxmessenger.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.webianks.scintaxxmessenger.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FacebookActivity extends AppCompatActivity implements View.OnClickListener{

    private ShareDialog shareDialog;
    private String TAG = "FacebookActivity";
    private String name, surname, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle inBundle = getIntent().getExtras();
        name = inBundle.getString("name");
        surname = inBundle.getString("surname");
        imageUrl = inBundle.getString("imageUrl");

        TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);

        new FacebookActivity.DownloadImage((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);
    }

    private void share(){
        shareDialog = new ShareDialog(this);
        List<String> ids = new ArrayList<String>();
        ids.add("{USER_ID}");
        ids.add("{USER_ID}");
        ids.add("{USER_ID}");

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://www.sitepoint.com"))
                .setContentTitle("This is a content title")
                .setContentDescription("This is a description")
                .setShareHashtag(new ShareHashtag.Builder().setHashtag("#sitepoint").build())
                .setPeopleIds(ids)
                .setPlaceId("{PLACE_ID}")
                .build();

        shareDialog.show(content);
    }

    private void getPosts(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(), "/me/posts", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.e(TAG,response.toString());
                    }
                }
        ).executeAsync();
    }

    private void logout(){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(FacebookActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share:
                share();
                break;

//            case R.id.getPosts:
//                getPosts();
//                break;

            case R.id.logout:
                logout();
                break;
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage) {
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

    /*try {
        Log.e(TAG,response.getJSONObject().getJSONArray("data").toString());
        for(int i=0;i<response.getJSONObject().getJSONArray("data").length();i++){
            JSONObject post = response.getJSONObject().getJSONArray("data").getJSONObject(i);
            if(post.has("story"))
                Log.e(TAG,post.getString("story"));
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }*/
}
