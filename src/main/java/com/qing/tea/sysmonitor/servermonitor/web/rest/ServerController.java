package com.qing.tea.sysmonitor.servermonitor.web.rest;

import com.qing.tea.sysmonitor.servermonitor.Server;
import com.qing.tea.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor/server")
public class ServerController {

    @GetMapping()
    public R<Server> getInfo(){
        Server server = new Server();
        try {
            server.copyTo();
        }catch (Exception e){
            R.failed("获取信息异常");
        }
        return R.success(server);
    }
}
