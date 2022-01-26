<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>差评评分汇总
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="4">
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
          <el-col :span="5" v-if="dbtype==2">
            <el-form-item label="所属岗位">
              <PostList
                @childSelectDepartment="getSelectStation"
                :selectedOptions="fullstationcode"
              ></PostList>
            </el-form-item>
          </el-col>
          <el-col :span="5" v-if="dbtype==1">
            <el-form-item label="所属支部">
              <BranchList
                @childSelectBranch="getSelectBranch"
                :selectedOptions="tempbranchcode"
              ></BranchList>
            </el-form-item>
          </el-col>
          <el-col :span="4" v-if="dbtype==1">
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
          <el-col :span="4">
            <el-form-item label="年份">
              <el-date-picker
                v-model="search.year"
                type="year"
                placeholder="选择年"
                value-format="yyyy"
                style="width:140px"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="4">
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
          <el-col :span="4" v-if="dbtype=='1'?false:true">
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
            {{scope.row.username}}
          </template>
        </el-table-column>
        <el-table-column
          prop="dutyTypeName"
          label="指标类型"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="dutyName"
          label="具体指标"
          show-overflow-tooltip
          width="450"
        >
          <template slot-scope="scope">
            <span v-html="scope.row.dutyName"></span>
          </template>
        </el-table-column>
        <el-table-column
          prop="scoreType"
          label="评分类型"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="cpsm"
          label="差评原因"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="scorringName"
          label="打分人姓名"
          show-overflow-tooltip
        >
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
import PostList from "../common/postList";
import BranchList from "../common/branchList";
import { getBadList } from "@/api/score/score";
import qs from "qs";
export default {
  data() {
    return {
      quarterOptions: this.common.seasonOptions(),
      tempbranchcode: [],
      title: "",
      search: {
        stationcode: "",
        username: "",
        month: "",
        year: "",
        postType: "",
        dbbk: "",
        branchcode: ''
      },
      tableData: [],
      stationcode: [""],
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
      dbtype: this.$store.state.user.user.dbtype,
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      fullstationcode: [""],
      tableLoading: true,
      forms: {},
      searchLoading: false
    };
  },
  components: {
    PostList,
    BranchList
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
    //获取支部选择
    getSelectBranch(data, row) {
      this.search.branchcode = data === undefined ? '' : data;
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
      if (this.search.postType !=null) {
        params.postType = this.search.postType
      } else {
        params.postType = "";
      }
      params.dbbk = this.search.dbbk
      params.branchcode = this.search.branchcode
      this.searchLoading = true;
      params.username = this.search.username;
      params.dbtype = this.dbtype;
      new Promise((response, reject) => {
        getBadList(qs.stringify(params))
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
      info.dbtype = this.dbtype;
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        // "http://localhost:8080/" +
        "scoreBad/export?info=" +
        JSON.stringify(info);
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
