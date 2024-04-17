<template>
  <d2-container>
    <template slot="header">
      <h1>
        xxx 的实习： 评分页面 {{ pid }}
      </h1>
    </template>

    <div style="margin-top: 20px">
      <el-row :gutter="gutter">
        <el-col :span="4">
          <el-card :shadow="shadow" style="height: 130px;background-color: rgba(255,0,0,0.07)">
            <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100px;">
              <h1 style="margin: 5px">不可评分</h1>
              <span>存在待阅报告</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card :shadow="shadow" style="height: 130px;background-color: rgba(85,255,0,0.07)">
            <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100px;">
              <h1 style="margin: 5px">等待评分</h1>
              <span>请提交最终得分</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <!-- 新增一个div作为容器，用于包裹需要靠左对齐的内容 -->
              <div style="align-self: flex-start;">
                <span style="margin: 0px;">学生姓名</span>
                <h1 style="margin: 0px;">刘伊尔</h1>
                <span style="margin-top: 5px;">xs</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px; color: red">30</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">报告待阅</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">2</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">今日提交</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">302</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">对话总数</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px;">20</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">重要通知</span>
                </div>
              </el-card>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="gutter">
        <el-col :span="12">
          <el-card :shadow="shadow" style="height: 280px">
            <el-descriptions title="用户信息" direction="vertical" :column="2">
              <el-descriptions-item label="手机号">{{ data.phone ? data.phone : '暂无' }}</el-descriptions-item>
              <el-descriptions-item label="电子邮件">{{ data.email }}</el-descriptions-item>
              <el-descriptions-item label="用户类型">{{ data.type }}</el-descriptions-item>
              <el-descriptions-item label="状态">{{ data.status }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ data.createTime }}</el-descriptions-item>
              <el-descriptions-item label="部门ID">{{ data.departmentId }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        <el-col :span="12" style="display: flex; flex-direction: column; justify-content: space-between; height: 280px">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">30%</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">周记占比</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">10%</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">月记占比</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:272px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">50%</span>
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
                <span style="margin: 0px;">正在参加的实习</span>
                <h1 style="margin: 0px;">网络工程师</h1>
                <span style="margin-top: 5px;">康陶科技 - 林毅儿</span>
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
              <el-steps :active="2" style="width: 100%;" finish-status="success">
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
                <h1 style="margin: 0px;">78</h1>
                <span style="margin-top: 5px;">计划评分（计算得）</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card :shadow="shadow" :style="{ height: stepsCardHeight }">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <div style="align-self: flex-start;">
                <span style="margin: 0px;"><i class="el-icon-check"></i></span>
                <h1 style="margin: 0px;">提交得分</h1>
                <span style="margin-top: 5px;">点击确认并提交得分</span>
              </div>
            </div>
          </el-card>
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
import { mapActions, mapState } from 'vuex'

export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ])
  },
  data () {
    return {
      gutter: 20,
      shadow: 'hover',
      stepsCardHeight: 'auto',
      welcomeCardHeight: '600px',
      msg: '正在加载...',
      pid: -1,
      data: {}
    }
  },
  mounted () {
    this.updateCardHeight()
    this.intervalId = setInterval(this.updateCardHeight, 500) // 每1000毫秒更新一次高度，确保布局变化能够反映出来
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
      // var requestBody = {
      //   planId: pid
      // }
      // const res = await this.$api.FETCH_SPEC_PLAN(requestBody)
      // this.data = res.data.plan
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
</style>
