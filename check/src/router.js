import Vue from 'vue'
import Router from 'vue-router'
import layout from '@/views/layout/layout'
import detailLayout from '@/views/layout/detailLayout'
import layoutM from '@/views/layout/layoutM'
import layoutPersonnel from '@/views/layout/layoutPersonnel'
import layoutQuestionnaires from '@/views/layout/layoutQuestionnaires'
import layoutJurisdiction from '@/views/layout/layoutJurisdiction'
import home from '@/views/home/index.vue'
import routeReplaceSelf from '@/utils/routeReplaceSelf'
import layoutYdyf from '@/views/layout/ydyf/layout'

Vue.use(Router)

//兼容safari 回车路由会自动跳转的bug
const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err);
};

/**
 * login 登录
 */
import login from '@/views/login/index.vue'
import forgetPsd from '@/views/login/forget.vue'
/**
 * 移动端端考核问卷
 */

import tabOne from '@/views/webShow/tabOne.vue'
import me from '@/views/webShow/me.vue'
import webShow from '@/views/webShow/index.vue'
import webShow2 from '@/views/webShow/index2.vue'
import tabTwo from '@/views/webShow/tabTwo.vue'
import webShowZp from '@/views/webShow/indexZp.vue'
import webShowZp2 from '@/views/webShow/indexZp2.vue'
import webShowSuccess from '@/views/webShow/success.vue'
import webShowLogin from '@/views/webShow/login.vue'
import webShowPersonal from '@/views/webShow/personal/personal.vue'
import webShowQuestion from '@/views/webShow/personal/question.vue'
import mobileUpdatePsd from '@/views/webShow/updatePsd.vue'

/**
 * 评估报告
 */
import assessmentReport from "@/views/assessmentReport/index" //容器
import departmentTotal from "@/views/assessmentReport/departmentTotal" // 所有人评估报告
import someone from "@/views/assessmentReport/someone" // 个人评估报告
import twoDimension from "@/views/assessmentReport/twoChart" // 二级维度
import totalReport from "@/views/assessmentReport/totalReport" // 所有人评估报告列表
import scorerList from "@/views/assessmentReport/scorerList" // 评分人列表

/**
 * 测评问卷
 */
import questionnaire from "@/views/questionnaire/index"
import design from "@/views/questionnaire/design"
import apply from "@/views/questionnaire/apply"
import analyze from "@/views/questionnaire/analyze"
import questionList from "@/views/questionnaire/questionList"
import mobileQuestionList from "@/views/questionnaire/mobileQuestionList"
import submitSuccess from "@/views/questionnaire/submitSuccess"
import questionPreview from "@/views/questionnaire/preview"

/**
 * 创建问卷第一步
 */
import create from "@/views/questionnaire/create"

/**
 * 创建考核第二步
 */
import relationship from "@/views/relationship/index" //创建评价关系
import addRelationship from "@/views/relationship/add" //添加评价关系
import test from "@/views/relationship/test" //
import result from "@/views/relationship/result" //评价关系结果

//岗位管理
import department from '@/views/post/department.vue'
import post from '@/views/post/post.vue'
import branch from '@/views/post/branch.vue'
import indicator from '@/views/post/indicator.vue'

//人员管理
import people from '@/views/people/people.vue'

//月结管理
import scorePeople from '@/views/score/scorePeople.vue'//评分关系管理
import gradeUserList from '@/views/score/gradeUserList.vue'//设置各类评分人
import gradeDutyUserList from '@/views/score/gradeDutyUserList.vue'//设置各类评分人
import byGradeUserList from '@/views/score/byGradeUserList.vue' // 设置被评分人
import scoreStationPeople from '@/views/score/scoreStationPeople.vue'//岗位关系管理
import gradeStationList from '@/views/score/gradeStationList.vue'//设置各类评分岗位
import gradeDutyStationList from '@/views/score/gradeDutyStationList.vue'//设置各类评分岗位
import byGradeStationList from '@/views/score/byGradeStationList.vue' // 设置被评分岗位
import gradeTotal from '@/views/score/gradeTotal.vue'//评分汇总管理
import historyGrade from '@/views/score/historyGrade.vue'//历史评分汇总
import scoreBadGrade from '@/views/score/scoreBadGrade.vue'//差评评分汇总
import scoreDutySmTjGrade from '@/views/score/scoreDutySmTjGrade.vue'//自评情况统计表
import scoreFlowTjGrade from '@/views/score/scoreFlowTjGrade.vue'//测评打分情况统计表成功
import scoreSortGrade from '@/views/score/scoreSortGrade.vue'//评分排序汇总
import quarter from '@/views/score/quarter.vue'//月度总结管理
import leaderUser from '@/views/score/leader.vue'//打分用户管理

//用户模块
import updatePsd from '@/views/user/updatePsd.vue' //修改密码
import userQuarter from '@/views/user/quarter.vue' //个人月度总结
import userGrade from '@/views/user/grade.vue' //个人评分
import userSelf from '@/views/user/userSelfEvaluate.vue' //人员自评
//考核
import assess from '@/views/assess/assess.vue' //考核

//考核
import assess2 from '@/views/assess/assess2.vue' //考核

//考核自评
import assessZp from '@/views/assess/assessZp.vue' //考核

//考核自评
import assessZp2 from '@/views/assess/assessZp2.vue' //考核
//登录提示
import notice from '@/views/notice/notice.vue' //考核

//人事部考核
import gradeLeadingIn from '@/views/personnel/gradeLeadingIn.vue'//评分导入
import gradeLeadingOut from '@/views/personnel/gradeLeadingOut.vue'//评分导出
import deptCheck from '@/views/personnel/deptCheck.vue'//部门考核办法
import deptCheckList from '@/views/personnel/deptCheckList.vue'//部门考核办法列表
import personnelPeopleInfo from '@/views/personnel/peopleManagement/info.vue'//人员管理-人员列表
import personnelPeopleScore from '@/views/personnel/peopleManagement/score.vue'//人员管理-分数列表
import personnelLeader from '@/views/personnel/leaderManagement/leader.vue'//用户管理
import complishSituation from '@/views/personnel/complishSituation.vue'//部门考核完成情况
import authorization from '@/views/personnel/authorization.vue'//人事授权
import principalSignatureList from '@/views/personnel/principalSignatureList.vue'//负责人签字pdf列表
import warnList from '@/views/personnel/warnList.vue'//考核办法完成情况未完成用户列表

//短信模板
import message from '@/views/message/message.vue'//组织部短信模板
import personnelMessage from '@/views/personnel/message/message.vue'//人事部短信模板

//选择模块
import index from '@/views/index/index.vue'

//权限配置
import jurisdiction from '@/views/jurisdiction/index.vue'
import peopleConfig from '@/views/jurisdiction/detail.vue'

/**
 * 医德医风
 */
import clinical from '@/views/ydyf/examine/From/clinical'

// import personal from '@/views/ydyf/examine/personal.vue'
import personal from '@/views/ydyf/examine/personal/personal.vue'
import submitted from '@/views/ydyf/examine/personal/submitted.vue'  //已提交人员信息管理
import notSubmitted from '@/views/ydyf/examine/personal/notSubmitted' //未提交人员管理、
import jurisdictionList from '@/views/ydyf/examine/personal/jurisdiction' //权限管理
import secretary from '@/views/ydyf/examine/secretary/secretary'   //书记信息管理
import director from '@/views/ydyf/examine/secretary/director'   //科室主任信息管理
import evaluationList from '@/views/ydyf/examine/evaluationList/evaluationList'  //我的考评列表
import yesAssessment from '@/views/ydyf/examine/Assessment/yesAssessment'  //已考评人员
import notAssessment from '@/views/ydyf/examine/Assessment/notAssessment'  //待考评人员
import analyse from '@/views/ydyf/examine/analyse/analyse'    //考评成绩分析列表
import analyseList from '@/views/ydyf/examine/analyse/analyseList'
import tabulateList from '@/views/ydyf/examine/analyse/tabulateList'  //考评成绩列表
// import informationList from '@/views/ydyf/examine/analyse/informationList'  //考核人员信息

import party from '@/views/ydyf/examine/party/party'
import personalList from '@/views/ydyf/examine/personalInfo/Personal'
// import clinic from '@/views/ydyf/examine/clinic.vue'
import nonclinical from '@/views/ydyf/examine/From/nonclinical.vue'  //非临床人员提交表单
import clinicalForm from "@/views/ydyf/examine/From/clinicalForm"     //临床人员提交表单
import resultclinicalForm from "@/views/ydyf/examine/From/resultclinicalForm"     //临床人员成绩单
import resultNonclinical from "@/views/ydyf/examine/From/resultNonclinical"     //非临床人员成绩单
import deparTment from "@/views/ydyf/examine/department/department"  //科室主任管理
import departmentPersonnel from "@/views/ydyf/examine/department/departmentPersonnel"  //科室人员管理
import ydyfpcDirectorNoclinic from "@/views/ydyf/examine/mark/directorNoclinic"  //主任非临床打分
import ydyfpcSecretaryNoclinic from "@/views/ydyf/examine/mark/secretaryNoclinic"  //书记非临床打分
import ydyfpcDirectorClinic from "@/views/ydyf/examine/mark/directorClinic"  //主任临床打分
import ydyfpcSecretaryClinic from "@/views/ydyf/examine/mark/secretaryClinic"  //书记临床打分
 
/**
 * 医德医风移动端
 */
import ydyfMobileList from "@/views/ydyf/mobile/list/index"  //考核列表
import ydyfMultiple from "@/views/ydyf/mobile/list/multiple"  //多重角色选择页
import ydyfDirectorNoclinic from "@/views/ydyf/mobile/mark/directorNoclinic"  //主任非临床打分
import ydyfSecretaryNoclinic from "@/views/ydyf/mobile/mark/secretaryNoclinic"  //书记非临床打分
import ydyfDirectorClinic from "@/views/ydyf/mobile/mark/directorClinic"  //主任临床打分
import ydyfSecretaryClinic from "@/views/ydyf/mobile/mark/secretaryClinic"  //书记临床打分
import ydyfUser from "@/views/ydyf/mobile/user/index"  //我的医德医风
import myClinic from "@/views/ydyf/mobile/mark/myClinic"  //我的医德医风临床
import myNoclinic from "@/views/ydyf/mobile/mark/myNoclinic"  //我的医德医风非临床

export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [{
        path: '/',
        name: "登录",
        component: login
    }, {
        path: '/login',
        name: "登录",
        component: login
    },
    {
        path: '/forgetPsd',
        name: "忘记密码",
        component: forgetPsd
    },
    {
        path: '/index',
        name: "选择功能",
        component: index
    },
    {
        path: '/web',
        name: "移动端首页",
        component: layoutM,
        children: [{
            path: '',
            name: "我的考核",
            component: tabOne,
            meta: { title: '我的考核' },
        }, {
            path: 'webShowPersonal',
            name: "个人评估报告",
            component: webShowPersonal,
            meta: { title: '个人评估报告' },
        }, {
            path: 'tabTwo',
            name: "自评",
            component: tabTwo,
            meta: { title: '自评' },
        },{
            path: 'me',
            name: "我",
            component: me,
            meta: { title: '我' },
        }, {
            path: "mobileUpdatePsd",
            name: "修改密码",
            component: mobileUpdatePsd,
            meta: { title: "修改密码" }
        },{
            path: 'ydyfMobileList',
            name: "医德医风考核",
            component: ydyfMobileList,
            meta: { title: '医德医风考核' },
        },{
            path: 'ydyfMultiple',
            name: "医德医风多重角色",
            component: ydyfMultiple,
            meta: { title: '医德医风多重角色' },
        },{
            path: 'ydyfDirectorNoclinic',
            name: "主任非临床打分",
            component: ydyfDirectorNoclinic,
            meta: { title: '主任非临床打分' },
        },{
            path: 'ydyfSecretaryNoclinic',
            name: "书记非临床打分",
            component: ydyfSecretaryNoclinic,
            meta: { title: '书记非临床打分' },
        },{
            path: 'ydyfDirectorClinic',
            name: "主任临床打分",
            component: ydyfDirectorClinic,
            meta: { title: '主任临床打分' },
        },{
            path: 'ydyfSecretaryClinic',
            name: "书记临床打分",
            component: ydyfSecretaryClinic,
            meta: { title: '书记临床打分' },
        },{
            path: 'ydyfUser',
            name: "我的医德医风",
            component: ydyfUser,
            meta: { title: '我的医德医风' },
        },{
            path: 'myClinic',
            name: "我的医德医风临床",
            component: myClinic,
            meta: { title: '我的医德医风临床' },
        },{
            path: 'myNoclinic',
            name: "我的医德医风非临床",
            component: myNoclinic,
            meta: { title: '我的医德医风非临床' },
        }]
    },
    {
        path: '/webShow',
        name: "医院考核",
        component: webShow,
    },
    {
        path: '/webShow2',
        name: "医院考核2",
        component: webShow2,
    },
    {
        path: '/webShowZp',
        name: "医院考核自评",
        component: webShowZp,
    },
    {
        path: '/webShowZp2',
        name: "医院考核自评2",
        component: webShowZp2,
    },
    {
        path: '/webShowSuccess',
        name: "问卷提交",
        component: webShowSuccess,
    },
    {
        path: '/webShowLogin',
        name: "医院考核问卷登录",
        component: webShowLogin,
    },
    {
        path: '/webShowQuestion',
        name: "考核个人评估报告",
        component: webShowQuestion,
    },
    {
        path: '/home',
        name: "首页",
        component: layout,
        children: [{
            path: '',
            name: "首页",
            component: home,
            meta: { title: '首页' },
        }, {
            path: 'department',
            name: "部门管理",
            component: department
        }, {
            path: 'post',
            name: "岗位管理",
            component: post
        },
         {
            path: 'branch',
            name: "支部管理",
            component: branch
        },
        {
            path: 'indicator',
            name: "指标管理",
            component: indicator
        },
        {
            path: 'people',
            name: "人员信息录入",
            component: people
        }, {
            path: 'scorePeople',
            name: "评分关系管理",
            component: routeReplaceSelf(scorePeople),
            children: [{
                path: 'gradeUserList',
                name: "评分关系管理",
                component: gradeUserList,
            }, {
                path: 'byGradeUserList',
                name: "评分关系管理",
                component: byGradeUserList,
            }, {
                path: 'gradeDutyUserList',
                name: "评分关系管理",
                component: gradeDutyUserList,
            }]
        }, {
            path: 'scoreStationPeople',
            name: "岗位关系管理",
            component: routeReplaceSelf(scoreStationPeople),
            children: [{
                path: 'gradeStationList',
                name: "岗位关系管理",
                component: gradeStationList,
            }, {
                path: 'byGradeStationList',
                name: "岗位关系管理",
                component: byGradeStationList,
            }, {
                path: 'gradeDutyStationList',
                name: "岗位关系管理",
                component: gradeDutyStationList,
            }]
        }, {
            path: 'gradeTotal',
            name: "评分汇总管理",
            component: gradeTotal
        }, {
            path: 'historyGrade',
            name: "历史评分汇总",
            component: historyGrade
        }, {
            path: 'scoreBadGrade',
            name: "差评评分汇总",
            component: scoreBadGrade
        }, {
            path: 'scoreDutySmTjGrade',
            name: "自评情况统计表",
            component: scoreDutySmTjGrade
        }, {
            path: 'scoreFlowTjGrade',
            name: "测评打分情况统计表",
            component: scoreFlowTjGrade
        }, {
            path: 'scoreSortGrade',
            name: "评分排序汇总",
            component: scoreSortGrade
        }
        // , {
        //     path: 'quarter',
        //     name: "月度评分管理",
        //     component: quarter
        // }
        , {
            path: 'leaderUser',
            name: "打分用户管理",
            component: leaderUser
        }, {
            path: 'updatePsd',
            name: "修改密码",
            component: updatePsd,
        }, {
            path: 'userQuarter',
            name: "个人季结管理",
            component: userQuarter,
        }, {
            path: 'userGrade',
            name: "个人评分",
            component: userGrade,
        }, {
            path: 'userSelf',
            name: "个人自评",
            component: userSelf,
        }, {
            path: 'assess',
            name: "考核打分",
            component: assess,
        },  {
            path: 'assess2',
            name: "考核打分",
            component: assess2,
        },{
            path: 'assessZp',
            name: "考核自评",
            component: assessZp,
        },  {
            path: 'assessZp2',
            name: "考核自评",
            component: assessZp2,
        },{
            path: 'notice',
            name: "登录提示",
            component: notice,
        }, , {
            path: 'message',
            name: "短信模板",
            component: message,
        }, {
            path: 'someone',
            name: "someone",
            component: someone,
            meta: { title: '个人评估报告' },
        }, {
            path: 'twoDimension',
            name: "twoDimension",
            component: twoDimension,
            meta: { title: '二级维度' },
        }, {
            path: 'totalReport',
            name: "totalReport",
            component: totalReport,
            meta: { title: '二级维度' },
        }, {
            path: 'departmentTotal',
            name: "departmentTotal",
            component: departmentTotal,
            meta: { title: '所有人员评估报告' },
        }, {
            path: 'scorerList',
            name: "scorerList",
            component: scorerList,
            meta: { title: '评分人列表' },
        }]
    },
    {
        path: '/detail',
        name: "detail",
        component: detailLayout,
        children: [{
            path: 'assessmentReport',
            name: "评估报告",
            component: assessmentReport,
            children: [{
                path: 'someone',
                name: "someone",
                component: someone,
                meta: { title: '个人评估报告' },
            }]
        },
        {
            path: 'relationship',
            name: "创建评价关系",
            component: relationship,
            children: [{
                path: '/',
                name: "addRelationship",
                component: addRelationship
            }, {
                path: 'test',
                name: "test",
                component: test,
                meta: { title: 'test' },
            }, {
                path: 'result',
                name: "评价关系结果",
                component: result,
                meta: { title: '评价关系结果' },
            }]
        },
        ]
    },
    {
        path: '/admin',
        name: "admin",
        component: layoutJurisdiction,
        children: [{
            path: 'jurisdiction',
            name: "权限配置",
            component: jurisdiction,
            meta: { title: '权限配置' }
        },
        {
            path: 'peopleConfig',
            name: "角色权限配置",
            component: peopleConfig,
            meta: { title: '角色权限配置' }
        }]
    },
    {
        path: '/personnel',
        name: "personnel",
        component: layoutPersonnel,
        children: [{
            path: '',
            name: "评分导入",
            component: gradeLeadingIn
        },
        {
            path: 'gradeLeadingIn',
            name: "评分导入",
            component: gradeLeadingIn
        },
        {
            path: 'gradeLeadingOut',
            name: "评分导出",
            component: gradeLeadingOut,
        },
        {
            path: 'deptCheck',
            name: "部门考核办法",
            component: deptCheck,
        },
        {
            path: 'deptCheckList',
            name: "部门考核办法列表",
            component: deptCheckList,
        },
        {
            path: 'personnelPeopleInfo',
            name: "人员信息",
            component: personnelPeopleInfo,
        },
        {
            path: 'personnelPeopleScore',
            name: "人员分数",
            component: personnelPeopleScore,
        },
        {
            path: 'personnelLeader',
            name: "用户管理",
            component: personnelLeader,
        },
        {
            path: 'complishSituation',
            name: "部门考核完成情况",
            component: complishSituation,
        },
        {
            path: 'authorization',
            name: "部长授权",
            component: authorization,
        },
        {
            path: 'notice',
            name: "登录提示",
            component: notice,
        },
        {
            path: 'updatePsd',
            name: "修改密码",
            component: updatePsd,
        },
        {
            path: 'principalSignatureList',
            name: "负责人签字pdf列表",
            component: principalSignatureList,
        },
        {
            path: 'warnList',
            name: "人事处考核未完成用户列表",
            component: warnList,
        },
        {
            path: 'personnelMessage',
            name: "人事处短信模板",
            component: personnelMessage,
        },
        ]
    },
    {
        path: '/questionnaire',
        name: "问卷调查",
        component: layoutQuestionnaires,
        children: [{
            path: '',
            name: "我的问卷",
            component: questionnaire,
            meta: { title: '我的问卷' },
        }, {
            path: 'create',
            name: "创建问卷名称",
            component: create,
            meta: { title: '创建问卷名称' },
        }, {
            path: 'apply',
            name: "发布考核",
            component: apply,
            meta: { title: '发布考核' },
        }, {
            path: 'design',
            name: "编辑问卷题目",
            component: design,
            meta: { title: '编辑问卷题目' },
        }, {
            path: 'analyze',
            name: "发放考核",
            component: analyze,
            meta: { title: '发放考核' },
        }]
    },
    {
        path: '/questionList',
        name: "问卷调查",
        component: questionList,
        meta: { title: '问卷调查' },
    },
    {
        path: '/mobileQuestionList',
        name: "问卷调查",
        component: mobileQuestionList,
        meta: { title: '问卷调查' },
    },
    {
        path: '/questionPreview',
        name: "问卷预览",
        component: questionPreview,
        meta: { title: '问卷预览' },
    },
    {
        path: '/submitSuccess',
        name: "问卷调查提交成功",
        component: submitSuccess,
        meta: { title: '问卷调查提交成功' },
    },
    {
        path: '/ydyf',
        name: "医德医风",
        component: layoutYdyf,
        meta: { title: '医德医风考核' },
        children: [{
            path: '',
            name: "我的考评列表",
            component: evaluationList,
            meta: { title: '我的考评列表' },
        },{
            path: 'clinical',
            name: "临床人员考评表",
            component: clinical,
            meta: { title: '临床人员考评表' },
        },
        {
            path: 'nonclinical',
            name: "非临床人员考评表",
            component: nonclinical,
            meta: { title: '非临床人员考评表' },
        },
        {
            path: 'clinicalForm',
            name: "临床人员评分表",
            component: clinicalForm,
            meta: { title: '临床人员评分表' },
        },
        {
            path: 'personal',
            name: "人员管理",
            component: personal,
            meta: { title: '人员管理' },
        },
        {
            path: 'submitted',
            name: "已提交人员信息管理",
            component: submitted,
            meta: { title: '已提交人员信息管理' },
        },
        {
            path: 'jurisdictionList',
            name: "人员权限管理",
            component: jurisdictionList,
            meta: { title: '人员权限管理' },
        },
        {
            path: 'notSubmitted',
            name: "未提交人员信息管理",
            component: notSubmitted,
            meta: { title: '未提交人员信息管理' },
        },
        {
            path: 'secretary',
            name: "书记信息管理",
            component: secretary,
            meta: { title: '书记信息管理' },
        },
        {
            path: 'director',
            name: "主任信息管理",
            component: director,
            meta: { title: '主任信息管理' },
        },

        {
            path: 'notAssessment',
            name: "待考评信息",
            component: notAssessment,
            meta: { title: '待考评信息' },
        },
        {
            path: 'yesAssessment',
            name: "已考评信息",
            component: yesAssessment,
            meta: { title: '已考评信息' },
        },
        {
            path: 'evaluationList',
            name: "我的考评列表",
            component: evaluationList,
            meta: { title: '我的考评列表' },
        },
        {
            path: 'analyse',
            name: "考核统计分析",
            component: analyse,
            meta: { title: '考核统计分析' },
        },
        {
            path: 'analyseList',
            name: "考核统计分析",
            component: analyseList,
            meta: { title: '考核统计分析' },
        },
        {
            path: 'tabulateList',
            name: "考核数据列表",
            component: tabulateList,
            meta: { title: '考核数据列表' },
        },
        {
            path: 'party',
            name: "党支部信息",
            component: party,
            meta: { title: '党支部信息' },
        },
        {
            path: 'personalList',
            name: "党支部信息",
            component: personalList,
            meta: { title: '个人信息' },
        },
        {
            path: 'deparTment',
            name: "科室主任信息",
            component: deparTment,
            meta: { title: '科室主任信息' },
        },
        {
            path: 'departmentPersonnel',
            name: "科室人员信息管理",
            component: departmentPersonnel,
            meta: { title: '科室人员信息管理' },
        },
        {
            path: 'resultclinicalForm',
            name: "临床人员成绩单",
            component: resultclinicalForm,
            meta: { title: '临床人员医德考评' },
        },
        {
            path: 'resultNonclinical',
            name: "非临床人员成绩单",
            component: resultNonclinical,
            meta: { title: '非临床人员医德考评' },
        },  
        {
            path: 'ydyfpcDirectorNoclinic',
            name: "科室主任非临床打分",
            component: ydyfpcDirectorNoclinic,
            meta: { title: '科室主任非临床打分' },
        },        
        {
            path: 'ydyfpcSecretaryNoclinic',
            name: "打分书记非临床打分",
            component: ydyfpcSecretaryNoclinic,
            meta: { title: '打分书记非临床打分' },
        },        
        {
            path: 'ydyfpcDirectorClinic',
            name: "科室主任临床打分",
            component: ydyfpcDirectorClinic,
            meta: { title: '科室主任临床打分' },
        },        
        {
            path: 'ydyfpcSecretaryClinic',
            name: "打分书记临床打分",
            component: ydyfpcSecretaryClinic,
            meta: { title: '打分书记临床打分' },
        },
        // {
        //     path: 'informationList',
        //     name: "考核人员信息",
        //     component: informationList,
        //     meta: { title: '考核人员信息' },
        // },        
    ]
    },
    ]
})
