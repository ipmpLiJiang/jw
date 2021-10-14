<template>
  <div>
    <h4 class="title">人员信息</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="6">
            <el-form-item label="姓名">
              <el-input
                placeholder="请输入员工姓名"
                v-model="search.username"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="部门名称">
              <deptList ref="searchSection"></deptList>
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
                @click="addPerson"
              ><i class="el-icon-plus"></i>添加</el-button>
              <el-button
                type="primary"
                :loading="excelLoading"
                @click="uploadExcel"
              ><i class="icon iconfont icon-daochu-tianchong"></i>上传人员excel表</el-button>
              <el-button
                type="info"
                @click="exportPerson"
              >导出人员信息</el-button>
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
          type="index"
          width="50"
          :index="indexMethod"
        >
        </el-table-column>
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
          fixed="right"
          label="操作"
          width="160"
        >
          <template slot-scope="scope">
            <el-button
              @click="editUser(scope.row)"
              type="text"
              size="small"
            >编辑</el-button>
            <el-button
              @click="deleteUser(scope.row)"
              type="text"
              size="small"
            >删除</el-button>
          </template>
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
    <!-- 添加人员dialog -->
    <el-dialog
      :title="form.id ? '修改人员' : '添加人员'"
      :visible.sync="dialogFormVisible"
      width="30%"
    >
      <el-form :model="form">
        <el-form-item
          label="用户名"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="form.username"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item
          label="账号"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="form.moneycard"
            autocomplete="off"
            :disabled="form.id ? true : false"
          ></el-input>
        </el-form-item>

        <el-form-item
          label="部门名称"
          :label-width="formLabelWidth"
        >
          <deptList
            ref="section"
            :dept="childDept"
          ></deptList>
        </el-form-item>

      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="addUser"
          :loading="addLoading"
        >确 定</el-button>
      </div>
    </el-dialog>
    <!-- 上传人员excel表 -->
    <el-dialog
      title="提示"
      :visible.sync="excelDialogVisible"
      width="30%"
    >
      <span class="monitor">请先下载模板填好人员信息，然后上传分数excel表格</span>
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
  </div>
</template>

<script>
import {
  getList,
  deleteUser,
  addUser,
  updateUser,
  confirmImportExcel,
  confirmUpdate
} from "@/api/personnel/user";
import Edit from "../authorizationEdit";
import deptList from "../common/deptList";
import qs from "qs";
//导入ip地址
export default {
  data() {
    return {
      search: {
        username: "",
        departmentname: "",
        flag: ""
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableLoading: true,
      excelLoading: false,
      excelActionPath:
        process.env.VUE_APP_ITEM_NAME +
        "personnelUser/importExcel",
      dialogVisible: false,
      detailData: {},
      title: "",
      dialogFormVisible: false,
      formLabelWidth: "80px",
      form: {
        username: "",
        moneycard: "",
        departmentname: ""
      },
      addLoading: false,
      childDept: "",
      excelDialogVisible: false,
      excelPath: ""
    };
  },
  components: {
    Edit,
    deptList
  },
  mounted() {},
  created() {
    this.getList();
    console.log(this.form.id ? 'true' : 'false');
  },
  methods: {
    indexMethod(index) {
      return (this.page.pageNum - 1) * this.page.pageSize + index + 1;
    },
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
      this.getList();
    },
    //查询列表
    getList() {
      let params = this.page;
      params.username = this.search.username;
      if (this.$refs.searchSection) {
        params.departmentname = this.$refs.searchSection.value;
      } else {
        params.departmentname = "";
      }
      params.flag = this.search.flag;
      new Promise((response, reject) => {
        getList(qs.stringify(params))
          .then(response => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
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
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        "personnelUser/downloadTemplate";
    },
    //导出人员信息
    exportPerson() {
      let data = {
        username: this.search.username,
        departmentname: this.$refs.searchSection.value,
        flag: this.search.flag
      };
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        "personnelUser/exportExcel?info=" +
        JSON.stringify(data);
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
    //删除人员
    deleteUser(val) {
      this.$confirm("此操作将永久删除该人员, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            moneycard: val.moneycard
          };
          new Promise((response, reject) => {
            deleteUser(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
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
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    //打开添加人员
    addPerson() {
      this.dialogFormVisible = true;
      this.form = {
        id:"",
        username: "",
        moneycard: "",
        departmentname: ""
      };
    },
    //添加人员
    addUser() {
      let data = {
        id: this.form.id,
        username: this.form.username,
        moneycard: this.form.moneycard,
        departmentname: this.$refs.section.value,
        //dbtype: this.$store.state.user.user.dbtype
      };
      if (!data.username) {
        this.$message.warning("请填写用户名");
        return;
      }
      if (!data.moneycard) {
        this.$message.warning("请填写账号");
        return;
      }
      if (!data.departmentname) {
        this.$message.warning("请选择部门");
        return;
      }
      this.addLoading = true;
      if (data.id) {
        //修改
        new Promise((response, reject) => {
          updateUser(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
                this.getList();
              } else if (response.data.code == 1) {
                this.$confirm(response.data.msg, "提示", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                })
                  .then(() => {
                    let confirmData = {
                      moneycard:this.form.moneycard
                    }
                    new Promise((response, reject) => {
                      confirmUpdate(qs.stringify(data))
                        .then(response => {
                          if (response.data.code == 0) {
                            this.$message({
                              message: response.data.msg,
                              type: "success"
                            });
                            this.getList();
                          } else {
                            this.$message({
                              message: response.data.msg,
                              type: "error"
                            });
                          }
                          this.dialogFormVisible = false;
                          this.addLoading = false;
                          this.getList();
                        })
                        .catch(error => {
                          reject(error);
                        });
                    });
                  })
                  .catch(() => {});
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
              this.dialogFormVisible = false;
              this.addLoading = false;
              this.getList();
            })
            .catch(error => {
              reject(error);
            });
        });
      } else {
        //添加
        new Promise((response, reject) => {
          addUser(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
                this.getList();
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
              this.dialogFormVisible = false;
              this.addLoading = false;
              this.getList();
            })
            .catch(error => {
              reject(error);
            });
        });
      }
    },
    //编辑
    editUser(row) {
      this.dialogFormVisible = true;
      this.form = row;
      this.childDept = row.departmentname;
    },
    //打开上传excel
    uploadExcel() {
      this.excelDialogVisible = true;
    },
    //excel确认
    confirmExcel() {
      if (!this.excelPath) {
        this.$message.warning("请先上传");
        return;
      }
      let data = {
        savepath: this.excelPath
        // usercode: this.$store.state.user.user.usercode
      };
      new Promise((response, reject) => {
        confirmImportExcel(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
              this.getList();
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.excelDialogVisible = false;
          })
          .catch(error => {
            reject(error);
          });
      });
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
}
.monitor {
  color: #f00;
  font-size: 18px;
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
