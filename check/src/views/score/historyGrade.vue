<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>历史评分管理
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="6" v-if="dbtype==2">
            <el-form-item label="所属岗位">
              <PostList
                @childSelectDepartment="getSelectStation"
                :selectedOptions="fullstationcode"
              ></PostList>
            </el-form-item>
          </el-col>
          <el-col :span="5" v-if="dbtype==1">
            <el-form-item label="党内身份">
            <el-select
              v-model="search.dbbk"
              placeholder="请选择"
              clearable
              style="width:100%;"
            >
              <el-option
                v-for="item in dbbk"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
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
            <el-form-item label="年份">
              <el-date-picker
                v-model="search.year"
                type="year"
                placeholder="选择年"
                value-format="yyyy"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="季度">
              <el-select
                v-model="search.month"
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
          <!-- <el-col
            :span="5"
            style="margin-top:20px;"
          >
            <el-form-item label="季结状态">
              <el-select
                v-model="search.state"
                clearable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in status"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col
            :span="5"
            style="margin-top:20px;"
          >
            <el-form-item label="打分状态">
              <el-select
                v-model="search.scorestatus"
                clearable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in gradeStatus"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col
            :span="6"
            style="margin-top:20px;"
            v-if="dbtype=='1'?false:true"
          >
            <el-form-item label="岗位类型">
              <el-select
                v-model="search.postType"
                clearable
                placeholder="请选择"
              >
              <el-option
                v-for="item in postTypeOptions"
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
                style="margin-left:-90px;"
                type="primary"
                @click="searchList"
                :loading="searchLoading"
              >搜索</el-button>
              <el-button
                type="warning"
                @click="exportExcel"
              >导出excel</el-button>
              <el-button
                type="primary"
                @click="notDialogVisible = true"
              >一键导出未评分和未完成用户</el-button>
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
          prop="username"
          label="用户姓名"
          show-overflow-tooltip
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
        <!-- <el-table-column
          label="角色权限"
          prop="rolename"
          v-if="dbtype=='1'?false:true"
          show-overflow-tooltip
        >
        </el-table-column> -->
        <el-table-column
          prop="dbbkName"
          v-if="dbtype=='2'?false:true"
          label="党内身份"
        >
        </el-table-column>
        <!-- <el-table-column
          label="打分状态"
          prop="scorestatusname"
          v-if="search.rolecode=='150'?true:false"
          show-overflow-tooltip
        >
        </el-table-column> -->
        <el-table-column
          label="季结状态"
          prop="statename"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="季结季度"
          prop="statename"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{scope.row.year}}{{'(第'+scope.row.month+'季度)'}}
          </template>
        </el-table-column>
        <el-table-column
          label="A"
          prop="ascore"
          show-overflow-tooltip
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
          label="B"
          prop="bscore"
          show-overflow-tooltip
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
          label="C"
          prop="cscore"
          show-overflow-tooltip
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
          label="D"
          prop="dscore"
          show-overflow-tooltip
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
          label="E"
          prop="score"
          show-overflow-tooltip
        >
        <template slot-scope="scope">
            <el-button
              @click="lookAssess(scope.row,'E')"
              type="text"
              size="small"
            >{{ scope.row.escore }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="F"
          prop="score"
          show-overflow-tooltip
        >
        <template slot-scope="scope">
            <el-button
              @click="lookAssess(scope.row,'F')"
              type="text"
              size="small"
            >{{ scope.row.fscore }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="总分"
          prop="totalscore"
          show-overflow-tooltip
        >
        </el-table-column>
        <!-- <el-table-column
          label="平均目标"
          v-if="dbtype=='1'?false:true"
          prop="avgMbScore"
          show-overflow-tooltip
        >
        </el-table-column> -->
        <el-table-column
          label="党风廉政"
          v-if="dbtype=='1'?false:true"
          prop="dfScore"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="总分和"
          v-if="dbtype=='1'?false:true"
          prop="sumTotalScore"
          show-overflow-tooltip
        >
        </el-table-column>
        <!-- <el-table-column
          label="月结修改/查看"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              @click="openAdd(scope.row)"
              type="text"
              size="small"
            >编辑</el-button>
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
    <!-- 添加月结 -->
    <AddQuarter
      :parentForms="forms"
      :dialogVisible="dialogVisible"
      :isAdd="1"
      :type="1"
      @childClose="childClose"
      @childGetList="getList"
    ></AddQuarter>
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
    <!-- 一键导出未评分和未完成用户 -->
    <el-dialog
      title="一键导出未评分和未完成用户"
      :visible.sync="notDialogVisible"
      width="30%"
    >
      <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="24">
            <el-form-item label="年份">
              <el-date-picker
                v-model="exportNot.year"
                type="year"
                placeholder="选择年"
                value-format="yyyy"
                style="width:70%"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="季度">
              <el-select
                v-model="exportNot.month"
                clearable
                placeholder="请选择"
                style="width:70%"
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
        </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="notDialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="exportNotSubmit"
        >确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import PostList from "../common/postList";
import { getList } from "@/api/score/history";
import AddQuarter from "../user/addQuarter";
import AssessLook from "../common/assessLook";
import AssessLook2 from "../common/assessLook2";
import qs from "qs";
export default {
  data() {
    return {
      quarterOptions: this.common.seasonOptions(),
      status: [
        {
          value: "0",
          label: "未提交",
        },
        {
          value: "5",
          label: "自评中",
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
      gradeStatus: [
        {
          value: "1",
          label: "未评分",
        },
        {
          value: "2",
          label: "未完成",
        },
        {
          value: "3",
          label: "已完成",
        },
      ],
      dbbk: [
        {
          value: "3",
          label: "党支部书记"
        },
        {
          value: "4",
          label: "党总支书记"
        }
      ],
      title: "",
      postTypeOptions: [{
          value: "1",
          label: "科主任"
        },
        {
          value: "2",
          label: "护士长"
        },
        {
          value: "3",
          label: "行政"
        }],
      search: {
        stationcode: "",
        username: "",
        postType: '',
        month: "",
        year: "",
        state: "",
        scorestatus: "",
        dbbk: ""
      },
      tableData: [],
      stationcode: [""],
      dbtype: this.$store.state.user.user.dbtype,
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      fullstationcode: [""],
      tableLoading: true,
      dialogVisible: false,
      setForm: {},
      swDialogVisible: false,
      swDialogVisible2: false,
      forms: {},
      searchLoading: false,
      notDialogVisible:false,
      exportNot:{
        year:"",
        month:""
      }
    };
  },
  components: {
    PostList,
    AddQuarter,
    AssessLook,
    AssessLook2
  },
  mounted() {},
  created() {
    this.getList();
  },
  methods: {
    //关闭月结
    childClose(val) {
      this.dialogVisible = val;
    },
    //打开月结
    openAdd(row) {
      this.forms = {
        serialno: row.serialno,
        title: row.title,
        content: row.content,
        savepath: row.savepath,
        filename: row.filename,
      };
      this.dialogVisible = true;
    },
    lookCancel(val){
      this.setForm = {}
      this.swDialogVisible = val;
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
          serialno: row.serialno,
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
      if(!isOK && row.escore !=0 && scoreType=='E'){
        isOK = true
      }
      if(!isOK && row.fscore !=0 && scoreType=='F'){
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
      if (
        (this.search.month && this.search.year) ||
        (!this.search.month && !this.search.year)
      ) {
        params.month = this.search.month;
        params.year = this.search.year;
      }else{
        this.$message.warning("年份和季度请同时选择");
        return;
      }
      if (this.search.stationcode.length > 0) {
        params.stationcode = this.search.stationcode[0];
      } else {
        params.stationcode = "";
      }
      this.searchLoading = true;
      params.username = this.search.username;
      params.state = this.search.state;
      params.postType = this.search.postType
      // params.rolecode = this.search.rolecode;
      params.scorestatus = this.search.scorestatus;
      params.dbtype = this.dbtype;
      params.dbbk = this.search.dbbk
      new Promise((response, reject) => {
        getList(qs.stringify(params))
          .then((response) => {
            this.searchLoading = false;
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
    //导出
    exportExcel() {
      let info = this.search;
      if (info.stationcode.length > 0) {
        info.stationcode = info.stationcode.join();
      }
      info.dbtype = this.dbtype;
      // window.location.href =
      //   process.env.VUE_APP_ITEM_NAME +
      //   "history/exportHistoryScore?info=" +
      //   JSON.stringify(info);
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        "history/exportHistoryScore?info=" +
        JSON.stringify(info);
    },
    //一键导出
    exportNotSubmit() {
      if (!this.exportNot.year) {
        this.$message.warning("请选择年份");
        return;
      }
      if(!this.exportNot.month){
        this.$message.warning("请选择季度");
        return;
      }
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        "history/oneClickDown?year=" +this.exportNot.year+ "&month="+this.exportNot.month;
      this.notDialogVisible  = false;
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
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
