<template>
  <div>

    <el-dialog
      title="添加短信模板"
      :visible.sync="dialogVisible"
      :before-close="cancel"
      width="50%"
    >
      <el-form
        label-position="left"
        label-width="80px"
        class="table-expand"
      >
        <el-form-item
          label="短信名称"
          :rules="[
              { required: true},
            ]"
        >
          <el-input
            v-model="form.templatename"
            placeholder="请输入短信名称"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="短信内容"
          :rules="[
              { required: true},
            ]"
        >
          <el-input
            type="textarea"
            :rows="4"
            v-model="form.templatecontent"
            placeholder="请输入短信内容"
          ></el-input>
        </el-form-item>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="cancel">取 消</el-button>
        <el-button
          type="primary"
          @click="addSubmit"
          v-loading="addDisabled"
        >{{this.form.templatecode ? "修 改" : "添 加"}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { addMessage, updateMessage } from "@/api/message/index";
import qs from "qs";
import { setTimeout } from "timers";
export default {
  data() {
    return {
      form: {},
      selfDialogVisible: this.dialogVisible,
      addDisabled: false
    };
  },
  props: {
    dialogVisible: {
      required: true
    },
    parentForms: {
      required: true
    }
  },
  components: {
  },
  created() {
    this.form = Object.assign({}, this.parentForms);
  },
  methods: {
    //添加/修改岗位
    addSubmit() {
      let _this = this;
      if (this.addDisabled) {
        return false;
      }
      if (!this.form.templatename) {
        this.$message.warning("请填写短信名称");
        return;
      }
      if (!this.form.templatecontent) {
        this.$message.warning("请填写短信内容");
        return;
      }
      //flag 1-组织部  2-人事部
      this.form.flag = 1;
      this.addDisabled = true;
      if (this.form.templatecode) {
        //修改
        new Promise((response, reject) => {
          updateMessage(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.getList();
              } else {
                this.$message.error(response.data.msg);
              }
              setTimeout(() => {
                _this.addDisabled = false;
              }, 500);
            })
            .catch(error => {
              reject(error);
            });
        });
      } else {
        //添加
        new Promise((response, reject) => {
          addMessage(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.getList();
              } else {
                this.$message.error(response.data.msg);
              }
              setTimeout(() => {
                _this.addDisabled = false;
              }, 500);
            })
            .catch(error => {
              reject(error);
            });
        });
      }
    },
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //调用父亲查询列表方法
    getList() {
      this.$emit("childGetList", false);
    }
  },
  watch: {
    dialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
    },
    parentForms(val, oldVal) {
      this.form = Object.assign({}, val);
    }
  }
};
</script>
<style scoped lang="scss">
// .table-expand {
//   padding: 0px;
//   .el-form-item {
//     margin-right: 0;
//     margin-bottom: 10px;
//     width: 100%;
//   }
// }
</style>
