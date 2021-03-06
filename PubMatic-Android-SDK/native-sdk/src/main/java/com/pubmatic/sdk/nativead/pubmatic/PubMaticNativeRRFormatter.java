package com.pubmatic.sdk.nativead.pubmatic;

import static com.pubmatic.sdk.common.CommonConstants.ID_STRING;
import static com.pubmatic.sdk.common.CommonConstants.NATIVE_ASSETS_STRING;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_CLICKTRACKERS;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_DATA;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_ERROR;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_FALLBACK;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_IMG;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_IMPTRACKERS;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_JSTRACKER;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_LINK;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_MEDIATION;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_NATIVE_STRING;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_TEXT;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_THIRDPARTY_STRING;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_TITLE;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_URL;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_VALUE;
import static com.pubmatic.sdk.common.CommonConstants.RESPONSE_VER;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.pubmatic.sdk.common.AdRequest;
import com.pubmatic.sdk.common.AdResponse;
import com.pubmatic.sdk.common.CommonConstants;
import com.pubmatic.sdk.common.RRFormatter;
import com.pubmatic.sdk.common.network.HttpRequest;
import com.pubmatic.sdk.common.network.HttpResponse;
import com.pubmatic.sdk.common.CommonConstants.AD_REQUEST_TYPE;
import com.pubmatic.sdk.common.CommonConstants.CONTENT_TYPE;
import com.pubmatic.sdk.nativead.NativeAdDescriptor;
import com.pubmatic.sdk.nativead.PMNativeAd.Image;
import com.pubmatic.sdk.nativead.bean.PMAssetResponse;
import com.pubmatic.sdk.nativead.bean.PMDataAssetResponse;
import com.pubmatic.sdk.nativead.bean.PMImageAssetResponse;
import com.pubmatic.sdk.nativead.bean.PMTitleAssetResponse;

public class PubMaticNativeRRFormatter implements RRFormatter {

	private final static String kPubMatic_BidTag = "PubMatic_Bid";
	private static final String kcreative_tag = "creative_tag";
	private static final String kerror_code = "error_code";
	private static final String kerror_message = "error_string";

	private AdRequest mRequest;

	@Override
	public HttpRequest formatRequest(AdRequest request) {
		mRequest = request;
		PubMaticNativeAdRequest adRequest = (PubMaticNativeAdRequest) request;
		HttpRequest httpRequest = new HttpRequest(CONTENT_TYPE.URL_ENCODED);
		httpRequest.setUserAgent(adRequest.getUserAgent());
		httpRequest.setConnection("close");
		httpRequest.setRequestUrl(request.getAdServerURL());
		httpRequest.setRequestType(AD_REQUEST_TYPE.PUB_NATIVE);
		httpRequest.setRequestMethod(CommonConstants.HTTPMETHODPOST);
		httpRequest.setPostData(adRequest.getPostData());
		return httpRequest;
	}

	@Override
	public AdResponse formatResponse(HttpResponse httpResponse) {
		AdResponse adResponse = new AdResponse();
		// adResponse.setStatusCode(response.getStatusCode());
		adResponse.setRequest(mRequest);

		NativeAdDescriptor nativeAdDescriptor = null;

		try {
			if (httpResponse != null && httpResponse.getResponseData()!=null) {
				ArrayList<PMAssetResponse> nativeAssetList = new ArrayList<PMAssetResponse>();
				String clickUrl = null;
				String fallbackUrl = null;
				String creativeId = null;
				String feedId = null;
				String type = "native";
				String subType = null;
				JSONObject mediationObj = null;
				JSONObject nativeObj = null;
				int nativeVersion = 0;
				String mediationPartnerName = null;
				String mediationId = null;
				JSONObject mediationData = null;
				String adUnitId = null;
				String errorMessage = null;
				String mediationSource = null;
				String[] clickTrackersStringArray = null;
				String[] impressionTrackerStringArray = null;
				String jsTrackerString = null;

				JSONObject responseObj = new JSONObject(httpResponse.getResponseData());
				JSONObject object = responseObj.getJSONObject(kPubMatic_BidTag);



				// If there is an error from the server which happens when provided
				// wrong ad parameters, return the error with error code and error
				// message.

				if (!object.isNull(kerror_code)
						&& !(object.getString(kerror_code).equalsIgnoreCase(""))) {

					adResponse.setErrorCode(object.getString(kerror_code));
					adResponse.setErrorMessage(object.getString(kerror_message));

					return adResponse;
				}

				if (object.isNull(kcreative_tag) == false) {
					JSONObject creative_tag = object.getJSONObject(kcreative_tag);

					/* Get the native object */
					nativeObj = creative_tag.getJSONObject(RESPONSE_NATIVE_STRING);

					if (nativeObj != null) {
						nativeVersion = nativeObj.optInt(RESPONSE_VER);

						/* Parse impression trackers starts */
						JSONArray imptracker = nativeObj
								.optJSONArray(RESPONSE_IMPTRACKERS);
						nativeObj.remove(RESPONSE_IMPTRACKERS);
						for (int i = 0; imptracker != null
								&& i < imptracker.length(); i++) {
							String url = imptracker.optString(i);
							if (impressionTrackerStringArray == null) {
								impressionTrackerStringArray = new String[imptracker
										.length()];
							}

							if (url != null) {
								impressionTrackerStringArray[i] = url;
							}
						}
						/* Parse impression trackers Ends */

						// Parse jsTracker
						jsTrackerString = nativeObj
								.optString(RESPONSE_JSTRACKER);

						/* Parse link object and contents */
						JSONObject linkObj = nativeObj
								.optJSONObject(RESPONSE_LINK);
						if (linkObj != null) {
							clickUrl = linkObj.optString(RESPONSE_URL);
							fallbackUrl = linkObj
									.optString(RESPONSE_FALLBACK);

							/* Parse click trackers */
							JSONArray clktrackerArray = linkObj
									.optJSONArray(RESPONSE_CLICKTRACKERS);
							linkObj.remove(RESPONSE_CLICKTRACKERS);
							for (int i = 0; clktrackerArray != null
									&& i < clktrackerArray.length(); i++) {
								String clickTrackUrl = clktrackerArray
										.optString(i);
								if (clickTrackersStringArray == null) {
									clickTrackersStringArray = new String[clktrackerArray
											.length()];
								}

								if (clickTrackUrl != null) {
									clickTrackersStringArray[i] = clickTrackUrl;
								}
							}
							/* Parse click trackers Ends */
						}

						// Parse assets.
						JSONArray assets = nativeObj
								.optJSONArray(NATIVE_ASSETS_STRING);
						if (assets != null && assets.length() > 0) {
							JSONObject asset = null;
							int assetId = -1;
							for (int i = 0; i < assets.length(); i++) {
								asset = assets.optJSONObject(i);
								assetId = asset.optInt(ID_STRING, -1);

								if (!asset.isNull(RESPONSE_IMG)) {
									JSONObject imageAssetObj = asset
											.optJSONObject(RESPONSE_IMG);
									PMImageAssetResponse imageAsset = new PMImageAssetResponse();
									imageAsset.assetId = assetId;
									imageAsset.setImage(Image
											.getImage(imageAssetObj));
									if (!TextUtils.isEmpty(imageAsset
											.getImage().url)) {
										nativeAssetList.add(imageAsset);
									}
									continue;
								} else if (!asset.isNull(RESPONSE_TITLE)) {
									JSONObject titleAssetObj = asset
											.optJSONObject(RESPONSE_TITLE);
									PMTitleAssetResponse titleAsset = new PMTitleAssetResponse();
									titleAsset.assetId = assetId;
									titleAsset.titleText = titleAssetObj
											.optString(RESPONSE_TEXT);
									if (!TextUtils
											.isEmpty(titleAsset.titleText)) {
										nativeAssetList.add(titleAsset);
									}
									continue;
								} else if (!asset.isNull(RESPONSE_DATA)) {
									JSONObject dataAssetObj = asset
											.optJSONObject(RESPONSE_DATA);
									PMDataAssetResponse dataAsset = new PMDataAssetResponse();
									dataAsset.assetId = assetId;
									dataAsset.value = dataAssetObj
											.optString(RESPONSE_VALUE);
									if (!TextUtils.isEmpty(dataAsset.value)) {
										nativeAssetList.add(dataAsset);
									}
								}
							}
						}
					}

				}
				//Native parsing ends

				/**
				 * Valid native ad should contain click url, at least one asset
				 * element from the list (main image, icon image, logo image,
				 * title, description), optionally rating, zero or more
				 * impression and click trackers.
				 */
				// @formatter:off
				if ((TextUtils.equals(RESPONSE_NATIVE_STRING, type) || (TextUtils
						.equals(RESPONSE_THIRDPARTY_STRING, type) && TextUtils
						.equals(RESPONSE_NATIVE_STRING, subType)))
						&& !TextUtils.isEmpty(clickUrl)
						&& nativeAssetList != null
						&& nativeAssetList.size() > 0) {
					nativeAdDescriptor = new NativeAdDescriptor(type,
							nativeVersion, clickUrl, fallbackUrl,
							impressionTrackerStringArray,
							clickTrackersStringArray, jsTrackerString,
							nativeAssetList);

					nativeAdDescriptor.setNativeAdJSON(httpResponse.getResponseData());
				} else if ((TextUtils.equals(RESPONSE_THIRDPARTY_STRING, type) && TextUtils
						.equals(RESPONSE_MEDIATION, subType))
						&& !TextUtils.isEmpty(mediationPartnerName)
						&& (!TextUtils.isEmpty(mediationId) || !TextUtils
								.isEmpty(creativeId))
						&& !TextUtils.isEmpty(adUnitId)
						&& !TextUtils.isEmpty(mediationSource)) {
					nativeAdDescriptor = new NativeAdDescriptor(type,
							creativeId, mediationPartnerName, mediationId,
							adUnitId, mediationSource,
							impressionTrackerStringArray,
							clickTrackersStringArray, jsTrackerString, feedId);
					nativeAdDescriptor.setNativeAdJSON(httpResponse.getResponseData());
				}
				// @formatter:on
			}
		} catch (JSONException e) {
			try {
				// Check whether there is an error. If the error format is
				/* { "error": "No ads available" } */
				JSONObject errorResponse = new JSONObject(httpResponse.getResponseData());
				String errorMessage = errorResponse.optString(RESPONSE_ERROR);
				if (!TextUtils.isEmpty(errorMessage)) {
					adResponse.setErrorMessage(errorMessage);
				}
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		adResponse.setRenderable(nativeAdDescriptor);
		return adResponse;
	}

	public AdRequest getAdRequest() {
		return mRequest;
	}

	public void setAdRequest(AdRequest mRequest) {
		this.mRequest = mRequest;
	}

	public AdRequest getRequest() {
		return mRequest;
	}

	public void setRequest(AdRequest mRequest) {
		this.mRequest = mRequest;
	}

}
