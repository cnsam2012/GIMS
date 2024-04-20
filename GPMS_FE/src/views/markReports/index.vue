<template>
  <d2-container>
    <template slot="header" style="padding: 0">
      <div style="display: flex; justify-content: space-between; align-items: center; width: 100%;">
        <div>
          <template v-if="!hasReport">
            <h1 style="color: red">
              查无报告{{ rid }}
            </h1>
          </template>
          <template v-else>
            <h1>
              报告{{ rid }} - {{ report.title }}
            </h1>
          </template>
        </div>
        <!-- 新增一个div作为el-card的容器，并设置margin-left: auto;来推送它到右边 -->
        <div style="display: flex; margin-left: auto;height: 63px">
          <el-card shadow="hover" style="width: 100px; margin-right: 10px;background-color: rgba(255,0,0,0.07)"
                   v-if="report.score < 0">
            未评分
          </el-card>
          <el-card shadow="hover" style="width: 130px;">
            {{ user.roleName }}
          </el-card>
        </div>
      </div>

    </template>
    <div style="margin-top: 30px">
      <el-card>
        <v-md-editor v-model="report.content" height="300px" mode="preview"></v-md-editor>
      </el-card>
    </div>
    <div style="margin-top: 30px">
      <el-select v-model="reportMark" placeholder="快速评分" style="margin-right: 30px;width: 180px">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-input v-model="reportMark" placeholder="报告得分，满分100" style="margin-right: 30px;width: 160px"></el-input>
      <el-button @click="submit" type="success">保存</el-button>
      <el-button @click="next" type="info">下一份报告</el-button>
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
    ]),
    ...mapState('d2admin/nextReport', [
      'reports',
      'currentReportIndex'
    ])
  },
  data () {
    return {
      hasReport: true,
      user: {
        roleName: 'Loading...'
      },
      rid: 0,
      report: {
        title: 'Loading...',
        content: '# 正在加载....',
        score: -1
      },
      title: '',
      reportMark: '',
      options: [
        {
          value: '-1',
          label: '不作评分，置为未读'
        },
        {
          value: '0',
          label: 'F'
        },
        {
          value: '40',
          label: 'D'
        },
        {
          value: '60',
          label: 'C'
        },
        {
          value: '75',
          label: 'B'
        },
        {
          value: '90',
          label: 'A'
        },
        {
          value: '100',
          label: 'A+'
        }
      ]
    }
  },
  async mounted () {
    this.rid = this.$route.params.rid
    let res = await this.$api.FETCH_SPEC_REPORTS(this.rid)
    console.info(res)
    if (res.success === false) {
      res.report = {
        title: '查无报告',
        content: '报告为空'
      }
      this.hasReport = false
    } else {
      res = res.data
      this.report = res.report
      if (!res.report.score === -1) {
        this.reportMark = res.report.score
      }
      this.user = res.user
    }
  },

  // beforeDestroy () {
  // },
  methods: {
    ...mapActions('d2admin/page', [
      'close'
    ]),
    ...mapActions('d2admin/nextReport', [
      'updateReports',
      'updateCurrentReportIndex'
    ]),
    closeThisTag () {
      this.close({ tagName: '/addReports' })
    },
    async submit () {
      const data = {
        id: this.rid,
        score: this.reportMark
      }
      const res = await this.$api.MARK_REPORTS(data)
      console.log(res)
      this.$message({
        type: 'success',
        message: '成功评分'
      })
      // 设置一个1秒的延迟来刷新页面
      setTimeout(() => {
        this.next()
      }, 1000)
    },
    async next () {
      let currentIndex = this.currentReportIndex
      const reports = this.reports
      if (currentIndex < reports.length - 1) {
        // 存在下一份报告
        currentIndex += 1
        // this.$store.dispatch('updateCurrentReportIndex', currentIndex) // 更新Vuex中的当前报告索引
        this.updateCurrentReportIndex(currentIndex)
        const nextReportId = reports[currentIndex].id
        // 重新加载下一份报告的数据...
        this.$router.push({
          path: '/markReports/' + nextReportId
        })
      } else {
        // 已经是最后一份报告了
        this.$message({
          message: '这已经是当页最后一份报告了',
          type: 'warning'
        })
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
