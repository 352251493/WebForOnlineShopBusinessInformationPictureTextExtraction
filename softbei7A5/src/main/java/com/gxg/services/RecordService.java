package com.gxg.services;

import com.gxg.dao.RecordDao;
import com.gxg.entities.Record;
import com.gxg.util.UDPClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭欣光 on 2018/5/28.
 */

@Service
public class RecordService {

    @Autowired
    private RecordDao recordDao;

    @Value("${process.udp.host}")
    private String host;

    @Value("${process.upd.port}")
    private int port;


    public String getAllImgNumberAndProcessTime() {
        JSONObject jsonObject = new JSONObject();
        String status;
        int imgAll;
        double processTimeAll;
        if (recordDao.getRecordCount() == 0) {
            status = "success";
            imgAll = 0;
            processTimeAll = 0;
        } else {
            status = "success";
            imgAll = recordDao.getAllImgCount();
            processTimeAll = (double)recordDao.getAllProcessTime() / 1000;
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("img_all", imgAll);
        jsonObject.accumulate("process_time_all", processTimeAll);
        return jsonObject.toString();
    }

    public String getLastImgProcessInformation() {
        JSONObject jsonObject = new JSONObject();
        String status;
        int lastImgAll;
        JSONObject compareImgAll = new JSONObject();
        String compareImgAllStatus;
        double compareImgAllNumber;
        double lastProcessTime;
        JSONObject compareProcessTime = new JSONObject();
        String compareProcessTimeStatus;
        double compareProcessTimeNumber;
        double lastAverageProcessTime;
        JSONObject compareAverageProcessTime = new JSONObject();
        String compareAverageProcessTimeStatus;
        double compareAverageProcessTimeNumber;
        int lastProcessNumber;
        JSONObject compareProcessNumber = new JSONObject();
        String compareProcessNumberStatus;
        double compareProcessNumberNumber;
        if (recordDao.getRecordCount() == 0) {
            status = "success";
            lastImgAll = 0;
            compareImgAllStatus = "up";
            compareImgAllNumber = 0;
            lastProcessTime = 0;
            compareProcessTimeStatus = "down";
            compareProcessTimeNumber = 0;
            lastAverageProcessTime = 0;
            compareAverageProcessTimeStatus = "down";
            compareAverageProcessTimeNumber = 0;
            lastProcessNumber = 0;
            compareProcessNumberStatus = "up";
            compareProcessNumberNumber = 0;
        } else {
            status = "success";
            List<Record> recordList = recordDao.getRecordListByOrderByTimeDescLimitStartAndEnd(0, 1);
            Record record = recordList.get(0);
            lastImgAll = record.getImgAll();
            lastProcessTime = (double)record.getProcessTime() / 1000;
            lastAverageProcessTime = lastProcessTime / lastImgAll;
            lastProcessNumber = record.getProcessNumber();
            if (recordDao.getRecordCount() == 1) {
                compareImgAllStatus = "up";
                compareImgAllNumber = 100;
                compareProcessTimeStatus = "down";
                compareProcessTimeNumber = 100;
                compareAverageProcessTimeStatus = "down";
                compareAverageProcessTimeNumber = 100;
                compareProcessNumberStatus = "up";
                compareProcessNumberNumber = 100;
            } else {
                List<Record> secondToLastRecordList = recordDao.getRecordListByOrderByTimeDescLimitStartAndEnd(1, 1);
                Record secondToLastRecord = secondToLastRecordList.get(0);
                int secondToLastImgAll = secondToLastRecord.getImgAll();
                double secondToLastProcessTime = (double)secondToLastRecord.getProcessTime() / 1000;
                double secondToLastAverageProcessTime = secondToLastProcessTime / secondToLastImgAll;
                int secondToLastProcessNumber = secondToLastRecord.getProcessNumber();
                if (secondToLastImgAll > lastImgAll) {
                    compareImgAllStatus = "down";
                    compareImgAllNumber = ((double)(secondToLastImgAll - lastImgAll) / (double)secondToLastImgAll) * 100;
                } else {
                    compareImgAllStatus = "up";
                    compareImgAllNumber = ((double)(lastImgAll - secondToLastImgAll) / (double)secondToLastImgAll) * 100;
                }
                if (secondToLastProcessTime > lastProcessTime) {
                    compareProcessTimeStatus = "down";
                    compareProcessTimeNumber = ((secondToLastProcessTime - lastProcessTime) / secondToLastProcessTime) * 100;
                } else {
                    compareProcessTimeStatus = "up";
                    compareProcessTimeNumber = ((lastProcessTime - secondToLastProcessTime) / secondToLastProcessTime) * 100;
                }
                if (secondToLastAverageProcessTime > lastAverageProcessTime) {
                    compareAverageProcessTimeStatus = "down";
                    compareAverageProcessTimeNumber = ((secondToLastAverageProcessTime - lastAverageProcessTime) / secondToLastAverageProcessTime) * 100;
                } else {
                    compareAverageProcessTimeStatus = "up";
                    compareAverageProcessTimeNumber = ((lastAverageProcessTime - secondToLastAverageProcessTime) / secondToLastAverageProcessTime) * 100;
                }
                if (secondToLastProcessNumber > lastProcessNumber) {
                    compareProcessNumberStatus = "down";
                    compareProcessNumberNumber = ((double)(secondToLastProcessNumber - lastProcessNumber) / secondToLastProcessNumber) * 100;
                } else {
                    compareProcessNumberStatus = "up";
                    compareProcessNumberNumber = ((double)(lastProcessNumber - secondToLastProcessNumber) / secondToLastProcessNumber) * 100;
                }
            }
        }
        compareImgAll.accumulate("status", compareImgAllStatus);
        compareImgAll.accumulate("number", compareImgAllNumber);
        compareProcessTime.accumulate("status", compareProcessTimeStatus);
        compareProcessTime.accumulate("number", compareProcessTimeNumber);
        compareAverageProcessTime.accumulate("status", compareAverageProcessTimeStatus);
        compareAverageProcessTime.accumulate("number", compareAverageProcessTimeNumber);
        compareProcessNumber.accumulate("status", compareProcessNumberStatus);
        compareProcessNumber.accumulate("number", compareProcessNumberNumber);
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("last_img_all", lastImgAll);
        jsonObject.accumulate("compare_img_all", compareImgAll);
        jsonObject.accumulate("last_process_time", lastProcessTime);
        jsonObject.accumulate("compare_process_time", compareProcessTime);
        jsonObject.accumulate("last_average_process_time", lastAverageProcessTime);
        jsonObject.accumulate("compare_average_process_time", compareAverageProcessTime);
        jsonObject.accumulate("last_process_number", lastProcessNumber);
        jsonObject.accumulate("compare_process_number", compareProcessNumber);
        return jsonObject.toString();
    }

    public String getLast7ProcessAverageTimeAndProcessNumber() {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        ArrayList<Double> averageTimeList = new ArrayList<>();
        ArrayList<Integer> processNumberList = new ArrayList<>();
        ArrayList<String> dataList = new ArrayList<>();
        if (recordDao.getRecordCount() == 0) {
            for (int i = 0 ; i < 7; i ++) {
                averageTimeList.add(0.0);
                processNumberList.add(0);
                dataList.add("0");
            }
        } else {
            List<Record> recordList = recordDao.getRecordListByOrderByTimeDescLimitStartAndEnd(0, 7);
            for (int i = recordList.size(); i < 7; i++) {
                averageTimeList.add(0.0);
                processNumberList.add(0);
                dataList.add("0");
            }
            for (int i = recordList.size() - 1; i >= 0; i--) {
                Record record = recordList.get(i);
                averageTimeList.add(((double)record.getProcessTime() / 1000) / record.getImgAll());
                processNumberList.add(record.getProcessNumber());
                dataList.add(record.getTime().toString().split("\\.")[0]);
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("average_time", averageTimeList);
        jsonObject.accumulate("process_number", processNumberList);
        jsonObject.accumulate("data", dataList);
        return jsonObject.toString();
    }

    public String uploadProcessImgInformation(String imgSrc, String saveSrc, int processNumber, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        if (imgSrc == null || "".equals(imgSrc)) {
            status = "error";
            message = "图片路径为空！";
        } else {
            if (imgSrc.length() > 500) {
                status = "error";
                message = "图片路径长度过长！";
            } else {
                if (saveSrc == null || "".equals(saveSrc)) {
                    status = "error";
                    message = "保存excel的路径为空！";
                } else {
                    if (saveSrc.length() > 500) {
                        status = "error";
                        message = "保存excel的路径长度过长！";
                    } else {
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        String timeString = time.toString();
                        String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                        Record record = new Record();
                        record.setId(id);
                        record.setImgSrc(imgSrc);
                        record.setSaveSrc(saveSrc);
                        record.setProcessNumber(processNumber);
                        record.setTime(time);
                        HttpSession session = request.getSession();
                        session.setAttribute("record", record);
                        JSONObject processInformation = new JSONObject();
                        processInformation.accumulate("src", imgSrc);
                        processInformation.accumulate("save_src", saveSrc);
                        processInformation.accumulate("process_number", processNumber + "");
                        UDPClient udpClient = new UDPClient(session, processInformation, host, port);
                        udpClient.start();
                        message = "记录成功！";
                    }
                }
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public String getProgress(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        HttpSession session = request.getSession();
        if (session.getAttribute("progress") == null) {
            status = "success";
            jsonObject.accumulate("img_all", "-1");
            jsonObject.accumulate("img_already", "0");
            jsonObject.accumulate("img_error", "0");
            jsonObject.accumulate("time", "0");
            jsonObject.accumulate("status", status);
        } else {
            JSONObject process = (JSONObject)session.getAttribute("progress");
            if ("error".equals(process.getString("status"))) {
                status = "error";
                String message = "udp出错！";
                jsonObject.accumulate("message", message);
                jsonObject.accumulate("status", status);
            } else {
                jsonObject = process;
                int imgAll = Integer.parseInt(process.getString("img_all"));
                int imgAlready = Integer.parseInt(process.getString("img_already"));
                int imgError = Integer.parseInt(process.getString("img_error"));
                int time = Integer.parseInt(process.getString("time"));
                if (imgAll == (imgAlready + imgError)) {
                    session.setAttribute("progress", null);
                    if (session.getAttribute("record") == null) {
                        System.out.println("没有记录信息！");
                    } else {
                        Record record = (Record) session.getAttribute("record");
                        record.setImgAll(imgAll);
                        record.setImgAlready(imgAlready);
                        record.setImgError(imgError);
                        record.setProcessTime(time);
                        session.setAttribute("record", record);
                        try {
                            System.out.println(record.toJSON().toString());
                            recordDao.insertRecord(record);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    public String getProcessResult(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        HttpSession session = request.getSession();
        if (session.getAttribute("record") == null) {
            status = "error";
            String message = "没有记录！";
            jsonObject.accumulate("message", message);
;        } else {
            Record record = (Record)session.getAttribute("record");
            if (record.toJSON().has("img_all")) {
                jsonObject.accumulate("record", record.toJSON());
            } else {
                status = "error";
                String message = "尚未处理完成！";
                jsonObject.accumulate("message", message);
            }
        }
        jsonObject.accumulate("status", status);
        return jsonObject.toString();
    }
}
