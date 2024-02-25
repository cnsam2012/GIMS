<template>
  <el-dropdown size="small" class="d2-mr">
    <span class="btn-text">{{ info.name ? `你好 ${info.name}` : '未登录' }}</span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item @click.native="alterUsername">
        <d2-icon name="pencil" class="d2-mr-5"/>修改用户名
      </el-dropdown-item>
      <el-dropdown-item @click.native="alterPassword">
        <d2-icon name="key" class="d2-mr-5"/>修改密码
      </el-dropdown-item>
      <el-dropdown-item @click.native="logOff">
        <d2-icon name="power-off" class="d2-mr-5"/>注销
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ])
  },
  methods: {
    ...mapActions('d2admin/account', [
      'logout'
    ]),
    /**
     * @description 登出
     */
    logOff () {
      this.logout({
        confirm: true
      })
    },
    /**
     * @description 修改密码
     */
    alterPassword () {
      console.log('alterPassword')
    },
    alterUsername () {
      console.log('alterUsername')
    }
  }
}
</script>

<style lang="scss">
.message-logout {
  .el-button--primary {
    color: white;
    background-color: #3fb355;
    border-color: #3fb355;
  }

  .el-button--primary:hover {
    color: #3fb355;
    background-color: rgba(63, 179, 85, 0.12);
  }

  .el-button:hover {
    border-color: #3fb355;
  }
}
</style>
