<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>评分关系管理
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
            <el-form-item label="所属岗位">
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
            type="warning"
            @click="exportExcel"
          ><i class="icon iconfont icon-daochu-tianchong"></i>导出excel</el-button>

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
          label="员工姓名"
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
          label="所属部门"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="stationname"
          label="所属岗位"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="A类评分人"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-button
              @click="setGradeUser(scope.row,'A')"
              type="text"
              size="small"
            >编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="B类评分人"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-button
              @click="setGradeUser(scope.row,'B')"
              type="text"
              size="small"
            >编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="C类评分人"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-button
              @click="setGradeUser(scope.row,'C')"
              type="text"
              size="small"
            >编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="D类评分人"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-button
              @click="gradeUser(scope.row,'D')"
              type="text"
              size="small"
            >编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="E类评分人"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-button
              @click="gradeUser(scope.row,'E')"
              type="text"
              size="small"
            >编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="F类评分人"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-button
              @click="gradeUser(scope.row,'F')"
              type="text"
              size="small"
            >编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="评分系数"
          width="200"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-button
              v-show="dbtype=='1'?true:false"
              @click="setWeight(scope.row)"
              type="text"
              size="small"
            >A:{{scope.row.aratio ? scope.row.aratio : "0"}} B:{{scope.row.bratio ? scope.row.bratio : "0"}} C:{{scope.row.cratio ? scope.row.cratio : "0"}} D:{{scope.row.dratio ? scope.row.dratio : "0"}} E:{{scope.row.eratio ? scope.row.eratio : "0"}} F:{{scope.row.fratio ? scope.row.fratio : "0"}}</el-button>
            <el-button
              v-show="dbtype=='2'?true:false"
              @click="setWeight(scope.row)"
              type="text"
              size="small"
            >A:{{scope.row.aratio2 ? scope.row.aratio2 : "0"}} B:{{scope.row.bratio2 ? scope.row.bratio2 : "0"}} C:{{scope.row.cratio2 ? scope.row.cratio2 : "0"}} D:{{scope.row.dratio2 ? scope.row.dratio2 : "0"}} E:{{scope.row.eratio2 ? scope.row.eratio2 : "0"}} F:{{scope.row.fratio2 ? scope.row.fratio2 : "0"}}</el-button>
          </template>
        </el-table-column>
        <!-- <el-table-column
          label="所有被评分人"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="setByGradeUser(scope.row)"
              type="text"
              size="small"
            >编辑</el-button>
          </template>
        </el-table-column> -->
        <!-- <el-table-column
          label="发送短信"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="openMessage(3,scope.row)"
              type="text"
              size="small"
            >发送</el-button>
          </template>
        </el-table-column> -->
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
    <SetWeight
      @childClose="cancel"
      @childGetList="getList"
      :swDialogVisible="swDialogVisible"
      :parentForms="setWeightForm"
    ></SetWeight>
    <!-- 短信模板选择 -->
    <!-- <MessageCheck
      @childClose="childClose"
      @childSendMessageUser="sendMessage"
      @childGetList="getList"
      :messageDialogVisible="messageDialogVisible"
      :messageType="messageType"
    ></MessageCheck> -->
  </div>
</template>

<script>
import PostList from "../common/postList";
import SetWeight from "../common/setWeight";
// import MessageCheck from "../common/messageCheck";
import { getList,exportExcel } from "@/api/people/people";
import { sendMessageUser } from "@/api/sms/sms";
import qs from "qs";
export default {
  data() {
    return {
      title: "",
      search: {
        stationcode: "",
        username: "",
        rolecode: "",
      },
      tableData: [],
      stationcode: [""],
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
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
      fullstationcode: [""],
      dialogVisible: false,
      swDialogVisible: false,
      setWeightForm: {},
      tableLoading: true,
      dbtype: this.$store.state.user.user.dbtype,
      messageDialogVisible: false,
      messageType: 3,
      checkUser: {},
    };
  },
  components: {
    PostList,
    SetWeight
    // MessageCheck,
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
      params.dbtype = this.dbtype
      params.username = this.search.username;
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
    //获取岗位选择
    getSelectStation(data, row) {
      this.search.stationcode = [];
      this.search.stationcode.push(data);
      this.fullstationcode = row;
    },
    //关闭
    cancel() {
      this.dialogVisible = false;
      this.swDialogVisible = false;
    },
    //设置评分人
    setGradeUser(row, type) {
      this.$router.push({
        path: "/home/scorePeople/gradeUserList",
        query: { code: row.usercode, type: type, linkType: 'abc' },
      });
    },
    //设置评分人
    setDutyGradeUser(row,type) {
      this.$router.push({
        path: "/home/scorePeople/gradeDutyUserList",
        query: { code: row.usercode, type: type, uname: row.username, ustationname: row.stationname, 
                udeptname: row.departmentname,station: row.stationcode,moneycard:row.moneycard, linkType: 'd' 
            },
      });
    },
    gradeUser (row, type) {
      if(this.dbtype == '2' && (type == 'E' || type == 'F')){
        this.setDutyGradeUser(row, type);
      } else {
        this.setGradeUser(row, type);
      }
    },
    //设置被评分人
    setByGradeUser(row) {
      this.$router.push({
        path: "/home/scorePeople/byGradeUserList",
        query: { code: row.usercode },
      });
    },
    //设置评分系数
    setWeight(row) {
      this.setWeightForm = row;
      this.swDialogVisible = true;
    },
    //打开短信模板选择
    openMessage(type, row = {}) {
      this.checkUser = row;
      this.messageType = type;
      this.messageDialogVisible = true;
    },
    //关闭月结
    childClose(val) {
      this.messageDialogVisible = val;
    },
    //发送短信
    sendMessage(templatecode) {
      let data = {
        usercode: this.checkUser.usercode,
        templatecontent: templatecode,
      };
      this.$confirm("此操作将给该用户发送短信，是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          new Promise((response, reject) => {
            sendMessageUser(qs.stringify(data))
              .then((response) => {
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
                this.childClose();
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {});
    },
    //导出
    exportExcel() {
      let data = this.search;
      // const loading = this.$loading({
      //   lock: true,
      //   text: "Loading",
      //   spinner: "el-icon-loading",
      //   background: "rgba(0, 0, 0, 0.7)",
      // });
      // let data = this.search;
      // let fileName =
      //   "评分关系列表" +
      //   this.common.dateFormat("yyyyMMddhhmmss", new Date()) +
      //   ".xlsx";
      // return new Promise((response, reject) => {
      //   exportExcel(qs.stringify(data))
      //     .then((response) => {
      //       let blob = new Blob([response.data], {
      //         type: "application/vnd.ms-excel",
      //       });
      //       //for IE
      //       if (window.navigator.msSaveOrOpenBlob) {
      //         navigator.msSaveBlob(blob, fileName);
      //       } else {
      //         // for Non-IE (chrome, firefox etc.)
      //         let link = document.createElement("a");
      //         let evt = document.createEvent("HTMLEvents");
      //         evt.initEvent("click", false, false);
      //         link.href = URL.createObjectURL(blob);
      //         link.download = fileName;
      //         link.style.display = "none";
      //         document.body.appendChild(link);
      //         link.click();
      //         window.URL.revokeObjectURL(link.href);
      //       }
      //       this.$message.success("导出成功");
      //       loading.close();
      //     })
      //     .catch((error) => {
      //       loading.close();
      //       reject(error);
      //     });
      // });
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        "user/exportExcel?stationcode=" +
        data.stationcode +
        "&username=" +
        data.username;
    },
  },
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
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
