<template>
  <div>
    <el-row
      v-loading="pageLoading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
    >
      <el-col :span="24">
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
          class="search"
        >
          <el-col :span="5">
            <el-form-item label="年份">
              <el-date-picker
                v-model="search.year"
                type="year"
                placeholder="选择年"
                value-format="yyyy"
                clearable
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="评分月">
              <el-select
                v-model="search.month"
                filterable
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="(item,index) in quarterOptions"
                  :key="'r'+index"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-button
            type="primary"
            @click="historySearch"
            :loading="searchLoading"
          >历史评分搜索</el-button>
        </el-form>
      </el-col>
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
            <div class="label">月度:</div>
            <div class="value">第{{detailData.month}}月</div>
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
          <li v-if="detailData.stationcode">
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
          </li>
          <li class="w100">
            <div class="label">月结内容:</div>
            <div
              class="value"
              v-html="detailData.content"
            ></div>
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
            v-for="(item,index) in dutyJichu"
            :key="'a'+index"
          >
            <div
              class="label"
              v-html="item.dutyname"
            ></div>
            <div class="value">
              <div class="core">优秀({{item.ascore}})</div>
              <div class="core">良好({{item.bscore}})</div>
              <div class="core">一般({{item.cscore}})</div>
              <div class="core">较差({{item.dscore}})</div>
              <div class="core">
                <el-input-number
                  placeholder="请输入分数"
                  v-model="item.score"
                  clearable
                  class="core-input"
                  :disabled="detailData.isedit == 1 ? true : false"
                >
                </el-input-number>
              </div>
            </div>
          </li>
          <li class="li-title">
            <div class="title">岗位职责(总分20)</div>
          </li>
          <li
            v-for="(item,index) in dutyYiban"
            :key="'b'+index"
          >
            <div
              class="label"
              v-html="item.dutyname"
            ></div>
            <div class="value">
              <div class="core">优秀({{item.ascore}})</div>
              <div class="core">良好({{item.bscore}})</div>
              <div class="core">一般({{item.cscore}})</div>
              <div class="core">较差({{item.dscore}})</div>
              <div class="core">
                <el-input-number
                  placeholder="请输入分数"
                  v-model="item.score"
                  clearable
                  class="core-input"
                  :disabled="detailData.isedit == 1 ? true : false"
                >
                </el-input-number>
              </div>
            </div>
          </li>
          <li class="li-title">
            <div class="title">重点任务(总分25)</div>
          </li>
          <li
            v-for="(item,index) in dutyZhongdian"
            :key="'c'+index"
          >
            <div
              class="label"
              v-html="item.dutyname"
            ></div>
            <div class="value">
              <div class="core">优秀({{item.ascore}})</div>
              <div class="core">良好({{item.bscore}})</div>
              <div class="core">一般({{item.cscore}})</div>
              <div class="core">较差({{item.dscore}})</div>
              <div class="core">
                <el-input-number
                  placeholder="请输入分数"
                  v-model="item.score"
                  clearable
                  class="core-input"
                  :disabled="detailData.isedit == 1 ? true : false"
                >
                </el-input-number>
              </div>
            </div>
          </li>
          <li class="li-title">
            <div class="title">目标任务(总分25)</div>
          </li>
          <li
            v-for="(item,index) in dutyMubiao"
            :key="'d'+index"
          >
            <div
              class="label"
              v-html="item.dutyname"
            ></div>
            <div class="value">
              <div class="core">优秀({{item.ascore}})</div>
              <div class="core">良好({{item.bscore}})</div>
              <div class="core">一般({{item.cscore}})</div>
              <div class="core">较差({{item.dscore}})</div>
              <div class="core">
                <el-input-number
                  placeholder="请输入分数"
                  v-model="item.score"
                  clearable
                  class="core-input"
                  :disabled="detailData.isedit == 1 ? true : false"
                >
                </el-input-number>
              </div>
            </div>
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
            <div v-else>
              <el-button
                type="primary"
                @click="compute"
              >计算总分</el-button>
              <el-button
                type="primary"
                @click="submitAssess"
                :loading="submitLoading"
              >提交</el-button>
              <el-button
                type="text"
                v-show="scoreShow"
              >总分为：{{totalScore}}分</el-button>
            </div>
          </li>
        </ul>
      </el-col>
    </el-row>
    <el-dialog
      title="职责"
      :visible.sync="respDialogVisible"
      width="50%"
      v-if="detailData.stationcode"
    >
      <el-form
        ref="form"
        label-width="130px"
      >
        <el-form-item
          label="职责描述"
          class="form-menu"
        >
          <el-input
            type="textarea"
            v-model="form.dutydesc"
            :rows="4"
            readonly="readonly"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="工作关系-内部"
          class="form-menu"
        >
          <span>{{form.relation1}}</span>
        </el-form-item>
        <el-form-item
          label="工作关系-外部"
          class="form-menu"
        >
          <span>{{form.relation2}}</span>
        </el-form-item>
        <el-form-item
          label="可晋身至此的岗位"
          class="form-menu"
        >
          <span>{{form.station1}}</span>
        </el-form-item>
        <el-form-item
          label="可相互轮换的岗位"
          class="form-menu"
        >
          <span>{{form.station2}}</span>
        </el-form-item>
        <el-form-item
          label="学历要求"
          class="form-menu"
        >
          <span>{{form.xueli}}</span>
        </el-form-item>
        <el-form-item
          label="职业资格要求"
          class="form-menu"
        >
          <span>{{form.zhiyezige}}</span>
        </el-form-item>
        <el-form-item
          label="培训要求"
          class="form-menu"
        >
          <span>{{form.peixun}}</span>
        </el-form-item>
        <el-form-item
          label="工作经验"
          class="form-menu"
        >
          <span>{{form.gongzuojingyan}}</span>
        </el-form-item>
        <el-form-item
          label="个性特征要求"
          class="form-menu"
        >
          <span>{{form.gexingtezheng}}</span>
        </el-form-item>
        <el-form-item
          label="基本技能素质要求"
          class="form-menu"
        >
          <span>{{form.jibenjineng}}</span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { getDutyDetail, queryByUser, dutyScoring } from "@/api/home/home";
import { download } from "@/api/common/common";
import qs from "qs";
export default {
  data() {
    return {
      form: {},
      quarterOptions: [
       {
          value: "1",
          label: "1月"
        },
        {
          value: "2",
          label: "2月"
        },
        {
          value: "3",
          label: "3月"
        },
        {
          value: "4",
          label: "4月"
        },
        {
          value: "5",
          label: "5月"
        }
        ,
        {
          value: "6",
          label: "6月"
        }
        ,
        {
          value: "7",
          label: "7月"
        },
        {
          value: "8",
          label: "8月"
        },
        {
          value: "9",
          label: "9月"
        },
        {
          value: "10",
          label: "10月"
        },
        {
          value: "11",
          label: "11月"
        },
        {
          value: "12",
          label: "12月"
        }
      ],
      totalScore: 0,
      scoreShow: false,
      respDialogVisible: false,
      detailData: {},
      dutyJichu: [],
      dutyYiban: [],
      dutyZhongdian: [],
      dutyMubiao: [],
      search: {
        year: "",
        month: ""
      },
      historyTotal: "",
      pageLoading: true,
      submitLoading: false,
      searchLoading: false
    };
  },
  created() {
    this.getDetail();
  },
  methods: {
    getDetail() {
      let data = {
        employeecode: this.$route.query.userCode,
        dbtype: this.$store.state.user.user.dbtype
      };
      if (this.$route.query.year && this.$route.query.month) {
        data.year = this.$route.query.year;
        data.month = this.$route.query.month;
      }
      new Promise((response, reject) => {
        getDutyDetail(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.detailData = response.data.data.detail;
              this.form = response.data.data.stations;
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
            this.tableLoading = false;
            this.pageLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    getMaxMin (obj) {
      // 优秀ascore,良好bscore,一般cscore,较差dscore,填入score
      let yx = 0,lh = 0,yb = 0,jc = 0,yx1 = -100,lh1 = -100,yb1 = -100,jc1 = -100;
      let arScore = [];
      if (obj.ascore != "" && obj.ascore != null && obj.ascore != undefined) {
        if (isNaN(obj.ascore)) {
          arScore= obj.ascore.split('-');
          if (arScore.length >= 2) {
            yx = arScore[0];
            yx1 = arScore[1];
          } 
          if (arScore.length == 1) {
            yx = arScore[0];
          }
        } else {
          yx = obj.ascore;
        }
      } else {
        yx = -100
      }
      arScore = [];
      if (obj.bscore != "" && obj.bscore != null && obj.bscore != undefined) {
        if (isNaN(obj.bscore)) {
          arScore = obj.bscore.split('-');
          if (arScore.length >= 2) {
            lh = arScore[0];
            lh1 = arScore[1];
          } 
          if (arScore.length == 1) {
            lh = arScore[0];
          }
        } else {
          lh = obj.bscore;
        }
      } else {
        lh = -100
      }
      arScore = [];
      if (obj.cscore != "" && obj.cscore != null && obj.cscore != undefined) {
        if (isNaN(obj.cscore)) {
          arScore = obj.cscore.split('-');
          if (arScore.length >= 2) {
            yb = arScore[0];
            yb1 = arScore[1];
          } 
          if (arScore.length == 1) {
            yb = arScore[0];
          }
        } else {
          yb = obj.cscore;
        }
      } else {
        yb = -100
      }
      arScore = [];
      if (obj.dscore != "" && obj.dscore != null && obj.dscore != undefined) {
        if (isNaN(obj.dscore)) {
          arScore = obj.dscore.split('-');
          if (arScore.length >= 2) {
            jc = arScore[0];
            jc1 = arScore[1];
          } 
          if (arScore.length == 1) {
            jc = arScore[0];
          }
        } else {
          jc = obj.dscore;
        }
      } else {
        jc = -100
      }
      arScore = []
      if (yx >= 0) {
        arScore.push(yx);
      }
      if (yx1 >= 0) {
        arScore.push(yx1);
      }
      if (lh >= 0) {
        arScore.push(lh);
      }
      if (lh1 >= 0) {
        arScore.push(lh1);
      }
      if (yb >= 0) {
        arScore.push(yb);
      }
      if (yb1 >= 0) {
        arScore.push(yb1);
      }
      if (jc >= 0) {
        arScore.push(jc);
      }
      if (jc1 >= 0) {
        arScore.push(jc1);
      }
      if (arScore.length > 1) {
        let maxN = Math.max.apply(null,arScore);
        let minN = Math.min.apply(null,arScore);
        arScore = [];
        arScore.push(minN);
        arScore.push(maxN);
      } else {
        arScore = [];
      }
      return arScore;
    },
    //计算总分
    compute() {
      let totalScore = 0;
      let scoreArr = [];
      for (let i = 0; i < this.dutyJichu.length; i++) {
        let val = this.dutyJichu[i];
        if (!val.score) {
          this.$message.warning("请先打完分数");
          return false;
        } else {
          if (!isNaN(val.score)) {
            let arScore = this.getMaxMin(val)
            if (arScore.length == 2) {
              let min = arScore[0];
              let max = arScore[1];
              if (val.score > max || val.score < min) {
                this.$message.warning("基础指标请填写"+ min +"-"+max+"之间数字");
                return false;
              }
              totalScore += parseInt(val.score);
              scoreArr.push(val.score);
            } else {
              this.$message.warning("基础指标在创建指标时,设置有误.");
              return false;
            }
          } else {
            this.$message.warning("基础指标请填写正确数字");
          }
        }
      }
      for (let i = 0; i < this.dutyYiban.length; i++) {
        let val = this.dutyYiban[i];
        if (!val.score) {
          this.$message.warning("请先打完分数");
          return false;
        } else {
          if (!isNaN(val.score)) {
            let arScore = this.getMaxMin(val)
            if (arScore.length == 2) {
              let min = arScore[0];
              let max = arScore[1];
              if (val.score > max || val.score < min) {
                this.$message.warning("岗位职责请填写"+ min +"-"+max+"之间数字");
                return false;
              }
              totalScore += parseInt(val.score);
              scoreArr.push(val.score);
            } else {
              this.$message.warning("岗位职责在创建指标时,设置有误.");
              return false;
            }
          } else {
            this.$message.warning("岗位职责请填写正确数字");
          }
        }
      }
      for (let i = 0; i < this.dutyZhongdian.length; i++) {
        let val = this.dutyZhongdian[i];
        if (!val.score) {
          this.$message.warning("请先打完分数");
          return false;
        } else {
          if (!isNaN(val.score)) {
            let arScore = this.getMaxMin(val)
            if (arScore.length == 2) {
              let min = arScore[0];
              let max = arScore[1];
              if (val.score > max || val.score < min) {
                this.$message.warning("重点任务请填写"+ min +"-"+max+"之间数字");
                return false;
              }
              totalScore += parseInt(val.score);
              scoreArr.push(val.score);
            } else {
              this.$message.warning("重点任务在创建指标时,设置有误.");
              return false;
            }
          } else {
            this.$message.warning("重点任务请填写正确数字");
          }
        }
      }
      for (let i = 0; i < this.dutyMubiao.length; i++) {
        let val = this.dutyMubiao[i];
        if (!val.score) {
          this.$message.warning("请先打完分数");
          return false;
        } else {
          if (!isNaN(val.score)) {
            let arScore = this.getMaxMin(val)
            if (arScore.length == 2) {
              let min = arScore[0];
              let max = arScore[1];
              if (val.score > max || val.score < min) {
                this.$message.warning("目标任务请填写"+ min +"-"+max+"之间数字");
                return false;
              }
              totalScore += parseInt(val.score);
              scoreArr.push(val.score);
            } else {
              this.$message.warning("目标任务在创建指标时,设置有误.");
              return false;
            }
          } else {
            this.$message.warning("目标任务请填写正确数字");
          }
        }
      }
      this.scoreShow = true;
      this.totalScore = totalScore;
      return true;
    },
    //历史查询
    historySearch() {
      if (!this.search.year || !this.search.month) {
        this.$message.warning("请选择年份和月份后在搜索");
        return;
      }
      this.searchLoading = true;
      let data = {
        employeecode: this.$route.query.userCode,
        year: this.search.year,
        month: this.search.month,
        dbtype: this.$store.state.user.user.dbtype
      };
      new Promise((response, reject) => {
        getDutyDetail(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              if (response.data.data) {
                this.detailData = response.data.data.detail;
                this.form = response.data.data.stations;
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
                  type: "warning"
                });
              }
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
            this.searchLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //提交考核
    submitAssess() {
      if (this.compute()) {
        this.submitLoading = true;
        let data = {
          serialno: this.detailData.serialno,
          employeecode: this.detailData.employeecode,
          scorringcode: this.$store.state.user.user.usercode,
          total: this.totalScore
        };
        //遍历基础和关键打分数据
        data.dutyJiChu = [];
        data.dutyYiban = [];
        data.dutyZhongdian = [];
        data.dutyMubiao = [];
        this.dutyJichu.forEach(row => {
          data.dutyJiChu.push({ topicId: row.dutycode, score: row.score, scoretype: row.scoretype });
        });
        this.dutyYiban.forEach(row => {
          data.dutyYiban.push({ topicId: row.dutycode, score: row.score, scoretype: row.scoretype });
        });
        this.dutyZhongdian.forEach(row => {
          data.dutyZhongdian.push({ topicId: row.dutycode, score: row.score, scoretype: row.scoretype });
        });
        this.dutyMubiao.forEach(row => {
          data.dutyMubiao.push({ topicId: row.dutycode, score: row.score, scoretype: row.scoretype });
        });
        data.dutyJiChu = JSON.stringify(data.dutyJiChu);
        data.dutyYiban = JSON.stringify(data.dutyYiban);
        data.dutyZhongdian = JSON.stringify(data.dutyZhongdian);
        data.dutyMubiao = JSON.stringify(data.dutyMubiao);
        data.month = this.detailData.month;
        data.year =this.detailData.year;
        data.dbtype =this.$store.state.user.user.dbtype;
        new Promise((response, reject) => {
          dutyScoring(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
                this.$router.push({
                  path: "/home",
                  query: {}
                });
              } else {
                this.$message({
                  message: response.data.msg,
                  type: "error"
                });
              }
              this.tableLoading = false;
              this.submitLoading = false;
            })
            .catch(error => {
              reject(error);
            });
        });
        // let data = {
        //   scorringcode: 380065,
        //   total: 80,
        //   dutyJiChu:'[{ topicId: "2653", score: "4" },{ topicId: "2812", score: "4" },{ topicId: "2962", score: "4" },{ topicId: "3115", score: "4" }, { topicId: "3265", score: "4" }]',
        //   dutyYiban:'[{ topicId: "3995", score: "12" },{ topicId: "3996", score: "12" },{ topicId: "3997", score: "12" },{ topicId: "3998", score: "12" },{ topicId: "3999", score: "12" }]'
        // };
        // let a = "2019-3-0040484,2019-3-0040430,2019-3-0022013,2019-3-0130116,2019-3-0010717,2019-3-0010776,2019-3-0010935,2019-3-0041439,2019-3-0011443,2019-3-0040495,2019-3-0041428,2019-3-0041455,2019-3-0130168,2019-3-0041427,2019-3-0011136,2019-3-0050890,2019-3-0050905,2019-3-0040471,2019-3-0040363,2019-3-0031477,2019-3-0050602,2019-3-0050987,2019-3-0041423,2019-3-0041372,2019-3-0041348,2019-3-0031336,2019-3-0020722,2019-3-0040347,2019-3-0022115,2019-3-0030393,2019-3-0130155,2019-3-0320006,2019-3-0040311,2019-3-0040338,2019-3-0040370,2019-3-0040371,2019-3-0054087,2019-3-0370006,2019-3-0041328,2019-3-0050908,2019-3-0041398,2019-3-0050643,2019-3-0041295,2019-3-0041299,2019-3-0041413,2019-3-0051378,2019-3-0054307,2019-3-0054313,2019-3-0031276,2019-3-0040416,2019-3-370067,2019-3-0040498,2019-3-370066,2019-3-0020777,2019-3-0330011,2019-3-0330021,2019-3-0030105,2019-3-0020826,2019-3-0041385,2019-3-0022085,2019-3-0020873,2019-3-0020783,2019-3-0020821,2019-3-0020876,2019-3-0022366,2019-3-0011061,2019-3-0020836,2019-3-0011543,2019-3-0010779,2019-3-0020814,2019-3-0022199,2019-3-0022365,2019-3-0022405,2019-3-0380063,2019-3-0022556,2019-3-0030415,2019-3-0041344,2019-3-0040322,2019-3-0030364,2019-3-0031273,2019-3-0030427,2019-3-0030454,2019-3-0031359,2019-3-0041320,2019-3-0031272,2019-3-0040488,2019-3-0020774,2019-3-0020835,2019-3-0020931,2019-3-0020962,2019-3-0022023,2019-3-0022174,2019-3-0022353,2019-3-0022554,2019-3-0022587,2019-3-0031345,2019-3-0031341,2019-3-0030297,2019-3-0031285,2019-3-0031286,2019-3-0031287,2019-3-0031291,2019-3-0031313,2019-3-0031319,2019-3-0031330,2019-3-0031340,2019-3-0031392,2019-3-0031448";
        // let b = "0040484,0040430,0022013,0130116,0010717,0010776,0010935,0041439,0011443,0040495,0041428,0041455,0130168,0041427,0011136,0050890,0050905,0040471,0040363,0031477,0050602,0050987,0041423,0041372,0041348,0031336,0020722,0040347,0022115,0030393,0130155,0320006,0040311,0040338,0040370,0040371,0054087,0370006,0041328,0050908,0041398,0050643,0041295,0041299,0041413,0051378,0054307,0054313,0031276,0040416,370067,0040498,370066,0020777,0330011,0330021,0030105,0020826,0041385,0022085,0020873,0020783,0020821,0020876,0022366,0011061,0020836,0011543,0010779,0020814,0022199,0022365,0022405,0380063,0022556,0030415,0041344,0040322,0030364,0031273,0030427,0030454,0031359,0041320,0031272,0040488,0020774,0020835,0020931,0020962,0022023,0022174,0022353,0022554,0022587,0031345,0031341,0030297,0031285,0031286,0031287,0031291,0031313,0031319,0031330,0031340,0031392,0031448";
        // let aArr = a.split(",");
        // let bArr = b.split(",");
        // for (let i = 0; i < 108; i++) {
        //   data.serialno = aArr[i];
        //   data.employeecode = bArr[i];
        //   new Promise((response, reject) => {
        //     scoring(qs.stringify(data))
        //       .then(response => {
        //         if (response.data.code == 0) {
        //           this.$message({
        //             message: response.data.msg,
        //             type: "success"
        //           });
        //           this.$router.push({
        //             path: "/home",
        //             query: {}
        //           });
        //         } else {
        //           this.$message({
        //             message: response.data.msg,
        //             type: "error"
        //           });
        //         }
        //         this.tableLoading = false;
        //         this.submitLoading = false;
        //       })
        //       .catch(error => {
        //         reject(error);
        //       });
        //   });
        // }
      }
    },
    //预览
    preview(path) {
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        "summaryattachment/download?savepath=" +
        path;
    }
  }
};
</script>
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
    margin-bottom: 20px;
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
}
.search {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px 0;
  margin: 10px;
  box-sizing: border-box;
  .el-form-item {
    margin: 0px;
  }
  .el-button {
    margin-left: 10px;
  }
}
.form-menu {
  margin-bottom: 0px;
}
</style>
