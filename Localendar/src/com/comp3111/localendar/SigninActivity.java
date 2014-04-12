package com.comp3111.localendar;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.auth.GoogleAuthUtil;

public class SigninActivity extends Activity {
	private AccountManager mAccountManager;
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Settings");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
	    onBackPressed();
	    overridePendingTransition(R.anim.left_in, R.anim.left_out);
	    return true;
	}
	/*
	 * this function may be used in optional function
	private String[] getAccountNames() {
	    mAccountManager = AccountManager.get(this);
	    Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
	    String[] names = new String[accounts.length];
	    for (int i = 0; i < names.length; i++) {
	        names[i] = accounts[i].name;
	    }
	    return names;
	}
	*/
}
