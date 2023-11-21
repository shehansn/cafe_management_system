package com.sn.shehan_n.cafe_management_system.serviceImpl;

import com.sn.shehan_n.cafe_management_system.repository.BillRepository;
import com.sn.shehan_n.cafe_management_system.repository.CategoryRepository;
import com.sn.shehan_n.cafe_management_system.repository.ProductRepository;
import com.sn.shehan_n.cafe_management_system.repository.UserRepository;
import com.sn.shehan_n.cafe_management_system.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private BillRepository billRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public DashboardServiceImpl(BillRepository billRepository,
                                CategoryRepository categoryRepository,
                                ProductRepository productRepository,
                                UserRepository userRepository) {
        this.billRepository = billRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map=new HashMap<>();
        map.put("category",categoryRepository.count());
        map.put("product",productRepository.count());
        map.put("bill",billRepository.count());
        map.put("user",userRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
