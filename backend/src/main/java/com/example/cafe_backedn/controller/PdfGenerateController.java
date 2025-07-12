package com.example.cafe_backedn.controller;

import com.example.cafe_backedn.advices.ApiResponse;
import com.example.cafe_backedn.services.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/pdf")
public class PdfGenerateController {

    private final PdfGeneratorService pdfGeneratorService;

    @GetMapping(value = "/generate/{id}",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        try{
            System.out.println("the id="+id);
        byte[] pdfBytes = pdfGeneratorService.generatePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }
}
