package com.anayasmi.orderService.helpar;

import com.anayasmi.orderService.models.response.BillingAddressResponse;
import com.anayasmi.orderService.models.response.LineItemResponse;
import com.anayasmi.orderService.models.response.OrderResponse;
import com.anayasmi.orderService.models.response.ShippingAddressResponse;
import com.anayasmi.orderService.services.kafkaService.KafkaFileProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
@RequiredArgsConstructor
@Component
@Slf4j
public class OrderPdfGenerator {

    private final KafkaFileProcessService kafkaFileProcessService;
    public void generateOrderPDF(OrderResponse order) {
        try (PDDocument document = new PDDocument()) {

            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/static/NotoSans-Regular.ttf");

            if (fontStream == null) {
                System.err.println("Font file not found! Please check the resource path.");
                return;
            }
            PDType0Font font = PDType0Font.load(document, fontStream);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);
            float y = 750;
            float leading = 18;

            // Title
            content.beginText();
            content.setFont(font, 16);
            content.newLineAtOffset(50, y);
            content.showText("Order Receipt");
            content.endText();

            y -= leading * 2;

            // Order Info
            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Order ID: " + order.getOrderCustomId());
            content.endText();

            y -= leading;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Order Date: " + order.getOrderDate().toLocalDate());
            content.endText();

            y -= leading;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Payment Method: " + order.getPaymentMethod());
            content.endText();

            y -= leading * 2;

            // Customer Info
            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Customer: " + order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
            content.endText();

            y -= leading;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Email: " + order.getCustomer().getEmail());
            content.endText();

            y -= leading;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Phone: " + order.getCustomer().getPhone());
            content.endText();

            y -= leading * 2;

            // Addresses
            y = addAddressBlockS(content, font, "Shipping Address", order.getShippingAddress(), y, leading);
            y = addAddressBlockB(content, font, "Billing Address", order.getBillingAddress(), y, leading);

            // Line Items
            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Items:");
            content.endText();
            y -= leading;

            for (LineItemResponse item : order.getLineItems()) {
                content.beginText();
                content.setFont(font, 11);
                content.newLineAtOffset(55, y);
                content.showText("- " + item.getItemName() + " | Qty: " + item.getQuantity() + " | "
                        +"Item Total Price : "+"( ₹ " + item.getUnitPrice() + " x " + item.getQuantity() +" ) = ₹ " +item.getTotalPrice());
                content.endText();
                y -= leading;
            }

            y -= leading;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Shipping: ₹" + order.getShippingCost());
            content.endText();

            y -= leading;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Discount (" + order.getDiscount().getCode() + "): -₹" + order.getDiscount().getAmount());
            content.endText();

            y -= leading;

            content.beginText();
            content.setFont(font, 12);
            content.newLineAtOffset(50, y);
            content.showText("Tax (" + order.getTax().getRate() + "%): ₹" + order.getTax().getAmount());
            content.endText();

            y -= leading;

            content.beginText();
            content.setFont(font, 13);
            content.setLeading(14.5f);
            content.newLineAtOffset(50, y);
            content.showText("Total: ₹" + order.getOrderTotal());
            content.endText();

            content.close();

            // Save PDF
            String path = "orders/order_" + order.getOrderCustomId() + "_" + System.currentTimeMillis() + ".pdf";
            File outFile = new File(path);
            outFile.getParentFile().mkdirs();
            document.save(outFile);

            log.info("✅PDF Generated and saved to: " + path);
            log.info("✅ PDF file path successfully sent to Kafka.");
            kafkaFileProcessService.sendPdfFileToKafka(path);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float addAddressBlockS(PDPageContentStream content, PDType0Font font, String title, ShippingAddressResponse address, float y, float leading) throws Exception {
        content.beginText();
        content.setFont(font, 12);
        content.newLineAtOffset(50, y);
        content.showText(title + ":");
        content.endText();
        y -= leading;

        content.beginText();
        content.setFont(font, 11);
        content.newLineAtOffset(55, y);
        content.showText(address.getAddressLine1() + ", " + address.getAddressLine2() + ", " + address.getCity() + ", " + address.getState() + " - " + address.getZipCode() + ", " + address.getCountry());
        content.endText();
        y -= leading * 2;

        return y;
    }

    private float addAddressBlockB(PDPageContentStream content, PDType0Font font, String title, BillingAddressResponse address, float y, float leading) throws Exception {
        content.beginText();
        content.setFont(font, 12);
        content.newLineAtOffset(50, y);
        content.showText(title + ":");
        content.endText();
        y -= leading;

        content.beginText();
        content.setFont(font, 11);
        content.newLineAtOffset(55, y);
        content.showText(address.getAddressLine1() + ", " + address.getAddressLine2() + ", " + address.getCity() + ", " + address.getState() + " - " + address.getZipCode() + ", " + address.getCountry());
        content.endText();
        y -= leading * 2;

        return y;
    }

}
