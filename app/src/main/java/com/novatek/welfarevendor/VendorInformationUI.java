package com.novatek.welfarevendor;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class VendorInformationUI {
	
	public Activity activity;
	
	private TextView VendorName;
	private TextView VendorPhone;
	private TextView VendorAddress;
	private TextView VendorDetail;
	
	public VendorInformationUI(Activity _activity) {
		this.activity = _activity;		
	}
	
	public void SetVendorInformationUI(String name, String area, String phone, String address, String detail) {
		this.activity.setContentView(R.layout.ui_vendorinformation);
		//MainTitle = (TextView) this.activity.findViewById(R.id.Registration_Title);
		VendorName = (TextView) this.activity.findViewById(R.id.vendor_name);
		VendorPhone = (TextView) this.activity.findViewById(R.id.vendor_phone);
		VendorAddress = (TextView) this.activity.findViewById(R.id.vendor_address);
		VendorDetail = (TextView) this.activity.findViewById(R.id.vendor_detail);
				
		VendorName.setText(name);
		VendorPhone.setText(phone);
		VendorAddress.setText(address);
		VendorDetail.setText(detail);
	}
	
	public void SetVendorCategoryUI(String category) {
		this.activity.setContentView(R.layout.ui_vendorinformation);
	}

}
