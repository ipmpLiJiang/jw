<template>
  <div>
    <h4 class="title">评分导入</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <!-- <el-col :span="6">
            <el-form-item label="年份">
              <el-date-picker
                v-model="search.year"
                type="year"
                placeholder="选择年"
                value-format="yyyy"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="月份">
              <el-select
                v-model="search.month"
                clearable
                placeholder="请选择"
                style="width:100%"
                filterable
              >
                <el-option
                  v-for="item in quarterOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col
            :span="6"
            v-if="flagStatus == '2'"
          >
            <el-form-item label="部门名称">
              <deptList ref="searchSection"></deptList>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="分数">
              <el-slider
                v-model="search.score"
                range
                show-stops
                :max="100"
              >
              </el-slider>
            </el-form-item>
          </el-col>

          <el-col
            :span="24"
            class="edit-btn"
          >
            <el-form-item>
              <el-button
                style="margin-left:-90px;"
                type="primary"
                @click="searchList"
              ><i class="el-icon-search"></i>搜索</el-button>
              <el-button
                type="primary"
                @click="uploadExcel"
              ><i class="icon iconfont icon-daochu-tianchong"></i>上传分数excel表</el-button>

              <!-- <el-button
                type="warning"
                @click="downloadDemo"
              ><i class="icon iconfont icon-daochu-tianchong"></i>下载模板</el-button> -->
              <!-- <el-button
                type="danger"
                :loading="pdfLoading"
                @click="uploadPdf"
              ><i class="icon iconfont icon-daochu-tianchong"></i>上传负责人签字的分数PDF</el-button>
              <el-button
                type="info"
                @click="pdfPreview"
              ><i class="icon iconfont icon-daochu-tianchong"></i>负责人签字PDF预览</el-button> -->
            </el-form-item>
          </el-col>
        </el-form>
      </el-col>
    </el-row>
    <el-row class="content">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="tableLoading"
      >
        <el-table-column
          prop="username"
          label="用户姓名"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="moneycard"
          label="账号"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="departmentname"
          label="部门"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="score"
          label="分数"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="year"
          label="年份"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="month"
          label="月份"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="notes"
          label="备注"
          show-overflow-tooltip
        >
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-row>
    <!-- 上传分数excel表 -->
    <el-dialog
      title="提示"
      :visible.sync="excelDialogVisible"
      width="30%"
    >
      <span class="monitor">请先下载模板填好分数，然后上传分数excel表格</span>
      <span
        class="a-demo"
        @click="downloadDemo"
      >模板下载.excel</span>
      <el-upload
        class="upload-grade-leading-in"
        :action="excelActionPath"
        :on-success="excelHandleSuccess"
        :on-error="excelHandleError"
        :on-progress="excelHandleProgress"
        :before-upload="beforeUpload"
        :data="fileData"
        multiple
        :limit="1"
        :on-exceed="excelHandleExceed"
        ref="excelUpload"
        accept=".xlsx,.xls"
      >
        <div style="text-align:left;">
          <el-button
            type="primary"
            :loading="excelLoading"
          ><i class="icon iconfont icon-daochu-tianchong"></i>上传分数excel表</el-button>
        </div>
      </el-upload>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="excelDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="confirmExcel"
        >提 交</el-button>
      </span>
    </el-dialog>
    <!-- 上传pdf -->
    <el-dialog
      title="提示"
      :visible.sync="pdfDialogVisible"
      width="30%"
    >
      <span class="monitor">只支持上传PDF格式文件</span>
      <el-upload
        class="upload-grade-leading-in"
        :action="pdfActionPath"
        :on-success="pdfHandleSuccess"
        :on-error="pdfHandleError"
        :on-progress="pdfHandleProgress"
        :before-upload="excelbeforeUpload"
        :data="fileData"
        multiple
        :limit="1"
        :on-exceed="pdfHandleExceed"
        ref="pdfUpload"
        accept=".PDF"
      >
        <div style="text-align:left;">
          <el-button
            type="danger"
            :loading="pdfLoading"
          ><i class="icon iconfont icon-daochu-tianchong"></i>上传负责人签字的分数PDF</el-button>
        </div>

      </el-upload>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="pdfDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="confirmPdf"
        >提 交</el-button>
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
  getList,
  confirmUpload,
  getSectionList,
  confirmUploadByManager,
  getAttachByDepart,
  confirmImportExcel
} from "@/api/personnel/index";
import { confirmImportExcelByManager } from "@/api/personnel/user";
import qs from "qs";
import deptList from "./common/deptList";
export default {
  data() {
    return {
      quarterOptions: this.common.seasonOptions(),
      title: "",
      search: {
        month: "",
        year: "",
        score: [0, 100]
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableLoading: true,
      excelLoading: false,
      pdfLoading: false,
      excelActionPath:
        process.env.VUE_APP_ITEM_NAME +
        "personnel/importExcel",
      pdfActionPath:
        process.env.VUE_APP_ITEM_NAME +
        "attachment/upload",
      pdfFilePath: "",
      pdfFileName: "",
      fileData: {},
      excelDialogVisible: false,
      pdfDialogVisible: false,
      excelPath: "",
      demoDialogVisible: false,
      choiceDemo: "",
      deptOptions: [],
      choiceDemoType: 1,
      choiceDemoLoading: false,
      flagStatus: ""
    };
  },
  components: {
    deptList
  },
  mounted() {},
  created() {
    this.getList();
    this.getDeptList();
    this.flagStatus = localStorage.getItem("flagStatus");
  },
  methods: {
    //初始化
    into() {
      this.page.pageNum = 1;
      this.page.pageSize = 10;
    },
    //设置每页多少条数据
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getList();
    },
    //翻页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getList();
    },
    //搜索
    searchList() {
      this.into();
      if (
        (this.search.month && this.search.year) ||
        (!this.search.month && !this.search.year)
      ) {
        this.getList();
      } else {
        this.$message.warning("年份和月份必须一起选择才可以搜索");
      }
    },
    //查询列表
    getList() {
      let params = this.page;
      params.month = this.search.month;
      params.year = this.search.year;
      params.score1 = this.search.score[0];
      params.score2 = this.search.score[1];
      if (this.$refs.searchSection) {
        params.departmentname = this.$refs.searchSection.value;
      } else {
        params.departmentname = "";
      }
      new Promise((response, reject) => {
        getList(qs.stringify(params))
          .then(response => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.pdfFilePath = response.data.attachment.filepath;
              this.total = response.data.pageTotals;
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
    //下载模板
    downloadDemo() {
      // this.$store.state.user.user.roleList
      if (localStorage.getItem("flagStatus") == "2") {
        this.choiceDemoType = 1;
        this.demoDialogVisible = true;
      } else {
        window.location.href =
          process.env.VUE_APP_ITEM_NAME +
          "personnel/downloadTemplate?moneycard=" +
          this.$store.state.user.user.moneycard;
      }
    },
    //pdf预览
    pdfPreview() {
      if (localStorage.getItem("flagStatus") == "2") {
        this.choiceDemoType = 3;
        this.demoDialogVisible = true;
      } else {
        if (!this.pdfFilePath) {
          this.$message.warning("请先上传负责人签字的分数PDF！");
          return;
        }
        window.open(process.env.VUE_APP_ITEM_NAME + this.pdfFilePath);
      }
    },
    //上传分数excel表
    excelHandleSuccess(file) {
      if (file.code == 0) {
        this.$message.success(file.msg);
        this.excelPath = file.savepath;
      } else {
        this.$message.warning(file.msg);
      }
      this.excelLoading = false;
      // this.$refs.excelUpload.clearFiles();
    },
    excelHandleError(err, file, fileList) {
      this.$message.error(err);
      this.excelLoading = false;
      this.$refs.excelUpload.clearFiles();
    },
    excelHandleProgress(event, file, fileList) {
      this.excelLoading = true;
    },
    excelHandleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
      this.excelLoading = false;
    },
    //上传分数pdf
    pdfHandleSuccess(file) {
      if (file.code == 0) {
        this.$message.success(file.msg);
        this.pdfFilePath = file.filepath;
        this.pdfFileName = file.filename;
      } else {
        this.$message.warning(file.msg);
      }
      this.pdfLoading = false;
      // this.$refs.pdfUpload.clearFiles();
    },
    pdfHandleError(err, file, fileList) {
      this.$message.error(err);
      this.pdfLoading = false;
      this.$refs.pdfUpload.clearFiles();
    },
    pdfHandleProgress(event, file, fileList) {
      this.pdfLoading = true;
    },
    pdfHandleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
      this.pdfLoading = false;
    },
    excelbeforeUpload() {
      this.fileData.year = this.search.year;
      this.fileData.month = this.search.month;
      this.fileData.usercode = this.$store.state.user.user.usercode;
    },
    beforeUpload() {
      this.fileData.year = this.search.year;
      this.fileData.month = this.search.month;
      this.fileData.usercode = this.$store.state.user.user.usercode;
    },
    //打开上传excel
    uploadExcel() {
      this.excelDialogVisible = true;
    },
    //打开上传pdf
    uploadPdf() {
      if (localStorage.getItem("flagStatus") == "2") {
        this.choiceDemoType = 2;
        this.demoDialogVisible = true;
      } else {
        this.pdfDialogVisible = true;
      }
    },
    //excel确认
    confirmExcel() {
      if (!this.excelPath) {
        this.$message.warning("请先上传");
        return;
      }
      let data = {
        year: this.search.year,
        month: this.search.month,
        savepath: this.excelPath,
        usercode: this.$store.state.user.user.usercode
      };
      if (localStorage.getItem("flagStatus") == "2") {
        data.departmentname = this.choiceDemo;
        new Promise((response, reject) => {
          confirmImportExcelByManager(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.getList();
              } else {
                // this.$message({
                //   message: response.data.msg,
                //   type: "error",
                //   duration:0
                // });
                this.$alert(response.data.msg, "友情提示", {
                  confirmButtonText: "确定",
                  type: 'error'
                });
              }
              this.excelDialogVisible = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      } else {
        new Promise((response, reject) => {
          data.departmentname = this.choiceDemo;
          confirmImportExcel(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.getList();
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
              this.excelDialogVisible = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      }
    },
    //pdf确认
    confirmPdf() {
      if (!this.pdfFilePath) {
        this.$message.warning("请先上传");
        return;
      }
      let data = {
        year: this.search.year,
        month: this.search.month,
        filepath: this.pdfFilePath,
        filename: this.pdfFileName
      };
      if (localStorage.getItem("flagStatus") == "2") {
        data.depart = this.choiceDemo;
        new Promise((response, reject) => {
          confirmUploadByManager(qs.stringify(data))
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
          confirmUpload(qs.stringify(data))
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
      if (this.choiceDemoType == 1) {
        window.location.href =
          process.env.VUE_APP_ITEM_NAME +
          "personnel/downloadTemplateByManager?departmentname=" +
          this.choiceDemo;
      } else if (this.choiceDemoType == 2) {
        this.pdfDialogVisible = true;
      } else if (this.choiceDemoType == 3) {
        let data = {
          year: this.search.year,
          month: this.search.month,
          depart: this.choiceDemo
        };
        this.choiceDemoLoading = true;
        new Promise((response, reject) => {
          getAttachByDepart(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                window.open(process.env.VUE_APP_ITEM_NAME + response.data.filepath);
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
              this.choiceDemoLoading = false;
              this.demoDialogVisible = false;
            })
            .catch(error => {
              reject(error);
            });
        });
        return;
      }

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
.search {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  margin: 20px;
  border-radius: 4px;
  padding-top: 15px;
  .el-form-item {
    margin: 0px;
  }
  .el-button {
    margin-left: 10px;
  }
  .edit-btn {
    padding: 15px 0px;
    margin-top: 15px;
    border: 1px solid #ededee;
    background: #fcfcfc;
    .el-button {
      margin-left: 10px;
    }
    span {
      color: #8a919b;
      font-size: 0.9em;
      float: right;
      .icon-jinggao {
        color: #f3ad0e;
        position: relative;
        top: 1px;
      }
      i {
        margin-right: 3px;
      }
      .icon-daochu-tianchong {
        font-size: 12px;
      }
      .icon-dayin {
        font-size: 12px;
      }
    }
  }
}
.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 0px 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
}
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
.upload-grade-leading-in {
  display: inline-block;
  text-align: left;
}
.monitor {
  color: #f00;
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 10px;
  display: block;
  text-align: left;
}
.a-demo {
  display: block;
  color: #e6a23c;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  text-decoration: underline;
  text-align: left;
  cursor: pointer;
}
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
