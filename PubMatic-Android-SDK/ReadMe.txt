Welcome to the PubMatic Mobile SDK package for Android, version 5.1.1
/*
 * PubMatic Inc. ("PubMatic") CONFIDENTIAL
 * Unpublished Copyright (c) 2006-2016 PubMatic, All Rights Reserved.
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

Release Notes:

------
3.1.0
------

- Updated code base with new interface.
- Animated GIF and images now render on a native ImageView view.

------
3.1.1
------

- Updating expand dialog to properly handle clicks when using full screen image or
  text (non-web based ad creatives) for interstitial ads.
- Updated onCloseButtonClick to allow developers to override click behavior.
- Updated tracking logic to cover proper cases of tracking (when view is made visible).


------
3.1.2
------

- Updating URL handling for non-HTTP/S links and the internal browser.  The browser
  will now only be invoked with HTTP/S URLs and internally open new HTTP/S URLs.  Any
  non-HTTP/S URL will be routed and opened with an Intent.  Corrected internal browser
  dialog load URL issue.  Added new sample for internal browser testing.
- Updating bridge loading for API 11 and higher for MRAID two-part creatives.  Other
  two-part creative fixes.
- Added layout parameters to WebView when added to expand dialog.  This corrected bug
  introduced with API 17 where the expanded ad view would not fill the expand dialog.
- Set flag to not use cache in WebView.
- Replacing touch handler with click handler for image and text ad rendering.
- Adding validation to third party ad descriptors that attempts to ensure image and
  text ads are rendered natively only if it appears the server's parsing was proper.
  

------
 3.2
------
 
Issues Fixed :  
-Issue related to improper scaling of webView has been fixed. Now webView is scaling properly as expected.
Features : 
-Introduced new method in class MASTAdView – getAdRequestCustomParameters()
-User can now append multiple values for the same custom parameter in the ad request. 
For instance: age=10&age15.
-We have added common-collections-4.4.0 library to achieve this functionality. 
-Now we have separate methods for adding default parameters and custom parameters as stated below.
-getAdRequestParameters() - This method is used for adding default parameters in the request like size_x,size_y etc.
-getAdRequestCustomParameters() - This method is used for adding custom network parameters defined by user, in the request.  


------
 3.3
------

- Native Ads Support : Native Ads allow a publisher to display ads using the same structure and 
formatting of their site/app without hindering the user's experience. 
Such ads grasp the attention of the visitors more than the traditional ad formats. 
Mocean and PubMatic now allow publishers to serve native ads on their inventory.
- Added support for client side mediation in SDK. 
- Added Library projects for adapters of FacebookAudienceNetwork and MoPub SDK.
- New class added for Native Ads support: MASTNativeAd
- Added new Ad request listener for Native ads: NativeRequestListener


------
3.3.1
------

- Refracted code to avoid Network operations on main UI thread and handled the crashes during AdTracking.


------
 3.4
------

Native:
- Changes in SDK to support OpenRTB Dynamic Native Ads API Specification (Version 1)
Link: http://www.iab.net/media/file/OpenRTB-Native-Ads-Specification-1_0-Final.pdf
- Major code change for Native Ad serving to support OpenRTB Native ads specification.
- Changed request and response methods for Native ads.
- Use addNativeAssetRequestList() or addNativeAssetRequest() methods from MASTNativeAd class to request for Native assets.
- On ad response, assets can be retreived using getNativeAssets() method.
- Refer to NativeActivity.java in Samples application for sample implementation of Native ads.
- Changes in mediation adapters for MoPub and FacebookAudienceNetwork as per OpenRTB native ads specification.


Banner:
- No code changes.

Interstitial:
- No code changes.

------
 4.1.1
------

CUU-486-94742 | MAST - Android SDK

------
 4.1.2
------

RJX-889-37479 | setCloseButtonDelay() issue

------
 4.1.3
------

Enabled multi-pixel tracking for SDK.

------
4.1.4
------
- Introduced functionality to retrieve image size when size_required = 1
- Fixed bug to retrieve IDFA

------
 4.2
------

- Added thirdparty mediation support via PubMatic SSP. 
- Removed mediation adapters for native.
- Changes in XML parsing for banner ads to support new <mediation> object.
- Added MediationData bean/modal class for as a container for mediation data.
- Added functionality for passback in case of mediation for banner ad view via thirdpartyPartnerDefaulted() method.
- Added sendImpression and sendClickTracker API methods for thirdparty ads. User should call this methods when third party ad is rendered/clicked.


------
 4.3
------

- Updated SDK version to 4.3
- Added Mocean Adapter for MoPub SDK.
- Added new Sample app for MoPub SDK to test Mocean adapter.
- IMPORTANT: Removed dependency for commons-collections library.
- Corresponding changes in MASTAdView class after removing above dependency.
- Return type of method getAdRequestCustomParameters() changed from MultiValueMap<String, String> to Map<String, List<String>>.
- Added convenience method for adding AdRequest custom parameters. (addAdRequestCustomParameter(String key, String value)).

------
 4.3.1
------

- RXM-499-23937 | Android Mocean viewableChange - Mraid 2.0.

------
 4.3.2
------

- YYB-245-58962 | Fixed bug for Android-id functionality.

------
 4.3.3
------

- NJE-810-93352 | Fixed broadcast-receiver unregistering bug.

------
 4.3.4
------

- Fixes to support AndroidAID & 3rd party pixel crash issues.


------
 4.3.5
------

- Adding support for latest Android 6.0 - Android-M (API level 23)
- Removed deprecated Apache HTTP Client API use in SDK. Upgraded to HTTPUrlConnection.
- Upgraded minimum Android SDK support from Android API level 8 to API level 9. 
- Added functionality to center align html script creatives to avoid left aligned creatives.
- Added Do-Not-Track feature in SDK.
- Added feature to fetch device id (AndroidId, Android AdvertisingId) in Native ads.


-------
 4.3.6
-------

- Added a fix for protocol independent ad creatives.
- Protocol independent creatives url's will now use the same protocol that of ad server base url (i.e. http:// or https://). 


-------
 5.0.0
-------

- PubMatic Andorid SDK is completely redesigned and re-structured.
- Same PubMatic SDK can now work with PubMatic as well as Mocean ad servers.
- Modularized SDK with separate modules for Banner and Native ad formats.
- User can choose to integrate either or all of the ad formats in application.


-------
 5.0.1
-------

- Users can now set update interval for auto reloading banner ads in SDK by using setUpdateInterval() method.


-------
 5.0.2
-------

- Support for Banner & Interstitial Ads via "Phoenix" - PubMatic ad server.


-------
 5.0.3
-------

- Fixed issue: Internal browser is displaying blank page in cases where landing page contains javascript. Enabled javascript in the WebView.

-------
 5.1.0
-------

- Handled the SSL error callback in case of any untrusted web page load inside the internal browser of SDK.

-------
 5.1.1
-------

- Published the SDK library to JitPack central repository for public distribution.