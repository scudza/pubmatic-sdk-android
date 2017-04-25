package com.pubmatic.sdk.common.pubmatic;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;

import com.pubmatic.sdk.common.AdRequest;
import com.pubmatic.sdk.common.AdvertisingIdClient;
import com.pubmatic.sdk.common.CommonConstants;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class PubMaticAdRequest extends AdRequest {

    public abstract void setAttributes(AttributeSet attr);

    protected PubMaticAdRequest(Context context) {
        super(CommonConstants.CHANNEL.PUBMATIC, context);
        mContext = context;

    }

    @Override
    public String getAdServerURL() {
        return TextUtils.isEmpty(mBaseUrl) ? CommonConstants.PUBMATIC_AD_NETWORK_URL : mBaseUrl;
    }

    @Override
    public boolean checkMandatoryParams() {
        return false;
    }

    @Override
    protected void initializeDefaultParams(Context context) {

    }

    @Override
    public void copyRequestParams(AdRequest adRequest) {
        if (adRequest != null && adRequest instanceof PubMaticAdRequest) {
            if (TextUtils.isEmpty(mAdId))
                this.mAdId = ((PubMaticAdRequest) adRequest).mAdId;
            if (TextUtils.isEmpty(mPubId))
                this.mPubId = ((PubMaticAdRequest) adRequest).mPubId;
            if (TextUtils.isEmpty(mSiteId))
                this.mSiteId = ((PubMaticAdRequest) adRequest).mSiteId;
            if (getWidth() <= 0)
                setWidth(adRequest.getWidth());
            if (getHeight() <= 0)
                setHeight(adRequest.getHeight());
        }
    }

    @Override
    protected void setupPostData() {

        super.setupPostData();

        //Append the basic & mandatory parameters
        putPostData(PubMaticConstants.PUB_ID_PARAM, mPubId);
        putPostData(PubMaticConstants.SITE_ID_PARAM, String.valueOf(this.mSiteId));
        putPostData(PubMaticConstants.AD_ID_PARAM, String.valueOf(this.mAdId));

        //Append custom parameters
        if(mCustomParams!=null && !mCustomParams.isEmpty()) {
            Set<String> set = mCustomParams.keySet();
            Iterator<String> iterator = set.iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                List<String> valueList = mCustomParams.get(key);
                for(String s : valueList) {
                    putPostData(key,s);
                }

            }
        }

        PUBDeviceInformation pubDeviceInformation = PUBDeviceInformation
                .getInstance(mContext);

        try {

            if (pubDeviceInformation.mDeviceAcceptLanguage != null) {
                // Appending did
                putPostData(PubMaticConstants.LANGUAGE, URLEncoder.encode(
                        pubDeviceInformation.mDeviceAcceptLanguage,
                        PubMaticConstants.URL_ENCODING));
            }
            // Setting country
            if (pubDeviceInformation.mDeviceCountryCode != null) {
                putPostData(PubMaticConstants.COUNTRY_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mDeviceCountryCode,
                        PubMaticConstants.URL_ENCODING));
            }

            // Setting carrier
            if (pubDeviceInformation.mCarrierName != null) {
                putPostData(PubMaticConstants.CARRIER_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mCarrierName,
                        PubMaticConstants.URL_ENCODING));
            }

            // Setting make
            if (pubDeviceInformation.mDeviceMake != null) {
                putPostData(PubMaticConstants.MAKE_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mDeviceMake,
                        PubMaticConstants.URL_ENCODING));
            }

            // Setting model
            if (pubDeviceInformation.mDeviceModel != null) {
                putPostData(PubMaticConstants.MODEL_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mDeviceModel,
                        PubMaticConstants.URL_ENCODING));
            }

            // Setting os
            if (pubDeviceInformation.mDeviceOSName != null) {
                putPostData(PubMaticConstants.OS_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mDeviceOSName,
                        PubMaticConstants.URL_ENCODING));
            }

            // Setting osv
            if (pubDeviceInformation.mDeviceOSVersion != null) {
                putPostData(PubMaticConstants.OSV_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mDeviceOSVersion,
                        PubMaticConstants.URL_ENCODING));
            }

            if (pubDeviceInformation.mApplicationName != null) {
                putPostData(PubMaticConstants.APP_NAME_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mApplicationName,
                        PubMaticConstants.URL_ENCODING));
            }

            if (pubDeviceInformation.mPackageName != null) {
                putPostData(PubMaticConstants.APP_BUNDLE_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mPackageName,
                        PubMaticConstants.URL_ENCODING));
            }

            if (pubDeviceInformation.mApplicationVersion != null) {
                putPostData(PubMaticConstants.APP_VERSION_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mApplicationVersion,
                        PubMaticConstants.URL_ENCODING));
            }

            if (pubDeviceInformation.mPageURL != null) {
                putPostData(PubMaticConstants.PAGE_URL_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mPageURL,
                        PubMaticConstants.URL_ENCODING));
            }

            // Setting js
            putPostData(PubMaticConstants.JS_PARAM, String.valueOf(PUBDeviceInformation.mJavaScriptSupport));
            putPostData(PubMaticConstants.IN_IFRAME_PARAM, String.valueOf(PUBDeviceInformation.mInIframe));
            putPostData(PubMaticConstants.AD_VISIBILITY_PARAM, String.valueOf(PUBDeviceInformation.mAdVisibility));
            putPostData(PubMaticConstants.AD_POSITION_PARAM, String.valueOf(PUBDeviceInformation.mAdPosition));
            putPostData(PubMaticConstants.APP_ID_PARAM, mAid);
            putPostData(PubMaticConstants.APP_CATEGORY_PARAM, mAppCategory);
            putPostData(PubMaticConstants.NETWORK_TYPE_PARAM, PubMaticUtils.getNetworkType(mContext));

            //Send Advertisement ID
            AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient.refreshAdvertisingInfo(mContext);
            if(adInfo!=null) {

                if (isAndoridAidEnabled() && !TextUtils.isEmpty(adInfo.getId())) {
                    putPostData(PubMaticConstants.UDID_PARAM, PubMaticUtils.sha1(adInfo.getId()));
                    putPostData(PubMaticConstants.UDID_TYPE_PARAM, String.valueOf(9));//9 - Android Advertising ID
                    putPostData(PubMaticConstants.UDID_HASH_PARAM, String.valueOf(0));//0 - raw udid
                }
			/*
			 * Pass dnt=1 if user have enabled Opt-Out of interest based ads in
			 * Google settings in Android device
			 */
                putPostData(PubMaticConstants.DNT_PARAM, String.valueOf(adInfo.isLimitAdTrackingEnabled() == true ? 1 : 0));
            } else if(mContext!=null){
                //Send Android ID
                putPostData(PubMaticConstants.UDID_PARAM, PubMaticUtils.getUdidFromContext(mContext));
                putPostData(PubMaticConstants.UDID_TYPE_PARAM, String.valueOf(3));//9 - Android ID
                putPostData(PubMaticConstants.UDID_HASH_PARAM, String.valueOf(2));//0 - SHA1
            }

            // Setting ver
            if (pubDeviceInformation.mApplicationVersion != null) {
                putPostData(PubMaticConstants.VER_PARAM, URLEncoder.encode(
                        pubDeviceInformation.mApplicationVersion,
                        PubMaticConstants.URL_ENCODING));
            }

            // Setting user specific information

            // Setting year of birth
            if (!TextUtils.isEmpty(mYearOfBirth)) {
                putPostData(PubMaticConstants.YOB_PARAM, URLEncoder.encode(
                        mYearOfBirth,
                        PubMaticConstants.URL_ENCODING));
            }

            // Setting Gender of user
            if (!TextUtils.isEmpty(mGender)) {
                putPostData(PubMaticConstants.GENDER_PARAM, URLEncoder.encode(
                        mGender, PubMaticConstants.URL_ENCODING));
            }

                // Setting zip code of user
            if (!TextUtils.isEmpty(mZip)) {
                putPostData(PubMaticConstants.ZIP_PARAM, URLEncoder.encode(
                        mZip, PubMaticConstants.URL_ENCODING));
            }

                // Setting the income
            if (!TextUtils.isEmpty(mIncome)) {
                putPostData(PubMaticConstants.USER_INCOME, URLEncoder.encode(
                        mIncome,
                        PubMaticConstants.URL_ENCODING));
            }

                // Setting the user entnicity
            if (!TextUtils.isEmpty(mEthnicity)) {
                putPostData(PubMaticConstants.USER_ETHNICITY, URLEncoder.encode(
                        mEthnicity,
                        PubMaticConstants.URL_ENCODING));
            }

            if (mKeywordsList!=null) {
                putPostData(PubMaticConstants.KEYWORDS_PARAM, URLEncoder.encode(
                        getKeywordString(),
                        PubMaticConstants.URL_ENCODING));
            }


            // Setting user city
            if (!TextUtils.isEmpty(mCity)) {
                putPostData(PubMaticConstants.USER_CITY, URLEncoder.encode(
                        mCity,
                        PubMaticConstants.URL_ENCODING));
            }

            // Setting the state
            if (!TextUtils.isEmpty(mState)) {
                putPostData(PubMaticConstants.USER_STATE, URLEncoder.encode(
                        mState,
                        PubMaticConstants.URL_ENCODING));
            }


            // Setting adOrientation
            if(!TextUtils.isEmpty(mAdOrientation))
             putPostData(PubMaticConstants.AD_ORIENTATION_PARAM, mAdOrientation);

            // Setting deviceOrientation
            putPostData(PubMaticConstants.DEVICE_ORIENTATION_PARAM, String.valueOf(getDeviceOrientation(mContext)));

            // Setting adRefreshRate
            putPostData(PubMaticConstants.AD_REFRESH_RATE_PARAM, String.valueOf(mAdRefreshRate));

            // Setting sdk_id
            putPostData(PubMaticConstants.SDK_ID_PARAM, URLEncoder
                    .encode(PUBDeviceInformation.msdkId,
                            PubMaticConstants.URL_ENCODING));

            // Setting sdk_ver
            putPostData(PubMaticConstants.SDK_VER_PARAM, URLEncoder.encode(
                    PUBDeviceInformation.msdkVersion,
                    PubMaticConstants.URL_ENCODING));

            if (mNetworkType != null) {
                putPostData(PubMaticConstants.NETWORK_TYPE_PARAM, URLEncoder.encode(mNetworkType,
                        PubMaticConstants.URL_ENCODING));
            }

            putPostData(PubMaticConstants.DNT_PARAM, String.valueOf(mDoNotTrack ? 1 : 0));
            putPostData(PubMaticConstants.COPPA_PARAM, String.valueOf(mCoppa ? 1 : 0));
            putPostData(PubMaticConstants.STORE_URL_PARAM, mStoreURL);
            putPostData(PubMaticConstants.PAID_PARAM, String.valueOf(mPaid ? 1 : 0));
            putPostData(PubMaticConstants.APP_DOMAIN_PARAM, mAppDomain);

            //Set the awt parameter
            if (mAWT != null) {
                switch (mAWT) {
                    case WRAPPED_IN_IFRAME:
                        putPostData(PubMaticConstants.AWT_PARAM, String.valueOf(1));
                        break;
                    case WRAPPED_IN_JS:
                        putPostData(PubMaticConstants.AWT_PARAM, String.valueOf(2));
                        break;
                }
            }

            //Set the location
            if (mLocation != null) {
                putPostData(PubMaticConstants.LOC_PARAM, mLocation.getLatitude() + ","
                        + mLocation.getLongitude());

                putPostData(PubMaticConstants.LOC_SOURCE_PARAM, URLEncoder.encode(
                        mLocation.getProvider()));
            }


        } catch (Exception e) {

        }
    }

    @Override
    public void createRequest(Context context) {
        mPostData		= null;
        initializeDefaultParams(context);
        setupPostData();
    }

    //PubMatic specific enums
    public enum AD_TYPE { TEXT, IMAGE, IMAGE_TEXT, RICHMEDIA, NATIVE, VIDEO, AUDIO }

    public enum OVER18 { DEFAULT_DENY, DENY, ONLY_OVER18, ALLOW_ALL }

    public enum OPERID { HTML, JAVA_SCRIPT, JSON, JSON_MOBILE }

    public enum RS { PURE_JSON, JSON_CALLBACK, JS_VAR}

    public enum AWT_OPTION { DEFAULT, WRAPPED_IN_IFRAME, WRAPPED_IN_JS }

    public enum AD_VISIBILITY { CAN_NOT_DETERMINE, ABOVE_FOLD, BELOW_FOLD, PARTIAL }

    public enum LOCATION_SOURCE { UNKOWN, GPS_OR_SERVICES, IP, UCER_PROVIDED }

    public enum ETHNICITY { HISPANIC, AFRICAN_AMERICAN, CAUCASIAN, ASIAN_AMERICAN, OTHER }

    //Mocean specific enums
    public enum FORMAT_KEY { HTML, XML, JSON, JSONP, GENERIC, VAST, DAAST, OFFLINE_XML }

    protected Context         mContext;
    protected String          mPubId;
    protected String          mSiteId;
    protected String          mAdId;
    protected RS              mRs;
    protected AD_TYPE         mAdType;
    protected int             mAdHeight;
    protected int             mAdWidth;
    protected boolean         mInIFrame;
    protected String          mAdNetwork;
    protected AD_VISIBILITY   mAdVisibility;
    protected String          mIABCategory;
    protected String          mPMZoneId;
    protected String          mAppName;
    protected String          mStoreURL;
    protected String          mAid;
    protected String          mAppCategory;
    protected String          mAppDomain;
    protected boolean         mPaid;
    protected int             mAdRefreshRate;
    protected int             mOrmmaComplianceLevel;
    protected String          mAdOrientation;
    protected String          mLanguage;
    protected String 		  mNetworkType;
    private boolean			  mDoNotTrack;
    private boolean			  mCoppa;
    private PubMaticAdRequest.AWT_OPTION mAWT;


    //Common for Mocean & PubMatic User info params
    private String mCity = null;
    private String mZip = null;
    private String mDMA = null;
    private String mEthnicity = null;
    private String mGender = null;
    //PubMatic User info
    private String mCountry = null;
    private String mState = null;
    private String mYearOfBirth = null;
    private String mIncome = null;
    private ArrayList<String> mKeywordsList = null;

    /**
     * Set the zip of the user.
     *
     * @param zip
     *            zip of the user
     */
    public void setZip(final String zip) {
        mZip = zip;
    }

    public String getDMA() {
        return mDMA;
    }

    public void setDMA(String mDMA) {
        this.mDMA = mDMA;
    }

    /**
     *
     * @return
     */
    public String getCity() {
        return mCity;
    }

    /**
     *
     * @param city
     */
    public void setCity(String city) {
        this.mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    /**
     *
     * @return
     */
    public String getState() {
        return mState;
    }

    /**
     *
     * @param state
     */
    public void setState(String state) {
        this.mState = state;
    }

    /**
     * Set the year of birth of the user.
     *
     * @param yearOfBirth
     *            - yearOfBirth of the user
     */
    public void setYearOfBirth(final String yearOfBirth) {
        mYearOfBirth = yearOfBirth;
    }
    /**
     * Sets the user income value
     *
     * @param income
     *            Sets the user income value
     */
    public void setIncome(final String income)
    {
        mIncome = income;
    }

    /**
     * Sets the ethnicity  of the user.
     *
     * @param ethnicity
     *            User ethnicity
     */
    public void setEthnicity(final String ethnicity)
    {
        mEthnicity = ethnicity;
    }


    /**
     * Add the new keyword that the user might be interested in.
     *
     * @param keyword
     *            the new keyword to be added to the keywords list
     */
    public void addKeyword(final String keyword) {
        if (mKeywordsList == null) {
            mKeywordsList = new ArrayList<String>();
        }

        mKeywordsList.add(keyword);
    }

    /**
     * Returns the year of birth of the user.
     *
     * @return the yearOfBirth
     */
    public String getYearOfBirth() {
        return mYearOfBirth;
    }

    /**
     * Returns the income  of the user.
     *
     * @return the income of user
     */
    public String getIncome()
    {
        return mIncome;
    }

    /**
     * Returns the keywords list in the form of comma separated String. e.g.
     * Cricket,Pizza
     */
    public String getKeywordString() {

        if (mKeywordsList != null && !mKeywordsList.isEmpty()) {
            StringBuffer keywordStringBuffer = null;

            for (String keyword : mKeywordsList) {
                if (keywordStringBuffer == null) {
                    keywordStringBuffer = new StringBuffer(keyword);
                } else {
                    keywordStringBuffer.append("," + keyword);
                }
            }
            return keywordStringBuffer.toString();
        }

        return null;
    }
    /**
     *
     * @return
     */
    public String getPubId() {
        return mPubId;
    }

    /**
     *
     * @return
     */
    public String getSiteId() {
        return mSiteId;
    }

    /**
     *
     * @return
     */
    public String getAdId() {
        return mAdId;
    }

    /**
     * AdTagId is associated with the Banner Ad Size. You have to create the
     * AdTagId for recommended size.
     *
     * Before loading the Ad's you have to set the respective AdTagId for given
     * Banner.
     *
     * @param adTagId
     *            adTagId to set
     */
    public void setAdId(String adTagId) {
        mAdId = adTagId;
    }

    /**
     * You have to set the site id before making call to loadAd.
     *
     * @param siteId
     *            siteId to set
     */
    public void setSiteId(String siteId) {
        mSiteId = siteId;
    }

    /**
     * You have to set the Publisher id before making a call to loadAd.
     *
     * @param publisherId
     *            publisherId The publisher id provided by PubMatic.
     */
    public void setPubId(String publisherId) {
        mPubId = publisherId;
    }

    public RS getRs() {
        return mRs;
    }

    public void setRs(RS mRs) {
        this.mRs = mRs;
    }

    public AD_TYPE getAdType() {
        return mAdType;
    }

    public void setAdType(AD_TYPE mAdType) {
        this.mAdType = mAdType;
    }

    public int getAdHeight() {
        return mAdHeight;
    }

    public void setAdHeight(int mAdHeight) {
        this.mAdHeight = mAdHeight;
    }

    public int getAdWidth() {
        return mAdWidth;
    }

    public void setAdWidth(int mAdWidth) {
        this.mAdWidth = mAdWidth;
    }

    public boolean isInIFrame() {
        return mInIFrame;
    }

    public void setInIFrame(boolean mInIFrame) {
        this.mInIFrame = mInIFrame;
    }

    public String getAdNetwork() {
        return mAdNetwork;
    }

    public void setAdNetwork(String mAdNetwork) {
        this.mAdNetwork = mAdNetwork;
    }

    public AD_VISIBILITY getAdVisibility() {
        return mAdVisibility;
    }

    public void setAdVisibility(AD_VISIBILITY mAdVisibility) {
        this.mAdVisibility = mAdVisibility;
    }

    public String getIABCategory() {
        return mIABCategory;
    }

    public void setIABCategory(String mIABCategory) {
        this.mIABCategory = mIABCategory;
    }

    public boolean isDoNotTrack() {
        return mDoNotTrack;
    }

    public void setDoNotTrack(boolean mDoNotTrack) {
        this.mDoNotTrack = mDoNotTrack;
    }

    public boolean isCoppa() {
        return mCoppa;
    }

    public void setCoppa(boolean mCoppa) {
        this.mCoppa = mCoppa;
    }

    public AWT_OPTION getAWT() {
        return mAWT;
    }

    public void setAWT(AWT_OPTION mAWT) {
        this.mAWT = mAWT;
    }

    public String getPMZoneId() {
        return mPMZoneId;
    }

    public void setPMZoneId(String mPMZoneId) {
        this.mPMZoneId = mPMZoneId;
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public String getStoreURL() {
        return mStoreURL;
    }

    public void setStoreURL(String mStoreURL) {
        this.mStoreURL = mStoreURL;
    }

    public String getAid() {
        return mAid;
    }

    public void setAid(String mAid) {
        this.mAid = mAid;
    }

    public String getAppDomain() {
        return mAppDomain;
    }

    public void setAppDomain(String mAppDomain) {
        this.mAppDomain = mAppDomain;
    }

    public String getAppCategory() {
        return mAppCategory;
    }

    public void setAppCategory(String mAppCategory) {
        this.mAppCategory = mAppCategory;
    }

    public void isApplicationPaid(boolean mPaid) {
        this.mPaid = mPaid;
    }

    public int getAdRefreshRate() {
        return mAdRefreshRate;
    }

    public void setAdRefreshRate(int mAdRefreshRate) {
        this.mAdRefreshRate = mAdRefreshRate;
    }

    public int getOrmmaComplianceLevel() {
        return mOrmmaComplianceLevel;
    }

    public void setOrmmaComplianceLevel(int mOrmmaComplianceLevel) {
        this.mOrmmaComplianceLevel = mOrmmaComplianceLevel;
    }

    public String getAdOrientation() {
        return mAdOrientation;
    }

    public void setAdOrientation(String mAdOrientation) {
        this.mAdOrientation = mAdOrientation;
    }

    public int getDeviceOrientation(Context context) {
        int rotation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
            return 0;
        return 1;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }


    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

}
