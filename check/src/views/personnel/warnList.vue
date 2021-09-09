<template>
  <div>
    <h4 class="title">人事处考核未完成用户列表</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="6">
            <el-form-item label="部长姓名">
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
                :loading="searchLoading"
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
        ref="multipleTable"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
        >
        </el-table-column>
        <el-table-column
          type="index"
          width="50"
        >
        </el-table-column>
        <el-table-column
          prop="scorringname"
          label="部门长名称"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="leaderphone"
          label="部门长电话"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="deptname"
          label="部门名称"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="excelcomplete"
          label="excel表完成情况"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span
              v-if="scope.row.excelcomplete == '1'"
              style="color:#409EFF;"
            ><i class="el-icon-check"></i></span>
            <span
              v-else
              style="color:#F56C6C;"
            ><i class="el-icon-close"></i></span>
          </template>
        </el-table-column>
        <!-- <el-table-column
          prop="pdfcompletemonth"
          label="负责人上传分数签字pdf完成情况"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span
              v-if="scope.row.pdfcompletemonth == '1'"
              style="color:#409EFF;"
            ><i class="el-icon-check"></i></span>
            <span
              v-else
              style="color:#F56C6C;"
            ><i class="el-icon-close"></i></span>
          </template>
        </el-table-column> -->
        <!-- <el-table-column
          prop="pdfcompleteyear"
          label="经负责人、分管院长签字的科室考核办法pdf完成情况"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span
              v-if="scope.row.pdfcompleteyear == '1'"
              style="color:#409EFF;"
            ><i class="el-icon-check"></i></span>
            <span
              v-else
              style="color:#F56C6C;"
            ><i class="el-icon-close"></i></span>
          </template>
        </el-table-column> -->
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
      </el-table>
      <!-- <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination> -->
      <el-row class="send-msg">
        <el-button type="primary" @click='echoUser'>发送短信</el-button>
      </el-row>
    </el-row>
    <!-- 短信模板选择 -->
    <!-- <MessageCheck
      @childClose="childClose"
      @childSendMessageAll="sendMessage"
      @childGetList="getList"
      :messageDialogVisible="messageDialogVisible"
      :messageType="messageType"
    ></MessageCheck> -->
    <!-- 回显选中的人 -->
    <el-dialog
      title="确定给以下用户发送短信吗？"
      :visible.sync="selectDialogTableVisible"
    >

      <el-table :data="selectData">
        <el-table-column
          property="scorringname"
          label="部门长名称"
          width="150"
        ></el-table-column>
        <el-table-column
          property="deptname"
          label="部门"
          width="200"
        ></el-table-column>
        <el-table-column
          property="leaderphone"
          label="手机号"
        ></el-table-column>
      </el-table>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="selectDialogTableVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="openMessage"
        >发送短信</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getNoImportExcelAndPdf } from "@/api/personnel/index";
import deptList from "./common/deptList";
// import MessageCheck from "./message/messageCheck";
import { sendMessageToRaterUsers } from "@/api/sms/sms";
import qs from "qs";
export default {
  data() {
    return {
      title: "",
      search: {
        deptname: "",
        scorringname: ""
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableLoading: true,
      searchLoading: false,
      dialogVisible: false,
      messageType: 1,
      selectData: [],
      messageDialogVisible: false,
      selectDialogTableVisible: false
    };
  },
  mounted() {},
  created() {
    this.getList();
  },
  components: {
    deptList
    // MessageCheck
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
      params.year = this.$route.query.year;
      params.month = this.$route.query.month;
      if (this.$refs.searchSection) {
        params.deptname = this.$refs.searchSection.value;
      } else {
        params.deptname = "";
      }
      this.searchLoading = true;
      new Promise((response, reject) => {
        getNoImportExcelAndPdf(qs.stringify(params))
          .then(response => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
              this.$nextTick(() => {
                this.tableData.forEach(item => {
                  this.$refs.multipleTable.toggleRowSelection(item);
                });
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
            this.searchLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //打开提醒用户年分月份选择框
    warnUser() {
      this.dialogVisible = true;
    },
    //确定选择年份月份
    warnList() {
      if (!this.warn.year) {
        this.$message.warning("请选择年份");
        return;
      }
      if (!this.warn.month) {
        this.$message.warning("请选择月份");
        return;
      }
      this.$router.push({
        path: "/warnList",
        query: { year: this.warn.year, month: this.warn.mont }
      });
    },
    //关闭短信弹框
    childClose(val) {
      this.messageDialogVisible = val;
    },
    //发送短信
    sendMessage(templatecode) {
      let phone = [];
      this.selectData.forEach((row)=>{
        phone.push(row.leaderphone);
      })
      let data = {
        mobiles: phone.join(","),
        templatecontent: templatecode
      };
      this.$confirm("此操作将给该用户发送短信，是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            sendMessageToRaterUsers(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.$router.push({
                    path:'/personnel/complishSituation'
                  })
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
    },
    //选中的数据
    handleSelectionChange(val) {
      this.selectData = val;
    },
    //打开回显框
    echoUser(){
      this.selectDialogTableVisible = true;
    },
    //打开短信模板选择
    openMessage(type,row = {}){
      if(!this.selectData.length > 0){
        this.$message.warning("请先选择发送短信对象");
        return;
      }
      this.messageDialogVisible = true;
    },
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
.notice {
  position: absolute;
  right: 20px;
  bottom: 12px;
}
.send-msg {
  text-align: center;
  padding: 20px 0;
}
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
