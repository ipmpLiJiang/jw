<template>
  <view
    class="bg-white"
    style="height: 100vh;"
  >
    <cu-custom
      bgColor="bg-cyan"
      :isBack="false"
    >
      <block slot="content">检测数据</block>
    </cu-custom>
    <view class="bg-white margin-top-sm">
      <view class=" text-lg">
        <text class="cuIcon-title  text-green"></text>
        <text class="text-lg text-black text-bold">添加数据</text>
        <text class="text-sm">(请添加血糖数据及出汗情况)</text>
      </view>
    </view>
    <view
      class="cu-bar flex bg-gray r"
      style="min-height: 61px;"
    >
      <view class="  flex-sub flex text-center">
        <view class="flex-sub text-sl">
          <text
            class="cuIcon-shuidi  "
            :class="checkCount>=3?'text-green':'text-gray'"
          ></text>
        </view>

        <view class="flex-sub text-sl ">
          <text
            class="cuIcon-sunfilling"
            :class="checkCount>=3?'text-green':'text-gray'"
          ></text>
        </view>
        <view
          class="flex-sub text-sl"
          @tap.stop="showAdd"
        >
          <text
            class="cuIcon-roundadd"
            :class="checkCount>=3?'text-gray':'text-green'"
          ></text>
        </view>
      </view>
    </view>
    <view class=" bg-white margin-top-sm">
      <view class=" text-lg">
        <text class="cuIcon-title  text-green"></text>
        <text class="text-lg text-black text-bold">今日血糖</text>
        <text class="text-xs"></text>
      </view>
    </view>
    <view class="cu-bar bg-cuorange  flex  r">
      <view class="flex-sub flex">
        <view class="flex-sub text-center">
          <view class="cuh">
            <text class="text-black  text-sl">{{bloodSugar_1.Value=='0'?'':bloodSugar_1.Value}}</text>
          </view>
          <view>
            <text class="text-white text-sm">第一次数据</text>
          </view>
        </view>
        <view class="flex-sub text-center">
          <view class="cuh">
            <text class="text-black  text-sl">{{bloodSugar_2.Value=='0'?'':bloodSugar_2.Value}}</text>
          </view>
          <view>
            <text class="text-white text-sm">第二次数据</text>
          </view>
        </view>
        <view class="flex-sub text-center">
          <view class="cuh">
            <text class="text-black  text-sl">{{bloodSugar_3.Value=='0'?'':bloodSugar_3.Value}}</text>
          </view>
          <view>
            <text class="text-white text-sm">第三次数据</text>
          </view>
        </view>
      </view>
    </view>
    <view class="bg-white ">
      <text class="text-sm margin-left">注意：每次血糖检测需要间隔2小时以上</text>
    </view>
    <view class=" bg-white margin-top-sm">
      <view class=" text-lg flex">
        <view class="flex-sub flex"><text class="cuIcon-title  text-green"></text>
          <view class="flex-sub"><text class="text-lg text-black text-bold">今日汗糖</text></view>
          <view class="flex-sub text-right padding-right-sm"><text
              class="text-sm text-right  text-green"
              @tap.stop="showHistory"
            >历史数据</text></view>
        </view>
        <!-- <text class="text-sm">(正常值：74-106mg/dL)</text> -->
        <!-- <view class="flex-sub"><button class="cu-btn  text-green" @tap.stop="endGenerate">结束生成</button></view>
				<view class="flex-sub"><input type="number" class="text-xl sm-border"
						style="border:1rpx  #4CD964 solid; " v-model="msnumber" placeholder="间隔ms" /></view>
				<view class="flex-sub"><input type="number" class="text-xl sm-border"
						style="border:1rpx  #4CD964 solid;" v-model="sendnumber" placeholder="发送ms" /></view> -->
      </view>
    </view>
    <view class="cu-bar bg-cyan  flex r">
      <view class="flex-sub flex">
        <view class="flex-sub text-center">
          <view class="cuh2 margin-top-sm">
            <text
              class="text-black "
              style="font-size: 40rpx;"
            >{{sweetSugar_1.Value=='0.0'?'':sweetSugar_1.Value+'μmol'}}</text>
          </view>
          <view>
            <!--<text class="text-green text-sm" @tap.stop="first">第一次数据{{!isShow&&opIndex==1?pakgeNum+'生成中...':''}}</text>-->
            <button
              class="cu-btn text-green"
              @tap.stop="getGroupNum"
            >第一次数据{{!isShow&&opIndex==1?pakgeNum+'生成中...':''}}</button>
          </view>
        </view>
        <view class="flex-sub text-center">
          <view class="cuh2 margin-top-sm">
            <text
              class="text-black"
              style="font-size: 40rpx;"
            >{{sweetSugar_2.Value=='0.0'?'':sweetSugar_2.Value+'μmol'}}</text>
          </view>
          <view>
            <!--<text class="text-green text-sm" @tap.stop="second">第二次数据{{!isShow&&opIndex==2?pakgeNum+'生成中...':''}}</text>-->
            <button
              class="cu-btn text-green"
              @tap.stop="second"
            >第二次数据{{!isShow&&opIndex==2?pakgeNum+'生成中...':''}}</button>
          </view>
        </view>
        <view class="flex-sub text-center ">
          <view class="cuh2 margin-top-sm">
            <text
              class="text-black "
              style="font-size: 40rpx;"
            >{{sweetSugar_3.Value=='0.0'?'':sweetSugar_3.Value+'μmol'}}</text>
          </view>
          <view>
            <!--<text class="text-white text-sm " @tap.stop="third">第三次数据{{!isShow&&opIndex==3?pakgeNum+'生成中...':''}}</text>-->
            <button
              class="cu-btn text-green"
              @tap.stop="third"
            >第三次数据{{!isShow&&opIndex==3?pakgeNum+'生成中...':''}}</button>
          </view>
        </view>
      </view>
    </view>
    <view class="bg-white ">
      <text class="text-sm margin-left">注意：汗糖检测与血糖检测间隔需在15分钟以内</text>
    </view>
    <view class=" bg-white margin-top-sm">
      <view class=" text-lg ">
        <text class="cuIcon-title  text-green"></text>
        <text class="text-lg text-black text-bold">汗糖/血糖数据对比</text>
      </view>
    </view>
    <view class="bg-white flex">
      <view class="bg-white nav text-center flex-sub">
        <view
          class="cu-item"
          :class="0==TabCur?'text-blue cur':''"
          :data-id="0"
          @tap.stop="tabSelect"
        >
          日
        </view>
        <view
          class="cu-item"
          :class="1==TabCur?'text-blue cur':''"
          :data-id="1"
          @tap.stop="tabSelect"
        >
          周
        </view>
        <view
          class="cu-item"
          :class="2==TabCur?'text-blue cur':''"
          :data-id="2"
          @tap.stop="tabSelect"
        >
          月
        </view>
      </view>
    </view>
    <view class="cu-bar bg-white">
      <view class="charts-box ">
        <qiun-data-charts
          type="column"
          v-if="TabCur==0"
          :chartData="chartDataDay"
          :echartsH5="true"
          :echartsApp="true"
        />
        <qiun-data-charts
          type="column"
          v-if="TabCur==1"
          :chartData="chartDataWeek"
          :echartsH5="true"
          :echartsApp="true"
        />
        <qiun-data-charts
          type="line"
          v-if="TabCur==2"
          :chartData="chartDataMonth"
          :echartsH5="true"
          :echartsApp="true"
        />
      </view>
    </view>

  </view>
</template>

<script>
import Bluetooth from '@/common/bluetooth.js';
let bluetooth = new Bluetooth();
export default {
  data () {
    return {
      paired: [],
      pairedIndex: 0,
      msnumber: 30,
      sendnumber: 0,
      manufacturer: this.$store.getters.getManufacturer,
      isShow: true,
      isEnd: 1,
      pakgeNum: 0,
      groupNum: 0, //循环使用 当前组数
      totalGroupNum: 0,//总组数
      onceStr: '', //81 包 ，存储每一包的数据
      opIndex: 0,
      electricStr: '',
      dataUx: [], //存放返回的字符串
      cmdArr: [
        '0103000000320036', '0103003200320068', '010300640032009A', '01030096003200CC', '010300C8003200FE',
        '010300FA00320130', '0103012C00320063', '0103015E00320095', '01030190003200C7', '010301C2003200F9',
        '010301F40032012B', '010302260032005E', '0103025800320090', '0103028A003200C2', '010302BC003200F4',
        '010302EE00320126', '0103032000320059', '010303520032008B', '01030384003200BD', '010303B6003200EF',
        '010303E800320121', '0103041A00320054', '0103044C00320086', '0103047E003200B8', '010304B0003200EA',
        '010304E20032011C', '010305140032004F', '0103054600320081', '01030578003200B3', '010305AA003200E5',
        '010305DC00320117', '0103060E0032004A', '010306400032007C', '01030672003200AE', '010306A4003200E0',
        '010306D600320112', '0103070800320045', '0103073A00320077', '0103076C003200A9', '0103079E003200DB',
        '010307D00032010D', '0103080200320040', '0103083400320072', '01030866003200A4', '01030898003200D6',
        '010308CA00320108', '010308FC0032013A', '0103092E0032006D', '010309600032009F', '01030992003200D1',
        '010309C400320103', '010309F600320135', '01030A2800320068', '01030A5A0032009A', '01030A8C003200CC',
        '01030ABE003200FE', '01030AF000320130', '01030B2200320063', '01030B5400320095', '01030B86003200C7',
        '01030BB8003200F9', '01030BEA0032012B', '01030C1C0032005E', '01030C4E00320090', '01030C80003200C2',
        '01030CB2003200F4', '01030CE400320126', '01030D1600320059', '01030D480032008B', '01030D7A003200BD',
        '01030DAC003200EF', '01030DDE00320121', '01030E1000320054', '01030E4200320086', '01030E74003200B8',
        '01030EA6003200EA', '01030ED80032011C', '01030F0A0032004F', '01030F3C00320081', '01030F6E003200B3',
        '01030FA0000C00BF'
      ],
      sweetSugar_1: {
        Value: ''
      },
      sweetSugar_2: {
        Value: ''
      },
      sweetSugar_3: {
        Value: ''
      },
      bloodSugar_1: {
        Value: ''
      },
      bloodSugar_2: {
        Value: ''
      },
      bloodSugar_3: {
        Value: ''
      },
      checkCount: 0,
      chartType: 'column',
      chartData: {
        categories: [],
        series: [{
          name: "汗糖值",
          data: []
        }, {
          name: "血糖值",
          data: []
        }]
      },
      chartDataDay: {
        categories: [],
        series: [{
          name: "汗糖值",
          data: []
        }, {
          name: "血糖值",
          data: []
        }]
      },
      chartDataWeek: {
        categories: [],
        series: [{
          name: "汗糖值",
          data: []
        }, {
          name: "血糖值",
          data: []
        }]
      },
      chartDataMonth: {
        categories: [],
        series: [{
          name: "汗糖值",
          data: []
        }, {
          name: "血糖值",
          data: []
        }]
      },
      TabCur: 0,
    }
  },
  computed: {

  },
  watch: {
    '$store.state.bluetooth.paired': {
      handler (e) {
        this.paired = e;
        var flag = 0;
        if (e != null && e.length > 0) {
          for (var i = 0; i < e.length; i++) {
            if (e[i].status) {
              flag = 1
              this.pairedIndex = i
              break
            }
          }
        }
        if (flag == 0) {
          this.endGenerate() //没有连接状态的设备 停止获取数据
        }
      },
      immediate: true,
      deep: true
    },

    //组数循环
    groupNum (newValue) {
      let that = this
      that.onceStr = ''
      console.info(newValue)
      if (newValue > 0) {
        if (newValue >= totalGroupNum + 1) {//结束所有的组获取命令
          setTimeout(() => {
            //清空
            that.$store.dispatch('writeManufacturer', {
              item: that.paired[that.pairedIndex],
              manufacturer: that.manufacturer[1],
              writeCode: '0110100400010204D200FE',
              index: 0
            }).then(res => {
              uni.showToast({
                title: res.errMsg,
                icon: 'none',
                position: 'bottom'
              });
            });
          }, 400)
          setTimeout(() => {
            //使能
            that.$store.dispatch('writeManufacturer', {
              item: that.paired[that.pairedIndex],
              manufacturer: that.manufacturer[1],
              writeCode: '01100FD2000102000000F5',
              index: 0
            }).then(res => {
              uni.showToast({
                title: res.errMsg,
                icon: 'none',
                position: 'bottom'
              });
            });
          }, 800)
        }
        else {
            var gCmd=that.calcGroupCommand(that.groupNum)
            that.uploadData(gCmd,1)
        }
      }

    },
    pakgeNum (newValue) {
      let that = this
      that.onceStr = ''
      console.info(newValue)
      if (newValue > 0) {
        //	if (newValue > 201) {
        //console.info('jieshu')
        //console.info(that.dataUx.toString())
        if (newValue == 202) {
          that.isEnd = 1
          that.loadDataToserver()
          setTimeout(()=>{
            that.groupNum+=1
          },1000)
          // setTimeout(() => {
          // 	//清空
          // 	that.$store.dispatch('writeManufacturer', {
          // 		item: that.paired[that.pairedIndex],
          // 		manufacturer: that.manufacturer[1],
          // 		writeCode: '0110100400010204D200FE',
          // 		index: 0
          // 	}).then(res => {
          // 		uni.showToast({
          // 			title: res.errMsg,
          // 			icon: 'none',
          // 			position: 'bottom'
          // 		});
          // 	});
          // }, 400)
          // setTimeout(() => {
          // 	//使能
          // 	that.$store.dispatch('writeManufacturer', {
          // 		item: that.paired[that.pairedIndex],
          // 		manufacturer: that.manufacturer[1],
          // 		writeCode: '01100FD2000102000000F5',
          // 		index: 0
          // 	}).then(res => {
          // 		uni.showToast({
          // 			title: res.errMsg,
          // 			icon: 'none',
          // 			position: 'bottom'
          // 		});
          // 	});
          // }, 800)

          //}
        } else {

          //setTimeout(() => {
          if (newValue == 201) {
            that.readManufacturerReadData('01030FA0000C00BF')
          } else {
            that.readManufacturerReadData(that.calcCommand(newValue - 1, 20))
          }
          //}, 100)
        }
      }
    }
  },
  onLoad () {
    this.fetch()
    uni.$on("handleFun", () => {
      this.fetch()
      // 清除监听
      // uni.$off('handleFun');
    })
  },
  components: {

  },
  methods: {
    calcCommand (Pack_ID, Length) {
      var Command = [];
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command[0] = 1;
      Command[1] = 3;
      var sum = 0;
      var Start_Address = Pack_ID * Length;
      Command[2] = Start_Address >> 8;
      Command[3] = Start_Address & 0xFF;
      Command[4] = Length >> 8;
      Command[5] = Length & 0xFF;
      for (var j = 0; j < 6; j++) {
        sum += Command[j];
      }
      Command[6] = sum >> 8;
      Command[7] = sum & 0xFF;
      //生成完毕，在此执行发送指令
      var str_cmd = ""
      for (var i = 0; i < 8; i++) {
        //console.info(Command[i])
        str_cmd += this.pad(parseInt(Command[i]).toString(16), 2)
      }
      //console.info(str_cmd)
      return str_cmd
    },
    calcGroupCommand (i) {
      var Command = [];
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
      Command.push('0');
     // 01 10 10 04 00 02 04 00 03 00
      Command[0] = 1;
      Command[1] = 16;
      Command[2] = 16;
      Command[3] = 4;
      Command[4] = 0;
      Command[5] = 2;
      Command[6] = 4;
      Command[7] = 0;
      Command[8] = 3;
      var sum = 0;
      var Start_Address = i;
      Command[9] = Start_Address >> 8;
      Command[10] = Start_Address & 0xFF;

      for (var j = 0; j < 11; j++) {
        sum += Command[j];
      }
      Command[11] = sum >> 8;
      Command[12] = sum & 0xFF;
      //生成完毕，在此执行发送指令
      var str_cmd = ""
      for (var i = 0; i < 13; i++) {
        //console.info(Command[i])
        str_cmd += this.pad(parseInt(Command[i]).toString(16), 2)
      }
      //console.info(str_cmd)
      return str_cmd
    },
    pad (num, n) {
      var tbl = [];
      var len = n - num.toString().length;
      if (len <= 0) return num;
      if (!tbl[len]) tbl[len] = (new Array(len + 1)).join('0');
      return tbl[len] + num;
    },
    showAdd () {
      uni.navigateTo({
        url: '/pages/Check/CheckAdd'
      })
    },
    showHistory () {
      uni.navigateTo({
        url: '/pages/Check/History'
      })
    },
    tabSelect (e) {
      this.TabCur = e.currentTarget.dataset.id;

    },
    ceshi () {
      uni.reLaunch({
        url: '/pages/Check/CheckAdd'
      });
    },
    endGenerate () {
      this.calcCommand(0, 20);
      this.isEnd = 1
      this.pakgeNum = 0
      this.isShow = true
    },
    loadDataToserver () {
      let that = this
      let userInfo = that.$store.getters['user/getUserInfo']
      let strU = that.dataUx.join(",")
      that.$api.check.addsweatsugarpkg({
        user_id: userInfo.Id,
        pkg: strU.replace(/,/g, ''),
        source: that.paired[that.pairedIndex].name
      }).then(res => {
        console.info(res)
        this.isShow = true
        if (res.Code == "1") {
          if (that.opIndex == 1) {
            that.sweetSugar_1.Value = that.fomatFloat(parseFloat(res.CalVal), 1)
          }
          if (that.opIndex == 2) {
            that.sweetSugar_2.Value = that.fomatFloat(parseFloat(res.CalVal), 1)
          }
          if (that.opIndex == 3) {
            that.sweetSugar_3.Value = that.fomatFloat(parseFloat(res.CalVal), 1)
          }
        }
      })
    },
    getSource () {
      return this.paired[this.pairedIndex].name
    },
    fetch () {
      console.info("fetch")
      var that = this
      let userInfo = that.$store.getters['user/getUserInfo']
      that.$api.check.reportCheck({
        userId: userInfo.Id
      }).then(res => {
        if (res.Code == "1") {
          let data = res.Data
          console.info(data)
          let SweetSugarData = data.DailyData
          let monthlyData = data.MonthlyData
          let weeklyData = data.WeeklyData
          let threeData = data.Top3DailyData
          if (threeData != null) {
            this.checkCount = 0
            for (let i = 0; i < threeData.length; i++) {

              if (i == 0) {
                this.sweetSugar_1.Value = this.fomatFloat(parseFloat(threeData[i].SweetSugarData), 1)
                this.bloodSugar_1.Value = threeData[i].BloodSugarData
                if (parseFloat(threeData[i].BloodSugarData) > 0) {
                  this.checkCount += 1
                }
              }
              if (i == 1) {
                this.sweetSugar_2.Value = this.fomatFloat(parseFloat(threeData[i].SweetSugarData), 1)
                this.bloodSugar_2.Value = threeData[i].BloodSugarData
                if (parseFloat(threeData[i].BloodSugarData) > 0) {
                  this.checkCount += 1
                }
              }
              if (i == 2) {
                this.sweetSugar_3.Value = this.fomatFloat(parseFloat(threeData[i].SweetSugarData), 1)
                this.bloodSugar_3.Value = threeData[i].BloodSugarData
                if (parseFloat(threeData[i].BloodSugarData) > 0) {
                  this.checkCount += 1
                }
              }


            }
          }

          if (SweetSugarData != null) {
            for (let i = 0; i < SweetSugarData.length; i++) {
              that.$set(that.chartDataDay.categories, i, SweetSugarData[i].Point)

              that.$set(that.chartDataDay.series[0].data, i, SweetSugarData[i].SweetSugarData)
              that.$set(that.chartDataDay.series[1].data, i, SweetSugarData[i].BloodSugarData)
            }
          }
          if (monthlyData != null) {
            for (let i = 0; i < monthlyData.length; i++) {
              that.$set(that.chartDataMonth.categories, i, monthlyData[i].Point)

              that.$set(that.chartDataMonth.series[0].data, i, monthlyData[i].SweetSugarData)
              that.$set(that.chartDataMonth.series[1].data, i, monthlyData[i].BloodSugarData)
            }
          }
          if (weeklyData != null) {
            for (let i = 0; i < weeklyData.length; i++) {
              that.$set(that.chartDataWeek.categories, i, weeklyData[i].Point)

              that.$set(that.chartDataWeek.series[0].data, i, weeklyData[i].SweetSugarData)
              that.$set(that.chartDataWeek.series[1].data, i, weeklyData[i].BloodSugarData)
            }
          }
          //that.chartDataDay = that.chartData



        } else {
          uni.showToast({
            icon: "none",
            title: "提交数据出错，请联系管理员",
          })
        }
      })
    },
    async first () {
      this.uploadData('0110100400020400030001002F', 1)
    },
    async second () {
      this.uploadData('01101004000204000300020030', 2)
    },
    async third () {
      this.uploadData('01101004000204000300030031', 3)
    },
    fomatFloat (value, n) {
      var f = Math.round(value * Math.pow(10, n)) / Math.pow(10, n);
      var s = f.toString();
      var rs = s.indexOf('.');
      if (rs < 0) {
        s += '.';
      }
      for (var i = s.length - s.indexOf('.'); i <= n; i++) {
        s += "0";
      }
      return s;
    },
    async uploadData (writeCode, opIndex) {
      let that = this
      that.isEnd = 1
      if (that.paired.length > 0 && that.paired[that.pairedIndex].status) {
        that.isShow = false
        that.opIndex = opIndex
        //console.info('88888')

        that.pakgeNum = 0
        that.dataUx = []
        let manufacturer = that.manufacturer
        let item = that.paired[that.pairedIndex]
        await bluetooth.notifyBLECharacteristicValueChange(item.deviceId, manufacturer[0].serviceId,
          manufacturer[0].characteristicId)
          .then(res => {
            //console.info(manufacturer[0].characteristicId)
            uni.onBLECharacteristicValueChange(function (res) {
              //console.info(3222)

              let str = bluetooth.ab2hex(res.value)

              if (str.indexOf('01101004') == 0 && that.isEnd == 0) { // 组命令获取
                that.pakgeNum = 1
                that.dataUx = []
              } else { // 其他为包命令
                if (that.isEnd == 0) {
                  that.onceStr += str
                }
                let le = parseInt(that.onceStr.length)
                if (that.pakgeNum == 201 && le == 58) {
                  that.dataUx.push(that.onceStr.substr(6, le - 10))

                  setTimeout(() => {
                    if (that.isEnd == 0) {
                      that.pakgeNum += 1 //执行下一包 202 结束
                    }
                  }, (that.msnumber == '' ? 0 : that
                    .msnumber)) // that.msnumber 页面上设置的间隔30ms
                } else {
                  if (le == 90 && that.pakgeNum < 201) {
                    that.dataUx.push(that.onceStr.substr(6, le - 10))

                    setTimeout(() => {
                      if (that.isEnd == 0) {
                        that.pakgeNum += 1 //执行下一包
                      }
                    }, (that.msnumber == '' ? 0 : that.msnumber))
                  }
                }
                // if (that.pakgeNum == 201 ) {
                // 	setTimeout(() => {
                //         console.info(that.onceStr)
                // 		let le = parseInt(that.onceStr.length)
                // 		that.dataUx.push(that.onceStr.substr(6, le - 10))
                // 		if (that.isEnd == 0) {
                // 			that.pakgeNum += 1
                // 		}
                // 	}, that.msnumber)
                // } else {
                // 	if (str.indexOf('010328') == 0 && that.pakgeNum<201) {
                // 		setTimeout(() => {
                // 			console.info(that.onceStr)
                // 			let le = parseInt(that.onceStr.length)
                // 			that.dataUx.push(that.onceStr.substr(6, le - 10))
                // 			if (that.isEnd == 0) {
                // 				that.pakgeNum += 1
                // 			}
                // 		},that.pakgeNum==1?200:that.msnumber)


                // 	}
                // }

                // else {
                // 	console.info(str)
                // 	console.info(that.onceStr)
                // 	that.onceStr += str
                // 	let calcStr= that.onceStr
                // 	let str2= calcStr.substr(6, 100)
                // 	if(str2.length==100) {
                // 	  that.dataUx[that.pakgeNum-1].push(str2)
                // 	  that.pakgeNum += 1
                // 	  that.onceStr =''
                // 	}
                // }
              }

            })
          });
        // setTimeout(() => {
        //   // 失能
        //   that.$store.dispatch('writeManufacturer', {
        //     item: that.paired[that.pairedIndex],
        //     manufacturer: manufacturer[1],
        //     writeCode: '01100FD2000102000100F6',
        //     index: 0
        //   }).then(res => {
        //     uni.showToast({
        //       title: res.errMsg,
        //       icon: 'none',
        //       position: 'bottom'
        //     });
        //   });
        // }, 600)

        setTimeout(() => {
          that.isEnd = 0
          //装在数据
          that.$store.dispatch('writeManufacturer', {
            item: that.paired[that.pairedIndex],
            manufacturer: manufacturer[1],
            writeCode: writeCode,
            index: 0
          }).then(res => {
            uni.showToast({
              title: res.errMsg,
              icon: 'none',
              position: 'bottom'
            });
          });
        }, 1000)

      } else {
        uni.showToast({
          title: '蓝牙连接已断开，请重新连接',
          icon: 'none',
          position: 'bottom'
        });
      }
    },
    async getGroupNum () {// 获取组数
      let that = this

      if (that.paired.length > 0 && that.paired[that.pairedIndex].status) {
        //console.info('88888')

        that.groupNum = 0
        that.totalGroupNum = 0

        let manufacturer = that.manufacturer
        let item = that.paired[that.pairedIndex]
        await bluetooth.notifyBLECharacteristicValueChange(item.deviceId, manufacturer[0].serviceId,
          manufacturer[0].characteristicId)
          .then(res => {
            //console.info(manufacturer[0].characteristicId)
            uni.onBLECharacteristicValueChange(function (res) {
              //console.info(3222)

              let str = bluetooth.ab2hex(res.value)

              if (str.indexOf('010308') == 0) { // 组命令获取
                that.totalGroupNum = parseInt(str.substr(8, 2), 16)
                that.groupNum = 1
              }

            })
          });
        setTimeout(() => {
          // 失能
          that.$store.dispatch('writeManufacturer', {
            item: that.paired[that.pairedIndex],
            manufacturer: manufacturer[1],
            writeCode: '01100FD2000102000100F6',
            index: 0
          }).then(res => {
            uni.showToast({
              title: res.errMsg,
              icon: 'none',
              position: 'bottom'
            });
          });
        }, 600)

        setTimeout(() => {
          //获取组数
          that.$store.dispatch('writeManufacturer', {
            item: that.paired[that.pairedIndex],
            manufacturer: manufacturer[1],
            writeCode: '01030FD0000400E7',
            index: 0
          }).then(res => {
            uni.showToast({
              title: res.errMsg,
              icon: 'none',
              position: 'bottom'
            });
          });
        }, 1000)

      } else {
        uni.showToast({
          title: '蓝牙连接已断开，请重新连接',
          icon: 'none',
          position: 'bottom'
        });
      }
    },
    readManufacturerReadData (num16) { // 读取包数 当前包数+1
      let that = this
      let item = that.paired[that.pairedIndex]
      let manufacturer = that.manufacturer
      // bluetooth.notifyBLECharacteristicValueChange(item.deviceId, manufacturer[0].serviceId, manufacturer[0]
      // 		.characteristicId)
      // 	.then(res => {
      // 		uni.onBLECharacteristicValueChange(function(res) {
      // 			//	console.info('000000000')

      // 			let str = bluetooth.ab2hex(res.value)
      // 			//  console.info(str)
      // 			if (str.indexOf('010364') == 0) {
      // 				console.info(str)
      // 				that.dataUx.push(str.sub(5, 50 * 2))
      // 				that.pakgeNum += 1
      // 			}

      // 		})
      // 	});
      setTimeout(() => {
        that.$store.dispatch('writeManufacturer', {
          item,
          manufacturer: manufacturer[1],
          writeCode: num16,
          index: 0
        }).then(res => {
          uni.showToast({
            title: res.errMsg,
            icon: 'none',
            position: 'bottom'
          });
        });
      }, (that.pakgeNum == 1) ? 2000 : (that.sendnumber == '' ? 0 : that.sendnumber))

      //	},300)

      //that.modalName = null
    },


  }
}
</script>

<style>
.bg-cuorange {
  background-color: #ffa54f;
}

.charts-box {
  width: 100%;
  height: 400rpx;
}

.r {
  border-radius: 10rpx;
  width: 96%;
  margin-left: 2%;
  padding-bottom: 10rpx;
}

.cuh {
  min-height: 45px;
}
.cuh2 {
  min-height: 30px;
}
</style>
