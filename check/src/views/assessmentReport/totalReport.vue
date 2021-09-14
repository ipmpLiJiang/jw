<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link>
      <i class="el-icon-arrow-right"></i>评估报告
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
          class="home-el-form"
        >
          <el-col
            :span="6"
            style="display: flex;"
          >
            <el-form-item label="提交人姓名">
              <el-input
                placeholder="请输入名字"
                v-model="search.username"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="年份">
              <el-date-picker
                v-model="search.year"
                type="year"
                placeholder="选择年"
                value-format="yyyy"
                style="width:190px;"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="dbtype=='1'?'评分季':'评分月'">
              <el-select
                v-model="search.month"
                clearable
                placeholder="请选择"
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
          </el-col>
          <el-button
            type="primary"
            @click="searchList"
          >搜索</el-button>
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
          label="序号"
          width="100px"
        >
        </el-table-column>
        <el-table-column
          prop="username"
          label="员工姓名"
        >
        <template
            slot-scope="scope"
            v-if="scope.row.username"
          >
            {{scope.row.username}}({{scope.row.moneycard}})
          </template>
        </el-table-column>
        <el-table-column
          prop="departmentname"
          label="部门"
        >
        </el-table-column>
        <el-table-column
          prop="stationname"
          label="岗位"
        >
        </el-table-column>
        <el-table-column
          prop="dbbk"
          v-if="dbtype=='2'?false:true"
          label="党内身份"
        >
        </el-table-column>
        <el-table-column
          prop="ascore"
          label="A类得分"
        >
          <template slot-scope="scope">
            <el-button
              @click="lookAssess(scope.row,'A')"
              type="text"
              size="small"
            >{{ scope.row.ascore }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="bscore"
          label="B类得分"
        >
        <template slot-scope="scope">
            <el-button
              @click="lookAssess(scope.row,'B')"
              type="text"
              size="small"
            >{{ scope.row.bscore }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="cscore"
          label="C类得分"
        >
        <template slot-scope="scope">
            <el-button
              @click="lookAssess(scope.row,'C')"
              type="text"
              size="small"
            >{{ scope.row.cscore }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="dscore"
          label="D类得分"
        >
        <template slot-scope="scope">
            <el-button
              @click="lookAssess(scope.row,'D')"
              type="text"
              size="small"
            >{{ scope.row.dscore }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="sumMbAvgScore"
          v-if="dbtype=='1'?false:true"
          label="目标平均"
        >
        </el-table-column>
        <el-table-column
          prop="mbScore"
          v-if="dbtype=='1'?false:true"
          label="目标得分"
        >
        </el-table-column>
        <el-table-column
          prop="dfScore"
          v-if="dbtype=='1'?false:true"
          label="医德医风"
        >
        </el-table-column>
        <el-table-column
          prop="totalscore"
          label="个人得分"
        >
        </el-table-column>
        <el-table-column
          prop="avgscore"
          label="总体平均分"
        >
        </el-table-column>
        <!-- <el-table-column
          label="考核进度"
          style="color:#409EFF"
        >
          <template slot-scope="scope">
            <span style="color:#409EFF">{{scope.row.plan}}%</span>
          </template>
        </el-table-column> -->
        <el-table-column
          fixed="right"
          label="操作"
          width="100px"
        >
          <template slot-scope="scope">
            <el-button
              @click="detailReport(scope.row)"
              type="text"
              size="small"
            >详细报告</el-button>
            <!-- <el-button
               @click="openMessage(5,scope.row)"
              type="text"
              size="small"
            >发送短信</el-button> -->
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
        ref="pagination"
      >
      </el-pagination>
    </el-row>
    <!-- 短信模板选择 -->
    <!-- <MessageCheck
      @childClose="childClose"
      @childSendMessageUser="sendMessage"
      @childGetList="getList"
      :messageDialogVisible="messageDialogVisible"
      :messageType="messageType"
    ></MessageCheck> -->
    <AssessLook
      @childClose="lookCancel"
      :swDialogVisible="swDialogVisible"
      :parentForms="setForm"
    ></AssessLook>
    <AssessLook2
      @childClose="lookCancel2"
      :swDialogVisible2="swDialogVisible2"
      :parentForms="setForm"
    ></AssessLook2>
  </div>
</template>

<script>
// import MessageCheck from "../common/messageCheck";
import { selectAllReport } from "@/api/assessmentReport/index";
import { sendMessageUser } from "@/api/sms/sms";
import AssessLook from "../common/assessLook";
import AssessLook2 from "../common/assessLook2";
import qs from "qs";
export default {
  data() {
    return {
      paginationShow: true,
      search: {
        username: "",
        year: "",
        month: ""
      },
      fromPath: '',
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      dbtype: this.$store.state.user.user.dbtype,
      tableLoading: false,
      setForm: {},
      swDialogVisible: false,
      swDialogVisible2: false,
      quarterOptions: this.common.seasonOptions(),
      messageDialogVisible: false,
      messageType: 5,
      checkUser: {}
    };
  },
  components: {
    // MessageCheck,
    AssessLook,
    AssessLook2
  },
  mounted() {},
  created() {
    // this.getList();
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      vm.fromPath = from.path
      vm.getCookie(vm.fromPath) 
    });
  },
  methods: {
    //初始化
    into() {
      this.page.pageNum = 1;
      this.page.pageSize = 10;
    },
    lookCancel(val){
      this.setForm = {}
      this.swDialogVisible = val;
    },
    setCookie () {
       var name = this.$route.path;
       let value = [this.search.username,this.search.year,this.search.month,this.page.pageNum,this.page.pageSize]
       let day = '1'
       if (day !== 0) {
        var expires = day * 24 * 60 * 60 * 1000;
        var date = new Date(+new Date() + expires);
        document.cookie = name + "=" + escape(JSON.stringify(value)) + ";expires=" + date.toUTCString();
      } else {
        document.cookie = name + "=" + + escape(JSON.stringify(value));
      }
    },
    getCookie (data) {
      if('/home/someone' == data || '/' == data) {
        var arr;
        var name = this.$route.path;
        var reg = new RegExp("(^|)" + name + "=([^;]*)(;|$)");
        if(arr = document.cookie.match(reg)){
          var test = JSON.parse(unescape(arr[2]));
          this.search = {
            username: test[0],
            year: test[1],
            month: test[2]
          }
          this.page.pageNum = test[3]
          this.page.pageSize = test[4]
          this.$nextTick(()=>{
            this.$refs.pagination.internalCurrentPage = test[3];
          });
          this.getList();
        }
      }else{
        this.search = {
          username: "",
          year: "",
          month: ""
        }
        this.setCookie()
        this.searchList();
      }
    },
    lookCancel2(val){
      this.setForm = {}
      this.swDialogVisible2 = val;
    },
    lookAssess(row,scoreType) {
      row.scoreType = scoreType;
      this.setForm = {
          year: row.year,
          month: row.month,
          usercode: row.usercode,
          serialno: row.mserialno,
          scoreType: scoreType
      };
      let isOK = false
      if(row.ascore !=0 && scoreType=='A'){
        isOK = true
      }
      if(!isOK && row.bscore !=0 && scoreType=='B'){
        isOK = true
      }
      if(!isOK && row.cscore !=0 && scoreType=='C'){
        isOK = true
      }
      if(!isOK && row.dscore !=0 && scoreType=='D'){
        isOK = true
      }
      if(isOK){
        if (this.dbtype == '2') {
          this.swDialogVisible = true;
        } else {
          this.swDialogVisible2 = true;
        }
      } else {
        this.$message.warning("当前无评分明细");
      }
    },
    //设置每页多少条数据
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.setCookie()
      this.getList();
    },
    //翻页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.setCookie()
      this.getList();
    },
    //搜索
    searchList() {
      this.into();
      this.getList();
    },
    //查询列表
    getList() {
      this.tableLoading = true
      let params = this.page;
      params.username = this.search.username;
      params.year = this.search.year;
      params.month = this.search.month;
      params.dbtype = this.dbtype
      new Promise((response, reject) => {
        selectAllReport(qs.stringify(params))
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
    //进入考核
    detailReport(row) {
      this.setCookie()
      this.$router.push({
        path: "/home/someone",
        query: { usercode: row.usercode,year:this.search.year,month:this.search.month }
      });
    },
    //发送短信 旧版 暂未使用
    sendSms(val) {
      this.$confirm("确定发送该用户测评报告吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            id: val.id
          };
          new Promise((response, reject) => {
            sendEvaluationReport(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
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
        .catch(() => {});
    },
    //关闭
    childClose() {
      this.messageDialogVisible = false;
    },
    //打开短信模板选择
    openMessage(type, row = {}) {
      this.checkUser = row;
      this.messageType = type;
      this.messageDialogVisible = true;
    },
    //发送短信
    sendMessage(templatecode) {
      let data = {
        usercode: this.checkUser.usercode,
        templatecontent: templatecode
      };
      this.$confirm("此操作将给该用户发送短信，是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            sendMessageUser(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error"
                  });
                }
                this.childClose();
              })
              .catch(error => {
                reject(error);
              });
          });
        })
        .catch(() => {});
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
  margin: 0px 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
}
.home-el-form {
  display: flex;
  align-items: center;
  .total {
    font-size: 22px;
    color: #f00;
    padding: 0 5px;
  }
}
</style>
