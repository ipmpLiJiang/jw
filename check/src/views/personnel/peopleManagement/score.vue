<template>
  <div>
    <h4 class="title">人员分数</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="6">
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
          <el-col :span="6">
            <el-form-item label="部门名称">
              <deptList ref="searchSection"></deptList>
            </el-form-item>
          </el-col>
          <el-col :span="6">
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
          <el-col :span="6">
            <el-form-item label="月份">
              <el-select
                v-model="search.month"
                clearable
                placeholder="请选择"
                style="width:100%"
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
          <el-col :span="6">
            <el-form-item label="分数">
              <el-slider
                v-model="search.score"
                range
                show-stops
                :max="100"
              >
              </el-slider>
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
              ><i class="el-icon-search"></i>搜索</el-button>
              <el-button
                type="info"
                @click="exportPerson"
              >导出人员分数</el-button>
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
          type="index"
          width="50"
          :index="indexMethod"
        >
        </el-table-column>
        <el-table-column
          prop="username"
          label="用户姓名"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="moneycard"
          label="发薪号"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="departmentname"
          label="部门"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="score"
          label="分数"
          show-overflow-tooltip
        >
        </el-table-column>
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
        <el-table-column
          prop="notes"
          label="备注"
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
import { getScoreList } from "@/api/personnel/user";
import qs from "qs";
//导入ip地址
import deptList from "../common/deptList";
export default {
  data() {
    return {
      search: {
        username: "",
        departmentname: "",
        year: "",
        month: "",
        score: [0, 100]
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableLoading: true,
      quarterOptions: this.common.seasonOptions()
    };
  },
  components: {
    deptList
  },
  mounted() {},
  created() {
    this.getList();
  },
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
      let params = this.page;
      params.username = this.search.username;
      if (this.$refs.searchSection) {
        params.departmentname = this.$refs.searchSection.value;
      } else {
        params.departmentname = "";
      }
      params.year = this.search.year;
      params.month = this.search.month;
      params.score1 = this.search.score[0];
      params.score2 = this.search.score[1];
      new Promise((response, reject) => {
        getScoreList(qs.stringify(params))
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
    //导出人员信息
    exportPerson() {
      let data = {
        username: this.search.username,
        year: this.search.year,
        month: this.search.month,
        score1: this.search.score[0],
        score2: this.search.score[1]
      };
      if (this.$refs.searchSection) {
        data.departmentname = this.$refs.searchSection.value;
      } else {
        data.departmentname = "";
      }
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        "personnel/exportScore?info=" +
        JSON.stringify(data);
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
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
