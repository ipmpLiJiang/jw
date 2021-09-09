<template>
  <div>
    <div class="iphone">
      <div class="iphone-top">
        <span class="camera"></span>
        <span class="sensor"></span>
        <span class="speaker"></span>
      </div>
      <div class="top-bar"></div>
      <div class="iphone-screen">


        <div class="question">
          <el-row>
            <el-col class="title">精神卫生中心360°考核系统<br/>2019年一月份门诊办公室考核评估问卷</el-col>
            <el-col class="content">
              <el-col class="list">
                <el-col class="question-title">
                  <el-col :span="2" class="index">1</el-col>
                  <el-col :span="22">有责任感，有主人翁精神，以身作则，在工作中言行一致，能自觉完成任务，主动承担责任</el-col>
                </el-col>
                <el-col class="answer on-line-top">
                  <el-col class="name">
                    <el-col :span="5">彭义香</el-col>
                  </el-col>
                  <el-col class="radio">
                    <el-radio-group v-model="radio" size="small">
                      <el-radio-button label="很不满意"></el-radio-button>
                      <el-radio-button label="不满意"></el-radio-button>
                      <el-radio-button label="一般"></el-radio-button>
                      <el-radio-button label="满意"></el-radio-button>
                      <el-radio-button label="很满意"></el-radio-button>
                    </el-radio-group>
                  </el-col>
                </el-col>
                <el-col class="answer on-line-top">
                  <el-col class="name">
                    <el-col :span="5">汪宏波</el-col>
                  </el-col>
                  <el-col class="radio">
                    <el-radio-group v-model="radio2" size="small">
                      <el-radio-button label="很不满意"></el-radio-button>
                      <el-radio-button label="不满意"></el-radio-button>
                      <el-radio-button label="一般"></el-radio-button>
                      <el-radio-button label="满意"></el-radio-button>
                      <el-radio-button label="很满意"></el-radio-button>
                    </el-radio-group>
                  </el-col>
                </el-col>
              </el-col>
              <el-col class="list">
                <el-col class="question-title">
                  <el-col :span="2" class="index">2</el-col>
                  <el-col :span="22">根据公司的目标和需要调整自己的定位，勇于承担工作职责，在高压力情境下也能保持高效的工作状态</el-col>
                </el-col>
                <el-col class="answer on-line-top">
                  <el-col class="name">
                    <el-col :span="5">彭义香</el-col>
                  </el-col>
                  <el-col class="radio">
                    <el-radio-group v-model="radio3" size="small">
                      <el-radio-button label="很不满意"></el-radio-button>
                      <el-radio-button label="不满意"></el-radio-button>
                      <el-radio-button label="一般"></el-radio-button>
                      <el-radio-button label="满意"></el-radio-button>
                      <el-radio-button label="很满意"></el-radio-button>
                    </el-radio-group>
                  </el-col>
                </el-col>
                <el-col class="answer on-line-top">
                  <el-col class="name">
                    <el-col :span="5">汪宏波</el-col>
                  </el-col>
                  <el-col class="radio">
                    <el-radio-group v-model="radio4" size="small">
                      <el-radio-button label="很不满意"></el-radio-button>
                      <el-radio-button label="不满意"></el-radio-button>
                      <el-radio-button label="一般"></el-radio-button>
                      <el-radio-button label="满意"></el-radio-button>
                      <el-radio-button label="很满意"></el-radio-button>
                    </el-radio-group>
                  </el-col>
                </el-col>
              </el-col>
              <el-col class="message">- 共2道题，您做完了-</el-col>
            </el-col>
          </el-row>
        </div>




      </div>
      <div class="buttons">
        <span class="on-off"></span>
        <span class="sleep"></span>
        <span class="up"></span>
        <span class="down"></span>
      </div>
      <div class="bottom-bar"></div>
      <div class="iphone-bottom">
        <span></span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
        radio: '',
        radio2: '',
        radio3: '',
        radio4: '',
    };
  },
  components: {
  },
  mounted() {
  },
  created() {
  },

  methods: {
  }
};
</script>


<style lang="scss" scoped>

  .iphone {
    box-shadow: inset 0 0 3px 0 rgba(0, 0, 0, 0.2), 0 0 0 1px #999, 0 0 30px 0px rgba(0, 0, 0, 0.7);
    border: 5px solid #d9dbdc;
    background: #f8f8f8;
    padding: 15px;
    border-radius: 50px;
    height: 877px;
    width: 423px;
    position: relative;
    top: -40px;
    left: 60px;
    -webkit-transform: scale(0.7);
    transform: scale(0.7);
  }
  .iphone-top {
    padding: 5px 110px 40px;
    position: relative;
  }
  .iphone-top .speaker {
    display: block;
    width: 70px;
    height: 6px;
    margin: 0 auto;
    border-radius: 6px;
    background: #292728;
  }
  .iphone-top .camera {
    display: block;
    margin: 0 auto;
    height: 10px;
    width: 10px;
    border-radius: 50%;
    margin-bottom: 13px;
    background: #333;
  }
  .iphone-top .sensor {
    display: block;
    width: 15px;
    height: 15px;
    float: left;
    background: #333;
    margin-top: -5px;
    border-radius: 50%;
  }
  .iphone .top-bar, .iphone .bottom-bar {
    display: block;
    width: 423px;
    height: 15px;
    border-left: 5px solid #BBB;
    border-right: 5px solid #BBB;
    position: absolute;
    left: -5px;
  }
  .iphone .top-bar {
    top: 65px;
  }
  .iphone .bottom-bar {
    bottom: 65px;
  }
  .iphone-screen {
    background: #eee;
    border: 1px solid #fff;
    height: 677px;
    width: 375px;
    margin: 0 auto;
    border: 2px solid rgba(0, 0, 0, 0.9);
    border-radius: 3px;
    overflow: hidden;
  }
  .iphone-screen img {
    width: 100%;
  }
  .iphone .buttons .on-off, .iphone .buttons .up, .iphone .buttons .down, .iphone .buttons .sleep {
    display: block;
    background: #CCC;
    position: absolute;
    border-radius: 2px 0px 0px 2px;
  }
  .iphone .buttons .on-off {
    height: 40px;
    width: 3px;
    top: 100px;
    left: -8px;
  }
  .iphone .buttons .up, .iphone .buttons .down, .iphone .buttons .sleep {
    height: 60px;
    width: 5px;
    left: -10px;
  }
  .iphone .buttons .up {
    top: 170px;
  }
  .iphone .buttons .down {
    top: 250px;
  }
  .iphone .buttons .sleep {
    left: auto;
    right: -10px;
    top: 170px;
    border-radius: 0px 2px 2px 0px;
  }
  .iphone-bottom {
    padding: 10px 0 0;
  }
  .iphone-bottom span {
    display: block;
    margin: 0 auto;
    width: 68px;
    height: 68px;
    background: #ccc;
    border-radius: 50%;
    background: -webkit-linear-gradient(315deg, #303233 0%, #b5b7b9 50%, #f0f2f2 69%, #303233 100%);
    background: linear-gradient(135deg, #303233 0%, #b5b7b9 50%, #f0f2f2 69%, #303233 100%);
    position: relative;
  }
  .iphone-bottom span:after {
    content: "";
    display: block;
    width: 60px;
    height: 60px;
    background: #fff;
    border-radius: 50%;
    position: absolute;
    left: 4px;
    top: 4px;
  }
  .question{
    .title{
      background: #fff;
      text-align: center;
      font-weight: bold;
      font-size: 1rem;
      padding: 0.5rem 0px;
      color:#409EFF;
    }
    .content{
      padding: 0 0 52px 0;
      .list{
        background: #fff;
        margin-top: 0.5rem;
        font-size: 0.8rem;
        .question-title{
          font-weight: bold;
          padding: 0.5rem 0px;
          text-align: left;
          .index{
            text-align: center;
          }
        }
        .on-line-top::after {
          content: ' ';
          position: absolute;
          pointer-events: none;
          -webkit-box-sizing: border-box;
          box-sizing: border-box;
          left: 0;
          right: 0;
          top: 0;
          width: 100%;
          -webkit-transform: scaleY(0.5);
          transform: scaleY(0.5);
          border-bottom: 1px solid #ebedf0;
        }
        .answer{
          background: #fafafa;
          position:relative;
          padding: 0.5rem 0 1rem 0;
          .name{
            margin-bottom: 0.5rem;
            .el-col{
              text-align: center;
              font-weight: bold;
              color: #777;
            }
          }
          .radio{
            text-align: center;
          }
        }
      }
      .message{
        margin: 1rem 0;
        color: #ccc;
        text-align: center;
      }
    }
  }
</style>
