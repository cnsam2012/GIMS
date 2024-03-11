<template>
  <d2-container>
    <template slot="header">

      <!--      <el-input-->
      <!--        type="text"-->
      <!--        v-model="formLogin.code"-->
      <!--        placeholder="验证码">-->
      <!--        <template slot="append">-->
      <!--          <img class="login-code" :src="imgSrc" alt="kaptcha_pic" @click="refreshKaptcha">-->
      <!--          &lt;!&ndash;                      <img class="login-code" src="./image/login-code.png" alt="kaptcha_pic" @click="refreshKaptcha">&ndash;&gt;-->
      <!--        </template>-->
      <!--      </el-input>-->
    </template>
    <d2-crud-x
      ref="departments"
      :columns="columns"
      :addTemplate="addTemplate"
      :data="data"
      :rowHandle="rowHandle"
      :pagination="pagination"
      :form-options="formOptions"
      @show-detail="showDetail"
      @on-edit="onEdit"
      @on-delete="onDelete"
      @pagination-current-change="paginationCurrentChange"
      @dialog-cancel="handleDialogCancel"
      @dialog-open="handleDialogOpen"
      @row-add="handleRowAdd"
    >
      <el-button
        slot="header"
        type="success"
        style="margin: 7px"
        @click="onAdd"
      >新增部门
      </el-button>
      <el-input
        slot="header"
        type="text"
        v-model="searchKeywords"
        placeholder="搜索部门..."
        style="width: 20%; margin: 7px"
      >
        <template slot="append">
          <el-button
            @click="onSearch"
          >搜索
          </el-button>
        </template>
      </el-input>
    </d2-crud-x>
  </d2-container>
</template>

<script>

export default {
  data () {
    return {
      addTemplate: {
        id: {
          title: '部门id',
          value: 'id'
        },
        type: {
          title: '部门类型',
          value: 'type'
        },
        name: {
          title: '部门名称',
          value: 'name'
        },
        content: {
          title: '备注',
          value: 'content'
        },
        belong_to: {
          title: '创建者',
          value: 'belong_to'
        }
      },
      columns: [
        {
          title: '部门id',
          key: 'id'
        },
        {
          title: '部门类型',
          key: 'type'
        },
        {
          title: '部门名称',
          key: 'name'
        },
        {
          title: '备注',
          key: 'content'
        },
        {
          title: '创建者',
          key: 'belong_to'
        }
      ],
      data: [
        {
          id: 'dep001',
          type: '研发',
          name: '技术部',
          content: '负责公司的技术开发和创新',
          belong_to: '张三'
        },
        {
          id: 'dep002',
          type: '市场',
          name: '营销部',
          content: '负责市场推广和销售策略',
          belong_to: '李四'
        }
      ],
      rowHandle: {
        custom: [
          {
            text: '查看',
            type: 'success',
            size: 'small',
            emit: 'show-detail'
          },
          {
            text: '更改',
            type: 'light',
            size: 'small',
            emit: 'on-edit'
          },
          {
            text: '删除',
            type: 'danger',
            size: 'small',
            emit: 'on-delete'
          }
        ]
      },
      pagination: {
        currentPage: 1,
        pageSize: 5,
        total: 6
      },
      formOptions: {
        maxHeight: '90%'
      },
      searchKeywords: ''
    }
  },
  methods: {
    showDetail ({
      index,
      row
    }) {
      console.log('1')
      console.log(row)
    },
    onEdit ({
      index,
      row
    }) {
      console.log('2')
      console.log(row)
    },
    onDelete ({
      index,
      row
    }) {
      console.log('3')
      console.log(row)
    },
    paginationCurrentChange (currentPage) {
      this.pagination.currentPage = currentPage
      console.log(currentPage)
      // this.fetchData()
    },
    onSearch () {
      console.log(this.searchKeywords)
    },
    onAdd () {
      this.$refs.departments.showDialog({
        mode: 'add'
      })
    },
    handleDialogCancel (done) {
      this.$message({
        message: '取消保存',
        type: 'warning'
      })
      done()
    },
    handleRowAdd (row, done) {
      this.formOptions.saveLoading = true
      setTimeout(() => {
        console.log(row)
        this.$message({
          message: '保存成功',
          type: 'success'
        })

        // done可以传入一个对象来修改提交的某个字段
        done({
          address: '我是通过done事件传入的数据！'
        })
        this.formOptions.saveLoading = false
      }, 300)
    },
    handleDialogOpen ({ mode }) {
      this.$message({
        message: '打开模态框，模式为：' + mode,
        type: 'success'
      })
    }
  }
}
</script>
