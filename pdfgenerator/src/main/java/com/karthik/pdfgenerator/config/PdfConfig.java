package com.karthik.pdfgenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfConfig {

    @Value("${pdf.storage.path}")
    private String storagePath;

    public String getStoragePath() {
        return storagePath;
    }
}
