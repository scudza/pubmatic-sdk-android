/*
 * PubMatic Inc. (�PubMatic�) CONFIDENTIAL
 * Unpublished Copyright (c) 2006-2014 PubMatic, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of PubMatic. The intellectual and technical concepts contained
 * herein are proprietary to PubMatic and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from PubMatic.  Access to the source code contained herein is hereby forbidden to anyone except current PubMatic employees, managers or contractors who have executed 
 * Confidentiality and Non-disclosure agreements explicitly covering such access.
 *
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes  
 * information that is confidential and/or proprietary, and is a trade secret, of  PubMatic.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE, 
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT OF PubMatic IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE 
 * LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
 */

package com.moceanmobile.mast.samples;

import java.util.Map;

import com.moceanmobile.mast.MASTAdView;
import com.moceanmobile.mast.MASTAdViewDelegate.RequestListener;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class AdvancedResizeAdSlot extends RefreshActivity {

	private MASTAdView adView;
	private int screenWidth, screenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_resize_adslot);
		adView = (MASTAdView) findViewById(R.id.adView);
		DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		RequestListener mRequestListener = new RequestListener() {

			@Override
			public void onReceivedThirdPartyRequest(MASTAdView arg0, Map<String, String> arg1,
					Map<String, String> arg2) {
			}

			@Override
			public void onReceivedAd(MASTAdView adView) {
				if (adView.getImageHeight() != 0) {
					if (screenWidth < adView.getImageWidth() && screenHeight < adView.getImageHeight()) {
						adView.setLayoutParams(
								new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					} else {
						if (screenWidth < adView.getImageWidth()) {
							adView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									adView.getImageHeight()));
						} else if (screenHeight < adView.getImageHeight()) {
							adView.setLayoutParams(
									new RelativeLayout.LayoutParams(adView.getImageWidth(), LayoutParams.MATCH_PARENT));
						} else {
							Log.d("Test","Ad slot else");
							adView.setLayoutParams(
									new RelativeLayout.LayoutParams(adView.getImageWidth(), adView.getImageHeight()));
						}

					}
				}
			}

			@Override
			public void onFailedToReceiveAd(MASTAdView arg0, Exception arg1) {
				Log.d("Test", "Ad falied");
			}
		};

		adView.setRequestListener(mRequestListener);
		adView.update();
	}

}
