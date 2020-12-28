package com.xlab13.yummycarrot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
	private Context context;

	private final int SHOW_BANNER = 1;
	private final int HIDE_BANNER = 0;
	private final int SHOW_ADS = 2;

	private InterstitialAd mInterstitialAd;
	private RewardedAdLoadCallback adLoadCallback;

	protected AdView adView;
	protected View gameView;

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case SHOW_BANNER:
					adView.setVisibility(View.VISIBLE);
					break;
				case HIDE_BANNER:
					adView.setVisibility(View.GONE);
					break;
				case SHOW_ADS:
					Log.d("~~~", "SHOW_ADS");
					mInterstitialAd.show();
					break;
			}
		}
	};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		RelativeLayout layout = new RelativeLayout(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags( WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		gameView = initializeForView(new YummyCarrot(this), config);
		layout.addView(gameView);

		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId("ca-app-pub-8059131308960326/3171152736"); //ca-app-pub-8059131308960326/3171152736

		AdRequest adRequest = new AdRequest.Builder()
				.build();
		adView.loadAd(adRequest);

		RelativeLayout.LayoutParams adParams =
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		layout.addView(adView, adParams);
		setContentView(layout);

		adView.setVisibility(View.VISIBLE);

		initAd();
	}

	private void initAd(){
		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId("ca-app-pub-8059131308960326/8360077849"); //ca-app-pub-8059131308960326/8360077849
		mInterstitialAd.setAdListener(new AdListener(){
			@Override
			public void onAdClosed() {
				super.onAdClosed();
				mInterstitialAd.loadAd(new AdRequest.Builder().build());
			}
		});
		mInterstitialAd.loadAd(new AdRequest.Builder().build());
	}


	public void showBanner(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_BANNER : HIDE_BANNER);
	}


	public void showAds() {
		handler.sendEmptyMessage(SHOW_ADS);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (adView != null) {
			adView.pause();
		}
	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}
}
