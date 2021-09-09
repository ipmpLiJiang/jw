<template>
  <div>
    <el-table
      :data="data"
      border
      style="width: 100%"
      size="small"
    >
      <el-table-column
        prop="username"
        label="员工姓名"
        width="180"
      >
      </el-table-column>
      <el-table-column
        prop="stationname"
        label="岗位"
        width="180"
      >
      </el-table-column>
      <!-- <el-table-column
        prop="ascore"
        label="A类得分"
      >
      </el-table-column>
      <el-table-column
        prop="bscore"
        label="B类得分"
      >
      </el-table-column>
      <el-table-column
        prop="cscore"
        label="C类得分"
      >
      </el-table-column>
      <el-table-column
        prop="dscore"
        label="D类得分"
      >
      </el-table-column> -->
      <el-table-column
        prop="totalscore"
        label="个人得分"
      >
      </el-table-column>
      <el-table-column
        prop="avgscore"
        label="总体平均分"
      >
      </el-table-column>
      <!-- <el-table-column
        label="进度"
        style="color:#409EFF"
      >
        <template slot-scope="scope">
          <span style="color:#409EFF">{{scope.row.plan}}%</span>
        </template>
      </el-table-column> -->
      <el-table-column
          fixed="right"
          label="操作"
          width="100px"
        >
          <template slot-scope="scope">
            <el-button
              @click="detailReport(scope.row)"
              type="text"
              size="small"
            >详细报告</el-button>
          </template>
        </el-table-column>
    </el-table>
    <div
      class="bg"
      id="chart2"
    ></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: []
    };
  },
  props:['data'],
  created() {
    
  },
  methods: {
    drawLine() {
      let nameData = [];
      let scoreData = [];
      for(let i=0; i<this.data.length; i++){
        nameData.push(this.data[i]['username']);
        scoreData.push(this.data[i]['totalscore']);
      }
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.init(document.getElementById("chart2"));
      // 绘制图表
      myChart.setOption({
        title: {
          text: ""
        },
        color: ["#3398DB"],

        xAxis: [
          {
            type: "category",
            data: nameData
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
        series: [
          {
            name: "个人得分",
            type: "bar",
            barWidth: "50%",
            data: scoreData
          }
        ]
      });
    },
    //进入考核
    detailReport(row) {
      
      this.$router.push({
        path: "/home/someone",
        query: { usercode: row.usercode }
      });
    }
  },
  mounted() {
    this.drawLine();
  }
};
</script>

<style lang="scss" scoped>
#chart2 {
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

