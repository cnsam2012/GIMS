package me.chang.gpms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chang.gpms.dao.MessageMapper;
import me.chang.gpms.pojo.Message;
import me.chang.gpms.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 私信/系统通知相关
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    /**
     * 查询当前用户的会话数量
     * @param userId
     * @return
     */
    public int findConversationCout(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    /**
     * 查询某个会话所包含的私信列表
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }
    public List<Message> findLetters(String conversationId) {
        return messageMapper.selectAllLetters(conversationId);
    }

    /**
     * 查询某个会话所包含的私信数量
     * @param conversationId
     * @return
     */
    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    /**
     * 查询未读私信的数量
     * @param userId
     * @param conversationId
     * @return
     */
    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    /**
     * 读取私信(将私信状态设置为已读)
     * @param ids
     * @return
     */
    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

    /**
     * 添加一条私信
     * @param message
     * @return
     */
    public int addMessage(Message message) {
        // 转义 HTML 标签
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        // 过滤敏感词
        message.setContent(sensitiveFilter.filter(message.getContent()));

        return messageMapper.insertMessage(message);
    }

    public int emptyMessageByFromUserId(int id) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != 0, "from_id", id);
        return messageMapper.delete(queryWrapper);
    }

    /**
     * 查询某个主题下最新的系统通知
     * @param userId
     * @param topic
     * @return
     */
    public Message findLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }

    /**
     * 查询某个主题下包含的系统通知数量
     * @param userId
     * @param topic
     * @return
     */
    public int findNoticeCount(int userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    /**
     * 查询未读的系统通知数量
     * @param userId
     * @param topic
     * @return
     */
    public int findNoticeUnReadCount(int userId, String topic) {
        return messageMapper.selectNoticeUnReadCount(userId, topic);
    }

    /**
     * 查询某个主题所包含的通知列表
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findNotices(int userId, String topic, int offset, int limit) {
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }


    public int findMessageFromSystemUnread(int userId) {
        // Define the query conditions
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_id", userId)
                .eq("from_id", 910000);
        // Use MyBatis Plus to execute the query and return the count of unread letters
        int unreadCount = Math.toIntExact(messageMapper.selectCount(queryWrapper));
        return unreadCount;
    }
}
