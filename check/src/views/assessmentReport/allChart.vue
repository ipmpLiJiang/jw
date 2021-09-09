<template>
  <div>
    <div id="allChart"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
        strData:{}
    };
  },
  props: ["chartData"],
  created() {
    this.strData = this.chartData;
  },
  methods: {
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
                value: [
                  this.strData.totalscore,
                  this.strData.basicscore,
                  this.strData.keyscore,
                  this.strData.avgscore
                ],
                name: "综合分析"
              }
            ]
          }
        ]
      });
    }
  },
  mounted() {
    this.drawLine();
  },
  watch: {
    chartData: {
      handler(newValue, oldValue) {
        this.strData = newValue;
        this.drawLine();
      },
      deep: true
    }
  }
};
</script>

<style lang="scss" scoped>
#allChart {
  width: 100%;
  height: 260px;
}
</style>

