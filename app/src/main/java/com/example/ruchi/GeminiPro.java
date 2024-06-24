package com.example.ruchi;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.BlockThreshold;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.HarmCategory;
import com.google.ai.client.generativeai.type.SafetySetting;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.concurrent.Executor;

public class GeminiPro {
    public void getResponse(String query, ResponseCallback callback) {
        GenerativeModelFutures model = getModel();

        Content content = new Content.Builder().addText(query).build();
        Executor executor = Runnable::run;

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String rawText = result.getText();
                System.out.println("Raw text from model: " + rawText); // Debug log

                String cleanedText = cleanUpText(rawText);
                System.out.println("Cleaned text: " + cleanedText); // Debug log

                callback.onResponse(cleanedText);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                callback.onError(throwable);
            }
        }, executor);
    }

    private GenerativeModelFutures getModel() {
        String apiKey = buildconfig.apikey;

        SafetySetting harassmentSafety = new SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH);


        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.9f;
        configBuilder.topK = 16;
        configBuilder.topP = 0.1f;
        GenerationConfig generationConfig = configBuilder.build();

        GenerativeModel gm = new GenerativeModel(
                "gemini-pro",
                apiKey,
                generationConfig,
                Collections.singletonList(harassmentSafety)
        );

        return GenerativeModelFutures.from(gm);
    }

    private String cleanUpText(String text) {
        text = text.replaceAll("\\*+", "*").replace("*", "");

        // Remove common bullet point symbols and unnecessary whitespace
        text = text.replaceAll("[•◦‣⁃⁌⁍]", "");
        text = text.replaceAll("[-]+", "");
        text = text.replaceAll("[\u2022\u2023\u25E6\u2043\u2219]", ""); // Other bullet-like symbols

        // Remove leading/trailing whitespaces and reduce multiple spaces to a single space
        text = text.trim().replaceAll("\\s+", " ");

        return text;
    }

}