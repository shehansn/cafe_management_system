package com.sn.shehan_n.cafe_management_system.controllerImpl;

import com.sn.shehan_n.cafe_management_system.controller.DashboardController;
import com.sn.shehan_n.cafe_management_system.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardControllerImpl implements DashboardController {

    private DashboardService dashboardService;

    public DashboardControllerImpl(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        return dashboardService.getCount();
    }
}
