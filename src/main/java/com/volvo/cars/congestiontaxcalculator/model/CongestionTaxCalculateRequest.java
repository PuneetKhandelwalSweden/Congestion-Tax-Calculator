package com.volvo.cars.congestiontaxcalculator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import com.volvo.cars.congestiontaxcalculator.dto.Vehicle;


@ApiModel
public class CongestionTaxCalculateRequest {

  private String city;
  private Vehicle vehicle_type;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private String[] dates;

  public Vehicle getVehicle_type() {
    return vehicle_type;
  }

  public void setVehicle_type(Vehicle vehicle_type) {
    this.vehicle_type = vehicle_type;
  }

  public String[] getDates() {
    return dates;
  }

  public void setDates(String[] dates) {
    this.dates = dates;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
