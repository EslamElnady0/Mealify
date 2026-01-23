package com.mealify.mealify.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MealDetailsUtils {

    public static List<String> parseInstructions(String str) {
        if (str == null || str.isEmpty())
            return new ArrayList<>();

        String normalized = str.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
        String[] lines = normalized.split("\n");
        List<String> steps = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty())
                continue;

            String cleaned = trimmed.replaceAll("(?i)^step\\s+\\d+(\\s+|:|,|.|\\b)", "").trim();

            if (!cleaned.isEmpty()) {
                steps.add(cleaned);
            }
        }

        if (steps.size() <= 1 && str.contains(". ")) {
            String[] sentences = str.split("\\.\\s+");
            if (sentences.length > 1) {
                steps.clear();
                for (String s : sentences) {
                    String trimmed = s.trim();
                    if (!trimmed.isEmpty()) {
                        if (!trimmed.endsWith("."))
                            trimmed += ".";
                        steps.add(trimmed);
                    }
                }
            }
        }

        return steps;
    }

    public static String extractVideoId(String youtubeUrl) {
        String videoId = null;
        if (youtubeUrl != null && !youtubeUrl.trim().isEmpty()) {
            String pattern = "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2F|youtu.be%2F|%2Fv%2F)[^#&?\\n]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(youtubeUrl);
            if (matcher.find()) {
                videoId = matcher.group();
            }
        }
        return videoId;
    }
}
