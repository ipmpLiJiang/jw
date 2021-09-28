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
            <el-form-item label="评分季度">
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
            <div class="label">季结内容:</div>
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
          <li class="li-title">
            <div class="title">基础指标(总分15)</div>
          </li>
          <li
            v-for="(item,index) in dutyJichu" :key="'a'+index"
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
                {{item.zpsm}}
              </el-col>
            </el-row>
            <div class="value">
              <el-radio-group v-model="item.score" @change="redioChange" :disabled="detailData.isedit == 1 ? true : false" style="display:list-item">
              <el-row type="flex" justify="space-around">
                <el-col :span="5">
                  <el-radio :label="item.ascore" border>优秀</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.bscore" border>良好</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.cscore" border>一般</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.dscore" border>较差</el-radio>
                </el-col>
                <el-col :span="4">
                <span v-html="jieguo(item)"></span>
                </el-col>
              </el-row>
              </el-radio-group>
            </div>
              <el-row class="cpsm" v-show="item.score == item.dscore?true:false">
                <el-col :span="2">
                  <font style="color:red"> * </font>差评原因：
                  <br v-if="detailData.isedit == 0"><font v-if="detailData.isedit == 0">(限制80字)</font>
                </el-col>
                <el-col :span="17">
                  <el-input v-show="detailData.isedit == 0 ? true :false" maxlength="80" type="textarea" v-model="item.cpsm"></el-input>
                  <span v-show="detailData.isedit == 1 ? true :false">{{item.cpsm}}</span>
                </el-col>
              </el-row>
              <el-col class="cpsmKong" v-show="item.score == item.dscore?false:true">
                &nbsp;
              </el-col>
          </li>
          <li class="li-title">
            <div class="title">岗位职责(总分20)</div>
          </li>
          <li
            v-for="(item,index) in dutyYiban" :key="'b'+index"
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
                {{item.zpsm}}
              </el-col>
            </el-row>
            <div class="value">
              <el-radio-group v-model="item.score" @change="redioChange" :disabled="detailData.isedit == 1 ? true : false" style="display:list-item">
              <el-row type="flex" justify="space-around">
                <el-col :span="5">
                  <el-radio :label="item.ascore" border>优秀</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.bscore" border>良好</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.cscore" border>一般</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.dscore" border>较差</el-radio>
                </el-col>
                <el-col :span="4">
                <span v-html="jieguo(item)"></span>
                </el-col>
              </el-row>
              </el-radio-group>
            </div>
              <el-row class="cpsm" v-show="item.score == item.dscore?true:false">
                <el-col :span="2">
                  <font style="color:red"> * </font>差评原因：
                  <br v-if="detailData.isedit == 0"><font v-if="detailData.isedit == 0">(限制80字)</font>
                </el-col>
                <el-col :span="17">
                  <el-input v-show="detailData.isedit == 0 ? true :false" maxlength="80" type="textarea" v-model="item.cpsm"></el-input>
                  <span v-show="detailData.isedit == 1 ? true :false">{{item.cpsm}}</span>
                </el-col>
              </el-row>
              <el-col class="cpsmKong" v-show="item.score == item.dscore?false:true">
                &nbsp;
              </el-col>
          </li>
          <li class="li-title">
            <div class="title">重点任务(总分25)</div>
          </li>
          <li
            v-for="(item,index) in dutyZhongdian" :key="'c'+index"
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
                {{item.zpsm}}
              </el-col>
            </el-row>
            <div class="value">
              <el-radio-group v-model="item.score" @change="redioChange" :disabled="detailData.isedit == 1 ? true : false" style="display:list-item">
              <el-row type="flex" justify="space-around">
                <el-col :span="5">
                  <el-radio :label="item.ascore" border>优秀</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.bscore" border>良好</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.cscore" border>一般</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.dscore" border>较差</el-radio>
                </el-col>
                <el-col :span="4">
                <span v-html="jieguo(item)"></span>
                </el-col>
              </el-row>
              </el-radio-group>
            </div>
              <el-row class="cpsm" v-show="item.score == item.dscore?true:false">
                <el-col :span="2">
                  <font style="color:red"> * </font>差评原因：
                  <br v-if="detailData.isedit == 0"><font v-if="detailData.isedit == 0">(限制80字)</font>
                </el-col>
                <el-col :span="17">
                  <el-input v-show="detailData.isedit == 0 ? true :false" maxlength="80" type="textarea" v-model="item.cpsm"></el-input>
                  <span v-show="detailData.isedit == 1 ? true :false">{{item.cpsm}}</span>
                </el-col>
              </el-row>
              <el-col class="cpsmKong" v-show="item.score == item.dscore?false:true">
                &nbsp;
              </el-col>
          </li>
          <li class="li-title">
            <div class="title">目标任务(总分25)</div>
          </li>
          <li
            v-for="(item,index) in dutyMubiao" :key="'d'+index"
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
                {{item.zpsm}}
              </el-col>
            </el-row>
            <div class="value">
              <el-radio-group v-model="item.score" @change="redioChange" :disabled="detailData.isedit == 1 ? true : false" style="display:list-item">
              <el-row type="flex" justify="space-around">
                <el-col :span="5">
                  <el-radio :label="item.ascore" border>优秀</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.bscore" border>良好</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.cscore" border>一般</el-radio>
                </el-col>
                <el-col :span="5">
                  <el-radio :label="item.dscore" border>较差</el-radio>
                </el-col>
                <el-col :span="4">
                <span v-html="jieguo(item)"></span>
                </el-col>
              </el-row>
              </el-radio-group>
            </div>
              <el-row class="cpsm" v-show="item.score == item.dscore?true:false">
                <el-col :span="2">
                  <font style="color:red"> * </font>差评原因：
                  <br v-if="detailData.isedit == 0"><font v-if="detailData.isedit == 0">(限制80字)</font>
                </el-col>
                <el-col :span="17">
                  <el-input v-show="detailData.isedit == 0 ? true :false" maxlength="80" type="textarea" v-model="item.cpsm"></el-input>
                  <span v-show="detailData.isedit == 1 ? true :false">{{item.cpsm}}</span>
                </el-col>
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
            <div v-else>
              <el-button
                type="primary"
                @click="submitAssess"
                :loading="submitLoading"
              >提交</el-button>
              <font style="color:red">&nbsp;&nbsp;&nbsp;{{laberror}}</font>
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
import { getDetail, queryByUser, scoring } from "@/api/home/home";
import { download } from "@/api/common/common";
import qs from "qs";
export default {
  data() {
    return {
      form: {},
      quarterOptions: [
       {
          value: "1",
          label: "第1季度"
        },
        {
          value: "2",
          label: "第2季度"
        },
        {
          value: "3",
          label: "第3季度"
        },
        {
          value: "4",
          label: "第4季度"
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
      laberror: '',
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
    jieguo (item) {
      let v = '';
      let color = '';
      if (item.score == item.ascore) {
        v = '优秀'
        color = '#FFFF00'
      } else if (item.score == item.bscore) {
        v = '良好'
        color = '#1AFD9C'
      } else if (item.score == item.cscore) {
        v = '一般'
        color = '#00FFFF'
      } else if (item.score == item.dscore) {
        v = '较差'
        color = '#FF9797'
      }
      return '<div style="width: 80px;height: 40px;background:'+color+';border-radius: 3px;font-size:14px;display: flex;align-items: center;justify-content: center;"><b>' + v + '</b></div>';
    },
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
        getDetail(qs.stringify(data))
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
      for (let i = 0; i < this.dutyYiban.length; i++) {
        let val = this.dutyYiban[i];
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
      for (let i = 0; i < this.dutyZhongdian.length; i++) {
        let val = this.dutyZhongdian[i];
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
      for (let i = 0; i < this.dutyMubiao.length; i++) {
        let val = this.dutyMubiao[i];
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
      this.scoreShow = true;
      this.totalScore = totalScore;
      return true;
    },
    //历史查询
    historySearch() {
      if (!this.search.year || !this.search.month) {
        this.$message.warning("请选择年份和季份后在搜索");
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
        getDetail(qs.stringify(data))
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
    redioChange () {
      this.laberror = ''
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
        this.laberror = ''
        this.dutyJichu.forEach(row => {
          data.dutyJiChu.push({ topicId: row.dutycode, score: row.score, scoretype: row.scoretype, cpsm: row.score != row.dscore?'':row.cpsm, zpsm: row.zpsm });
          if (row.score == row.dscore && (row.cpsm == null || row.cpsm == '' || row.cpsm == undefined)) {
            this.laberror = '基础指标，差评原因 不能为空.'
          }
        });
        if(this.laberror == ''){
          this.dutyYiban.forEach(row => {
            data.dutyYiban.push({ topicId: row.dutycode, score: row.score, scoretype: row.scoretype, cpsm: row.score != row.dscore?'':row.cpsm, zpsm: row.zpsm });
            if (row.score == row.dscore && (row.cpsm == null || row.cpsm == '' || row.cpsm == undefined)) {
              this.laberror = '岗位职责，差评原因 不能为空.'
            }
          });
        }
        if(this.laberror == ''){
          this.dutyZhongdian.forEach(row => {
            data.dutyZhongdian.push({ topicId: row.dutycode, score: row.score, scoretype: row.scoretype, cpsm: row.score != row.dscore?'':row.cpsm, zpsm: row.zpsm });
            if (row.score == row.dscore && (row.cpsm == null || row.cpsm == '' || row.cpsm == undefined)) {
              this.laberror = '重点任务，差评原因 不能为空.'
            }
          });
        }
        if(this.laberror == ''){
          this.dutyMubiao.forEach(row => {
            data.dutyMubiao.push({ topicId: row.dutycode, score: row.score, scoretype: row.scoretype, cpsm: row.score != row.dscore?'':row.cpsm, zpsm: row.zpsm });
            if (row.score == row.dscore && (row.cpsm == null || row.cpsm == '' || row.cpsm == undefined)) {
              this.laberror = '目标任务，差评原因 不能为空.'
            }
          });
        }
        if (this.laberror != ''){
          this.$message.warning(this.laberror);
          this.tableLoading = false;
          this.submitLoading = false;
          return
        }
        data.dutyJiChu = JSON.stringify(data.dutyJiChu);
        data.dutyYiban = JSON.stringify(data.dutyYiban);
        data.dutyZhongdian = JSON.stringify(data.dutyZhongdian);
        data.dutyMubiao = JSON.stringify(data.dutyMubiao);
        data.month = this.detailData.month;
        data.year =this.detailData.year;
        data.dbtype =this.$store.state.user.user.dbtype;
        new Promise((response, reject) => {
          scoring(qs.stringify(data))
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
.cpsm {
    padding-top:15px;
    padding-bottom:15px;
  }
.cpsmKong {
    padding-top:1px;
    padding-bottom:1px;
  }
</style>
