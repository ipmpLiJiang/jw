<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>岗位关系管理
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
            <el-form-item label="所在部门">
              <DepartmentList
                @childSelectDepartment="getSelectDepartment"
                :selectedOptions="fulldepartmentcode"
              ></DepartmentList>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="岗位姓名">
              <el-input
                placeholder="请输入岗位姓名"
                v-model="search.stationName"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-button
            type="primary"
            @click="searchList"
          >搜索</el-button>
          <el-button
            type="warning"
            @click="shengChengScore"
          >生成</el-button>

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
  </div>
</template>

<script>

import SetWeight from "../common/setWeight";
import DepartmentList from "../common/departmentList";
import { getList } from "@/api/post/post";
import { shengChengScore } from "@/api/score/scoreStation";
import qs from "qs";
export default {
  data() {
    return {
      title: "",
      search: {
        departmentcode: [],
        stationName: ""
      },
      tableData: [],
      departmentcode: [""],
      fulldepartmentcode: [""],
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
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
    SetWeight,
    DepartmentList
    // MessageCheck,
  },
  mounted() {},
  created() {
    this.getList();
  },
  methods: {
    //获取部门选择
    getSelectDepartment(data, row) {
      this.search.departmentcode = [];
      this.search.departmentcode.push(data);
      this.fulldepartmentcode = row;
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
      if (this.search.departmentcode.length > 0) {
        params.departmentcode = this.search.departmentcode[0];
      } else {
        params.departmentcode = "";
      }
      params.stationname = this.search.stationName;
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
    //关闭
    cancel() {
      this.dialogVisible = false;
      this.swDialogVisible = false;
    },
    //设置评分人
    setGradeUser(row, type) {
      this.$router.push({
        path: "/home/scoreStationPeople/gradeStationList",
        query: { code: row.stationcode, type: type, linkType: 'abc' },
      });
    },
    //设置评分人
    setDutyGradeUser(row,type) {
      this.$router.push({
        path: "/home/scoreStationPeople/gradeDutyStationList",
        query: { code: row.stationcode,name:row.stationname,dcode:row.departmentcode,dname:row.departmentname, type: type, linkType: 'd' 
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
    shengChengScore() {
      this.$confirm("此操作将生成人员评分关系, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            dbtype: this.dbtype
          };
          new Promise((response, reject) => {
            shengChengScore(qs.stringify(data))
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
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    //设置被评分人
    setByGradeUser(row) {
      this.$router.push({
        path: "/home/scoreStationPeople/byGradeStationList",
        query: { code: row.stationcode },
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
        "user/exportExcel?departmentcode=" +
        data.departmentcode +
        "&stationname=" +
        data.stationname;
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
