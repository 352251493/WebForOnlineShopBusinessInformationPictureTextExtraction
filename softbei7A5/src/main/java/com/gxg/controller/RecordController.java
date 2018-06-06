package com.gxg.controller;

import com.gxg.services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 郭欣光 on 2018/5/28.
 */

@Controller
@RequestMapping(value = "/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping(value = "/get_all_img_number_and_process_time")
    @ResponseBody
    public String getAllImgNumberAndProcessTime() {
        return recordService.getAllImgNumberAndProcessTime();
    }

    @GetMapping(value = "/get_last_img_process_information")
    @ResponseBody
    public String getLastImgProcessInformation() {
        return recordService.getLastImgProcessInformation();
    }

    @GetMapping(value = "/get_last_7_process_average_time_and_process_number")
    @ResponseBody
    public String getLast7ProcessAverageTimeAndProcessNumber() {
        return recordService.getLast7ProcessAverageTimeAndProcessNumber();
    }

    @PostMapping(value = "/upload_process_img_information")
    @ResponseBody
    public String uploadProcessImgInformation(@RequestParam("img_src") String imgSrc, @RequestParam("save_src") String saveSrc, @RequestParam("process_number") int processNumber, HttpServletRequest request) {
        return recordService.uploadProcessImgInformation(imgSrc, saveSrc, processNumber, request);
    }

    @GetMapping(value = "/get_progress")
    @ResponseBody
    public String getProgress(HttpServletRequest request){
        return recordService.getProgress(request);
    }

    @GetMapping(value = "/get_process_result")
    @ResponseBody
    public String getProcessResult(HttpServletRequest request) {
        return recordService.getProcessResult(request);
    }
}
