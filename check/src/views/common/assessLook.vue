

<template>

  <div>
    <el-dialog
      title="考核详情查看"
      :visible.sync="selfDialogVisible"
      width="90%"
      center
    >
      <el-col :span="24">
        <ul class="personal-information">
          <li>
            <div class="label">姓名:</div>
            <div class="value">{{detailData.username}}({{detailData.moneycard}})</div>
          </li>
          <li>
            <div class="label">标题:</div>
            <div class="value">{{detailData.title}}</div>
          </li>
          <li>
            <div class="label">部门:</div>
            <div class="value">{{detailData.departmentname}}</div>
          </li>
          <li>
            <div class="label">岗位:</div>
            <div class="value">{{detailData.stationname}}</div>
          </li>
          <li>
            <div class="label">年份:</div>
            <div class="value">{{detailData.year}}</div>
          </li>
          <li>
            <div class="label">季度:</div>
            <div class="value">第{{detailData.month}}季度</div>
          </li>
          <!-- <li>
            <div class="label">照片:</div>
            <div class="value">
              <el-button
                type="text"
                style="padding:0"
              >点击查看</el-button>
            </div>
          </li> -->
          <!-- <li v-if="detailData.stationcode">
            <div class="label">职责:</div>
            <div class="value">
              <el-button
                type="text"
                style="padding:0"
                @click="respDialogVisible = true"
              >点击查看</el-button>
            </div>
          </li>
          <li v-if="detailData.filename">
            <div class="label">附件:</div>
            <div
              class="value"
              style="cursor:pointer;"
              @click="preview(detailData.savepath)"
            ><a>{{detailData.filename}}</a></div>
          </li> -->
          <li class="w100">
            <div class="label">评分人:</div>
            <div class="value">
              <el-select
                placeholder="请选择评分人"
                v-model="userValue"
                @change="userChange"
                style="width:100%"
              >
                <el-option
                  v-for="(item,index) in userList"
                  :key="'r'+index"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </li>
          <!-- <li class="w100 operation">
            <el-button type="primary">历史绩效</el-button>
            <el-button type="primary">职责描述</el-button>
          </li> -->
        </ul>
      </el-col>
      <el-col :span="24">
        <ul class="indicator">
          <li
            class="title"
            v-if="historyTotal"
          >总分：<span style="color:#f00;">{{historyTotal}}</span></li>
          <li class="li-title">
            <div class="title">基础指标(总分15)</div>
          </li>
          <li
            v-for="(item,index) in dutyJichu" :key="'jc' + index"
            style="border-bottom:1px solid #409eff"
          >
            <el-row style="padding-bottom:5px;">
              <el-col :span="19">
                <span v-html="item.dutyname"></span>
              </el-col>
            </el-row>
            <el-row style="padding-top:10px;padding-bottom:15px;">
              <el-col :span="2">
                自评说明：
              </el-col>
              <el-col :span="17">
                {{item.zpsm=='' || item.zpsm==null?'无':item.zpsm}}
              </el-col>
            </el-row>
            <el-row class="pingjia">
              <el-col :span="5"> 优秀({{item.ascore}}) </el-col>
              <el-col :span="5"> 良好({{item.bscore}}) </el-col>
              <el-col :span="5"> 一般({{item.cscore}}) </el-col>
              <el-col :span="5"> 较差({{item.dscore}}) </el-col>
              <el-col :span="4"> {{item.score}} </el-col>
            </el-row>
            <el-row class="cpsm" v-show="item.score == item.dscore?true:false">
              <el-col :span="2"> 差评原因：</el-col>
              <el-col :span="17"> {{item.cpsm}} </el-col>
            </el-row>
            <el-col class="cpsmKong" v-show="item.score == item.dscore?false:true">
              &nbsp;
            </el-col>
          </li>
          <li class="li-title">
            <div class="title">岗位职责(总分20)</div>
          </li>
          <li
            v-for="(item,index) in dutyYiban" :key="'gw' + index"
            style="border-bottom:1px solid #409eff"
          >
            <el-row style="padding-bottom:5px;">
              <el-col :span="19">
                <span v-html="item.dutyname"></span>
              </el-col>
            </el-row>
            <el-row style="padding-top:10px;padding-bottom:15px;">
              <el-col :span="2">
                自评说明：
              </el-col>
              <el-col :span="17">
                {{item.zpsm=='' || item.zpsm==null?'无':item.zpsm}}
              </el-col>
            </el-row>
            <el-row class="pingjia">
              <el-col :span="5"> 优秀({{item.ascore}}) </el-col>
              <el-col :span="5"> 良好({{item.bscore}}) </el-col>
              <el-col :span="5"> 一般({{item.cscore}}) </el-col>
              <el-col :span="5"> 较差({{item.dscore}}) </el-col>
              <el-col :span="4"> {{item.score}} </el-col>
            </el-row>
            <el-row class="cpsm" v-show="item.score == item.dscore?true:false">
              <el-col :span="2"> 差评原因：</el-col>
              <el-col :span="17"> {{item.cpsm}} </el-col>
            </el-row>
            <el-col class="cpsmKong" v-show="item.score == item.dscore?false:true">
              &nbsp;
            </el-col>
          </li>
          <li class="li-title">
            <div class="title">重点任务(总分25)</div>
          </li>
          <li
            v-for="(item,index) in dutyZhongdian" :key="'zd' + index"
            style="border-bottom:1px solid #409eff"
          >
            <el-row style="padding-bottom:5px;">
              <el-col :span="19">
                <span v-html="item.dutyname"></span>
              </el-col>
            </el-row>
            <el-row style="padding-top:10px;padding-bottom:15px;">
              <el-col :span="2">
                自评说明：
              </el-col>
              <el-col :span="17">
                {{item.zpsm=='' || item.zpsm==null?'无':item.zpsm}}
              </el-col>
            </el-row>
            <el-row class="pingjia">
              <el-col :span="5"> 优秀({{item.ascore}}) </el-col>
              <el-col :span="5"> 良好({{item.bscore}}) </el-col>
              <el-col :span="5"> 一般({{item.cscore}}) </el-col>
              <el-col :span="5"> 较差({{item.dscore}}) </el-col>
              <el-col :span="4"> {{item.score}} </el-col>
            </el-row>
            <el-row class="cpsm" v-show="item.score == item.dscore?true:false">
              <el-col :span="2"> 差评原因：</el-col>
              <el-col :span="17"> {{item.cpsm}} </el-col>
            </el-row>
            <el-col class="cpsmKong" v-show="item.score == item.dscore?false:true">
              &nbsp;
            </el-col>
          </li>
          <li class="li-title">
            <div class="title">目标任务(总分25)</div>
          </li>
          <li
            v-for="(item,index) in dutyMubiao" :key="'mb' + index"
            style="border-bottom:1px solid #409eff"
          >
            <el-row style="padding-bottom:5px;">
              <el-col :span="19">
                <span v-html="item.dutyname"></span>
              </el-col>
            </el-row>
            <el-row style="padding-top:10px;padding-bottom:15px;">
              <el-col :span="2">
                自评说明：
              </el-col>
              <el-col :span="17">
                {{item.zpsm=='' || item.zpsm==null?'无':item.zpsm}}
              </el-col>
            </el-row>
            <el-row class="pingjia">
              <el-col :span="5"> 优秀({{item.ascore}}) </el-col>
              <el-col :span="5"> 良好({{item.bscore}}) </el-col>
              <el-col :span="5"> 一般({{item.cscore}}) </el-col>
              <el-col :span="5"> 较差({{item.dscore}}) </el-col>
              <el-col :span="4"> {{item.score}} </el-col>
            </el-row>
            <el-row class="cpsm" v-show="item.score == item.dscore?true:false">
              <el-col :span="2"> 差评原因：</el-col>
              <el-col :span="17"> {{item.cpsm}} </el-col>
            </el-row>
            <el-col class="cpsmKong" v-show="item.score == item.dscore?false:true">
              &nbsp;
            </el-col>
          </li>
          <li
            class="w100 operation"
            v-if="detailData.isedit == 1 ? false : true"
          >
            <div v-if="dutyJichu.length == 0 && dutyJichu.length == 0 && dutyZhongdian.length == 0 && dutyMubiao.length == 0">
              <el-button
                type="default"
                disabled="disabled"
              >没有检测到该岗位题目，无法考核</el-button>
            </div>
          </li>
        </ul>
      </el-col>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="cancel">取 消</el-button>
      </span>
    </el-dialog>
  </div>

</template>
<script>
import { getDetail } from "@/api/home/home";
import { getUserByScoreFlow } from "@/api/people/people";
import qs from "qs";
export default {
  data() {
    return {
      selfDialogVisible: this.swDialogVisible,
      detailData: {},
      dutyJichu: [],
      dutyYiban: [],
      dutyZhongdian: [],
      dutyMubiao: [],
      userList: [],
      userValue: undefined,
      historyTotal: 0,
      form: {}
    };
  },
  props: {
    swDialogVisible: {
      required: true
    },
    parentForms: {
      required: true
    }
  },
  created() {
    // this.form = Object.assign({}, this.parentForms);
    // this.getDetail();
  },
  methods: {
    getUserFlowType () {
      this.userList = [] 
      let data = {
        serialNo: this.form.serialno,
        scoreType: this.form.scoreType,
        dbtype: this.$store.state.user.user.dbtype
      };
      new Promise((response, reject) => {
        getUserByScoreFlow(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              if (response.data.data.length > 0) {
                  let userArr = response.data.data
                  userArr.forEach(item => {
                    this.userList.push({value: item.usercode, label: item.username})
                  }); 
                  this.userValue = response.data.data[0].usercode
                  this.getDetail()
                }else {
                  this.userList = []
                  this.userValue = ''
                  this.dutyJichu = [];
                  this.dutyYiban = [];
                  this.dutyZhongdian = [];
                  this.dutyMubiao = [];
                  this.historyTotal = 0
                  this.getDetail()
                }
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    userChange (val) {
      this.userValue = val
      this.getDetail()
    },
    getDetail() {
      let data = {
        employeecode: this.form.usercode,
        year: this.form.year,
        month: this.form.month,
        scorringUserCode: this.userValue,
        dbtype: this.$store.state.user.user.dbtype
      };
      new Promise((response, reject) => {
        getDetail(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.detailData = response.data.data.detail;
              // this.form = response.data.data.stations;
              this.dutyJichu = [];
              this.dutyYiban = [];
              this.dutyZhongdian = [];
              this.dutyMubiao = [];
              this.dutyJichu = response.data.data.dutyJichu;
              this.dutyYiban = response.data.data.dutyYiban;
              this.dutyZhongdian = response.data.data.dutyZhongdian;
              this.dutyMubiao = response.data.data.dutyMubiao;
              this.historyTotal = response.data.data.total;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
          })
          .catch(error => {
            reject(error);
          });
      });
            
    },
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    }
  },
  watch: {
    selfDialogVisible(val, oldVal) {
      if (!val) {
        this.$emit("childClose", val);
      }
    },
    swDialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
    },
    parentForms(val, oldVal) {
      if (this.selfDialogVisible === true) {
        this.form = Object.assign({}, val);
        this.getUserFlowType();
      }
    }
  }
}
</script>
<style scoped>
</style>
<style scoped lang="scss">
.personal-information {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  margin: 10px;
  padding-bottom: 20px;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  .w100 {
    width: 100%;
  }
  li {
    width: 50%;
    display: flex;
    margin-top: 20px;
    padding: 0 20px;
    .label {
      margin-right: 10px;
      min-width: 70px;
      text-align: right;
    }
  }
}
.operation {
  border-top: 1px solid #f1f1f1;
  padding-top: 20px !important;
}
.indicator {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  margin: 10px;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  .li-title {
    background: #409eff;
    padding: 15px 20px;

    color: #fff;
    margin-bottom: 20px;
  }
  .title {
    font-size: 16px;
    font-weight: 500;
  }
  li {
    width: 100%;
    margin-bottom: 10px;
    padding: 0 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
    }
    .core {
      width: 20%;
      display: inline-block;
    }
    .core-input {
      width: 150px;
    }
  }
  .cpsm {
    padding-top:15px;
    padding-bottom:15px;
  }
  .cpsmKong {
    padding-top:1px;
    padding-bottom:1px;
  }
  .pingjia {
    background:#D2E9FF;
    padding-top:10px;
    padding-bottom:10px;
  }
}
</style>