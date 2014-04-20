package com.bitapeso.bitapeso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.w3c.dom.Text;

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
		TextView legalText = (TextView)findViewById(R.id.legal_text);
		TextView extraInfoText = (TextView) findViewById(R.id.info_text_patch);
		
		infoText.setText(Html.fromHtml(readRawTextFile(R.raw.info)));
		legalText.setText(Html.fromHtml(readRawTextFile(R.raw.legal)));
		extraInfoText.setText(Html.fromHtml("<a href=\"http://www.cesar.sx\">www.cesar.sx</a><br /><br/>"));
		
		infoText.setLinkTextColor(Color.parseColor("#F9A01B"));
		extraInfoText.setLinkTextColor(Color.parseColor("#F9A01B"));
		
		infoText.setMovementMethod (LinkMovementMethod.getInstance());
		legalText.setMovementMethod(LinkMovementMethod.getInstance());
		extraInfoText.setMovementMethod(LinkMovementMethod.getInstance());
		
		Linkify.addLinks(infoText, Linkify.ALL);
		Linkify.addLinks(legalText, Linkify.ALL);
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
