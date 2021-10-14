<template>
  <div class="main-content" v-if="isData==0">
    <el-row class="box-wrap">
      <h2 class="title">{{detailData.year}}年 第{{detailData.month}}季度 测评报告</h2>
      <el-col :span="16">
        <h4>{{detailData.username}}<span>&nbsp;&nbsp;&nbsp;
          账号：{{detailData.moneycard}}&nbsp;&nbsp;&nbsp;
          部门：{{detailData.departmentname}}&nbsp;&nbsp;&nbsp;
          岗位：{{detailData.stationname}}</span>&nbsp;&nbsp;&nbsp;
          <span v-show="postTypeName!=''?true:false">岗位类型：{{postTypeName}}</span>
        </h4>
        <el-col :span="2">
          <h5>{{detailData.totalscore}}</h5>
          <p>总得分</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='1'?false:true">
          <h5>{{detailData.basicscore}}</h5>
          <p>基础得分</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='2'?false:true">
          <h5>{{detailData.basicscore}}</h5>
          <p>政治建设</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='1'?false:true">
          <h5>{{detailData.keyscore}}</h5>
          <p>岗位得分</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='2'?false:true">
          <h5>{{detailData.keyscore}}</h5>
          <p>思想建设</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='1'?false:true">
          <h5>{{detailData.zdScore}}</h5>
          <p>重点得分</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='2'?false:true">
          <h5>{{detailData.zdScore}}</h5>
          <p>组织建设</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='1'?false:true">
          <h5>{{detailData.mbScore}}</h5>
          <p>目标得分</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='1'?false:true">
          <h5>{{detailData.sumMbAvgScore}}</h5>
          <p>目标平均</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='2'?false:true">
          <h5>{{detailData.mbScore}}</h5>
          <p>党建创新</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='1'?false:true">
          <h5>{{detailData.dfScore}}</h5>
          <p>党风廉政</p>
        </el-col>
        <el-col :span="3" v-show="dbtype=='2'?false:true">
          <h5>{{detailData.zfScore}}</h5>
          <p>作风建设</p>
        </el-col>
        <el-col :span="4">
          <h5>{{detailData.avgscore}}</h5>
          <p>{{avgTitle}}</p>
        </el-col>
        <!-- <el-col :span="4">
          <h5 style="color:#409EFF;">{{detailData.plan}}%</h5>
          <p>评分进度</p>
        </el-col> -->
      </el-col>

    </el-row>
    <el-row class="box-wrap">
      <el-col>
        <b>总得分：{{detailData.totalscore}} 分，{{postTypeName}}整体平均分差距0%，较个人上一季度0%差距。</b>
        <br><br>
        <b>另附：总得分和单项得分的柱状图（实际得分和平均分）</b>
      </el-col>
    </el-row>
    <div class="box-data">
      <h4 class="bd-title">综合结果</h4>
      <SomeoneChart :chartData="reportsData" :id="detailData.usercode" :year="$route.query.year" :month="$route.query.month" :dutyType="dutyType" :avgTitle="avgTitle"></SomeoneChart>
    </div>
  </div>
  <div class="none-data" v-else>该用户暂无评估报告！</div>
</template>

<script>
import allChart from "./allChart"; // 综合得分图表
import SomeoneChart from "./someoneChart"; // 综合得分图表
import TwoChart from "./twoChart"; // 二级维度结果
import { getUserDetail } from "@/api/assessmentReport/index";
import qs from "qs";
export default {
  data() {
    return {
      detailData: {},
      reportsData: [],
      dutyType: [],
      isData: 0,
      dbtype: this.$store.state.user.user.dbtype,
      roleCode:this.$store.state.user.user.rolecode,
      avgTitle: '整体平均值',
      postTypeName: ''
    };
  },
  mounted() {},
  created() {
    this.getDetail();
  },
  components: {
    allChart,
    SomeoneChart,
    TwoChart
  },
  methods: {
    getDetail() {
      let data = {};
      if(this.$store.state.user.user.rolecode == 300){
        data.usercode = this.$store.state.user.user.usercode;
      }else{
        data.usercode = this.$route.query.usercode;
      }
      if(this.$route.query.year){
        data.year = this.$route.query.year;
      }else{
        data.year = "";
      }
      if(this.$route.query.month){
        data.month = this.$route.query.month;
      }else{
         data.month = "";
      }
      data.dbtype = this.dbtype

      if(data.dbtype == '1') {
        this.dutyType = ["政治建设", "思想建设", "组织建设", "党建创新","作风建设"]
      } else {
        this.dutyType = ["基础评分", "关键评分", "重点评分", "目标评分","党风廉政"]
      }
      
      const loading = this.$loading({
          lock: true,
          text: '加载中...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
      new Promise((response, reject) => {
        getUserDetail(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.detailData = response.data.data.evaluationReport;
              if (this.dbtype=='2'){
                this.avgTitle = this.detailData.postType == '1' ? '科主任平均值' :
                this.detailData.postType == '2' ? '护士长平均值' :
                this.detailData.postType == '3' ? '行政平均值' : this.avgTitle;
                this.postTypeName = this.detailData.postType == '1' ? '科主任' :
                this.detailData.postType == '2' ? '护士长' :
                this.detailData.postType == '3' ? '行政' : '';
              }
              this.reportsData = response.data.data.reports;
            } else if (response.data.code == 2) {
              this.isData = 2;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            loading.close();
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
.title {
  text-align: center;
  font-size: 18px;
  margin: 0px 0 10px;
}
.box-wrap {
  background: #fff;
  border-radius: 5px;
  padding: 20px;
  border-radius: 5px;
  .good {
    color: #409EFF;
    margin-right: 10px;
  }
  .bad {
    color: #f56c6c;
    margin-right: 10px;
  }
  .up {
    color: #409EFF;
    margin-right: 10px;
  }
  .down {
    color: #f56c6c;
    margin-right: 10px;
  }
  .fx-title {
    margin-top: 20px;
    font-weight: 500;
    font-size: 16px;
  }
  h4 {
    margin: 10px 0px 10px 0px;
    font-size: 20px;
    span {
      font-size: 14px;
      color: #888;
      margin-left: 4px;
    }

    .icon {
      font-size: 12px;
      font-weight: normal;
      margin-left: 2px;
    }
    .level {
      font-size: 16px;
      font-weight: normal;
      margin-left: 8px;
    }
  }
  p {
    text-align: center;
  }
  h5 {
    font-size: 24px;
    text-align: center;
  }
}
.box-select {
  margin-right: 10px;
}

.box-data {
  padding: 20px;
  background: #fff;
  border-radius: 5px;
  margin-top: 10px;
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
}
.none-data{
  text-align: center;
  color:#999;
  background: #fff;
  padding: 100px 0;

}
</style>
