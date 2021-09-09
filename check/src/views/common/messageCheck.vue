<template>

  <div>
    <el-dialog
      :title="title"
      :visible.sync="messageDialogVisible"
      :before-close="cancel"
      width="50%"
      custom-class="score-dialog"
    >
      <el-form
        ref="form"
        label-width="80px"
      >
        <el-form-item label="短信模板">
          <el-select
            v-model="message"
            placeholder="请选择短信模板"
            style="width:100%"
            filterable
            clearable
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.templatecode"
              :label="item.templatename"
              :value="item.templatecontent"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="短信内容" v-if="message">
          <el-input type="textarea" v-model="content"></el-input>
        </el-form-item>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="cancel">取 消</el-button>
        <el-button
          type="primary"
          @click="editSubmit"
        >确定</el-button>
      </span>
    </el-dialog>
  </div>

</template>
<script>
import { getAllList } from "@/api/message/index";
import qs from "qs";
export default {
  data() {
    return {
      title: "请选择短信模板",
      message: "",
      statusOptions: [],
      selfDialogVisible: this.messageDialogVisible,
      selfSelectedOptions: [""],
      content:""
    };
  },
  props: {
    messageDialogVisible: {
      required: true
    },
    messageType: {
      required: true
    }
  },
  created() {
    this.selfSelectedOptions = [""];
    this.getMessageList();
  },
  methods: {
    //获取短信模板列表
    getMessageList() {
      let data = {
        flag: 1
      };
      new Promise((response, reject) => {
        getAllList(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.statusOptions = response.data.data;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    /**
     * 修改短信模板
     * type = 1 一键发送所有人
     * type = 2 月份总结发送短信
     * type = 3 评分关系管理发送短信
     * type = 4 评分关系人发送短信
     * type = 5 评分报告发送短信
     */
    editSubmit() {
      if (!this.message) {
        this.$message.warning("请先选择模板");
        return;
      }
      if (this.messageType == 1) {
        this.$emit("childSendMessageAll", this.content);
      } else if (this.messageType == 2) {
        this.$emit("childSendMessagePart", this.content);
      } else if (this.messageType == 3 || this.messageType == 4 || this.messageType == 5) {
        this.$emit("childSendMessageUser", this.content);
      }
    }
  },
  watch: {
    messageDialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
      this.selfSelectedOptions = [""];
    },
    message(val){
      this.content = val;
    }
  }
};
</script>
<style scoped>
</style>
