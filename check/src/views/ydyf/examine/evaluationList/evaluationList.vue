<template>
  <div>
    <h4 class="title">我的考评列表</h4>

    <div class="my-check-container">
      <el-button
        type="primary"
        class="nextForm"
        @click="nextForm"
        size="small"
        v-if="nextbutton"
      >请去填写考核表</el-button>
      <el-tabs
        v-model="activeName"
        class="tabs-box"
      >
        <el-tab-pane
          label="临床人员列表"
          name="first"
          v-if="clinicalTable.length > 0"
        >
          <el-table
            :data="clinicalTable"
            border
            style="width: 100%"
            v-loading="tableLoading"
          >
            <el-table-column
              type="index"
              width="70"
              :index="indexMethod"
              align="center"
            >
            </el-table-column>
            <el-table-column
              prop="username"
              label="用户姓名"
              show-overflow-tooltip
              align="center"
              width="80"
            >
            </el-table-column>
            <el-table-column
              prop="sex"
              label="性别"
              show-overflow-tooltip
              align="center"
              width="50"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.sex == 0 ? "女" : "男" }}</span>            
              </template>
            </el-table-column>
            <el-table-column
              prop="userId"
              label="发薪号"
              show-overflow-tooltip
              align="center"
              width="100"
            >
            </el-table-column>
            <el-table-column
              prop="birth"
              label="出生年月"
              show-overflow-tooltip
              align="center"
              width="120"
            >
            </el-table-column>
            <el-table-column
              prop="partyCommitteesName"
              label="所属党委"
              show-overflow-tooltip
              align="center"
              width="130"
            >
            </el-table-column>
            <el-table-column
              prop="generalBranchName"
              label="所属党总支"
              show-overflow-tooltip
              align="center"
              width="130"
            >
            </el-table-column>
            <el-table-column
              prop="branchName"
              label="所属党支部"
              show-overflow-tooltip
              align="center"
              width="130"
            >
            </el-table-column>
            <!-- <el-table-column
          prop=""
          label="所属科室"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column> -->

            <el-table-column
              prop="year"
              label="考核年度"
              show-overflow-tooltip
              align="center"
              width="100"
            >
            </el-table-column>
            <el-table-column
              label="员工类型"
              show-overflow-tooltip
              align="center "
              width="100"
            >
              <template slot-scope="scope">
                <span>{{
              scope.row.personType == 0 ? "临床人员" : "非临床人员"
            }}</span>            
              </template>
            </el-table-column>

            <el-table-column
              label="考核状态"
              show-overflow-tooltip
              align="center"
              width="100"
            >
              <template slot-scope="scope">
                <template v-if="scope.row.personType == 1">
                  <span
                    v-if="scope.row.step == 0"
                    style="color:#909399"
                  >未填写</span>
                  <span
                    v-if="scope.row.step==1"
                    style="color:#409EFF"
                  >用户完成自我评价</span>
                  <span
                    v-if="scope.row.step==2"
                    style="color:#409EFF"
                  >主任已经完成考核</span>
                  <span
                    v-if="scope.row.step==3"
                    style="color:#409EFF"
                  >书记已经完成考核</span>
                  <span
                    v-if="scope.row.step==6"
                    style="color:#409EFF"
                  >已结束</span>
                </template>
                <template v-if="scope.row.personType == 0">
                  <span
                    v-if="scope.row.step == 0"
                    style="color:#909399"
                  >未填写完成</span>
                  <span
                    v-else-if="scope.row.step<=3"
                    style="color:#409EFF"
                  >用户完成自我评价</span>
                  <span
                    v-else-if="scope.row.step==4"
                    style="color:#409EFF"
                  >主任已经完成考核</span>
                  <span
                    v-else-if="scope.row.step==5"
                    style="color:#409EFF"
                  >书记已经完成考核</span>
                  <span
                    v-if="scope.row.step==6"
                    style="color:#409EFF"
                  >已结束</span>
                </template>
              </template>
            </el-table-column>
            <el-table-column
              label="自我评分"
              align="center"
              width="80"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.step > 2">{{ scope.row.totalSelfScore }}</span>
                <span v-else>暂无数据</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="deptHeadOpinion"
              label="科室主任评分"
              align="center"
              width="110"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.step > 3">{{ scope.row.totalHeadScore }}</span>
                <span v-else>暂无数据</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="branchOpinion"
              label="打分书记评分"
              align="center"
              width="110"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.step > 4">{{ scope.row.totalBranchScore }}</span>
                <span v-else>暂无数据</span>
              </template>
            </el-table-column>
            <el-table-column
              label="合计总分"
              show-overflow-tooltip
              align="center"
              width="80"
              v-if="personType == 0"
            >
              <template slot-scope="scope">
                <template v-if="personType == 0">
                  <span>{{
                scope.row.step == 3 ? "待合计" : scope.row.score
              }}</span>
                </template>
                <template v-if="personType == 1">
                  <span>{{
                scope.row.step == 5 ? "待合计" : scope.row.score
              }}</span>
                </template>
              </template>
            </el-table-column>
            <el-table-column
              label="评级"
              show-overflow-tooltip
              align="center"
              width="80"
            >
              <template slot-scope="scope">
                <span
                  v-if="scope.row.level == 1&&scope.row.step==5"
                  style="color: #2bd52b"
                >优秀</span>

                <span
                  v-else-if="scope.row.level == 2&&scope.row.step==5"
                  style="color: #f79709"
                >良好</span>  
                <span
                  v-else-if="scope.row.level == 3&&scope.row.step==5"
                  style="color: #f79709"
                >一般</span>   
                <span
                  v-else-if="scope.row.level == 4 &&scope.row.step==5"
                  style="color: #f79709"
                >较差</span>
                <span v-else>待总结</span>
              </template>
            </el-table-column>

            <!-- <el-table-column
          prop="departmentname"
          label="部门"
          show-overflow-tooltip
        >
        </el-table-column> -->
            <el-table-column
              fixed="right"
              label="操作"
              align="center"
            
            >
              <template slot-scope="scope">
                <el-button
                  @click="nextForm(scope.row)"
                  type="text"
                  size="small"
                  v-if="scope.row.step < 3"
                >编辑</el-button>
                <el-button
                  @click="editUser(scope.row)"
                  type="text"
                  size="small"
                >查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane
          label="非临床人员列表"
          name="second"
          v-if="noclinicalTable.length > 0"
        >
          <el-table
            :data="noclinicalTable"
            border
            style="width: 100%"
            v-loading="tableLoading"
          >
            <el-table-column
              type="index"
              width="50"
              :index="indexMethod"
              align="center"
            >
            </el-table-column>
            <el-table-column
              prop="username"
              label="用户姓名"
              show-overflow-tooltip
              align="center"
              width="80"
            >
            </el-table-column>
            <el-table-column
              prop="sex"
              label="性别"
              show-overflow-tooltip
              align="center"
              width="50"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.sex == 0 ? "女" : "男" }}</span>            
              </template>
            </el-table-column>
            <el-table-column
              prop="userId"
              label="发薪号"
              show-overflow-tooltip
              align="center"
              width="100"
            >
            </el-table-column>
            <el-table-column
              prop="birth"
              label="出生年月"
              show-overflow-tooltip
              align="center"
              width="120"
            >
            </el-table-column>
            <el-table-column
              prop="partyCommitteesName"
              label="所属党委"
              show-overflow-tooltip
              align="center"
              width="130"
            >
            </el-table-column>
            <el-table-column
              prop="generalBranchName"
              label="所属党总支"
              show-overflow-tooltip
              align="center"
              width="130"
            >
            </el-table-column>
            <el-table-column
              prop="branchName"
              label="所属党支部"
              show-overflow-tooltip
              align="center"
              width="130"
            >
            </el-table-column>
            <!-- <el-table-column
          prop=""
          label="所属科室"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column> -->

            <el-table-column
              prop="year"
              label="考核年度"
              show-overflow-tooltip
              align="center"
              width="100"
            >
            </el-table-column>
            <el-table-column
              label="员工类型"
              show-overflow-tooltip
              align="center "
              width="100"
            >
              <template slot-scope="scope">
                <span>{{
              scope.row.personType == 0 ? "临床人员" : "非临床人员"
            }}</span>            
              </template>
            </el-table-column>

            <el-table-column
              label="考核状态"
              show-overflow-tooltip
              align="center"
              width="100"
            >
              <template slot-scope="scope">
                <template v-if="scope.row.personType == 1">
                  <span
                    v-if="scope.row.step == 0"
                    style="color:#909399"
                  >未填写</span>
                  <span
                    v-if="scope.row.step==1"
                    style="color:#409EFF"
                  >用户完成自我评价</span>
                  <span
                    v-if="scope.row.step==2"
                    style="color:#409EFF"
                  >主任已经完成考核</span>
                  <span
                    v-if="scope.row.step==3"
                    style="color:#409EFF"
                  >书记已经完成考核</span>
                </template>
                <template v-if="scope.row.personType == 0">
                  <span
                    v-if="scope.row.step == 0"
                    style="color:#909399"
                  >未填写完成</span>
                  <span
                    v-else-if="scope.row.step<=3"
                    style="color:#409EFF"
                  >用户完成自我评价</span>
                  <span
                    v-else-if="scope.row.step==4"
                    style="color:#409EFF"
                  >主任已经完成考核</span>
                  <span
                    v-else-if="scope.row.step==5"
                    style="color:#409EFF"
                  >书记已经完成考核</span>
                </template>
              </template>
            </el-table-column>
            <el-table-column
              prop="deptHeadOpinion"
              label="科室主任评分"
              align="center"
              width="110"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.deptHeadOpinion == 1">优秀</span>
                <span v-if="scope.row.deptHeadOpinion == 2">较好</span>
                <span v-if="scope.row.deptHeadOpinion == 3">一般</span>
                <span v-if="scope.row.deptHeadOpinion == 4">较差</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="branchOpinion"
              label="打分书记评分"
              align="center"
              width="110"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.branchOpinion == 1">优秀</span>
                <span v-if="scope.row.branchOpinion == 2">较好</span>
                <span v-if="scope.row.branchOpinion == 3">一般</span>
                <span v-if="scope.row.branchOpinion == 4">较差</span>
              </template>
            </el-table-column>

            <el-table-column
              label="评级"
              show-overflow-tooltip
              align="center"
              width="80"
            >
              <template slot-scope="scope">
                <template v-if="scope.row.step == 3">
                  <span
                    v-if="scope.row.level == 1"
                    style="color: #2bd52b"
                  >优秀</span>

                  <span
                    v-else-if="scope.row.level == 2"
                    style="color: #f79709"
                  >良好</span>  
                  <span
                    v-else-if="scope.row.level == 3"
                    style="color: #f79709"
                  >一般</span>   
                  <span
                    v-else-if="scope.row.level == 4"
                    style="color: #f79709"
                  >较差</span>
                  <span v-else>待总结</span>
                    </template> 
              </template>
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              align="center"
              
            >
              <template slot-scope="scope">
                <el-button
                  @click="nextForm(scope.row)"
                  type="text"
                  size="small"
                  v-if="scope.row.step < 1"
                >编辑</el-button>
                <el-button
                  @click="editUser(scope.row)"
                  type="text"
                  size="small"
                  v-if="scope.row.step >= 1"
                >查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script>
import qs from "qs";
import {
  list,
  add,
  Delete,
  update,
  downloadExcel,
  readExcel,
  doSubmit,
  realList,
  getMyList,
} from "../../api/addData/addData";
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
      classify: "",
      personnelList: [
        {
          value: 0,
          label: "2020年",
        },
        {
          value: 1,
          label: "2021年",
        },
      ],
      value: "",
      condition: "", //选择状态
      tableData: [],
      useridList: [],
      userId: "",
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      tableLoading: true,
      dialogVisible: false,
      personType: "",
      nextbutton: false,

      detailData: {},
      title: "",
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
      clinicalTable: [],
      noclinicalTable: [],
      activeName: "first",
    };
  },
  computed: {},
  created() {
    this.getmyList();
    // this.getList();
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
    searchList() {},
    //查询列表
    getmyList() {
      let year = "2020";
      new Promise((response, reject) => {
        getMyList(year)
          .then((response) => {
          
            if (response.data.code == 0) {

              this.tableData = response.data.data;
              this.personType = response.data.personType;
              //拆分数组变成临床和非临床两个数组
              this.clinicalTable = [];
              this.noclinicalTable = [];
              this.tableData.forEach((row) => {
                if (row.personType == 0) {
                  this.clinicalTable.push(row);
                } else {
                  this.noclinicalTable.push(row);
                }
              });
              if(this.tableData.length <= 0){
                this.nextbutton = true;
              }
              this.tableLoading = false;
              //判断初始展示
              if(this.clinicalTable.length > 0 ){
                this.activeName = "first";
                return;
              }
              if(this.noclinicalTable.length > 0 ){
                this.activeName = "second";
                return;
              }
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
              this.tableLoading = false;
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //下一步填写表单
    nextForm() {
      let userId = this.$store.state.user.user.moneycard;
      if (this.personType == 1) {
        this.$router.push({
          path: "/ydyf/nonclinical",
          query: { userId: userId },
        });
      } else {
        this.$router.push({
          path: "/ydyf/clinical",
          query: { userId: userId },
        });
      }
    },
    personnel() {},

    handleRemove(file, fileList) {},
    handlePreview(file) {},
    beforeUpload(file) {},

    onUploadChange(file) {
      this.excelPath = file;
    },
    //查询
    addPerson() {},
    //查看
    editUser(row) {
      
      if (row.personType == 1) {
        if (row.step > 0) {
          this.$router.push({
            path: "/ydyf/resultNonclinical",
            query: { userId: row.userId },
          });
        } else {
          this.$message.warning("请填写完自我考评后再查看");
          return;
        }
      } else {
        if (row.step > 2) {
          this.$router.push({
            path: "/ydyf/resultclinicalForm",
            query: { userId: row.userId },
          });
        } else {
          this.$message.warning("请填写完自我考评后再查看");
          return;
        }
      }
      // } else {
      //   this.$message.error("您的分数正在考核中...请稍等");
      // }
    },
    //查看
    deleteUser(row) {
    
      this.$router.push({
        path: "/ydyf/clinicalForm",
        query: {
          userId: row.userId,
          year: row.year,
        },
      });
    },
  },
  components: {},
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
.tabs-box {
  margin: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 5px;
  position: relative;
}
.score-dialog {
  max-height: 500px;
  overflow: auto;
}

.nextForm {
  position: absolute;
    right: 40px;
    top: 10px;
    z-index: 99;
}
.my-check-container{
  position: relative;
}
</style>