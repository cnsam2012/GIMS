<template>
  <d2-container>
    <template slot="header" style="padding: 0">
      <h1>
        新建公告
      </h1>
    </template>

    <el-input
      type="textarea"
      :rows="2"
      placeholder="请输入内容"
      v-model="text">
    </el-input>

    <div style="margin-top: 30px;">
      <el-button @click="submit" type="success">确认无误，发布公告</el-button>
      <el-button @click="toAddAnno" type="warning">查看已发布公告</el-button>
      <el-button @click="closeThisTag" type="info">取消</el-button>
    </div>
    <template slot="footer">
      <random-motto/>
    </template>
  </d2-container>
</template>
<script>
import { mapActions, mapState } from 'vuex'

export default {
  // TODO 新建报告未接入
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ]),
    ...mapState('d2admin/page', [
      'current',
      'opened'
    ])
  },
  data () {
    return {
      text: '示例公告'
    }
  },
  async mounted () {
    // this.$refs.editor.focus()
  },
  // beforeDestroy () {
  // },
  methods: {
    ...mapActions('d2admin/page', [
      'close'
    ]),
    closeThisTag () {
      this.close({ tagName: '/addAnnouncement' })
    },
    toAddAnno () {
      this.$router.push({ name: 'announcement' })
    },
    async submit () {
      var data = {
        content: this.text
      }
      await this.$api.SEND_ANNOUNCEMENT(data)
      this.$message.info('发布成功')
      this.closeThisTag()
    }
  }
}
</script>
<style>
.d2-layout-header-aside-group .d2-layout-header-aside-content .d2-theme-container .d2-theme-container-main .d2-theme-container-main-body .container-component .d2-container-full .d2-container-full__header {
  padding: 0px 20px 0px 20px;
}
</style>
