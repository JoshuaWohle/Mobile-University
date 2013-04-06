package com.mobileuni.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.evernote.client.android.AsyncNoteStoreClient;
import com.evernote.client.android.EvernoteSession;
import com.evernote.edam.notestore.NoteFilter;
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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CourseNoteController extends Activity implements OnClickListener, CourseChangeListener {
	
	final int EVERNOTE_CREATED_NOTE = 1;
	final int EVERNOTE_VIEW_NOTE = 2;
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
		if(!Session.getEs().isLoggedIn() && AppStatus.isOnline()) { // If online && we're not logged-in, it means we need a new token
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
		spec.setIncludeTitle(true);
		spec.setIncludeCreated(true);
		if(ns == null) {
			try {
				ns = Session.getEs().getClientFactory().createNoteStoreClient();
			} catch (TTransportException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(AppStatus.isOnline())
			ns.findNotesMetadata(filter, 0, pageSize, spec, el);
		else
			notesChanged();
	}
	
	private void addNote(MetaNote note) {
		LinearLayout temp = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item, null);
		temp.setTag(note);
		temp.setOnClickListener(this);
		TextView title = (TextView) temp.findViewById(R.id.item_title);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		title.setText(note.getName() + " - Created: " + sdf.format(note.getDateCreated().getTime()));
		
		Button shareButton = new Button(this);
		shareButton.setText("Share");
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		shareButton.setTag("share_note-"+note.getEvernoteId());
		shareButton.setOnClickListener(this);
		
		((RelativeLayout) temp.findViewById(R.id.list_item_title_layout)).addView(shareButton, lp);
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
		} else if(v.getTag() instanceof MetaNote) {
			MetaNote note = (MetaNote) v.getTag();
			Intent intent = new Intent("com.evernote.action.VIEW_NOTE");
			intent.putExtra("NOTE_GUID", note.getEvernoteId());
			if(evernoteInstalled())
				startActivityForResult(intent, EVERNOTE_VIEW_NOTE);
			else {
				AlertDialog dialog = evernoteInstallDialog();
				dialog.show();
			}
		} else if(((String)v.getTag()).startsWith("share_note")) {
			String tag = (String)v.getTag();
			String evernoteId = tag.replaceAll("share_note-", "");
			ns.getNoteContent(evernoteId, el);
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
			if (resultCode == Activity.RESULT_OK)
				findRelatedNotes();
			break;
		default:
			break;
		}
	}

	public void courseContentsChanged() {
		//Nothing to do
	}

	public void fileChanged(String filePath) {
		//Nothing to do
	}

	public void notesChanged() {
		for(MetaNote note : Session.getCurrentSelectedCourse().getNotes()) {
			addNote(note);
		}
	}
}
