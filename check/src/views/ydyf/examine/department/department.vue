<template>
  <div>
    <h4 class="title">科室管理</h4>

    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
            <el-form-item label="姓名">
              <el-input
                placeholder="请填写姓名"
                v-model="search.queryName"
                clearable
              >
              </el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="5">
            <el-form-item label="发薪号">
              <el-input
                placeholder="请填写发薪号"
                v-model="search.userId"
                clearable
              >
              </el-input>
            </el-form-item>
          </el-col> -->

          <el-col :span="5">
            <el-form-item label="科室">
              <el-input
                placeholder="请输入科室"
                v-model="search.departmentName"
                clearable
              >
              </el-input>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item
              label="党支部"
              label-width="100px"
            >
              <el-cascader
                v-model="search.branchId"
                :options="secretaryOptions"
                placeholder="请选择党支部"
                @change="handleChange"
                style="width: 100%"
                clearable
              ></el-cascader>
            </el-form-item>
          </el-col>

          <el-col
            :span="24"
            class="edit-btn"
          >
            <el-form-item>
              <el-button
                style="margin-left: -90px"
                type="primary"
                @click="searchList"
              ><i class="el-icon-search"></i>搜索
              </el-button>
              <el-button
                type="primary"
                @click="Add"
              >添加</el-button>
              <!-- <el-button
                type="info"
                @click="reset"
              >重置</el-button> -->
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
        element-loading-text="拼命加载中"
      >
                      

        <el-table-column
          type="index"
          width="50"
          :index="indexMethod"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="directorName"
          label="科室主任"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="directorUserId"
          label="科室主任发薪号"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="partyCommitteesName"
          label="党委名称"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>

        <el-table-column
          prop="generalBranchName"
          label="党总支名称"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="branchName"
          label="党支部名称"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <!-- <el-table-column
          prop="branchName"
          label="党支部书记"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column> -->
        <el-table-column
          prop="departmentName"
          label="科室名称"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        
        <!-- <el-table-column
          prop="secretaryName"
          label="科室书记"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column> -->

        <!-- <el-table-column label="员工类型" show-overflow-tooltip align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.personType == 0 ? "临床人员" : "非临床人员"
            }}</span
            >            
          </template>
        </el-table-column> -->

        <el-table-column
          fixed="right"
          label="操作"
          width="160"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="editUser(scope.row)"
              type="primary"
              size="small"
            >编辑</el-button>
            <el-button
              @click="deleteUser(scope.row)"
              type="danger"
              size="small"
            >删除
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

    <!-- 添加人员dialog -->
    <el-dialog
      title="添加科室信息"
      :visible.sync="dialogFormVisible"
      width="30%"
    >
      <el-form :model="addmodificationForm">
        <el-form-item
          label="科室名称"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="addmodificationForm.departmentName"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item
          label="科室主任姓名"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="addmodificationForm.directorName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="科室主任发薪号"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="addmodificationForm.directorUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item
          label="科室书记"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="addmodificationForm.secretaryName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="科室书记发薪号"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="addmodificationForm.secretaryUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item> -->
        <el-form-item
          label="党支部名称"
          :label-width="formLabelWidth"
        >
          <el-cascader
            v-model="addmodificationForm.branchId"
            :options="secretaryOptions"
            placeholder="请选择党支部"
            @change="AddhandleChange"
            :props="{ checkStrictly: true }"
            style="width: 100%"
          ></el-cascader>
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

    <!-- 编辑科室 -->
    <el-dialog
      title="编辑人员"
      :visible.sync="amendDialogFormVisible"
      width="30%"
    >
      <el-form :model="modificationForm">
        <el-form-item
          label="科室名称"
          :label-width="formLabelWidth"
        >
          <el-input v-model="modificationForm.departmentName"></el-input>
        </el-form-item>

        <el-form-item
          label="科室主任姓名"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="modificationForm.directorName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="科室主任发薪号"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="modificationForm.directorUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item
          label="科室书记"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="modificationForm.secretaryName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="科室书记发薪号"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="modificationForm.secretaryUserId"
            autocomplete="off"
          ></el-input>
        </el-form-item> -->

        <el-form-item
          label="党支部名称"
          :label-width="formLabelWidth"
        >
          <el-cascader
            v-model="modificationForm.branchId"
            :options="secretaryOptions"
            placeholder="请选择党支部"
            @change="updatehandleChange"
            style="width: 100%"
          ></el-cascader>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="amendDialogFormVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="redact"
          :loading="addLoading"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import qs from "qs";
import {
  getList,
  Add,
  tree,
  Delete,
  update,
  getDeptDetail,
} from "../../../ydyf/api/department/department";

export default {
  props: {},
  data() {
    return {
      search: {
        queryName: "",
        branchId: [], //党支部id
        departmentName: "", //科室名字
      },
      secretaryOptions: [],

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
          value: 0,
          label: "临床人员",
        },
        {
          value: 1,
          label: "非临床人员",
        },
      ],
      value: "",

      // submitShow:false, //提交按钮显示隐藏
      tableData: [],
      useridList: [],
      secretaryValue: [], //书记级联选择
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
      dialogFormVisible: false, //添加科室信息显示框
      amendDialogFormVisible: false, //编辑科室显示框
      formLabelWidth: "120px",
      form: {
        username: "",
        moneycard: "",
        departmentname: "",
      },
      modificationForm: {
        branchId: [], //科室名称
        departmentName: "", //科室名称
        directorName: "", //科室主任id
        directorUserId: "", //科室主任姓名
        secretaryUserId: "", //科室书记姓名
        secretaryName: "",
      },
      addmodificationForm: {}, //添加科室表单
      addLoading: false,
      childDept: "",
      excelDialogVisible: false,
    };
  },
  computed: {},
  created() {
    this.into();
    // this.EthicsUser List();
    this.getList();
    this.EthicsUserList();

  },
  mounted() {
    // this.getList();
  },
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
    //党支部书记列表
    EthicsUserList() {
      let that = this;
      let params = this.page;
      params.userName = this.search.userName;
      params.userId = this.search.userId;
      params.status = this.search.status;
      params.personType = this.search.personType;
      new Promise((response, reject) => {
        tree(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.secretaryOptions = response.data.data.children;
              
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
    },

    //列表页查询
    getList() {
      // let that = this;
      let params = this.page;
      params.queryName = this.search.queryName;
      params.branchId = this.search.branchId;
      params.departmentName = this.search.departmentName;
      // params.userId = this.search.userId;
      // params.personType = this.search.personType;
      // params.relationsId=this.search.secretaryValue
      // (params.roleCode = "300"),
      this.tableLoading=true;
      
      new Promise((response, reject) => {
        getList(qs.stringify(params))
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

    submitUpload() {
      this.$refs.upload.submit();
    },
    //添加人员
    addUser() {
      // let data = {
      //   userName: this.modificationForm.username,
      //   userId: this.modificationForm.moneycard,
      // };
      let data = this.addmodificationForm;
      if (!data.departmentName) {
        this.$message.warning("请填写科室名称");
        return;
      }
      if (!data.directorName) {
        this.$message.warning("请填写科室主任姓名");
        return;
      }
      if (!data.directorUserId) {
        this.$message.warning("请填写科室主任发薪号");
        return;
      }

      // if (!data.secretaryName) {
      //   this.$message.warning("请填写科室书记");
      //   return;
      // }
      // if (!data.secretaryUserId) {
      //   this.$message.warning("请填写科室书记发薪号");
      //   return;
      // }
      if (!data.branchId.length) {
        this.$message.warning("请选择党支部");
        return;
      }

      //添加
      this.addLoading = true;
      new Promise((response, reject) => {
        Add(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              this.addLoading = false;
              this.dialogFormVisible = false;
              this.getList();
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
              this.addLoading = false;
            }
          })
          .catch((error) => {
            reject(error);
            this.addLoading = false;
          });
      });
    },
    //编辑
    editUser(row) {
      // this.modificationForm={}
      new Promise((response, reject) => {
        getDeptDetail(row.id)
          .then((response) => {
            if (response.data.code == 0) {
              this.amendDialogFormVisible = true;
              this.modificationForm.branchId = response.data.data.partyArray;
              this.modificationForm.departmentName = response.data.data.departmentName;
              this.modificationForm.directorName = response.data.data.directorName;
              this.modificationForm.directorUserId = response.data.data.directorUserId;
              this.modificationForm.secretaryUserId = response.data.data.secretaryUserId;
              this.modificationForm.secretaryName = response.data.data.secretaryName;
              this.modificationForm.id = response.data.data.id;
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
      } else {
        this.$message.warning("请先勾选您要提交的选项，再进行提交！");
      }
    },

    handleChange(val) {
    
      if (val.length > 0) {
        this.search.branchId = val[val.length - 1];
      }
    },
    //新增科室表单
    AddhandleChange(val) {
   
      if (val.length > 0) {
        this.addmodificationForm.branchId =val[val.length - 1]
        
      }
    
    },
    //编辑党支部名单
    updatehandleChange(val) {
      if (val.length > 0) {
        this.modificationForm.branchId = val;
      }
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
      
      let deptId = data.branchId[data.branchId.length - 1];
      delete data.branchId
      data.branchId = deptId;
      this.addLoading = true;
      new Promise((response, reject) => {
        update(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              this.amendDialogFormVisible = false;
              this.addLoading = false;
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
                          this.addLoading = false;
                          this.getList();
                        } else {
                          this.$message({
                            message: response.data.msg,
                            type: "error",
                          });
                          this.addLoading = false;
                        }

                        this.getList();
                        this.addLoading = false;
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
              this.addLoading = false;
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
      (this.search.queryName = ""),
        (this.search.departmentName = ""),
        (this.search.branchId = []);

      let params = this.page;
      params.queryName = this.search.queryName;
      params.departmentName = this.search.branchId;
      params.branchId = [];
      new Promise((response, reject) => {
        getList(qs.stringify(params))
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
    //添加人员
    Add() {
      this.addmodificationForm = {};
      this.dialogFormVisible = true;
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