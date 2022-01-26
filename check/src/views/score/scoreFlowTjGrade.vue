<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>测评打分情况统计表
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
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
              <!-- <el-button
                type="warning"
                @click="exportExcel"
              >导出excel</el-button> -->
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
          prop="num"
          label="序号"
          show-overflow-tooltip
          width="90"
        >
        </el-table-column>
        <el-table-column
          prop="scoreProj"
          label="被考核对象"
          show-overflow-tooltip
          width="140"
        >
        </el-table-column>
        <el-table-column
          prop="khrs"
          label="被考核人数(人)"
          show-overflow-tooltip
          width="150"
        >
        </el-table-column>
        <el-table-column label="应参与 / 实际参与测评打分人数(人)">
          <el-table-column
            prop="ycyABCrs"
            label="院领导"
            width="140">
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.num!=4 && scope.row.ycyABCrs!=0"
                  @click="gradeFlowDetail(scope.row,'ABC')"
                  type="text"
                  size="small"
                >{{scope.row.ycyABCrs}}&nbsp;/&nbsp;{{scope.row.sjcyABCrs}}</el-button>
                <p v-else>{{scope.row.ycyABCrs}}&nbsp;/&nbsp;{{scope.row.sjcyABCrs}}</p>
              </template>
          </el-table-column>
          <el-table-column
            prop="ycyEFrs"
            label="中层干部"
            width="140">
            <template slot-scope="scope">
                <el-button
                  v-if="scope.row.num!=4 && scope.row.ycyEFrs!=0"
                  @click="gradeFlowDetail(scope.row,'EF')"
                  type="text"
                  size="small"
                >{{scope.row.ycyEFrs}}&nbsp;/&nbsp;{{scope.row.sjcyEFrs}}</el-button>
                <p v-else>{{scope.row.ycyEFrs}}&nbsp;/&nbsp;{{scope.row.sjcyEFrs}}</p>
              </template>
          </el-table-column>
          <el-table-column
            prop="ycyDrs"
            label="员工"
            width="140">
            <template slot-scope="scope">
                <el-button
                  v-if="scope.row.num!=4 && scope.row.ycyDrs!=0"
                  @click="gradeFlowDetail(scope.row,'D')"
                  type="text"
                  size="small"
                >{{scope.row.ycyDrs}}&nbsp;/&nbsp;{{scope.row.sjcyDrs}}</el-button>
                <p v-else>{{scope.row.ycyDrs}}&nbsp;/&nbsp;{{scope.row.sjcyDrs}}</p>
              </template>
          </el-table-column>
        </el-table-column>
        <el-table-column
          label="导出"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-button
              @click="exportExcel(scope.row)"
              type="text"
              size="small"
              v-if="scope.row.num!=4"
            >导出excel</el-button>
            <p v-else>&nbsp;</p>
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
    <UserFlowDetailList
      @childClose="childClose"
      @childGetList="getList"
      :uslDialogVisible="uslDialogVisible"
      :form="data"
    ></UserFlowDetailList>
  </div>
</template>

<script>
import { getFlowTjList } from "@/api/score/score";
import UserFlowDetailList from "../common/userFlowDetailList";
import qs from "qs";
export default {
  data() {
    return {
      quarterOptions: this.common.seasonOptions(),
      title: "",
      search: {
        month: "",
        year: "",
        postType: ""
      },
      tableData: [],
      uslDialogVisible: false,
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
      dbtype: this.$store.state.user.user.dbtype,
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      data: {},
      total: 0,
      tableLoading: true,
      forms: {},
      searchLoading: false
    };
  },
  components: {
    UserFlowDetailList
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
    gradeFlowDetail(row,type){
      this.data.dbtype = this.dbtype;
      this.data.postType = row.posttype
      this.data.scoreType = type
      this.data.scoreProj = row.scoreProj
      this.uslDialogVisible = true
    },
    childClose(val) {
      this.uslDialogVisible = val;
    },
    //搜索
    searchList() {
      this.into();
      this.getList();
    },
    //查询列表
    getList() {
      let params = this.page;
      this.searchLoading = true;
      params.dbtype = this.dbtype;
      new Promise((response, reject) => {
        getFlowTjList(qs.stringify(params))
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
    //导出
    exportExcel(item) {
      let info = this.search;
      info.dbtype = this.dbtype;
      info.postType = item.posttype
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        // "http://localhost:8080/" +
        "scoreflow/export?info=" +
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
    // margin-top: 15px;
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
