package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.services.ResultService;
import com.results.HpcDashboard.util.GenerateCSVReport;
import com.results.HpcDashboard.util.GenerateExcelReport;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;


@Controller
public class ResultDashboardController {
    @Autowired
    ResultService resultService;

    @Autowired
    Util util;

    @ModelAttribute("command")
    public FormCommand formCommand() {
        return new FormCommand();
    }

    @GetMapping("/result")
    public String showResultForm() {
        return "resultForm";
    }

    @PostMapping("/result")
    public String insertResult(
            @ModelAttribute("command") @Valid FormCommand command,
            Model model) {

        String[] resultReturned = command.getTextareaField().split("!");
        for (String individualResult : resultReturned) {

            String[] resultData = individualResult.split(",");

            if (resultData.length > 8 && individualResult.contains("[")) {
                resultData = util.performRegex(individualResult);
            }

            if (resultData.length != 8) {
                return "redirect:/result?failure";
            }
            resultService.insertResult(resultData);
        }
        return "redirect:/result?success";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        List<String> cpu_list = resultService.getCpu();
        List<String> app_list = resultService.getApp();
        List<String> bm_list = null;
        List<Integer> node_list = resultService.getNodes();
        model.addAttribute("cpus", cpu_list);
        model.addAttribute("apps", app_list);
        model.addAttribute("nodes", node_list);
        model.addAttribute("bms", bm_list);
        return "resultDashboard";
    }

    @RequestMapping(value = "/bms", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllBMs(
            @RequestParam(value = "appName", required = true) String appName) {

        return resultService.getSelectBm(appName);
    }

    @GetMapping(value = "/resultsExcel")
    public ResponseEntity<InputStreamResource> excelResults() throws IOException {
        List<Result> results = resultService.getAllResults();
        ByteArrayInputStream in = GenerateExcelReport.resultsToExcel(results);
        HttpHeaders headers = new HttpHeaders();
        String str = "result_"+ LocalDate.now().toString()+".xlsx";
        headers.add("Content-Disposition", "attachment; filename="+str);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }

    @RequestMapping(value = "/resultsCsv", method = RequestMethod.GET)
    public void csvResults(HttpServletResponse response) throws IOException {
        List<Result> results = resultService.getAllResults();
        GenerateCSVReport.writeResults(response.getWriter(), results);
        String str = "result_"+ LocalDate.now().toString()+".csv";
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=results.csv");
    }

}
