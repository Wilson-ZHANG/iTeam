package util;//package jarvis.logintest.util;
//
//import android.text.TextUtils;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.litepal.crud.DataSupport;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import tw.com.flags.demo.db.AdminInfo;
//import tw.com.flags.demo.db.FriendInfo;
//import tw.com.flags.demo.db.ProjectInfo;
//import tw.com.flags.demo.db.TaskInfo;
//
//import static tw.com.flags.demo.constant.Constant.gson;
//
///**
// * Created by jarvis on 2017/2/27.
// */
//
//public class Utility {
//    public static AdminInfo handleAdminInfoResponse(String response){
//        if(!TextUtils.isEmpty(response)){
//            try{
//                Log.d("Test0","dsjch");
//
//                AdminInfo tmp=gson.fromJson(response,AdminInfo.class);
//                return tmp;
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//    public static List<FriendInfo> handleSearchFriendResponse(String response){
//        if(!TextUtils.isEmpty(response)){
//            try{
//                JSONObject jsonObject=new JSONObject(response);
//                JSONArray jsonArray=jsonObject.getJSONArray("Result");
//                List<FriendInfo> friendInfoList=new ArrayList<>();
//                for(int i=0;i<jsonArray.length();i++){
//                    FriendInfo friendInfo;
//                    String str=jsonArray.getJSONObject(i).toString();
//                    friendInfo=gson.fromJson(str,FriendInfo.class);
//                    friendInfoList.add(friendInfo);
//                }
//                return friendInfoList;
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//    public static boolean handleFriendInfoResponse(String response){
//        if(!TextUtils.isEmpty(response)){
//            try{
//                JSONObject jsonObject=new JSONObject(response);
//                JSONArray jsonArray=jsonObject.getJSONArray("Friend");
//                for(int i=0;i<jsonArray.length();i++){
//                    FriendInfo friendInfo;
//                    String str=jsonArray.getJSONObject(i).toString();
//                    friendInfo=gson.fromJson(str,FriendInfo.class);
//                    if(DataSupport.where("friendId = ?", String.valueOf(friendInfo.getFriendId()))
//                            .find(FriendInfo.class).isEmpty()){
//                        friendInfo.save();
//                    }else{
//                        List<FriendInfo> friendInfos=DataSupport.where("friendId = ?", String.valueOf(friendInfo.getFriendId()))
//                                .find(FriendInfo.class);
//                        for(FriendInfo friendInfo1:friendInfos)
//                            friendInfo.update(friendInfo1.getId());
//                    }
//                }
//                return true;
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//    public static boolean handleProjectInfoResponse(String response){
////        Connector.getDatabase();
//        if(!TextUtils.isEmpty(response)){
//            try{
//                Log.d("Test0","project");
//                JSONObject jsonObject=new JSONObject(response);
//                JSONArray jsonArray=jsonObject.getJSONArray("Project");
//                for(int i=0;i<jsonArray.length();i++){
//                    ProjectInfo projectInfo;
//                    String str=jsonArray.getJSONObject(i).toString();
//                    projectInfo=gson.fromJson(str,ProjectInfo.class);
//                    if(DataSupport.where("projectId = ?", String.valueOf(projectInfo.getProjectId()))
//                            .find(ProjectInfo.class).isEmpty()){
//                        projectInfo.save();
//                    }else{
//                        List<ProjectInfo> projectInfoList=DataSupport.where("projectId = ?", String.valueOf(projectInfo.getProjectId()))
//                                .find(ProjectInfo.class);
//                        for(ProjectInfo projectInfo1:projectInfoList)
//                            projectInfo.update(projectInfo1.getId());
//                    }
//                }
//                return true;
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//    public static boolean handleTaskInfoResponse(String response){
////        Connector.getDatabase();
//        if(!TextUtils.isEmpty(response)){
//            try{
//                Log.d("Test0","task");
//                JSONObject jsonObject=new JSONObject(response);
//                JSONArray jsonArray=jsonObject.getJSONArray("Job");
//                for(int i=0;i<jsonArray.length();i++){
//                    TaskInfo taskInfo;
//                    String str=jsonArray.getJSONObject(i).toString();
//                    taskInfo=gson.fromJson(str,TaskInfo.class);
//                    if(DataSupport.where("taskId = ?", String.valueOf(taskInfo.getTaskId()))
//                            .find(TaskInfo.class).isEmpty()){
//                        taskInfo.save();
//                    }else{
//                        List<TaskInfo> taskInfoList=DataSupport.where("taskId = ?", String.valueOf(taskInfo.getTaskId()))
//                                .find(TaskInfo.class);
//                        for(TaskInfo taskInfo1:taskInfoList)
//                            taskInfo.update(taskInfo1.getId());
//                    }
//                }
//                return true;
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//}
