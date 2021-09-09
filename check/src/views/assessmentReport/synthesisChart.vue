<template>
  <div>
    <el-table
        :data="tableData"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column
          prop="name"
          label="人员"
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="grade"
          label="个人得分"
          width="180"
        >
        </el-table-column>
        <el-table-column
          prop="average"
          label="总体平均分"
        >
        </el-table-column>
        <el-table-column
          prop="gap"
          label="分值差"
        >
        </el-table-column>
        <el-table-column
          fixed="right"
          label="详细报告"
          width="100">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="someone">个人测评报告</el-button>
          </template>
          </el-table-column>
      </el-table>
    <div
      class="bg"
      id="chart1"
    ></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [{
          name: '袁莉',
          grade: '3.5',
          average: '3.5',
          gap: '0',
          detail: '个人详细报告'
        },{
          name: '杨光耀',
          grade: '4',
          average: '4',
          gap: '0',
          detail: '个人详细报告'
        },{
          name: '刘亚玲',
          grade: '5',
          average: '5',
          gap: '0',
          detail: '个人详细报告'
        },{
          name: '安锐',
          grade: '3',
          average: '3',
          gap: '0',
          detail: '个人详细报告'
        },{
          name: '胡豫',
          grade: '4',
          average: '4',
          gap: '0',
          detail: '个人详细报告'
        }]
    };
  },
  created() {},
  methods: {
    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.init(document.getElementById("chart1"));
      // 绘制图表
      myChart.setOption({
        title: {
          text: ""
        },
        color: ["#3398DB"],

        xAxis: [
          {
            type: "category",
            data: ["袁莉", "杨光耀", "刘亚玲", "安锐", "胡豫"]
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
            name: "总分",
            type: "bar",
            barWidth: "60%",
            data: [3.5, 4, 5, 3, 4]
          }
        ]
      });
    },
    someone(){
      this.$router.push({
              path: '/detail/assessmentReport/someone',
              query: {
              }
          })
    }
  },
  mounted() {
    this.drawLine();
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

