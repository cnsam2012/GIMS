<template>
  <d2-container>
    <template slot="header">
      <h1>
        {{ getTimeState() }}这里是测试登录页面
      </h1>
    </template>
    <el-descriptions title="用户信息">
      <el-descriptions-item label="用户名">kooriookami</el-descriptions-item>
      <el-descriptions-item label="手机号">18100000000</el-descriptions-item>
      <el-descriptions-item label="居住地">苏州市</el-descriptions-item>
      <el-descriptions-item label="备注">
        <el-tag size="small" type="success">学校</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="联系地址">江苏省苏州市吴中区吴中大道 1188 号</el-descriptions-item>
      <el-descriptions-item label="msg">{{ msg }}</el-descriptions-item>
    </el-descriptions>
    <template>
      <el-button @click="submit" type="success">TEST - 登录</el-button>
    </template>
    <div style="margin-top: 1rem">
      <el-descriptions title="Kaptcha TEST">
        <el-descriptions-item label="用户名">okkkk</el-descriptions-item>
      </el-descriptions>
      <img :src="imgSrc" alt="wsdwd" @click="refreshKaptcha"/>
    </div>
    <template slot="footer"><random-motto/></template>
  </d2-container>
</template>
<script>
export default {
  data () {
    return {
      msg: '正在加载...',
      imgSrc: './image/login-code.png'
    }
  },
  mounted () {
    // this.timeInterval = setInterval(() => {
    //   this.refreshData()
    // }, 30 * 1000)
    console.log('MOUNTED')
    this.refreshKaptcha()
  },
  // beforeDestroy () {
  // },
  methods: {
    /**
     * 按键事件
     * @returns {Promise<void>}
     */
    async submit () {
      console.log('trying logging')
      try {
        var data = {
          username: 'cc',
          password: '2012',
          code: 'string',
          rememberMe: true
        }
        const res = await this.$api.SYS_USER_LOGIN(data)
        console.log(res)
        this.msg = res.userinfo.username
      } catch (error) {
        console.error(error)
      }
    },
    /**
     * 刷新数据
     * @returns {Promise<void>}
     */
    async refreshData () {
      // console.log('refreshing data')
      // try {
      //   const res = await this.$api.DEMO_FETCH()
      //   console.log('data got')
      //   console.log(res)
      // } catch (error) {
      //   console.error(error)
      // }
    },
    async refreshKaptcha () {
      this.$api.GET_KAPTCHA().then(res => {
        if (res.type === 'application/json') {
          console.error(res)
          console.error('kaptcha got faild')
        } else if (res) {
          this.imgSrc = window.URL.createObjectURL(res)
        } else {
          console.error(res)
          console.error('kaptcha got faild')
        }
      })
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
