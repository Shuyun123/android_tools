#Android常用工具类总结
###长期更新



         
###目录：
   
      1.ScreenUtils.java -------------------------- 屏幕辅助类 
***
       
      2.SDCardUtils.java -------------------------- SDCard辅助类
***
      3.L.java ------------------------------------ Logcat日志统一管理类,需要使用到SDCardUtils.java
***
      4.T.java ------------------------------------ Toast统一管理类
***      
      5.HttpUtils.java ---------------------------- Http请求工具类
***
      6.NetUtils.java ----------------------------- 网络相关辅助类
***
      7.ViewPagerIndicator ------------------------ ViewPager指示器总结
	           | 
	           +---------- PagerIndicator.java ---- 一个显示当前视图和总的视图数量的接口类   
	           |   
               +---------- TabPagerIndicator.java --- 选项卡指示器类    
               |
               +---------- IconPagerAdapter.java  --- tab选项卡图片适配接口 
               |
               +---------- IcsLinearLayout.java  ---- 自定义的tabs选项卡上方分割线视图类                  
    
      该源码来自大神[Jake Wharton](https://github.com/JakeWharton/ViewPagerIndicator/)
      重新覆写,并添加中文注释(英语不好,欢迎指正*_-)








##用法：
    直接拷贝到当前项目下即可