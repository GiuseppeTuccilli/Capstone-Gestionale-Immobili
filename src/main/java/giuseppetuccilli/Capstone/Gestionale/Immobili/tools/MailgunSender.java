package giuseppetuccilli.Capstone.Gestionale.Immobili.tools;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Utente;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Component
public class MailgunSender {
    private String domain;
    private String apiKey;

    public MailgunSender(@Value("${mailgun.domain}") String domain, @Value("${mailgun.apiKey}") String apiKey) {
        this.domain = domain;
        this.apiKey = apiKey;
    }


    public void sendRegistrationEmail(Utente recipient, String og, String mes) {

        kong.unirest.core.HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domain + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", "Mailgun Sandbox <postmaster@" + domain + ">")//in caso cambiare la mail.
                .queryString("to", recipient.getEmail()) // Qua potrà esserci solo uno degli indirizzi autorizzati precedentemente sulla dashboard di Mailgun
                .queryString("subject", og)
                .queryString("text", mes)
                .asJson();
        System.out.println(response.getBody()); // <-- Consiglio questo log per debuggare eventuali problemi
    }

    public void addToMailgun(String email) {
        String auth = Base64.getEncoder().encodeToString(("api:" + this.apiKey).getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.mailgun.net/v5/sandbox/auth_recipients?email=" + email))
                .header("Authorization", "Basic " + auth)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());
        } catch (Exception ex) {
            throw new RuntimeException("errore verifica email");
        }
    }
}
