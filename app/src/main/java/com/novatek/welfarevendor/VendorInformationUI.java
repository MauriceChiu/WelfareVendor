package com.novatek.welfarevendor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class VendorInformationUI {
	
	public Activity activity;

	private TextView VendorName;
	private TextView VendorPhone;
	private TextView VendorAddress;
	private TextView VendorDetail;

    private ImageView VendorMap;

	private String Map_VendorName, Map_VendorAddress;

    private Geocoder Coder;
    private List<Address> CoderAddress;
    private Address CoderLocation;

	public VendorInformationUI(Activity _activity) {
		this.activity = _activity;

        Coder = new Geocoder(this.activity);
	}

	public void SetVendorInformationUI(String name, String area, String phone, String address, String detail) {
        this.activity.setContentView(R.layout.ui_vendorinformation);

        Map_VendorName = name;
        Map_VendorAddress = address;

		// find textview from MainActivity
		VendorName = (TextView) this.activity.findViewById(R.id.vendor_name);
		VendorPhone = (TextView) this.activity.findViewById(R.id.vendor_phone);
		VendorAddress = (TextView) this.activity.findViewById(R.id.vendor_address);
		VendorDetail = (TextView) this.activity.findViewById(R.id.vendor_detail);

		VendorName.setText(name);
		VendorPhone.setText(phone);
		VendorAddress.setText(address);
		VendorDetail.setText(detail);

        VendorMap = (ImageView) this.activity.findViewById(R.id.vendor_map);
        Display display = this.activity.getWindowManager().getDefaultDisplay();
        VendorMap.getLayoutParams().width  = display.getWidth() / 5;
        VendorMap.getLayoutParams().height = VendorMap.getLayoutParams().width;
        VendorMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // transfer address to lat/lng
                try {
                    CoderAddress = Coder.getFromLocationName(Map_VendorAddress, 2);
                    CoderLocation = CoderAddress.get(0);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }

                String uri = "http://maps.google.com/maps?q=loc:" + CoderLocation.getLatitude() + "," + CoderLocation.getLongitude() + " (" + Map_VendorName + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                activity.startActivity(intent);
            }
        });

        VendorDetail.setMovementMethod(new ScrollingMovementMethod());
    }
	
	public void SetVendorCategoryUI(String category) {
		this.activity.setContentView(R.layout.ui_vendorinformation);
	}

    private boolean isPackageExisted(String targetPackage){
        PackageManager pm = this.activity.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }
}
