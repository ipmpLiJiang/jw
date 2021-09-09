<template>
  <div>

    <el-dialog
      title="月结管理"
      :visible.sync="dialogVisible"
      :before-close="cancel"
      width="50%"
      :close-on-click-modal="closeModal"
    >
      <el-form label-width="80px">
        <!-- <el-form-item label="当前年份">
          <el-input
            v-model="form.stationname"
            disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="当前月度">
          <el-input
            v-model="form.stationname"
            disabled="true"
          ></el-input>
        </el-form-item> -->
        <el-form-item
          label="月结标题"
          :rules="[
              { required: true},
            ]"
        >
          <el-input
            v-model="form.title"
            :disabled="type==2 ? true : false"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="月结内容"
          :rules="[
              { required: true},
            ]"
        >
          <!-- <el-input
            type="textarea"
            v-model="form.content"
            :rows="4"
            :disabled="type==2 ? true : false"
          ></el-input> -->
          <Ckeditor ref="ckditor" :type="type" :fatherContent="form.content"></Ckeditor>
          
        </el-form-item>
        <el-form-item
          label="附件"
          v-if="type != 2"
        >
          <el-upload
            class="upload-demo"
            :action="actionPath"
            :on-remove="handleRemove"
            :on-exceed="handleExceed"
            :on-success="handleSuccess"
            multiple
            :limit="1"
            :file-list="fileList"
            :data="data"
            :on-preview="handlePreview"
          >
            <el-button
              size="small"
              type="primary"
            >点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item
          label="总结文件"
          v-else
        >
          <a :href="form.savepath">{{form.filename}}</a>
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
          v-if="type!=2"
        >提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { addQuarter, updateQuarter } from "@/api/user/quarter";
import { download } from "@/api/common/common";
import Ckeditor from "../common/ckeditor";
import qs from "qs";
//导入ip地址
export default {
  data() {
    return {
      form: {},
      selfDialogVisible: this.dialogVisible,
      fileList: [],
      actionPath:
        process.env.VUE_APP_ITEM_NAME +
        "summaryattachment/upload",
      data: {
        serialno: ""
      },
      closeModal: false,
      content: "",
    };
  },
  props: {
    isAdd: {
      required: true
    },
    dialogVisible: {
      required: true
    },
    type: {
      required: true
    },
    parentForms: {
      required: true
    }
  },
  components: {
    Ckeditor,
  },
  created() {
    this.form = Object.assign({}, this.parentForms);
    this.data.serialno = this.form.serialno;
    if (this.form.filename && this.form.savepath) {
      this.fileList = [{ name: this.form.filename, url: this.form.savepath }];
    } else {
      this.fileList = [];
    }
  },
  methods: {
    //添加/修改岗位
    addSubmit() {
      if (!this.form.title) {
        this.$message.warning("标题不能为空");
        return;
      }
      if (!this.$refs.ckditor.content) {
        this.$message.warning("内容不能为空");
        return;
      }
      this.form.content = this.$refs.ckditor.content;
      this.form.dbtype = this.$store.state.user.user.dbtype
      if (this.isAdd == 1) {
        //修改
        new Promise((response, reject) => {
          updateQuarter(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.getList();
              } else {
                this.$message.error(response.data.msg);
              }
            })
            .catch(error => {
              reject(error);
            });
        });
      } else {
        //添加
        delete this.form.serialno;
        new Promise((response, reject) => {
          addQuarter(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.getList();
              } else {
                this.$message.error(response.data.msg);
              }
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
    },
    handleRemove(file, fileList) {
      this.form.filename = "";
      this.form.savepath = "";
    },
    handleSuccess(file, fileList) {
      if (file.code == 0) {
        this.$message.success(file.msg);
        this.form.filename = file.filename;
        this.form.savepath = file.savepath;
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },
    handlePreview(file) {
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        "summaryattachment/download?savepath=" +
        this.form.savepath;
    }
  },
  watch: {
    dialogVisible(val, oldVal) {
      this.selfDialogVisible = val === "false" ? false : true;
    },
    parentForms(val, oldVal) {
      this.form = Object.assign({}, val);
      this.data.serialno = this.form.serialno;
      if (this.form.filename && this.form.savepath) {
        this.fileList = [{ name: this.form.filename, url: this.form.savepath }];
      } else {
        this.fileList = [];
      }
    }
  }
};
</script>
