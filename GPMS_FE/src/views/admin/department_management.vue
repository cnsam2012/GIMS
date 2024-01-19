<template>
  <d2-container>
    <template slot="header">
      <h1>
        {{ getTimeState() }}这里是部门管理
      </h1>
    </template>
    <template>
      <el-button @click="submit" type="success">TEST</el-button>
    </template>
    <!-- d2-crud start here -->
    <div>
      <d2-crud-x
        ref="d2Crud"
        :columns="columns"
        :data="data"
        :loading="loading"
        :options="options"
      />
    </div>
    <!-- d2-crud end -->
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
      msg: '正在加载...',
      columns: [
        {
          title: '日期',
          key: 'date',
          width: '180'
        },
        {
          title: '姓名',
          key: 'name',
          width: '180'
        },
        {
          title: '地址',
          key: 'address'
        }
      ],
      data: [
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        },
        {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄'
        },
        {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1519 弄'
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄'
        }
      ],
      loading: true,
      options: {
        stripe: true
      }
    }
  },
  mounted () {
    // 获取变化后的表格数据
    console.log(this.$refs.d2Crud.d2CrudData)
    this.loading = false
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
