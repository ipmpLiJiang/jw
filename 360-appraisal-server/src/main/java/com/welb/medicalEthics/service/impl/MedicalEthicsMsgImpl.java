package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.entity.*;
import com.welb.medicalEthics.mapper.MedicalEthicsMsgMapper;
import com.welb.medicalEthics.mapper.MedicalEthicsMsgTempMapper;
import com.welb.medicalEthics.service.*;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.service.IUserRoleService;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.entity.BranchDepartment;
import com.welb.sysBase.entity.EvaluationDepartmentUser;
import com.welb.sysBase.service.BranchDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import com.welb.sysBase.service.IPermissionService;
import com.welb.sysBase.util.Constant;
import com.welb.util.MD5;
import com.welb.util.PageData;
import com.welb.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

@Service
public class MedicalEthicsMsgImpl implements MedicalEthicsMsgService {

    @Autowired(required = false)
    private MedicalEthicsMsgMapper mapper;

    @Autowired(required = false)
    private MedicalEthicsMsgTempMapper medicalEthicsMsgTempMapper;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private EvaluationClinicalService clinicalService;

    @Autowired
    private EvaluationClinicalScoreService scoreService;

    @Autowired
    private EvaluationNonClinicalService nonClinicalService;

    @Autowired
    private EvaluationDepartmentUserService departmentUserService;

    @Autowired
    private BranchDepartmentService branchDepartmentService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;


    @Override
    public List<MedicalEthicsMsg> list(Map<String, String> params) {
        //?????????????????????????????????
        String uId = params.get("u_id");
        //??????sql????????????
        params.remove("pageSize");
        params.remove("pageNum");
        Map<String, String> resultMap = medicalEthicsUserService.loadPermissionCondition(uId);
        params.putAll(resultMap);
        //????????????
        List<MedicalEthicsMsg> dataSource = mapper.list(params);
        //????????????????????????
//        dataSource.stream().forEach(m -> m.setPersonType(this.loadPersonType(m)));
        //?????????????????????
        if (!StringUtil.isEmpty(params.get("personType"))) {
            dataSource = dataSource.stream().filter(d -> d.getPersonType() == Integer.valueOf(params.get("personType"))).collect(Collectors.toList());
        }
        //?????????????????????????????????
        return dataSource;
    }

    @Override
    public List<MedicalEthicsMsg> getList(Map<String, String> params) {
        return mapper.list(params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String userId) {
        MedicalEthicsMsg medicalEthicsMsg = selectByUserId(userId);
        int personType = medicalEthicsMsg.getPersonType();
        //??????????????????????????????
        if (USER_TYPE_CLI == personType) {
            clinicalService.deleteByUserId(userId);
            scoreService.deleteByUserId(userId);
        } else if (USER_TYPE_NO_CLI == personType) {
            //????????????????????????????????????
            nonClinicalService.deleteByUserId(userId);
        }
        //??????msg???
        mapper.deleteByUserId(userId);
        //????????????????????????????????????????????????user-role
        Map<String, String> deleteParams = new HashMap<>();
        deleteParams.put("roleId", "3");
        deleteParams.put("userId", userId);
        medicalEthicsUserService.deleteUserRoleByParams(deleteParams);
        //????????????????????????
        medicalEthicsMsgTempMapper.deleteByUserId(userId);
        Map<String, String> delParam = new HashMap<>();
        delParam.put("userId", userId);
        departmentUserService.deleteByParams(delParam);
    }

    @Override
    public void deleteByUserId(String userId) {
        mapper.deleteByUserId(userId);
    }

    @Transactional
    @Override
    public void update(MedicalEthicsMsg medicalEthicsMsg) {
        mapper.update(medicalEthicsMsg);
    }

    @Transactional
    @Override
    public List<String> insert(MedicalEthicsMsg medicalEthicsMsg) {
        List<String> resultList = new ArrayList<>();
        //????????????
        List<MedicalEthicsMsg> repeatData = mapper.list(new HashMap<>()).stream().filter(m -> m.getUserId().equals(medicalEthicsMsg.getUserId())).collect(Collectors.toList());
        if (repeatData != null && !repeatData.isEmpty()) {
            //???????????????
            resultList.add("???????????????,????????????");
        } else {
            mapper.insert(medicalEthicsMsg);
            resultList.add("??????????????????");
        }

        return resultList;
    }

    @Transactional
    @Override
    public List<String> batchInsert(List<MedicalEthicsMsg> dataSource) {
        List<String> resultList = new ArrayList<>();
        return resultList;
    }

    @Transactional
    @Override
    public List<String> doSubmit(List<Integer> idList) {
        List<String> resultList = new ArrayList<>();
        //??????????????????????????????
        List<MedicalEthicsMsgTemp> tempList = medicalEthicsMsgTempMapper.listByIds(idList);
        List<MedicalEthicsMsg> medicalEthicsMsgList = new ArrayList<>();
        List<MedicalEthicsUser> medicalEthicsUserList = new ArrayList<>();
        //????????????
        List<String> userIdList = tempList.stream().map(u -> u.getUserId()).collect(Collectors.toList());
        List<PageData> submitUser = permissionService.showUserByUserId(userIdList);
        //???????????????????????????
        List<PageData> submitLoginUser = permissionService.showLoginUserByUserId(userIdList);
        List<UserRoleKey> insertUserRoleList = new ArrayList<>();
        tempList.stream().forEach(o -> {
            //?????????
            MedicalEthicsMsg medicalEthicsMsg = new MedicalEthicsMsg(o.getUserId(), o.getPartyCommitteesId(), o.getGeneralBranchId(), o.getBranchId(), o.getUserName(), STATUS_ZERO, o.getPersonType());
            medicalEthicsMsgList.add(medicalEthicsMsg);
            //????????????
            PageData curUser = submitUser.stream().filter(s -> o.getUserId().equals(s.getString("u_id"))).collect(Collectors.toList()).get(0);
            //??????
            int sex = curUser.getString("u_sex").equals(SEX_MALE) ? STATUS_ONE : STATUS_ZERO;
            //?????????
            String idCard = curUser.getString("u_id_card");
            //????????????
            String hireData = curUser.getString("u_hospital_time");
            //???????????????
            MedicalEthicsUser medicalEthicsUser = new MedicalEthicsUser(o.getUserId(),
                    o.getUserName(),
                    sex,
                    o.getPartyCommitteesId(),
                    o.getGeneralBranchId(),
                    o.getBranchId(),
                    idCard,
                    null,
                    null,
                    null,
                    hireData,
                    null,
                    STATUS_ZERO,
                    null, o.getPersonType());
            medicalEthicsUserList.add(medicalEthicsUser);
            //????????????
            this.handleLoginUser(curUser, submitLoginUser, insertUserRoleList);
        });

        if (!idList.isEmpty()) {
            //???????????????????????????(??????status????????????)
            tempList.stream().forEach(o -> o.setStatus(STATUS_ONE));
            medicalEthicsMsgTempMapper.batchUpdate(tempList);
        }

        //????????????????????????????????????
        if (!insertUserRoleList.isEmpty()) {
            userRoleService.batchInsert(insertUserRoleList);
        }

        //???????????????????????????????????????
        this.handleRepeatData(medicalEthicsMsgList, medicalEthicsUserList, resultList);
        return resultList;
    }

    @Override
    public MedicalEthicsMsg selectByUserId(String userId) {
        return mapper.selectByUserId(userId);
    }

    @Override
    public MedicalEthicsMsg selectById(Integer id) {
        return mapper.selectByUId(id);
    }

    @Override
    public List<MedicalEthicsMsg> selectByBranchId(Integer branchId) {
        Map<String, String> params = new HashMap<>();
        params.put("branchId", String.valueOf(branchId));
        return list(params);
    }

    @Override
    public List<MedicalEthicsMsg> selectAll(Map<String, String> params) {
        return mapper.selectAll(params);
    }

    @Override
    public List<String> allIdList() {
        return mapper.allIdList();
    }

    @Override
    public List<MedicalEthicsMsg> showMissingUsers() {
        return mapper.showMissingUsers();
    }

    /**
     * selectCommonUser ???????????????????????????
     *
     * @return {@link List<MedicalEthicsMsg>}
     */
    @Override
    public List<MedicalEthicsMsg> selectCommonUser(String branchId) {
        return mapper.selectCommonUser(branchId);
    }

    /**
     * updateMsg ?????????????????????
     *
     * @param userId       userId
     * @param departmentId departmentId
     * @param personType   personType
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMsg(String userId, String departmentId, String personType) {
        MedicalEthicsMsgTemp medicalEthicsMsgTemp = medicalEthicsMsgTempMapper.selectByUserId(userId);
        //??????????????????
        medicalEthicsMsgTemp.setPersonType(Integer.parseInt(personType));
        //??????????????????
        EvaluationDepartmentUser departmentUser = new EvaluationDepartmentUser();
        departmentUser.setUserId(Integer.parseInt(userId));
        departmentUser.setDepartmentId(Integer.parseInt(departmentId));
        departmentUserService.bindNewUser(departmentUser);
        //??????????????????????????????
        BranchDepartment branchDepartment = branchDepartmentService.selectByDepartmentId(Integer.parseInt(departmentId));
        Integer branchId = branchDepartment.getBranchId();
        int generalBranchId = 0;
        int committeedId = 0;
        PartyBranchRelations branchParty = partyBranchRelationsService.selectById(branchId);
        if (branchParty != null) {
            PartyBranchRelations generalParty = partyBranchRelationsService.selectById(branchParty.getParentId());
            if (generalParty != null) {
                generalBranchId = generalParty.getId();
                PartyBranchRelations committeedParty = partyBranchRelationsService.selectById(generalParty.getParentId());
                if (committeedParty != null) {
                    committeedId = committeedParty.getId();
                    //??????temp???????????????
                    medicalEthicsMsgTemp.setBranchId(branchId);
                    medicalEthicsMsgTemp.setGeneralBranchId(generalBranchId);
                    medicalEthicsMsgTemp.setPartyCommitteesId(committeedId);
                }
            }
        }

        //??????message
        MedicalEthicsMsg medicalEthicsMsg = mapper.selectByUserId(userId);
        if (medicalEthicsMsg != null) {
            medicalEthicsMsg.setPersonType(Integer.parseInt(personType));
            medicalEthicsMsg.setBranchId(branchId);
            medicalEthicsMsg.setGeneralBranchId(generalBranchId);
            medicalEthicsMsg.setPartyCommitteesId(committeedId);
            mapper.update(medicalEthicsMsg);
        }

        medicalEthicsMsgTempMapper.update(medicalEthicsMsgTemp);
        //???????????????????????????
        if (Integer.parseInt(personType) == USER_TYPE_CLI) {
            EvaluationClinical clinical = clinicalService.selectByUserId(userId);
            nonClinicalService.deleteByUserId(userId);
            if (clinical != null) {
                clinical.setBranchId(branchId);
                clinical.setGeneralBranchId(generalBranchId);
                clinical.setPartyCommitteesId(committeedId);
                clinicalService.updateById(clinical);
            }
        } else if (Integer.parseInt(personType) == USER_TYPE_NO_CLI) {
            EvaluationNonClinical nonClinical = nonClinicalService.selectByUserId(userId);
            //????????????????????????
            clinicalService.deleteByUserId(userId);
            scoreService.deleteByUserId(userId);
            if (nonClinical != null) {
                nonClinical.setBranchId(branchId);
                nonClinical.setGeneralBranchId(generalBranchId);
                nonClinical.setPartyCommitteesId(committeedId);
                nonClinicalService.updateById(nonClinical);
            }
        }
    }

    private void handleLoginUser(PageData curUser, List<PageData> submitLoginUser, List<UserRoleKey> insertUserRoleList) {
        //???????????????????????????????????????
        UserRoleKey userRoleKey = new UserRoleKey();
        userRoleKey.setRolecode(MEDICAL_ETHICS_USER_ROLE);
        String userCode;
        if (!submitLoginUser.isEmpty()) {
            //???????????????????????????,????????????????????????????????????????????????????????????
            PageData curLoginUser = submitLoginUser.stream().filter(s -> s.getString("MoneyCard").equals(curUser.getString("u_id"))).collect(Collectors.toList()).get(0);
            userCode = curLoginUser.getString("UserCode");
            //????????????????????????
            userRoleKey.setUsercode(userCode);
        } else {
            //?????????????????????,???????????????????????????????????????????????????
            User user = new User();
            //????????????????????????
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            String email = curUser.getString("u_email");
            Matcher matcher = regex.matcher(email);
            boolean isMatched = matcher.matches();
            if (isMatched) {
                user.setEmail(email);
            } else {
                user.setEmail("");
            }
            user.setUsername(curUser.getString("u_name"));
            user.setPassword(MD5.md5(INITIAL_PASSWORD));
            user.setMobile(curUser.getString("u_phone"));
            user.setNation(curUser.getString("u_nation"));
            user.setMoneycard(curUser.getString("u_id"));
            user.setSex(SEX_MALE.equals(curUser.getString("u_sex")) ? "1" : "2");
            //??????????????????????????????(0-?????????  1-?????????  2-????????????' 3-????????????)
            user.setRoletype(USER_TYPE_MEDICAL_ETHICS);
            userService.insertSelective(user);
            userCode = user.getUsercode();
            //????????????????????????????????????
            userRoleKey.setUsercode(userCode);
        }

        //???????????????????????????????????????
        UserRoleKey repeatData = userRoleService.selectOne(userRoleKey);
        if (repeatData == null) {
            insertUserRoleList.add(userRoleKey);
        }
    }

    private void handleRepeatData(List<MedicalEthicsMsg> medicalEthicsMsgList, List<MedicalEthicsUser> medicalEthicsUserList, List<String> resultList) {
        //????????????????????????
        List<String> userIdsList = medicalEthicsMsgList.stream().map(m -> m.getUserId()).collect(Collectors.toList());
        //?????????????????????
        List<MedicalEthicsMsg> repeatMsgData = mapper.listByUserId(userIdsList);
        //???????????????????????????
        List<MedicalEthicsUser> repeatUserData = medicalEthicsUserService.listByUserId(userIdsList);
        if (repeatMsgData != null && !repeatMsgData.isEmpty()) {
            //???????????????????????????,???????????????????????????
            mapper.batchDelete(repeatMsgData.stream().map(m -> m.getId()).collect(Collectors.toList()));
        }
        if (!medicalEthicsMsgList.isEmpty()) {
            //?????????????????????????????????
            mapper.batchInsert(medicalEthicsMsgList);
        }

        if (repeatUserData != null && !repeatUserData.isEmpty()) {
            //?????????????????????????????????,???????????????????????????
            medicalEthicsUserService.batchDelete(repeatUserData.stream().map(m -> m.getId()).collect(Collectors.toList()));
            //?????????????????????????????????,???????????????????????????
//            medicalEthicsUserService.batchDeleteUserRole(repeatUserData.stream().map(m -> m.getUserId()).collect(Collectors.toList()));
            medicalEthicsUserService.batchDeleteCommonUserRole(repeatUserData.stream().map(m -> m.getUserId()).collect(Collectors.toList()));
        }

        if (!medicalEthicsUserList.isEmpty()) {
            //??????????????????????????????
            medicalEthicsUserService.batchInsert(medicalEthicsUserList);
            //????????????????????????
            List<MedicalEthicsUserRole> userRoleList = new ArrayList<>();
            medicalEthicsUserList.stream().forEach(m -> {
                MedicalEthicsUserRole medicalEthicsUserRole = new MedicalEthicsUserRole();
                medicalEthicsUserRole.setUserId(m.getUserId());
                medicalEthicsUserRole.setRoleId(medicalEthicsUserService.getRoleInfo(new HashMap<String, String>() {{
                    put("roleCode", MEDICAL_ETHICS_ORDINARY_USER);
                }}).getId());
                userRoleList.add(medicalEthicsUserRole);
            });
            if (!userRoleList.isEmpty()) {
                medicalEthicsUserService.batchInsertIntoUserRole(userRoleList);
            }
            resultList.add("??????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private int loadPersonType(MedicalEthicsMsg medicalEthicsMsg) {
        int personType;
        //(??????/?????????/?????????)
        if (medicalEthicsMsg.getBranchId() != NUMBER_ZERO) {
            //?????????????????????,????????????????????????
            personType = medicalEthicsMsg.getBranchPersonType();
        } else if (medicalEthicsMsg.getBranchId() == NUMBER_ZERO && medicalEthicsMsg.getGeneralBranchId() != NUMBER_ZERO) {
            //????????????????????????,?????????????????????,???????????????????????????
            personType = medicalEthicsMsg.getGeneralBranchPersonType();
        } else {
            //?????????????????????,????????????????????????
            personType = medicalEthicsMsg.getPartyCommitteesPersonType();
        }

        return personType;
    }
}
