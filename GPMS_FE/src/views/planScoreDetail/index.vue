<template>
  <d2-container>
    <div style="margin-top: 20px">
      <el-row :gutter="gutter">

        <template v-if="data.isMarkable">
          <el-col :span="8">
            <el-card :shadow="shadow" style="height: 130px;background-color: rgba(85,255,0,0.07)">
              <div
                style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100px;">
                <h1 style="margin: 5px">等待评分</h1>
                <span>{{ data.markableReason }}</span>
              </div>
            </el-card>
          </el-col>
        </template>
        <template v-else>
          <el-col :span="8">
            <el-card :shadow="shadow" style="height: 130px;background-color: rgba(255,0,0,0.07)"
                     @click.native="onMarkingFaildClick">
              <div
                style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100px;">
                <h1 style="margin: 5px">不可评分</h1>
                <span>{{ data.markableReason }}</span>
              </div>
            </el-card>
          </el-col>
        </template>
        <el-col :span="4">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <!-- 新增一个div作为容器，用于包裹需要靠左对齐的内容 -->
              <div style="align-self: flex-start;">
                <span style="margin: 0px;">学生姓名</span>
                <h1 style="margin: 0px;">{{ data.student.roleName }}</h1>
                <span style="margin-top: 5px;">{{ data.student.type === 1 ? '学生' : '非学生' }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px"
                       @click.native="toReadingRepo">
                <span style="font-size: 40px; color: red">{{ data.reportUnreadRows }}</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">报告待阅</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">{{ data.todayCreatedRows }}</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">今日提交</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px"
                       @click.native="toMessageReading">
                <span style="font-size: 40px">{{ data.messageCount }}</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">对话总数</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px;">{{ data.reportSummaryRows }}</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">提交总结</span>
                </div>
              </el-card>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="gutter">
        <el-col :span="12">
          <el-card :shadow="shadow" style="height: 280px">
            <div v-if="this.reportChartOptions.xAxis.data" style="height: 280px" id="lineGrid"></div>
          </el-card>
        </el-col>
        <el-col :span="12" style="display: flex; flex-direction: column; justify-content: space-between; height: 280px">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">{{ data.plan.percentInWeeklyReport }}%</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">周记占比</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">{{ data.plan.percentInMonthlyReport }}%</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">月记占比</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:272px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">{{ data.plan.percentInSummary }}%</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">总结占比</span>
                </div>
              </el-card>
            </div>
          </el-card>
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <!-- 新增一个div作为容器，用于包裹需要靠左对齐的内容 -->
              <div style="align-self: flex-start;">
                <span style="margin: 0px;">{{ data.planLineOne }}</span>
                <h1 style="margin: 0px;">{{ data.planLineTwo }}</h1>
                <span style="margin-top: 5px;">{{ data.planLineThree }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <!--  实习状态  -->
      <el-row :gutter="gutter">
        <el-col :span="16">
          <el-card :shadow="shadow" style="width: 100%" ref="elStepsCard">
            <div style="display: flex; justify-content: center; align-items: center; height: 100%; width: 100%">
              <el-steps :active="data.planStage" style="width: 100%;" finish-status="success">
                <el-step title="选择实习"
                         description="在实习准备阶段，学校、单位会先考察潜在实习单位，并开设实习课程。学生可以选择集中式实习或自主寻找实习机会，自主实习的学生请在实习管理中填写具体实习信息。"></el-step>
                <el-step title="进行实习"
                         description="在实习实施阶段，学生须定期提交实习周报和月报以记录实习体验。同时，指导老师会提供日常指导，监督实习进度。"></el-step>
                <el-step title="实习结束"
                         description="实习总结阶段，学生需撰写实习报告，反思和总结实习经历，并提交所有实习记录文件。指导老师将对学生的实习表现进行评价和等级评定。"></el-step>
              </el-steps>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card :shadow="shadow" :style="{ height: stepsCardHeight }">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <div style="align-self: flex-start;">
                <span style="margin: 0px;"><i class="el-icon-edit"></i></span>
                <h1 style="margin: 0px;">{{ data.score }}</h1>
                <span style="margin-top: 5px;">计划评分（计算得）</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">

          <template v-if="data.isMarkable">
            <el-card :shadow="shadow" :style="{ height: stepsCardHeight }" @click.native="my_submit">
              <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
                <div style="align-self: flex-start;">
                  <span style="margin: 0px;"><i class="el-icon-check"></i></span>
                  <h1 style="margin: 0px;">提交得分</h1>
                  <span style="margin-top: 5px;">点击确认并提交得分</span>
                </div>
              </div>
            </el-card>
          </template>
          <template v-else>
            <el-card :shadow="shadow" :style="{ height: stepsCardHeight }" class="is-disabled">
              <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
                <div style="align-self: flex-start;">
                  <span style="margin: 0px;"><i class="el-icon-check"></i></span>
                  <h1 style="margin: 0px;">提交得分</h1>
                  <span style="margin-top: 5px;">点击确认并提交得分</span>
                </div>
              </div>
            </el-card>
          </template>

        </el-col>
      </el-row>
      <!--  实习状态end  -->
    </div>

    <template slot="footer">
      <random-motto/>
    </template>
  </d2-container>
</template>
<script>
// 引入ECharts主模块
// import ECharts from 'vue-echarts'
import * as echarts from 'echarts'
// // 手动引入所需的折线图
// import 'echarts/lib/chart/line'
// // 引入提示框和标题组件
// import 'echarts/lib/component/tooltip'
// import 'echarts/lib/component/title'
import { mapActions, mapState } from 'vuex'
export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ])
  },
  components: {
    // ECharts
  },
  data () {
    return {
      reportChartOptions: {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: [] // 假设日期数据已经在data.reportChartData.dates中
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [], // 假设报告提交数量数据已经在data.reportChartData.reportCounts中
          type: 'line',
          smooth: true // 使线条平滑显示
        }]
      },
      gutter: 20,
      shadow: 'hover',
      stepsCardHeight: 'auto',
      welcomeCardHeight: '600px',
      msg: '正在加载...',
      pid: -1,
      data: {
        markableReason: '-',
        messageCount: 21,
        student: {
          id: -1,
          username: '-',
          password: '',
          salt: '',
          email: '-',
          phone: null,
          type: 1,
          status: 1,
          activationCode: '-',
          headerUrl: '-',
          createTime: '2024-04-07T03:30:38.000+00:00',
          wechatOpenId: null,
          departmentId: -1,
          roleName: '-',
          tutor: null
        },
        reportSummaryRows: 0,
        reportChartData: {
          dates: [
            '2024-04-11'
          ],
          reportCounts: [
            1
          ]
        },
        todayCreatedRows: 0,
        reportUnreadRows: 0,
        planLineTwo: '-------',
        planLineOne: '-------',
        plan: {
          id: 40,
          type: '-',
          name: '-',
          majorOrieId: '-',
          grade: 4,
          startD: '2024-01-01T00:00:00.000+00:00',
          endD: '2024-07-01T00:00:00.000+00:00',
          classHour: 0,
          credit: 4,
          percentIn: 0,
          percentEx: 0,
          content: '-',
          objective: '-',
          demand: '-',
          scoreCalType: 0,
          percentInDailyReport: 5,
          percentInWeeklyReport: 15,
          percentInMonthlyReport: 30,
          percentInSummary: 50,
          deadline: '2024-04-30T16:00:00.000+00:00',
          creator: 910020,
          _creator: null,
          deleted: false
        },
        planStage: 2,
        planLineThree: '- ',
        isMarkable: true,
        score: -1
      }
    }
  },
  mounted () {
    this.updateCardHeight()
    this.intervalId = setInterval(this.updateCardHeight, 500) // 每1000毫秒更新一次高度，确保布局变化能够反映出来
    this.refreshData()
    // this.$nextTick(() => {
    //   this.initCharts()
    // })
  },
  // beforeDestroy () {
  // },
  methods: {
    ...mapActions('d2admin/page', [
      'close'
    ]),
    async onMarkingFaildClick () {
      this.$prompt('提醒 ' + this.data.student.roleName, '发送提醒', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        // 设置输入框的初始值为当前的分数
        inputValue: '您好，您的实习尚不能被评分，理由是：' + this.data.markableReason + '。请尽快完成任务',
        inputErrorMessage: '无效的内容'
      }).then(({ value }) => {
        // 确定按钮被点击，value为输入的评分
        // 调用后端接口提交评分
        const requestBody = {
          toName: this.data.student.roleName,
          content: value
        }
        // 替换以下代码为实际调用后端接口的代码
        this.$api.SEND_MESSAGE(requestBody).then(response => {
          // 处理响应
          this.$message({
            type: 'success',
            message: '信息发送成功'
          })
        }).catch(e => {
          // 处理错误
          this.$message.error('信息发送失败: ' + e)
        })
      }).catch(() => {
        // 取消按钮被点击
      })
    },
    async my_submit () {
      // TODO
      // 这里的后端接口是 this.$api.MARKING_A_PLAN_C(request_body)
      // request_body ={
      //   "id": 32,
      //     "score": -1
      // }
      // 点击 提交得分 后，弹出一个带输入框的确认框，输入框的值是this.data.score，输入框提示的内容是“为 this.data.roleName 的实习评分”
      // 包含确定和取消按钮，点击确定后，访问后端接口提交得分

      // 使用Element UI的MessageBox.prompt来弹出输入框
      this.$prompt('为 ' + this.data.student.roleName + ' 的实习评分', '提交得分', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        // 设置输入框的初始值为当前的分数
        inputValue: this.data.score,
        // 验证输入的评分是否符合要求
        inputValidator: (value) => {
          // 首先，检查输入是否为数字
          if (isNaN(value)) {
            return '只能输入数字'
          }
          // 将输入值转换为数字类型，以进行范围检查
          const numberValue = Number(value)
          // 然后，检查数字是否在0到100之间
          if (numberValue < 0 || numberValue > 100) {
            return '评分范围应在0到100之间'
          }
          // 如果通过所有检查，则返回true，表示输入有效
          return true
        },
        inputErrorMessage: '无效的评分'
      }).then(({ value }) => {
        // 确定按钮被点击，value为输入的评分
        // 调用后端接口提交评分
        const requestBody = {
          id: this.pid, // 假设使用计划ID作为提交评分的标识
          score: value
        }
        // 替换以下代码为实际调用后端接口的代码
        this.$api.MARKING_A_PLAN_C(requestBody).then(response => {
          // 处理响应
          this.$message({
            type: 'success',
            message: '评分提交成功'
          })
        }).catch(e => {
          // 处理错误
          this.$message.error('评分提交失败: ' + e)
        })
        console.log('提交的评分:', requestBody) // 测试输出，实际开发中应调用接口
      }).catch(() => {
        // 取消按钮被点击
      })
    },
    updateCardHeight () {
      // 使用this.$refs来获取el-card的DOM引用
      const elStepsCard = this.$refs.elStepsCard
      if (elStepsCard) {
        this.stepsCardHeight = elStepsCard.$el.offsetHeight + 'px' // 获取高度并转换为px单位
      }
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
      this.pid = pid
      var requestBody = pid
      const res = await this.$api.FETCH_SPEC_PLAN_C_DETAIL(requestBody)
      console.log(res)
      this.data = res.data
      this.reportChartOptions.xAxis.data = res.data.reportChartData.dates
      this.reportChartOptions.series[0].data = res.data.reportChartData.reportCounts
      this.initCharts()
    },
    initCharts () {
      console.log(this.reportChartOptions.xAxis.data)
      console.log(this.reportChartOptions.series[0].data)
      const chartDom = document.getElementById('lineGrid')
      const myChart = echarts.init(chartDom)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          backgroundColor: '#fff', // 悬浮框背景色
          borderColor: '#000', // 悬浮框边框颜色
          borderWidth: 1, // 悬浮框边框宽度
          textStyle: { // 悬浮框文字样式
            color: '#000',
            fontSize: 12
          }
        },
        title: {
          text: '学生报告提交趋势', // This sets the title of the chart
          left: 'left', // This positions the title in the center. Options include 'left', 'right', and 'center'.
          top: 20, // This positions the title 20px from the top of the chart container.
          bottom: 30
        },
        xAxis: {
          type: 'category',
          data: this.reportChartOptions.xAxis.data
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: this.reportChartOptions.series[0].data,
            type: 'line',
            smooth: true
          }
        ]
      }
      option && myChart.setOption(option)
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
    },
    toReadingRepo () {
      this.$router.push({
        path: '/reports_management'
      })
    },
    toMessageReading () {
      let conversationId = ''
      if (this.data.student.id > this.info.userId) {
        conversationId = this.info.userId + '_' + this.data.student.id
      } else {
        conversationId = this.data.student.id + '_' + this.info.userId
      }
      const a = this.info.rolename
      const b = this.data.student.roleName
      this.$router.push({
        path: '/myMessageDetail/' + conversationId + '/' + a + '/' + b
      })
    }
  }
}
</script>
<style scoped>
.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  background: #d3dce6;
}

.bg-purple-light {
  background: #e5e9f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.el-row {
  margin-bottom: 20px;

  &:last-child {
    margin-bottom: 0;
  }
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  background: #d3dce6;
}

.bg-purple-light {
  background: #e5e9f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.is-disabled {
  pointer-events: none; /* 禁止点击 */
  opacity: 0.5; /* 半透明显示 */
}
</style>
