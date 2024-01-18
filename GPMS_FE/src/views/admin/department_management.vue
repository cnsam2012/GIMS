<template>
  <d2-container>
    <template slot="header">
      <h1>
        {{ getTimeState() }}这里是部门管理
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
      <el-button @click="submit" type="success">TEST</el-button>
    </template>
    <template>
    </template>
    <template slot="footer">footer</template>
  </d2-container>
</template>
<script>
import util from '@/libs/util.js'
export default {
  data () {
    return {
      msg: '正在加载...'
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
        const res = await this.$api.DEMO_FETCH()
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
        const res = await this.$api.DEMO_FETCH()
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
<style>

</style>
