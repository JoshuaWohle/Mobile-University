package com.mobileuni.listeners;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.util.Log;

import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.error.EDAMErrorCode;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.notestore.NotesMetadataList;
import com.mobileuni.controller.CourseNoteController;
import com.mobileuni.model.MetaNote;
import com.mobileuni.other.Session;

public class EvernoteListener extends OnClientCallback {
	
	Activity a;
	
	public EvernoteListener(Activity a) {
		this.a = a;
	}

	@Override
	public void onSuccess(Object data) {

		if(data instanceof NotesMetadataList) {
			ArrayList<MetaNote> notes = new ArrayList<MetaNote>();
			for(NoteMetadata note : ((NotesMetadataList) data).getNotes()) {
				MetaNote mNote = new MetaNote();
				mNote.setName(note.getTitle());
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(note.getCreated());
				mNote.setDateCreated(cal);
				mNote.setEvernoteId(note.getGuid());
				notes.add(mNote);
			}
			Session.getCurrentSelectedCourse().setNotes(notes);
		}
	}

	@Override
	public void onException(Exception e) {
		if(e.getCause() instanceof EDAMUserException) {
			EDAMUserException edam = (EDAMUserException) e.getCause();
			EDAMErrorCode code = edam.getErrorCode();
			switch(code) {
			case AUTH_EXPIRED:
				((CourseNoteController) a).evernoteAuthenticate();
				break;
			case PERMISSION_DENIED:
				Log.d("Notes", "Not allowed to access the notes");
				break;
			default:
				Log.d("Notes", edam.toString());
				break;
			}
		}
		
		Log.d("Notes", "Error, didn't get notes");
		e.printStackTrace();
	}

}
