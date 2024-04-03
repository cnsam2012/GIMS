<template>
  <el-tooltip effect="dark" :content="tooltipContent" placement="bottom">
    <el-button class="d2-ml-0 d2-mr btn-text can-hover" type="text" @click="handleClick">
      <el-badge v-if="messageUnread > 0" :max="99" :value="messageUnread">
        <d2-icon :name="messageUnread === 0 ? 'bell-o' : 'bell'" style="font-size: 20px"/>
      </el-badge>
      <d2-icon v-else name="bell-o" style="font-size: 20px"/>
    </el-button>
  </el-tooltip>
</template>

<script>
import { mapGetters, mapState } from 'vuex'

export default {
  computed: {
    ...mapGetters('d2admin', {
      messageUnread: 'message/unread'
    }),
    ...mapState('d2admin/message', [
      'unread'
    ]),
    tooltipContent () {
      return this.messageUnread === 0
        ? '没有新消息'
        : `${this.messageUnread} 条新消息`
    }
  },
  methods: {
    handleClick () {
      this.$router.push({
        name: 'myMessage'
      })
    }
  }
}
</script>
