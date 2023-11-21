package com.sn.shehan_n.cafe_management_system.service;

import com.sn.shehan_n.cafe_management_system.entity.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService{
    ResponseEntity<String> generateReport(Map<String, Object> requestData);

    ResponseEntity<List<Bill>> getBills();

    ResponseEntity<byte[]> getPdf(Map<String, Object> requestData);

    ResponseEntity<String> deleteBill(Integer id);
}
