package com.sn.shehan_n.cafe_management_system.serviceImpl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sn.shehan_n.cafe_management_system.auth.filter.TokenAuthenticationFilter;
import com.sn.shehan_n.cafe_management_system.commons.constants.Constants;
import com.sn.shehan_n.cafe_management_system.commons.util.Utils;
import com.sn.shehan_n.cafe_management_system.entity.Bill;
import com.sn.shehan_n.cafe_management_system.entity.User;
import com.sn.shehan_n.cafe_management_system.repository.BillRepository;
import com.sn.shehan_n.cafe_management_system.repository.UserRepository;
import com.sn.shehan_n.cafe_management_system.service.BillService;
import jdk.jshell.execution.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl implements BillService {
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final BillRepository billRepository;
    private final UserRepository userRepository;

    public BillServiceImpl(TokenAuthenticationFilter tokenAuthenticationFilter, BillRepository billRepository, UserRepository userRepository) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.billRepository = billRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestData) {
        try {
            String fileName;
            if (validateRequestData(requestData)) {
                if (requestData.containsKey("isGenerate") && !(Boolean) requestData.containsKey("isGenerate")) {
                    fileName = (String) requestData.get("uuid");
                } else {
                    fileName = Utils.getUUID();
                    requestData.put("uuid", fileName);
                    saveBill(requestData);
                }

                String data = "Name: " + requestData.get("name") + "\n" +
                        "Contact Number: " +  requestData.get("contactNo") + "\n" +
                        "Email: " + requestData.get("email") + "\n" +
                        "Payment Method: " + requestData.get("paymentMethod");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(Constants.STORE_LOCATION + "\\" + fileName + ".pdf"));

                document.open();
                setRectangleInPdf(document);

                Paragraph chunk = new Paragraph("Cafe Management System", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph=new Paragraph(data+"\n \n",getFont("Data"));
                document.add(paragraph);

                PdfPTable table=new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

               JSONArray jsonArray= Utils.getJsonArrayFromString((String) requestData.get("productDetails"));

               for (int i=0; i< jsonArray.length();i++){
                   addRows(table,Utils.getMapFromJson(jsonArray.getString(i)));
               }

               document.add(table);

               Paragraph footer=new Paragraph("Total :"+requestData.get("total")
               +"\n " + "Thank You For Visiting.Please visit again",getFont("Data"));

               document.add(footer);
               document.close();
               return new ResponseEntity<>("{\"uuid \" : \" "+fileName + "\" }",HttpStatus.OK);

            }
            return Utils.getResponseEntity("REQUIRED DATA NOT FOUND", HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Utils.getResponseEntity("ERROR WHEN GENERATING REPORT", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list =new ArrayList<>();
        if (tokenAuthenticationFilter.isAdmin()){
            list=billRepository.findAll();
        }else{
            User user=userRepository.findByEmail(tokenAuthenticationFilter.getCurrentUser());
//            list=billRepository.getAllByUserName(tokenAuthenticationFilter.getCurrentUser());
            list=billRepository.getAllByUserID(user.getId().toString());
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestData) {
        log.info("Info getPdf : requestMap {}",requestData);
        try {
            byte[] byteArray=new byte[0];
            if(!requestData.containsKey("uuid") && validateRequestData(requestData)){
                return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
            }else {
                String filePath=Constants.STORE_LOCATION+"\\"+(String) requestData.get("uuid") + ".pdf";
                if (Utils.isFileExist(filePath)){
                    byteArray=getByteArray(filePath);
                    return new ResponseEntity<>(byteArray,HttpStatus.OK);
                }
                else {
                    requestData.put("isGenerate",false);
                    generateReport(requestData);
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray,HttpStatus.OK);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        log.info("starting delete bill {}",id);
        try {
            Optional bill = billRepository.findById(id);
            if (!bill.isEmpty()){
                billRepository.deleteById(id);
                return Utils.getResponseEntity("Bill Deleted Successfully",HttpStatus.OK);
            }
            return Utils.getResponseEntity("Bill Id Doesnt Exists",HttpStatus.BAD_REQUEST);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity("ERROR WHEN DELETING BILL", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private byte[] getByteArray(String filePath) throws Exception{
        File initialFile=new File(filePath);
        InputStream targetStream=new FileInputStream(initialFile);
        byte[] byteArray= IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside add rows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("inside add table header");
        Stream.of("Name","Category","Quantity","Price","Sub Total")
                .forEach(columnTitle->{
                    PdfPCell header=new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(0);
//                    header.setPhrase(new Phrase(columnTitle,getFont("Data2")));
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    header.setPaddingBottom(2);
                    table.addCell(header);
                });
    }

    private Font getFont(String type) {
        log.info("inside get font");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;

            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;

            case "Data2":
                Font dataFont2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.WHITE);
                dataFont2.setStyle(Font.BOLD);
                return dataFont2;

            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("inside set rectangle in pdf");
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private void saveBill(Map<String, Object> requestData) {
        String userName = tokenAuthenticationFilter.getCurrentUser();
        User user= userRepository.findByEmail(userName);
        log.info(String.valueOf(user.getId()));
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestData.get("uuid"));
            bill.setName((String) requestData.get("name"));
            bill.setEmail((String) requestData.get("email"));
            bill.setContactNo((String) requestData.get("contactNo"));
            bill.setPaymentMethod((String) requestData.get("paymentMethod"));
            bill.setTotal(Double.parseDouble((String) requestData.get("total")));
            bill.setProductDetails((String) requestData.get("productDetails"));
            bill.setCreatedBy(String.valueOf(user.getId()));
//            bill.setCreatedBy(String.valueOf(user.getId()));
//            bill.setCreatedBy(user.getId());
            billRepository.save(bill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestData(Map<String, Object> requestData) {
        return requestData.containsKey("name") &&
                requestData.containsKey("contactNo") &&
                requestData.containsKey("email") &&
                requestData.containsKey("paymentMethod") &&
                requestData.containsKey("productDetails") &&
                requestData.containsKey("total");
    }
}
