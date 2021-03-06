package com.welb.medicalEthics.controller;

import com.welb.medicalEthics.entity.*;
import com.welb.medicalEthics.mapper.MedicalEthicsMsgMapper;
import com.welb.medicalEthics.service.*;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.entity.EvaluationDepartment;
import com.welb.sysBase.service.EvaluationDepartmentService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.Constant;
import com.welb.sysBase.util.PageUtils;
import com.welb.util.LogUtil;
import com.welb.util.StringUtil;
import com.welb.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

@RestController
@RequestMapping("/medicalEthicsMsg")
public class MedicalEthicsMsgController extends BaseController {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private MedicalEthicsMsgService medicalEthicsMsgService;

    @Autowired
    private IUserService userService;

    @Autowired
    private EvaluationClinicalService clinicalService;

    @Autowired
    private EvaluationNonClinicalService nonClinicalService;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired(required = false)
    private MedicalEthicsMsgMapper mapper;

    @Autowired
    private EvaluationDepartmentService departmentService;

    @RequestMapping("/list")
    public Object list() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            List<MedicalEthicsMsg> dataSource = medicalEthicsMsgService.list(new HashMap<>(param));
            List<MedicalEthicsMsg> listData = PageUtils.PaginationSettingForPages(param, dataSource);
            map.put("totalPages", dataSource.size());
            map.put("data", listData);
            map.put("msg", "????????????");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "????????????");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    @RequestMapping("/resubmitScore")
    public Object resubmitScore() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            String personType = param.get("personType");
            String userId = param.get("userId");
            if (EVALUATION_CLINICAL_TEMP_USER.intValue() == Integer.valueOf(personType)) {
                //????????????
                clinicalService.updateStepByUserId(userId);
            } else if (EVALUATION_CLINICAL_NOT_TEMP_USER.intValue() == Integer.valueOf(personType)) {
                //???????????????
                nonClinicalService.updateStepByUserId(userId);
            }
            map.put("msg", "????????????");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "????????????");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * getCheckList ???????????????/???????????????
     *
     * @return {@link Object}
     */
    @RequestMapping("/checkList")
    public Object getCheckList() {
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        // type 0-?????????   1-?????????
        int type = Integer.parseInt(param.get("type"));
        //????????????,?????????????????????
        String scoreLevelStr = param.get("scoreLevel");
        int scoreLevel = 0;
        if (StringUtils.isNotEmpty(scoreLevelStr)) {
            scoreLevel = Integer.parseInt(param.get("scoreLevel"));
        }

        String userCode = request.getHeader("u_id");
        //?????????????????????????????????
        String roleType = param.get("roleType");
        User user = userService.getUserByUserCode(userCode);
        String userId = user.getMoneycard();
        //????????????
        Map<String, String> pageParams = new HashMap<String, String>() {{
            put("pageSize", param.get("pageSize"));
            put("pageNum", param.get("pageNum"));
        }};
        //????????????
        Map<String, Object> selectParams = new HashMap<>(param);
        selectParams.remove("pageSize");
        selectParams.remove("pageNum");
        List<MedicalEthicsUserRole> medicalEthicsUserRoles = medicalEthicsUserService.selectUserRoleByUserId(userId);
        List<MedicalEthicsMsg> totalList = new ArrayList<>();
        List returnList = new ArrayList<>();
        if (medicalEthicsUserRoles.isEmpty()) {
            map.put("code", 1);
            map.put("msg", "????????????????????????");
        } else {
            String roleId = "";
            Set<String> roleSet = new HashSet<>();
            for (MedicalEthicsUserRole m : medicalEthicsUserRoles) {
                roleSet.add(String.valueOf(m.getRoleId()));
            }
            //???????????????????????????????????????????????????????????????????????????????????????
            if (roleSet.contains(Constant.ROLE_ID_SCORE_DIC) && roleSet.contains(Constant.ROLE_ID_DIRECTOR)) {
                roleId = roleType;
            } else if (roleSet.contains(Constant.ROLE_ID_SCORE_DIC)) {
                roleId = Constant.ROLE_ID_SCORE_DIC;
            } else if (roleSet.contains(Constant.ROLE_ID_DIRECTOR)) {
                roleId = Constant.ROLE_ID_DIRECTOR;
            }
            switch (roleId) {
                //?????????
                case Constant.ROLE_ID_ADMIN:
                    totalList = mapper.listByParam(selectParams).stream().sorted(Comparator.comparing(MedicalEthicsMsg::getIsEditable).thenComparing(MedicalEthicsMsg::getStep, Comparator.reverseOrder())).collect(Collectors.toList());
                    returnList = PageUtils.PaginationSettingForPages(pageParams, totalList);
                    map.put("totalPages", totalList.size());
                    map.put("data", returnList);
                    map.put("code", 0);
                    break;
                //????????????
                case Constant.ROLE_ID_SCORE_DIC:
                    Map<String, String> partyParams = new HashMap<String, String>() {{
                        put("directorUserId", userId);
                    }};
                    List<PartyBranchRelations> list = partyBranchRelationsService.list(partyParams).stream().filter(d -> d.getLevel() == 3).collect(Collectors.toList());
                    List<String> branchIdList = new ArrayList<>();
                    list.forEach(l -> {
                        if (!branchIdList.contains(l.getId().toString())) {
                            branchIdList.add(l.getId().toString());
                        }
                    });
                    Map<String, Object> branchParams = new HashMap<String, Object>() {{
                        put("branchIdList", branchIdList);
                    }};
                    branchParams.putAll(selectParams);
                    List<MedicalEthicsMsg> branchDataRes = mapper.listByParam(branchParams);
                    branchDataRes = extendProcessInfo(branchDataRes);
                    if (type == 0) {
                        totalList = checkProcessStatus(branchDataRes, roleId).stream().filter(d -> d.getIsFinished() == 0).collect(Collectors.toList());
                    } else {
                        totalList = checkProcessStatus(branchDataRes, roleId).stream().filter(d -> d.getIsFinished() == 1).collect(Collectors.toList());
                    }

                    //??????????????????
                    if (scoreLevel != 0) {
                        int finalScoreLevel = scoreLevel;
                        totalList = totalList.stream().filter(d -> d.getScoreLevel() == finalScoreLevel).collect(Collectors.toList());
                    }

                    totalList = totalList.stream().sorted(Comparator.comparing(MedicalEthicsMsg::getIsEditable).thenComparing(MedicalEthicsMsg::getStep, Comparator.reverseOrder())).collect(Collectors.toList());
                    returnList = PageUtils.PaginationSettingForPages(pageParams, totalList);
                    map.put("totalPages", totalList.size());
                    map.put("data", returnList);
                    map.put("code", 0);
                    break;
                //????????????
                //????????????id
                case Constant.ROLE_ID_DIRECTOR:
                    Map<String, String> queryParams = new HashMap<String, String>() {{
                        put("directorUserId", userId);
                    }};
                    List<EvaluationDepartment> departmentList = departmentService.list(queryParams);
                    if (departmentList.isEmpty()) {
                        map.put("msg", "????????????????????????");
                        map.put("code", 1);
                    } else {
                        //????????????????????????
                        List<String> departmentIdList = new ArrayList<>();
                        departmentList.forEach(d -> {
                            if (!departmentIdList.contains(d.getId().toString())) {
                                departmentIdList.add(d.getId().toString());
                            }
                        });
                        Map<String, Object> deptParams = new HashMap<String, Object>() {{
                            put("departmentIdList", departmentIdList);
                        }};
                        deptParams.putAll(selectParams);
                        List<MedicalEthicsMsg> headDataResList = mapper.listByParam(deptParams);
                        headDataResList = extendProcessInfo(headDataResList);

                        //0-?????????  1-?????????
                        if (type == 0) {
                            totalList = checkProcessStatus(headDataResList, roleId).stream().filter(h -> h.getIsFinished() == 0).collect(Collectors.toList());
                        } else if (type == 1) {
                            totalList = checkProcessStatus(headDataResList, roleId).stream().filter(h -> h.getIsFinished() == 1).collect(Collectors.toList());
                        }

                        //??????????????????
                        if (scoreLevel != 0) {
                            int finalScoreLevel = scoreLevel;
                            totalList = totalList.stream().filter(d -> d.getScoreLevel() == finalScoreLevel).collect(Collectors.toList());
                        }

                        totalList = totalList.stream().sorted(Comparator.comparing(MedicalEthicsMsg::getIsEditable).thenComparing(MedicalEthicsMsg::getStep, Comparator.reverseOrder())).collect(Collectors.toList());
                        returnList = PageUtils.PaginationSettingForPages(pageParams, totalList);
                        map.put("totalPages", totalList.size());
                        map.put("data", returnList);
                        map.put("code", 0);
                    }
                    break;
                default:
                    map.put("totalPages", totalList.size());
                    map.put("data", returnList);
                    map.put("code", 0);
            }
        }
        return ajaxJson(map);
    }

    /**
     * ??????????????????
     *
     * @param originList
     * @param roleId
     * @return
     */
    private List<MedicalEthicsMsg> checkProcessStatus(List<MedicalEthicsMsg> originList, String roleId) {
        originList.forEach(data -> {
            int personType = data.getPersonType();
            String userId = data.getUserId();
            //????????????????????????
            if (Constant.USER_TYPE_CLI == personType) {
                EvaluationClinical evaluationClinical = clinicalService.selectByUserId(userId);
                if (null != evaluationClinical) {
                    if (evaluationClinical.getLevel() != null) {
                        data.setScoreLevel(evaluationClinical.getLevel());
                    }
                    Integer step = evaluationClinical.getStep();
                    //????????????
                    if (Constant.ROLE_ID_SCORE_DIC.equals(roleId)) {
                        //???????????????4,????????????
                        if (CLI_STEP_FOUR == step) {
                            data.setIsEditable(0);
                        } else {
                            data.setIsEditable(1);
                        }

                        if (Constant.CLI_STEP_FIVE == step) {
                            data.setIsFinished(1);
                        } else {
                            data.setIsFinished(0);
                        }
                        //????????????
                    } else if (Constant.ROLE_ID_DIRECTOR.equals(roleId)) {
                        if (step < CLI_STEP_FOUR) {
                            data.setIsFinished(0);
                        } else {
                            data.setIsFinished(1);
                        }
                        //?????????3????????????????????????
                        if (CLI_STEP_THREE == step) {
                            data.setIsEditable(0);
                        } else {
                            data.setIsEditable(1);
                        }
                    }
                }
                //???????????????
            } else if (Constant.USER_TYPE_NO_CLI == personType) {
                //?????????
                EvaluationNonClinical evaluationNonClinical = nonClinicalService.selectByUserId(userId);
                if (evaluationNonClinical != null) {
                    if (evaluationNonClinical.getLevel() != null) {
                        data.setScoreLevel(evaluationNonClinical.getLevel());
                    }
                    Integer step = evaluationNonClinical.getStep();
                    //??????
                    if (Constant.ROLE_ID_DIRECTOR.equals(roleId)) {
                        if (step >= Constant.NON_CLI_STEP_TWO) {
                            data.setIsFinished(1);
                        } else {
                            data.setIsFinished(0);
                        }
                        if (NON_CLI_STEP_ONE == step) {
                            data.setIsEditable(0);
                        } else {
                            data.setIsEditable(1);
                        }
                        //????????????
                    } else if (Constant.ROLE_ID_SCORE_DIC.equals(roleId)) {
                        if (step <= Constant.NON_CLI_STEP_TWO) {
                            data.setIsFinished(0);
                        } else {
                            data.setIsFinished(1);
                        }
                        if (NON_CLI_STEP_TWO == step) {
                            data.setIsEditable(0);
                        } else {
                            data.setIsEditable(1);
                        }
                    }
                }
            }
        });
        return originList;
    }

    /**
     * doSubmit ??????????????????
     *
     * @return {@link Object}
     */
    @RequestMapping("/doSubmit")
    public Object doSubmit() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            //?????????????????????
            List<Integer> idList = Arrays.stream(param.get("ids").split(COMMA)).map(r -> Integer.valueOf(r)).collect(Collectors.toList());
            List<String> resultList = medicalEthicsMsgService.doSubmit(idList);
            map.put("msg", resultList);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "????????????");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * getMyList ??????????????????
     *
     * @param request request
     * @param year    ????????????
     * @return {@link Object} ???????????????
     */
    @RequestMapping("/getMyList")
    public Object getMyList(HttpServletRequest request, String year) {
        String userCode = request.getHeader("u_id");
        ModelMap map = new ModelMap();
        User user = userService.getUserByUserCode(userCode);
        if (null != user) {
            String userId = user.getMoneycard();
            MedicalEthicsMsg medicalEthicsMsg = medicalEthicsMsgService.selectByUserId(userId);
            if (null == medicalEthicsMsg) {
                //???????????????
                map.put("code", 0);
                map.put("msg", "????????????????????????");
                map.put("personType", 999);
                map.put("data", new ArrayList<>());
            } else {
                Integer personType = medicalEthicsMsg.getPersonType();
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                if (StringUtil.isNotEmpty(year)) {
                    params.put("year", year);
                }
                //????????????????????????
                if (Constant.USER_TYPE_CLI.intValue() == personType.intValue()) {
                    params.put("userId", userId);
                    List<EvaluationClinical> resultList = clinicalService.list(params);
                    if (!resultList.isEmpty()) {
                        resultList = clinicalService.extendPartyInfo(resultList);
                        String departmentName = departmentService.getDepartmentNameByUserId(userId);
                        resultList.forEach(d -> {
                            d.setPersonType(personType);
                            d.setDepartmentName(departmentName);
                        });
                    }
                    map.put("personType", personType);
                    map.put("totalCount", resultList.size());
                    map.put("data", resultList);
                    map.put("code", 0);

                } else if (Constant.USER_TYPE_NO_CLI.intValue() == personType.intValue()) {
                    //???????????????????????????
                    List<EvaluationNonClinical> resultList = nonClinicalService.list(params);
                    if (!resultList.isEmpty()) {
                        resultList = nonClinicalService.extendPartyInfo(resultList);
                        String departmentName = departmentService.getDepartmentNameByUserId(userId);
                        resultList.forEach(d -> {
                            d.setPersonType(personType);
                            d.setDepartmentName(departmentName);
                        });
                    }
                    //????????????
                    map.put("personType", personType);
                    map.put("data", resultList);
                    map.put("totalCount", resultList.size());
                    map.put("code", 0);
                }
            }
        }
        return ajaxJson(map);
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @RequestMapping("/delete")
    public Object delete() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            String userId = param.get("userId");
            if (StringUtils.isEmpty(userId)) {
                map.put("msg", "????????????????????????");
                map.put("code", 1);
            } else {
                medicalEthicsMsgService.delete(userId);
                map.put("msg", "????????????");
                map.put("code", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "????????????");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * extendProcessInfo ??????personType??????step??????
     *
     * @param dataList dataList
     * @return {@link List<MedicalEthicsMsg>}
     */
    private List<MedicalEthicsMsg> extendProcessInfo(List<MedicalEthicsMsg> dataList) {
        if (dataList != null) {
            dataList.forEach(o -> {
                int personType = o.getPersonType();
                if (Constant.USER_TYPE_CLI == personType) {
                    //????????????
                    EvaluationClinical evaluationClinical = clinicalService.selectByUserId(o.getUserId());
                    if (evaluationClinical != null) {
                        o.setStep(evaluationClinical.getStep());
                    }
                    //???????????????
                } else if (Constant.USER_TYPE_NO_CLI == personType) {
                    EvaluationNonClinical evaluationNonClinical = nonClinicalService.selectByUserId(o.getUserId());
                    if (evaluationNonClinical != null) {
                        o.setStep(evaluationNonClinical.getStep());
                    }
                }
            });
        }
        return dataList;
    }
}
