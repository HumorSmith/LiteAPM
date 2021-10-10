# 指定cmke版本
cmake_minimum_required(VERSION 3.4.1)
add_library(jvmti_agent
        SHARED
        src/main/cpp/JvmMonitor.cpp) #添加.c源文件
include_directories(src/main/cpp/include) #添加头文件的位置
target_link_libraries(jvmti_agent
        log) #选择要使用的库