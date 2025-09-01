package com.rama.jobportal.job_portal.utils;


import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ResumeParser {
    private static final Tika tika = new Tika();

    public static String extractText(byte[] resumeBytes) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(resumeBytes)) {
            return tika.parseToString(in);
        } catch (TikaException e) {
            throw new IOException("Failed to parse resume", e);
        }
    }
}
