<template>
  <div class="personal">
    <el-row v-if="isData==2" class="no-data">
      <i class="el-icon-warning-outline"></i>
      <p>暂未生成个人评估报告</p>
    </el-row>
    <el-row v-else>
      <el-col class="title">武汉市精神卫生中心360°考核系统{{detailData.year}}年第{{detailData.month}}季度个人评估报告</el-col>

      <el-col class="top">
        <el-col
          :span="24"
          class="on-line-top all"
        >
          <h4>{{detailData.username}}<span>{{detailData.departmentname}}（{{detailData.stationname}}）</span></h4>
          <h5><span>总分</span>{{detailData.totalscore}}</h5>
        </el-col>
        <el-col
          :span="8"
          class="half"
        >
          <p>基础得分</p>
          <h5>{{detailData.basicscore}}<span>分</span></h5>
        </el-col>
        <el-col
          :span="8"
          class="half"
        >
          <p>关键得分</p>
          <h5>{{detailData.keyscore}}<span>分</span></h5>
        </el-col>
        <el-col
          :span="8"
          class="half"
        >
          <p>整体平均分</p>
          <h5>{{detailData.avgscore}}<span>分</span></h5>
        </el-col>
        <!-- <el-col
          :span="6"
          class="half"
        >
          <p>评分进度</p>
          <h5 style="color:#409EFF;">{{detailData.plan}}<span>%</span></h5>
        </el-col> -->
      </el-col>

      <el-col
        :span="24"
        class="list-bg"
      >
        <el-col class="list">
          <el-col class="line">总得分<span class="count">{{detailData.totalscore}}</span></el-col>
          <el-col class="on-line-top message">
            <span  v-if="detailData.totalcomparelast >= 0">较上一月份提升<span class="green">{{detailData.totalcomparelast}}%</span></span>
            <span  v-else>较上一月份差距<span class="red">{{Math.abs(detailData.totalcomparelast)}}%</span></span>
            <span v-if="detailData.totalcomparemark >= 0">较整体平均分超越<span class="green">{{detailData.totalcomparemark}}%</span></span>
            <span v-else>较整体平均分差距<span class="red">{{Math.abs(detailData.totalcomparemark)}}%</span></span>
          </el-col>
        </el-col>
        <el-col class="list">
          <el-col class="line">最高分<span class="count">{{detailData.maxscore}}</span></el-col>
          <el-col class="on-line-top message">
            <span v-if="detailData.maxcomparelast >= 0">较上一月份提升<span class="green">{{detailData.maxcomparelast}}%</span> </span>
            <span v-else>较上一月份差距<span class="red">{{Math.abs(detailData.maxcomparelast)}}%</span></span>
            <span v-if="detailData.maxcomparemark >= 0">较最高平均分超越<span class="green">{{detailData.maxcomparemark}}%</span></span>
            <span v-else>较最高平均分差距<span class="red">{{Math.abs(detailData.maxcomparemark)}}%</span></span>
          </el-col>
        </el-col>
        <el-col class="list">
          <el-col class="line">最低分<span class="count">{{detailData.minscore}}</span></el-col>
          <el-col class="on-line-top message">
            <span v-if="detailData.mincomparelast >= 0">较上一月份提升<span class="green">{{detailData.mincomparelast}}%</span></span>
            <span v-else>较上一月份差距<span class="green">{{Math.abs(detailData.mincomparelast)}}%</span> </span>
            <span v-if="detailData.mincomparemark >= 0">较最低平均分超越<span class="green">{{detailData.mincomparemark}}%</span></span>
            <span v-else>较最低平均分差距<span class="red">{{Math.abs(detailData.mincomparemark)}}%</span></span>
          </el-col>
        </el-col>
      </el-col>
      <el-col class="charts">
        <div id="allChart"></div>
      </el-col>
      <el-col
        :span="24"
        class="list-bg"
      >
        <el-col class="list" v-for="(item,index) in reportsData" :key="index">
          <el-col class="small-title"><span>{{item.resultreportname}}</span>
            <el-button
              type="primary"
              @click="next(item)"
            >查看详情</el-button>
          </el-col>
          <el-col class="line">个人得分<span>{{item.score}}</span></el-col>
          <el-col class="line">平均分<span>{{item.avgscore}}</span></el-col>
          <!-- <el-col class="line">A类评分人<span>{{item.ascore}}</span></el-col>
          <el-col class="line">B类评分人<span>{{item.bscore}}</span></el-col>
          <el-col class="line">C类评分人<span>{{item.cscore}}</span></el-col>
          <el-col class="line">D类评分人<span>{{item.dscore}}</span></el-col> -->
        </el-col>
      </el-col>
      <el-col class="charts">
        <div id="twoChart"></div>
      </el-col>

    </el-row>
  </div>
</template>

<script>
import { evaluationReport } from "@/api/mobile/check";
import qs from "qs";
export default {
  data() {
    return {
      detailData: {},
      reportsData: [],
      isData: 1,
      type: 0,
      series: [],
    };
  },
  created() {
    this.getDetail();
  },
  methods: {
    next(row) {
      this.$router.push({
        path: "/webShowQuestion",
        query:  {"usercode":this.detailData.usercode,"id":row.id,'resultreportname':row.resultreportname}
      });
    },
    getDetail() {
      var toast = this.$toast.loading({
        mask: true,
        message: '加载中...',
        duration:0
      });
      let data = {};
      data.usercode = this.$store.state.user.user.usercode;
      data.isvalidation = 1;
      data.dbtype = this.$store.state.user.user.dbtype
      new Promise((response, reject) => {
        evaluationReport(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.detailData = response.data.data.evaluationReport;
              this.reportsData = response.data.data.reports;
              this.isData = 1;
              this.drawLine();
              this.drawLine2();
            } else if (response.data.code == 2) {
              this.isData = 2;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
            toast.clear();
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      let allChart = this.$echarts.init(document.getElementById("allChart"));
      // 绘制图表
      allChart.setOption({
        color: ["#409eff"],
        title: {
          top: 0,
          left: 0,
          subtext: "综合分析表",
          subtextStyle: {
            color: "#444",
            fontSize: 16,
            fontWeight: 600
          }
        },
        tooltip: {
          show: true
        },
        calculable: true,
        polar: [
          {
            indicator: [
              { text: "总得分", max: 100 },
              { text: "基础得分", max: 20 },
              { text: "关键得分", max: 80 },
              { text: "整体平均分", max: 100 }
            ],
            radius: 90,
            center: ["50%", "54%"]
          }
        ],
        series: [
          {
            name: "",
            type: "radar",
            itemStyle: {
              normal: {
                areaStyle: {
                  type: "default"
                }
              }
            },
            data: [
              {
                value: [this.detailData.totalscore,
                  this.detailData.basicscore,
                  this.detailData.keyscore,
                  this.detailData.avgscore],
                name: "综合分析"
              }
            ]
          }
        ]
      });
    },
    drawLine2() {
      if (this.type == 300) {
        this.series = [
          {
            name: "个人得分",
            type: "bar",
            data: [this.reportsData[0].score, this.reportsData[1].score]
          },
          {
            name: "平均分",
            type: "bar",
            data: [this.reportsData[0].avgscore, this.reportsData[1].avgscore]
          }
        ];
      } else {
        this.series = [
          {
            name: "A类评分人",
            type: "bar",
            data: [this.reportsData[0].ascore, this.reportsData[1].ascore]
          },
          {
            name: "B类评分人",
            type: "bar",
            data: [this.reportsData[0].bscore, this.reportsData[1].bscore]
          },
          {
            name: "C类评分人",
            type: "bar",
            data: [this.reportsData[0].cscore, this.reportsData[1].cscore]
          },
          {
            name: "D类评分人",
            type: "bar",
            data: [this.reportsData[0].dscore, this.reportsData[1].dscore]
          },
          {
            name: "个人得分",
            type: "bar",
            data: [this.reportsData[0].score, this.reportsData[1].score]
          },
          {
            name: "平均分",
            type: "bar",
            data: [this.reportsData[0].avgscore, this.reportsData[1].avgscore]
          }
        ];
      }
      if (this.reportsData.length == 0) {
        return;
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
        series: this.series
      });
    }
  },
  mounted() {

  }
};
</script>


<style lang="scss" scoped>
.personal {
  .no-data{
    position: absolute;
    width: 100%;
    height: 100%;
    top: -50px;
    left: 0px;
    background: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    i{
      font-size: 5rem;
      color: #dedede;
    }
    p{
      font-size: 0.8rem;
      margin-top: 1rem;
      color: #b4b4b4;
    }
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
  .title {
    background: #fff;
    text-align: center;
    font-weight: bold;
    font-size: 1rem;
    color: #409EFF;
    padding:.5rem 1rem;
  }
  .top {
    background: #fff;
    position: relative;
    text-align: center;
    padding: 10px 0px;
    .all {
      margin-bottom: 4px;
      h4 {
        span {
          color: #c1bfbf;
          margin-left: 4px;
        }
      }
      h5 {
        font-size: 1.6rem;
        span {
          font-size: 0.5rem;
          margin-right: 4px;
        }
      }
    }
    .half {
      p {
        color: #bbbbbb;
        font-size: 0.8rem;
      }
      h5 {
        font-size: 1.2rem;
        span {
          font-size: 0.6rem;
          margin-left: 4px;
        }
      }
    }
  }
  .list-bg {
    background: #fff;
    margin: 10px 0px 0px 0px;
    .list {
      position: relative;
      padding: 10px 0px;
      .small-title {
        background: #fafafa;
        padding: 10px 15px;
        margin-bottom: 10px;
        display: flex;
        align-items: center;
        .el-button {
          float: right;
        }
        span {
          flex: 1;
        }
      }
      .line {
        padding-left: 15px;
        font-size: 0.9rem;
        margin: 2px 0px;
        span {
          float: right;
          padding-right: 15px;
        }
        .count {
          font-size: 1.2rem;
        }
      }

      .message {
        padding: 0px 15px;
        font-size: 0.8rem;
        color: #bbbbbb;
      }
      .red {
        color: red;
      }
      .green {
        color: green;
      }
    }
  }
  .charts {
    background: #fff;
    margin-top: 10px;
  }
  #allChart {
    width: 100%;
    height: 260px;
  }
  #twoChart {
    width: 100%;
    height: 260px;
  }
}
</style>
