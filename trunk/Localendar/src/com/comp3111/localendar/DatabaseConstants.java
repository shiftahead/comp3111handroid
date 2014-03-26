package com.comp3111.localendar;

import android.provider.BaseColumns;

public interface DatabaseConstants extends BaseColumns {

	public static final String TABLE_NAME = "events";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String LOCATION = "location";
    public static final String TRANSPORTATION = "transportation";
    public static final String COMPULSORY = "compulsory";
}
