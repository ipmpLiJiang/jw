<template>
  <div>
    <el-table
      :data="tableData"
      border
      style="width: 100%;margin-bottom:20px;"
      size="small"
    >
      <el-table-column
        prop="resultreportname"
        label="维度"
        width="180"
      >
      </el-table-column>
      <el-table-column
        prop="ascore"
        label="A类评分人"
        width="180"
      >
      </el-table-column>
      <el-table-column
        prop="bscore"
        label="B类评分人"
      >
      </el-table-column>
      <el-table-column
        prop="cscore"
        label="C类评分人"
      >
      </el-table-column>
      <el-table-column
        prop="dscore"
        label="D类评分人"
      >
      </el-table-column>
      <el-table-column
        prop="escore"
        label="E类评分人"
      >
      </el-table-column>
      <el-table-column
        prop="fscore"
        label="F类评分人"
      >
      </el-table-column>
      <el-table-column
        prop="sumMbAvgScore"
        v-if="dbtype=='1'?false:true"
        label="目标平均"
      >
      </el-table-column>
      <el-table-column
        prop="score"
        label="个人得分"
      >
      </el-table-column>
      <!-- <el-table-column
        prop="avgscore"
        label="整体平均分"
      >
      </el-table-column> -->
    </el-table>
    <div
      class="bg"
      id="chart1"
    ></div>
    <el-dialog
      title="二级维度结果"
      :visible.sync="dialogVisible"
      width="80%"
      :before-close="handleClose"
    >
      <TwoChart></TwoChart>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="dialogVisible = false"
        >确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import TwoChart from "./twoChart"; // 二级维度结果
export default {
  data() {
    return {
      dialogVisible: false,
      tableData: [],
      type: 0,
      dbtype: this.$store.state.user.user.dbtype,
      series: [],
      dutyChartType: [],
      myColor:[]
    };
  },
  props: ["chartData","id","year","month","dutyType"],
  created() {
    this.dutyChartType = this.dutyType
    this.tableData = this.chartData;
    this.type = this.$store.state.user.user.rolecode;
  },
  components: {
    TwoChart
  },
  methods: {
    topic(row) {
      this.$router.push({
        path: "/home/twoDimension",
        query: {"usercode":this.id,"id":row.id,'resultreportname':row.resultreportname,"year":this.year,"month":this.month}
      });
    },
    handleClose() {
      this.dialogVisible = false;
    },
    drawLine() {
      let serDataA = []
        let serDataB = []
        let serDataC = []
        let serDataD = []
        let serDataE = []
        let serDataF = []
        let serData = []
        if(this.tableData !=undefined && this.tableData.length > 0) {
          serDataA = [this.tableData[0].ascore, this.tableData[1].ascore, this.tableData[2].ascore, this.tableData[3].ascore]
          serDataB = [this.tableData[0].bscore, this.tableData[1].bscore, this.tableData[2].bscore, this.tableData[3].bscore]
          serDataC = [this.tableData[0].cscore, this.tableData[1].cscore, this.tableData[2].cscore, this.tableData[3].cscore]
          serDataD = [this.tableData[0].dscore, this.tableData[1].dscore, this.tableData[2].dscore, this.tableData[3].dscore]
          serDataE = [this.tableData[0].escore, this.tableData[1].escore, this.tableData[2].escore, this.tableData[3].escore]
          serDataF = [this.tableData[0].fscore, this.tableData[1].fscore, this.tableData[2].fscore, this.tableData[3].fscore]
          serData = [this.tableData[0].score, this.tableData[1].score, this.tableData[2].score, this.tableData[3].score]
          if(this.dbtype =='1') {
            serDataA.push(this.tableData[4].ascore)
            serDataB.push(this.tableData[4].bscore)
            serDataC.push(this.tableData[4].cscore)
            serDataD.push(this.tableData[4].dscore)
            serDataE.push(this.tableData[4].escore)
            serDataF.push(this.tableData[4].fscore)
            serData.push(this.tableData[4].score)
          }
        }
      // if (this.type == 300) {
      //   this.series = [
      //     {
      //       name: "个人得分",
      //       type: "bar",
      //       data: [this.tableData[0].score, this.tableData[1].score]
      //     }
      //     // {
      //     //   name: "总体平均分",
      //     //   type: "bar",
      //     //   data: [this.tableData[0].avgscore, this.tableData[1].avgscore]
      //     // }
      //   ];
      // } else {
        this.series = [
          {
            name: "A类评分人",
            type: "bar",
            data: serDataA
          },
          {
            name: "B类评分人",
            type: "bar",
            data: serDataB
          },
          {
            name: "C类评分人",
            type: "bar",
            data: serDataC
          },
          {
            name: "D类评分人",
            type: "bar",
            data: serDataD
          },
          {
            name: "E类评分人",
            type: "bar",
            data: serDataE
          },
          {
            name: "F类评分人",
            type: "bar",
            data: serDataF
          }
          // {
          //   name: "总体平均分",
          //   type: "bar",
          //   data: [this.tableData[0].avgscore, this.tableData[1].avgscore, this.tableData[2].avgscore, this.tableData[3].avgscore]
          // }
        ];
      // }
      if(this.dbtype =='2') {
        let serDataAvgMb = [0, 0, 0, this.tableData[3].sumMbAvgScore]
        this.series.push({
            name: "目标平均",
            type: "bar",
            data: serDataAvgMb
          });
        this.myColor = ["#4cabce", "#e5323e", "#f00220","#003366","#B3D9D9","	#D8D8EB","#6495ED","#006699"]
      }else{
        this.myColor = ["#4cabce", "#e5323e", "#f00220","#003366","#B3D9D9","	#D8D8EB", "#006699"]
      }
      this.series.push({
            name: "个人得分",
            type: "bar",
            data: serData
          });
      if (this.tableData ==undefined || this.tableData.length == 0) {
        return;
      }
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.init(document.getElementById("chart1"));
      // 绘制图表
      myChart.setOption({
        color: this.myColor,
        tooltip: {
          trigger: "axis"
        },
        title: {
          text: ""
        },
        xAxis: [
          {
            type: "category",
            data: this.dutyChartType
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
    this.drawLine();
  },
  watch: {
    chartData: {
      handler(newValue, oldValue) {
        this.tableData = newValue;
        this.drawLine();
      },
      deep: true
    }
  }
};
</script>

<style lang="scss" scoped>
#chart1 {
  width: 100%;
  height: 350px;
  border: 1px solid #f1f1f1;
  margin-top: 15px;
}
// .bg {
//   box-shadow: 0px 1px 10px #dfdfdf;
//   border-radius: 4px;
//   border: 1px solid #e8e8e8;
// }
</style>

