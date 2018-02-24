package com.example.administrator.iteam_fragment.zoom;

import android.annotation.TargetApi;
import android.view.View;

@SuppressWarnings("ALL")
@TargetApi(16)
public class SDK16 {

	public static void postOnAnimation(View view, Runnable r) {
		view.postOnAnimation(r);
	}
	
}
