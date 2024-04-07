<template>
  <d2-container>
    <template slot="header" style="padding: 0">
      <h1>
        新建报告
      </h1>
    </template>
    <el-input v-model="title" placeholder="请输入标题"></el-input>
    <div style="margin-top: 30px">
      <v-md-editor
        v-model="text"
        height="400px"
        ref="editor"
        :autofocus="true"
        left-toolbar="undo redo clear | h bold italic strikethrough quote | ul ol table hr | link code | save"
      ></v-md-editor>
    </div>
    <div style="margin-top: 30px">
      <el-select v-model="reportType" placeholder="请选择报告类型" style="margin-right: 30px">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button @click="submit(0)" type="success">确认无误，提交报告</el-button>
      <el-button @click="submit(1)" type="info">保存为草稿</el-button>
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
      text: '## 实习月记大纲\n' +
        '\n' +
        '**日期**：\n' +
        '**工作内容**：\n' +
        '**学习收获**：\n' +
        '**困难与解决**：\n' +
        '**感想与建议**：\n' +
        '\n' +
        '## 周记大纲\n' +
        '\n' +
        '**周数**：\n' +
        '**工作内容**：\n' +
        '**学习收获**：\n' +
        '**困难与解决**：\n' +
        '**工作反思**：\n' +
        '**下周计划**：\n' +
        '\n' +
        '## 总结大纲\n' +
        '\n' +
        '**实习概述**：\n' +
        '**工作内容**：\n' +
        '**学习收获**：\n' +
        '**成果展示**：\n' +
        '**挑战与成长**：\n' +
        '**感谢和反馈**：\n' +
        '**职业规划**：',
      title: '',
      reportType: '',
      options: [{
        value: '1',
        label: '周记'
      }, {
        value: '2',
        label: '月记'
      }, {
        value: '3',
        label: '总结'
      }]
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
      this.close({ tagName: '/addReports' })
    },
    async submit (judge) {
      if (this.title === '') {
        this.$message.warning('请输入标题')
        return -1
      }
      if (this.text === '') {
        this.$message.warning('请输入报告内容')
        return -1
      }
      if (this.reportType === '') {
        this.$message.warning('请选择报告类型')
        return -1
      }
      var data = {
        title: this.title,
        content: this.text,
        type: this.reportType,
        isDraft: judge
      }
      console.log(data)
      try {
        var res = await this.$api.ADD_REPORTS(data)
        console.log(res)
        this.$message.success('报告添加成功')
        await this.close({ tagName: '/addReports' })
      } catch (e) {
        console.warn(e)
        this.$message.error('报告添加失败')
      }
    }
  }
}
</script>
<style>
.d2-layout-header-aside-group .d2-layout-header-aside-content .d2-theme-container .d2-theme-container-main .d2-theme-container-main-body .container-component .d2-container-full .d2-container-full__header {
  padding: 0px 20px 0px 20px;
}
</style>
