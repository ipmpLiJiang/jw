<template>
  <div class="question-container">
    <div class="question-content">
      <div class="question-list">
        <el-row class="edit">
          <el-col
            :span="24"
            class="right"
          >
            <el-col class="title">
              {{title}}
              <span>的分析报告</span>
            </el-col>
            <el-col
              class="list"
              v-for="(item,index) in data"
              :key="index"
            >
              <div class="title_question_all">
                <strong
                  class="topic_number"
                  v-if="item.type != 4"
                >第{{item.questionorder}}题：</strong>
                <div
                  class="jz-title"
                  v-html="item.questiontitle"
                ></div>
                <div
                  class="topic-type"
                  v-if='item.type == 1'
                >[单选题]</div>
                <div
                  class="topic-type"
                  v-if='item.type == 2'
                >[多选题]</div>
                <div
                  class="topic-type"
                  v-if='item.type == 3'
                >[填空题]</div>
              </div>
              <el-col class="people">
                <template v-if="item.type == 1">
                  <el-table
                    :data="item.optionList"
                    style="width: 100%"
                    show-summary
                    sum-text="本题有效填写人次"
                  >
                    <el-table-column
                      prop="optioncontent"
                      label="选项"
                    >
                    </el-table-column>
                    <el-table-column
                      prop="count"
                      label="小计"
                      width="180"
                    >
                    </el-table-column>
                    <el-table-column label="比例">
                      <template slot-scope="scope">
                        <el-progress :percentage="scope.row.ratio"></el-progress>
                      </template>
                    </el-table-column>
                    <el-table-column label="详情">
                      <template slot-scope="scope">
                        <el-button
                          type="primary"
                          v-if="scope.row.gap == 1"
                          @click="openSelectList(scope.row)"
                        >填空详情</el-button>
                        <el-button
                          type="info"
                          disabled
                          v-else
                        >无填空</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </template>
                <template v-if="item.type == 2">
                  <el-table
                    :data="item.optionList"
                    style="width: 100%"
                  >
                    <el-table-column
                      prop="optioncontent"
                      label="选项"
                    >
                    </el-table-column>
                    <el-table-column
                      prop="count"
                      label="小计"
                      width="180"
                    >
                    </el-table-column>
                    <el-table-column label="比例">
                      <template slot-scope="scope">
                        <el-progress :percentage="scope.row.ratio"></el-progress>
                      </template>
                    </el-table-column>
                    <el-table-column label="详情">
                      <template slot-scope="scope">
                        <el-button
                          type="primary"
                          v-if="scope.row.gap == 1"
                          @click="openSelectList(scope.row)"
                        >填空详情</el-button>
                        <el-button
                          type="info"
                          disabled
                          v-else
                        >无填空</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <div class="total-box">
                    <div class="tb-title">本题有效填写人次</div>
                    <div class="count">{{item.count}}</div>
                    <div
                      class="formula"
                      @click="dialogVisible = true"
                    >查看多选题百分比计算方法</div>
                  </div>
                </template>
                <template v-if="item.type == 3">
                  <el-button
                    type="primary"
                    plain
                    @click="openFillList(item.id,item.questiontitle)"
                  >查看详情</el-button>
                </template>
              </el-col>
            </el-col>
          </el-col>
        </el-row>
      </div>
    </div>
    <!-- 多选题百分比计算公式 -->
    <el-dialog
      title="多选题百分比计算方法"
      :visible.sync="dialogVisible"
      width="30%"
      center
    >
      <p class="p-formula">1、多选题选项百分比＝该选项被选择次数÷有效答卷份数；</p>
      <p class="p-formula">2、含义为选择该选项的人次在所有填写人数中所占的比例。</p>
      <p class="p-formula">所以对于多选题百分比相加可能超过百分之一百。</p>
      <p class="p-formula">举例说明：有10个填写了一道多选题，其中6个人选择了A，5个人选择了B，3个人选择了C。 则选择A的比例是60%，选择B的是50%，选择C的是30%。3个百分比相加为140%。</p>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="dialogVisible = false"
        >确 定</el-button>
      </span>
    </el-dialog>
    <!-- 填空题详情 -->
    <el-dialog
      title="填空题详情列表"
      :visible.sync="fillDialogVisible"
      width="50%"
      center
      v-loading="fillLoading"
      element-loading-text="拼命加载中"
    >
      <el-table :data="fillData">
        <el-table-column
          property="content"
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
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="fillDialogVisible = false"
        >确 定</el-button>
      </span>
    </el-dialog>
    <!-- 多选单选填空详情 -->
    <el-dialog
      title="填空详情列表"
      :visible.sync="selectDialogVisible"
      width="50%"
      center
      v-loading="selectLoading"
      element-loading-text="拼命加载中"
    >
      <SelectFillList ref="selectFill"></SelectFillList>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="selectDialogVisible = false"
        >确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import SelectFillList from "./analyze/selectFillList";
import { getAnswerDetail, getPackList } from "@/api/questionnaire/index";
import * as math from "mathjs";
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
      selectDialogVisible: false,
      selectLoading: false,
    };
  },
  components: {
    SelectFillList,
  },
  mounted() {},
  created() {
    this.getDetail();
  },

  methods: {
    link() {
      this.dialogTableVisible = true;
    },
    getDetail() {
      let data = {
        surveyinfoid: this.$route.query.id,
      };
      new Promise((response, reject) => {
        getAnswerDetail(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              this.data = response.data.data;
              //处理精度问题
              for(let i=0; i < this.data.length; i++){
                for(let j=0; j<this.data[i]['optionList'].length; j++){
                  this.data[i]['optionList'][j]['ratio'] = this.printFn(parseFloat(this.data[i]['optionList'][j]['ratio']) * 100);
                }
              }
              this.title = response.data.title;
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
      params.questionid = this.fillId;
      this.fillLoading = true;
      new Promise((response, reject) => {
        getPackList(qs.stringify(params))
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
    //打开填空题弹出框
    openFillList(id, title) {
      this.fillTitle = title;
      if (this.fillId != id) {
        this.fillId = id;
        this.into();
        this.getList();
      }
      this.fillDialogVisible = true;
    },
    //打开选择填空题弹出框
    openSelectList(row) {
      this.selectDialogVisible = true;
      this.$nextTick(() => {
        if (this.$refs.selectFill.fillId != row.id) {
          this.$refs.selectFill.fillId = row.id;
          this.$refs.selectFill.into();
          this.$refs.selectFill.getList();
        }
      });
    },
    //解决精度方法
    printFn(value) {
      const precision = 14;
      return Number(math.format(value, precision));
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
