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
import com.evernote.thrift.transport.TTransportException;
import com.mobileuni.controller.CourseNoteController;
import com.mobileuni.model.MetaNote;
import com.mobileuni.model.Session;
import com.mobileuni.other.Constants;

public class EvernoteListener extends OnClientCallback {
	
	Activity a;
	
	public EvernoteListener(Activity a) {
		this.a = a;
	}

	@Override
	public void onSuccess(Object data) {

		if(data instanceof NotesMetadataList) {
			ArrayList<MetaNote> notes = new ArrayList<MetaNote>();
			for(final NoteMetadata note : ((NotesMetadataList) data).getNotes()) {
				MetaNote mNote = new MetaNote();
				mNote.setName(note.getTitle());
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(note.getCreated());
				mNote.setDateCreated(cal);
				mNote.setEvernoteId(note.getGuid());
				notes.add(mNote);
				try {
					Session.getEs().getClientFactory().createNoteStoreClient().getNoteContent(note.getGuid(), new OnClientCallback<String>() {

						@Override
						public void onSuccess(String data) {
							Session.getCurrentSelectedCourse().getNoteFromEvernoteId(note.getGuid()).setContent(data);
							Log.d(Constants.LOG_NOTES, "Got note content");
						}

						@Override
						public void onException(Exception exception) {
							Log.d(Constants.LOG_NOTES, "Something went wrong whilst getting data of the note");
							
						}
						
					});
				} catch (TTransportException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Session.getCurrentSelectedCourse().setNotes(notes);
		} else if(data instanceof String) {
			Log.d(Constants.LOG_NOTES, (String) data);
		} else {
			Log.d(Constants.LOG_NOTES, data.getClass().getName());
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
				Log.d(Constants.LOG_NOTES, "Not allowed to access the notes");
				break;
			default:
				Log.d(Constants.LOG_NOTES, edam.toString());
				break;
			}
		}
		
		Log.d(Constants.LOG_NOTES, "Error, didn't get notes");
		e.printStackTrace();
	}

}
