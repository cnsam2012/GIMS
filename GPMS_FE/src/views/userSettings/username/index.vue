<template>
  <d2-container>
    <template slot="header">
      <h1 @click="see">
        您正在修改用户名
      </h1>
    </template>
    <div style="padding-bottom: 20px">
      <el-descriptions title="用户信息">
        <el-descriptions-item label="用户名">{{ username }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <!--    TODO 后端添加验证：用户名不可以包含中文或者中文符号，查看是否能加入在服务层       -->
    <div style="width: 40%">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="auto" label-position="left"
               class="demo-ruleForm">
        <el-form-item label="新用户名" prop="newUsername" :error="formError.newUsername">
          <el-input type="text" v-model="ruleForm.newUsername" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <template slot="footer">
      <random-motto/>
    </template>
  </d2-container>
</template>
<script>
import { mapActions, mapState } from 'vuex'
import api from '@/api'
import { Loading } from 'element-ui'

export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ]),
    ...mapState('d2admin/page', [
      'current',
      'opened'
    ])
  },
  data () {
    var validateNewUserName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新用户名'))
      } else {
        callback()
      }
    }
    return {
      msg: '正在加载...',
      username: '正在加载...',
      ruleForm: {
        newUsername: ''
      },
      formError: {
        newUsername: ''
      },
      rules: {
        newUsername: [
          {
            validator: validateNewUserName,
            trigger: 'blur'
          }
        ]
      }
    }
  },
  async mounted () {
    await this.refreshData()
  },
  // beforeDestroy () {
  // },
  methods: {
    async see () {
      console.log(this.current)
      console.log(this.opened)
    },
    ...mapActions('d2admin/page', [
      'close'
    ]),
    ...mapActions('d2admin/account', [
      'logout'
    ]),
    startLoading () { // 使用Element loading-start 方法
      return Loading.service({
        lock: true,
        text: '加载中……',
        background: 'rgba(0,0,0,0.6)'
      })
    },
    /**
     * 刷新数据
     * @returns {Promise<void>}
     */
    async refreshData () {
      // console.log('refreshing data')
      try {
        this.username = this.info.name
      } catch (error) {
        console.error(error)
      }
    },
    async submitValidate (valid) {
      const ld = this.startLoading()
      if (valid) {
        const data = {
          username: this.ruleForm.newUsername
        }
        let alterRes = await api.SYS_USER_ALTER_USERNAME(data)
        alterRes = alterRes.data
        let newUsernameError = ''
        try {
          newUsernameError = alterRes.newUsernameError
        } catch (e) {
          // console.info('{"NO_NEW_USERNAME_ERROR"}')
        }
        if (newUsernameError) {
          this.formError.newUsername = newUsernameError
          ld.close()
        } else {
          await this.$alert('修改成功，将注销登录，请使用新用户名重新登录', '提示', {
            type: 'success',
            confirmButtonClass: 'el-button el-button--default el-button--small el-button--success',
            showClose: false
          })
          ld.close()
          await this.close({ tagName: '/alterUsername' })
          await this.logout()
        }
        ld.close()
      } else {
        console.error('{"RECEIVE_DATA_VALID_ERROR"}')
        ld.close()
        return false
      }
    },
    async submitForm (formName) {
      this.formError.newUsername = ''
      await this.$confirm('此操作将修改用户名, 原用户名将不可再被使用、并可以被其他人注册为新用户名, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'el-button el-button--default el-button--small el-button--success',
        showClose: false
      }).then(() => {
        this.$refs[formName].validate((valid) => this.submitValidate(valid))
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消修改'
        })
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>
<style>
</style>
