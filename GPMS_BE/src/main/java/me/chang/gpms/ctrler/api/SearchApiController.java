package me.chang.gpms.ctrler.api;


import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.chang.gpms.pojo.Page;
import me.chang.gpms.pojo.Report;
import me.chang.gpms.pojo.User;
import me.chang.gpms.pojo.ro.PageWithFuzzyRo;
import me.chang.gpms.service.ElasticsearchService;
import me.chang.gpms.service.LikeService;
import me.chang.gpms.service.ReportService;
import me.chang.gpms.service.UserService;
import me.chang.gpms.util.R;

import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "SAC", description = "SearchApiController")
@RestController
public class SearchApiController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    /**
     *
     * @param pageWithFuzzyRo
     * @return
     */
    @Operation(summary = "SAC", description = "SAC")
    @PostMapping("api/search")
    @ResponseBody
    public R search(
            @RequestBody
            PageWithFuzzyRo pageWithFuzzyRo
    ) {
//        return null;
        var page = pageWithFuzzyRo;
        Map<String, Object> data = new HashMap<>();
        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
            page.setLimit(5);
        }

        // 搜索帖子 (Spring 提供的 Page 当前页码从 0 开始计数)
//        org.springframework.data.domain.Page<Report> searchResult =
//                elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        var res = reportService.searchReports(page.getKeywords(), page.getCurrent(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (res != null) {
            for (Report post : res) {
                Map<String, Object> map = new HashMap<>();
                map.put("reports", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
//                long likeCount = likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_POST.value(), post.getId());
//                map.put("likeCount", likeCount);
                var userId = post.getLastedEditUserId();
                User lEuser = userService.findUserById(userId);
                map.put("latestEditUserName", lEuser.getUsername());
                map.put("latestEditRoleName", lEuser.getRoleName());
                discussPosts.add(map);
            }
        }
        page.setRows(res.size());
        data.put("page", page);
        data.put("reports", discussPosts);
//        // 聚合数据
//        List<Map<String, Object>> discussPosts = new ArrayList<>();
//        if (searchResult != null) {
//            for (DiscussPost post : searchResult) {
//                Map<String, Object> map = new HashMap<>();
//                // 帖子
//                map.put("post", post);
//                // 作者
//                map.put("user", userService.findUserById(post.getUserId()));
//                // 点赞数量
//                map.put("likeCount", likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_POST.value(), post.getId()));
//
//                discussPosts.add(map);
//            }
//        }
//
//        data.put("discussPosts", discussPosts);
//        data.put("keyword", keyword);
//        // 设置分页
//        page.setPath("/search?keyword=" + keyword);
//        page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalElements());
//        data.put("page", page);

//        return BbUtil.getJSONString(status, "success", data);
        return R.ok(GPMSResponseCode.OK.value(), "搜索", data);
    }


}
