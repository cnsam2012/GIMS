<template>
  <d2-container>
    <div style="margin-top: 20px">
      <el-row :gutter="gutter" v-if="welcomeMesgDisplay">
        <el-col :span="24">
          <el-card :shadow="shadow" style="width: 100%" :style="{ height: welcomeCardHeight }">
            <div style="display: flex; justify-content: center; align-items: center; width: 100%;"
                 :style="{ height: welcomeCardHeight }">
              <!-- 新增一个div作为容器，用于包裹需要靠左对齐的内容 -->
              <div style="font-size: 80px">
                <span style="margin: 0px;">{{ getTimeState() }}</span>
                <h1 style="margin-top: 5px;">{{ data.loginUser.username }}, 欢迎您</h1>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="gutter">
        <el-col :span="24">
          <el-card :shadow="shadow" style="width: 100%" @click.native="searchBoxClick">
            <div style="display: flex; justify-content: center; align-items: center; height: 100%;">
              <el-icon class="el-icon-search"></el-icon>
              <span>搜索功能</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="gutter">
        <el-col :span="6">
          <a :href="data.headerUrl">
            <el-card :shadow="shadow" style="height: 130px">
              <div style="display: flex; align-items: center; justify-content: center; height: 100px">
                <img :src="data.loginUser.headerUrl" alt=""/>
              </div>
            </el-card>
          </a>
        </el-col>
        <el-col :span="6">
          <el-card :shadow="shadow" style="height: 130px" @click.native="onNameCardClick">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <!-- 新增一个div作为容器，用于包裹需要靠左对齐的内容 -->
              <div style="align-self: flex-start;">
                <span style="margin: 0px;">{{ getTimeState() }}</span>
                <h1 style="margin: 0px;">{{ data.loginUser.roleName }}</h1>
                <span style="margin-top: 5px;">{{ data.loginUser.username }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px"
                       @click.native="onReportTotalClick">
                <span style="font-size: 40px">{{ data.reportRows }}</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">报告总数</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px"
                       @click.native="onTodaySubmitClick">
                <span style="font-size: 40px">{{ data.todaySubmitReportRows }}</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">今日提交</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px"
                       @click.native="onUnreadMsgClick">
                <span style="font-size: 40px">{{ data.unreadSum }}</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">未读消息</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px"
                       @click.native="onImportantMsgClick">
                <span style="font-size: 40px; color: red">{{ data.messageFromAdminUnreadCount }}</span>
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
          <el-card shadow="none" style="height: 280px">
            <el-descriptions title="用户信息" direction="vertical" :column="2">
              <el-descriptions-item label="手机号">
                <b>{{ data.loginUser.phone ? data.loginUser.phone : '暂无' }}</b>
              </el-descriptions-item>
              <el-descriptions-item label="电子邮件">
                <b>{{ data.loginUser.email }}</b>
              </el-descriptions-item>
              <el-descriptions-item label="用户类型">
                <b>{{ formatStatus(data.loginUser.type) }}</b>
              </el-descriptions-item>
              <el-descriptions-item label="状态">
                <b>{{ data.loginUser.status === 1 ? '已激活' : '未激活' }}</b>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">
                <b>{{ colCreateTimeFormatter(data.loginUser.createTime) }}</b>
              </el-descriptions-item>
              <el-descriptions-item label="部门ID">
                <b>{{ data.loginUser.departmentId }}</b>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        <el-col :span="12" style="display: flex; flex-direction: column; justify-content: space-between; height: 280px">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <el-card shadow="none" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">{{ data.activateUser }}</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">活跃用户</span>
                </div>
              </el-card>
              <el-card shadow="none" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">{{ data.systemUv }}</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">系统UV</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:272px;height: 95px; border: none; margin-right: 10px"
                       @click.native="onNewMsgClick">
                <template v-if="data.conversations[0].unreadCount == 0">
                  <span style="font-size: 40px">
                  0
                </span>
                  <div class="bottom clearfix">
                    <span style="color: #99a9bf">没有未读信息</span>
                  </div>
                </template>
                <template v-else>
                  <span style="font-size: 33px">
                  {{ trimmedContent(data.conversations[0].conversation.content) }}
                </span>
                  <div class="bottom clearfix">
                    <span style="color: #99a9bf">{{ data.conversations[0].conversation._fromId }}的信息</span>
                  </div>
                </template>
              </el-card>
            </div>
          </el-card>
          <el-card :shadow="shadow" style="height: 130px" @click.native="onPlanShowClick">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <!-- 新增一个div作为容器，用于包裹需要靠左对齐的内容 -->
              <div style="align-self: flex-start;">
                <span style="margin: 0px;">{{ data.planLineOne }}</span>

                <template v-if="data.planStage === 1">
                  <h1 style="margin: 0px; color: red">{{ data.planLineTwo }}</h1>
                </template>
                <template v-else>
                  <h1 style="margin: 0px;">{{ data.planLineTwo }}</h1>
                </template>

                <span style="margin-top: 5px;">{{ data.planLineThree }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <!--  实习状态  -->
      <el-row :gutter="gutter">
        <el-col :span="16">
          <el-card :shadow="shadow" style="width: 100%" ref="elStepsCard" @click.native="onPlanStepClick">
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
          <el-card :shadow="shadow" :style="{ height: stepsCardHeight }" @click.native="onSubmitReportClick">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <div style="align-self: flex-start;">
                <span style="margin: 0px;"><i class="el-icon-circle-plus-outline"></i></span>
                <h1 style="margin: 0px;">提交报告</h1>
                <span style="margin-top: 5px;">点此快速提交报告</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card :shadow="shadow" :style="{ height: stepsCardHeight }" @click.native="onChangePlanClick">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <div style="align-self: flex-start;">
                <span style="margin: 0px;"><i class="el-icon-info"></i></span>
                <h1 style="margin: 0px;">更改实习</h1>
                <span style="margin-top: 5px;">点此更改实习信息</span>
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
import util from '@/libs/util.js'
import { mapMutations, mapState } from 'vuex'

export default {
  data () {
    return {
      gutter: 20,
      shadow: 'hover',
      msg: '正在加载...',
      stepsCardHeight: 'auto',
      welcomeCardHeight: '600px',
      welcomeMesgDisplay: true,
      data: {
        reportRows: 0,
        messageFromAdminUnreadCount: 0,
        unreadSum: 0,
        loginUser: {
          id: 0,
          username: '',
          password: '',
          salt: '',
          email: '',
          phone: null,
          type: 0,
          status: 0,
          activationCode: '',
          headerUrl: '',
          createTime: '',
          wechatOpenId: null,
          departmentId: 0,
          roleName: '',
          tutor: null
        },
        planLineTwo: '',
        activateUser: 0,
        planLineOne: '',
        systemUv: 0,
        todaySubmitReportRows: 0,
        conversations: [
          {
            letterCount: 0,
            unreadCount: 0,
            conversation: {
              id: 0,
              fromId: 0,
              _fromId: '',
              toId: 0,
              _toId: '',
              conversationId: '',
              content: '',
              status: 0,
              createTime: ''
            },
            target: {
              id: 0,
              username: '',
              password: '',
              salt: '',
              email: '',
              phone: null,
              type: 0,
              status: 0,
              activationCode: null,
              headerUrl: '',
              createTime: '',
              wechatOpenId: null,
              departmentId: 0,
              roleName: '',
              tutor: null
            }
          }
        ],
        planStage: 0,
        planLineThree: ''
      }

    }
  },
  async mounted () {
    this.updateCardHeight()
    this.intervalId = setInterval(this.updateCardHeight, 1000) // 每1000毫秒更新一次高度，确保布局变化能够反映出来
    this.intervalId = setInterval(this.updateWelcomeMsgShow, 1500)
    const res = await this.$api.SYS_USER_GET_CURRENT_INFO()
    this.data = res.data
  },
  beforeDestroy () {
    if (this.intervalId) {
      clearInterval(this.intervalId)
    }
  },
  computed: {
    ...mapState('d2admin', {
      searchActive: state => state.search.active,
      searchHotkey: state => state.search.hotkey
    })
  },
  methods: {
    ...mapMutations({
      searchToggle: 'd2admin/search/toggle',
      searchSet: 'd2admin/search/set'
    }),
    trimmedContent (content) {
      const maxLength = 4 // 最大显示字符数
      if (content.length > maxLength) {
        return content.substr(0, maxLength) + '...'
      }
      return content
    },
    colCreateTimeFormatter (cellValue) {
      try {
        var dateRegex = /^\d{4}-\d{2}-\d{2}/
        var match = cellValue.match(dateRegex)
        if (match) {
          return match[0] // 返回匹配到的日期部分
        } else {
          return 'NULL' // 如果没有匹配到日期部分，则返回空字符串
        }
      } catch (e) {
        console.warn(e)
      }
    },
    formatStatus (status) {
      const statusMap = {
        1: '学生',
        2: '老师',
        3: '实习单位',
        9: '管理员'
      }
      return statusMap[status] || '未知状态'
    },
    updateCardHeight () {
      // 使用this.$refs来获取el-card的DOM引用
      const elStepsCard = this.$refs.elStepsCard
      if (elStepsCard) {
        this.stepsCardHeight = elStepsCard.$el.offsetHeight + 'px' // 获取高度并转换为px单位
      }
    },
    updateWelcomeMsgShow () {
      // this.welcomeMesgDisplay = false
      this.welcomeCardHeight = 0 + 'px'
      setTimeout(() => {
        this.welcomeMesgDisplay = false
      }, 500)
    },
    searchBoxClick () {
      this.searchToggle()
      if (this.searchActive) {
        setTimeout(() => {
          if (this.$refs.panelSearch) {
            this.$refs.panelSearch.focus()
          }
        }, 500)
      }
    },
    /**
     * 按键事件
     * @returns {Promise<void>}
     */
    async submit () {
      console.log('manual refresh')
      console.log('refreshing data')
      try {
        const res = await this.$api.DEMO_FETCH().data
        console.log(res)
        console.log(util.cookies.getAll())
        this.msg = res.reMsg
      } catch (error) {
        console.error(error)
      }
    },
    /**
     * 刷新数据
     * @returns {Promise<void>}
     */
    async refreshData () {
      console.log('refreshing data')
      try {
        const res = await this.$api.DEMO_FETCH().data
        console.log('data got')
        console.log(res)
      } catch (error) {
        console.error(error)
      }
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
    onSubmitReportClick () {
      this.$router.push({
        path: '/addReports'
      })
    },
    onChangePlanClick () {
      this.$router.push({
        path: '/planchoose_management'
      })
    },
    onReportTotalClick () {
      this.$router.push({
        path: '/reports_management'
      })
    },
    onTodaySubmitClick () {
      this.$router.push({
        path: '/reports_management'
      })
    },
    onUnreadMsgClick () {
      this.$router.push({
        path: '/myMessage'
      })
    },
    onImportantMsgClick () {
      const path = '/myMessageDetail/910000_' + this.data.loginUser.id + '/系统通知/' + this.data.loginUser.roleName
      this.$router.push({
        path: path
      })
    },
    onNewMsgClick () {
      const path = '/myMessageDetail/' + this.data.conversations[0].conversation.conversationId + '/' + this.data.conversations[0].conversation._fromId +
        '/' + this.data.conversations[0].conversation._toId
      this.$router.push({
        path: path
      })
    },
    onNameCardClick () {
      this.$router.push({
        path: '/alterUsername'
      })
    },
    onPlanShowClick () {
      this.$router.push({
        path: '/planchoose_management'
      })
    },
    onPlanStepClick () {
      // TODO 按照当前步骤作出不同跳转
      this.$router.push({
        path: '/planchoose_management'
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
</style>
