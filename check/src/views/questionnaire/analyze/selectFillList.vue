<template>
  <div class="question-container">
    <el-table :data="fillData">
      <el-table-column
        property="gaptext"
        label="填写内容"
      ></el-table-column>
      <el-table-column
        property="submittime"
        label="填写时间"
      ></el-table-column>
    </el-table>
    <div style="text-align: center;padding-top:15px;">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.pageNum"
        :page-sizes="[5, 10]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
</template>
<script>
import { getOptionPackList } from "@/api/questionnaire/index";
import qs from "qs";
export default {
  data() {
    return {
      data: [],
      title: "",
      dialogVisible: false,
      fillDialogVisible: false,
      page: {
        pageNum: 1,
        pageSize: 5,
      },
      total: 0,
      fillData: [],
      fillLoading: false,
      fillId: "",
      fillTitle: "",
    };
  },
  components: {
  },
  mounted() {},
  created() {
  },

  methods: {
    link() {
      this.dialogTableVisible = true;
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
    //初始化
    into() {
      this.page.pageNum = 1;
      this.page.pageSize = 10;
    },
    //搜索
    searchList() {
      this.into();
      this.getList();
    },
    //查询填空题详情列表
    getList() {
      let params = this.page;
      params.optionid = this.fillId;
      this.fillLoading = true;
      new Promise((response, reject) => {
        getOptionPackList(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.fillData = response.data.data;
              this.total = response.data.totalPages;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.fillLoading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.question-content {
  width: 920px;
  margin: 0 auto;
  font-size: 0;
}
.question-header {
  width: 920px;
  font-size: 0;
}
.question-list {
  box-shadow: 0px 1px 6px 0px rgba(205, 220, 245, 1);
  width: 920px;
  font-size: 14px;
  background: #fff;
  margin: 20px;
}
.edit {
  margin-top: 10px;
  border-radius: 2px;
  border: 1px solid rgba(217, 217, 217, 1);
  width: 920px;
  padding-bottom: 5px;
  margin: 0;
  overflow: auto;
  background: #ffffff;
  box-shadow: 0px 2px 5px 0px #d9d9d9;
  position: relative;
  .title {
    text-align: center;
    font-size: 22px;
    font-weight: bold;
    padding: 50px 0 100px 0;
    color: #409EFF;
  }
  .right {
    background: #fff;
    padding: 30px 70px;
    border-radius: 5px;
    .list {
      position: relative;
      padding-bottom: 10px;
      padding-top: 20px;
      border-bottom: 1px solid #e7e7e7;
      .people {
        margin: 10px 0;
      }
    }
  }
}
.topic_number {
  width: 70px;
  display: inline-block;
  color: #676767;
  font-size: 16px;
}
.jz-title {
  display: inline-block;
  vertical-align: top;
  font-weight: bold;
}
.title_question_all {
  padding-top: 2px;
  font-size: 15px;
  color: #444444;
  font-weight: bold;
  height: auto;
  line-height: 20px;
  position: relative;
}
.topic-type {
  display: inline-block;
  color: #a7a7a7;
}
.total-box {
  line-height: 49px;
  display: flex;
  justify-content: space-between;
  background-color: #f5f7fa;
  padding: 0 11px;
  color: #606266;
  .tb-title {
    width: 40%;
  }
  .count {
    width: 20%;
    margin-left: -7px;
  }
  .formula {
    width: 40%;
    color: #1ea0fa;
    cursor: pointer;
    padding-left: 23px;
    &:hover {
      color: #0284de;
      transition: all 0.3s;
    }
  }
}
.p-formula {
  font-size: 15px;
  color: #666;
  line-height: 26px;
  margin-top: 8px;
}
</style>
