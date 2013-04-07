package com.mobileuni.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.mobileuni.R;
import com.mobileuni.other.Session;

public class DialogHelper {
	public static AlertDialog evernoteInstallDialog(final Activity a) {
		// If the intent was not found, it means the app was not installed, so just redirect the user to install Evernote
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setMessage(R.string.install_evernote)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.evernote"));
				a.startActivity(browserIntent);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		return builder.create();
	}
	
	public static AlertDialog evernoteAuthenticateDialog(final Activity a) {
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setMessage(R.string.authenticate_evernote)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Session.getEs().authenticate(Session.getContext());
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		return builder.create();
	}
}
