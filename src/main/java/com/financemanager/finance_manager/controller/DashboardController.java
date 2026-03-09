package com.financemanager.finance_manager.controller;

import com.financemanager.finance_manager.dto.dashboard.DashboardResponse;
import com.financemanager.finance_manager.model.UserModel;
import com.financemanager.finance_manager.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // GET /api/dashboard?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
    @GetMapping
    public ResponseEntity<DashboardResponse> getSummary(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @AuthenticationPrincipal UserModel user
            ){
        return ResponseEntity.ok(dashboardService.getSummary(user, startDate, endDate));
    }
}
