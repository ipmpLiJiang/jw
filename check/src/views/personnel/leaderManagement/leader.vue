<template>
  <div>
    <h4 class="title">用户管理</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="6">
            <el-form-item label="姓名">
              <el-input
                placeholder="请输入姓名"
                v-model="search.scorringname"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="职员代码">
              <el-input
                placeholder="请输入职员代码"
                v-model="search.scorringcode"
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
                @click="addLeader"
              ><i class="el-icon-plus"></i>添加</el-button>
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
          prop="scorringname"
          label="用户姓名"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="scorringcode"
          label="职工代码"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="department"
          label="部门"
          show-overflow-tooltip
        >
        </el-table-column>
        <!-- <el-table-column
          prop="phone"
          label="手机号"
          show-overflow-tooltip
        >
        </el-table-column> -->
        <el-table-column
          prop="leaderphone"
          label="主任手机号"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="remarks"
          label="备注"
          show-overflow-tooltip
        >
        </el-table-column>
        <!-- <el-table-column
          prop="remarks2"
          label="备注2"
          show-overflow-tooltip
        >
        </el-table-column> -->
        <el-table-column
          fixed="right"
          label="操作"
          width="160"
        >
          <template slot-scope="scope">
            <el-button
              @click="reset(scope.row)"
              type="text"
              size="small"
            >重置密码</el-button>
            <el-button
              @click="editLeader(scope.row)"
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
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="35%"
      :before-close="handleClose"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <Edit
        ref="edit"
        :detailData="detailData"
      ></Edit>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="submit"
        >确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {
  getLeaderList,
  delLeaderList,
  addLeaderList,
  updateLeaderList
} from "@/api/personnel/user";
import { resetPassword } from "@/api/people/people";
import Edit from "./edit";
import deptList from "../common/deptList";
import qs from "qs";
export default {
  data() {
    return {
      search: {
        scorringname: "",
        scorringcode: ""
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableLoading: true,
      dialogVisible: false,
      detailData: {},
      title: ""
    };
  },
  components: {
    Edit,
    deptList
  },
  mounted() {},
  created() {
    this.getList();
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
      this.getList();
    },
    //查询列表
    getList() {
      let params = this.page;
      params.scorringname = this.search.scorringname;
      params.scorringcode = this.search.scorringcode;
      if (this.$refs.searchSection) {
        params.department = this.$refs.searchSection.value;
      } else {
        params.department = "";
      }
      return new Promise((response, reject) => {
        getLeaderList(qs.stringify(params))
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
    //删除人员
    deleteUser(val) {
      this.$confirm("此操作将永久删除该人员, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            ratercode: val.ratercode
          };
          new Promise((response, reject) => {
            delLeaderList(qs.stringify(data))
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
    //重置密码
    reset(val) {
      this.$confirm("此操作将该人员密码重置为whuh123456, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            usercode: val.usercode
          };
          new Promise((response, reject) => {
            resetPassword(qs.stringify(data))
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
    //关闭添加/修改弹出框
    handleClose(done) {
      done();
    },
    //打开添加框
    addLeader() {
      this.dialogVisible = true;
      this.detailData = {
        ratercode: "",
        department: "",
        phone: "",
        scorringcode: "",
        scorringname: "",
        leaderphone: "",
        remarks: "",
        remarks2: ""
      };
      this.title = "添加用户";
    },
    //打开编辑框
    editLeader(row) {
      this.dialogVisible = true;
      this.detailData = JSON.parse(JSON.stringify(row));
      this.title = "修改用户";
    },
    //提交
    submit() {
      if (!this.$refs.edit.form.scorringname) {
        this.$message.warning("请填写姓名");
        return;
      }
      if (!this.$refs.edit.form.department) {
        this.$message.warning("请填写科室");
        return;
      }
      if (!this.$refs.edit.form.scorringcode) {
        this.$message.warning("请填写职工代码");
        return;
      }
      if (!this.$refs.edit.form.leaderphone) {
        this.$message.warning("请填写主任手机号");
        return;
      }
      let data = this.$refs.edit.form;
      if (this.$refs.edit.form.ratercode) {
        //修改
        return new Promise((response, reject) => {
          updateLeaderList(qs.stringify(data))
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
                  type: "warning"
                });
              }
              this.dialogVisible = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      } else {
        //添加
        return new Promise((response, reject) => {
          addLeaderList(qs.stringify(data))
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
                  type: "warning"
                });
              }
              this.dialogVisible = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      }
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
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
