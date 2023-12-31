package com.qavi.carmaintanence.business.controllers;

import com.qavi.carmaintanence.business.entities.Invoice;
import com.qavi.carmaintanence.business.entities.SalesReport;
import com.qavi.carmaintanence.business.models.InvoiceModel;
import com.qavi.carmaintanence.business.repositories.VehicleRepository;
import com.qavi.carmaintanence.business.services.InvoiceService;
import com.qavi.carmaintanence.business.utils.InvoiceConverter;
import com.qavi.carmaintanence.usermanagement.models.ResponseModel;
import com.qavi.carmaintanence.usermanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/invoice")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/get-invoices/{businessId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','OWNER')")
    @Transactional
    public ResponseEntity<List<InvoiceModel>> getAllInvoice(@PathVariable Long businessId){
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Invoices Recived Successfully")
                .data(new Object())
                .build();
        List<InvoiceModel> invoices=invoiceService.getAllInvoices(businessId);

            if(invoices.isEmpty()){
                responseModel.setStatus(HttpStatus.NOT_FOUND);
                responseModel.setMessage("Invoices not found");
                return new ResponseEntity<>(invoices, HttpStatus.NOT_FOUND);
            }
            else{
                return new ResponseEntity<>(invoices, HttpStatus.OK);
            }

    }
    @GetMapping("/get-invoice/{invoice_id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','OWNER')")
    @Transactional
    public ResponseEntity<InvoiceModel> getOneInvoice(@PathVariable("invoice_id") Long invoiceId) {
        InvoiceModel invoiceModel = invoiceService.getInvoice(invoiceId);

        if (invoiceModel != null) {

            ResponseModel responseModel = ResponseModel.builder()
                    .status(HttpStatus.OK)
                    .message("Invoice Details Found Successfully")
                    .data(invoiceModel)
                    .build();

            return new ResponseEntity<>(invoiceModel, HttpStatus.OK);
        } else {
            ResponseModel responseModel = ResponseModel.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Failed to Fetch Invoice Detail")
                    .build();

            return new ResponseEntity<>(invoiceModel, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/get-invoice-salesReport/{businessId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','OWNER')")
    @Transactional
    public ResponseEntity<?> getSalesReport(@RequestBody InvoiceModel invoiceModel, @PathVariable Long businessId) {
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Invoices Received Successfully")
                .data(new Object())
                .build();

        List<InvoiceModel> invoices = invoiceService.getInvoiceReport(invoiceModel, businessId);

        if (invoices.isEmpty()) {
            responseModel.setStatus(HttpStatus.NOT_FOUND);
            responseModel.setMessage("Invoices not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
        }



        return ResponseEntity.status(HttpStatus.OK).body(invoices);
    }




    @PostMapping("/businessId/{businessId}/create-invoice/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','OWNER')")
    public ResponseEntity<ResponseModel> createInvoice(@RequestBody InvoiceModel invoiceModel, @PathVariable Long businessId, @PathVariable Long id, @AuthenticationPrincipal String userId){
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Invoice Created Successfully")
                .data(new Object())
                .build();

        if(!invoiceService.addInvoice(invoiceModel,businessId,id,Long.valueOf(userId)))
        {
            responseModel.setMessage("Failed To Create Invoice");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @PutMapping("/edit-invoice/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','OWNER')")
    public ResponseEntity<ResponseModel> editInvoice(@RequestBody InvoiceModel invoiceModel,@PathVariable Long id){
        ResponseModel responseModel = ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Invoice updated Successfully")
                .data(new Object())
                .build();
        if(!invoiceService.editInvoice(invoiceModel,id))
        {
            responseModel.setMessage("Failed To Edit Invoice");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @PutMapping("/{Id}/delete-invoice")
    public ResponseEntity<ResponseModel> deleteInvoices(@PathVariable Long Id)
    {
        ResponseModel responseModel=ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Invoice has been Deleted Successfully")
                .data(new Object())
                .build();
        if(!invoiceService.deleteInvoice(Id))
        {
            responseModel.setMessage("Failed To Delete Invoice");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @GetMapping("/{businessId}/invoiceDue")
    @PreAuthorize("hasAnyRole('EMPLOYEE','OWNER')")
    public ResponseEntity<ResponseModel> getInvoiceDues(@PathVariable Long businessId)
    {
        ResponseModel responseModel=ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Invoice Due Successfully Retrieved")
                .data(new Object())
                .build();
        Long invoiceCount = invoiceService.getInvoiceDueCount(businessId);

        if (invoiceCount == null) {
            responseModel.setMessage("Failed To Get Invoice Dues");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        } else {
            responseModel.setData(invoiceCount);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }


}

