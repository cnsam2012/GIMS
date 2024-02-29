<template>
  <d2-container>
    <template slot="header">
      <h1>
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
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="auto" label-position="left" class="demo-ruleForm">
        <el-form-item label="原密码" prop="oldPass">
          <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="pass">
          <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <template slot="footer">footer</template>
  </d2-container>
</template>
<script>
export default {
  data () {
    // var checkAge = (rule, value, callback) => {
    //   if (!value) {
    //     return callback(new Error('年龄不能为空'))
    //   }
    //   setTimeout(() => {
    //     if (!Number.isInteger(value)) {
    //       callback(new Error('请输入数字值'))
    //     } else {
    //       if (value < 18) {
    //         callback(new Error('必须年满18岁'))
    //       } else {
    //         callback()
    //       }
    //     }
    //   }, 1000)
    // }
    var validateOldPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入原密码'))
      } else {
        // if (this.ruleForm.oldPass !== '') {
        //   this.$refs.ruleForm.validateField('oldPass')
        // }
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
      rules: {
        oldPass: [
          { validator: validateOldPass, trigger: 'blur' }
        ],
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ]
      }
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
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!')
          var data = {
            oldPwd: this.ruleForm.oldPass,
            newPwd: this.ruleForm.pass
          }
          console.log(data)
        } else {
          console.log('error submit!!')
          return false
        }
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
