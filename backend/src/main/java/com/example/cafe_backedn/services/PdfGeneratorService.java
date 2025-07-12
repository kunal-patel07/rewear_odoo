package com.example.cafe_backedn.services;

import com.example.cafe_backedn.entity.CategoryEntity;
import com.example.cafe_backedn.entity.OrderEntity;
import com.example.cafe_backedn.entity.ProductEntity;
import com.example.cafe_backedn.exception.ResourceNotFound;
import com.example.cafe_backedn.repo.CategoryRepo;
import com.example.cafe_backedn.repo.OrderRepo;
import com.example.cafe_backedn.repo.ProductRepo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import javax.swing.text.*;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;



    public byte[] generatePdf(Long id) {
        try {
        OrderEntity order = orderRepo.findById(id).orElseThrow(()->new ResourceNotFound("not found"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Title
        Paragraph header = new Paragraph("INVOICE")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(24)
                .setBold()
                .setMarginBottom(20);
        document.add(header);
        // Company + Customer Info
        document.add(new Paragraph("From: Cafe Management")
                .setMarginBottom(10));
        document.add(new Paragraph("To: "+order.getCustomerName())
                .setMarginBottom(20));
        document.add(new Paragraph("Bill Id: "+order.getBill_id())
                .setMarginBottom(20));

        // Table Header
        Table table = new Table(UnitValue.createPercentArray(new float[]{2,2, 1, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        table.addHeaderCell(new Cell().add(new Paragraph("Name").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Category").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Qty").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Unit Price").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()));

        // Split product IDs and fetch products
        String[] productIds = order.getProduct_id().split(",");
        double grandTotal = 0;

        for (String productIdStr : productIds) {

                Long productId = Long.parseLong(productIdStr.trim());
                ProductEntity product = productRepo.findById(productId)
                        .orElseThrow(() -> new ResourceNotFound("Product ID " + productId + " not found"));
                CategoryEntity category = categoryRepo.findById(product.getCategoryId()).orElseThrow(() -> new ResourceNotFound("Category ID " + product.getCategoryId() + " not found"));

                int quantity = 1; // Or get from order if available
//                double price = product.getPrice(); // assuming price is double
                double price =399; // assuming price is double
                double total = quantity * price;
                grandTotal += total;

                table.addCell("");
                table.addCell(String.valueOf(category.getName()));
                table.addCell(String.valueOf(quantity));
                table.addCell("Rs." + price);
                table.addCell("Rs." + total);


        }

        document.add(table);

        // Grand Total
        Paragraph total = new Paragraph("Grand Total: Rs." + grandTotal)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBold()
                .setFontSize(14)
                .setMarginBottom(30);
        document.add(total);


        // Footer
        Paragraph footer = new Paragraph("Thank you for your business!")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12)
                .setFontColor(Color.GRAY);
        document.add(footer);

        document.close();
        return out.toByteArray();
        } catch (Exception ex) {
            System.out.println("exception=="+ex.getMessage());
            throw new RuntimeException("error to generate invoice");
            // Handle malformed product ID gracefully
        }
    }
}
