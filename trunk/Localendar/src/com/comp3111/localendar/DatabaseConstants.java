package com.comp3111.localendar;

import android.provider.BaseColumns;

public interface DatabaseConstants extends BaseColumns {

	public static final String TABLE_NAME = "events";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    public static final String DURATION_HOUR = "dhour";
    public static final String DURATION_MINUTE = "dminute";
    public static final String LOCATION = "location";
    public static final String TRANSPORTATION = "transportation";
    public static final String COMPULSORY = "compulsory";
}
