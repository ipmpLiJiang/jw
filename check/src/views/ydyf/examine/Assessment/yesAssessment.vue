<template>
  <div>
    <h4 class="title">已考核人员信息</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
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
          <el-col :span="5">
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

          <el-col :span="6">
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
      <el-radio-group
        class="role-type"
        v-model="roleType"
        @change="changeRoleType"
        v-if="$store.state.user.user.medicalEthicsRoleList.indexOf('400')!= -1 && $store.state.user.user.medicalEthicsRoleList.indexOf('300')!= -1"
      >
        <el-radio :label="4">科室主任已考核列表</el-radio>
        <el-radio :label="7">打分书记已考核列表</el-radio>
      </el-radio-group>
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="tableLoading"
        @selection-change="changeFun"
      >
        <!-- <el-table-column type="selection" width="40" align="center" >
        </el-table-column> -->

                      

        <el-table-column
          type="index"
          width="50"
          :index="indexMethod"
        >
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
          label="员工类型"
          show-overflow-tooltip
          align="center"
        >
          <template slot-scope="scope">
            <span>{{
              scope.row.personType == 0 ? "临床人员" : "非临床人员"
            }}</span>            
          </template>
        </el-table-column>
        <el-table-column
          prop="partyCommitteesName"
          label="所属党委"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="generalBranchName"
          label="所属党总支"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="branchName"
          label="所属党支部"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          label="考核状态"
          show-overflow-tooltip
          align="center"
          width="160"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.personType == 1">
              <span
                v-if="scope.row.step == 0"
                style="color:#909399"
              >未填写完成</span>
              <span
                v-if="scope.row.step==1"
                style="color:#E6A23C"
              >用户完成自我评价<br/>等待主任考核</span>
              <span
                v-if="scope.row.step==2"
                style="color:#E6A23C"
              >主任已经完成考核<br/>等待书记考核</span>
              <span
                v-if="scope.row.step==3"
                style="color:#409EFF"
              >书记已经完成考核</span>
              <span
                v-if="scope.row.step==6"
                style="color:#409EFF"
              >已结束</span>
            </span>
            <span v-if="scope.row.personType == 0">
              <span
                v-if="scope.row.step == 0"
                style="color:#909399"
              >未填写完成</span>
              <span
                v-else-if="scope.row.step<=3"
                style="color:#E6A23C"
              >用户完成自我评价<br/>等待主任考核</span>
              <span
                v-else-if="scope.row.step==4"
                style="color:#E6A23C"
              >主任已经完成考核<br/>等待书记考核</span>
              <span
                v-else-if="scope.row.step==5"
                style="color:#409EFF"
              >书记已经完成考核</span>
              <span
                v-if="scope.row.step==6"
                style="color:#409EFF"
              >已结束</span>
            </span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.personType == 1">
              <!-- 主任权限 -->
              <template v-if="roleType == 4">
                <el-button
                  @click="editUser(scope.row)"
                  type="text"
                  size="small"
                  v-if="scope.row.step == 1 || scope.row.step == 2"
                >考核</el-button>
                <span
                  v-else
                  style="color:#909399"
                >暂无操作</span>
              </template>
              <template v-if="roleType == 7">
                <el-button
                  @click="editUser(scope.row)"
                  type="text"
                  size="small"
                  v-if="scope.row.step == 2 || scope.row.step == 3"
                >考核</el-button>
                <span
                  v-else
                  style="color:#909399"
                >暂无操作</span>
              </template>

            </span>
            <span v-if="scope.row.personType == 0">
              <!-- 主任权限 -->
              <template v-if="roleType == 4">
                <el-button
                  @click="editUser(scope.row)"
                  type="text"
                  size="small"
                  v-if="scope.row.step == 3 || scope.row.step == 4"
                >考核</el-button>
                <span
                  v-else
                  style="color:#909399"
                >暂无操作</span>
              </template>
              <!-- 书记权限 -->
              <template v-if="roleType == 7">
                <el-button
                  @click="editUser(scope.row)"
                  type="text"
                  size="small"
                  v-if="scope.row.step == 4 || scope.row.step == 5"
                >考核</el-button>
                <span
                  v-else
                  style="color:#909399"
                >暂无操作</span>
              </template>
            </span>
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
import { notFilled } from "../../api/Form/index";

// import {
//   getList,
//   deleteUser,
//   addUser,
//   updateUser,
//   confirmImportExcel,
//   confirmUpdate,
// } from "@/api/personnel/user";
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
      condition: "", //选择状态
      classify: "", //选择非临床医务人员
      // submitShow:false, //提交按钮显示隐藏
      roleType: 4, //选择考核单位
      tableData: [],
      roleList: [],
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
      roleDialogVisible: false,
      roleType: 4,
      tempRow: {},
      type:1,
      multi:false,
    };
  },
  computed: {},
  created() {
    //判断用户是否多角色
    if(this.$store.state.user.user.medicalEthicsRoleList.indexOf('300')!= -1 && this.$store.state.user.user.medicalEthicsRoleList.indexOf('400')!= -1){
      this.multi = true;
    }else if(this.$store.state.user.user.medicalEthicsRoleList.indexOf('300')!= -1){
      this.roleType = 4;
      this.multi = false;
    }else if(this.$store.state.user.user.medicalEthicsRoleList.indexOf('400')!= -1){
      this.roleType = 7;
      this.multi = false;
    }
    this.getList();
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
      // let that = this;
      let params = this.page;
      (params.roleType = this.roleType),
        (params.userName = this.search.userName);
      params.userId = this.search.userId;
      params.status = this.search.status;
      params.personType = this.search.personType;
      (params.type = "1"),
        (params.roleType = this.roleType),
        new Promise((response, reject) => {
          notFilled(qs.stringify(params))
            .then((response) => {
              if (response.data.code == 0) {
                this.tableData = response.data.data;

                this.total = response.data.totalPages;
                this.dialogVisible = false;
              } else {
                // this.$message({
                //   message: response.data.msg,
                //   type: "error",
                // });
              }
              this.tableLoading = false;
            })
            .catch((error) => {
              reject(error);
            });
        });
    },
    //判断是否打开用户角色选择框  暂时弃用
    isOpenEditUser(row) {
      let roleList = this.$store.state.user.user.medicalEthicsRoleList;
      this.tempRow = row;
      //用户多权限，让用户自己选择
      if (
        this.roleList.indexOf("101") != -1 &&
        this.roleList.indexOf("300") != -1
      ) {
        this.roleDialogVisible = true;
      } else {
        this.tempRow.userId = row.userId;
        this.editUser();
      }
    },
    //编辑用户
    editUser(item) {
      if (item.personType == 1) {
        //非临床
        if (this.roleType == 4) {
          this.$router.push({
            path: "/ydyf/ydyfpcDirectorNoclinic",
            query: {
              u_id: item.userId,
              roleType: this.roleType,
              type: this.type,
            },
          });
        } else if (this.roleType == 7) {
          this.$router.push({
            path: "/ydyf/ydyfpcSecretaryNoclinic",
            query: {
              u_id: item.userId,
              roleType: this.roleType,
              type: this.type,
            },
          });
        }
      } else {
        //临床
        if (this.roleType == 4) {
          this.$router.push({
            path: "/ydyf/ydyfpcDirectorClinic",
            query: {
              u_id: item.userId,
              roleType: this.roleType,
              type: this.type,
            },
          });
        } else if (this.roleType == 7) {
          this.$router.push({
            path: "/ydyf/ydyfpcSecretaryClinic",
            query: {
              u_id: item.userId,
              roleType: this.roleType,
              type: this.type,
            },
          });
        }
      }
    },
    //获取选中数据
    changeFun(val) {
      let that = this;
      let list = val;
      this.useridList.length = 0;
      this.userId = "";
      list.forEach((item) => {
        that.useridList.push(item.id);
      });

      that.userId = that.useridList.toString();
    },
    //获取员工类型数据
    personnel(val) {
      this.search.personType = val;
    },
    //选择考核单位
    unitpersonnel(val) {
      this.roleType = val;
    },
    handleClose() {},
    //改变考核身份
    changeRoleType(val) {
      this.page.pageNum = 1;
      this.page.pageSize = 10;
      this.roleType = val;
      this.getList();
    },
  },
  components: {},
};
</script>

<style lang="scss" scoped>
h4 {
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eaeaea;
  padding: 0px 10px;
  font-weight: 600;
  color: #424242;
  background: #fff;
}
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
.role-type {
  display: block;
  text-align: center;
  padding-bottom: 15px;
}
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>