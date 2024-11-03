package com.karthik.pdfgenerator.service.impl;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.karthik.pdfgenerator.config.PdfConfig;
import com.karthik.pdfgenerator.model.InvoiceRequest;
import com.karthik.pdfgenerator.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private PdfConfig config;

    public String generateAndStorePdf(InvoiceRequest invoiceRequest) throws IOException, NoSuchAlgorithmException {
        String pdfFilePath = config.getStoragePath() + generateHash(invoiceRequest) + ".pdf";
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            createPdf(invoiceRequest, pdfFilePath);
        }
        return pdfFilePath;
    }

    private void createPdf(InvoiceRequest invoiceRequest, String pdfFilePath) throws IOException {
        String htmlContent = generateHtmlContent(invoiceRequest);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             FileOutputStream fileOut = new FileOutputStream(pdfFilePath)) {

            PdfWriter pdfWriter = new PdfWriter(out);
            HtmlConverter.convertToPdf(htmlContent, pdfWriter, createConverterProperties());
            out.writeTo(fileOut);
        } catch (Exception e) {
            throw new IOException("Failed to create PDF", e);
        }
    }

    private String generateHtmlContent(InvoiceRequest invoiceRequest) {
        Context context = new Context();
        context.setVariable("seller", invoiceRequest.getSeller());
        context.setVariable("sellerGstin", invoiceRequest.getSellerGstin());
        context.setVariable("sellerAddress", invoiceRequest.getSellerAddress());
        context.setVariable("buyer", invoiceRequest.getBuyer());
        context.setVariable("buyerGstin", invoiceRequest.getBuyerGstin());
        context.setVariable("buyerAddress", invoiceRequest.getBuyerAddress());
        context.setVariable("items", invoiceRequest.getItems());

        return templateEngine.process("template", context);
    }

    private ConverterProperties createConverterProperties() {
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setFontProvider(new DefaultFontProvider(false, true, false));
        return converterProperties;
    }

    private String generateHash(InvoiceRequest invoiceRequest) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(invoiceRequest.toString().getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
