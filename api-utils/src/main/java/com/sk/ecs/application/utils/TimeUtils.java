package com.sk.ecs.application.utils;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class TimeUtils {

    public static final ZoneOffset ZONE_OFFSET_UTC8 = ZoneOffset.ofHours(8);

    public static final DateTimeFormatter TABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter QUERY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
