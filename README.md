# congestion-tax-calculator

congestion-tax-calculator is a Springboot REST Api service to calculate the congestion  tax calculator in a City.

As part of calculation, this applications implemented some of exemption on Holidays, weekends and some of vehicle.

REST Endpoint:

**API URL:** http://localhost:8080/tax/calculator/sweden

**API method:** POST

**API Content Type:** application/json

**Input Json:** 

    {
        "city": "Gothenburg" ,
        "vehicle_type": "Car",
        "dates": [
            "2013-01-14 21:00:00","2013-01-15 21:00:00",
            "2013-02-07 06:23:27","2013-02-07 15:27:00",
            "2013-02-08 06:27:00","2013-02-08 06:20:27",
            "2013-02-08 14:35:00","2013-02-08 15:29:00",
            "2013-02-08 15:47:00","2013-02-08 16:01:00",
            "2013-02-08 16:48:00","2013-02-08 17:49:00",
            "2013-02-08 18:29:00","2013-02-08 18:35:00",
            "2013-03-26 14:25:00","2013-03-28 14:07:27"
        ]
    }
   Output: 

       {
    		"error": null,
    		"tax": 58,
    		"message": "Tax calculated successfully, Vehicle :Car Amount : 58",
    		"timestamp": 1637177384362
       }

** for now Other cities have not been onboarded **

    {
        "city": "Stockholm" ,
        "vehicle_type": "Car",
        "dates": [
            "2013-01-14 21:00:00","2013-01-15 21:00:00"
        ]
    }
Output:

       {
    		"error": "Only Gothenburg is supported",
    		"tax": 0,
    		"message": "Stockholm has not been onboarded yet",
    		"timestamp": 1637177384362
       }