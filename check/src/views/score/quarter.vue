<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>季度总结管理
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
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
            <el-form-item label="季结状态">
              <el-select
                v-model="search.state"
                clearable
                placeholder="请选择"
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
            :loading="searchLoading"
          >搜索</el-button>
        </el-form>
      </el-col>
    </el-row>
    <el-row class="content">
      <el-col style="margin-bottom:20px;">
        <el-button
          type="primary"
          @click="updateSubmit"
        >批量修改季结提交</el-button>
        <el-button
          type="primary"
          @click="updateGrade"
        >批量修改季结评分</el-button>
        <el-button
          type="warning"
          @click="updateAllStatus"
        >全部季结评分</el-button>
        <el-button
          type="primary"
          @click="manual"
          :loading="manualLoading"
        >开启下一个月考核</el-button>
        <!-- <el-button
          v-if="state == 1"
          type="primary"
          @click="automation"
          :loading="manualLoading"
        >开启自动考核</el-button> -->
      </el-col>
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="tableLoading"
        @selection-change="changeFun"
      >
        <el-table-column
          type="selection"
          width="55"
        >
        </el-table-column>
        <el-table-column
          prop="username"
          label="用户姓名"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="mobile"
          label="手机号"
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
          label="季结季度"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{scope.row.year}}-{{scope.row.month}}
          </template>
        </el-table-column>
        <el-table-column
          prop="statename"
          label="季结状态"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="修改状态"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="openStatus(scope.row)"
              type="text"
              size="small"
            >修改</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="季结修改/查看"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="openAdd(scope.row)"
              type="text"
              size="small"
            >编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="发送短信"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="openMessage(2,scope.row)"
              type="text"
              size="small"
            >发送</el-button>
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
    <!-- 短信模板选择 -->
    <!-- <MessageCheck
      @childClose="childClose"
      @childSendMessageAll="warnAll"
      @childSendMessagePart="sendMessage"
      @childGetList="getList"
      :messageDialogVisible="messageDialogVisible"
      :messageType="messageType"
    ></MessageCheck> -->
    <!-- 修改月结 -->
    <AddQuarter
      :parentForms="forms"
      :dialogVisible="dialogVisible"
      :isAdd="1"
      :type="1"
      @childClose="childClose"
      @childGetList="getList"
    ></AddQuarter>
    <!-- 修改状态 -->
    <el-dialog
      title="修改状态"
      :visible.sync="statusDialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-select
        v-model="status"
        placeholder="请选择"
        style="width:100%"
      >
        <el-option
          v-for="item in statusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="statusDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="updateStatus"
        >确 定</el-button>
      </span>
    </el-dialog>
    <!-- 开启时间选择框 -->
    <el-dialog
      title="提示"
      :visible.sync="timeDialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <el-form
        ref="form"
        label-width="80px"
      >
        <el-form-item label="开启时间">
          <el-date-picker
            v-model="startTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="timeDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="submitManual"
        >确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import PostList from "../common/postList";
// import MessageCheck from "../common/messageCheck";
import {
  getList,
  updateSummaryGradeStateAll,
  updateSummarySubmitState,
  updateSummaryGradeState,
  isAllFinish,
  openManualAssessment,
  closeManualAssessment
} from "@/api/score/quarter";
import { updateStateBySerialNo } from "@/api/user/quarter";
import { sendOneMessage, sendMessageAll } from "@/api/sms/sms";
import AddQuarter from "../user/addQuarter";
import qs from "qs";
export default {
  data() {
    return {
      quarterOptions: [
        {
          value: "0",
          label: "未提交"
        },
        // {
        //   value: "1",
        //   label: "已提交"
        // },
        {
          value: "5",
          label: "自评中"
        },
        {
          value: "6",
          label: "评分中"
        },
        {
          value: "7",
          label: "评分完成"
        }
      ],
      statusOptions: [
        {
          value: "5",
          label: "季结提交"
        },
        {
          value: "6",
          label: "季结评分"
        }
      ],
      gradeStatus: [
        {
          value: "1",
          label: "未评分"
        },
        {
          value: "2",
          label: "未完成"
        },
        {
          value: "3",
          label: "已完成"
        }
      ],
      title: "",
      search: {
        state: "",
        username: ""
      },
      tableData: [],
      stationcode: [""],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      fullstationcode: [""],
      dialogVisible: false,
      tableLoading: true,
      status: "",
      statusDialogVisible: false,
      tempStatusRow: {},
      forms: {},
      checkBoxData: [],
      messageDialogVisible: false,
      messageType: 1,
      checkUser: {},
      searchLoading: true,
      timeDialogVisible: false,
      state: 0,
      startTime: "",
      manualLoading: false
    };
  },
  components: {
    PostList,
    AddQuarter
    // MessageCheck
  },
  mounted() {},
  created() {
    this.getList();
    this.state = Number(localStorage.getItem("checkState"));
  },
  methods: {
    handleClose() {
      this.statusDialogVisible = false;
    },
    //关闭月结
    childClose(val) {
      this.dialogVisible = val;
      this.messageDialogVisible = val;
    },
    //打开月结
    openAdd(row) {
      this.forms = {
        serialno: row.serialno,
        title: row.title,
        content: row.content,
        savepath: row.savepath,
        filename: row.filename
      };
      this.dialogVisible = true;
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
      params.state = this.search.state;
      params.scorestatus = this.search.scorestatus;
      this.searchLoading = true;
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
            this.searchLoading = false;
          })
          .catch(error => {
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
    //打开月结状态
    openStatus(row) {
      this.tempStatusRow = row;
      this.statusDialogVisible = true;
    },
    //修改月结状态
    updateStatus() {
      if (!this.status) {
        this.$message.warning("请选择状态");
        return;
      }
      let params = {
        serialno: this.tempStatusRow.serialno,
        state: this.status
      };
      new Promise((response, reject) => {
        updateStateBySerialNo(qs.stringify(params))
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
            this.tableLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
      this.statusDialogVisible = false;
    },
    //全部季结评分
    updateAllStatus() {
      this.$confirm("此操作将所有人季结状态改成季结评分, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            updateSummaryGradeStateAll({dbtype: this.$store.state.user.user.dbtype})
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                  // this.into();
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
    //批量修改月结提交
    updateSubmit() {
      if (this.checkBoxData.length <= 0) {
        this.$message.warning("请先勾选需要更改的数据");
        return;
      }
      let tData = [];
      this.checkBoxData.forEach(row => {
        tData.push(row.serialno);
      });
      let data = {
        serialnos: tData.join(",")
      };
      this.$confirm("此操作将季结状态改成季结提交, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            updateSummarySubmitState(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                  this.into();
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
    //批量修改月结评分
    updateGrade() {
      if (this.checkBoxData.length <= 0) {
        this.$message.warning("请先勾选需要更改的数据");
        return;
      }
      let tData = [];
      this.checkBoxData.forEach(row => {
        tData.push(row.serialno);
      });
      let data = {
        serialnos: tData.join(",")
      };
      this.$confirm("此操作将季结状态改成季结评分, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            updateSummaryGradeState(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                  this.into();
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
    //获取选中的值
    changeFun(val) {
      this.checkBoxData = val;
    },
    //打开短信模板选择
    openMessage(type, row = {}) {
      this.checkUser = row;
      this.messageType = type;
      this.messageDialogVisible = true;
    },
    //一键短信提醒所有人
    warnAll(templatecode) {
      let data = {
        templatecontent: templatecode
      };
      this.$confirm(
        "此操作将给所有人(不包含打分用户)发送考核开始的提醒短信，并且一个季度只能使用一次此功能，是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          new Promise((response, reject) => {
            sendMessageAll(qs.stringify(data))
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
    },
    //发送短信
    sendMessage(templatecode) {
      let data = {
        usercode: this.checkUser.usercode,
        templatecontent: templatecode
      };
      this.$confirm(
        "此操作将给该用户所有评分人发送短信（如果某评分人已收到过其他考核人短信提醒，将不再收到短信），是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          new Promise((response, reject) => {
            sendOneMessage(qs.stringify(data))
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
    },
    //请求手动操作
    manual() {
      this.manualLoading = true;
      new Promise((response, reject) => {
        isAllFinish({dbtype: this.$store.state.user.user.dbtype})
          .then(response => {
            if (response.data.code == 0) {
              this.$confirm(response.data.msg, "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              })
                .then(() => {
                  this.timeDialogVisible = true;
                })
                .catch(() => {});
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.manualLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //开启手动操作
    submitManual() {
      let data = {
        time: this.startTime,
        createmoneycard:this.$store.state.user.user.moneycard,
        dbtype: this.$store.state.user.user.dbtype
      };
      new Promise((response, reject) => {
        openManualAssessment(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success"
              });
              this.state = 1;
              localStorage.setItem("checkState", 1);
              this.timeDialogVisible = false;
              console.log(localStorage.getItem("checkState"));
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
    },
    //开启自动操作
    automation() {
      let data = {
        createmoneycard:this.$store.state.user.user.moneycard
      };
      this.$confirm("确认开启自动考核吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          new Promise((response, reject) => {
            closeManualAssessment(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.state = 2;
                  localStorage.setItem("checkState", 2);
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
