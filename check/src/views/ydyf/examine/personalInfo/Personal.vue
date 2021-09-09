<template>
  <div>
    <h4 class="title">人员信息</h4>

    <el-row class="search">
      <el-col>
        <el-form label-width="100px" show-overflow-tooltip="true">
          <el-col :span="4">
            <el-form-item label="姓名">
              <el-input
                placeholder="请输入员工姓名"
                v-model="search.userName"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="发薪号">
              <el-input
                placeholder="请选择发薪号"
                v-model="search.userId"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
        
             <el-col :span="4">
            <el-form-item label="选择科室">
              <el-select
                v-model="classify"
                placeholder="请选择科室"
                @change="personnel"
              >
                <el-option
                  v-for="item in personnelList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>


          <el-col :span="4">
            <el-form-item label="员工类型">
              <el-select
                v-model="classify"
                placeholder="请选择员工类型"
                @change="personnel"
              >
                <el-option
                  v-for="item in personnelList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
            <el-col :span="6">
            <el-form-item label="党支部">
              <el-select
                v-model="condition"
                placeholder="请选择"
                @change="currentSel"
              >
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24" class="edit-btn">
            <el-form-item>
              <el-button
                style="margin-left: -90px"
                type="primary"
                @click="searchList"
                ><i class="el-icon-search"></i>搜索
              </el-button>
            
           
        
              <!-- <el-button type="info" @click="reset">重置</el-button> -->
               <el-button type="warning" @click="reset">切换图表</el-button>
           
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
        @selection-change="changeFun"
      >
        <!-- <el-table-column type="selection" width="40" align="center" >
        </el-table-column> -->

        <!-- <el-table-column
          type="selection"
          width="40"
          align="center"
          :selectable="checkboxT"
          disabled="true"
        >
        </el-table-column>
                       -->

        <el-table-column type="index" width="50" :index="indexMethod">
        </el-table-column>
        <el-table-column
          prop="userName"
          label="用户姓名"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="userId"
          label="发薪号"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="userId"
          label="出生年月"
          show-overflow-tooltip
          align="center"
        >
        
        </el-table-column>
          <el-table-column
          prop="userId"
          label="政治面貌"
          show-overflow-tooltip
          align="center"
        >
        
        </el-table-column>
          <el-table-column
          prop="userId"
          label="文化程度"
          show-overflow-tooltip
          align="center"
        >
        
        </el-table-column>
        <el-table-column
          prop="userId"
          label="职称"
          show-overflow-tooltip
          align="center"
        >
        
        </el-table-column>
        <el-table-column
          prop="userId"
          label="聘用时间"
          show-overflow-tooltip
          align="center"
        >
        
        </el-table-column>
         <el-table-column
          prop="userId"
          label="考核年份"
          show-overflow-tooltip
          align="center"
        >
        
        </el-table-column>
         <el-table-column
          prop="userId"
          label="科室"
          show-overflow-tooltip
          align="center"
        >
        
        </el-table-column>
        <el-table-column
          prop="userId"
          label="所在党支部"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="userId"
          label="自我评分"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="userId"
          label="部门负责人评分"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="userId"
          label="党支部评分"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column label="员工类型" show-overflow-tooltip align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.personType == 0 ? "临床人员" : "非临床人员"
            }}</span
            >            
          </template>
        </el-table-column>
      
        <el-table-column
          prop="departmentname"
          label="考核成绩"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="160" align="center">
          <template slot-scope="scope">
            <!-- <el-button
              @click="editUser(scope.row)"
              type="text"
              size="small"
              v-if="scope.row.status == 0"
              >编辑</el-button
            > -->
            <el-button @click="deleteUser(scope.row)" type="text" size="small"
              >详情
            </el-button>
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

   
 
  </div>
</template>

<script>
import qs from "qs";
import {
  list,
  add,
  Delete,
  update,
  downloadExcel,
  readExcel,
  doSubmit,
  realList,
} from "../../api/addData/addData";
export default {
  props: {},
  data() {
    return {
      search: {
        userName: "",
        userId: "",
        status: "",
        personType: "",
      },
      options: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: "0",
          label: "未提交",
        },
        {
          value: "1",
          label: "已提交",
        },
      ],

      personnelList: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: 0,
          label: "临床人员",
        },
        {
          value: 1,
          label: "非临床人员",
        },
      ],
      value: "",
      condition: "", //选择状态
      classify: "", //选择非临床医务人员
      // submitShow:false, //提交按钮显示隐藏
      tableData: [],
      useridList: [],
      userId: "",
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      tableLoading: true,
      excelLoading: false,

      dialogVisible: false,
      detailData: {},
      title: "",
      dialogFormVisible: false,
      amendDialogFormVisible: false,
      formLabelWidth: "80px",
      form: {
        username: "",
        moneycard: "",
        departmentname: "",
      },
      modificationForm: {
        id: "",
        userName: "",
        userId: "",
        personType: "",
      },
      addLoading: false,
      childDept: "",
      excelDialogVisible: false,
      excelPath: "",
      fileList: [],
      file: {},
    };
  },
  computed: {},
  created() {
    // this.getList();
  },
  mounted() {},
  watch: {},
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
      let that = this;
      let params = this.page;
      params.userName = this.search.userName;
      params.userId = this.search.userId;
      params.status = this.search.status;
      params.personType = this.search.personType;

      // if (this.$refs.searchSection) {
      //   params.departmentname = this.$refs.searchSection.value;
      // } else {
      //   params.departmentname = "";
      // }

      new Promise((response, reject) => {
        list(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.tableLoading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //下载模板
  
  
   
 
    //删除人员
    deleteUser(val) {
      this.$confirm("此操作将永久删除该人员, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let data = {
            id: val.id,
          };

          new Promise((response, reject) => {
            Delete(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success",
                  });
                  this.getList();
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {
          // this.$message({
          //   type: "info",
          //   message: "已取消删除",
          // });
        });
    },
    //打开添加人员
    addPerson() {
      this.dialogFormVisible = true;
      this.modificationForm = {
        id: "",
        userName: "",
        userId: "",
        personType: "",
      };
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    //添加人员
    addUser() {
      let data = {
        userName: this.modificationForm.username,
        userId: this.modificationForm.moneycard,
      };
      if (!data.userName) {
        this.$message.warning("请填写用户名");
        return;
      }
      if (!data.userId) {
        this.$message.warning("请填写发薪号");
        return;
      }

      this.addLoading = true;
      if (data.id) {
        //修改
        new Promise((response, reject) => {
          add(qs.stringify(data))
            .then((response) => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg[0],
                  type: "success",
                });
                this.getList();
              } else if (response.data.code == 1) {
                this.$confirm(response.data.msg, "提示", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning",
                })
                  .then(() => {
                    let confirmData = {
                      moneycard: this.form.moneycard,
                    };
                    new Promise((response, reject) => {
                      confirmUpdate(qs.stringify(data))
                        .then((response) => {
                          if (response.data.code == 0) {
                            this.$message({
                              message: response.data.msg,
                              type: "success",
                            });
                            this.getList();
                          } else {
                            this.$message({
                              message: response.data.msg,
                              type: "error",
                            });
                          }
                          this.dialogFormVisible = false;
                          this.addLoading = false;
                          this.getList();
                        })
                        .catch((error) => {
                          reject(error);
                        });
                    });
                  })
                  .catch(() => {});
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error",
                });
              }
              this.dialogFormVisible = false;
              this.addLoading = false;
              this.getList();
            })
            .catch((error) => {
              reject(error);
            });
        });
      } else {
        //添加
        new Promise((response, reject) => {
          add(qs.stringify(data))
            .then((response) => {
              if (response.data.code == 2) {
                this.$message({
                  message: response.data.msg,
                  type: "error",
                });
                this.getList();
              }
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success",
                });
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error",
                });
              }
              this.dialogFormVisible = false;
              this.addLoading = false;
              this.getList();
            })
            .catch((error) => {
              reject(error);
            });
        });
      }
    },
    //编辑
    // editUser(row) {
    //   this.modificationForm.personType = row.personType;
    //   this.modificationForm.status = row.status;
    //   this.modificationForm.userName = row.userName;
    //   this.modificationForm.userId = row.userId;
    //   this.modificationForm.id = row.id;
    //   this.amendDialogFormVisible = true;
    // },
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
        file: this.excelPath,
        // usercode: this.$store.state.user.user.usercode
      };
      new Promise((response, reject) => {
        readExcel(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
              this.getList();
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.excelDialogVisible = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    handleRemove(file, fileList) {},
    handlePreview(file) {},
    beforeUpload(file) {},
    httpRequest(param) {
      let formData = new FormData();
      formData.append("file", param.file);
      new Promise((response, reject) => {
        readExcel(formData)
          .then((response) => {
            if (response.data.code == 0) {
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.excelDialogVisible = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    onUploadChange(file) {
      this.excelPath = file;
    },
    //提交资料

    submit() {
      let ids = {
        ids: this.userId,
      };
      if (this.userId) {
        this.$confirm("请再次提交确认信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          new Promise((response, reject) => {
            doSubmit(qs.stringify(ids))
              .then((response) => {
                if (response.data.code == 0) {
                  this.getList();
                  this.$message({
                    message: response.data.msg[0],
                    type: "success",
                  });
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
              })
              .catch((error) => {
                reject(error);
              });
          });
        });
      }else{
         this.$message.warning("请先勾选您要提交的选项，再进行提交！");
      }
     
    },

    //获取选中数据
    changeFun(val) {
      // if(val.length > 0){

      //   this.submitShow=true
      // }else{
      //   this.submitShow=false
      // }

      let that = this;
      let list = val;
      this.useridList.length = 0;
      this.userId = "";
      list.forEach((item) => {
        that.useridList.push(item.id);
      });

      that.userId = that.useridList.toString();
    },
    handleSuccess(response) {
      this.$refs.upload.clearFiles();
      if (response.code == 0) {
        this.$message.success(response.msg);
        this.getList();
      } else {
        this.common.importExcelErrorMsg(this, response);
      }

      this.excelDialogVisible = false;
    },
    //上传失败
    fail(err, file, fileList) {
      this.excelDialogVisible=false;
       this.$confirm("上传失败，请重新上传", "提示", {
                confirmButtonText: "确定",
                type: "warning",
              })
    },
    //获取下拉选择框数据
    currentSel(selVal) {
      this.search.status = selVal;
    },
    //获取员工类型数据
    personnel(val) {
      this.search.personType = val;
    },
    //编辑下拉框
    modification(val) {
      this.modificationForm.personType = val;
    },
    //编辑确认
    redact() {
      let data = this.modificationForm;
      new Promise((response, reject) => {
        update(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              this.amendDialogFormVisible = false;
              this.getList();
            } else if (response.data.code == 1) {
              // this.amendDialogFormVisible = false
              this.$confirm(response.data.msg, "提示", {
                confirmButtonText: "确定",
                type: "warning",
              })
                .then(() => {
                  let confirmData = {
                    moneycard: this.form.moneycard,
                  };
                  new Promise((response, reject) => {
                    confirmUpdate(qs.stringify(data))
                      .then((response) => {
                        if (response.data.code == 0) {
                          this.$message({
                            message: response.data.msg,
                            type: "success",
                          });
                          this.getList();
                        } else {
                          this.$message({
                            message: response.data.msg,
                            type: "error",
                          });
                        }

                        this.getList();
                      })
                      .catch((error) => {
                        reject(error);
                      });
                  });
                })
                .catch(() => {});
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.dialogFormVisible = false;
            this.addLoading = false;
            // this.getList();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //重置按钮
    reset() {
      (this.search.userName = ""),
        (this.search.userId = ""),
        (this.search.status = ""),
        (this.search.personType = ""),
        (this.condition = ""),
        (this.classify = "");
      let params = this.page;
      params.userName = this.search.userName;
      params.userId = this.search.userId;
      params.status = this.search.status;
      params.personType = this.search.personType;
      new Promise((response, reject) => {
        list(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
              this.$message({
                message: response.data.msg,
                type: "success",
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.tableLoading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // //判断多选框

    checkboxT(row, index) {
      if (row.status == 1) {
        return false;
      } else {
        return true;
      }
    },
  },
  components: {},
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