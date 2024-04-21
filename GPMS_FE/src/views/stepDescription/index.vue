<template>
  <d2-container>
    <template slot="header" style="padding: 0">
      <div style="display: flex; justify-content: space-between; align-items: center; width: 100%;">
        <div>
            <h1>
              实习过程描述
            </h1>
        </div>
      </div>

    </template>
    <div style="margin-top: 30px">
      <el-card>
        <v-md-editor v-model="report.content" height="300px" mode="preview"></v-md-editor>
      </el-card>
    </div>
    <div style="margin-top: 30px">
      <el-button @click="cancel" type="info">取消</el-button>
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
        content: '# 选择实习\n' +
          '在实习准备阶段，学校、单位会先考察潜在实习单位，并开设实习课程。学生可以选择集中式实习或自主寻找实习机会，自主实习的学生请在实习管理中填写具体实习信息。\n# 进行实习\n' +
          '在实习实施阶段，学生须定期提交实习周报和月报以记录实习体验。同时，指导老师会提供日常指导，监督实习进度。\n# 实习结束\n' +
          '实习总结阶段，学生需撰写实习报告，反思和总结实习经历，并提交所有实习记录文件。指导老师将对学生的实习表现进行评价和等级评定。',
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
    // this.rid = this.$route.params.rid
    // let res = await this.$api.FETCH_SPEC_REPORTS(this.rid)
    // console.info(res)
    // if (res.success === false) {
    //   res.report = {
    //     title: '查无报告',
    //     content: '报告为空'
    //   }
    //   this.hasReport = false
    // } else {
    //   res = res.data
    //   this.report = res.report
    //   if (!res.report.score === -1) {
    //     this.reportMark = res.report.score
    //   }
    //   this.user = res.user
    // }
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
