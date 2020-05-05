package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;


@Controller
public class AppController {

    @Autowired
    UserService userService;

    @Autowired
    AverageResultService averageResultService;

    @ModelAttribute("command")
    public FormCommand formCommand() {
        return new FormCommand();
    }

    @RequestMapping({ "/", "/index" })
    public String index(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if(user!=null){
            model.addAttribute("user", user.getFirstName());
        }
        return "index";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
     return "login";
    }

    @GetMapping("/mChart")
    public String showMultiCharts(Model model) {
        List<String> app_list = averageResultService.getApp();
        List<String> cpu_list = averageResultService.getCpu();
        model.addAttribute("cpus", cpu_list );
        model.addAttribute("apps", app_list );
        return "multiCharts";
    }

    @GetMapping("/charts")
    public String showCharts(Model model) {
        List<String> cpu_list = averageResultService.getCpu();
        model.addAttribute("cpus", cpu_list );
        return "charts";
    }

    @RequestMapping(value = "/bms", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllBMs(
            @RequestParam(value = "appName", required = true) String appName, @RequestParam(value = "cpu", required = true) String cpu) {

        return averageResultService.getSelectBm(appName,cpu);
    }

    @GetMapping("/data")
    public String showData(Model model) {

        List<String> app_list = averageResultService.getApp();
        List<String> cpu_list = averageResultService.getCpu();
        model.addAttribute("cpus", cpu_list );
        model.addAttribute("apps", app_list );
        return "result";
    }

    @GetMapping("/part-comparision")
    public String showDataComparision(Model model) {

        List<String> app_list = averageResultService.getApp();
        model.addAttribute("apps", app_list );
        return "partComparision";
    }

    @RequestMapping(value = "/cpus", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllCPUs(
            @RequestParam(value = "appName", required = true) String appName) {

        return averageResultService.getCpu(appName);
    }

    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllApps(
            @RequestParam(value = "cpu", required = true) String cpu) {

        return averageResultService.getApp(cpu);
    }
}
