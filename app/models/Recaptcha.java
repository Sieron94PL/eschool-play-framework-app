package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.libs.ws.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletionStage;

import play.libs.ws.WSBodyReadables.*;

import javax.inject.Inject;
import javax.validation.Constraint;


public class Recaptcha implements WSBodyReadables, WSBodyWritables {

    public String recaptcha;

    public static String getURL(String response) {
        String secret = "6LdpazsUAAAAAF0IIEtc17cD2vNWzV1fmX4qefw1";
        return "https://www.google.com/recaptcha/api/siteverify?secret=" + secret + "&response=" + response;
    }


}
