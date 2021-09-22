<template>
  <div>
    <h4 class="title">部门考核完成情况</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
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
            <el-form-item label="评分季度">
              <el-select
                v-model="search.month"
                clearable
                placeholder="请选择"
                style="width:100%"
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
              <el-button
                style=""
                type="primary"
                @click="warnUser"
              ><i class="el-icon-chat-line-round"></i>一键短信提醒用户</el-button>
            </el-form-item>
            <el-form-item class="notice">
              <span>友情提示：搜索框的年份和季度都为空就是查询全部数据</span>
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
        <el-table-column
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
        </el-table-column>
        <el-table-column
          prop="complete"
          label="完成情况"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span
              v-if="scope.row.complete == '1'"
              style="color:#409EFF;"
            ><i class="el-icon-check"></i></span>
            <span
              v-else
              style="color:#F56C6C;"
            ><i class="el-icon-close"></i></span>
          </template>
        </el-table-column>
        <el-table-column
          prop="year"
          label="年份"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="month"
          label="季度"
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
    <!-- 短信提醒用户 -->
    <el-dialog
      title="友情提示"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <span class="msg-warn">在选择发送短信前必须先设置好短信模板内容才可以发送</span>
      <el-form
        label-width="80px"
        show-overflow-tooltip="true"
      >
        <el-col :span="24">
          <el-form-item label="年份">
            <el-date-picker
              v-model="warn.year"
              type="year"
              placeholder="选择年"
              value-format="yyyy"
              style="width:100%"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="季度">
            <el-select
              v-model="warn.month"
              clearable
              placeholder="请选择"
              style="width:100%"
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
      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="warnList"
        >确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { deptCompleteList } from "@/api/personnel/index";
import deptList from "./common/deptList";
import qs from "qs";
export default {
  data() {
    return {
      quarterOptions: this.common.seasonOptions(),
      title: "",
      search: {
        month: "",
        year: "",
        deptname: ""
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableLoading: true,
      searchLoading: false,
      warn: {
        year: "",
        month: ""
      },
      dialogVisible: false
    };
  },
  mounted() {},
  created() {
    this.getList();
  },
  components: {
    deptList
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
      params.month = this.search.month;
      params.year = this.search.year;
      params.deptname = this.search.deptname;
      if (this.$refs.searchSection) {
        params.deptname = this.$refs.searchSection.value;
      } else {
        params.deptname = "";
      }
      this.searchLoading = true;
      new Promise((response, reject) => {
        deptCompleteList(qs.stringify(params))
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
        this.$message.warning("请选择季度");
        return;
      }
      this.$router.push({
        path: "/personnel/warnList",
        query: { year: this.warn.year, month: this.warn.month }
      });
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
.notice {
  position: absolute;
  right: 20px;
  bottom: 12px;
}
.msg-warn {
  color: #f00;
  font-size: 16px;
  display: block;
  text-align: center;
  margin-bottom: 15px;
  margin-top: -20px;
}
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>
