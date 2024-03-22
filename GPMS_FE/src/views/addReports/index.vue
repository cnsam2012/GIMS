<template>
  <d2-container>
    <template slot="header">
      <h1>
        新建报告
      </h1>
    </template>
    <template slot="footer" > <random-motto/> </template>
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
    var validateOldPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入原密码'))
      } else {
        callback()
      }
    }
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      msg: '正在加载...',
      username: '正在加载...',
      imgSrc: './image/login-code.png',
      ruleForm: {
        pass: '',
        checkPass: '',
        oldPass: ''
      },
      formError: {
        pass: '',
        checkPass: '',
        oldPass: ''
      },
      rules: {
        oldPass: [
          {
            validator: validateOldPass,
            trigger: 'blur'
          }
        ],
        pass: [
          {
            validator: validatePass,
            trigger: 'blur'
          }
        ],
        checkPass: [
          {
            validator: validatePass2,
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
    ...mapActions('d2admin/page', [
      'close'
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
        var data = {
          oldPwd: this.ruleForm.oldPass,
          newPwd: this.ruleForm.pass
        }
        let alterRes = await api.SYS_USER_ALTER_PASSWORD(data)
        alterRes = alterRes.data
        let oldPasswordError = ''
        let newPasswordError = ''
        try {
          oldPasswordError = alterRes.oldPasswordError
        } catch (e) {
          // console.info('{"NO_PASSWORD_ERROR"}')
        }
        try {
          newPasswordError = alterRes.newPasswordError
        } catch (e) {
          // console.info('{"NO_NEW_PASSWORD_ERROR"}')
        }
        if (oldPasswordError) {
          this.formError.oldPass = oldPasswordError
          ld.close()
        } else if (newPasswordError) {
          this.formError.pass = newPasswordError
          ld.close()
        } else {
          await this.$alert('修改成功', '提示', {
            type: 'success',
            confirmButtonClass: 'el-button el-button--default el-button--small el-button--success',
            showClose: false
          })
          ld.close()
          await this.close({ tagName: '/alterPassword' }) // TODO 未能正常关闭标签页
        }
        ld.close()
      } else {
        console.log('{"RECEIVE_DATA_VALID_ERROR"}')
        ld.close()
        return false
      }
    },
    submitForm (formName) {
      this.formError.oldPass = ''
      this.formError.pass = ''
      this.$refs[formName].validate((valid) => this.submitValidate(valid))
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>
<style>
</style>
