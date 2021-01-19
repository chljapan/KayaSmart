package com.smartkaya.api;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cms")
public class FrowardController {
//    private Logger logger = LoggerFactory.getLogger(FrowardController.class);
    //跳转到登录页
//    @RequestMapping("toLogin")
//    public String toLogin(){
//
//        logger.info("cms/login XXXXXX");
//        return "cms/login";
//    }
    //跳转到首页
    @RequestMapping("toIndex")
    public String index(){
        return "cms/index";
    }
//
//    //跳转到工作台
//    @RequestMapping("toDeskManager")
//    public String toDeskManager(){
//        return "system/index/DeskManager";
//    }
//    //跳转到登录日志界面
//    @RequestMapping("toLoginfoManager")
//    public String toLoginfoManager(){
//        return "system/loginfo/loginfoManager";
//    }
//
//    //跳转公告管理
//    @RequestMapping("toNoticeManager")
//    public String toNoticeManager(){
//    return "system/notice/noticeManager";
//}
//
//    //跳转部门管理
//    @RequestMapping("toDeptManager")
//    public String toDeptManager(){
//        return "/system/dept/deptManager";
//    }
//
//    //跳转部门管理left
//    @RequestMapping("toDeptLeft")
//    public String toDeptLeft(){
//        return "/system/dept/deptLeft";
//    }
//
//
//    //跳转部门管理right
//    @RequestMapping("toDeptRight")
//    public String toDeptRight(){
//        return "/system/dept/deptRight";
//    }
//
//
//    //跳转菜单管理
//    @RequestMapping("toMenuManager")
//    public String toMenuManager(){
//        return "/system/menu/menuManager";
//    }
//
//    //跳转菜单管理left
//    @RequestMapping("toMenuLeft")
//    public String toMenuLeft(){
//        return "/system/menu/menuLeft";
//    }
//
//
//    //跳转菜单管理right
//    @RequestMapping("toMenuRight")
//    public String toMenuRight(){
//        return "/system/menu/menuRight";
//    }
//
//    //跳转权限管理
//    @RequestMapping("toPermissionManager")
//    public String toPermissionManager(){
//        return "/system/permission/permissionManager";
//    }
//
//    //跳转权限管理left
//    @RequestMapping("toPermissionLeft")
//    public String toPermissionLeft(){
//        return "/system/permission/permissionLeft";
//    }
//
//
//    //跳转权限管理right
//    @RequestMapping("toPermissionRight")
//    public String toPermissionRight(){
//        return "/system/permission/permissionRight";
//    }
//
//    @RequestMapping("toRoleManager")
//    public String toRoleManager(){
//        return "/system/Role/RoleManager";
//    }

}
