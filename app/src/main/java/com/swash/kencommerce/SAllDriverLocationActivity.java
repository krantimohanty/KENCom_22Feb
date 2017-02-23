/* 
 * This example demonstrates a good way to communicate between Activity and Service.
 * 
 * 1. Implement a service by inheriting from AbstractService
 * 2. Add a ServiceManager to your activity
 *   - Control the service with ServiceManager.start() and .stop()
 *   - Send messages to the service via ServiceManager.send() 
 *   - Receive messages with by passing a Handler in the constructor
 * 3. Send and receive messages on the service-side using send() and onReceiveMessage()
 * 
 * Author: Philipp C. Heckel; based on code by Lance Lefebure from
 *         http://stackoverflow.com/questions/4300291/example-communication-between-activity-and-service-using-messaging
 * Source: https://code.launchpad.net/~binwiederhier/+junk/android-service-example
 * Date:   6 Jun 2012
 */
package com.swash.kencommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.swash.kencommerce.LocationUtils.DataParser;
import com.swash.kencommerce.Utils.Constants;
import com.swash.kencommerce.Utils.Utils;

public class SAllDriverLocationActivity extends FragmentActivity {
	
	//GoogleMap mMap;

	//LatLng fromPosition = new LatLng(Double.parseDouble(Constants.strCurLat), Double.parseDouble(Constants.strCurLong));
	//LatLng toPosition = new LatLng(Double.parseDouble(Constants.strSelectedLat), Double.parseDouble(Constants.strSelectedLong));


	public static GoogleMap map;
	ImageView img_back;
	Utils utils;
	static float mapZoomLevel = 16;
	Timer timer;
	public Timer t = new Timer();

	MarkerOptions marker;
	MarkerOptions markerDriver;
	Marker mkrDriver;
	Marker mkrDestination;

	Marker mkr;
	int totCount = 0;
	int callCount = 0;

	LatLng origin, dest;
	LatLng DeliveryAddress;
	LatLng driverLocation;

	public static String str_address = "";
	public static Double destLat = 0.0;
	public static Double destLong = 0.0;

	ImageView img_email_us;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_track_in_map);
		utils=new Utils(this);

		initFields();
    }

	private void initFields() {
		//shPrefDeliverAddr = this.getSharedPreferences(Constants.strShPrefDelAddrName, Context.MODE_PRIVATE);
		img_back = (ImageView) findViewById(R.id.img_back);
		img_email_us=(ImageView)findViewById(R.id.img_email_us);
		img_email_us.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialogCoverage();
			}
		});

		// This should be initialized
		//map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		Constants.flagMapPage = true;
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		//map_hostel.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.getUiSettings().setAllGesturesEnabled(true);
		map.setMyLocationEnabled(true);
		map.setBuildingsEnabled(true);
		map.setTrafficEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);

		img_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		if (utils.isConnectionPossible()){
			//getDriverStartLocation();
			getDestination();
		}else{
			utils.displayAlert("Internet connection is not available, Turn it on and proceed.");
		}

		map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				if (mapZoomLevel > 14) {
					mapZoomLevel = cameraPosition.zoom;
					Log.d("zoom", String.valueOf(mapZoomLevel));
				}
				else {
					mapZoomLevel = 16;
					Log.d("zoom_new", String.valueOf(mapZoomLevel));
				}
			}
		});
	}

	private String getUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


		return url;
	}
	// Fetches data from url passed
	private class FetchUrl extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
				Log.d("Background Task data", data.toString());
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/**
	 * A class to parse the Google Places in JSON format
	 */
	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				//Log.d("ParserTask",jsonData[0].toString());
				DataParser parser = new DataParser();
				//Log.d("ParserTask", parser.toString());

				// Starts parsing data
				routes = parser.parse(jObject);
				//Log.d("ParserTask","Executing routes");
				//Log.d("ParserTask",routes.toString());

			} catch (Exception e) {
				//Log.d("ParserTask",e.toString());
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points;
			PolylineOptions lineOptions = null;

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(10);
				lineOptions.color(R.color.app_theme_blue);

				//Log.d("onPostExecute","onPostExecute lineoptions decoded");

			}

			// Drawing polyline in the Google Map for the i-th route
			if(lineOptions != null) {
				map.clear();
				map.addPolyline(lineOptions);
				CameraPosition cameraPosition = null;
				if (driverLocation!=null) {
					cameraPosition = new CameraPosition.Builder().target(driverLocation).zoom(mapZoomLevel).build();
				}
				else {
					cameraPosition = new CameraPosition.Builder().target(DeliveryAddress).zoom(mapZoomLevel).build();
				}
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

				Bitmap.Config conf = Bitmap.Config.ARGB_8888;
				Bitmap bmp = Bitmap.createBitmap(100, 120, conf);
				Canvas canvas1 = new Canvas(bmp);
				// paint defines the text color, stroke width and size
				Paint color = new Paint();
				color.setTextSize(16);
				// modify canvas
				canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.pin), 0, 0, color);
				//canvas1.drawText("Start", 20, 30, color);

				// add marker to Map
				map.addMarker(new MarkerOptions().position(driverLocation)
						.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pin",50,60)))
								// Specifies the anchor to be at a particular point in the marker image.
						.anchor(0.5f, 1));

				Bitmap.Config conf1 = Bitmap.Config.ARGB_8888;
				Bitmap bmp1 = Bitmap.createBitmap(100, 120, conf1);
				Canvas canvas11 = new Canvas(bmp1);
				// paint defines the text color, stroke width and size
				Paint color1 = new Paint();
				color1.setTextSize(16);
				// modify canvas
				canvas11.drawBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.icon_delivery_address), 0,0, color1);
				//canvas11.drawText("End", 20, 30, color);

				// add marker to Map
				/*map.addMarker(new MarkerOptions().position(DeliveryAddress)
						.icon(BitmapDescriptorFactory.fromBitmap(bmp1))
								// Specifies the anchor to be at a particular point in the marker image.
						.anchor(0.5f, 1));*/
				map.addMarker(new MarkerOptions().position(DeliveryAddress)
						.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_delivery_address",50,60)))
								// Specifies the anchor to be at a particular point in the marker image.
						.anchor(0.5f, 1));
			}
			else {
				Log.d("onPostExecute","without Polylines drawn");
			}
		}
	}

	/**
	 * A method to download json data from url
	 */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();
			Log.d("downloadUrl", data.toString());
			br.close();

		} catch (Exception e) {
			Log.d("Exception", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	public Bitmap resizeMapIcons(String iconName,int width, int height){
		Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
		return resizedBitmap;
	}

	private void getDestination() {
		//Showing the progress dialog
		final ProgressDialog pDialog = ProgressDialog.show(SAllDriverLocationActivity.this, "Loading...", "Please Wait...", false, false);
		StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.urlMainKenCommerce + Constants.urlGetDelData,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject jObject = new JSONObject(s);
							str_address = jObject.getString("address");
							destLat = Double.parseDouble(jObject.getString("latitude"));
							destLong = Double.parseDouble(jObject.getString("longitude"));
							DeliveryAddress = new LatLng(destLat, destLong);
							/*marker = new MarkerOptions().position(new LatLng(destLat, destLong))
									.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_delivery_address", 50, 60)));
							mkrDestination = map.addMarker(marker);*/
							t.scheduleAtFixedRate(new TimerTask() {
													  @Override
													  public void run() {
														  getDriverLocation();
													  }
												  }, 0,
									5000);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						finally {
							pDialog.dismiss();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						//Dismissing the progress dialog
						pDialog.dismiss();
						//Showing toast
//                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				//String image = getStringImage(bim);
				Map<String, String> params = new Hashtable<String, String>();
				return params;
			}
		};

		//Creating a Request Queue
		RequestQueue requestQueue = Volley.newRequestQueue(SAllDriverLocationActivity.this);

		//Adding request to the queue
		requestQueue.add(stringRequest);
	}

	private void getDriverLocation() {
		//Showing the progress dialog
		//final ProgressDialog pDialog = ProgressDialog.show(TrackInMapActivity.this, "Connecting...", "Please wait...", false, false);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.urlMain+ Constants.urlGetLocation,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject jObject = new JSONObject(s);
							JSONObject obj_result = jObject.getJSONObject("result");
							String resStat = obj_result.getString("error");
							if (resStat.equalsIgnoreCase("false")) {
								JSONObject objData = obj_result.getJSONObject("data");
								String res_latitude = objData.getString("latitude");
								String res_longitude = objData.getString("longitude");

								processResp(res_latitude, res_longitude);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						finally {
							//pDialog.dismiss();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						//Dismissing the progress dialog
						//pDialog.dismiss();

						//Showing toast
//                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				//String image = getStringImage(bim);
				Map<String, String> params = new Hashtable<String, String>();
				params.put("user_id", "190");
				return params;
			}
		};

		//Creating a Request Queue
		RequestQueue requestQueue = Volley.newRequestQueue(SAllDriverLocationActivity.this);

		//Adding request to the queue
		requestQueue.add(stringRequest);
	}

	private void processResp(String res_latitude, String res_longitude) {


		/*if (markerDriver!=null){
			mkrDriver.remove();
		}
		markerDriver = new MarkerOptions().position(new LatLng(Double.parseDouble(res_latitude), Double.parseDouble(res_longitude)))
				.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("map_icon_pin", 50, 60)));
		mkrDriver = map.addMarker(markerDriver);*/
		driverLocation = null;
		driverLocation = new LatLng(Double.parseDouble(res_latitude), Double.parseDouble(res_longitude));
		// Getting URL to the Google Directions API
		//String url = getUrl(origin, dest);
		String url = getUrl(new LatLng(Double.parseDouble(res_latitude), Double.parseDouble(res_longitude)), DeliveryAddress);
		//Log.d("onMapClick", url.toString());
		FetchUrl FetchUrl = new FetchUrl();
		// Start downloading json data from Google Directions API
		FetchUrl.execute(url);
	}
	private void getDialogCoverage() {
		final Dialog dialog = new Dialog(SAllDriverLocationActivity.this);
		Window window = dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog_age);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		TextView header=(TextView)dialog.findViewById(R.id.header);
		TextView msg=(TextView)dialog.findViewById(R.id.msg);
		Button btn_no=(Button)dialog.findViewById(R.id.btn_no);
		Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);
		btn_yes.setText("Ok");
		btn_no.setText("Cancel");

		header.setText("Stockup");
		msg.setText("Coming Soon");
		btn_no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btn_yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}
