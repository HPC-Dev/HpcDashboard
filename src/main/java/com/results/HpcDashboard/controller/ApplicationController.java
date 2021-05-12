package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.*;
import com.results.HpcDashboard.repo.AppCategoryRepo;
import com.results.HpcDashboard.repo.AppMapRepo;
import com.results.HpcDashboard.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;

    @Autowired
    MessageSource messages;

    @Autowired
    AppMapRepo appMapRepo;

    @Autowired
    AppCategoryRepo appCategoryRepo;

    @GetMapping("/application")
    public String showApplication() {
        return "application";
    }


    @PostMapping(value = "/appJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertAppJson(@RequestBody List<Application> applications) {
        if (applications != null || applications.size() > 0)
            applicationService.insertAppCsv(applications);

        return new ResponseEntity("Success!", HttpStatus.OK);
    }

    @PostMapping(value = "/appMapJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertappMap(@RequestBody List<AppMap> appMaps) {
        if (appMaps != null || appMaps.size() > 0)
            applicationService.insertAppMapCSV(appMaps);
        return new ResponseEntity("Success!", HttpStatus.OK);
    }

    @GetMapping("/appMetricInsert")
    public String showappMetricInsert(Model model) {
        model.addAttribute("appMetricInsert", new AppMap());
        return "appMetricInsert";
    }

    @PostMapping("/appMetricInsert")
    public String submitAppStatus(@ModelAttribute AppMap appMap, final Locale locale, RedirectAttributes redirectAttrs) {

        appMapRepo.save(appMap);
        redirectAttrs.addFlashAttribute("insertSuccess", messages.getMessage("message.insertSuccess", null, locale));
        return "redirect:/appMetricInsert";

    }


    @GetMapping("/appCategoryInsert")
    public String showAppCategoryInsert(Model model) {
        model.addAttribute("appCategoryInsert", new AppCategory());
        return "appCategoryInsert";
    }

    @PostMapping("/appCategoryInsert")
    public String submitAppCategory(@ModelAttribute AppCategory appCategory, final Locale locale, RedirectAttributes redirectAttrs) {

        appCategoryRepo.save(appCategory);
        redirectAttrs.addFlashAttribute("insertSuccess", messages.getMessage("message.insertSuccess", null, locale));
        return "redirect:/appMetricInsert";

    }

}
