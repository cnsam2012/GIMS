<template>
  <d2-container>
    <template slot="header">
      <h1>
        实习：{{ data.name }}
      </h1>
    </template>
    <el-descriptions title="实习信息">
      <el-descriptions-item label="类型">{{ data.type }}</el-descriptions-item>
      <el-descriptions-item label="主要方向">{{ data.majorOrieId }}</el-descriptions-item>
      <el-descriptions-item label="等级">{{ data.grade }}</el-descriptions-item>
      <el-descriptions-item label="学分">{{ data.credit }}</el-descriptions-item>
      <el-descriptions-item label="内容">{{ data.content }}</el-descriptions-item>
      <el-descriptions-item label="目标">{{ data.objective }}</el-descriptions-item>
      <el-descriptions-item label="需求">{{ data.demand }}</el-descriptions-item>
      <el-descriptions-item label="评分计算类型">{{ data.scoreCalType }}</el-descriptions-item>
      <el-descriptions-item label="日报占比">{{ data.percentInDailyReport }}%</el-descriptions-item>
      <el-descriptions-item label="周报占比">{{ data.percentInWeeklyReport }}%</el-descriptions-item>
      <el-descriptions-item label="月报占比">{{ data.percentInMonthlyReport }}%</el-descriptions-item>
      <el-descriptions-item label="总结占比">{{ data.percentInSummary }}%</el-descriptions-item>
      <el-descriptions-item label="截止日期">{{ data.deadline }}</el-descriptions-item>
      <el-descriptions-item label="创建者">{{ data.creator }}</el-descriptions-item>
    </el-descriptions>
    <template>
      <el-button @click="my_submit" type="success">参加</el-button>
      <el-button @click="cancel" type="warn">取消</el-button>
    </template>
    <template slot="footer">
      <random-motto/>
    </template>
  </d2-container>
</template>
<script>
import { mapActions, mapState } from 'vuex'

export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ])
  },
  data () {
    return {
      msg: '正在加载...',
      data: {
        id: -1,
        type: -1,
        name: '正在加载...',
        major_orie_id: -1,
        grade: -1,
        start_d: -1,
        end_d: -1,
        class_hour: -1,
        credit: 45.05,
        percent_in: -1,
        percent_ex: -1,
        content: '正在加载...',
        objective: '正在加载...',
        demand: '正在加载...',
        score_cal_type: 0,
        percent_in_daily_report: 0,
        percent_in_weekly_report: 0,
        percent_in_monthly_report: 0,
        percent_in_summary: 0,
        deadline: '2019-10-02T16:00:00.000+00:00',
        creator: 910007,
        _creator: 'hello',
        _deleted: false
      }
    }
  },
  mounted () {
    this.refreshData()
  },
  // beforeDestroy () {
  // },
  methods: {
    ...mapActions('d2admin/page', [
      'close'
    ]),
    async my_submit () {
      // this.$confirm('此操作将参加该实习, 是否继续?', '提示', {
      //   confirmButtonText: '确定',
      //   cancelButtonText: '取消',
      //   type: 'warning',
      //   confirmButtonClass: 'el-button el-button--default el-button--small el-button--success',
      //   showClose: false
      // }).then(() => {
      //   var data = {
      //     userId: this.info.userId,
      //     planId: this.data.id,
      //     status: 2
      //   }
      //   console.log(data)
      // }).catch(() => {
      //   this.$message({
      //     type: 'info',
      //     message: '已取消'
      //   })
      // })
      this.$confirm('此操作将参加该实习, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'el-button el-button--default el-button--small el-button--success',
        showClose: false
      }).then(async () => {
        var data = {
          userId: this.info.userId,
          planId: this.data.id,
          status: 2
        }
        await this.$api.ADD_A_PLAN_C(data)
        await this.$router.push({ name: 'planchoose_management' })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
    },
    async cancel () {
      var path = this.$route.path + ''
      console.log(path)
      await this.close({ tagName: path })
    },
    /**
     * 刷新数据
     * @returns {Promise<void>}
     */
    async refreshData () {
      var pid = this.$route.params.planid
      var requestBody = {
        planId: pid
      }
      const res = await this.$api.FETCH_SPEC_PLAN(requestBody)
      this.data = res.data.plan
    },
    /**
     * 自适应问候
     * @returns {string}
     */
    getTimeState () {
      // 获取当前时间
      const timeNow = new Date()
      // 获取当前小时
      const hours = timeNow.getHours()
      // 设置默认文字
      let state = ''
      // 判断当前时间段
      if (hours >= 0 && hours <= 10) {
        state = '早上好! '
      } else if (hours > 10 && hours <= 14) {
        state = '中午好! '
      } else if (hours > 14 && hours <= 18) {
        state = '下午好! '
      } else if (hours > 18 && hours <= 24) {
        state = '晚上好! '
      }
      return state
    }
  }
}
</script>
<style>

</style>
