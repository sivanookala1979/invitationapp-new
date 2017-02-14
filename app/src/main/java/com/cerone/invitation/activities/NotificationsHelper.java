package com.cerone.invitation.activities;

import java.util.ArrayList;
import java.util.List;

public class NotificationsHelper {
	
	static List<InternalNotificationListener> listeners = new ArrayList<InternalNotificationListener>();
	
	public static void addListener(InternalNotificationListener listener)
	{
		if(!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}
	
	public static void notifyListeners(int notificationsCount)
	{
		for(InternalNotificationListener listener : listeners)
		{
			listener.onNotificationRecieved(notificationsCount);
		}
		
	}

}
