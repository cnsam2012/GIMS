<template>
  <div>
    <div>
      通过给 D2 Crud 传入 rowHandle 可开启表格操作列，传入 columnHeader 可以自定义操作列的表头，传入 edit 对象可以开启编辑模式，需要传入 edit-template 来为编辑添加模板，通过 ref 调用 D2 Crud 的 showDialog 方法，并传入 mode: 'edit' 、rowIndex 属性和 template对象，可使用自定义模板编辑指定行，定义 edit-title 来修改编辑模态框的标题，向form-options 中传入 labelWidth 和 labelPosition 来控制表单中label的显示, saveLoading 则控制保存按钮的loading状态， row-edit 事件控制数据编辑，参数： index 是当前编辑行的索引， row 是当前编辑行的数据， done 用于控制编辑成功，可以在 done() 之前加入自己的逻辑代码，done()可以传入包含表单字段的对象来覆盖提交的数据，done(false) 可以取消编辑，通过 ref 调用 D2 Crud 的 closeDialog 方法可以关闭模态框。代码如下：
    </div>
    <d2-crud-x
      ref="d2Crud"
      :columns="columns"
      :data="data"
      :rowHandle="rowHandle"
      edit-title="我的修改"
      :edit-template="editTemplate"
      :form-options="formOptions"
      @dialog-open="handleDialogOpen"
      @row-edit="handleRowEdit"
      @dialog-cancel="handleDialogCancel">
      <el-button slot="header" style="margin-bottom: 5px" @click="editRowWithNewTemplate">使用自定义模板编辑第三行</el-button>
    </d2-crud-x>
  </div>
</template>

<script>
export default {
  data () {
    return {
      columns: [
        {
          title: '日期',
          key: 'date'
        },
        {
          title: '姓名',
          key: 'name'
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
          address: '上海市普陀区金沙江路 1518 弄',
          forbidEdit: true,
          showEditButton: true
        },
        {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄',
          forbidEdit: false,
          showEditButton: true
        },
        {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1519 弄',
          forbidEdit: false,
          showEditButton: false
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄',
          forbidEdit: false,
          showEditButton: true
        }
      ],
      rowHandle: {
        columnHeader: '编辑表格',
        edit: {
          icon: 'el-icon-edit',
          text: '点我进行编辑',
          size: 'small',
          show (index, row) {
            if (row.showEditButton) {
              return true
            }
            return false
          },
          disabled (index, row) {
            if (row.forbidEdit) {
              return true
            }
            return false
          }
        }
      },
      editTemplate: {
        date: {
          title: '日期',
          value: ''
        },
        name: {
          title: '姓名',
          value: ''
        },
        address: {
          title: '地址',
          value: ''
        },
        forbidEdit: {
          title: '禁用按钮',
          value: false,
          component: {
            show: false
          }
        },
        showEditButton: {
          title: '显示按钮',
          value: true,
          component: {
            show: false
          }
        }
      },
      formOptions: {
        labelWidth: '80px',
        labelPosition: 'left',
        saveLoading: false
      }
    }
  },
  methods: {
    handleDialogOpen ({ mode, row }) {
      this.$message({
        message: '打开模态框，模式为：' + mode,
        type: 'success'
      })
    },
    // todo 看这里看这里
    editRowWithNewTemplate () {
      this.$refs.d2Crud.showDialog({
        mode: 'edit',
        rowIndex: 2,
        template: {
          date: {
            title: '日期',
            value: ''
          },
          name: {
            title: '姓名',
            value: ''
          }
        }
      })
    },
    handleRowEdit ({ index, row }, done) {
      this.formOptions.saveLoading = true
      setTimeout(() => {
        console.log(index)
        console.log(row)
        this.$message({
          message: '编辑成功xxxxx',
          type: 'success'
        })

        // done可以传入一个对象来修改提交的某个字段
        done({
          address: '我是通过done事件传入的数据！'
        })
        this.formOptions.saveLoading = false
      }, 300)
    },
    handleDialogCancel (done) {
      this.$message({
        message: '取消编辑',
        type: 'warning'
      })
      done()
    }
  }
}
</script>
