package me.inventory.admin.aug;


import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class App extends Application {


    private static String id;
    AdView adView ;
    InterstitialAd mInterstitialAd;
    RelativeLayout relativeLayout ;

    String url = "https://raw.githubusercontent.com/nouamanelachhab/Ads/master/FlexADS.json";

    public App() {

    }


    @Override
    public void onCreate() {
        getMyIdsFromServers();
        super.onCreate();


        //  getMyIdsFromFireBase();
    }



    private void getidinter(String id)
    {
       App.id = id;
    }
    private void getMyIdsFromServers(){


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONObject object = response.getJSONObject("MyAds");

                    String BannerValue = object.getString("BannerNL");
                    String Interstitialvalue = object.getString("InterstitialNL");

                    BuildMyAds(BannerValue , Interstitialvalue);
                    getidinter(Interstitialvalue);




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });


        requestQueue.add(jsonObjectRequest);

    }



    private void BuildMyAds(String bannerID , String interstitialId){

        // Build Interstitial :

        // ca-app-pub-3940256099942544/1033173712





            // Build banner :
        // ca-app-pub-3940256099942544/6300978111
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(bannerID);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (relativeLayout !=null){
                    Showbanner(relativeLayout);
                }
            }


        });



    }


    public void ShowInterstitial(Activity aa){




            InterstitialAd.load(this,App.id, new AdRequest.Builder().build(),
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            Toast.makeText(App.this,"ad loaded",Toast.LENGTH_LONG).show();


                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            mInterstitialAd = null;
                            Toast.makeText(App.this, loadAdError.getMessage(),Toast.LENGTH_LONG).show();

                        }


                    });
        if (mInterstitialAd != null) {
            mInterstitialAd.show(aa);

        }
        else {
            Toast.makeText(aa, "click",Toast.LENGTH_LONG).show();
        }


    }

    public void Showbanner(RelativeLayout relativeLayout){
        if (adView == null){
            return;
        }

        if (adView.getParent() != null){
            ((ViewGroup)adView.getParent()).removeView(adView);
        }
        relativeLayout.addView(adView);

    }


}
