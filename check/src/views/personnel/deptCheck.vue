<template>
  <div>
    <h4 class="title">部门考核办法</h4>
    <el-row class="content">
      <el-form
        status-icon
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="年份">
          <el-date-picker
            v-model="year"
            type="year"
            placeholder="选择年"
            value-format="yyyy"
            @change="changeYear"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="附件"
          prop="pass"
        >
          <el-button
            type="primary"
            :loading="loading"
            @click="uploadPdf"
          ><i class="icon iconfont icon-daochu-tianchong"></i>上传负责人签字、分管院长签字版的pdf</el-button>
        </el-form-item>
        <el-form-item
          label="附件预览"
          prop="checkPass"
        >
          <div
            @click="previewPdf"
            class="file-name"
            v-if="flagStatus !='2'"
          >{{pdfFileName}}</div>
          <div
            class="file-name-manger"
            v-else
          >
            管理员请在部门考核列表查看
          </div>
        </el-form-item>
        <el-form-item
          label="备注"
          prop="checkPass"
        >
          <span style="color:#F56C6C;font-weight:bold;">上传的文件请命名为:XXX处/办公室/科（部门名）绩效考核办法</span>
        </el-form-item>
      </el-form>
    </el-row>
    <!-- 上传 -->
    <el-dialog
      title="提示"
      :visible.sync="pdfDialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <span class="monitor">只支持上传PDF格式文件</span>
      <el-upload
        class="upload-grade-leading-in"
        :action="actionPath"
        :on-success="handleSuccess"
        :on-error="handleError"
        :on-progress="handleProgress"
        :before-upload="beforeUpload"
        :data="fileData"
        multiple
        :limit="1"
        :on-exceed="handleExceed"
        ref="upload"
        accept=".PDF"
      >
        <el-button
          type="primary"
          :loading="loading"
        ><i class="icon iconfont icon-daochu-tianchong"></i>上传负责人签字、分管院长签字版的pdf</el-button>
      </el-upload>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="pdfDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="confirmPdf"
        >确 定</el-button>
      </span>
    </el-dialog>
    <!-- 超级管理员选择下载模板 -->
    <el-dialog
      title="提示"
      :visible.sync="demoDialogVisible"
      width="30%"
    >
      <span>选择模板部门：</span>
      <el-select
        v-model="choiceDemo"
        placeholder="请选择"
        filterable
        clearable
      >
        <el-option
          v-for="item in deptOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="demoDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="confirmDemo"
          :loading="choiceDemoLoading"
        >确 认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  selectDeptCheckByYear,
  confirmDept,
  getSectionList,
  deptCheckConfirmUploadByManager
} from "@/api/personnel/index";
import qs from "qs";
export default {
  data() {
    return {
      year: new Date(),
      loading: false,
      actionPath:
        process.env.VUE_APP_ITEM_NAME +
        "deptCheck/upload",
      pdfFilePath: "",
      pdfFileName: "",
      fileData: {
        year: ""
      },
      pdfDialogVisible: false,
      demoDialogVisible: false,
      choiceDemo: "",
      deptOptions: [],
      choiceDemoType: 1,
      choiceDemoLoading: false,
      flagStatus: ""
    };
  },
  components: {},
  mounted() {},
  created() {
    this.getDeptList();
    this.getDetail();
    this.flagStatus = localStorage.getItem("flagStatus");
  },
  methods: {
    //查询部门pdf
    getDetail() {
      let data = {};
      data.year = this.yearCheck();
      new Promise((response, reject) => {
        selectDeptCheckByYear(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.pdfFilePath = response.data.data.filepath;
              this.pdfFileName = response.data.data.filename;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //改变年份
    changeYear() {
      this.getDetail();
    },
    //上传pdf
    handleSuccess(file) {
      if (file.code == 0) {
        this.$message.success(file.msg);
        this.pdfFilePath = file.filepath;
        this.pdfFileName = file.filename;
      } else {
        this.$message.warning(file.msg);
      }
      this.loading = false;
      // this.$refs.upload.clearFiles();
    },
    handleError(err, file, fileList) {
      this.$message.error(err);
      this.loading = false;
      this.$refs.upload.clearFiles();
    },
    handleProgress(event, file, fileList) {
      this.loading = true;
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
      this.loading = false;
    },
    previewPdf() {
      if (!this.pdfFilePath) {
        this.$message.warning("请先上传负责人签字、分管院长签字版的pdf！");
        return;
      }
      window.open(process.env.VUE_APP_ITEM_NAME + this.pdfFilePath);
    },
    beforeUpload() {
      this.fileData.year = this.yearCheck();
    },
    //打开上传pdf
    uploadPdf() {
      if (localStorage.getItem("flagStatus") == "2") {
        this.demoDialogVisible = true;
      } else {
        this.pdfDialogVisible = true;
      }
    },
    //pdf确认
    confirmPdf() {
      if (localStorage.getItem("flagStatus") == "2") {
        if (!this.choiceDemo) {
          this.$message.warning("请选择部门先");
          return;
        }
      }
      if (!this.pdfFilePath) {
        this.$message.warning("请先上传");
        return;
      }
      let data = {
        filepath: this.pdfFilePath,
        filename: this.pdfFileName
      };
      data.year = this.yearCheck();
      if (localStorage.getItem("flagStatus") == "2") {
        data.depart = this.choiceDemo;
        new Promise((response, reject) => {
          deptCheckConfirmUploadByManager(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
              } else {
                // this.$message({
                //   message: response.data.msg,
                //   type: "error"
                // });
                this.$alert(response.data.msg, "友情提示", {
                  confirmButtonText: "确定",
                  type: 'error'
                });
              }
              this.pdfDialogVisible = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      } else {
        new Promise((response, reject) => {
          confirmDept(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
              } else {
                // this.$message({
                //   message: response.data.msg,
                //   type: "error"
                // });
                this.$alert(response.data.msg, "友情提示", {
                  confirmButtonText: "确定",
                  type: 'error'
                });
              }
              this.pdfDialogVisible = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      }
    },
    //年份验证
    yearCheck() {
      let tYear;
      if (this.year instanceof Date) {
        let date = new Date();
        tYear = date.getFullYear();
      } else {
        tYear = this.year;
      }

      return tYear;
    },
    //查询部门列表
    getDeptList() {
      new Promise((response, reject) => {
        getSectionList()
          .then(response => {
            if (response.data.code == 0) {
              this.deptOptions = [];
              response.data.data.forEach(row => {
                this.deptOptions.push({
                  value: row.department,
                  label: row.department
                });
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //选择模板下载
    confirmDemo() {
      if (!this.choiceDemo) {
        this.$message.warning("请选择部门");
        return;
      }
      this.pdfDialogVisible = true;
      this.demoDialogVisible = false;
    }
  }
};
</script>


<style lang="scss" scoped>
.title {
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eaeaea;
  padding: 0px 10px;
  font-weight: 600;
  color: #424242;
  background: #fff;
  span {
    font-weight: normal;
    color: #9b9b9b;
  }
  i {
    margin: 0px 4px;
    color: #9b9b9b;
  }
}

.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
  .table-expand {
    padding: 0px;
    .el-form-item {
      margin-right: 0;
      margin-bottom: 10px;
      width: 33.33%;
    }
    .el-select {
      width: 185px;
    }
  }
}
.file-name {
  color: #409eff;
  text-decoration: underline;
  cursor: pointer;
}
.file-name-manger {
  color: #409eff;
}
.w200 {
  width: 260px;
}
.monitor {
  color: #f00;
  font-size:18px;
  margin-bottom: 10px;
  display: block;
  text-align: left;
}
</style>
