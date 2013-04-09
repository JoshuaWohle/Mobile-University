package com.mobileuni.helpers;

import android.app.Activity;

import com.mobileuni.R;
import com.slidingmenu.lib.SlidingMenu;

public class MenuHelper {
	public static void setSlideMenu(Activity a) {
		SlidingMenu menu = new SlidingMenu(a);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setBehindOffsetRes(R.dimen.menu_offset_right);
		menu.attachToActivity(a, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu);
	}
}
