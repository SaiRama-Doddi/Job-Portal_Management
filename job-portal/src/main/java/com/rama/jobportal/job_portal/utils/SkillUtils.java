package com.rama.jobportal.job_portal.utils;


import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class SkillUtils {

    public static Set<String> normalizeCsvSkills(String csv) {
        if (csv == null || csv.isBlank()) return Set.of();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(SkillUtils::normalize)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static String normalize(String input) {
        if (input == null) return "";
        String s = input.toLowerCase(Locale.ROOT);
        s = Normalizer.normalize(s, Normalizer.Form.NFKC);
        s = s.replaceAll("[^a-z0-9+#. ]", " "); // keep letters, digits, +, #, ., spaces
        s = s.replaceAll("\\s+", " ").trim();
        return s;
    }

    /** Returns how many job skills appear in the resume text (normalized contains check). */
    public static long countMatchesInText(Set<String> jobSkills, String resumeText) {
        String text = normalize(resumeText);
        long count = 0;
        for (String skill : jobSkills) {
            String token = normalize(skill);
            if (!token.isBlank() && text.contains(token)) {
                count++;
            }
        }
        return count;
    }
}
