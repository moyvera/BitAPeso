package com.bitapeso.bitapeso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutDialog extends Dialog{

	private static Context mContext = null;
	
	public AboutDialog(Context context) {
		super(context);
		mContext = context;
	}
	
	/**
     * This is the standard Android on create method that gets called when the activity initialized.
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.about);
		
		TextView infoText = (TextView)findViewById(R.id.info_text);
		infoText.setText(Html.fromHtml(readRawTextFile(R.raw.info)));
		
		TextView legalText = (TextView)findViewById(R.id.legal_text);
		legalText.setText(Html.fromHtml(readRawTextFile(R.raw.legal)));
		
		infoText.setLinkTextColor(Color.parseColor("#F9A01B"));
		infoText.setClickable(true);
		infoText.setMovementMethod (LinkMovementMethod.getInstance());
		//Linkify.addLinks(infoText, Linkify.ALL);
	}
	
	public static String readRawTextFile(int id) {
		InputStream inputStream = mContext.getResources().openRawResource(id);
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buf = new BufferedReader(in);
        String line;
        StringBuilder text = new StringBuilder();
        try {
        	while (( line = buf.readLine()) != null) text.append(line);
         } catch (IOException e) {
            return null;
         }
         return text.toString();
     }

}
