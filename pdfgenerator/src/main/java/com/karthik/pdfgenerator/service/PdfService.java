package com.karthik.pdfgenerator.service;

import com.karthik.pdfgenerator.model.InvoiceRequest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface PdfService {
    String generateAndStorePdf(InvoiceRequest invoiceRequest) throws IOException, NoSuchAlgorithmException;
}
