package com.smartvault.service;

import com.smartvault.model.Document;
import com.smartvault.model.OcrData;
import com.smartvault.model.enums.OcrStatus;
import com.smartvault.repository.OcrDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OcrService {

    private final OcrDataRepository ocrDataRepository;

    @Value("${app.ocr.tessdata-path}")
    private String tessdataPath;

    @Value("${app.ocr.enabled}")
    private boolean ocrEnabled;

    @Async("taskExecutor")
    @Transactional
    public void processDocument(Document document, byte[] fileBytes) {
        if (!ocrEnabled) {
            log.info("OCR is disabled. Skipping document: {}", document.getId());
            saveFailedOcr(document, "OCR processing is disabled in configuration.");
            return;
        }

        log.info("Starting OCR processing for document: {}", document.getId());

        OcrData ocrData = ocrDataRepository.findByDocumentId(document.getId())
                .orElse(OcrData.builder().document(document).status(OcrStatus.PROCESSING).build());
        ocrData.setStatus(OcrStatus.PROCESSING);
        ocrDataRepository.save(ocrData);

        try {
            String extractedText = performOcr(document.getFileType(), fileBytes);

            ocrData.setExtractedText(extractedText);
            ocrData.setStatus(OcrStatus.COMPLETED);
            ocrData.setProcessedAt(LocalDateTime.now());
            ocrDataRepository.save(ocrData);

            log.info("OCR completed successfully for document: {}", document.getId());

        } catch (Exception e) {
            log.error("OCR failed for document: {}", document.getId(), e);
            saveFailedOcr(document, e.getMessage());
        }
    }

    private String performOcr(String fileType, byte[] fileBytes) throws IOException, TesseractException {
        ITesseract tesseract = new Tesseract();
        
        File tessDataDir = new File(tessdataPath);
        if (tessDataDir.exists()) {
             tesseract.setDatapath(tessdataPath);
        } else {
             // Fallback or let it use default if tessdataPath doesn't exist
             log.warn("Tessdata path does not exist: {}", tessdataPath);
        }
        
        tesseract.setLanguage("eng");

        StringBuilder extractedText = new StringBuilder();

        if (fileType != null && fileType.toLowerCase().contains("pdf")) {
            try (PDDocument pdDocument = org.apache.pdfbox.Loader.loadPDF(new org.apache.pdfbox.io.RandomAccessReadBuffer(fileBytes))) {
                PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
                for (int page = 0; page < pdDocument.getNumberOfPages(); ++page) {
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
                    extractedText.append(tesseract.doOCR(bim)).append("\n");
                }
            }
        } else if (fileType != null && fileType.toLowerCase().contains("image")) {
            ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes);
            BufferedImage image = ImageIO.read(bais);
            if(image != null) {
                extractedText.append(tesseract.doOCR(image));
            } else {
                throw new IOException("Could not read image bytes.");
            }
        } else {
            throw new IllegalArgumentException("Unsupported file type for OCR: " + fileType);
        }

        return extractedText.toString();
    }

    private void saveFailedOcr(Document document, String errorMessage) {
        OcrData ocrData = ocrDataRepository.findByDocumentId(document.getId())
                .orElse(OcrData.builder().document(document).build());
        ocrData.setStatus(OcrStatus.FAILED);
        ocrData.setErrorMessage(errorMessage);
        ocrData.setProcessedAt(LocalDateTime.now());
        ocrDataRepository.save(ocrData);
    }
}
