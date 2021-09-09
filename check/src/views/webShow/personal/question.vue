<template>
  <div class="personal">
    <el-row>
      <el-col class="title">{{$route.query.resultreportname}}</el-col>
      <el-col
        :span="24"
        class="list-bg"
      >
        <el-col class="list" v-for="(item,index) in tableData" :key="index">
          <el-col class="on-line-top message">{{item.dutyname}}</el-col>
          <!-- <el-col class="line">A类评分<span>{{item.ascore}}</span></el-col>
          <el-col class="line">B类评分<span>{{item.bscore}}</span></el-col>
          <el-col class="line">C类评分<span>{{item.cscore}}</span></el-col>
          <el-col class="line">D类评分<span>{{item.dscore}}</span></el-col> -->
          <el-col class="line">个人得分<span>{{item.score}}</span></el-col>
          <el-col class="line">总体平均分<span>{{item.avgscore}}</span></el-col>
        </el-col>
      </el-col>

      <el-col class="charts">
        <!-- <div id="twoChart"></div> -->
      </el-col>

    </el-row>
  </div>
</template>

<script>
import { mobileGetSingleTotalScore } from "@/api/mobile/check";
import qs from "qs";
export default {
  data() {
    return {
      tableData: [],
      type: 0
    };
  },
  created() {
    this.type = this.$route.query.rolecode;
    this.getDetail();
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
      if (this.type == 300 || this.type == 200) {
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
      var labelOption = {
        normal: {
          show: true,
          position: "insideBottom",
          distance: 3,
          align: "left",
          verticalAlign: "middle",
          rotate: 90,
          formatter: "{c}  {name|{a}}",
          fontSize: 16,
          rich: {
            name: {
              textBorderColor: "#fff"
            }
          }
        }
      };
      // 基于准备好的dom，初始化echarts实例
      let twoChart = this.$echarts.init(document.getElementById("twoChart"));
      // 绘制图表
      twoChart.setOption({
        color: ["#4cabce", "#e5323e", "#f00220", "#003366", "#006699"],
        tooltip: {
          trigger: "axis"
        },
        title: {
          text: ""
        },
        xAxis: [
          {
            type: "category",
            data: ["基础评分", "关键评分"]
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
        usercode: this.$store.state.user.user.usercode,
        id: this.$route.query.id,
        isvalidation:1
      };
      new Promise((response, reject) => {
        mobileGetSingleTotalScore(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              // this.drawLine();
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
    }
  }
};
</script>


<style lang="scss" scoped>
.personal {
  .title {
    background: #fff;
    text-align: center;
    font-weight: bold;
    font-size: 1rem;
    padding: 0.5rem 0px;
    color: #409EFF;
  }
  .on-line-top::after {
    content: " ";
    position: absolute;
    pointer-events: none;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    left: 0;
    right: 0;
    top: 0;
    width: 100%;
    -webkit-transform: scaleY(0.5);
    transform: scaleY(0.5);
    border-bottom: 1px solid #ebedf0;
  }
  .list-bg {
    background: #fff;
    margin-bottom: 10px;
    .list {
      position: relative;
      padding: 10px 0px;
      .line {
        padding-left: 15px;
        font-size: 0.9rem;
        margin: 2px 0px;
        color: #bbb;
        span {
          float: right;
          padding-right: 15px;
          color: #424242;
        }
      }
      .message {
        padding: 0px 15px;
        font-size: 0.9rem;
        font-weight: bold;
        margin-bottom: 5px;
      }
    }
  }
  .charts {
    background: #fff;
    margin-bottom: 10px;
  }
  #twoChart {
    width: 100%;
    height: 260px;
  }
}
</style>
