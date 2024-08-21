package com.dhan.pdf_generator.java.controller;

import com.dhan.pdf_generator.java.model.Request;
import com.dhan.pdf_generator.java.services.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONException;

import java.io.*;
import java.util.Map;
import java.util.Set;

@RestController
public class PdfGenerator {

    @Value("${sourceBasePathTemplate}")
    String sourceBasePathTemplate;

    @Value("${destinationBasePathTemplate}")
    String destinationBasePathTemplate;
    @Autowired
    ApiService apiService;


    String LocaldirectorySrc = "src/main/templates/source";
    String LocaldirectoryDes = "src/main/templates/destination";





    @RequestMapping(value = "/generatePdf", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generatePdf(@RequestBody Object inputPayload, @RequestHeader Map<String, String> headers) throws JSONException, IOException, DocumentException {

        JSONObject request_body =  new JSONObject(new Gson().toJson(inputPayload));
        JSONObject request =  request_body.getJSONObject("request_body");


        try {

            if (request_body.get("request_type").toString().equals("PDF_LOCAL")) {

                String source_path = sourceBasePathTemplate;
                String destination_path = destinationBasePathTemplate;
                if (request.has("SourcePath")) {
                    source_path = request.get("SourcePath").toString();
                }
                if (request.has("DestinationPath")) {
                    destination_path = request.get("DestinationPath").toString();
                }

                File src = new File(source_path);
                File dest = new File(destination_path);
                FileUtils.copyFile(src, dest);

                PdfReader pdfReader = new PdfReader(source_path);
                AcroFields acroFields = pdfReader.getAcroFields();
                Set<String> entrySet = acroFields.getFields().keySet();

                PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(destination_path));
                AcroFields form = pdfStamper.getAcroFields();

                for (String k : entrySet) {
                    if (request.has(k)) {
                        JSONObject field = request.getJSONObject(k);
                        switch (field.getString("AcroFieldType")) {
                            case "check":
                                if (field.has("AcroFieldValue") && field.getString("AcroFieldValue").equals("Yes")) {
                                    form.setField(k, "Yes", true);
                                } else {
                                    // check field not found error
                                    System.out.println(" check field not found error");
                                }
                                break;
                            case "text":
                                if (field.has("AcroFieldValue")) {
                                    form.setField(k, field.getString("AcroFieldValue"));
                                } else {
                                    // value field not found error
                                    System.out.println("value field not found error");
                                }
                                break;
                            case "image":
                                if (field.has("AcroFieldImagePath")) {
                                    java.util.List<AcroFields.FieldPosition> page01Photograph = form.getFieldPositions(k);
                                    if (page01Photograph != null && page01Photograph.size() > 0) {

                                        Rectangle rect = page01Photograph.get(0).position;

                                        Image img = Image.getInstance(field.getString("AcroFieldImagePath"));

                                        img.scaleToFit(rect.getWidth(), rect.getHeight());
                                        img.setBorder(2);
                                        img.setAbsolutePosition(
                                                page01Photograph.get(0).position.getLeft()
                                                , page01Photograph.get(0).position.getTop() - (rect.getHeight()));
                                        PdfContentByte cb = pdfStamper.getOverContent((int) page01Photograph.get(0).page);
                                        cb.addImage(img);
                                        cb.saveState();

                                    }
                                } else {
                                    // Acro image path not found error
                                    System.out.println("Acro image path not found error");
                                }
                                break;
                            default:
                                break;

                        }
                    }
                }

                pdfStamper.setFormFlattening(true);
                pdfStamper.close();
                pdfReader.close();
                pdfStamper.setFullCompression();
            } else if (request_body.get("request_type").toString().equals("PDF_REMOTE")) {

                // coming soon download file from remote
                System.out.println("coming soon download file from remote");

                String source_path = sourceBasePathTemplate;
                String destination_path = destinationBasePathTemplate;

                if (request.has("SourcePath")) {
                    source_path = request.get("SourcePath").toString();
                }
                if (request.has("DestinationPath")) {
                    destination_path = request.get("DestinationPath").toString();
                }

                String source_pdf  = LocaldirectorySrc+"/Template1.pdf";
                String destination_pdf  = LocaldirectorySrc+"/Template1.pdf";

                apiService.downloadImage(source_path ,source_pdf);

                File src = new File(LocaldirectorySrc);
                File dest = new File(LocaldirectoryDes);
                FileUtils.copyDirectory(src, dest);


                PdfReader pdfReader = new PdfReader(source_pdf);
                AcroFields acroFields = pdfReader.getAcroFields();
                Set<String> entrySet = acroFields.getFields().keySet();

                PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(destination_pdf));
                AcroFields form = pdfStamper.getAcroFields();

                for (String k : entrySet) {
                    if (request.has(k)) {
                        JSONObject field = request.getJSONObject(k);
                        switch (field.getString("AcroFieldType")) {
                            case "check":
                                if (field.has("AcroFieldValue") && field.getString("AcroFieldValue").equals("Yes")) {
                                    form.setField(k, "Yes", true);
                                } else {
                                    // check field not found error
                                    System.out.println(" check field not found error");
                                }
                                break;
                            case "text":
                                if (field.has("AcroFieldValue")) {
                                    form.setField(k, field.getString("AcroFieldValue"));
                                } else {
                                    // value field not found error
                                    System.out.println("value field not found error");
                                }
                                break;
                            case "image":
                                if (field.has("AcroFieldImagePath")) {
                                    java.util.List<AcroFields.FieldPosition> page01Photograph = form.getFieldPositions(k);
                                    if (page01Photograph != null && page01Photograph.size() > 0) {

                                        String source_image_path = LocaldirectorySrc+"/"+k+".png";

                                        apiService.downloadImage(field.getString("AcroFieldImagePath"),source_image_path);

                                        Rectangle rect = page01Photograph.get(0).position;

                                        Image img = Image.getInstance(source_image_path);

                                        img.scaleToFit(rect.getWidth(), rect.getHeight());
                                        img.setBorder(2);
                                        img.setAbsolutePosition(
                                                page01Photograph.get(0).position.getLeft()
                                                , page01Photograph.get(0).position.getTop() - (rect.getHeight()));
                                        PdfContentByte cb = pdfStamper.getOverContent((int) page01Photograph.get(0).page);
                                        cb.addImage(img);
                                        cb.saveState();

                                    }
                                } else {
                                    // Acro image path not found error
                                    System.out.println("Acro image path not found error");
                                }
                                break;
                            default:
                                break;

                        }
                    }
                }

                pdfStamper.setFormFlattening(true);
                pdfStamper.close();
                pdfReader.close();
                pdfStamper.setFullCompression();

                apiService.uploadFile(destination_pdf,destination_path);
            }

        }catch (Exception e){
            System.out.println("Exception Message "+e.getMessage().toString()+" StrackTrace "+e.getStackTrace().toString());
        }
        apiService.clearDirectory(new File(LocaldirectorySrc));
        apiService.clearDirectory(new File(LocaldirectoryDes));

        return ResponseEntity.ok().body(new JSONObject());
    }

}
