/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author CeroneFedora
 * @version 1.0, Aug 14, 2015
 */
public class GPSTracker extends Service implements LocationListener {

	private final Context mContext;
	boolean isGPSEnabled = false;
	boolean canGetLocation = false;
	boolean isNetworkEnabled = false;
	Location location;
	double latitude;
	GoogleMap googleMap;
	String completeAddress = "";
	String fullAddress, city, latitude1, longitude1, state, country,
			postalCode;
	String totalAddress;

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	double longitude;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private static final long MIN_TIME_BW_UPDATES = 5000;
	protected LocationManager locationManager;

	public GPSTracker(Context context) {
		mContext = context;
		getLocation();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!isGPSEnabled && !isNetworkEnabled) {
				//showSettingsAlert();
			} else {
				canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				if (isGPSEnabled) {
					if (location == null) {
						//if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						if (ActivityCompat.checkSelfPermission((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
							ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
							return location;
						}

						//}
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();

							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

	public String getCoordinatesByLocationName(String locationName) {
		String result = "";
		if (locationName != null && locationName.length() > 0) {
			if (canGetLocation()) {
				latitude = 0;
				longitude = 0;
				Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
				try {
					List<Address> addressesList = geocoder.getFromLocationName(
							locationName, 1);
					if (!addressesList.isEmpty()) {
						Address addressDetails = addressesList.get(0);
						latitude = addressDetails.getLatitude();
						longitude = addressDetails.getLongitude();
						result = addressDetails.getLatitude() + ","
								+ addressDetails.getLongitude();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				//showSettingsAlert();
			}
		}
		System.out.println("Location name " + locationName + " latitude "
				+ latitude + " longitude " + longitude);
		return result;
	}

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * app
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	public double getSearchLocationLatitude() {
		return latitude;
	}

	public double getSearchLocationLongitude() {
		return longitude;
	}

	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}
		return latitude;
	}

	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}
		return longitude;
	}

	public boolean canGetLocation() {
		return canGetLocation;
	}

	public void showSettingsAlert() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
				alertDialog.setTitle("GPS is settings");
				alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
				alertDialog.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								mContext.startActivity(intent);
								dialog.cancel();
							}
						});
				alertDialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				alertDialog.show();
			}
		}, 100);
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public String getLocationAddress() {
		getLocation();
		String addressText = "Location Not available";
		if (canGetLocation) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			// Get the current location from the input parameter list
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				/*
				 * Return 1 address.
				 */
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
			} catch (IOException e1) {
				e1.printStackTrace();
				return ("IO Exception trying to get address:" + e1);
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments "
						+ Double.toString(latitude) + " , "
						+ Double.toString(longitude)
						+ " passed to address service";
				e2.printStackTrace();
				return errorString;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
				String address1 = address.getMaxAddressLineIndex() > 0 ? address
						.getAddressLine(0) : "";
				String locality = address.getLocality() != null ? address
						.getLocality() : "";
				String country = address.getCountryName() != null ? address
						.getCountryName() : "";

				address1 = getValidStringWithBoolenFlag(address1, false);
				locality = getValidStringWithBoolenFlag(locality, false);
				country = getValidStringWithBoolenFlag(country, false);

				addressText = getValidString(address1)
						+ getValidString(locality) + getValidString(country);
				// Return the text
			}
		}
		return addressText;
	}

	public String getAllAddressDetails(Context context, double lat,
									   double longit) {
		List<Address> addresses;

		if (lat > 0 && longit > 0) {
			try {
				Geocoder geocoder1 = new Geocoder(mContext, Locale.getDefault());
				addresses = geocoder1.getFromLocation(lat, longit, 1);
				fullAddress = addresses.get(0).getAddressLine(0); // If any
																	// additional
																	// address
																	// line
																	// present
																	// than
																	// only,
																	// check
																	// with max
																	// available
																	// address
																	// lines by
																	// getMaxAddressLineIndex()
				city = addresses.get(0).getLocality();
				state = addresses.get(0).getAdminArea();
				country = addresses.get(0).getCountryName();
				postalCode = addresses.get(0).getPostalCode();
				longitude1 = longit + "";
				latitude1 = lat + "";
				totalAddress = getValidString(city) + getValidString(state)
						+ getValidString(country) + getValidString(postalCode);
				city = getValidStringWithBoolenFlag(city, false);
				state = getValidStringWithBoolenFlag(state, false);
				country = getValidStringWithBoolenFlag(country, false);
				postalCode = getValidStringWithBoolenFlag(postalCode, false);

				completeAddress = getValidString(fullAddress)
						+ getValidString(city) + getValidString(state)
						+ getValidString(country) + getValidString(postalCode)
						+ getValidString(latitude1 + "")
						+ getValidString(longitude1 + "");
				totalAddress = totalAddress.endsWith(",") ? totalAddress
						.substring(0, totalAddress.length() - 1) : totalAddress;
				// Toast.makeText(getBaseContext(), "Address " +completeAddress
				// , Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return city + " " + state + " " + country + " " + postalCode;
	}

	public static String getValidString(String data) {
		return getValidStringWithBoolenFlag(data, true);
	}

	public static String getValidStringWithBoolenFlag(String data, boolean value) {
		String comma = value ? ", " : "";
		return (data != null && data.length() > 0) ? data + comma : "";
	}
}
