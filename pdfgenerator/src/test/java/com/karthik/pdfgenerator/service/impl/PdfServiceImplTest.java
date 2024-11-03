package com.karthik.pdfgenerator.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.karthik.pdfgenerator.config.PdfConfig;
import com.karthik.pdfgenerator.model.InvoiceRequest;
import com.karthik.pdfgenerator.model.Item;

class PdfServiceImplTest {

    @InjectMocks
    private PdfServiceImpl pdfService;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private PdfConfig pdfConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(pdfConfig.getStoragePath()).thenReturn("pdf-storage/");
    }

    @Test
    void generateAndStorePdf() throws IOException, NoSuchAlgorithmException {
        InvoiceRequest request = getInvoiceRequest();
        when(templateEngine.process(eq("template"), any(Context.class))).thenReturn("<html><body>Test PDF</body></html>");
        
        String pdfFilePath1 = pdfService.generateAndStorePdf(request);
        File pdfFile1 = new File(pdfFilePath1);
        assertTrue(pdfFile1.exists(), "PDF file should be created.");
        
        String pdfFilePath2 = pdfService.generateAndStorePdf(request);
        assertEquals(pdfFilePath1, pdfFilePath2, "PDF file paths should be the same.");
        
        pdfFile1.delete();
    }

    private InvoiceRequest getInvoiceRequest() {
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setSeller("XYZ Pvt. Ltd.");
        invoiceRequest.setSellerGstin("29AABBCCDD121ZD");
        invoiceRequest.setSellerAddress("New Delhi, India");
        invoiceRequest.setBuyer("Vedant Computers");
        invoiceRequest.setBuyerGstin("29AABBCCDD131ZD");
        invoiceRequest.setBuyerAddress("New Delhi, India");
        Item item = new Item();
        item.setName("Product 1");
        item.setQuantity("12 Nos");
        item.setRate(123.00);
        item.setAmount(1476.00);
        invoiceRequest.setItems(List.of(item));
        return invoiceRequest;
    }

}
