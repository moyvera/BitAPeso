package com.bitapeso.bitapeso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	static TextWatcher inputTextWatcherBitcoin;
	static TextWatcher inputTextWatcherPeso;

	static EditText bitcoin;
	static EditText peso;

	static String url = "http://bitapeso.com/json/";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		TextView urlText = (TextView) findViewById(R.id.url);

		urlText.setText(Html
				.fromHtml("<a href=\"http://www.bitapeso.com\"> www.bitapeso.com </a> "));
		urlText.setMovementMethod(LinkMovementMethod.getInstance());
		Linkify.addLinks(urlText, Linkify.ALL);

		bitcoin = (EditText) findViewById(R.id.bitcoin);
		peso = (EditText) findViewById(R.id.peso);

		new LongOperation().execute(url);
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.about_us) {
			AboutDialog about = new AboutDialog(this);
			about.setTitle("Sobre Nosotros");
			about.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	private class LongOperation extends AsyncTask<String, Void, Void> {

		private final HttpClient Client = new DefaultHttpClient();
		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

		private float btc = 0;
		private float dolar = 0;
		private float mxn = 0;

		protected void onPreExecute() {
			Dialog.setMessage("Espera por favor...");
			Dialog.setCancelable(false);
			Dialog.show();
		}

		protected Void doInBackground(String... urls) {
			BufferedReader reader = null;

			try {
				URL url = new URL(urls[0]);
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				// Get the server response
				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					// Append server response in string
					sb.append(line + "");
				}
				// Append Server Response To Content String
				Content = sb.toString();
			} catch (Exception ex) {
				Error = ex.getMessage();
			} finally {
				try {
					reader.close();
				} catch (Exception ex) {
				}
			}
			return null;
		}

		protected void onPostExecute(Void unused) {
			if (Error != null) {
				// settext with error
				peso.setText("NaN");
				bitcoin.setText("NaN");
			} else {
				JSONObject jsonResponse;
				try {
					jsonResponse = new JSONObject(Content);

					this.btc = (float) jsonResponse.getDouble("btc");
					this.dolar = (float) jsonResponse.getDouble("dolar");
					this.mxn = (float) jsonResponse.getDouble("mxn");
					Toast.makeText(getApplicationContext(),
							"Valor actual bitcoin $" + this.mxn,
							Toast.LENGTH_SHORT).show();

					inputTextWatcherBitcoin = new TextWatcher() {
						public void afterTextChanged(Editable s) {
							peso.removeTextChangedListener(inputTextWatcherPeso);
							float realValue;
							if (s.toString().compareTo("") != 0) {
								realValue = (float) (Double.valueOf(s
										.toString().replace(",", ".")) * getMXN());
								DecimalFormat decimalFormat = new DecimalFormat(
										"0.########");
								peso.setText(decimalFormat.format(realValue)
										.replace(",", "."));
							} else
								peso.setText("");
							peso.addTextChangedListener(inputTextWatcherPeso);
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
						}
					};
					inputTextWatcherPeso = new TextWatcher() {
						public void afterTextChanged(Editable s) {
							bitcoin.removeTextChangedListener(inputTextWatcherBitcoin);
							float realValue;
							if (s.toString().compareTo("") != 0) {
								realValue = (float) (Float.valueOf(s.toString()
										.replace(",", ".")) / getMXN());
								DecimalFormat decimalFormat = new DecimalFormat(
										"0.#####");
								bitcoin.setText(decimalFormat.format(realValue)
										.toString().replace(",", "."));
							} else
								bitcoin.setText("");
							bitcoin.addTextChangedListener(inputTextWatcherBitcoin);
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
						}
					};

					bitcoin.addTextChangedListener(inputTextWatcherBitcoin);
					peso.addTextChangedListener(inputTextWatcherPeso);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			Dialog.dismiss();
		}

		public double getBTC() {
			return this.btc;
		}

		public double getMXN() {
			return this.mxn;
		}

		public double getDolar() {
			return this.dolar;
		}
	}

}
