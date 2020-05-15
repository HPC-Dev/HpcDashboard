package com.results.HpcDashboard.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.services.ResultService;
import com.results.HpcDashboard.util.GenerateCSVReport;
import com.results.HpcDashboard.util.GenerateExcelReport;
import com.results.HpcDashboard.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
            Model model, @RequestParam("file") MultipartFile file, Errors errors, RedirectAttributes redirectAttributes) {

        if(command.getRadioButtonSelectedValue().equals("paste")) {
            String[] resultReturned = command.getTextareaField().split("!");
            for (String individualResult : resultReturned) {

                String[] resultData = individualResult.split(",");

                if (resultData.length > 8 && individualResult.contains("[")) {
                    resultData = util.performRegex(individualResult);
                }

                if (resultData.length != 8) {
                    redirectAttributes.addFlashAttribute("failure", "Please provide date in the below format (job_id,app_name,bm_name,nodes,cores,node_name,result,cpu)");
                    return "redirect:/result";
                }
                try {
                    resultService.insertResult(resultData);
                }
                catch (Exception e){
                    redirectAttributes.addFlashAttribute("failMessage", ExceptionUtils.getRootCauseMessage(e));
                    return "redirect:/result";
                }
            }
        }
        else if(command.getRadioButtonSelectedValue().equals("upload")){
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("fileNotUploaded", "Please select a file to upload");
                return "redirect:/result";
            }

            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                CsvToBean<Result> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Result.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                try {
                    List<Result> results = csvToBean.parse();
                    resultService.insertResultCsv(results);
                }

                catch (Exception exception){
                    redirectAttributes.addFlashAttribute("exceptionMessage", ExceptionUtils.getRootCauseMessage(exception));
                    return "redirect:/result";
                }
            }
            catch (Exception ex) {
                System.out.println(ExceptionUtils.getStackTrace(ex));
            }

        }
        redirectAttributes.addFlashAttribute("successMessage", "Result is successfully inserted!");
        return "redirect:/result";
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
