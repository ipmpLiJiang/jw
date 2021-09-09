<template>
  <div>
    <h4 class="title">考核评分汇总</h4>

    <el-row class="search">
      <el-col>
        <el-form label-width="100px" show-overflow-tooltip="true">
          <el-col :span="4">
            <el-form-item label="姓名">
              <el-input
                placeholder="请输入员工姓名"
                v-model="search.username"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
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

          <el-col :span="4">
            <el-form-item label="选择科室">
              <el-select
                v-model="search.departmenName"
                filterable
                placeholder="请选择"
                style="width: 100%"
                clearable
                multiple
                @change="department"
              >
                <el-option
                  v-for="item in deptOptions"
                  :key="item.id"
                  :label="item.departmentName"
                  :value="item.id"
                >
                  <span style="float: left">{{ item.departmentName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{
                    item.branchName
                  }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="员工类型">
              <el-select
                v-model="classify"
                placeholder="请选择员工类型"
                @change="personnel"
                clearable
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

          <el-col :span="4">
            <el-form-item label="考核结果">
              <el-select
                v-model="search.level"
                placeholder="请选择考核成绩"
                @change="result"
                clearable
              >
                <el-option
                  v-for="item in resultList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

           <el-col :span="4" >
            <el-form-item label="政治面貌" style="margin-right: 9px;">
              <el-select
                v-model="search.politicalStatus"
                placeholder="请选择政治面貌"
                @change="result"
                clearable
              >
                <el-option
                  v-for="item in appellation"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          

          <el-col :span="24" style="margin-top: 15px">
            <el-col :span="4">
              <el-form-item label="文化程度">
                <el-select
                  v-model="culture"
                  placeholder="请选择文化程度"
                  @change="Culture"
                  clearable
                  multiple
                >
                  <el-option
                    v-for="item in cultureList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="4">
              <el-form-item label="职称">
                <el-select
                  v-model="search.title"
                  placeholder="请选择职称"
                  @change="professiona"
                  clearable
                >
                  <el-option
                    v-for="item in titleList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="4">
              <el-form-item label="党支部" label-width="100px">
                <el-cascader
                  v-model="search.branchId"
                  :options="secretaryOptions"
                  placeholder="请选择党支部"
                  style="width: 100%"
                  clearable
                  @change="handleChange"
                   :props="{ checkStrictly: true }"
                ></el-cascader>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="请选择年龄段" style="margin-left: 15px">
                <el-input-number :sapn="3" v-model="search.minAge" />
                --
                <el-input-number :sapn="3" v-model="search.maxAge" />
              </el-form-item>
            </el-col>
            
          </el-col>

          <el-col :span="24" class="edit-btn">
            <el-form-item>
              <el-button
                style="margin-left: -90px"
                type="primary"
                @click="searchList"
                ><i class="el-icon-search"></i>搜索
              </el-button>

              <!-- <el-button type="info" @click="reset">重置</el-button> -->
              <el-button type="warning" @click="exportPerson" :loading="isLoginLoading">
                <i class="el-icon-download"></i>导出</el-button
              >
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
        @selection-change="changeFun"
      >
        <el-table-column type="index" width="50" :index="indexMethod">
        </el-table-column>
        <el-table-column
          prop="username"
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
          prop="birth"
          label="出生年月"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="politicalStatusName"
          label="政治面貌"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="educationLevelName"
          label="文化程度"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="titleName"
          label="职称"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="hireDate"
          label="聘用时间"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="year"
          label="考核年份"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="departmentName"
          label="科室"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="generalBranchName"
          label="所在党支部"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <!-- <el-table-column
          prop="totalSelfScore"
          label="自我评分"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column> -->
        <!-- <el-table-column
          prop="totalHeadScore"
          label="部门负责人评分"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column> -->
        <!-- <el-table-column
          prop="totalBranchScore"
          label="党支部评分"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column> -->
        <el-table-column label="员工类型" show-overflow-tooltip align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.personType == 0 ? "临床人员" : "非临床人员"
            }}</span
            >            
          </template>
        </el-table-column>
        <!-- <el-table-column label="状态" show-overflow-tooltip align="center">
          <template slot-scope="scope">
            
            <span v-if="scope.row.status == 1" style="color: #2bd52b"
              >已提交</span
            > 
            <span v-if="scope.row.status == 0" style="color: #f79709"
              >未提交</span
            >               
          </template>
        </el-table-column> -->
        <el-table-column label="考核结果" show-overflow-tooltip align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.step == '5' && scope.row.personType == '0'">{{
              scope.row.levelName
            }}</span>
            <span
              v-else-if="scope.row.step == '3' && scope.row.personType == '1'"
              >{{ scope.row.levelName }}</span
            >
            <span v-else>暂无结果</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="160" align="center">
          <template slot-scope="scope">
            <el-button @click="deleteUser(scope.row)" type="text" size="small"
              >详情
            </el-button>
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
import fileDownload from 'js-file-download';
import { checkResult } from "../../api/addData/addData";
import { deptListAll, tree,exportCheckResult  } from "../../api/department/department";

export default {
  props: {},
  data() {
    return {
      search: {
        username: "",
        userId: "",
        status: "",
        personType: "",
        //  level: "",
        // culture: "",
        minAge: null,
        maxAge: null,
        title: "",
        // branchId: "",
        educationList: "",
        departmenName: "",
        level:''
      },
      options: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: "0",
          label: "未提交",
        },
        {
          value: "1",
          label: "已提交",
        },
      ],

      personnelList: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: 0,
          label: "临床人员",
        },
        {
          value: 1,
          label: "非临床人员",
        },
      ],
      resultList: [
        {
          value: "",
          label: "全部",
        },
        {
          value: 1,
          label: "优秀",
        },
        {
          value: 2,
          label: "良好",
        },
        {
          value: 3,
          label: "一般",
        },
        {
          value: 4,
          label: "较差",
        },
      ],

      cultureList: [
        {
          value: "1",
          label: "大专及以下",
        },
        {
          value: "2",
          label: "本科",
        },
        {
          value: "3",
          label: "硕士研究生",
        },
        {
          value: "4",
          label: "博士研究生",
        },
      ],
      ageList: [
        {
          value: "1",
          label: "20——30周岁",
        },
        {
          value: "2",
          label: "30——40周岁",
        },
        {
          value: "3",
          label: "40——50周岁",
        },
        {
          value: "4",
          label: "50——60周岁",
        },
      ],
      titleList: [
        {
          value: "1",
          label: "教授",
        },
        {
          value: "2",
          label: "副教授",
        },
        {
          value: "3",
          label: "讲师",
        },
        {
          value: "4",
          label: "助教",
        },
        {
          value: "5",
          label: "正高级工程师",
        },
        {
          value: "6",
          label: "高级工程师",
        },
        {
          value: "7",
          label: "工程师",
        },
        {
          value: "8",
          label: "助理工程师",
        },
        {
          value: "9",
          label: "工人",
        },
        {
          value: "10",
          label: "研究员",
        },
        {
          value: "11",
          label: "副研究员",
        },
        {
          value: "12",
          label: "助理研究员",
        },
        {
          value: "13",
          label: "主任医师",
        },
        {
          value: "14",
          label: "副主任医师",
        },
        {
          value: "15",
          label: "主治医师",
        },
        {
          value: "16",
          label: "医师",
        },
        {
          value: "17",
          label: "主任药师",
        },
        {
          value: "18",
          label: "副主任药师",
        },
        {
          value: "19",
          label: "主管药师",
        },
        {
          value: "20",
          label: "药师",
        },
        {
          value: "21",
          label: "药士",
        },
        {
          value: "22",
          label: "主任护师",
        },

        {
          value: "23",
          label: "副主任护师",
        },
        {
          value: "24",
          label: "主管护师",
        },
        {
          value: "25",
          label: "护师",
        },
        {
          value: "26",
          label: "护士",
        },
        {
          value: "27",
          label: "主任技师",
        },

        {
          value: "28",
          label: "副主任技师",
        },
        {
          value: "29",
          label: "主管技师",
        },
        {
          value: "30",
          label: "技师",
        },

        {
          value: "31",
          label: "技士",
        },
        {
          value: "32",
          label: "三级职员",
        },
        {
          value: "33",
          label: "四级职员",
        },
        {
          value: "34",
          label: "五级职员",
        },
        {
          value: "35",
          label: "六级职员",
        },
        {
          value: "36",
          label: "七级职员",
        },
        {
          value: "37",
          label: "八级职员",
        },
        {
          value: "38",
          label: "九级职员",
        },
        {
          value: "39",
          label: "十级职员",
        },
        {
          value: "40",
          label: "其他",
        },
      ],
       appellation: [
        {
          value: "1",
          label: "中国共产党员",
        },
        {
          value: "2",
          label: "团员",
        },
        {
          value: "3",
          label: "民盟",
        },
        {
          value: "4",
          label: "民建",
        },
        {
          value: "5",
          label: "民进",
        },
        {
          value: "6",
          label: "农工党",
        },
        {
          value: "8",
          label: "致公党",
        },

        {
          value: "9",
          label: "九三学社",
        },
        {
          value: "10",
          label: "无党派人士",
        },
        {
          value: "11",
          label: "群众",
        },
        {
          value: "12",
          label: "其他",
        },
      ],
      value: "",
      condition: "", //选择状态
      classify: "", //选择非临床医务人员
      culture: "", //文化程度
      roleType: 1,
      // submitShow:false, //提交按钮显示隐藏
      tableData: [],
      useridList: [],
      educationList: "",
      userId: "",
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      tableLoading: true,
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
       isLoginLoading: false,
      deptOptions: [], //科室信息
      secretaryOptions: [], //党支部信息
    };
  },
  computed: {},
  created() {
  
    if(this.$route.query.educationListid){
      this.search.level=parseInt(this.$route.query.educationListid)
    }
   
     if(this.$route.query.cultureid){
      this.search.educationLevel=parseInt(this.$route.query.cultureid)
    }
       if(this.$route.query.politicsIndex){
      this.search.politicalStatus=this.$route.query.politicsIndex
    }
    
    this.getList();
    this.deptListAll();
    this.tree();
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
      let params = Object.assign(this.search, this.page)
      this.tableLoading = true;
      new Promise((response, reject) => {
        checkResult(qs.stringify(params))
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

    //获取科室
    deptListAll() {
      new Promise((response, reject) => {
        deptListAll()
          .then((response) => {
            if (response.data.code == 0) {
              this.deptOptions = response.data.data;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    //党支部书记列表
    tree() {
      let params = this.page
      new Promise((response, reject) => {
        tree(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.secretaryOptions = response.data.data.children;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    //获取选中数据
    changeFun(val) {
      let list = val;
      this.useridList.length = 0;
      this.userId = "";
      list.forEach((item) => {
        that.useridList.push(item.id);
      });

      that.userId = that.useridList.toString();
    },

    //获取下拉选择框数据
    currentSel(selVal) {
      this.search.status = selVal;
    },
    //获取员工类型数据
    personnel(val) {
      this.search.personType = val;
    },
    result(val) {
      console.log(val)
    },
    //编辑下拉框
    modification(val) {
      this.modificationForm.personType = val;
    },

    //重置按钮
    reset() {
      (this.search.userName = ""),
        (this.search.userId = ""),
        (this.search.status = ""),
        (this.search.personType = ""),
        (this.condition = ""),
        (this.classify = "");
      let params = this.page;
      params.userName = this.search.userName;
      params.userId = this.search.userId;
      params.status = this.search.status;
      params.personType = this.search.personType;
      new Promise((response, reject) => {
        list(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
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
            this.tableLoading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //导出
    exportPerson(){
    let params =this.search;
    this.isLoginLoading=true;
     new Promise((response, reject) => {
        exportCheckResult(qs.stringify(params))
          .then((response) => {
            fileDownload(response.data,'统计列表.xls')
            this.isLoginLoading=false;
          })
          .catch((error) => {
            reject(error);
          });
      });
   
    },
    //列表详情
    deleteUser(row) {
      if (row.personType == 1) {
      
        let url= this.$router.resolve({
          path: "/ydyf/resultNonclinical",
          query: { userId: row.userId },
        });
   
         window.open(url.href, '_blank');
      } else {
  
        let url= this.$router.resolve({
          path: "/ydyf/resultclinicalForm",
          query: { userId: row.userId },
        });
    
         window.open(url.href, '_blank');
      }
    },

    //选择临床非临床
    changeRoleType(val) {
      this.page.pageNum = 1;
      this.page.pageSize = 10;
      this.roleType = val;
      this.getList();
    },
    //文化程度
    Culture(val) {
      this.educationList = val.toString();
      this.search.educationLevel = this.educationList;
    },
    department(val) {
 
      this.search.departmentId = val.toString();
    },
    //选择职称
    professiona(val) {
     
    },
    //选择党支部
    handleChange(val) {

      if (val.length > 0) {
        this.search.branchId = val[val.length - 1];
      }
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
.role-type {
  display: block;
  text-align: center;
  padding-bottom: 15px;
}
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
