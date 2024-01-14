# ikoddi-java-sdk

The JAVA Client SDK to communicate with IKODDI, the airtime and SMS delivery plateform in West Africa

## How to install

```xml
<dependency>
    <groupId>com.kreezus</groupId>
    <artifactId>ikoddi-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

## How to use it ?

```java


/**AIRTIME**/

//Initialization of the API key for sending AIRTIME, the URL, and the GroupId (organization_id)
var ikoddiAirtime = Ikoddi
                .builder()
                .groupId("10268496")
                .apiKey("LffL6sO6BHy5lBXHHrzKz5hn2LRg3tzx")
                .build();
/*For testing purposes,
add .apiBaseURL("https://api.staging.ikoddi.com/api/v1/groups/")
during initialization.*/

// Retrieve all internet plans
var airtimePlanResponse = ikoddiAirtime.internetPlans();

// Send airtime
var airtimeResponse = ikoddiAirtime.sendAirtime(List.of("226XXXXXXXX"), "11203", "100", "null", "226", "BF");

/* sendAirtime methode attributes :
numbers : List of number to send airtime,
ref : The airtime reference;
      for mobile credit, use 11101 for Orange operator, 12131 for Moov operator, 13160 for Telecel operator.
      for the internet package, use a reference that you obtain by retrieving the reference list.
amount : airtime amount,
campagnName : This is the name you give to this transaction,
phoneCode : This is your international phone identifier. Default is 226,
isoCode : This is the ISO code of your country. Default is BF
*/

/**SMS**/

//Initialization of the API key for sending SMS, the URL, and the GroupId (organization_id)
var ikoddiSMS = Ikoddi
                .builder()
                .groupId("10268496")
                .apiKey("zixVtKzPPjuFp3Bdw3MWLCDxtRTXjWXP")
                .build();
/*For testing purposes,
add .withApiBaseURL("https://api.staging.ikoddi.com/api/v1/groups/")
during initialization.*/

//Send SMS
var smsResponse = ikoddiSMS.sendSMS(List.of("22676467345"), "Ikoddi", "Test message", "", "226", "BF");

/* sendAirtime methode attributes :
numbers : List of number to send sms,
from : The person or organization sending the message,
message : The message,
smsBroadCast :This is the name you give to this bulk message sending,
phoneCode : This is your international phone identifier. Default is 226,
isoCode : This is the ISO code of your country. Default is BF
*/
```

## OTP As A Service

```java
// 1- Initialize Ikoddi Client
var ikoddiOTP = Ikoddi.builder()
  .groupId("10268496")
  .apiKey("Zhh4McBGKgsQgT8FGmj1DBNvy4R6IV1t")
  .otpAppId("clok6631r0000s601khk5dtr8")
  .build();

// 2- Send OTP Code

var smsOTPResponse = ikoddiOTP.sendOTP("226XXXXXXXX", "sms");
// {"status":0,"otpToken":"UCQS2bNyfVOKENFcCnQTV17OTL/Ja2rt0ku5C0aZMopzE0kQOX10OQ4RF8aT2zQTN0LsTiozcY9e1YMxK7xAd8Tbz1xNnFMlIfz43D0ZQofy3TVAed1zmg52a1+29GGYGuN0NSzvE5fVPFxWvk0jC0f8q8R/84BxhmZD2OaMGVkh1DufftnSXvnV8LXtCMI3"}

// The User should receive an OTP code

// 3- Verify the OTP
var smsOTPVerifyResponse = ikoddiOTP.verifyOTP("226XXXXXXXX", "123456",
                "o2G3oFb4QdJJTqqrnFE67cJoyEiT3W94xZWklXoGf7rOKTmBN7wi8V8PJKHrbnMybx7IEDTqaVJicMtzd5eRQEG9CkYs02/t5HyFjlioUjtFUan0pUcb0i9RyJeyz3XUkVzLjDo16W3IWRHOfC/lZ4Gdp5E3qgV0Si1i7BqKDKMW8Cdut0VUqKqf06XYvZZH");
// {"status":0,"message":"OTP Matched for 22670707070"}
```
