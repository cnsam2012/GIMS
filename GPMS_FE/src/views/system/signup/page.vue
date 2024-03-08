<template>
  <div class="page-login">
    <div class="page-login--layer page-login--layer-area">
      <ul class="circles">
        <li v-for="n in 10" :key="n"></li>
      </ul>
    </div>
    <div
      class="page-login--layer page-login--layer-time"
      flex="main:center cross:center">
      <!--      {{ time }}-->
    </div>
    <div class="page-login--layer">
      <div
        class="page-login--content"
        flex="dir:top main:justify cross:stretch box:justify">
        <div class="page-login--content-header">

        </div>
        <div
          class="page-login--content-main"
          flex="dir:top main:center cross:center">
          <!-- logo -->
          <!--          <img class="page-login&#45;&#45;logo" src="./image/logo@2x.png">-->
          <h1 style="color: #717277">注&nbsp;册</h1>
          <!-- form -->
          <div class="page-login--form">
            <el-card shadow="never">
              <el-form
                ref="signUpForm"
                label-position="top"
                :rules="rules"
                :model="formSignup"
                size="default">
                <el-form-item prop="username">
                  <el-input
                    type="text"
                    v-model="formSignup.username"
                    placeholder="用户名">
                    <i slot="prepend" class="fa fa-user-circle-o"></i>
                  </el-input>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input
                    type="password"
                    v-model="formSignup.password"
                    placeholder="密码"
                    auto-complete="off"
                  >
                    <i slot="prepend" class="fa fa-keyboard-o"></i>
                  </el-input>
                </el-form-item>
                <el-form-item prop="confirmPassword">
                  <el-input
                    type="password"
                    v-model="formSignup.confirmPassword"
                    placeholder="确认密码"
                    auto-complete="off"
                  >
                    <i slot="prepend" class="fa fa-key"></i>
                  </el-input>
                </el-form-item>
                <el-form-item prop="email">
                  <el-input
                    type="text"
                    v-model="formSignup.email"
                    placeholder="电子邮箱">
                    <i slot="prepend" class="fa fa-envelope-o"></i>
                  </el-input>
                </el-form-item>
                <el-form-item prop="roleType">
                  <el-select
                    v-model="formSignup.roleType"
                    placeholder="您是..."
                    class="el-input-group"
                    @change="roleTypeChanged"
                  >
                    <el-option label="实习学生" value="1"></el-option>
                    <el-option label="实习指导老师" value="2"></el-option>
                    <el-option label="实习单位" value="3"></el-option>
                    <el-option label="院系管理人员" value="9" disabled></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item prop="roleName" v-show="isRoleNameShow">
                  <el-input
                    type="text"
                    v-model="formSignup.roleName"
                    v-bind:placeholder="roleNamePlaceHolder">
                  </el-input>
                </el-form-item>
                <el-button
                  size="default"
                  @click="submit"
                  type="success"
                  class="button-login"
                >
                  注册
                </el-button>
              </el-form>
            </el-card>
            <p
              class="page-login--options"
              flex="main:justify cross:center">
            </p>
          </div>
        </div>
        <div class="page-login--content-footer">
          <!--          多语言切换 -->
          <!--          <p class="page-login&#45;&#45;content-footer-locales">-->
          <!--            <a-->
          <!--              v-for="language in $languages"-->
          <!--              :key="language.value"-->
          <!--              @click="onChangeLocale(language.value)">-->
          <!--              {{ language.label }}-->
          <!--            </a>-->
          <!--          </p>-->
          <p class="page-login--content-footer-copyright">
            By Chen C. for GraProj only
          </p>
          <p class="page-login--content-footer-copyright">
            Front End Thanks for FairyEver
          </p>
          <p class="page-login--content-footer-copyright">
            Copyright
            <d2-icon name="copyright"/>
            2018 D2 Projects 开源组织出品
            <a href="https://github.com/FairyEver">
              @FairyEver
            </a>
          </p>
          <p class="page-login--content-footer-options">
            <a href="#">帮助</a>
            <a href="#">隐私</a>
            <a href="#">条款</a>
          </p>
        </div>
      </div>
    </div>
    <el-dialog
      title="快速选择用户"
      :visible.sync="dialogVisible"
      width="400px">
      <el-row :gutter="10" style="margin: -20px 0px -10px 0px;">
        <el-col v-for="(user, index) in users" :key="index" :span="8">
          <div class="page-login--quick-user" @click="handleUserBtnClick(user)">
            <d2-icon name="user-circle-o"/>
            <span>{{ user.name }}</span>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import dayjs from 'dayjs'
import { mapActions } from 'vuex'
import localeMixin from '@/locales/mixin.js'
import kaptchaFaild from './image/login-code.png'
import api from '@/api'

export default {
  mixins: [
    localeMixin
  ],
  data () {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.formSignup.confirmPassword !== '') {
          this.$refs.signUpForm.validateField('confirmPassword')
        }
        callback()
      }
    }
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.formSignup.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    /**
     * 验证是否为邮箱
     * @param {*} s
     */
    const isEmail = (s) => {
      return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s)
    }
    const validateEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入邮箱'))
      } else if (!isEmail(value)) {
        callback(new Error('请输入正确的邮箱地址如：mymail@example.com'))
      } else {
        callback()
      }
    }
    return {
      timeInterval: null,
      time: dayjs().format('HH:mm:ss'),
      // 快速选择用户
      dialogVisible: false,
      users: [
        {
          name: 'Admin',
          username: 'admin',
          password: 'admin'
        },
        {
          name: 'Editor',
          username: 'editor',
          password: 'editor'
        },
        {
          name: 'User1',
          username: 'user1',
          password: 'user1'
        }
      ],
      // 表单
      formSignup: {
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        roleType: '',
        roleName: ''
      },
      // 表单校验
      rules: {
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          }
        ],
        password: [
          {
            validator: validatePass,
            trigger: 'blur'
          }
        ],
        confirmPassword: [
          {
            validator: validatePass2,
            trigger: 'blur'
          }
        ],
        email: [
          {
            validator: validateEmail,
            trigger: 'blur'
          }
        ],
        roleType: [
          {
            required: true,
            message: '请选择角色类型',
            trigger: 'blur'
          }
        ],
        roleName: [
          {
            required: true,
            message: '该项不能为空',
            trigger: 'blur'
          }
        ]
      },
      imgSrc: kaptchaFaild,
      roleNameId: '',
      roleNamePlaceHolder: '1',
      isRoleNameShow: false
    }
  },
  beforeMount () {
  },
  mounted () {
    // this.timeInterval = setInterval(() => {
    //   this.refreshTime()
    // }, 1000)
    // this.refreshKaptcha()
    this.refreshPlaceHolder()
  },
  beforeDestroy () {
    clearInterval(this.timeInterval)
  },
  methods: {
    ...mapActions('d2admin/account', [
      'login'
    ]),
    refreshPlaceHolder () {
      this.roleNamePlaceHolder = '请输入' + (this.roleNameId ? this.roleNameId : '名称')
      this.rules.roleName = [{
        required: true,
        message: this.roleNamePlaceHolder,
        trigger: 'blur'
      }]
    },
    /**
     * @description 接收选择一个用户快速登录的事件
     * @param {Object} user 用户信息
     */
    handleUserBtnClick (user) {
      this.formSignup.username = user.username
      this.formSignup.password = user.password
      this.submit()
    },
    /**
     * @description 提交表单
     */
    // 提交登录信息
    async submit () {
      await this.$refs.signUpForm.validate(async (valid) => {
        if (valid) {
          await this.$message.info('提交')
          console.info(this.formSignup)
          const data = {
            username: this.formSignup.username,
            password: this.formSignup.password,
            email: this.formSignup.email,
            departmentId: -1,
            roleName: this.formSignup.roleName,
            type: this.formSignup.roleType
          }
          const res = await api.SYS_USER_SIGNUP(data)
          console.log(res)
        } else {
          // 登录表单校验失败
          this.$message.error('表单校验失败，请检查')
        }
      })
    },
    sendEmailCode () {
      this.$message.info('发送邮箱验证码')
    },
    roleTypeChanged () {
      this.$message.info(this.formSignup.roleType)
      switch (this.formSignup.roleType) {
        case '1':
          this.roleNameId = '学生真实姓名'
          this.refreshPlaceHolder()
          break
        case '2':
          this.roleNameId = '指导老师真实姓名'
          this.refreshPlaceHolder()
          break
        case '3':
          this.roleNameId = '企业 (公司) 名'
          this.refreshPlaceHolder()
          break
        default:
          break
      }
      if (!this.isRoleNameShow) {
        this.isRoleNameShow = true
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.page-login {
  @extend %unable-select;
  $backgroundColor: #F0F2F5;
  // ---
  background-color: $backgroundColor;
  height: 100%;
  position: relative;
  // 层
  .page-login--layer {
    @extend %full;
    overflow: auto;
  }

  .page-login--layer-area {
    overflow: hidden;
  }

  // 时间
  .page-login--layer-time {
    font-size: 24em;
    font-weight: bold;
    color: rgba(0, 0, 0, 0.03);
    overflow: hidden;
  }

  // 登陆页面控件的容器
  .page-login--content {
    height: 100%;
    min-height: 500px;
  }

  // header
  .page-login--content-header {
    padding: 1em 0;

    .page-login--content-header-motto {
      margin: 0px;
      padding: 0px;
      color: $color-text-normal;
      text-align: center;
      font-size: 20px;
    }
  }

  // main
  .page-login--logo {
    width: 240px;
    margin-bottom: 2em;
    margin-top: -2em;
  }

  // 登录表单
  .page-login--form {
    width: 370px;
    // 卡片
    .el-card {
      margin-bottom: 2rem;
    }

    // 登录按钮
    .button-login {
      width: 100%;
      //background-color: $color-primary;
    }

    .button-login:hover {
      width: 100%;
      background-color: rgba(63, 179, 85, 0.16);
    }

    //.el-button:hover {
    //  width: 100%;
    //  background-color: rgba(63, 179, 85, 0.16);
    //}

    // 输入框左边的图表区域缩窄
    .el-input-group__prepend {
      padding: 0px 14px;
    }

    .login-code {
      height: 40px - 2px;
      display: block;
      margin: 0px -20px;
      border-top-right-radius: 2px;
      border-bottom-right-radius: 2px;
    }

    // 登陆选项
    .page-login--options {
      margin: 0px;
      padding: 0px;
      font-size: 14px;
      color: $color-light;
      margin-bottom: 15px;
      font-weight: bold;
    }

    .page-login--quick {
      width: 100%;
    }
  }

  // 快速选择用户面板
  .page-login--quick-user {
    @extend %flex-center-col;
    padding: 10px 0px;
    border-radius: 4px;

    &:hover {
      background-color: $color-bg;

      i {
        color: $color-text-normal;
      }

      span {
        color: $color-text-normal;
      }
    }

    i {
      font-size: 36px;
      color: $color-text-sub;
    }

    span {
      font-size: 12px;
      margin-top: 10px;
      color: $color-text-sub;
    }
  }

  // footer
  .page-login--content-footer {
    padding: 1em 0;

    .page-login--content-footer-locales {
      padding: 0px;
      margin: 0px;
      margin-bottom: 15px;
      font-size: 12px;
      line-height: 12px;
      text-align: center;
      color: $color-text-normal;

      a {
        color: $color-text-normal;
        margin: 0 .5em;

        &:hover {
          color: $color-text-main;
        }
      }
    }

    .page-login--content-footer-copyright {
      padding: 0px;
      margin: 0px;
      margin-bottom: 10px;
      font-size: 12px;
      line-height: 12px;
      text-align: center;
      color: $color-text-normal;

      a {
        color: $color-text-normal;
      }
    }

    .page-login--content-footer-options {
      padding: 0px;
      margin: 0px;
      font-size: 12px;
      line-height: 12px;
      text-align: center;

      a {
        color: $color-text-normal;
        margin: 0 1em;
      }
    }
  }

  // 背景
  .circles {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
    margin: 0px;
    padding: 0px;

    li {
      position: absolute;
      display: block;
      list-style: none;
      width: 20px;
      height: 20px;
      background: #FFF;
      animation: animate 25s linear infinite;
      bottom: -200px;
      @keyframes animate {
        0% {
          transform: translateY(0) rotate(0deg);
          opacity: 1;
          border-radius: 0;
        }
        100% {
          transform: translateY(-1000px) rotate(720deg);
          opacity: 0;
          border-radius: 50%;
        }
      }

      &:nth-child(1) {
        left: 15%;
        width: 80px;
        height: 80px;
        animation-delay: 0s;
      }

      &:nth-child(2) {
        left: 5%;
        width: 20px;
        height: 20px;
        animation-delay: 2s;
        animation-duration: 12s;
      }

      &:nth-child(3) {
        left: 70%;
        width: 20px;
        height: 20px;
        animation-delay: 4s;
      }

      &:nth-child(4) {
        left: 40%;
        width: 60px;
        height: 60px;
        animation-delay: 0s;
        animation-duration: 18s;
      }

      &:nth-child(5) {
        left: 65%;
        width: 20px;
        height: 20px;
        animation-delay: 0s;
      }

      &:nth-child(6) {
        left: 75%;
        width: 150px;
        height: 150px;
        animation-delay: 3s;
      }

      &:nth-child(7) {
        left: 35%;
        width: 200px;
        height: 200px;
        animation-delay: 7s;
      }

      &:nth-child(8) {
        left: 50%;
        width: 25px;
        height: 25px;
        animation-delay: 15s;
        animation-duration: 45s;
      }

      &:nth-child(9) {
        left: 20%;
        width: 15px;
        height: 15px;
        animation-delay: 2s;
        animation-duration: 35s;
      }

      &:nth-child(10) {
        left: 85%;
        width: 150px;
        height: 150px;
        animation-delay: 0s;
        animation-duration: 11s;
      }
    }
  }
}

</style>
