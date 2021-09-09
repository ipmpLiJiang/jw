<template>
    <div>
        <el-row class="step">
            <el-steps
                    :active="active"
                    finish-status="success"
                    align-center
            >
                <el-step title="创建评价关系"></el-step>
                <el-step title="编辑考核"></el-step>
                <el-step title="发布考核"></el-step>
                <!-- <el-step title="评估报告"></el-step> -->
            </el-steps>
        </el-row>

        <el-row class="apply">
            发布考核：
            <el-select v-model="value" placeholder="请选择发送方式">
                <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
            <el-button type="primary" icon="el-icon-success" @click="link">获取考核链接给考核人</el-button>
        </el-row>

        <el-row class="edit">
            <el-table
                    :data="tableData"
                    stripe
                    style="width: 100%">
                <el-table-column
                        prop="name"
                        label="被评人"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="a"
                        label="A类评分人">
                </el-table-column>
                <el-table-column
                        prop="b"
                        label="B类评分人">
                </el-table-column>
                <el-table-column
                        prop="c"
                        label="C类评分人">
                </el-table-column>
                <el-table-column
                        prop="d"
                        label="D类评分人">
                </el-table-column>
                <el-table-column
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text"><i class="icon iconfont icon-weibiaoti2010104"></i>设置密码</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-row>


        <el-dialog :visible.sync="dialogTableVisible">
            <el-row>
                <el-col class="code" :span="6" :offset="9">
                    <img src="../../assets/img/code.png"/>
                    <p>链接二维码</p>
                </el-col>
                <el-col class="link">
                    <el-col class="message">考核链接</el-col>
                    <el-input v-model="input" placeholder="请输入内容"></el-input>
                    <el-button type="primary">复制链接</el-button>
                </el-col>
                <el-col class="pwd">
                    <el-col class="message">设置密码提示语</el-col>
                    <el-input v-model="input2"  :maxlength="20" placeholder="请输入内容"></el-input>
                    <el-button type="primary">保存提示</el-button>
                </el-col>
            </el-row>
        </el-dialog>

    </div>
</template>
<script>

    export default {
        data() {
            return {
                active: 3,
                tableData: [{
                    name: '袁莉',
                    a: '杨光辉',
                    b: '刘亚玲',
                    c: '胡豫 张明',
                    d: '彭义香',
                },{
                    name: '汪宏波',
                    a: '邹俊宁',
                    b: '张明',
                    c: '樊玮 刘亚玲',
                    d: '刘亚玲',
                }],
                options: [{
                    value: '1',
                    label: '密码登录'
                }],
                value: '1',
                dialogTableVisible: false,
                input:'http://132.232.145.217:8090/user/#/webShowLogin',
                input2:'密码为工号'
            };
        },
        components: {

        },
        mounted() {},
        created() {},

        methods: {
            link(){
                this.dialogTableVisible = true
            },
        }
    };
</script>
<style lang="scss" scoped>
    .step {
        background: #fff;
        padding: 20px;
        border-radius: 5px;
    }
    .edit {
        background: #fff;
        padding: 20px;
        margin: 10px 0px;
        border-radius: 5px;
        .message {
            margin: 20px 0px 10px 0px;
            color: #cf9236;
        }
    }
    .apply{
        background: #fff;
        padding: 20px;
        border-radius: 5px;
        margin:10px 0px;
        .el-button{
            margin-left: 10px;
        }
    }
    .code{
        background:#fafafa;
        border-radius:5px;
        img{
            width:60%;
            margin:20px 20% 20px 20%;
        }
        p{
            text-align: center;
            height:30px;
            line-height: 30px;
            background: #f4f4f4;
            border-radius:0px 0px 5px 5px;
        }
    }
    .link{
        text-align: center;
        margin-top:20px;
        .message{
            margin-left: 8%;
            text-align: left;
            margin-bottom: 10px;
        }
        .el-input{
            width:70%;
            margin-right: 10px;
        }
    }
    .pwd{
        text-align: center;
        margin-top:20px;
        .message{
            margin-left: 8%;
            text-align: left;
            margin-bottom: 10px;
        }
        .el-input{
            width:70%;
            margin-right: 10px;
        }
    }
</style>
