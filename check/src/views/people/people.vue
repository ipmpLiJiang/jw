<template>
  <div>
    <h4 class="title"><router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>人员信息录入</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
            <el-form-item label="所在岗位">
              <PostList
                @childSelectDepartment="getSelectStation"
                :selectedOptions="fullstationcode"
              ></PostList>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="员工姓名">
              <el-input
                placeholder="请输入员工姓名"
                v-model="search.username"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="角色权限">
              <el-select
                v-model="search.rolecode"
                clearable
                placeholder="请选择"
              >
              <el-option
                v-for="item in roleOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
            </el-form-item>
          </el-col>
          <el-button
            type="primary"
            @click="searchList"
          >搜索</el-button>
          <el-button
            type="primary"
            @click="openAdd"
          >新增员工</el-button>
        </el-form>
      </el-col>
    </el-row>
    <el-row class="content">
      <el-table
        size="small"
        :data="tableData"
        border
        style="width: 100%"
        v-loading="tableLoading"
      >
        <el-table-column
          label="员工姓名"
          prop="username"
        >
        </el-table-column>
        <el-table-column label="性别">
          <template slot-scope="scope">
            <span v-if="scope.row.sex == 1">男</span>
            <span v-else-if="scope.row.sex == 2">女</span>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column
          label="账号"
          prop="moneycard"
        >
        </el-table-column>
        <el-table-column
          label="手机号码"
          prop="mobile"
        >
        </el-table-column>
        <el-table-column label="用户状态">
          <template slot-scope="scope">
            <span v-if="scope.row.userstate == 0">停用</span>
            <span v-else-if="scope.row.userstate == 1">启用</span>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column
          label="所属部门"
          prop="departmentname"
        >
        </el-table-column>
        <el-table-column
          label="所属岗位"
          prop="stationname"
        >
        </el-table-column>
        <el-table-column
          label="角色权限"
          prop="rolename"
        >
        </el-table-column>
        <el-table-column
          label="党内身份"
          prop="dbbkName"
        >
        </el-table-column>
        <el-table-column
          label="所属支部"
          prop="branchname"
        >
        </el-table-column>
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
    <!-- 添加人员 -->
    <AddPeople
      :parentForms="forms"
      :dialogVisible="dialogVisible"
      :stationcode="stationcode"
      :branchcode="branchcode"
      @childClose="childClose"
      @childGetList="getList"
    ></AddPeople>
  </div>
</template>

<script>
import PostList from "../common/postList";
import { getList, deleteUser,resetPassword } from "@/api/people/people";
import AddPeople from "./addPeople";
import qs from "qs";
export default {
  data() {
    return {
      forms: {
        username: "",
        sex: "",
        email: "",
        mobile: "",
        nation: "",
        political: "",
        education: "",
        userstate: 1,
        rolecode: "300",
        dbbk: ''
      },
      search: {
        stationcode: "",
        username: "",
        rolecode: "",
      },
      tableData: [],
      stationcode: [""],
      branchcode: [""],
      roleOption: [{
          value: "100",
          label: "组织部"
        },
        {
          value: "150",
          label: "打分用户"
        },
        {
          value: "200",
          label: "部门长"
        },
        {
          value: "300",
          label: "普通用户"
        }],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      dialogVisible: false,
      fullstationcode: [""],
      tableLoading: true
    };
  },
  components: {
    AddPeople,
    PostList
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
    //关闭列表
    childClose(val) {
      this.dialogVisible = val;
    },
    //搜索
    searchList() {
      this.into();
      this.getList();
    },
    //查询列表
    getList() {
      let params = this.page;
      if (this.search.stationcode.length > 0) {
        params.stationcode = this.search.stationcode[0];
      } else {
        params.stationcode = "";
      }
      if (this.search.rolecode != "") {
        params.rolecode = this.search.rolecode
      } else {
        params.rolecode = ""
      }
      params.username = this.search.username;
      
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
    //打开添加人员
    openAdd() {
      this.forms = {
        username: "",
        sex: "",
        email: "",
        mobile: "",
        nation: "",
        political: "",
        education: "",
        userstate: "1",
        rolecode: "",
        dbbk: ''
      };
      if(this.$store.state.user.user.rolecode != 400){
        this.forms.rolecode = "300";
      }
      this.stationcode = [""];
      this.branchcode = [""];
      this.dialogVisible = true;
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
            usercode: val.usercode
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
    //重置密码
    reset(val) {
      this.$confirm("此操作将该人员密码重置为jw123456, 是否继续?", "提示", {
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
    //编辑人员
    editUser(row) {
      this.forms = row;
      if (this.forms.fullstationcode) {
        this.stationcode = this.forms.fullstationcode.split(",");
      }else{
        this.stationcode = [];
      }
      if (this.forms.fullbranchcode) {
        this.branchcode = this.forms.fullbranchcode.split(",");
      }else{
        this.branchcode = [];
      }
      this.dialogVisible = true;
    },
    //获取岗位选择
    getSelectStation(data, row) {
      this.search.stationcode = [];
      this.search.stationcode.push(data);
      this.fullstationcode = row;
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
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-form-item {
    margin: 0px;
  }
  .el-button {
    margin-left: 10px;
  }
}
.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 0px 20px 20px 20px;
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
</style>
