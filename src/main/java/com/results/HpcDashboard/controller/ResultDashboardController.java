package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.services.ResultService;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
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
    public String showDashboard1(Model model) {

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

}
