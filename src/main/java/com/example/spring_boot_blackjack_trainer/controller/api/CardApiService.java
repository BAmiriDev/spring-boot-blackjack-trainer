package com.example.spring_boot_blackjack_trainer.controller.api;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CardApiService {

    private final RestTemplate restTemplate;

    @Autowired
    public CardApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createNewDeck() {
        String url = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1";
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);
        return json.getString("deck_id");  // returns the deck ID
    }

    public String drawCard(String deckId) {
        String url = "https://deckofcardsapi.com/api/deck/" + deckId + "/draw/?count=1";
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);
        return json.getJSONArray("cards").getJSONObject(0).getString("code"); // like "AS"
    }
}
