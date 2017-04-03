package com.example.utills;

import com.example.dataobjects.Event;
import com.example.dataobjects.Invitation;
import com.example.dataobjects.User;
import com.example.exception.InvtAppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.example.syncher.BaseSyncher.exceptionHandler;


public class StringUtils {

    public static final SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    public static final SimpleDateFormat eventTimeFormat = new SimpleDateFormat("hh:mm a");
    public static final SimpleDateFormat newEventDateFormat = new SimpleDateFormat("dd MM yyyy");
    public static final SimpleDateFormat newEventTimeFormat = new SimpleDateFormat("hh mm a");
    public static final SimpleDateFormat eventInfoFormat = new SimpleDateFormat("E, yyyy MMM dd - hh:mm a");

    public static Date StringToDate(String date) {
        Date dateTime = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateTime = simpleDateFormat.parse(date);
        } catch (ParseException ex) {
            System.out.println("Exception " + ex);
        }
        return dateTime;
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (simpleDateFormat.format(new Date()));
    }

    public static boolean inOrder(String startDateString, String endDateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null, endtDate = null;
        try {
            startDate = simpleDateFormat.parse(startDateString);
            endtDate = simpleDateFormat.parse(endDateString);
            return startDate.equals(endtDate) || startDate.before(endtDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static long DiffBetweenTwoDates() {
        Calendar startDate = Calendar.getInstance(), endDate = Calendar.getInstance();
        startDate.setTime(StringToDate("2015-12-12 11:00:12"));
        endDate.setTime(StringToDate("2015-12-12 13:00:12"));
        long diff = (endDate.getTimeInMillis() - startDate.getTimeInMillis()) / (60 * 1000);
        System.out.println(diff);
        return diff;
    }

    public static boolean isGivenDateGreaterThanOrEqualToCurrentDate(String dateTimeInfo) {
        boolean status = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sdf.parse(dateTimeInfo);
            Date date2 = sdf.parse(getCurrentDate());
            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                status = true;
            }
            if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
            }
            if (date1.equals(date2)) {
                status = true;
                System.out.println("Date1 is equal Date2");
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static boolean validateStartAndEndDates(String startDateInfo, String endDateInfo) {
        Date startDate = null;
        Date endDate = null;
        boolean status = true;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
        try {
            startDate = simpleDateFormat.parse(startDateInfo);
            endDate = simpleDateFormat.parse(endDateInfo);
            if (startDate.after(endDate)) {
                System.out.println("Date1 is after Date2");
                status = false;
            }
            if (startDate.before(endDate)) {
                System.out.println("Date1 is before Date2");
            }
            if (startDate.equals(endDate)) {
                status = false;
                System.out.println("Date1 is equal Date2");
            }
        } catch (Exception ex) {
            System.out.println("Exception " + ex);
        }
        System.out.println(startDateInfo + " : " + endDateInfo + " Status " + status);
        return status;
    }

    public static List<Event> getFutureEvents(List<Event> events) {
        List<Event> futureEvents = new ArrayList<Event>();
        for (Event event : events) {
            String startDateTime = event.getStartDateTime().replace('T', ' ').replace('Z', ' ').trim();
            String endDateTime = event.getEndDateTime().replace('T', ' ').replace('Z', ' ').trim();
            if (isGivenDateGreaterThanOrEqualToCurrentDate(startDateTime)) {
                futureEvents.add(event);
            }
        }
        return futureEvents;
    }

    public static String getValidString(String data) {
        return (data != null && data.length() > 0) ? data + "\n" : "";
    }

    public static void sortEventsBadsedonStartDate(List<Event> events) {
        Collections.sort(events, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                Event e1 = (Event) o1;
                Event e2 = (Event) o2;
                return e1.getStartDateTime().compareTo(e2.getStartDateTime());
            }
        });
    }

    public static String getNewDate(String oldDate, int minutes) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Old dateTime " + oldDate);
        String newDate = oldDate;
        Date stringToDate = StringToDate(oldDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(stringToDate);
        cal.add(Calendar.MINUTE, minutes);
        newDate = sdf.format(cal.getTime());
        System.out.println("New dateTime " + newDate);
        return newDate;
    }

    public static String getFormatedDateFromServerFormatedDate(String serverFormattedDate) {
        if (serverFormattedDate != null && serverFormattedDate.contains("+")) {
            serverFormattedDate = serverFormattedDate.substring(0, serverFormattedDate.indexOf('+'));
        }
        return serverFormattedDate.replace('T', ' ').replace('Z', ' ').trim();
    }

    public static String getEventIds(List<Invitation> listOfInvitations) {
        String result = "";
        for (int i = 0; i < listOfInvitations.size() - 1; i++) {
            result += listOfInvitations.get(i).getEventId() + ", ";
        }
        result += listOfInvitations.get(listOfInvitations.size() - 1).getEventId();
        return result;
    }

    public static String getAcceptedEventIds(List<Invitation> listOfInvitations) {
        String result = "";
        for (int i = 0; i < listOfInvitations.size() - 1; i++) {
            Invitation invitation = listOfInvitations.get(i);
            if (invitation.isAccepted()) {
                result += invitation.getEventId() + ", ";
            }
        }
        Invitation invitation = listOfInvitations.get(listOfInvitations.size() - 1);
        result += invitation.isAccepted() ? invitation.getEventId() : "";
        return result;
    }

    public static String getContactsString(List<String> contacts) {
        String result = "";
        for (int i = 0; i < contacts.size() - 1; i++) {
            result += contacts.get(i) + ", ";
        }
        result = result.endsWith(",") ? (result.substring(0, result.length() - 1)) : result;
        return result;
    }

    public static String getTrimmeDistance(String distance) {
        String result = "Not found";
        DecimalFormat df = new DecimalFormat("#.0");
        df.setMinimumIntegerDigits(1);
        if (distance != null && !distance.equals("Not found")) {
            double doubleInfo = Double.parseDouble(distance);
            result = df.format(doubleInfo) + " km";
        }
        return result;
    }

    public static String formatDateAndTime(String string, int index) {
        String info = "Not found";
        Date dateTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateTime = simpleDateFormat.parse(string);
            switch (index) {
                case 1:
                    info = eventDateFormat.format(dateTime);
                    break;
                case 2:
                    info = eventTimeFormat.format(dateTime);
                    break;
                case 3:
                    info = newEventDateFormat.format(dateTime);
                    break;
                case 4:
                    info = newEventTimeFormat.format(dateTime);
                    break;
                case 5:
                    info = eventInfoFormat.format(dateTime);
                    break;
                default:
                    break;
            }
        } catch (ParseException ex) {
            System.out.println("Exception " + ex);
        }
        System.out.println("Formated data " + info);
        return info;
    }

    public static String getContactsListFromUsers(List<User> users) {
        String contacts = "";
        for (User user : users) {
            if (user != null && user.getPhoneNumber() != null)
                contacts = contacts + user.getPhoneNumber().trim() + ",";
        }
        contacts = contacts.endsWith(",") ? (contacts.substring(0, contacts.length() - 1)) : contacts;
        return contacts;
    }

    public static boolean isOldDate(String dateTimeInfo) {
        boolean status = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
            Date date1 = sdf.parse(dateTimeInfo);
            Date date2 = sdf.parse(sdf.format(new Date()));
            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                status = true;
            }
            if (date1.equals(date2)) {
                status = true;
                System.out.println("Date1 is equal Date2");
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static Date chatStringToDate(String date) {
        Date dateTime = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateTime = simpleDateFormat.parse(date);
        } catch (Exception ex) {
            System.out.println("Exception " + ex);
        }
        return dateTime;
    }

    public static String enc(String input) {
        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            exceptionHandler.handleException(ex);
        }
        throw new InvtAppException("Failed to encode string " + input);
    }

    public static double getDoubleFormString(String input) {
        return ((input == null) || input.trim().isEmpty()) ? 0.0 : Double.valueOf(input.trim());
    }

    public static String getEventDateFormat(String dateTime) throws ParseException {
        String[] d = dateTime.split(" ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(d[0]);
        SimpleDateFormat formatt = new SimpleDateFormat("EEE"+", "+"dd/MM/yyyy");
        String newDate = formatt.format(date);
        return newDate;
    }

    public static String getTimeFormat(String dateTime) throws ParseException {
        String[] t = dateTime.split(" ");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date time = formatter.parse(t[1]);
        SimpleDateFormat formatt = new SimpleDateFormat("hh:mm a");
        String newTime = formatt.format(time);
        return newTime;
    }

    public static String getEventTimeFormat(String dateTime1, String dateTime2) throws ParseException {
        String time1 = getTimeFormat(dateTime1);
        String time2 = getTimeFormat(dateTime2);
        return time1+"-"+time2;
    }
}