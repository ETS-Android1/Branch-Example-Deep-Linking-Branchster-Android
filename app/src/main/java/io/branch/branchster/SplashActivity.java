package io.branch.branchster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;

import io.branch.branchster.util.MonsterPreferences;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.PrefHelper;
import io.branch.referral.util.LinkProperties;

public class SplashActivity extends Activity {

    private ImageView imgSplash1, imgSplash2;
    private final int ANIM_DURATION = 1500;
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PrefHelper.Debug("BranchSDK", "SplashActivity.onCreate");

        imgSplash1 = findViewById(R.id.imgSplashFactory1);
        imgSplash2 = findViewById(R.id.imgSplashFactory2);
    }

    @Override protected void onStart() {
        super.onStart();
        PrefHelper.Debug("BranchSDK", "SplashActivity.onStart");
        Branch.getInstance().initSession(branchReferralInitListener, this.getIntent().getData(), this);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        PrefHelper.Debug("BranchSDK", "SplashActivity.onNewIntent");
//        Branch.getInstance().reInitSession(this, branchReferralInitListener);
        }

    public Branch.BranchUniversalReferralInitListener branchReferralInitListener = new Branch.BranchUniversalReferralInitListener() {
        @Override public void onInitFinished(BranchUniversalObject branchUniversalObject,
                                             LinkProperties linkProperties, BranchError branchError) {
            //If not Launched by clicking Branch link
            PrefHelper.Debug("BranchSDK", "SplashActivity.onStart, onInitFinished, branchUniversalObject = " + branchUniversalObject + ", linkProperties = " + linkProperties + ", branchError = " + branchError);
            if (branchUniversalObject == null) {
                proceedToAppTransparent();
            } else if (linkProperties != null &&
                    !TextUtils.isEmpty(linkProperties.getControlParams().get("$web_only"))
                    && !TextUtils.isEmpty(linkProperties.getControlParams().get("$3p"))) {
                String webOnlyParam = linkProperties.getControlParams().get("$web_only");
                String is3pParam = linkProperties.getControlParams().get("$3p");
                if (!TextUtils.isEmpty(webOnlyParam) && !TextUtils.isEmpty(is3pParam)) {
                    if (Boolean.parseBoolean(webOnlyParam)) {
                        String url = linkProperties.getControlParams().get("$original_url");
                        WebView webView = findViewById(R.id.web_only_view);
                        webView.setVisibility(View.VISIBLE);
                        webView.loadUrl(url);
                    }
                }
            }
            /* In case the clicked link has $android_deeplink_path the Branch will launch the MonsterViewer automatically since AutoDeeplinking feature is enabled.
             * Launch Monster viewer activity if a link clicked without $android_deeplink_path
             */
            else if (!branchUniversalObject.getContentMetadata().getCustomMetadata().containsKey("$android_deeplink_path")) {
                MonsterPreferences prefs = MonsterPreferences.getInstance(getApplicationContext());
                prefs.saveMonster(branchUniversalObject);
                Intent intent = new Intent(SplashActivity.this, MonsterViewerActivity.class);
                intent.putExtra(MonsterViewerActivity.MY_MONSTER_OBJ_KEY, prefs.getLatestMonsterObj());
                startActivity(intent);
                finish();
            }
        }
    };
    
    private void proceedToAppTransparent() {
        Animation animSlideIn = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
        animSlideIn.setDuration(ANIM_DURATION);
        animSlideIn.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }

            @Override public void onAnimationEnd(Animation animation) {
                MonsterPreferences prefs = MonsterPreferences.getInstance(getApplicationContext());
                Intent intent;
                if (prefs.getMonsterName() == null || prefs.getMonsterName().length() == 0) {
                    prefs.setMonsterName("");
                    intent = new Intent(SplashActivity.this, MonsterCreatorActivity.class);
                } else {
                    // Create a default monster
                    intent = new Intent(SplashActivity.this, MonsterViewerActivity.class);
                    intent.putExtra(MonsterViewerActivity.MY_MONSTER_OBJ_KEY, prefs.getLatestMonsterObj());
                }
                startActivity(intent);
                finish();
            }

            @Override public void onAnimationRepeat(Animation animation) { }
        });

        imgSplash1.setVisibility(View.VISIBLE);
        imgSplash2.setVisibility(View.VISIBLE);
        imgSplash2.startAnimation(animSlideIn);
    }
}
