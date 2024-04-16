<template>
  <d2-container>
    <div style="margin-top: 20px">
      <el-row :gutter="gutter">
        <el-col :span="6">
          <a :href="data.headerUrl">
            <el-card :shadow="shadow" style="height: 130px">
              <div style="display: flex; align-items: center; justify-content: center; height: 100px">
                <img :src="data.headerUrl" alt=""/>
              </div>
            </el-card>
          </a>
        </el-col>
        <el-col :span="6">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <!-- 新增一个div作为容器，用于包裹需要靠左对齐的内容 -->
              <div style="align-self: flex-start;">
                <span style="margin: 0px;">{{ getTimeState() }}</span>
                <h1 style="margin: 0px;">{{ data.roleName }}</h1>
                <span style="margin-top: 5px;">{{ data.username }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card :shadow="shadow" style="height: 130px">
            <div style="display: flex; align-items: center; justify-content: flex-start; height: 100%; width: 100%;">
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">26</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">报告总数</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">2</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">今日提交</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">26</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">未读消息</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px; color: red">3</span>
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
            <el-descriptions title="用户信息" direction="vertical" column="2">
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
                <span style="font-size: 40px">1</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">活跃用户</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:130px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">5</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">系统UV</span>
                </div>
              </el-card>
              <el-card shadow="hover" style="width:272px;height: 95px; border: none; margin-right: 10px">
                <span style="font-size: 40px">0</span>
                <div class="bottom clearfix">
                  <span style="color: #99a9bf">没有未读信息</span>
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
      <el-row :gutter="gutter">
        <el-col :span="8">
          <div class="grid-content bg-purple">4</div>
        </el-col>
        <el-col :span="8">
          <div class="grid-content bg-purple-light">5</div>
        </el-col>
        <el-col :span="4">
          <div class="grid-content bg-purple-light">5a</div>
        </el-col>
        <el-col :span="4">
          <div class="grid-content bg-purple">6</div>

        </el-col>
      </el-row>
      <el-row :gutter="gutter">
        <el-col :span="6">
          <div class="grid-content bg-purple">7</div>
        </el-col>
        <el-col :span="6">
          <div class="grid-content bg-purple-light">8</div>
        </el-col>
        <el-col :span="6">
          <div class="grid-content bg-purple">9</div>
        </el-col>
        <el-col :span="6">
          <div class="grid-content bg-purple-light">a</div>
        </el-col>
      </el-row>
      <el-row :gutter="gutter">
        <el-col :span="4">
          <div class="grid-content bg-purple">b</div>
        </el-col>
        <el-col :span="4">
          <div class="grid-content bg-purple-light">c</div>
        </el-col>
        <el-col :span="4">
          <div class="grid-content bg-purple">d</div>
        </el-col>
        <el-col :span="4">
          <div class="grid-content bg-purple-light">e</div>
        </el-col>
        <el-col :span="4">
          <div class="grid-content bg-purple">f</div>
        </el-col>
        <el-col :span="4">
          <div class="grid-content bg-purple-light">g</div>
        </el-col>
      </el-row>
    </div>
    <template slot="footer">
      <random-motto/>
    </template>
  </d2-container>
</template>
<script>
import util from '@/libs/util.js'

export default {
  data () {
    return {
      gutter: 20,
      shadow: 'hover',
      msg: '正在加载...',
      data: {
        id: 910007,
        username: 'cc',
        password: '',
        salt: '',
        email: 'cnsam2012@qq.com',
        phone: null,
        type: 9,
        status: 1,
        activationCode: '44bf21c5ee9341a9ae5bd02e0635e150',
        headerUrl: 'http://images.nowcoder.com/head/12t.png',
        createTime: '2024-01-07T08:33:40.000+00:00',
        wechatOpenId: null,
        departmentId: 810008,
        roleName: '管理员',
        tutor: null
      }
    }
  },
  mounted () {
    this.timeInterval = setInterval(() => {
      this.refreshData()
    }, 30 * 1000)
  },
  // beforeDestroy () {
  // },
  methods: {
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
