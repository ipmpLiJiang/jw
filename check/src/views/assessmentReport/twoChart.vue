<template>
  <div class="main-content">
    <div class="contianer">
      <h4 class="bd-title">{{$route.query.resultreportname}}&nbsp;&nbsp;&nbsp;(分数计算公式：类型评分相加总数/人数*系数)</h4>
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column
          type="index"
          label="序号"
        >
        </el-table-column>
        <el-table-column
          prop="dutyname"
          label="题目"
          width="380"
        >
          <template slot-scope="scope">
            <span v-html="scope.row.dutyname"></span>
          </template>
        </el-table-column>
        <el-table-column
          prop="ascore"
          label="A类评分"
          v-if="type == 50 || type == 100"
        >
          <template slot-scope="scope">
            <el-button
              @click="scorerList(scope.row,'A')"
              type="text"
              size="small"
              v-if="type==50"
            >{{scope.row.ascore}}</el-button>
            <el-button
              type="text"
              size="small"
              v-else
            >{{scope.row.ascore}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="bscore"
          label="B类评分"
          v-if="type == 50 || type == 100"
        >
          <template slot-scope="scope">
            <el-button
              @click="scorerList(scope.row,'B')"
              type="text"
              size="small"
              v-if="type==50"
            >{{scope.row.bscore}}</el-button>
            <el-button
              type="text"
              size="small"
              v-else
            >{{scope.row.bscore}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="cscore"
          label="C类评分"
          v-if="type == 50 || type == 100"
        >
          <template slot-scope="scope">
            <el-button
              @click="scorerList(scope.row,'C')"
              type="text"
              size="small"
              v-if="type==50"
            >{{scope.row.cscore}}</el-button>
            <el-button
              type="text"
              size="small"
              v-else
            >{{scope.row.cscore}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="dscore"
          label="D类评分"
          v-if="type == 50 || type == 100"
        >
          <template slot-scope="scope">
            <el-button
              @click="scorerList(scope.row,'D')"
              type="text"
              size="small"
              v-if="type==50"
            >{{scope.row.dscore}}</el-button>
            <el-button
              type="text"
              size="small"
              v-else
            >{{scope.row.dscore}}</el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="score"
          label="个人得分"
        >
        </el-table-column>
        <el-table-column
          prop="avgscore"
          label="平均分"
        >
        </el-table-column>
      </el-table>
      <div
        class="bg"
        id="chart2"
      ></div>
    </div>
  </div>
</template>

<script>
import { getSingleTotalScore } from "@/api/assessmentReport/index";
import qs from "qs";
export default {
  data() {
    return {
      tableData: [],
      type: 0
    };
  },
  created() {
    this.getDetail();
    this.type = this.$store.state.user.user.rolecode;
  },
  methods: {
    drawLine() {
      let serive = [];
      let aveScore = [];
      let allScore = [];
      let aScore = [];
      let bScore = [];
      let cScore = [];
      let dScore = [];
      let topic = [];
      for (let i = 0; i < this.tableData.length; i++) {
        allScore.push(this.tableData[i]["score"]);
        aveScore.push(this.tableData[i]["avgscore"]);
        aScore.push(this.tableData[i]["ascore"]);
        bScore.push(this.tableData[i]["bscore"]);
        cScore.push(this.tableData[i]["cscore"]);
        dScore.push(this.tableData[i]["dscore"]);
        topic.push(this.tableData[i]["dutyname"]);
      }
      if (this.type == 300) {
        serive = [
          {
            name: "总分",
            type: "bar",
            data: allScore
          },
          {
            name: "平均得分",
            type: "bar",
            data: aveScore
          }
        ];
      } else {
        serive = [
          {
            name: "A类评分",
            type: "bar",
            data: aScore
          },
          {
            name: "B类评分",
            type: "bar",
            data: bScore
          },
          {
            name: "C类评分",
            type: "bar",
            data: cScore
          },
          {
            name: "D类评分",
            type: "bar",
            data: dScore
          },
          {
            name: "总分",
            type: "bar",
            data: allScore
          },
          {
            name: "平均得分",
            type: "bar",
            data: aveScore
          }
        ];
      }
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.init(document.getElementById("chart2"));
      // 绘制图表
      myChart.setOption({
        color: ["#409EFF", "#F56C6C"],
        title: {
          text: ""
        },
        tooltip: {
          trigger: "axis"
        },
        xAxis: [
          {
            type: "category",
            axisLabel: {
              formatter: function(value, index) {
                // 10 6 这些你自定义就行
                var v = value.substring(0, 6) + "...";
                return value.length > 10 ? v : value;
              }
            },
            data: topic
          }
        ],
        yAxis: [
          {
            type: "value",
            color: "#f00",
            axisTick: {
              show: false
            },
            axisLine: {
              show: false
            },
            axisLabel: {
              textStyle: {
                color: "#999"
              }
            }
          }
        ],
        series: serive
      });
    },
    getDetail() {
      let data = {
        usercode: this.$route.query.usercode,
        id: this.$route.query.id,
        dbtype: this.$store.state.user.user.dbtype 
      };

      if (this.$route.query.year) {
        data.year = this.$route.query.year;
      } else {
        data.year = "";
      }
      if (this.$route.query.month) {
        data.month = this.$route.query.month;
      } else {
        data.month = "";
      }
      new Promise((response, reject) => {
        getSingleTotalScore(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.drawLine();
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
    scorerList(row, type) {
      this.$router.push({
        path: "/home/scorerList",
        query: {
          usercode: this.$route.query.usercode,
          id: row.id,
          type: type,
          year: this.$route.query.year,
          month: this.$route.query.month
        }
      });
    }
  },
  mounted() {}
};
</script>

<style lang="scss" scoped>
.main-content {
  background: #fff;
  padding: 20px;
}
.contianer {
  margin-bottom: 20px;
}
#chart2,
#chart3 {
  width: 100%;
  height: 350px;
  border: 1px solid #f1f1f1;
  margin-top: 15px;
}
.bd-title {
  color: #409eff;
  position: relative;
  font-weight: bold;
  padding-left: 10px;
  line-height: 1;
  text-align: left;
  margin-bottom: 20px;
  &::after {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    width: 4px;
    background-color: #30a6f5;
    border-radius: 2px;
  }
}
</style>

