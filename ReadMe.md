
# Android常用工具类总结

## 目录：
   
      1.ScreenUtils.java -------------------------- 屏幕辅助类 

       
      2.SDCardUtils.java -------------------------- SDCard辅助类
      

      3.L.java ------------------------------------ Logcat日志统一管理类,需要使用到SDCardUtils.java
      

      4.T.java ------------------------------------ Toast统一管理类

    
      5.HttpUtils.java ---------------------------- Http请求工具类


      6.NetUtils.java ----------------------------- 网络相关辅助类

     
      7.DateUtils.java ---------------------------- 时间格式转换类
      
      
      8.DataUtils.java ---------------------------- 数据转换类


      9.ViewPagerIndicator ------------------------ ViewPager指示器总结
	           | 
	           +---------- PagerIndicator.java --------------- 一个显示当前视图和总的视图数量的接口类   
	           |   
               +---------- TabPagerIndicator.java ------------ 选项卡指示器类    
               |
               +---------- IconPagerAdapter.java  ------------ tab选项卡图片适配接口类 
               |
               +---------- IcsLinearLayout.java  ------------- 自定义的tabs选项卡上方分割线视图类  
               |
               +---------- IconPagerIndicator.java ----------- 图标指示器类
               |
               +---------- LinePagerIndicator.java ----------- 线条指示器类  
               |
               +---------- UnderlinePagerIndicator.java ------ 下划线指示器类
               |
               +---------- CirclePagerIndicator.java --------- 圆圈指示器类      
               |
               +---------- TitlePagerIndicator.java ---------- 标题指示器类      









## 用法：
    直接拷贝到当前项目下即可
