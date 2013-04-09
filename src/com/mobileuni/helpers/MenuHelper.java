package com.mobileuni.helpers;

import android.app.Activity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mobileuni.R;
import com.mobileuni.listeners.MenuListener;
import com.slidingmenu.lib.SlidingMenu;

public class MenuHelper {
	public static void setSlideMenu(Activity a) {
		MenuListener ml = new MenuListener(a);
		SlidingMenu menu = new SlidingMenu(a);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setBehindOffsetRes(R.dimen.menu_offset_right);
		menu.attachToActivity(a, SlidingMenu.SLIDING_CONTENT);
		LinearLayout menuView = (LinearLayout) a.getLayoutInflater().inflate(R.layout.menu, null);
		((Button) menuView.findViewById(R.id.menu_select_course)).setOnClickListener(ml);
		((Button) menuView.findViewById(R.id.menu_upload_document)).setOnClickListener(ml);
		((Button) menuView.findViewById(R.id.menu_settings)).setOnClickListener(ml);
		((Button) menuView.findViewById(R.id.menu_selected_course)).setOnClickListener(ml);
		menu.setMenu(menuView);
	}
}
