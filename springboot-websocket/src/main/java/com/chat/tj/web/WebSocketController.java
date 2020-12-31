package com.chat.tj.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.OnOpen;
import javax.websocket.Session;

@Slf4j
@Controller
public class WebSocketController {

    @RequestMapping("/websocket/{name}/{roomId}")
    public void webSocket(@PathVariable String name,@PathVariable String roomId, Model model) {
        try {
            log.info("跳转到websocket的页面上");
            model.addAttribute("username", name);
            model.addAttribute("roomId", roomId);
            // return "websocket";
        } catch (Exception e) {
            log.info("跳转到websocket的页面上发生异常，异常信息是：" + e.getMessage());
            // return "error";
        }
    }
}
