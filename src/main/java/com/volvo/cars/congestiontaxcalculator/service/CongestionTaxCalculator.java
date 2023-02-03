package com.volvo.cars.congestiontaxcalculator.service;

import com.volvo.cars.congestiontaxcalculator.dto.TimeCalculator;
import com.volvo.cars.congestiontaxcalculator.dto.HolidayMonth;
import com.volvo.cars.congestiontaxcalculator.dto.HolidayList;
import com.volvo.cars.congestiontaxcalculator.dto.HolidayYear;
import com.volvo.cars.congestiontaxcalculator.dto.TaxExempt;
import com.volvo.cars.congestiontaxcalculator.dto.TaxPrice;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CongestionTaxCalculator {


  final TaxExempt taxExempt;

  final TaxPrice taxPrice;


  final HolidayList holidayList;
  @Value("${singlecharge.time}")
  private int singleChargeTime;

  public CongestionTaxCalculator(TaxExempt taxExempt, TaxPrice taxPrice, HolidayList holidayList) {
    this.taxPrice = taxPrice;
    this.taxExempt = taxExempt;
    this.holidayList = holidayList;
  }

  public int calculateTax(String vehicle, String[] dates) throws ParseException {

    //will convert Date to localDate for correct time management and better tracking.
    List<LocalDateTime> localDateTimeList=Stream.of(dates).map(d->convertToLocalDateTime(d)).collect(Collectors.toList());

    //Sort the dates in ascending order
    List<LocalDateTime> dateTimeSortedList = localDateTimeList.stream().sorted((o1, o2) -> {
      if (o1.isBefore(o2)) {
        return -1;
      }
      return 1;
    }).collect(Collectors.toList());

    //Map to store dates of toll passed in a single day corresponding to that particular day
    Map<String, List<LocalDateTime>> map = getDayWiseMapping(dateTimeSortedList);

    // Interval Start date for the first time
    return getTaxUpdated(vehicle, map, localDateTimeList.get(0) );

  }

  private Map<String, List<LocalDateTime>> getDayWiseMapping(List<LocalDateTime> dateTimeSortedList) {
    Map<String, List<LocalDateTime>> map = new LinkedHashMap<>();
    for (LocalDateTime i : dateTimeSortedList) {
      // custom key for the particular day
      String day = i.getDayOfMonth() + "-" + i.getMonthValue() + "-" + i.getYear();

      if (!map.containsKey(day)) {
        List<LocalDateTime> list = new ArrayList<>();
        list.add(i);
        map.put(day, list);
      } else {
        List<LocalDateTime> tempList = map.get(day);
        tempList.add(i);
        map.put(day, tempList);
      }
    }
    return map;
  }

  public int getTaxUpdated(String vehicle, Map<String, List<LocalDateTime>> dateMap, LocalDateTime intervalStartDate)
  {
    int totalFee = 0;

    for (String key : dateMap.keySet()) { //day wise loop for per day fee tracking
      int totalDayFee = 0;
      int maxPreviousFee = 0;
      List<LocalDateTime> dateList = dateMap.get(key);

      for (LocalDateTime day : dateList) { // No. of toll per day tracking loop for checking the actual logic
        Date date = Date.from(day.atZone(ZoneId.systemDefault()).toInstant());
        Date startDate = Date.from(intervalStartDate.atZone(ZoneId.systemDefault()).toInstant());
        int nextFee = getTollFee(date, vehicle);
        int tempFee = getTollFee(startDate, vehicle);
        //maxPreviousFee is to track last Tax fee
        tempFee = Math.max(tempFee, maxPreviousFee);

        long minutes= Math.abs(ChronoUnit.MINUTES.between(day, intervalStartDate));
        if (minutes <= 60) {
          if (totalDayFee > 0) {
            totalDayFee -= tempFee;
          }
          if (nextFee >= tempFee) {
            totalDayFee += nextFee;
            maxPreviousFee = nextFee;
          } else {
            totalDayFee += tempFee;
            maxPreviousFee = nextFee;
          }
        } else {
          intervalStartDate = day;
          totalDayFee += nextFee;
          tempFee = nextFee;
          maxPreviousFee = nextFee;
        }
      }
      if (totalDayFee >= 60) {
        totalDayFee = 60;
      }

      totalFee += totalDayFee; //Adding in total Tax fee
    }
    //Fixed multiple Bugs
    return totalFee;
  }

  private boolean dateDiff(Date d1, Date d2) {
    long diff = d2.getTime() - d1.getTime();
    return TimeUnit.MILLISECONDS.toMinutes(diff) > singleChargeTime;
  }

  private boolean isTollFreeVehicle(String vehicleType) {
      if (vehicleType == null) {
          return false;
      }
    return taxExempt.getVehicles().contains(vehicleType);
  }

  public int getTollFee(Date date, String vehicle) {
      if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) {
          return 0;
      }
    String hour = date.getHours() + ":" + date.getMinutes();
    return taxPrice.getTimings().stream().filter(obj -> obj.isTimeBetween(hour)).findFirst()
        .orElse(new TimeCalculator()).getPrice();
  }

  private Boolean isTollFreeDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

      if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
          return true;
      }

    for (HolidayYear hYear : holidayList.getYears()) {
      if (hYear.getYear() == year) {
        for (HolidayMonth hmonth : hYear.getMonths()) {
            if (hmonth.getMonth() == month) {
                if (hmonth.getDates().isEmpty()) {
                    return true;
                } else if (hmonth.getDates().contains(dayOfMonth) || hmonth.getDates()
                    .contains(dayOfMonth + 1)) {
                    return true;
                }
            }
        }
      }
    }
    return false;
  }

  public static LocalDateTime convertToLocalDateTime(String dateToConvert) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(dateToConvert, format);
  }
}
