package clash_of_cards.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ContentLoader {
    private final Random random = new Random();
    private final ArrayList<String> answers = new ArrayList<>();
    private final ArrayList<String> sentences = new ArrayList<>();
    private String edition;
    private String resourceFile;

    public ContentLoader(String edition) {
        this.edition = edition;
        this.resourceFile = getResourceFileName("Answer");
        loadContent("Answer");
        this.resourceFile = getResourceFileName("Sentence");
        loadContent("Sentence");
    }

    private void loadContent(String type) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.resourceFile);
            if (inputStream == null) {
                throw new IllegalStateException("Resource file not found: " + resourceFile);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                List<String> lines = reader.lines().collect(Collectors.toList());
                if (type.equals("Answer")) {
                    answers.addAll(lines);
                } else {
                    sentences.addAll(lines);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load " + type.toLowerCase() + " from file: " + e.getMessage());
            e.printStackTrace();
        }
    }    

    private String getResourceFileName(String type) {
        if (type.equals("Answer")) {
            return edition.equals("nerd") ? "nerdAnswers.txt" : "familyAnswers.txt";
        } else {
            return edition.equals("nerd") ? "nerdSentences.txt" : "familySentences.txt";
        }
    }

    public String getRandom(String type) {
        ArrayList<String> list = type.equals("Answer") ? answers : sentences;
        return !list.isEmpty() ? list.get(random.nextInt(list.size())) : "No " + type.toLowerCase() + " available.";
    }
}