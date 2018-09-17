package com.novatek.welfarevendor;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.method.ScrollingMovementMethod;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class VendorInformationUI {
	
	public Activity activity;

	private JavaScriptInterface JSInterface;
	
	private TextView VendorName;
	private TextView VendorPhone;
	private TextView VendorAddress;
	private TextView VendorDetail;

	private WebView  VendorMap;
    private Geocoder Coder;
    private List<Address> CoderAddress;
    private Address CoderLocation;
	
	public VendorInformationUI(Activity _activity) {
		this.activity = _activity;

		// Initial JS interface for google map v3
		JSInterface = new JavaScriptInterface();
        Coder = new Geocoder(this.activity);

        VendorMap = (WebView)this.activity.findViewById(R.id.vendor_map);
	}

	public void SetVendorInformationUI(String name, String area, String phone, String address, String detail) {
        this.activity.setContentView(R.layout.ui_vendorinformation);

		// find textview from MainActivity
		VendorName = (TextView) this.activity.findViewById(R.id.vendor_name);
		VendorPhone = (TextView) this.activity.findViewById(R.id.vendor_phone);
		VendorAddress = (TextView) this.activity.findViewById(R.id.vendor_address);
		VendorDetail = (TextView) this.activity.findViewById(R.id.vendor_detail);

		if (isConnected()) {
            VendorMap = (WebView)this.activity.findViewById(R.id.vendor_map);
            VendorMap.getSettings().setJavaScriptEnabled(true);
            VendorMap.addJavascriptInterface(JSInterface, "android");

            // transfer address to lat/lng
            try {
                CoderAddress = Coder.getFromLocationName(address, 2);
                CoderLocation = CoderAddress.get(0);
                JSInterface.setLatitude(CoderLocation.getLatitude());
                JSInterface.setLongitude(CoderLocation.getLongitude());
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            // load google map
            VendorMap.loadUrl("file:///android_asset/GoogleMap.html");
        }
        else {
            JSInterface.setLatitude(24.771588);
            JSInterface.setLongitude(121.025196);

            Toast toast = Toast.makeText(this.activity,
                    R.string.no_internet, Toast.LENGTH_LONG);
            toast.show();
        }

		VendorName.setText(name);
		VendorPhone.setText(phone);
		VendorAddress.setText(address);
		VendorDetail.setText(detail);

        VendorDetail.setMovementMethod(new ScrollingMovementMethod());
	}
	
	public void SetVendorCategoryUI(String category) {
		this.activity.setContentView(R.layout.ui_vendorinformation);
	}

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private class JavaScriptInterface {
        private double latitude,longitude;

        // default address: Novatek
        public JavaScriptInterface() {
            latitude = 24.771588;
            longitude = 121.025196;
        }

        public void setLatitude(double lat) {
            latitude = lat;
        }

        public void setLongitude(double lng) {
            longitude = lng;
        }

	    @JavascriptInterface
        public double getLatitude() {
            return latitude;
        }
        @JavascriptInterface
        public double getLongitude() {
            return longitude;
        }
    };

}
