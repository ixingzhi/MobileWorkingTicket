package com.shichuang.mobileworkingticket.common;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface Constants {
    //String MAIN_ENGINE = "http://192.168.2.124:8080/ydgp";
    //String MAIN_ENGINE = "http://192.168.31.64:8081/ydgp";
    //String MAIN_ENGINE = "http://61.160.111.74:8081/ydgp";
    String MAIN_ENGINE = "http://workticket.wxboiler.com:8081/ydgp";

    //String MAIN_ENGINE_PIC = "http://192.168.2.124:8080";
    //String MAIN_ENGINE_PIC = "http://192.168.31.64:8081";
    //String MAIN_ENGINE_PIC = "http://61.160.111.74:8081";
    String MAIN_ENGINE_PIC = "http://workticket.wxboiler.com:8081";

    // 上传文件
    String uploadFile = MAIN_ENGINE + "/upload/uploadFile";
    String appUpdateUrl = MAIN_ENGINE + "/get_appversion";
    String loginUrl = MAIN_ENGINE + "/user/login_sub";
    String bannerUrl = MAIN_ENGINE + "/api/base/getAdvPics";
    String messageCountUrl = MAIN_ENGINE + "/message/getMessageCount";
    String getDirectoriesUrl = MAIN_ENGINE + "/dept/getDirectories";
    String userDetailsUrl = MAIN_ENGINE + "/dept/user_detail";
    String getDeptListUrl = MAIN_ENGINE + "/dept/deptList";
    String getUserInfoUrl = MAIN_ENGINE + "/user/getMyselfInfo";
    String getMessageListUrl = MAIN_ENGINE + "/message/getMessageList";
    String messageDetailsUrl = MAIN_ENGINE + "/message/messageDetail";
    String ticketListUrl = MAIN_ENGINE + "/ticket/ticket_list";
    String getProcessListUrl = MAIN_ENGINE + "/process/getProcessList";
    String ticketDetailsUrl = MAIN_ENGINE + "/ticket/ticket_detail";
    String chooseTeamMemberUrl = MAIN_ENGINE + "/process/chooseTeamMember";
    String distributionTeamMemberUrl = MAIN_ENGINE + "/process/distributionTeamMember";
    String receiveMaterialUrl = MAIN_ENGINE + "/process/receiveMaterial";
    String startWorkUrl = MAIN_ENGINE + "/process/startWork";
    String endWorkUrl = MAIN_ENGINE + "/process/endWork";
    String qualityTestingUrl = MAIN_ENGINE + "/process/qualityTesting";
    String todayStatisticsUrl = MAIN_ENGINE + "/workBench/todayStatistics";
    String historicalWorkInfoUrl = MAIN_ENGINE + "/ticket/historicalWorkInfo";
    String replaceHeadPortraitUrl = MAIN_ENGINE + "/api/base/replaceHeadPortrait";
    String getProductStatisticsUrl = MAIN_ENGINE + "/report/getProductStatistics";
    String getByWorkingProcedureUrl = MAIN_ENGINE + "/report/getByWorkingProcedure";
    String getByRejectsUrl = MAIN_ENGINE + "/report/getByRejects";
    String getPartsByProductUrl = MAIN_ENGINE + "/report/getPartsByProduct";
    String getWorkshopInfoListUrl = MAIN_ENGINE + "/api/base/getWorkshopInfoList";
    String getSparePartsByPartsUrl = MAIN_ENGINE + "/report/getSparePartsByParts";
    String getServicePhoneUrl = MAIN_ENGINE + "/api/base/getForgetPasswordContacts";
    String editPasswordUrl = MAIN_ENGINE + "/user/editpassword";
    String createRemarksUrl = MAIN_ENGINE + "/creatRemarks";

    // 帮助中心
    String helpCenterUrl = MAIN_ENGINE + "/help_center?type=1";
    // 关于我们
    String aboutUsUrl = MAIN_ENGINE + "/help_center?type=2";

}
