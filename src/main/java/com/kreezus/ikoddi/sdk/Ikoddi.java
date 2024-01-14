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
}
