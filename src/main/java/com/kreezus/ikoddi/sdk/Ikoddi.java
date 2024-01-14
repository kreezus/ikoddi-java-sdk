package com.kreezus.ikoddi.sdk;

import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONObject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

/**
 * Hello world!
 *
 */
public class Ikoddi {
    private String apiBaseURL;
    private String groupId;
    private String apiKey;
    private String otpAppId;

    Ikoddi(String apiBaseURL, String groupId, String apiKey, String otpAppId) {
        this.apiBaseURL = apiBaseURL;
        this.groupId = groupId;
        this.apiKey = apiKey;
        this.otpAppId = otpAppId;
    }

    public static IkoddiBuilder builder() {
        return new IkoddiBuilder();
    }

    private void assertAllParametersAreCorrect() throws Exception {
        if (this.apiBaseURL == null) {
            throw new Exception("[Ikoddi] - Api Base URL should be defined");
        }
        if (this.apiKey == null) {
            throw new Exception("[Ikoddi] - Api Key should be defined");
        }
        if (this.groupId == null) {
            throw new Exception("[Ikoddi] - The group ID should be defined");
        }
    }

    /*
     * SMS
     */

    public String sendSMS(
            List<String> numbers,
            String from,
            String message,
            String smsBroadCast,
            String phonecode,
            String isoCode) throws Exception {
        assertAllParametersAreCorrect();
        var payload = new JSONObject();
        payload.put("sentTo", numbers);
        payload.put("from", from);
        payload.put("message", message);
        payload.put("smsBroadCast", smsBroadCast);
        payload.put("phonecode", phonecode);
        payload.put("isoCode", isoCode);
        payload.put("isDiferred", false);
        var request = HttpRequest.newBuilder()
                .uri(new URI(this.apiBaseURL + this.groupId + "/sms"))
                .headers("Content-Type", "application/json")
                .headers("x-api-key", this.apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
        var response = httpResponse.body();
        return response;
    }

    public String sendOTP(
            String identity,
            String type) throws Exception {
        assertAllParametersAreCorrect();
        if (this.otpAppId == null) {
            throw new Exception("OTP App ID should be defined");
        }
        var request = HttpRequest.newBuilder()
                .uri(new URI(this.apiBaseURL + this.groupId + "/otp/" + this.otpAppId + "/" + type + "/"
                        + URLEncoder.encode(identity, StandardCharsets.UTF_8.toString())))
                .headers("Content-Type", "application/json")
                .headers("x-api-key", this.apiKey)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
        var response = httpResponse.body();
        return response;
    }

    public String verifyOTP(
            String identity,
            String otp,
            String verificationKey) throws Exception {
        assertAllParametersAreCorrect();
        if (this.otpAppId == null) {
            throw new Exception("OTP App ID should be defined");
        }
        var payload = new JSONObject();
        payload.put("identity", identity);
        payload.put("otp", otp);
        payload.put("verificationKey", verificationKey);
        var request = HttpRequest.newBuilder()
                .uri(new URI(this.apiBaseURL + this.groupId + "/otp/" + this.otpAppId + "/verify"))
                .headers("Content-Type", "application/json")
                .headers("x-api-key", this.apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
        var response = httpResponse.body();
        return response;
    }

    /*
     * AIRTIME
     */

    public String internetPlans() throws Exception {
        assertAllParametersAreCorrect();
        var request = HttpRequest.newBuilder()
                .uri(new URI(this.apiBaseURL + this.groupId + "/airtimes/internet-plans"))
                .headers("Content-Type", "application/json")
                .headers("x-api-key", this.apiKey)
                .GET()
                .build();

        HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
        var response = httpResponse.body();
        return response;
    }

    public String sendAirtime(
            List<String> numbers,
            String ref,
            String amount,
            String campaignName,
            String phonecode,
            String isoCode) throws Exception {
        assertAllParametersAreCorrect();
        var payload = new JSONObject();
        payload.put("sentTo", numbers);
        payload.put("ref", ref);
        payload.put("amount", amount);
        payload.put("campaignName", campaignName);
        payload.put("countryNumberCode", phonecode);
        payload.put("countryStringCode", isoCode);
        var request = HttpRequest.newBuilder()
                .uri(new URI(this.apiBaseURL + this.groupId + "/airtimes"))
                .headers("Content-Type", "application/json")
                .headers("x-api-key", this.apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
        var response = httpResponse.body();
        return response;
    }

    public static class IkoddiBuilder {
        private String apiBaseURL = "https://api.ikoddi.com/api/v1/groups/";
        private String groupId;
        private String apiKey;
        private String otpAppId;

        IkoddiBuilder() {
        }

        public IkoddiBuilder apiBaseURL(String apiBaseURL) {
            this.apiBaseURL = apiBaseURL;
            return this;
        }

        public IkoddiBuilder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public IkoddiBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public IkoddiBuilder otpAppId(String otpAppId) {
            this.otpAppId = otpAppId;
            return this;
        }

        public Ikoddi build() {
            return new Ikoddi(apiBaseURL, groupId, apiKey, otpAppId);
        }
    }

    public static void main(String[] args) throws Exception {
        // SMS
        var ikoddiSMS = Ikoddi
                .builder()
                .apiBaseURL("https://api.staging.ikoddi.com/api/v1/groups/")
                .groupId("10268496")
                .apiKey("zixVtKzPPjuFp3Bdw3MWLCDxtRTXjWXP")
                .build();
        var smsResponse = ikoddiSMS.sendSMS(List.of("22676467345"), "Ikoddi", "Test message", "", "226", "BF");
        System.out.println("SMS Response : " + smsResponse);
        var ikoddiOTP = Ikoddi
                .builder()
                .apiBaseURL("https://api.staging.ikoddi.com/api/v1/groups/")
                .groupId("10268496")
                .apiKey("Zhh4McBGKgsQgT8FGmj1DBNvy4R6IV1t")
                .otpAppId("clok6631r0000s601khk5dtr8")
                .build();
        var smsOTPResponse = ikoddiOTP.sendOTP("22676467345", "sms");
        System.out.println("OTP Response : " + smsOTPResponse);
        var smsOTPVerifyResponse = ikoddiOTP.verifyOTP("22676467345", "123456",
                "o2G3oFb4QdJJTqqrnFE67cJoyEiT3W94xZWklXoGf7rOKTmBN7wi8V8PJKHrbnMybx7IEDTqaVJicMtzd5eRQEG9CkYs02/t5HyFjlioUjtFUan0pUcb0i9RyJeyz3XUkVzLjDo16W3IWRHOfC/lZ4Gdp5E3qgV0Si1i7BqKDKMW8Cdut0VUqKqf06XYvZZH");
        System.out.println("OTP Verify : " + smsOTPVerifyResponse);
        // Airtime
        var ikoddiAirtime = Ikoddi
                .builder()
                .apiBaseURL("https://api.staging.ikoddi.com/api/v1/groups/")
                .groupId("10268496")
                .apiKey("LffL6sO6BHy5lBXHHrzKz5hn2LRg3tzx")
                .build();
        var airtimePlanResponse = ikoddiAirtime.internetPlans();
        System.out.println("Internet plans Response : " + airtimePlanResponse);
        var airtimeResponse = ikoddiAirtime.sendAirtime(List.of("22676467345"), "11203", "100", "null", "226", "BF");
        System.out.println("Airtime Response : " + airtimeResponse);
    }
}
