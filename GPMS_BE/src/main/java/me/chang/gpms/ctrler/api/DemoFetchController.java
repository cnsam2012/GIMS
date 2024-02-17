package me.chang.gpms.ctrler.api;


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

import java.util.HashMap;

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
