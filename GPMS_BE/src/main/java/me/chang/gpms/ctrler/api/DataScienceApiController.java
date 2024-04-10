package me.chang.gpms.ctrler.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ds")
@Tag(name = "DSAC", description = "DataScienceApiController")
public class DataScienceApiController {

}
