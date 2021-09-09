<template>
  <div>
    <!-- <h4 class="title">部门长授权</h4> -->
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="8">
            <el-form-item label="授权人姓名">
              <el-input
                placeholder="请输入姓名"
                v-model="search.deptusername"
                clearable
                @keyup.enter.native="getList"
                style="width:220px;"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="被授权人姓名">
              <el-input
                placeholder="请输入发薪号"
                v-model="search.agentusername"
                clearable
                @keyup.enter.native="getList"
                style="width:220px;"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="授权状态">
              <el-select
                v-model="search.flag"
                filterable
                placeholder="请选择"
                clearable
                style="width:220px;"
              >
                <el-option
                  v-for="item in flagOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" style="margin-top:10px;">
            <el-form-item label="用户归属">
              <el-select
                v-model="search.ispersonnel"
                filterable
                placeholder="请选择"
                clearable
                style="width:220px;"
              >
                <el-option
                  v-for="item in ispersonnelOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col
            :span="8"
            style="margin-top:10px;"
          >
            <el-form-item label="代理开始时间">
              <el-date-picker
                v-model="search.starttime"
                type="date"
                placeholder="选择代理开始时间"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col
            :span="8"
            style="margin-top:10px;"
          >
            <el-form-item label="代理结束时间">
              <el-date-picker
                v-model="search.endtime"
                type="date"
                placeholder="选择代理结束时间"
                value-format="yyyy-MM-dd"
              >
              </el-date-picker>
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
          prop="deptusername"
          label="授权人"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="deptuser"
          label="授权人发薪号"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="agentusername"
          label="被授权人"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="agent"
          label="被授权人发薪号"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="starttime"
          label="代理开始时间"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="endtime"
          label="代理结束时间"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="用户归属"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-if="scope.row.ispersonnel == '1'">组织部</span>
            <span v-if="scope.row.ispersonnel == '2'">人事处</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="flag"
          label="代理状态"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span
              v-if="scope.row.flag == 1"
              style="color:#E6A23C"
            >已授权</span>
            <span
              v-else-if="scope.row.flag == 2"
              style="color:#F56C6C"
            >代理中</span>
            <span
              v-else-if="scope.row.flag == 4"
              style="color:#409EFF"
            >已完成</span>
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
import { getHistoryList } from "@/api/personnel/authorization";
import Edit from "./authorizationEdit";
import qs from "qs";
export default {
  data() {
    return {
      search: {
        deptusername: "",
        agentusername: "",
        flag: "",
        starttime: "",
        endtime: "",
        ispersonnel:""
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
      title: "",
      activeName: "first",
      flagOptions: [
        {
          value: "1",
          label: "已授权"
        },
        {
          value: "2",
          label: "代理中"
        },
        {
          value: "4",
          label: "已完成"
        }
      ],
      ispersonnelOptions: [
        {
          value: "1",
          label: "组织部"
        },
        {
          value: "2",
          label: "人事处"
        }
      ]
    };
  },
  components: {
    Edit
  },
  mounted() {},
  created() {
    this.getList();
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
      params.loginmoneycard = this.$store.state.user.user.moneycard;
      params.deptusername = this.search.deptusername;
      params.agentusername = this.search.agentusername;
      params.starttime = this.search.starttime;
      params.endtime = this.search.endtime;
      params.flag = this.search.flag;
      params.ispersonnel = this.search.ispersonnel;
      return new Promise((response, reject) => {
        getHistoryList(qs.stringify(params))
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
    //查询历史列表
    getHistory() {},
    //关闭添加/修改弹出框
    handleClose(done) {
      this.dialogVisible = false;
      this.getList();
    },
    //打开添加框
    add(row) {
      this.dialogVisible = true;
      this.detailData = row;
      this.title = "代理部长权限授权";
    },
    //打开编辑框
    openEdit(row) {
      this.dialogVisible = true;
      this.detailData = row;
      this.title = "代理部长权限授权";
    },
    //提交
    submit() {
      if (!this.$refs.edit.form.starttime) {
        this.$message.warning("请填写开始时间");
        return;
      }
      if (!this.$refs.edit.form.endtime) {
        this.$message.warning("请填写结束时间");
        return;
      }
      let data = {
        agent: this.$refs.edit.form.moneycard,
        deptuser: this.$store.state.user.user.moneycard,
        starttime: this.$refs.edit.form.starttime,
        endtime: this.$refs.edit.form.endtime
      };
      if (this.$refs.edit.form.flag != 3) {
        //修改
        return new Promise((response, reject) => {
          updateAuthorization(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "warning"
                });
              }
              this.getList();
              this.dialogVisible = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      } else {
        //添加
        return new Promise((response, reject) => {
          openAuthorization(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "warning"
                });
              }
              this.getList();
              this.dialogVisible = false;
            })
            .catch(error => {
              reject(error);
            });
        });
      }
    },
    //取消授权人员
    del(row) {
      this.$confirm("此操作将取消该人员代理部长功能, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            agent: row.moneycard,
            deptuser: this.$store.state.user.user.moneycard
          };
          new Promise((response, reject) => {
            closeAuthorization(qs.stringify(data))
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
