package com.gxg.util;

import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by 郭欣光 on 2018/6/6.
 */

public class UDPClient implements Runnable{

    private HttpSession session;
    private JSONObject processInformation;
    private String host;
    private int port;
    private Thread thread;


    public UDPClient(HttpSession session, JSONObject processInformation, String host, int port) {
        this.session = session;
        this.processInformation = processInformation;
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        JSONObject progress = new JSONObject();
        String status = "success";
        Boolean isNotEnd = true;
        DatagramSocket datagramSocket = null;
        if (processInformation.has("src") && processInformation.has("save_src") && processInformation.has("process_number")) {
            try {
                datagramSocket = new DatagramSocket();
                String message = this.processInformation.toString();
                DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(this.host), this.port);
                datagramSocket.send(datagramPacket);
                while (isNotEnd) {
                    byte[] receBuf = new byte[1024];
                    DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
                    datagramSocket.receive(recePacket);
                    String receStr = new String(recePacket.getData(), 0, recePacket.getLength());
//                    System.out.println(receStr);
                    progress = new JSONObject(receStr);
                    if (progress.has("img_all") && progress.has("img_already") && progress.has("img_error") && progress.has("time")) {
                        status = "success";
                        if (progress.has("status")) {
                            progress.put("status", status);
                        } else {
                            progress.accumulate("status", status);
                        }
                        session.setAttribute("progress", progress);
                        int imgAll = Integer.parseInt(progress.getString("img_all"));
                        int imgAlready = Integer.parseInt(progress.getString("img_already"));
                        int imgError = Integer.parseInt(progress.getString("img_error"));
                        if (imgAll == (imgAlready + imgError)) {
                            isNotEnd = false;
                        }
                    } else {
                        isNotEnd = false;
                        status = "error";
                        if (progress.has("status")) {
                            progress.put("status", status);
                        } else {
                            progress.accumulate("status", status);
                        }
                        session.setAttribute("progress", progress);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
                status = "error";
                if (progress.has("status")) {
                    progress.put("status", status);
                } else {
                    progress.accumulate("status", status);
                }
                session.setAttribute("progress", progress);
                isNotEnd = false;
            } finally {
                if (datagramSocket != null) {
                    datagramSocket.close();
                }
            }
        } else {
            status = "error";
            progress.accumulate("status", status);
            session.setAttribute("process", progress);
        }
    }

    public void start() {
        if (thread == null) {
            System.out.println("发送图片处理请求线程启动！");
            thread = new Thread(this);
            thread.start();;
        }
    }
}
