<template>
  <d2-container>
    <template slot="header">
      <h1 @click="see">
        您正在修改密码
      </h1>
    </template>
    <div style="padding-bottom: 20px">
      <el-descriptions title="用户信息">
        <el-descriptions-item label="用户名">{{ username }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <!--    <div style="margin-top: 1rem">-->
    <!--      <el-input placeholder="请输入原密码" v-model="originPassword" show-password></el-input>-->
    <!--      <el-input placeholder="请输入新密码" v-model="newPassword" show-password></el-input>-->
    <!--      <el-input placeholder="请再次输入新密码" v-model="newPasswordConfirm" show-password></el-input>-->
    <!--    </div>-->
    <!--    <div>-->
    <!--      <el-button @click="submit" type="success">修改</el-button>-->
    <!--    </div>-->

    <div style="width: 40%">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="auto" label-position="left"
               class="demo-ruleForm">
        <el-form-item label="原密码" prop="oldPass" :error="formError.oldPass">
          <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="pass" :error="formError.pass">
          <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass" :error="formError.checkPass">
          <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

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
    async see () {
      console.log(this.current)
      console.log(this.opened)
    },
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
