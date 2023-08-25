package com.qavi.carmaintanence.business.utils;

import com.qavi.carmaintanence.business.entities.*;
import com.qavi.carmaintanence.business.models.InvoiceModel;
import com.qavi.carmaintanence.business.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class InvoiceConverter {

    @Autowired
    VehicleRepository vehicleRepository;

    public static InvoiceModel convertInvoiceToInvoiceModel(Invoice invoice, VehicleRepository vehicleRepository) {


        String registrationNumber = vehicleRepository.getRegistrationNumberFromId(invoice.getVehicleId());



        InvoiceModel invoiceModel= new InvoiceModel();
        invoiceModel.setId(invoice.getId());
        invoiceModel.setDate(invoice.getDate());
        invoiceModel.setInvoiceDue(invoice.getInvoiceDue());
        invoiceModel.setMaintainedById(invoice.getMaintainedById());
        invoiceModel.setRegistrationNumber(registrationNumber);
        invoiceModel.setTotal(invoice.getTotal());
        List<Item> descriptions = invoice.getDescriptions();
        List<Map<String, Object>> descriptionDataList = new ArrayList<>();
        for (Item description : descriptions) {
            descriptionDataList.add(convertDescriptionToMap(description));
        }
        invoiceModel.setDescriptions(descriptionDataList);

        List<Discount> discounts = invoice.getDiscounts();
        List<Map<String, Object>> discountDataList = new ArrayList<>();
        for (Discount discount : discounts) {
            discountDataList.add(convertDiscountToMap(discount));
        }
        invoiceModel.setDiscounts(discountDataList);

        List<Tax> taxes = invoice.getTaxes();
        List<Map<String, Object>> taxDataList = new ArrayList<>();
        for (Tax tax : taxes) {
            taxDataList.add(convertTaxToMap(tax));
        }
        invoiceModel.setTaxes(taxDataList);

        return invoiceModel;
    }

    // Example conversion methods
    private static Map<String, Object> convertDescriptionToMap(Item description) {
        Map<String, Object> descriptionMap = new HashMap<>();
        descriptionMap.put("item", description.getItem());
        descriptionMap.put("rate", description.getRate());
        descriptionMap.put("quantity", description.getQuantity());
        descriptionMap.put("amount", description.getAmount());
        return descriptionMap;
    }

    private static Map<String, Object> convertDiscountToMap(Discount discount) {
        Map<String, Object> discountMap = new HashMap<>();
        discountMap.put("discountName", discount.getDiscountName());
        discountMap.put("discountRate", discount.getDiscountRate());
        return discountMap;
    }

    private static Map<String, Object> convertTaxToMap(Tax tax) {
        Map<String, Object> taxMap = new HashMap<>();
        taxMap.put("taxName", tax.getTaxName());
        taxMap.put("taxRate", tax.getTaxRate());
        return taxMap;
    }





    public static Invoice convertInvoiceModelToInvoice(InvoiceModel model) {
        Invoice invoice = new Invoice();
        List<Map<String, Object>> fetchedTaxes = model.getTaxes();
        List<Map<String, Object>> fetchedDiscounts = model.getDiscounts();
        List<Map<String, Object>> fetchedDescriptions = model.getDescriptions();

        List<Tax> taxes = new ArrayList<>();
        List<Discount> discounts = new ArrayList<>();
        List<Item> descriptions = new ArrayList<>();

        for (Map<String, Object> taxMap : fetchedTaxes) {
            com.qavi.carmaintanence.business.entities.Tax tax = new Tax();
            tax.setTaxName((String) taxMap.get("taxName"));

            tax.setTaxRate(((Number) taxMap.get("taxRate")).doubleValue());
            taxes.add(tax);
        }
        invoice.setTaxes(taxes);

        for (Map<String, Object> discountMap : fetchedDiscounts) {
            com.qavi.carmaintanence.business.entities.Discount discount = new Discount();
            discount.setDiscountName((String) discountMap.get("discountName"));

            discount.setDiscountRate(((Number) discountMap.get("discountRate")).doubleValue());
            discounts.add(discount);
        }
        invoice.setDiscounts(discounts);

        for (Map<String, Object> descriptionMap : fetchedDescriptions) {
            Item description = new Item();
            description.setItem((String) descriptionMap.get("item"));

            description.setRate(((Number) descriptionMap.get("rate")).doubleValue());
            description.setQuantity((int) descriptionMap.get("quantity"));
            description.setAmount(((Number) descriptionMap.get("amount")).doubleValue());
            descriptions.add(description);
        }
        invoice.setDescriptions(descriptions);

        return invoice;
    }

}
