package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.ScoreYdyf;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IManualSetTimeService;
import com.welb.organization_check.service.IScoreYdyfService;
import com.welb.organization_check.service.IUserService;
import com.welb.util.ImportExcelUtils;
import com.welb.util.LogUtil;
import com.welb.util.NumberUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luoyaozu
 * @title: ScoreHistoryController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2020/7/21
 */
@RestController
@RequestMapping("/ydyf")
public class ScoreYdyfController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    IScoreYdyfService ydyfService;
    @Resource
    IManualSetTimeService setTimeService;
    @Autowired
    IUserService userService;

    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectScoreYdyfAll(HttpServletRequest req, ScoreYdyf ydyf, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        //pageNum:当前页  pageSize:每页总数
        if (usercode != null) {
            PageHelper.startPage(pageNum, pageSize);
            List<ScoreYdyf> ydyfs;
            try {
                ydyfs = ydyfService.findYdyfList(ydyf);
                PageInfo<ScoreYdyf> pageInfo = new PageInfo<>(ydyfs);
                ydyfs = pageInfo.getList();
                //获取总数据量
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "查询指标成功");
                map.put("data", ydyfs);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询指标失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    @RequestMapping(value = "/upload", produces = "application/json;charset=utf-8")
    public Object upload(HttpServletRequest req, @RequestParam MultipartFile file, String dbtype) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                String message = "";
                if (file.isEmpty()) {
                    message = "空文件";
                } else {
//                    String uploadFileName = file.getOriginalFilename();
                    String filePath = "D:/jw/UploadFile/";//febsProperties.getUploadPath(); // 上传后的路径
                    File getFile = this.fileUpLoad(file, filePath, UUID.randomUUID().toString(), "YdyfTemp");
                    Map<Integer, String> sheetMap = this.getSheelNames(getFile);
                    if (sheetMap.size() > 0) {
                        List<Object[]> obj = ImportExcelUtils.importExcelBySheetIndex(getFile, 0, 0, 0);
                        if (obj.size() > 1) {
                            if (obj.get(0).length >= 2) {
                                List<ScoreYdyf> insertList = new ArrayList<>();
                                List<ScoreYdyf> updateList = new ArrayList<>();
                                List<ScoreYdyf> queryList = new ArrayList<>();
                                boolean isNum = false;
                                ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "",dbtype);
                                ScoreYdyf query = new ScoreYdyf();
                                query.setYear(setTime.getYear());
                                query.setMonth(setTime.getMonth());
                                List<ScoreYdyf> list = ydyfService.findYdyfList(query);
//                                List<String> roleList = new ArrayList<>();
//                                roleList.add("300");
//                                List<User> userList = userService.findUserByRoleCode(roleList);
                                List<User> userList = userService.findUserAll();
                                Double maxSoore = 15.0;
                                boolean isUpdate = true;
                                long count = 0;
                                int hang = 1;
                                for (int i = 1; i < obj.size(); i++) {
                                    hang = i + 1;
                                    String moneyCard = this.importTernaryOperate(obj.get(i), 0);
                                    String strScore = this.importTernaryOperate(obj.get(i), 1);
                                    if (!moneyCard.equals("") && !strScore.equals("")) {
                                        count = userList.stream().filter(s -> s.getMoneycard().equals(moneyCard)).count();
                                        if (count > 0) {
                                            isNum = NumberUtil.isNumber(strScore);
                                            if (isNum) {
                                                Double score = Double.parseDouble(strScore);
                                                if (score >= 0 && score <= maxSoore) {
                                                    ScoreYdyf ydyf = new ScoreYdyf();
                                                    ydyf.setYear(setTime.getYear());
                                                    ydyf.setMonth(setTime.getMonth());
                                                    ydyf.setMoneyCard(moneyCard);
                                                    ydyf.setScore(score);
                                                    if (list.size() == 0) {
                                                        insertList.add(ydyf);
                                                    } else {
                                                        queryList = list.stream().filter(s -> s.getMoneyCard().equals(ydyf.getMoneyCard())).collect(Collectors.toList());
                                                        if (queryList.size() > 0) {
                                                            ydyf.setId(queryList.get(0).getId());
                                                            updateList.add(ydyf);
                                                        } else {
                                                            insertList.add(ydyf);
                                                        }
                                                    }
                                                } else {
                                                    isUpdate = false;
                                                    message = "Excec 第" + hang + "行, 分数必须大于0, 小于" + maxSoore;
                                                    break;
                                                }
                                            } else {
                                                isUpdate = false;
                                                message = "Excec 第" + hang + "行, 分数不是数字";
                                                break;
                                            }
                                        } else {
                                            isUpdate = false;
                                            message = "Excec 第" + hang + "行, 当前发薪号[" + moneyCard + "]不存在.";
                                            break;
                                        }
                                    } else {
                                        isUpdate = false;
                                        message = "Excec 第" + hang + "行, 数据为空.";
                                        break;
                                    }
                                }
                                if (isUpdate) {
                                    if (updateList.size() > 0) {
                                        for (ScoreYdyf item : updateList) {
                                            ydyfService.updateByPrimaryKeySelective(item);
                                        }
                                    }
                                    if (insertList.size() > 0) {
                                        for (ScoreYdyf item : insertList) {
                                            ydyfService.insertSelective(item);
                                        }
                                    }
                                    message = "OK";
                                }
                            } else {
                                message = "Exccel 数据为空.";
                            }
                        } else {
                            message = "Exccel 数据为空.";
                        }
                    } else {
                        message = "Exccel Shell为空.";
                    }
                }
                map.put("msg", message.equals("OK") ? "导入党风廉政数据成功." : message);
                map.put("code", message.equals("OK") ? 0 : 1);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "导入党风廉政数据失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    public String importTernaryOperate(Object[] obj, int nThis) {
        if (obj.length >= nThis + 1) {
            return obj[nThis] != null ? obj[nThis].toString().trim() : "";
        } else {
            return "";
        }
    }

    public File fileUpLoad(@RequestParam MultipartFile file, String filePath, String fileName, String folderName) throws Exception {
        String fileName2 = file.getOriginalFilename();  // 文件名
        String suffixName = fileName2.substring(fileName2.lastIndexOf("."));  // 后缀名

        String guId = UUID.randomUUID().toString();
        if (fileName == "" || fileName == null) {
            fileName = guId;
        }
        String newfileName = fileName + suffixName; // 新文件名
        File dest = new File(filePath + folderName + "/" + newfileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }

    public Map<Integer, String> getSheelNames(File file) throws Exception {
        Map<Integer, String> result = new HashMap<>();
        Workbook wb = null;
        Iterator<Sheet> sheets = null;
        String fileName = file.getName();// 读取上传文件(excel)的名字，含后缀后

        if (fileName.endsWith("xls")) {
            wb = new HSSFWorkbook(new FileInputStream(file));
            sheets = wb.iterator();
        } else if (fileName.endsWith("xlsx")) {
            wb = new XSSFWorkbook(new FileInputStream(file));
            sheets = wb.iterator();
        }
        if (sheets == null) {
            throw new Exception("excel中不含有sheet工作表");
        }
        int i = 0;
        while (sheets.hasNext()) {
            Sheet sheet = sheets.next();
            result.put(i, sheet.getSheetName());
            i++;
        }
        wb.close();
        return result;
    }
}
