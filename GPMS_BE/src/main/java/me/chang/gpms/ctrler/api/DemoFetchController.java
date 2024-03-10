package me.chang.gpms.ctrler.api;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Tag(name = "DFC", description = "Demo Fetch Controller")
@CrossOrigin
public class DemoFetchController {
    @RequestMapping(value = "api/_df", method = {RequestMethod.GET, RequestMethod.POST})
    @Operation(summary = "Contains demo data")
    @SecurityRequirement(name = "ticket")
    public R demoFetch(HttpServletResponse resp) {
        var data = new HashMap<String, Object>();

        var ints = new int[]{1, 2, 3, 4, 5};
        data.put("ints", ints);
        data.put("reMsg", "123nbnb");

        return R.ok(resp, 200, "success", data);
//        return R.ok(4001, "success", data);
    }

    @RequestMapping(value = "api/_page", method = {RequestMethod.GET})
    @Operation(summary = "Contains demo data for d2-crud-demo")
    @SecurityRequirement(name = "ticket")
    public R demoFetchData(HttpServletResponse resp) {

        /**
         *         current:1, //当前页码
         *         size:20, //每页记录条数
         *         total: 100, //总记录数
         *         records:[...] //数据列表
         *
         *  [
         *     {date: '2016-05-02',status: '0', province: 'sz'},
         *     {date: '2016-05-04',status: '1',province: 'sh,sz'},
         *     {date: 2232433534511,status: '1', province: 'gz'},  //支持各种时间类型
         *     {date: '2016-05-03',status: '2',province: 'wh,gz'}
         *   ]
         */

        var ret = new JSONObject();
        ret.set("current", 1);
        ret.set("size", 20);
        ret.set("total", 5);

        var records = new JSONArray();
        var js1 = new JSONObject();
        js1.set("date", "2016-05-02");
        js1.set("status", "0");
        js1.set("province", "sz");
        records.put(js1);
        js1 = new JSONObject();
        js1.set("date", "2016-05-02");
        js1.set("status", "0");
        js1.set("province", "sz");
        records.put(js1);
        js1 = new JSONObject();
        js1.set("date", "2016-05-02");
        js1.set("status", "0");
        js1.set("province", "sz");
        records.put(js1);
        js1 = new JSONObject();
        js1.set("date", "2016-05-02");
        js1.set("status", "0");
        js1.set("province", "sz");
        records.put(js1);

        ret.set("records", records);


        return R.ok(resp, 200, "success", ret);
//        return R.ok(4001, "success", data);
    }

    @RequestMapping(value = "api/_dp", method = {RequestMethod.POST})
    @Operation(summary = "Get data from request body and display it")
    @SecurityRequirement(name = "ticket")
    public R demoPush(HttpServletResponse resp, String testJson) {
        var data = new HashMap<String, Object>();
        data.put("dataRec", testJson);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "success",
                data
        );
    }
}
