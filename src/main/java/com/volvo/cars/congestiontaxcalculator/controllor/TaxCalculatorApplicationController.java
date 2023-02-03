package com.volvo.cars.congestiontaxcalculator.controllor;


import com.volvo.cars.congestiontaxcalculator.model.*;
import com.volvo.cars.congestiontaxcalculator.service.CongestionTaxCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tax")
public class TaxCalculatorApplicationController {

  final CongestionTaxCalculator congestionTaxCalculator;

  public TaxCalculatorApplicationController(CongestionTaxCalculator congestionTaxCalculator) {
    this.congestionTaxCalculator = congestionTaxCalculator;
  }

  @RequestMapping(value = "/calculator/sweden", method = RequestMethod.POST)
  public ResponseEntity<TaxCalculatorResponse> taxCalculate(@RequestBody CongestionTaxCalculateRequest request) {
    TaxCalculatorResponse response;
    try {
      if ("Gothenburg".equals(request.getCity())) {
        int tax = congestionTaxCalculator.calculateTax(request.getVehicle_type().name(), request.getDates());

        response = new TaxCalculatorResponse(null, tax,
            "Tax calculated successfully, Vehicle :" + request.getVehicle_type().name()
                + " Amount : " + tax);
        return new ResponseEntity<TaxCalculatorResponse>(response, HttpStatus.OK);
      } else {
        response = new TaxCalculatorResponse("Only Gothenburg is supported", 0,
            request.getCity() + " has not been onboarded yet");
        return new ResponseEntity<TaxCalculatorResponse>(response, HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      response = new TaxCalculatorResponse(e.getLocalizedMessage(), 0,
          "Tax calculation failed.");
      return new ResponseEntity<TaxCalculatorResponse>(response, HttpStatus.BAD_REQUEST);
    }
  }
}
