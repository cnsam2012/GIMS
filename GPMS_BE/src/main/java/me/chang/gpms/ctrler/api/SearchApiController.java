package me.chang.gpms.ctrler.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.chang.gpms.pojo.Page;
import me.chang.gpms.service.ElasticsearchService;
import me.chang.gpms.service.LikeService;
import me.chang.gpms.service.UserService;
import me.chang.gpms.util.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SAC", description = "SearchApiController")
@RestController
public class SearchApiController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    /**
     * TODO 根据关键词搜索
     *
     * @param keyword
     * @param page
     * @return
     */
    @Operation(summary = "SAC", description = "SAC")
    @GetMapping("api/search")
    @ResponseBody
    @Deprecated
    public R search(
            @RequestParam("keyword")
            @Parameter(name = "keyword")
            String keyword,
            Page page
    ) {
        return null;
//        Map<String, Object> data = new HashMap<>();
//        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
//            page.setLimit(5);
//        }
//
//        // 搜索帖子 (Spring 提供的 Page 当前页码从 0 开始计数)
//        org.springframework.data.domain.Page<DiscussPost> searchResult =
//                elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
//
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
//
//        // 设置分页
//        page.setPath("/search?keyword=" + keyword);
//        page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalElements());
//        data.put("page", page);
//
//        var status = HttpStatus.SC_OK;
//        resp.setStatus(status);
////        return BbUtil.getJSONString(status, "success", data);
//        return R.ok(status, "搜索", data);
    }


}
