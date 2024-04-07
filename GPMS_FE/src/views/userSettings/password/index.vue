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
    var validateOldPass = (rule, value, callback) => {
      // 验证原密码是否为空
      if (value === '') {
        // 如果原密码为空，则返回错误提示
        callback(new Error('请输入原密码'))
      } else {
        // 如果原密码不为空，通过验证
        callback()
      }
    }
    var validatePass = (rule, value, callback) => {
      // 验证新密码是否为空
      if (value === '') {
        // 如果新密码为空，则返回错误提示
        callback(new Error('请输入密码'))
      } else {
        // 如果已经输入确认密码，需要再次验证确认密码
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        // 新密码不为空，通过验证
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      // 验证确认密码是否为空
      if (value === '') {
        // 如果确认密码为空，则返回错误提示
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.pass) {
        // 如果确认密码与新密码不一致，则返回错误提示
        callback(new Error('两次输入密码不一致!'))
      } else {
        // 如果确认密码不为空且与新密码一致，通过验证
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
    /**
     * 定义submitForm方法，用于提交表单
     * 参数formName是一个字符串，指定了要提交的表单的名称
     * @param formName
     */
    submitForm (formName) {
      // 清空表单错误信息
      // 这里假设formError是一个对象，用来存储表单验证过程中出现的错误信息
      // oldPass和pass可能是表单中的两个字段，分别对应旧密码和新密码
      // 在提交表单之前，将这两个字段的错误信息清空，以确保不会显示之前的验证错误
      this.formError.oldPass = ''
      this.formError.pass = ''

      // 使用$refs访问Vue组件中的DOM元素或子组件
      // [formName]是一个动态的属性名，对应于传入的formName参数
      // validate是Vue表单组件的一个方法，用于执行表单验证
      // validate方法接受一个回调函数作为参数，这个回调函数会在验证完成后被调用
      // 回调函数的参数valid是一个布尔值，表示表单是否通过验证
      this.$refs[formName].validate((valid) => {
        // 在回调函数内部调用submitValidate方法
        // 将valid作为参数传递给submitValidate方法，以便根据表单验证的结果执行相应的操作
        this.submitValidate(valid)
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
