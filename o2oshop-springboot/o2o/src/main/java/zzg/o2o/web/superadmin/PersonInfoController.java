package zzg.o2o.web.superadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zzg.o2o.dto.ConstantForSuperAdmin;
import zzg.o2o.dto.PersonInfoExecution;
import zzg.o2o.entity.PersonInfo;
import zzg.o2o.enums.PersonInfoStateEnum;
import zzg.o2o.service.PersonInfoService;
import zzg.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("superadmin")
public class PersonInfoController {
    @Autowired
    private PersonInfoService personInfoService;

    @RequestMapping(value = "/listpersonInfos", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> listPersonInfos(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        PersonInfoExecution pie = null;
        int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
        int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
        if (pageIndex > 0 && pageSize > 0) {
            try {
                PersonInfo personInfo = new PersonInfo();
                int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
                if (enableStatus > -1) {
                    personInfo.setEnableStatus(enableStatus);
                }
                String name = HttpServletRequestUtil.getString(request, "name");
                if (name != null) {
                    personInfo.setName(URLDecoder.decode(name, "UTF-8"));
                }
                pie = personInfoService.getPersonInfoList(personInfo, pageIndex, pageSize);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
            if (pie.getPersonInfoList() != null) {
                modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, pie.getPersonInfoList());
                modelMap.put(ConstantForSuperAdmin.TOTAL, pie.getCount());
                modelMap.put("success", true);
            } else {
                modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<PersonInfo>());
                modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
                modelMap.put("success", true);
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "空的查询信息");
            return modelMap;
        }
    }


    @RequestMapping(value = "/modifypersonInfo", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyPersonInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        long userId = HttpServletRequestUtil.getLong(request, "userId");
        int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
        if (userId >= 0 && enableStatus >= 0) {
            try {
                PersonInfo pi = new PersonInfo();
                pi.setUserId(userId);
                pi.setEnableStatus(enableStatus);
                PersonInfoExecution ae = personInfoService.modifyPersonInfo(pi);
                if (ae.getState() == PersonInfoStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ae.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入需要修改的帐号信息");
        }
        return modelMap;
    }

}


