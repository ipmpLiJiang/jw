<template>
  <div>
    <el-row class="content">
      <el-col :span="14">
        <h1>文化程度分析</h1>
        <div
          id="chartmainline"
          style="width: 500px; height: 500px; margin-left: 20px"
        ></div>
      </el-col>

      <el-col :span="10">
        <h1>评优百分比</h1>
        <div id="Excellent" style="width: 500px; height: 500px"></div>
      </el-col>
    </el-row>
    <el-row class="content">
      <h1>政治面貌</h1>

      <div id="technical" style="width: 100%; height: 500px"></div>
    </el-row>
  </div>
</template>

<script>
import qs from "qs";
import {
  educationLevel,
  scoreLevel,
  politicalStatus,
} from "../../api/analyse/analyse";
export default {
  props: ["branchId"],

  data() {
    return {
      data: ["大专及以下", "本科", "硕士研究生", "博士研究生"],
      options: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: "0",
          label: "未提交",
        },
        {
          value: "1",
          label: "已提交",
        },
      ],
      datas: [],
      percentlist: [],
      personnelList: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: 0,
          label: "临床人员",
        },
        {
          value: 1,
          label: "非临床人员",
        },
      ],
      politicsData: [
        "中国共产党员",
        "团员",
        "民盟",
        "民建",
        "民进",
        "农工党",
        "致公党",
        "九三学社",
        "无党派人士",
        "群众",
        "其他",
      ],
      politicsList: [],
      branchId: "",
    };
  },
  computed: {},
  created() {
    this.educationLevel();
    this.scoreLevel();
    this.politicalStatus();
  },
  mounted() {
    // this.Excellent();
    // this.academic();
  },
  watch: {},
  methods: {
    //文化程度分析
    drawLine: function () {
      //基于准本好的DOM，初始化echarts实例
      let chartmainline = this.$echarts.init(
        document.getElementById("chartmainline")
      );
      let colorList = [
        "#C6504F",
        "#4D4CDB",
        "#7D5DCC",
        "#44AE2E",
        "#35C87A",
        "#D39255",
        "#D6C76E",
        "#2268D4",
        "#2498E3",
        "#41CCD3",
      ];

      let option = {
        color: colorList,

        xAxis: {
          show: false,
        },
        yAxis: {
          type: "category",
          show: true,
          gridIndex: 0,
          offset: 5, // 偏移
          axisTick: {
            // 刻度
            show: false,
          },
          axisLine: {
            // 轴线
            show: false,
          },
          axisLabel: {
            // 文字
            color: "#949AC7",
            fontSize: 18,
          },
          data: this.data,
        },
        grid: {
          left: "72%",
          height: 350,
          top: "middle",
          right: "2%",
          containLabel: true,
        },
        tooltip: {
          trigger: "item",
          textStyle: {
            fontSize: 14,
          },
          formatter: "{b} : {c} (人)",
        },
        series: [
          {
            name: "Sale",
            type: "bar",
            data: this.datas,

            // datasetIndex:0,
            xAxisIndex: 0,
            yAxisIndex: 0,
            zlevel: 1,
            itemStyle: {
              // 柱子样式
              normal: {
                color: function (params) {
                  return colorList[params.dataIndex];
                },
                barBorderRadius: [0, 15, 15, 0],
              },
            },
            barWidth: 20,
          },
          {
            stack: "a",
            type: "pie",
            selectedMode: false, // 不能选中
            radius: ["10%", "57%"],
            center: ["38%", "55%"], // 圆心
            //   center: ['55%', '35%'],
            roseType: "area",
            clockwise: false, // 逆时针
            startAngle: 220, // 起始角度
            //   zlevel: 10,
            silent: true, // 不相应鼠标
            label: {
              // 文字
              // color:'#2ABEFF',
              formatter: "{a|{b}}\n{rate|{d}%}",
              position: "outside",
              // fontSize: 12,
              align: "center",
              show: true,
              verticalAlign: "top",
              rich: {
                // 文字位置
                a: {
                  fontSize: 16,
                  color: "#fff",
                  // padding: [10, -10, -20, -45],
                  padding: [10, -60, -60, -60],
                },

                rate: {
                  fontSize: 14,
                  color: "#2ABEFF",
                  padding: [10, -85, -20, -60],
                  //  padding: [10, -30, -20, -45],
                },
              },
            },
            labelLine: {
              show: true,
              length: 15,
              length2: 65,
              lineStyle: {
                color: "#2ABEFF",
              },
            },
            data: this.datas,
          },
        ],
      };
      chartmainline.setOption(option);
      let that = this;
      chartmainline.on("click", function (params) {
        console.log(params.dataIndex);
        let dataIndex = "";
        if (params.dataIndex == 0) {
          dataIndex = 1;
        }
        if (params.dataIndex == 1) {
          dataIndex = 2;
        }
        if (params.dataIndex == 2) {
          dataIndex = 3;
        }
        if (params.dataIndex == 3) {
          dataIndex = 4;
        }
        that.$router.push({
          path: "/ydyf/analyseList",
          query: { cultureid: dataIndex },
        });
      });
    },
    //评优百分比
    Excellent() {
      let Excellent = this.$echarts.init(document.getElementById("Excellent"));

      var plantCap = this.percentlist;

      var datalist = [
        {
          offset: [56, 48],
          symbolSize: 120,
          opacity: 0.95,
          color: "#f467ce",
        },
        {
          offset: [35, 80],
          symbolSize: 95,
          opacity: 0.88,
          color: "#7aabe2",
        },
        {
          offset: [20, 43],
          symbolSize: 90,
          opacity: 0.84,
          color: "#ff7123",
        },
        {
          offset: [83, 30],
          symbolSize: 90,
          opacity: 0.8,
          color: "#ffc400",
        },
        {
          offset: [36, 20],
          symbolSize: 65,
          opacity: 0.75,
          color: "#5e333f",
        },
        {
          offset: [64, 10],
          symbolSize: 70,
          opacity: 0.7,
          color: "#6b3442",
        },
        {
          offset: [75, 75],
          symbolSize: 60,
          opacity: 0.68,
          color: "#8a3647",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
        {
          offset: [88, 62],
          symbolSize: 50,
          opacity: 0.6,
          color: "#68333f",
        },
      ];
      var datas = [];
      for (var i = 0; i < plantCap.length; i++) {
        var item = plantCap[i];
        var itemToStyle = datalist[i];
        datas.push({
          name: item.value + "\n" + item.name,
          value: itemToStyle.offset,
          symbolSize: itemToStyle.symbolSize,
          label: {
            normal: {
              textStyle: {
                fontSize: 14,
              },
            },
          },
          itemStyle: {
            normal: {
              color: itemToStyle.color,
              opacity: itemToStyle.opacity,
            },
          },
        });
      }
      let option = {
        grid: {
          show: false,
          top: 10,
          bottom: 10,
        },
        xAxis: [
          {
            gridIndex: 0,
            type: "value",
            show: false,
            min: 0,
            max: 100,
            nameLocation: "middle",
            nameGap: 5,
          },
        ],
        yAxis: [
          {
            gridIndex: 0,
            min: 0,
            show: false,
            max: 100,
            nameLocation: "middle",
            nameGap: 30,
          },
        ],
        series: [
          {
            type: "scatter",
            symbol: "circle",
            symbolSize: 120,
            label: {
              normal: {
                show: true,
                formatter: "{b}",
                color: "#fff",
                textStyle: {
                  fontSize: "20",
                },
              },
            },
            itemStyle: {
              normal: {
                color: "#00acea",
              },
            },
            data: datas,
          },
        ],
      };

      Excellent.setOption(option);
      let that = this;
      Excellent.on("click", function (params) {
        let dataIndex = "";
        if (params.dataIndex == 0) {
          dataIndex = 1;
        }
        if (params.dataIndex == 1) {
          dataIndex = 2;
        }
        if (params.dataIndex == 2) {
          dataIndex = 3;
        }
        if (params.dataIndex == 3) {
          dataIndex = 4;
        }
        that.$router.push({
          path: "/ydyf/analyseList",
          query: { educationListid: dataIndex },
        });
      });
    },

    //职称分析
    academic() {
      let technical = this.$echarts.init(document.getElementById("technical"));
      let option = {
        color: ["#60ABFC"],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        legend: {
          orient: "horizontal",
          data: this.politicsData,
          textStyle: {
            color: "#858FA6",
            fontFamily: "Microsoft YAHei,serf",
          },
        },
        grid: {
          top: 20,
          left: 20,
          right: 20,
          bottom: 10,
          containLabel: true,
        },
        xAxis: {
          data: this.politicsData,
          axisLabel: {
            show: true,
            color: "#858fa6",
          },
          axisTick: {
            show: false,
          },
          splitLine: {
            lineStyle: {
              width: 1,
              color: "#E2E2E2",
            },
          },
          axisLine: {
            lineStyle: {
              width: 1,
              color: "#E2E2E2",
            },
          },
        },
        yAxis: {
          data: [],
          axisTick: {
            show: false,
          },
          splitLine: {
            lineStyle: {
              width: 1,
              color: "#E2E2E2",
            },
          },
          axisLabel: {
            color: "#858fa6",
          },
          axisLine: {
            lineStyle: {
              width: 0,
              color: "#E2E2E2",
              show: false,
            },
          },
        },
        series: [
          {
            name: "数量",
            type: "bar",
            data: this.politicsList,
            barGap: 10,
            barWidth: "60%",
          },
        ],
      };
      technical.setOption(option);

      let that = this;
      technical.getZr().off("click");

      technical.getZr().on("click", function (param) {
        let pointInPixel = [param.offsetX, param.offsetY];
        if (technical.containPixel("grid", pointInPixel)) {
          //获取当前点击的索引值
          //注意：若柱状图为纵向则获取x轴的索引，若柱状图为横向则需获取y轴的索引(见下方注释)
          let xIndex = technical.convertFromPixel({ seriesIndex: 0 }, [
            param.offsetX,
            param.offsetY,
          ])[0];
          let xData = option.xAxis.data[xIndex]; //当前点击柱子的数据

          //....  业务逻辑
          let politicsIndex = "";
          switch (xIndex) {
            case 0:
              politicsIndex = 1;
              break;
            case 1:
              politicsIndex = 2;
              break;
            case 2:
              politicsIndex = 3;
              break;
            case 3:
              politicsIndex = 4;
              break;
            case 4:
              politicsIndex = 5;
              break;
            case 5:
              politicsIndex = 6;
              break;
            case 6:
              politicsIndex = 8;
              break;
            case 7:
              politicsIndex = 9;
              break;
            case 8:
              politicsIndex = 10;
              break;
            case 9:
              politicsIndex = 11;
              break;
            case 10:
              politicsIndex = 12;
              break;
          }
          that.$router.push({
            path: "/ydyf/analyseList",
            query: { politicsIndex: politicsIndex },
          });
        }
      });
    },
    //查询文化列表
    educationLevel() {
      let data = {
        branchId: this.$parent.$parent.branchId,
      };
      new Promise((response, reject) => {
        educationLevel(qs.stringify(data))
          .then((response) => {
            if (response.data.code === 0) {
              this.datas = response.data.data;
              this.drawLine();
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    //查询评优比
    scoreLevel() {
      let data = {
        branchId: this.$parent.$parent.branchId,
      };
      new Promise((response, reject) => {
        scoreLevel(qs.stringify(data))
          .then((response) => {
            if (response.data.code === 0) {
              this.percentlist = response.data.data;
              this.Excellent();
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    //政治面貌分析
    politicalStatus() {
      let data = {
        branchId: this.$parent.$parent.branchId,
      };
      new Promise((response, reject) => {
        politicalStatus(qs.stringify(data))
          .then((response) => {
            if (response.data.code === 0) {
              this.politicsList = response.data.data;

              this.academic();
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    //初始化
    into() {},
    personnel() {},
  },
  components: {},
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

.upload-grade-leading-in {
  display: inline-block;
}

.monitor {
  color: #f00;
  font-size: 18px;
  margin-bottom: 10px;
  display: block;
  text-align: left;
}

.a-demo {
  display: block;
  color: #e6a23c;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  text-decoration: underline;
  text-align: left;
  cursor: pointer;
}
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>