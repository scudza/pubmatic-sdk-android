package com.adserver.adview.bridges;

import com.adserver.adview.Utils;
import com.vdopia.client.android.VDO;
import com.vdopia.client.android.VDOView;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;

public class AdBridgeiVdopia extends AdBridgeAbstract {

	public AdBridgeiVdopia(Context context, WebView view, String campaignId,
			String externalParams,String trackUrl) {
		super(context, view, campaignId, externalParams, trackUrl);
	}

	public void run() {
		try {
			String applicationKey = Utils.scrape(externalParams, "<param name=\"applicationKey\">", "</param>");
			VDOView iVdopiaView = new VDOView(context);
			iVdopiaView.setLayoutParams(view.getLayoutParams());
	        VDO.initialize(applicationKey, context);
	        VDO.setListener(new VdopiaEventListener());
	        
	        iVdopiaView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Click();
				}
	        }
			);
	        view.addView(iVdopiaView);
			view.setBackgroundColor(Color.WHITE);
			view.loadDataWithBaseURL(null, "", "text/html", "UTF-8", null);			
		} catch (Exception e) {
		}
	}
	
	private class VdopiaEventListener implements VDO.AdEventListener {
			
		public void adShown(int type) {
			DownloadEnd();
		}
		
		public void noAdsAvailable(int type, int willCheckAgainAfterSeconds) {
			DownloadError("[ERROR] AdBridgeiVdopia: noAdsAvailable");
		}

		@Override
		public void adStart(int arg0) {
			//DownloadEnd();			
		}	
		
	}

}