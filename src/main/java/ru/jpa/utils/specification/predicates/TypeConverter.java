package ru.jpa.utils.specification.predicates;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

interface TypeConverter {

  static String dateTimeWithMicroseconds(LocalDateTime dateTime) {
    return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss."))
        + String.format("%06d", Math.round(dateTime.getNano() / 1000f));
  }

  static String dateTimeWithMicroseconds(ZonedDateTime dateTime) {
    return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss."))
        + String.format("%06d", Math.round(dateTime.getNano() / 1000f));
  }

}
