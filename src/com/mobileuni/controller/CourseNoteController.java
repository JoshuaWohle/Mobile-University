package com.mobileuni.controller;

import java.util.ArrayList;
import java.util.List;

import com.evernote.client.android.AsyncNoteStoreClient;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.error.EDAMErrorCode;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.thrift.transport.TTransportException;
import com.mobileuni.R;
import com.mobileuni.helpers.AppStatus;
import com.mobileuni.listeners.CourseChangeListener;
import com.mobileuni.listeners.EvernoteListener;
import com.mobileuni.model.MetaNote;
import com.mobileuni.other.Session;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseNoteController extends Activity implements OnClickListener, CourseChangeListener {
	
	final int EVERNOTE_CREATED_NOTE = 1;
	AsyncNoteStoreClient ns;
	EvernoteListener el = new EvernoteListener(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list);
		Session.setContext(this);
		Session.getCurrentSelectedCourse().addListener(this);
		
		((TextView) findViewById(R.id.title)).setText(Session.getContext()
				.getResources().getString(R.string.notes_title));
		createAddNoteButton();
		if(Session.getEs().isLoggedIn()) {
			Log.d("Note", "Logged in to evernote");
		} else if(AppStatus.isOnline()) { // If online && we're not logged-in, it means we need a new token
			AlertDialog dialog = evernoteAuthenticateDialog();
			dialog.show();
		}
		findRelatedNotes();
	}

	public void createAddNoteButton() {
		LinearLayout list = (LinearLayout) findViewById(R.id.item_list);
		Button addNoteButton = new Button(this);
		addNoteButton.setText(R.string.note_add);
		addNoteButton.setWidth(LayoutParams.MATCH_PARENT);
		addNoteButton.setHeight(LayoutParams.WRAP_CONTENT);
		addNoteButton.setTag("add_note");
		addNoteButton.setOnClickListener(this);
		list.addView(addNoteButton);
	}
	
	private void findRelatedNotes() {
		int pageSize = 10;
		NoteFilter filter = new NoteFilter();
		List<String> tags = new ArrayList<String>();
		tags.add(Session.getCurrentSelectedCourse().getShortName());
		filter.setWords(Session.getCurrentSelectedCourse().getShortName());
		NotesMetadataResultSpec spec = new NotesMetadataResultSpec();
		if(ns == null) {
			try {
				ns = Session.getEs().getClientFactory().createNoteStoreClient();
			} catch (TTransportException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		ns.findNotesMetadata(filter, 0, pageSize, spec, el);
	}
	
	private void addNote(MetaNote note) {
		LinearLayout temp = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item, null);
		((LinearLayout) findViewById(R.id.item_list)).addView(temp);
	}
	
	public void evernoteAuthenticate() {
		Session.getEs().authenticate(this);
	}
	
	/**
	 * Checks if Evernote is installed
	 * @return
	 */
	private boolean evernoteInstalled() {
		try{
		    ApplicationInfo info = getPackageManager().
		            getApplicationInfo("com.evernote", 0 );
		    return true;
		} catch( PackageManager.NameNotFoundException e ){
		    return false;
		}
	}

	public void onClick(View v) {

		if (v.getTag().equals("add_note")) {
			Intent intent = new Intent("com.evernote.action.CREATE_NEW_NOTE");
			ArrayList<String> tags = new ArrayList<String>();
			tags.add("Mobile University");
			tags.add(Session.getCurrentSelectedCourse().getShortName());
			tags.add(Session.getCurrentSelectedCourse().getIdNumber());
			intent.putExtra("TAG_NAME_LIST", tags);
			if(evernoteInstalled())
				startActivityForResult(intent, EVERNOTE_CREATED_NOTE);
			else {
				AlertDialog dialog = evernoteInstallDialog();
				dialog.show();
			}
		}

	}
	
	private AlertDialog evernoteInstallDialog() {
		// If the intent was not found, it means the app was not installed, so just redirect the user to install Evernote
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.install_evernote)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.evernote"));
				startActivity(browserIntent);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		return builder.create();
	}
	
	private AlertDialog evernoteAuthenticateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		// Update UI when oauth activity returns result
		case EvernoteSession.REQUEST_CODE_OAUTH:
			if (resultCode == Activity.RESULT_OK)
				findRelatedNotes();
			break;
		case EVERNOTE_CREATED_NOTE:
			if (resultCode == Activity.RESULT_OK) {
				Log.d("Note", "Successfully created note");
			}
			break;
		default:
			break;
		}
	}

	public void courseContentsChanged() {
		// TODO Auto-generated method stub
		
	}

	public void fileChanged(String filePath) {
		// TODO Auto-generated method stub
		
	}

	public void notesChanged() {
		for(MetaNote note : Session.getCurrentSelectedCourse().getNotes()) {
			addNote(note);
		}
	}
}
